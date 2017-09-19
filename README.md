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
git clone git@github.com:jthielens/connector-random.git
mvn clean package
cp target/random-5.4.1.0-SNAPSHOT-distribution.zip $CLEOHOME
cd $CLEOHOME
unzip -o random-5.4.1.0-SNAPSHOT-distribution.zip
./Harmonyd stop
./Harmonyd start
```

When Harmony/VLTrader restarts, you will see a new `Template` in the host tree
under `Connections` > `Generic` > `Generic RANDOM`.  Select `Clone and Activate`
and a new `RANDOM` connection (host) will appear on the `Active` tab.

Change the default `<receive>` action to read `GET test.bin` (instead of `GET *`)
and run the action.  You will find a 1k (1024 byte) file of nulls in `inbox/test.bin`.