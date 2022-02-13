package com.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import io.javalin.http.staticfiles.Location;

public class WebServer {

    private final Server jettyServer;
    private final Javalin javalinServer;
    private final WebServerConfig config;
    private final QueuedThreadPool threadPool;

    public WebServer(final WebServerConfig webServerConfig) {
        threadPool = new QueuedThreadPool(webServerConfig.getNbPoolThreads());
        jettyServer = new Server(threadPool);
        this.javalinServer = Javalin.create(javalinConfig -> {
            javalinConfig.maxRequestSize = webServerConfig.getQueueSize();
            javalinConfig.addStaticFiles(webServerConfig.getRoot(), Location.EXTERNAL);
            javalinConfig.server(() -> jettyServer);
        });
        javalinServer.addHandler(HandlerType.PUT, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        javalinServer.addHandler(HandlerType.POST, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        javalinServer.addHandler(HandlerType.DELETE, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        javalinServer.addHandler(HandlerType.PATCH, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        javalinServer.addHandler(HandlerType.OPTIONS, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
        this.config = webServerConfig;
    }

    public void run() {
        javalinServer.start(config.getHost(), config.getPort());
    }

    public void stop() {
        javalinServer.stop();
        System.out.println("Server shutdown.");
    }
}
