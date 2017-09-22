## File Attributres &mdash; the ATTR Command ##

### Overview ###

A connector implementation supports file attributes through the `ATTR`
command, which is special in the sense that it does not use a `xxxCommand`
object as part of the method interface, but instead leverages the
`AttributeView` abstraction built into the `java.nio.file.attribute`
package.

The recommended approach is to isolate attribute handling into its own
class implementing both `DosFileAttributes` (extends `BasicFileAttributes`)
and `DosFileAttributeView` (extends `BasicFileAttributeView`).  The
client class instantiates an instance of the attribute class, passing
whatever information or context is needed, and returns it as the result
of the `ATTR` command.  The attribute class then handles both attribute
setting and getting operations.

```java
import static com.cleo.connector.api.command.ConnectorCommandName.ATTR;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;

import com.cleo.connector.api.ConnectorException;
import com.cleo.connector.api.annotations.Command;

//...

    @Command(name = ATTR)
    public BasicFileAttributeView getAttributes(String path) throws ConnectorException, IOException {
        return new RandomFileAttributes(config);
    }
```

In this sample only the configuration is needed to handle attributes, but in
most real world connectors some additional processing will be required to
convert the `path` into a usable reference to a connected resource.

The attribute class implementation then follows this pattern.

```java
import java.io.IOException;
import java.nio.file.attribute.DosFileAttributeView;
import java.nio.file.attribute.DosFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RandomFileAttributes implements DosFileAttributes, DosFileAttributeView {
    RandomConnectorConfig config;

    public RandomFileAttributes(RandomConnectorConfig config) {
        this.config = config;
    }

    @Override
    public FileTime lastModifiedTime() {
        return FileTime.from(new Date().getTime(), TimeUnit.MILLISECONDS);
    }

    //...
```

In case of unsupported attributes, throw an `UnsupportedOperationException`.

```java
    @Override
    public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
        if (lastModifiedTime != null || lastAccessTime != null || createTime != null) {
            throw new UnsupportedOperationException("setTimes() not supported on Random streams");
        }
    }
```

### Property Catalog ###

This information is summarized from the Java Platform documentation for
[BasicFileAttributes](http://docs.oracle.com/javase/8/docs/api/java/nio/file/attribute/BasicFileAttributes.html),
[BasicFileAttributeView](http://docs.oracle.com/javase/8/docs/api/java/nio/file/attribute/BasicFileAttributeView.html)
and [DosFileAttributeView](http://docs.oracle.com/javase/8/docs/api/java/nio/file/attribute/DosFileAttributeView.html).


View                   | Attribute        | Type | Setter
-----------------------|------------------|------|-------
BasicFileAttributes    | lastModifiedTime | FileTime | setFileTimes
&nbsp;                 | lastAccessTime   | FileTime | setFileTimes
&nbsp;                 | creationTime     | FileTime | setFileTimes
&nbsp;                 | size             | Long     |
&nbsp;                 | isRegularFile    | Boolean  |
&nbsp;                 | isDirectory      | Boolean  |
&nbsp;                 | isSymbolicLink   | Boolean  |
&nbsp;                 | isOther          | Boolean  |
&nbsp;                 | fileKey          | Object   |
BasicFileAttributeView | name             | String   |
&nbsp;                 | readAttributes   | BasicFileAttributes |
DosFileAttributeView   | isReadOnly       | Boolean  | setReadOnly
&nbsp;                 | isHidden         | Boolean  | setHidden
&nbsp;                 | isSystem         | Boolean  | setSystem
&nbsp;                 | isArchive        | Boolean  | setArchive