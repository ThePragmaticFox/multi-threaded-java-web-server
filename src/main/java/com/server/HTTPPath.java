package com.server;

import java.nio.file.Path;
import java.nio.file.Paths;

public class HTTPPath {

    private final Path path;

    public HTTPPath(final String path) {
        this.path = Paths.get(path);
    }

    public String toString() {
        return path.toString();
    }
}
