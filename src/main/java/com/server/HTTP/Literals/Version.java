package com.server.HTTP.Literals;

import java.util.Optional;

public enum Version {
    HTTP_1_1("HTTP/1.1");

    public static Optional<Version> getVersion(final String version) {
        return switch (version) {
            case "HTTP/1.1" -> Optional.of(HTTP_1_1);
            default -> Optional.empty();
        };
    }

    private final String literal;

    private Version(final String literal) {
        this.literal = literal;
    }

    public boolean equalsName(final String otherLiteral) {
        return literal.equals(otherLiteral);
    }

    public byte[] getBytes() {
        return literal.getBytes();
    }

    public String getString() {
        return literal;
    }
}
