## Properties &mdash; the Connector Schema ##

The properties for a connector are defined in a schema class, from which the configuration
form is automatically generated.  Although there is no concept of grouping in the schema,
in the UI the properties are organized into tabs, in a way that is consistent with the
built-in connector technologies.  The tabs are:

* **General** contains general properties that typically apply to every connector
technology or protocol.
* **_Connector_** is named for the connector technology (e.g. SSH FTP or HTTPS, or for
this sample project, RANDOM) and contains typical settings specific to the connector.
A group of advanced settings specific to the connector may also be included on the
**Advanced** tab.
* **Advanced** contains advanced or less commonly used settings
* **Info** only appears for Connector Shell connectors (not built-in connectors)
and provides a space for Connector Shell implementations to provide some help or
explanatory text.
* **Notes** provides a free text area for the administrator to enter some notes
of his or her own.

The Connector Shell framework provides some built-in capabilities and supporting
properties by default, without any explicit configuration or coding in the connector
implementation.  Some of these properties and associated capabilities may be excluded
in the connector schema where they are not appropriate.  For example, the Random
connector in this project excludes archiving (sentbox and receivedbox) as the content
streams are always fixed by length and seed.

The table below summarizes these properties by comparing the properties for a
Connector Shell implementation with those for the built-in SSH FTP connector.
Note that:

* there are a few properties that are standard for Connector Shell implementations
that are not supported for SSH FTP (_Log Transfers For Put And Get_ and the _Info_ tab).
* built-in connectors like SSH FTP have a mix of standard (on the **SSH FTP** tab) and
advanced (on the **Advanced** tab in the **SSH FTP** group) properties.  For Connector
Shell implementations, all schema-defined properties appear on the **_Connector_** tab.


Tab      | Property                                         | Description | Connector?
---------|--------------------------------------------------|-------------|-----------
General  | Server Address                                   | hostname or IP address of the remote server
         | Port                                             | port of the remote server
         | Connection Type                                  | hold over from the dial-up days
         | Forward Proxy                                    | if/how to proxy outbound connections
         | Inbox                                            | where the incoming files go | &#x2714;
         | Outbox                                           | where the outgoing files are staged | &#x2714;
         | Sentbox                                          | where the outgoing archive is kept | &#x2714; _unless SentReceivedBoxes excluded_
         | Receivedbox                                      | where the incoming archive is kept | &#x2714; _unless SentReceivedBoxes excluded_
SSH FTP  | Verify Host Key                                  | enable known hosts checking | _from schema_
         | Host Key                                         | the host key to match | _from schema_
         | Set Key                                          | retrieve host key from remote server | _from schema_
