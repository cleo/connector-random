## Commands &mdash; the Connector Client ##

### Overview ###

The [Connector Schema](SCHEMA.md) uses the `@Client` annotation to bind the
connector implementation to the schema descriptor.

```java
import com.cleo.connector.api.ConnectorConfig;
import com.cleo.connector.api.annotations.Client;
import com.cleo.connector.api.annotations.Connector;
import com.cleo.connector.api.annotations.ExcludeType;

@Connector(scheme = "random", description = "Random Content Streams",
           excludeType = { @ExcludeType(type = ExcludeType.SentReceivedBoxes),
                           @ExcludeType(type = ExcludeType.Exchange) })
@Client(RandomConnectorClient.class)
public class RandomConnectorSchema extends ConnectorConfig {
```

The Connector Client class requires no further annoation at the class level,
but extends `ConnectorClient`:

```java
import com.cleo.connector.api.ConnectorClient;

public class RandomConnectorClient extends ConnectorClient {
    private RandomConnectorConfig config;

    public RandomConnectorClient(RandomConnectorSchema schema) {
        this.config = new RandomConnectorConfig(this, schema);
    }
```

The public constructor takes an instance of the schema class as an argument.
In this sample project, the schema is wrapped with the connector client
instance in a [configuration](CONFIG.md) wrapper for convenience, but this
technique is not mandated by the interface.

### Commands ###

The fundamental metaphor of the connector shell is the _command_, each of which
represents an entry point into the connector implementation.  Commands can be
directly represented in Harmony/VLTrader command actions as individual command
lines, although this is not the only way they can be invoked.

For those familiar
with Java Servlets, the command metaphor is very similar to the servlet interface
with separate handler methods for each HTTP verb, e.g. `doGet`, `doPost`.

To indicate than a client class method supports a command, use the `@Command`
annotation.  The Random sample project implements commands for `PUT`, `GET`,
and [`ATTR`](ATTRIBUTES.md).  Here is a fragment from the `PUT` implementation.

```java
import static com.cleo.connector.api.command.ConnectorCommandName.PUT;
import static com.cleo.connector.api.command.ConnectorCommandOption.Delete;
import static com.cleo.connector.api.command.ConnectorCommandOption.Unique;

import java.io.IOException;

import com.cleo.connector.api.ConnectorException;
import com.cleo.connector.api.annotations.Command;
import com.cleo.connector.api.command.ConnectorCommandResult;
import com.cleo.connector.api.command.PutCommand;

//...

    @Command(name = PUT, options = { Delete, Unique })
    public ConnectorCommandResult put(PutCommand put) throws ConnectorException, IOException {
```

The `PutCommand` object contains all the information that might be encoded on
the action command line.  Here is a snippet from the Harmony/VLTrader documentation
on the `PUT` command:

> **PUT**
> 
> Send one or more files to the host.
>
> `PUT –DEL -APE "source" "destination" name=value,...`
>
> `–DEL`
> 
> If the PUT command is successful, delete local file(s).
> 
> `-APE`
> 
> Append copied file to existing destination file.
> 
> `"source"`
> 
> Local source path
> 
> * Path can be to a filename or to a directory
> * You can use * and ?, or a regular expression when you specify a filename. See Using Wildcards and Regular Expressions for additional information.
> * If you specify a relative path, the command uses the default outbox.
> * You can use macro variables. See Using Macro Variables (Source File context) for a list of the applicable macros.
> * If the path contains a space, dash (-), comma (,), or equal sign (=), it must be enclosed with double quotes ("...").
> 
> `"destination"`
> 
> Remote destination path.
> 
> * You can use macro variables. See Using Macro Variables (Source File context) for a list of the applicable macros.
> * If the path contains a space, dash (-), comma (,), or equal sign (=), it must be enclosed with double quotes ("...").

The `PutCommand` object represents this information as _options_, _source_, _destination_,
and _parameters_.

#### Options ####

Only options indicated in the `@Command` annotation as `options` will be supported and may
appear in the `PutCommand` object.  A static helper method in `ConnectorCommandUtil` is
provided for the client to check for the presence of an option (including all necessary
`null` checks):

```java
import static com.cleo.connector.api.command.ConnectorCommandOption.Unique;

import com.cleo.connector.api.command.ConnectorCommandUtil;

//...
        if (ConnectorCommandUtil.isOptionOn(put.getOptions(), Unique)) {
```

The following options are defined for use in Connector Shell implementations:

Option        | Syntax | Description
--------------|--------|------------
MultipleFiles | `-MUL` | Multiple files packaged as one message
Append        | `-APE` | Append to existing content
Unique        | `-UNI` | Ensure a unique name for destination
Directory     | `-DIR` | Use a directory listing
Delete        | `-DEL` | Delete source file after `GET` or `PUT`
Recurse       | `-REC` | Recurse through files


