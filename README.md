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