Advanced | _advanced properties are organized into groups_  | _Group_
         | Add Mailbox Alias Directory To Inbox             | Common
         | Add Mailbox Alias Directory To Outbox            | Common
         | Add Mailbox Alias Directory To Receivedbox       | Common
         | Add Mailbox Alias Directory To Sentbox           | Common
         | Allow Actions To Run Concurrently                | Common | &#x2714;
         | Block Size                                       | SSH FTP
         | Buffer Requests                                  | SSH FTP
         | Command Retries                                  | Common
         | Connection Timeout (seconds)                     | Common
         | Create File Times                                | SSH FTP
         | Delete Zero Length Files                         | SSH FTP
         | Do Not Send Zero Length Files                    | SSH FTP
         | Email On Check Conditions Met                    | Email/Execute | &#x2714;
         | Email On Check Conditions Not Met                | Email/Execute | &#x2714;
         | Email On Fail                                    | Email/Execute | &#x2714;
         | Email On Flag                                    | Email/Execute | &#x2714;
         | Email On Repetitive Action Failures              | Email/Execute | &#x2714;
         | Email On Successful Copy                         | Email/Execute | &#x2714;
         | Email On Successful Receive                      | Email/Execute | &#x2714;
         | Email On Successful Send                         | Email/Execute | &#x2714;
         | Execute On Check Conditions Met                  | Email/Execute | &#x2714;
         | Execute On Check Conditions Not Met              | Email/Execute | &#x2714;
         | Execute On Fail                                  | Email/Execute | &#x2714;
         | Execute On Repetitive Action Failures            | Email/Execute | &#x2714;
         | Execute On Successful Copy                       | Email/Execute | &#x2714;
         | Execute On Successful Receive                    | Email/Execute | &#x2714;
         | Execute On Successful Send                       | Email/Execute | &#x2714;
         | Fixed Record EOL Characters                      | Common | &#x2714; _unless Exchange excluded_
         | Fixed Record Incoming Delete EOL                 | Common | &#x2714; _unless Exchange excluded_
         | Fixed Record Incoming Insert EOL                 | Common | &#x2714; _unless Exchange excluded_
         | Fixed Record Length                              | Common | &#x2714;
         | Fixed Record Outgoing Insert EOL                 | Common | &#x2714; _unless Exchange excluded_
         | Get Number Of Files Limit                        | SSH FTP
         | High Priority                                    | Common | &#x2714;
         | Ignore Directory Listing Attributes              | SSH FTP
         | Include Failure In Subject Of Email              | Email/Execute | &#x2714;
         | Interim Retrieve                                 | SSH FTP
         | Key Exchange Data Limit (mbytes)                 | SSH FTP
         | Key Exchange Time Limit (minutes)                | SSH FTP
         | LCOPY Archive                                    | LCOPY | &#x2714;
         | Log Individual LCOPY Results To Transfer Logging | LCOPY | &#x2714; _unless Exchange excluded_
         | Log Transfers For Put And Get _(not SSH FTP)_    | Connector | &#x2714; _unless Exchange excluded_
         | Macro Date Format                                | Common | &#x2714;
         | Macro Time Format                                | Common | &#x2714;
         | Maximum Incoming Transfer Rate (kbytes/s)        | Common
         | Maximum Outgoing Transfer Rate (kbytes/s)        | Common
         | Next File On Fail                                | SSH FTP
         | Only Retrieve First Available File               | SSH FTP
         | Only Retrieve Last Available File                | SSH FTP
         | Outbox Sort                                      | Common | &#x2714;
         | Outgoing Insert EOL Between Interchanges         | Common | &#x2714; _unless Exchange excluded_
         | PGP Compression Algorithm                        | Packaging
         | PGP Encryption Algorithm                         | Packaging
         | PGP Hash Algorithm                               | Packaging
         | PGP Integrity Check                              | Packaging
         | PGP Signature Verification                       | Packaging
         | PGP V3 Signature                                 | Packaging
         | Post Get Command                                 | SSH FTP
         | Post Put Command                                 | SSH FTP
         | Pre Get Command                                  | SSH FTP
         | Pre Put Command                                  | SSH FTP
         | Pre Put Command For First File Only              | SSH FTP
         | Preferred Cipher Algorithm                       | SSH FTP
         | Preferred Compression Algorithm                  | SSH FTP
         | Preferred Key Exchange Algorithm                 | SSH FTP
         | Preferred MAC Algorithm                          | SSH FTP
         | Preferred Public Key Algorithm                   | SSH FTP
         | REST Enabled                                     | SSH FTP
         | Resume Failed Transfers                          | SSH FTP
         | Retrieve Directory Sort                          | SSH FTP
         | Retrieve Last Failed File First                  | SSH FTP
         | Retry Delay (seconds)                            | Common
         | Server Side Path Name                            | SSH FTP
         | Terminate On Fail                                | Common | &#x2714;
         | Unzip Use Path                                   | LCOPY | &#x2714;
         | Wait For Execute On                              | Email/Execute | &#x2714;
         | Window Size                                      | SSH FTP
         | XML Encryption Algorithm                         | Packaging
         | Zip Comment                                      | LCOPY | &#x2714;
         | Zip Compression Level                            | LCOPY | &#x2714;
         | Zip Subdirectories Into Individual Zip Files     | LCOPY | &#x2714;
Info     | _(not for SSH FTP)_                              | connector "README"| &#x2714;
Notes    |                                                  | a place to keep text notes| &#x2714;


The following code fragment illustrates a connector schema class:

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

The `@Connector` annotation defines the scheme name and description.  The optional
`excludeType` suppresses some of the built-in properties as outlined in the table
above.  The `@Client` annotation links the schema class to the actual implementation
of the connector.

Each connector property is defined with the `@Property` annotation and is of
type `IConnectorProperty<T>` where `T` is a primitive wrapper type (e.g. 
`Integer`, `Long`, `Boolean`, ...) or `String`.  To build out the
schema details for the `IConnectorProperty` use the `PropertyBuilder`:

