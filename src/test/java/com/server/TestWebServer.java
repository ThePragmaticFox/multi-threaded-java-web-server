package com.server;

import static org.junit.Assert.assertEquals;
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
        return new WebServerConfig(root, "0.0.0.0", port, 250, 1000, 100000);
    }

    @Test
    public void requestGETExistingShouldSucceed() {
        // given
        final WebServerConfig config = getWebServerConfig(3125);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
        final HttpResponse<String> res1 = Unirest.get(baseAddr).asString();
        System.out.println("\n\n\n" + res1.getBody());
        final HttpResponse<String> res2 = Unirest.get(baseAddr + "about").asString();
        System.out.println("\n\n\n" + res2.getBody());
        server.stop();

        // then
        assertEquals(200, res1.getStatus());
        assertEquals("OK", res1.getStatusText());
        assertEquals(200, res2.getStatus());
        assertEquals("OK", res2.getStatusText());
    }

    @Test
    public void requestHEADExistingShouldSucceed() {
        // given
        final WebServerConfig config = getWebServerConfig(3126);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
        final HttpResponse<String> res2 = Unirest.head(baseAddr + "about").asString();
        final HttpResponse<String> res1 = Unirest.head(baseAddr).asString();
        server.stop();

        // then
        assertEquals(200, res1.getStatus());
        assertEquals("OK", res1.getStatusText());
        assertEquals(200, res2.getStatus());
        assertEquals("OK", res2.getStatusText());
    }

    @Test
    public void requestGETNonExistingShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3127);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
        final HttpResponse<String> res = Unirest.get(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(404, res.getStatus());
        assertEquals("Not Found", res.getStatusText());
    }

    @Test
    public void requestHEADNonExistingShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3128);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
        final HttpResponse<String> res = Unirest.head(baseAddr + "hidden").asString();
        server.stop();

        // then
        assertEquals(404, res.getStatus());
        assertEquals("Not Found", res.getStatusText());
    }

    @Test
    public void requestPOSTShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3129);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
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
    }

    @Test
    public void requestPUTShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3130);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
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
    }

    @Test
    public void requestDELETEShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3131);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
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
    }

    @Test
    public void requestPATCHShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3132);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
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
    }

    @Test
    public void requestOPTIONSShouldFail() {
        // given
        final WebServerConfig config = getWebServerConfig(3133);
        final String baseAddr = "http://" + config.getHost() + ":" + config.getPort() + "/";

        // when
        final WebServer server = WebServer.start(config);
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
    }
}
