package com.server;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

public class TestWebServer {

    private String root = "www";

    @Before
    public void setUpBeforeClass() {
        final String localRoot = System.getProperty("TestWebServerRoot");
        if (localRoot != null) {
            root = localRoot;
        }
    }

    private WebServerConfig getWebServerConfig(int port) {
        return new WebServerConfig(root, "0.0.0.0", port, 5, 250);
    }

    @Test
    public void requestGETExistingShouldSucceed() {
        // given
        final WebServerConfig config = getWebServerConfig(3125);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.get(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.get(baseAddr + "about").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 200);
        assertEquals(res1.getStatusText(), "OK");
        assertEquals(res2.getStatus(), 200);
        assertEquals(res2.getStatusText(), "OK");
    }

    @Test
    public void requestHEADExistingShouldSucceed() {
        // given
        final WebServerConfig config = getWebServerConfig(3126);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.head(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.head(baseAddr + "about").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 200);
        assertEquals(res1.getStatusText(), "OK");
        assertEquals(res2.getStatus(), 200);
        assertEquals(res2.getStatusText(), "OK");
    }

    @Test
    public void requestGETNonExistingShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3127);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res = Unirest.get(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res.getStatus(), 404);
        assertEquals(res.getStatusText(), "Not Found");
    }

    @Test
    public void requestHEADNonExistingShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3128);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res = Unirest.head(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res.getStatus(), 404);
        assertEquals(res.getStatusText(), "Not Found");
    }

    @Test
    public void requestPOSTShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3129);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.post(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.post(baseAddr + "about").asString();
        final HttpResponse<String> res3 = Unirest.post(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 405);
        assertEquals(res1.getStatusText(), "Method Not Allowed");
        assertEquals(res2.getStatus(), 405);
        assertEquals(res2.getStatusText(), "Method Not Allowed");
        assertEquals(res3.getStatus(), 405);
        assertEquals(res3.getStatusText(), "Method Not Allowed");
    }

    @Test
    public void requestPUTShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3130);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.put(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.put(baseAddr + "about").asString();
        final HttpResponse<String> res3 = Unirest.put(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 405);
        assertEquals(res1.getStatusText(), "Method Not Allowed");
        assertEquals(res2.getStatus(), 405);
        assertEquals(res2.getStatusText(), "Method Not Allowed");
        assertEquals(res3.getStatus(), 405);
        assertEquals(res3.getStatusText(), "Method Not Allowed");
    }

    @Test
    public void requestDELETEShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3131);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.delete(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.delete(baseAddr + "about").asString();
        final HttpResponse<String> res3 = Unirest.delete(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 405);
        assertEquals(res1.getStatusText(), "Method Not Allowed");
        assertEquals(res2.getStatus(), 405);
        assertEquals(res2.getStatusText(), "Method Not Allowed");
        assertEquals(res3.getStatus(), 405);
        assertEquals(res3.getStatusText(), "Method Not Allowed");
    }

    @Test
    public void requestPATCHShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3132);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.patch(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.patch(baseAddr + "about").asString();
        final HttpResponse<String> res3 = Unirest.patch(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 405);
        assertEquals(res1.getStatusText(), "Method Not Allowed");
        assertEquals(res2.getStatus(), 405);
        assertEquals(res2.getStatusText(), "Method Not Allowed");
        assertEquals(res3.getStatus(), 405);
        assertEquals(res3.getStatusText(), "Method Not Allowed");
    }

    @Test
    public void requestOPTIONSShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3133);
        final WebServer server = new WebServer(config);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        server.run();
        final HttpResponse<String> res1 = Unirest.options(baseAddr).asString();
        final HttpResponse<String> res2 = Unirest.options(baseAddr + "about").asString();
        final HttpResponse<String> res3 = Unirest.options(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(res1.getStatus(), 405);
        assertEquals(res1.getStatusText(), "Method Not Allowed");
        assertEquals(res2.getStatus(), 405);
        assertEquals(res2.getStatusText(), "Method Not Allowed");
        assertEquals(res3.getStatus(), 405);
        assertEquals(res3.getStatusText(), "Method Not Allowed");
    }
}
