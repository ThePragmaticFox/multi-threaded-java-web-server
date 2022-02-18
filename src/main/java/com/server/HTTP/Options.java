package com.server.HTTP;

public enum Options {
    CONNECTION_CLOSE("Connection: close"),
    NO_SNIFF("X-Content-Type-Options: nosniff"),
    CONTENT_LENGTH("Content-Length:"),
    CONTENT_TYPE("Content-Type:");

    private final String literal;

    private Options(final String literal) {
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
