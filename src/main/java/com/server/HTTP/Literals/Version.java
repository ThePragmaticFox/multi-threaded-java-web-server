package com.server.HTTP.Literals;

public enum Version {
    HTTP_1_1("HTTP/1.1"), UNKNOWN("HTTP/1.1");

    public static Version getVersion(final String version) {
        return switch (version) {
            case "HTTP/1.1" -> HTTP_1_1;
            default -> UNKNOWN;
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