```java
import com.cleo.connector.api.annotations.Property;
import com.cleo.connector.api.interfaces.IConnectorProperty;
import com.cleo.connector.api.property.PropertyBuilder;

//...

@Property
final IConnectorProperty<String> length = new PropertyBuilder<>("Length", "1k")
        .setDescription("The number of random bytes.")
        .addPossibleRegexes("\\d+(?i:[kmgt]b?)?")
        .setRequired(true)
        .build();
```

The `PropertyBuilder` constructor provides the property name and (type-specific)
default value.  The property name must not contain spaces and must be
formatted in [UpperCamelCase](https://en.wikipedia.org/wiki/PascalCase).  The
legends in the UI will add spaces between the words, e.g. `UpperCamelCase` will
be displayed as `Upper Camel Case`.  The following aspects of the schema property
may be specified before the final `.build()`:

setter | description | default
-------|-------------|--------
`setDescription(String)` | define property "tooltip" | no tooltip
`setRequired(true)` | makes property mandatory | not required
`setAllowedInSetCommand(false)` | allows override in `SET` command or URI `?property=value` | override permitted
`setType(IConnectorProperty.Type.PathType)` | `String` property is a file path | no special handling
`setPossibleValues(values...)` | define drop-down pick list | free data entry (no dropdown)
`addPossibleValues(values...)` | extend drop-down pick list | 
`setPossibleRegexes(String...)` | define validation patterns | no validation
`addPossibleRegexes(String...)` | extend validation patterns |
`setPossibleRanges(new PropertyRange<>(min,max),...)` | define validation range | no validation
`addPossibleRanges(new PropertyRange<>(min,max),...)` | extend validation range |
`addAttribute(Attribute.Password)` | store value encrypted | value stored unencrypted
`addAttribute(Attribute.Certificate)` | TODO | no special handling
`setExtendedClass(class)` | TODO (see below) | special handling

In addition to connector-specific properties, well-known `CommonProperty` selections
can be added to the schema and trigger built-in behaviors.  For example, the Random
connector enables the debug log:

```
import com.cleo.connector.api.interfaces.IConnectorProperty;
import com.cleo.connector.api.property.CommonProperties;
import com.cleo.connector.api.property.CommonProperty;

//...

@Property
final IConnectorProperty<Boolean> enableDebug = CommonProperties.of(CommonProperty.EnableDebug);
```

This allows calls to `logger.debug(String message)` to be controlled (`logger` is
inherited from `ConnectorClient`, the superclass of `RandomConnectorClient`.

Property | Description
---------|------------
Address | The host server connection address.
Port | The host server connection port.
ConnectionTimeout | The host server connection timeout; the number of seconds allowed for each read operation.
ResourcePath | The host server connection resource path.
UserName | The host server connection account user name.
UserPassword | The host server connection account user password.
CommandRetries | The number of times the command should be retried when an error or exception occurs.
CommandRetryDelay | The number of seconds to wait between retries.
DoNotSendZeroLengthFiles | For PUT, a switch that indicates to not send a file if it is zero-length.
DeleteReceivedZeroLengthFiles | For GET, a switch that indicates to remove a received file that is zero-length.
NextFileOnFail | For the set of files to be PUT or GET, a switch that indicates to continue to the next file even when a PUT/GET file fails.
GetNumberOfFilesLimit | For GET, limit the number of files retrieved from a directory listing.
RetrieveDirectorySort | For PUT, the sorting options for the list of outbound files.
StoreAndForward | A switch that indicates whether or not to store the contents locally when the connector endpoint is down and subsequently forward to the connector endpoint when it is back up.  Note: For this feature to be work, the Receivedbox must be configured in the host using this Connector and Administration>Other>Disable Date/Time Portion Of Filenames In Sent/Received Box must be unchecked.
StoreAndForwardRetryDelay | The number of seconds to wait between 'Store And Forward' retries.
EnableDebug | A switch that indicates whether to perform debug logging.
SystemSchemeName | The URI scheme name used as a shortcut to this host.

The remaining obligation of the connector schema is to load the text for the **Info** tab:

```
import java.io.IOException;

import com.cleo.connector.api.annotations.Info;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

//...

@Info
protected static String info() throws IOException {
    return Resources.toString(RandomConnectorSchema.class.getResource("info.txt"), Charsets.UTF_8);
}
```

Note that the connector API shell JAR depends on Google Guava (`com.google.common` packages) so
an explicit Maven dependency on Guava is not technically needed in the POM.

