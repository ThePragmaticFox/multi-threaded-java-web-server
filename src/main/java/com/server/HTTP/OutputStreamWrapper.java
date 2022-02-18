package com.server.HTTP;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;

public class OutputStreamWrapper {

    private final StringWriter stringWriter;
    private final OutputStream outputStream;
    private final BufferedOutputStream bufferedOutputStream;

    public OutputStreamWrapper(final OutputStream outputStream) {
        this.stringWriter = new StringWriter();
        this.outputStream = outputStream;
        this.bufferedOutputStream = new BufferedOutputStream(outputStream);
    }

    public String toString() {
        return stringWriter.toString();
    }

    public BufferedOutputStream get() {
        return bufferedOutputStream;
    }

    public void write(final byte[] bytes) throws IOException {
        stringWriter.write(new String(bytes));
        bufferedOutputStream.write(bytes);
    }

    public void close() throws IOException {
        stringWriter.close();
        bufferedOutputStream.close();
        outputStream.close();
    }
}
