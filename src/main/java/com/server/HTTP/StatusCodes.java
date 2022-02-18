package com.server.HTTP;

public enum StatusCodes {
    OK("200 OK"),
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found"),
    INTERNAL_SERVER_ERROR("500 Internal Server Error"),
    NOT_IMPLEMENTED("501 Not Implemented"),
    HTTP_VERSION_NOT_SUPPORTED("505 HTTP Version Not Supported");

    private final String literal;

    private StatusCodes(final String literal) {
        this.literal = literal;
    }

    public boolean equalsName(final String otherLiteral) {
        return literal.equals(otherLiteral);
    }

    public byte[] getBytes(final Version version) {
        return (version.getString() + Literals.SPACE.getString() + literal).getBytes();
    }

    public byte[] getBytes() {
        return literal.getBytes();
    }

    public String getString() {
        return literal;
    }
}
