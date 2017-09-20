package com.cleo.labs.connector.random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Date;

import org.junit.Ignore;
import org.junit.Test;

import com.google.common.io.ByteStreams;

public class TestRandomConnectorClient {

    public static final long KB = 1024L;
    public static final long MB = 1024L*KB;
    public static final long GB = 1024L*MB;
    public static final long TB = 1024L*GB;

    @Test
    public final void test500M() {
        long seed = new Date().getTime();
        long length = RandomConnectorClient.parseLength("500K");
        try (RandomInputStream in = new RandomInputStream(seed, length);
             RandomOutputStream out = new RandomOutputStream(seed, length)) {
            assertEquals(500*KB, ByteStreams.copy(in, out));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public final void test500Mzero() {
        long seed = 0;
        long length = RandomConnectorClient.parseLength("500M");
        try (RandomInputStream in = new RandomInputStream(seed, length);
             RandomOutputStream out = new RandomOutputStream(seed, length)) {
            assertEquals(500*MB, ByteStreams.copy(in, out));
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public final void testFailOnExtraWrite() {
        long seed = new Date().getTime();
        long length = 400;
        try (RandomInputStream in = new RandomInputStream(seed, length);
             RandomOutputStream out = new RandomOutputStream(seed, length)) {
            out.write(in.read()); // advance one byte
            ByteStreams.copy(in, out); // copy the rest
            try {
                out.write('a');
                fail("exception expected on extra write");
            } catch (IOException e) {
                // expected
            }
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public final void testParseLength() {
        assertEquals(1024, RandomConnectorClient.parseLength("1k"));
        assertEquals(1024, RandomConnectorClient.parseLength("1Kb"));
        assertEquals(MB, RandomConnectorClient.parseLength("1m"));
        assertEquals(5*TB, RandomConnectorClient.parseLength("5tb"));
    }
}
