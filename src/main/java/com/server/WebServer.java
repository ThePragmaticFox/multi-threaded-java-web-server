package com.server;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import io.javalin.http.staticfiles.Location;

public class WebServer {

    final Javalin server;
    final WebServerConfig config;

    public WebServer(final WebServerConfig webServerConfig) {
        this.server = Javalin.create(javalinConfig -> {
            javalinConfig.maxRequestSize = webServerConfig.getQueueSize();
            javalinConfig.addStaticFiles(webServerConfig.getRoot(), Location.EXTERNAL);
        });
        server.addHandler(HandlerType.PUT, "/*", ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        server.addHandler(HandlerType.POST, "/*", ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        server.addHandler(HandlerType.DELETE, "/*", ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        server.addHandler(HandlerType.PATCH, "/*", ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        server.addHandler(HandlerType.OPTIONS, "/*", ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        this.config = webServerConfig;
    }

    public void run() {
        server.start(config.getHost(), config.getPort());
    }

    public void stop() {
        server.stop();
        System.out.println("Server shutdown.");
    }
}
