package com.server.HTTP;

public enum Literals {
    EMPTY(""),
    SPACE(" "),
    NEWLINE("\n"),
    NEW_EMPTYLINE("\r\n\r\n");

    private final String literal;

    private Literals(final String literal) {
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
