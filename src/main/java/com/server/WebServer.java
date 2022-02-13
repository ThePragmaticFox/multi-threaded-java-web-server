package com.server;

import io.javalin.Javalin;
import io.javalin.http.HandlerType;
import io.javalin.http.HttpCode;
import io.javalin.http.staticfiles.Location;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class WebServer {

    private final Server jettyServer;
    private final Javalin javalinServer;
    private final WebServerConfig config;
    private final QueuedThreadPool threadPool;

    public WebServer(final WebServerConfig webServerConfig) {

        this.config = webServerConfig;

        this.threadPool = new QueuedThreadPool(this.config.getNbPoolThreads());
        this.jettyServer = new Server(threadPool);

        this.javalinServer = Javalin.create(javalinConfig -> {
            javalinConfig.addStaticFiles(this.config.getRoot(), Location.EXTERNAL);
            javalinConfig.server(() -> this.jettyServer);
        });

        this.javalinServer.addHandler(HandlerType.PUT, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));

        this.javalinServer.addHandler(HandlerType.POST, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));

        this.javalinServer.addHandler(HandlerType.DELETE, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));

        this.javalinServer.addHandler(HandlerType.PATCH, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));

        this.javalinServer.addHandler(HandlerType.OPTIONS, "/*",
                ctx -> ctx.status(HttpCode.METHOD_NOT_ALLOWED));
    }

    public void run() {
        this.javalinServer.start(this.config.getHost(), this.config.getPort());
    }

    public void stop() {
        this.javalinServer.stop();
        System.out.println("Server shutdown.");
    }
}
