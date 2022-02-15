package com.server;

import java.io.PrintWriter;

/**
 * This pattern is adapted from
 * https://apocalisp.wordpress.com/2009/08/21/structural-pattern-matching-in-java/
 * 
 * It is very easy to extend it with more HTTP methods.
 */

public abstract class HTTPMethod {

    private final HTTPHeader header;
    private final PrintWriter outputStream;

    protected HTTPMethod(final HTTPHeader header, final PrintWriter outputStream) {
        this.header = header;
        this.outputStream = outputStream;
    }

    public HTTPHeader getHeader() {
        return header;
    }

    public PrintWriter getOutputStream() {
        return outputStream;
    }

    public abstract <T> T match(Function<GET, T> get, Function<HEAD, T> head,
            Function<UNKNOWN, T> unknown);

    public static final class GET extends HTTPMethod {

        public GET(final HTTPHeader header, final PrintWriter outputStream) {
            super(header, outputStream);
        }

        @Override
        public <T> T match(Function<GET, T> get, Function<HEAD, T> head,
                Function<UNKNOWN, T> unknown) {
            return get.function(this);
        }
    }

    public static final class HEAD extends HTTPMethod {

        public HEAD(final HTTPHeader header, final PrintWriter outputStream) {
            super(header, outputStream);
        }

        @Override
        public <T> T match(Function<GET, T> get, Function<HEAD, T> head,
                Function<UNKNOWN, T> unknown) {
            return head.function(this);
        }
    }

    public static final class UNKNOWN extends HTTPMethod {

        public UNKNOWN(final HTTPHeader header, final PrintWriter outputStream) {
            super(header, outputStream);
        }

        @Override
        public <T> T match(Function<GET, T> get, Function<HEAD, T> head,
                Function<UNKNOWN, T> unknown) {
            return unknown.function(this);
        }
    }

    public static HTTPMethod parseMethod(final HTTPHeader header, final PrintWriter outputStream) {
        switch (header.getMethod()) {
            case "GET":
                return new GET(header, outputStream);
            case "HEAD":
                return new HEAD(header, outputStream);
            default:
                return new UNKNOWN(header, outputStream);
        }
    }
}
