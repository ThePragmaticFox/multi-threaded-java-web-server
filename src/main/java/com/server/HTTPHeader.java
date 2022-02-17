package com.server;

import java.nio.file.Path;

public class HTTPHeader {

    private final Path path;
    private final HTTPMethod method;
    private final HTTPVersion version;

    public HTTPHeader(final Path path, final HTTPMethod method, final HTTPVersion version) {
        this.path = path;
        this.method = method;
        this.version = version;
    }

    public Path getPath() {
        return path;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public HTTPVersion getVersion() {
        return version;
    }
}
