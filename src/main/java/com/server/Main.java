package com.server;

import java.lang.System.Logger.Level;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        final String path = args.length == 2 && args[0].equals("-f") ? args[1] : "config.json";
        final WebServerConfig webServerConfig = WebServerConfig.importConfig(path);
        final Optional<WebServer> webServer = WebServer.start(webServerConfig);
        if (webServer.isEmpty()) {
            ServerLogger.log(Level.ERROR, "Creation of web server failed.");
            ServerLogger.log(Level.ERROR, "Exit program.");
            System.exit(1);
        }
    }
}
