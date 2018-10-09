# README #

The Cleo Harmony/VLTrader (aka VersaLex) Connector Shell API allows new connector
technologies to be plugged in to be configured and used by the administrator.

This sample project provides a basic example of a connector.  It provides a
kind of _virtual_ file whose contents are a random, but predictable based on
a provided seed, sequence of bytes.  When used in a `GET` operation (which
produces a readable `InputStream`), the random bytes are retrieved.  When used in a
`PUT` operation (which produces a writable `OutputStream`), the byte sequence
is compared with the expected values based on an `InputStream` with the same
seed, throwing an Exception in case of a mismatch in content or length.

As a special case, a seed of `0` produces a predictable sequence of null bytes
`'\0'`, not unlike `/dev/zero`.

## TL;DR ##

The POM for this project creates a ZIP archive intended to be expanded from
the Harmony/VLTrader installation directory (`$CLEOHOME` below).

```
git clone git@github.com:cleo/connector-random.git
mvn clean package
cp target/random-5.5.0.0-distribution.zip $CLEOHOME
cd $CLEOHOME
unzip -o random-5.5.0.0-distribution.zip
./Harmonyd stop
./Harmonyd start
```

When Harmony/VLTrader restarts, you will see a new `Template` in the host tree
under `Connections` > `Generic` > `Generic RANDOM`.  Select `Clone and Activate`
and a new `RANDOM` connection (host) will appear on the `Active` tab.

Change the default `<receive>` action to read `GET test.bin` (instead of `GET *`)
and run the action.  You will find a 1k (1024 byte) file of nulls in `inbox/test.bin`.

## Structure of a Connector Shell Project ##

A Connector Shell project comprises n essential artifacts:

The connector schema class defines the properties required to configure connection
instances using the connector.  It is also used to automatically render a configuration
UI.  See [About the Connector Schema](SCHEMA.md).

The connector client class implements the methods supported by the connector.
In the connector shell, these methods are abstracted as _commands_ and represent
canonical file-based operations like `GET`, `PUT`, and `DIR` that can be
executed directly from Command actions.  See [About the Connector Client](CLIENT.md).

File attributes are handled using a special `ATTR` command and associated
class implementing the `DosFileAttributes` and `DosFileAttributeView` interfaces
from the Java standard `com.nio.file.attribute` package.
See [About Connector Attributes](ATTRIBUTES.md).

A specific mechanism is used to read connection configuration values according
to the schema.  The sample project uses a simple read-only bean to wrap this
mechanism.  See [About Connector Configuration](CONFIG.md).

The sample Random connector uses a Maven project to import the dependency on
the Connector Shell API and to build and package the connector for deployment.
See [About Maven and the Connector Shell](MAVEN.md).
