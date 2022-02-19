package com.server;

import java.lang.System.Logger.Level;
import java.util.List;
import com.server.HTTP.OutputStreamWrapper;

public class ServerLogger {

    private static final Level LEVEL = Level.ALL;

    public static void log(final Level level, final String message) {
        if (LEVEL.getSeverity() > level.getSeverity()) {
            return;
        }
        System.out.println(message);
    }

    public static void log(final List<String> headerLines, final OutputStreamWrapper outputStream) {
        if (LEVEL.getSeverity() > Level.DEBUG.getSeverity()) {
            return;
        }
        System.out.println("Request:\n");
        System.out.println(headerLines.stream().reduce("", (x, y) -> x + y + "\n"));
        System.out.println();
        System.out.println("Response:\n");
        final String debugString = outputStream.toString(0, 250);
        System.out.println(debugString + "\n");
        System.out.println(new String(new char[79]).replace("\0", "-"));
        System.out.println();
    }
}
