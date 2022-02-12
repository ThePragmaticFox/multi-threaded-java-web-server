package com.server;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.Gson;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;

public class WebServer {

    final WebServerConfig webServerConfig;

    public WebServer(final WebServerConfig webServerConfig) {
        this.webServerConfig = webServerConfig;
    }

    public static void main(String[] args) throws IOException {
        final Gson gson = new Gson();
        final Reader reader = Files.newBufferedReader(Paths.get("config.json"));
        final WebServerConfig webServerConfig = gson.fromJson(reader, WebServerConfig.class);
        final WebServer webServer = new WebServer(webServerConfig);
        webServer.run();
    }

    private void run() {
        final Javalin javalinWebServer = Javalin.create(javalinConfig -> {
            javalinConfig.maxRequestSize = webServerConfig.getQueueSize();
            javalinConfig.addStaticFiles(webServerConfig.getRoot(), Location.EXTERNAL);
        });
        javalinWebServer.jettyServer().setServerHost(webServerConfig.getLocalIp());
        javalinWebServer.jettyServer().setServerPort(webServerConfig.getPort());
        //javalinWebServer.get("/", ctx -> ctx.result("Henlo"));
        javalinWebServer.start();
    }
}
