package com.server;

import java.io.IOException;
import java.io.OutputStream;

/**
 * wrapper class, so we can debug the output more conveniently.
 */

public class HTTPOutputStream {

    private final OutputStream outputStream;

    public HTTPOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public OutputStream get() {
        return outputStream;
    }

    public void write(byte[] arg0) throws IOException {
        outputStream.write(arg0);
    }

    public void flush() throws IOException {
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }
}