#### `IConnectorOutgoing` &mdash; `PUT` Source(s) ####

For a `PUT` command, the source (or sources if the `MultipleFiles` option is enabled
in the `@Command` annotation) is/are provided by the framework, and it is up to the
connector implementation to handle the destination.

```java
import static com.cleo.connector.api.command.ConnectorCommandName.PUT;
import static com.cleo.connector.api.command.ConnectorCommandOption.Delete;
import static com.cleo.connector.api.command.ConnectorCommandOption.Unique;

import java.io.IOException;

import com.cleo.connector.api.ConnectorException;
import com.cleo.connector.api.annotations.Command;
import com.cleo.connector.api.command.ConnectorCommandResult;
import com.cleo.connector.api.command.PutCommand;
import com.cleo.connector.api.interfaces.IConnectorOutgoing;

//...

    @Command(name = PUT, options = { Delete, Unique })
    public ConnectorCommandResult put(PutCommand put) throws ConnectorException, IOException {
        IConnectorOutgoing source = put.getSource();
        //...
        transfer(source.getStream(), ByteStreams.nullOutputStream(), false);
        //...
```

`getSource()` returns the `IConnectorOutoing` object, from which an `InputStream` can
be obtained using `getStream()`.  Once the appropriate destination `OutputStream`
has been created (`ByteStreams.nullOutputStream()` in the sample above is Guava's
version of `/dev/null`), invoke `transfer(source, destination, false)` to copy the
source to the destination.  `transfer` is a method inherited from the `ConnectorClient`
superclass and implements the necssary stream wrappers from the framework.  It also
closes the source and destination streams.

> **Note:** the third argument to `transfer` is `false` for `PUT`

#### `IConnectorIncoming` &mdash; `GET` Destination ####

For `GET`, the framework provides the destination and the connector implementation
handles the source.  The pattern is very similar to `PUT`, but in reverse.

```java
import static com.cleo.connector.api.command.ConnectorCommandName.GET;

import java.io.IOException;

import com.cleo.connector.api.ConnectorException;
import com.cleo.connector.api.annotations.Command;
import com.cleo.connector.api.command.ConnectorCommandResult;
import com.cleo.connector.api.command.GetCommand;
import com.cleo.connector.api.interfaces.IConnectorIncoming;

//...

    @Command(name = GET)
    public ConnectorCommandResult get(GetCommand get) throws ConnectorException, IOException {
        IConnectorIncoming destination = get.getDestination();
        //...
        transfer(new RandomInputStream(config.getSeed(), parseLength(config.getLength())),
                 destination.getStream(), true);
        //...
    }
```

`getDestination()` returns the `IConnectorIncoming` object, from which an `OutputStream`
can be obtained using `getStream()`.  Once the appropriate source `InputStream` has
been created (a `new RandomInputStream(...)` in the sample above), invoke
`transfer(source, destination, true)`.

> **Note:** the third argument to `transfer` is `true` for `GET`


#### `Entry` &mdash; `PUT` Destination or `GET` Source ####

While the `PUT` source or `GET` destination is handled by the framework, the
job of the connector implementation is to provide the opposite end of the transfer.
Each connector is different, but the framework provides an abstraction over the
file system model called an `Entry`, from which the connector can calculate and
provide a stream, in conjunction with the configured properties.

```java
import static com.cleo.connector.api.command.ConnectorCommandName.PUT;
import static com.cleo.connector.api.command.ConnectorCommandOption.Delete;
import static com.cleo.connector.api.command.ConnectorCommandOption.Unique;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import com.cleo.connector.api.ConnectorException;
import com.cleo.connector.api.annotations.Command;
import com.cleo.connector.api.command.ConnectorCommandResult;
import com.cleo.connector.api.command.PutCommand;
import com.cleo.connector.api.directory.Entry;

//...
    private boolean exists(String path) {...}
    private OutputStream getOutputStream(String path) {...}
    
    @Command(name = PUT, options = { Unique, Delete })
    public ConnectorCommandResult put(PutCommand put) throws ConnectorException, IOException {
        String destination = put.getDestination().getPath();
        //...
        if (ConnectorCommandUtil.isOptionOn(put.getOptions(), Unique) && exists(destination)) {
            String fullPath = FilenameUtils.getFullPath(destination);
            String base = FilenameUtils.getBaseName(destination);
            String ext = FilenameUtils.getExtension(destination)
                                      .replaceFirst("^(?=[^\\.])",".");
                                      // if non-empty and doesn't start with ., prefix with .
            do {
                destination = fullPath + base + "." + Long.toString(new Random().nextInt(Integer.MAX_VALUE)) + ext;
            } while (exists(destination));
        }
        //...
        transfer(source.getStream(), getOutputStream(destination), false);

```

