package com.server.HTTP;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.logging.Level;
import com.server.ServerLogger;

public class OutputStreamWrapper implements Closeable {

    private final StringWriter stringWriter;
    private final OutputStream outputStream;
    private final BufferedOutputStream bufferedOutputStream;

    public OutputStreamWrapper(final OutputStream outputStream) {
        this.stringWriter = new StringWriter();
        this.outputStream = outputStream;
        this.bufferedOutputStream = new BufferedOutputStream(outputStream);
    }

    public String toString(final int startIndex, final int stopIndex) {
        if (ServerLogger.getLevel().intValue() > Level.FINEST.intValue()) {
            return "";
        }
        return stringWriter.getBuffer().substring(startIndex, Math.min(stopIndex, stringWriter.getBuffer().length()));
    }

    public BufferedOutputStream get() {
        return bufferedOutputStream;
    }

    public void write(final byte[] bytes, final int startIndex, final int stopIndex) throws IOException {
        bufferedOutputStream.write(bytes, startIndex, stopIndex);
        if (ServerLogger.getLevel().intValue() > Level.FINEST.intValue()) {
            return;
        }
        stringWriter.write(new String(bytes, startIndex, stopIndex));
    }

    public void write(final byte[] bytes) throws IOException {
        bufferedOutputStream.write(bytes);
        if (ServerLogger.getLevel().intValue() > Level.FINEST.intValue()) {
            return;
        }
        stringWriter.write(new String(bytes));
    }

    public void close() throws IOException {
        stringWriter.close();
        bufferedOutputStream.close();
        outputStream.close();
    }
}
