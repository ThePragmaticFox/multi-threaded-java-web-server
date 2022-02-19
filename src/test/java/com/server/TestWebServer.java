package com.server;

import static org.junit.Assert.assertEquals;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class TestWebServer {

    private String root = "www";

    @Before
    public void setup() {
        final String localRoot = System.getProperty("TestWebServerRoot");
        if (localRoot != null) {
            root = localRoot;
        }
    }

    private WebServerConfig getWebServerConfig(int port) {
        return new WebServerConfig(root, "0.0.0.0", port, 24, 100);
    }

    @Test
    public void requestGETExistingShouldSucceed() {
        // given
        final WebServerConfig config = getWebServerConfig(3125);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";
        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.get(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.get(baseAddr + "about").asString();
            server.stop();

            // then
            assertEquals(200, res1.getStatus());
            assertEquals("OK", res1.getStatusText());
            assertEquals(200, res2.getStatus());
            assertEquals("OK", res2.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestGETNonExistingShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3127);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res = Unirest.get(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(404, res.getStatus());
            assertEquals("Not Found", res.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestHEADShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3128);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.head(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.head(baseAddr + "about").asString();
            final HttpResponse<String> res3 = Unirest.head(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(501, res1.getStatus());
            assertEquals("Not Implemented", res1.getStatusText());
            assertEquals(501, res2.getStatus());
            assertEquals("Not Implemented", res2.getStatusText());
            assertEquals(501, res3.getStatus());
            assertEquals("Not Implemented", res3.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestPOSTShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3129);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.post(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.post(baseAddr + "about").asString();
            final HttpResponse<String> res3 = Unirest.post(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(501, res1.getStatus());
            assertEquals("Not Implemented", res1.getStatusText());
            assertEquals(501, res2.getStatus());
            assertEquals("Not Implemented", res2.getStatusText());
            assertEquals(501, res3.getStatus());
            assertEquals("Not Implemented", res3.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestPUTShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3130);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.put(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.put(baseAddr + "about").asString();
            final HttpResponse<String> res3 = Unirest.put(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(501, res1.getStatus());
            assertEquals("Not Implemented", res1.getStatusText());
            assertEquals(501, res2.getStatus());
            assertEquals("Not Implemented", res2.getStatusText());
            assertEquals(501, res3.getStatus());
            assertEquals("Not Implemented", res3.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestDELETEShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3131);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.delete(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.delete(baseAddr + "about").asString();
            final HttpResponse<String> res3 = Unirest.delete(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(501, res1.getStatus());
            assertEquals("Not Implemented", res1.getStatusText());
            assertEquals(501, res2.getStatus());
            assertEquals("Not Implemented", res2.getStatusText());
            assertEquals(501, res3.getStatus());
            assertEquals("Not Implemented", res3.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestPATCHShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3132);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.patch(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.patch(baseAddr + "about").asString();
            final HttpResponse<String> res3 = Unirest.patch(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(501, res1.getStatus());
            assertEquals("Not Implemented", res1.getStatusText());
            assertEquals(501, res2.getStatus());
            assertEquals("Not Implemented", res2.getStatusText());
            assertEquals(501, res3.getStatus());
            assertEquals("Not Implemented", res3.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }

    @Test
    public void requestOPTIONSShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3133);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        WebServer.start(config).ifPresentOrElse(server -> {
            final HttpResponse<String> res1 = Unirest.options(baseAddr).asString();
            final HttpResponse<String> res2 = Unirest.options(baseAddr + "about").asString();
            final HttpResponse<String> res3 = Unirest.options(baseAddr + "hidden").asString();
            server.stop();

            // then
            assertEquals(501, res1.getStatus());
            assertEquals("Not Implemented", res1.getStatusText());
            assertEquals(501, res2.getStatus());
            assertEquals("Not Implemented", res2.getStatusText());
            assertEquals(501, res3.getStatus());
            assertEquals("Not Implemented", res3.getStatusText());
        }, () -> Assert.fail("Web server couldn't be started."));
    }
}
