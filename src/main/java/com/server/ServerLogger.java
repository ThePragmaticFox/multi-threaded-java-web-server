package com.server;

import java.util.logging.Level;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import com.server.HTTP.OutputStreamWrapper;
import com.server.HTTP.Literals.Other;

/**
 * Work in Progress TODO: Substitue with real logger and manage levels / filters.
 */

public class ServerLogger {

    private static final Level LEVEL = Level.INFO;

    public static Level getLevel() {
        return LEVEL;
    }

    public static void log(final Level level, final String message) {
        if (LEVEL.intValue() > level.intValue()) {
            return;
        }
        System.out.println(message);
    }

    public static void logRequest(final List<String> headerLines) {
        if (LEVEL.intValue() > Level.FINEST.intValue()) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        final String logRequest = builder.append("Request:").append(Other.NEWLINE.getString()).append(
                headerLines.stream().reduce(Other.EMPTY.getString(), (x, y) -> x + y + Other.NEWLINE.getString()))
                .toString();
        System.out.println(logRequest);
    }

    public static void logResponse(final OutputStreamWrapper outputStream) {
        if (LEVEL.intValue() > Level.FINEST.intValue()) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        final String logResponse = builder.append("Response:").append(Other.NEWLINE.getString())
                .append(outputStream.toString(0, 250)).append(Other.NEWLINE.getString())
                .append(new String(new char[79]).replace("\0", "-")).toString();
        System.out.println(logResponse);
    }

    public static void log(final Level level1, final Level level2, final Exception exception) {
        if (LEVEL.intValue() > level1.intValue()) {
            return;
        }
        System.out.println(exception.getLocalizedMessage());
        if (LEVEL.intValue() > level2.intValue()) {
            return;
        }
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        final String stackTrace = stringWriter.toString();
        System.out.println(stackTrace);
    }

    public static void logThreadStart(final Level level) {
        if (LEVEL.intValue() > level.intValue()) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        final String threadStarted = builder.append("Thread <<").append(Thread.currentThread().getId()).append(", ")
                .append(Thread.currentThread().getName()).append(">> started.").toString();
        System.out.println(threadStarted);
    }

    public static void logThreadStop(final Level level) {
        if (LEVEL.intValue() > level.intValue()) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        final String threadFinished = builder.append("Thread <<").append(Thread.currentThread().getId()).append(", ")
                .append(Thread.currentThread().getName()).append(">> finished.").toString();
        System.out.println(threadFinished);
    }
}
