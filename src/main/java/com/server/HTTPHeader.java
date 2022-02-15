package com.server;

import java.nio.file.Path;

public class HTTPHeader {

    private final Path path;
    private final String method;
    private final HTTPVersion version;

    public HTTPHeader(final Path path, final String method, final HTTPVersion version) {
        this.path = path;
        this.method = method;
        this.version = version;
    }

    public Path getPath() {
        return path;
    }

    public String getMethod() {
        return method;
    }

    public HTTPVersion getVersion() {
        return version;
    }
}
