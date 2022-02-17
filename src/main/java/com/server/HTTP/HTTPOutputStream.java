package com.server.HTTP;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HTTPOutputStream {

    private final OutputStream outputStream;
    private final BufferedOutputStream bufferedOutputStream;

    public HTTPOutputStream(final OutputStream outputStream) {
        this.outputStream = outputStream;
        this.bufferedOutputStream = new BufferedOutputStream(outputStream);
    }

    public BufferedOutputStream get() {
        return bufferedOutputStream;
    }

    public void write(byte[] arg0) throws IOException {
        bufferedOutputStream.write(arg0);
    }

    public void close() throws IOException {
        bufferedOutputStream.close();
        outputStream.close();
    }
}