`PutCommand.getDestination()`  and `GetCommand.getSource()` both return an `Entry` object,
from which `getPath()` returns the `String` representing the connected resource according to
the syntax conventions of the connector implementation.

This sample postulates private `exists()` and `getOutputStream()` methods and shows how they
can be used with typical filename.extension conventions to implement `PUT` with the `Unique`
option.

Note that the connector API shell JAR depends on Apache Commons IO (`org.apache.commons.io` package) so
an explicit Maven dependency on Commons IO is not technically needed in the POM.

#### Parameters ####

Any additional parameters included on the command line are included in a `Map<String,Object>`
accessible through `getParameters()`.  In a typical action command-line case, the command
syntax follows this example.

```
PUT –DEL -APE "source" "destination" name=value,...
```

The `name=value` pairs will appear in the `getParameters()` map as `String` values.  But the
values may be any type, and the special `PUT+GET` command uses the parameters map as a
mechanism to pass information from the `PUT` phase to the `GET` phase.  The following snippet
from the built-in `File` connector illustrates this pattern.

```java
    @Command(name=PUT,options={Unique, Append, Delete})
    @Command(name=PUT_AND_GET,options={Unique, Append, Delete}) // where Delete applies to the PUT and Unique and Append apply to the GET
    public ConnectorCommandResult put(PutCommand put)
        throws ConnectorException, IOException
    {
        File file;
        // ... determine the destination File file and do the PUT
        if (put.getCommandName() == PUT_AND_GET) {
            put.getParameters().put("PUT.filepath", file.toPath());
        }
        return new ConnectorCommandResult(ConnectorCommandResult.Status.Success);
    }

    @Command(name=GET,options={Directory,Delete,Unique,Recurse})
    public ConnectorCommandResult get(GetCommand get)
        throws ConnectorException, IOException
    {
        Path path;
        if (get.getCommandName() == PUT_AND_GET) {
             path = (Path)get.getParameters().get("PUT.filepath");
        } else { 
             // ... determine the destination file Path for a standalone GET
        }
        //.. do the GET phase
```

#### Returning Results ####

The method signature for connector commands is:

```java
import com.cleo.connector.api.ConnectorException;
import com.cleo.connector.api.command.ConnectorCommandResult;

public ConnectorCommandResult method(xxxCommand command)
        throws ConnectorException, IOException;
```

The expectation is that _either_ a `ConnectorCommandResult` will be returned
_or_ a `ConnectorException` will be thrown, depending on the cirumstances.

In case of a **successful** operation, return a result with `Success` status.

```java
return new ConnectorCommandResult(ConnectorCommandResult.Status.Success);
```

In case of a failed operation in the specific case where the target of the
command is **not found** or not accessible, throw a `ConnectorException` with
the `fileNonExistentOrNoAccess` category.

```java
throw new ConnectorException(String.format("'%s' does not exist or is not accessible", target),
              ConnectorException.Category.fileNonExistentOrNoAccess);
```

In case of a typical **failed** operation, return a result with `Error` status
and an explanatory message.

```java
return new ConnectorCommandResult(Status.Error, "Error message.");
```

In case of a malformed or otherwise **unprocessable** operation, throw a general
`ConnectorException` with an explanatory message:

```java
throw new ConnectorException("Error message.");
```

In general, if the command operation is understood and valid, but fails during execution
for some reason, return a `ConnectorCommandResult` with `Error` status.  If the operation
can not be even initiated due to some flaw in the request, throw a `ConnectorException`.

### Command Catalog ###

The following commands are supported for Protocol Shell `@Client` class implementations:

Command Name | Description | Argument | Returns
-------------|-------------|----------|--------
CONNECT | CONNECT to a host server | OtherCommand | ConnectorCommandResult
PUT | PUT content to the host server | PutCommand | ConnectorCommandResult
GET | GET content from the host server | GetCommand | ConnectorCommandResult
PUT\_AND\_GET | PUT content to then GET content back from the host server | PutCommand | ConnectorCommandResult
DELETE | DELETE content from the host server | OtherCommand | ConnectorCommandResult
RENAME | RENAME content on the host server | OtherCommand | ConnectorCommandResult
DIR | DIRectory list content on the host server | DirCommand | ConnectorCommandResult
CD | Change current Directory on the host server | OtherCommand | ConnectorCommandResult
PWD | Print Working Directory on the host server | OtherCommand | ConnectorCommandResult
MKDIR | MaKe a DIRectory on the host server | OtherCommand | ConnectorCommandResult
ATTR | Get/Set ATTRibutes of content on the host server | String | BasicFileAttributeView
RMDIR | ReMove a DIRectory on the host server | OtherCommand | ConnectorCommandResult
DISCONNECT | DISCONNECT from the host server | OtherCommand | ConnectorCommandResult
QUOTE | Custom host command | OtherCommand | ConnectorCommandResult
