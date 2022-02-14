package com.server;

/**
 * This pattern is adapted from
 * https://apocalisp.wordpress.com/2009/08/21/structural-pattern-matching-in-java/
 * 
 * It is very easy to extend it with more HTTP methods.
 */

public abstract class ParseHTTP {

    public interface F<A, B> {
        public B f(A a);
    }

    private ParseHTTP() {}

    public abstract <T> T match(F<GET, T> get, F<HEAD, T> head, F<UNRECOGNIZED, T> unrecognized);

    public static final class GET extends ParseHTTP {

        @Override
        public <T> T match(F<GET, T> get, F<HEAD, T> head, F<UNRECOGNIZED, T> unrecognized) {
            return get.f(this);
        }
    }

    public static final class HEAD extends ParseHTTP {

        @Override
        public <T> T match(F<GET, T> get, F<HEAD, T> head, F<UNRECOGNIZED, T> unrecognized) {
            return head.f(this);
        }
    }

    public static final class UNRECOGNIZED extends ParseHTTP {

        @Override
        public <T> T match(F<GET, T> get, F<HEAD, T> head, F<UNRECOGNIZED, T> unrecognized) {
            return unrecognized.f(this);
        }
    }
}
