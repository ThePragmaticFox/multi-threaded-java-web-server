package com.server;

import java.util.logging.Level;
import java.util.List;
import com.server.HTTP.OutputStreamWrapper;
import com.server.HTTP.Literals.Other;

public class ServerLogger {

    private static final Level LEVEL = Level.FINE;

    public static void log(final Level level, final String message) {
        if (LEVEL.intValue() > level.intValue()) {
            return;
        }
        System.out.println(message);
    }

    public static void log(final Level level, final List<String> headerLines, final OutputStreamWrapper outputStream) {
        if (LEVEL.intValue() > level.intValue()) {
            return;
        }
        final StringBuilder builder = new StringBuilder();
        final String logString = builder.append("Request:").append(Other.NEWLINE.getString())
                .append(headerLines.stream().reduce(Other.EMPTY.getString(),
                        (x, y) -> x + y + Other.NEWLINE.getString()))
                .append(Other.NEWLINE.getString()).append("Response:").append(Other.NEWLINE.getString())
                .append(outputStream.toString(0, 250)).append(Other.NEWLINE.getString())
                .append(new String(new char[79]).replace("\0", "-")).append(Other.NEWLINE.getString()).toString();
        System.out.print(logString);
    }
}
