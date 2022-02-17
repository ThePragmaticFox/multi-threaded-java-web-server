package com.server.HTTP;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * wrapper class, so we can debug the output more conveniently.
 */

public class HTTPOutputStream {

    private final BufferedOutputStream outputStream;

    public HTTPOutputStream(final OutputStream outputStream) {
        this.outputStream = new BufferedOutputStream(outputStream);
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
