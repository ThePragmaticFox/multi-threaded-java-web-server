package com.server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        final String path = args.length == 2 && args[0].equals("-f") ? args[1] : "config.json";
        final WebServer webServer = new WebServer(WebServerConfig.importConfig(path));
        webServer.run();
    }
}
