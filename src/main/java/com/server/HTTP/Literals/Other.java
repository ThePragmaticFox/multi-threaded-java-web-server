package com.server.HTTP.Literals;

public enum Other {
    EMPTY(""),
    SPACE(" "),
    NEWLINE("\n"),
    NEW_EMPTYLINE("\r\n\r\n");

    private final String literal;

    private Other(final String literal) {
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
