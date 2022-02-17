package com.server.HTTP;

public enum HTTPConstants {
    NEWLINE("\n"),
    NEW_EMPTYLINE("\r\n\r\n"),
    CONNECTION_CLOSE("Connection: close"),
    HTTP_VERSION_NOT_SUPPORTED("HTTP/1.1 505 HTTP Version Not Supported"),
    BAD_REQUEST("HTTP/1.1 400 Bad Request"),
    INTERNAL_SERVER_ERROR("HTTP/1.1 500 Internal Server Error"),
    NOT_IMPLEMENTED("HTTP/1.1 501 Not Implemented"),
    OK("HTTP/1.1 200 OK"),
    NOT_FOUND("HTTP/1.1 404 Not Found"),
    NO_SNIFF("X-Content-Type-Options: nosniff"),
    CONTENT_LENGTH("Content-Length: ");

    private final String literal;

    private HTTPConstants(final String literal) {
        this.literal = literal;
    }

    public boolean equalsName(final String otherLiteral) {
        return literal.equals(otherLiteral);
    }

    public byte[] getBytes() {
        return this.literal.getBytes();
    }
}
