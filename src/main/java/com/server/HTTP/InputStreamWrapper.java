package com.server.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class InputStreamWrapper implements AutoCloseable {

    private final InputStream inputStream;
    private final BufferedReader bufferedReader;
    private final InputStreamReader inputStreamReader;

    public InputStreamWrapper(final InputStream inputStream) {
        this.inputStream = inputStream;
        this.inputStreamReader = new InputStreamReader(inputStream);
        this.bufferedReader = new BufferedReader(inputStreamReader);
    }

    public BufferedReader get() {
        return bufferedReader;
    }

    public String readLine() throws IOException {
        return bufferedReader.readLine();
    }

    public void close() throws IOException {
        bufferedReader.close();
        inputStreamReader.close();
        inputStream.close();
    }
}
