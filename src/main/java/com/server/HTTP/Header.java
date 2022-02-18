package com.server.HTTP;

import java.nio.file.Path;
import com.server.HTTP.Literals.Version;

public class Header {

    private final Path path;
    private final Method method;
    private final Version version;

    public Header(final Path path, final Method method, final Version version) {
        this.path = path;
        this.method = method;
        this.version = version;
    }

    public Path getPath() {
        return path;
    }

    public Method getMethod() {
        return method;
    }

    public Version getVersion() {
        return version;
    }
}
