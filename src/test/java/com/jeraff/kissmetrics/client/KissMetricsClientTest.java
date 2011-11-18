package com.jeraff.kissmetrics.client;

import com.ning.http.client.AsyncHttpClient;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class KissMetricsClientTest {
    private static KissMetricsClient client;

    @BeforeClass
    public static void setupClient() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        client = new KissMetricsClient(System.getProperty("KISS_API"), "arinTesting", httpClient, false);
    }

    @Test
    public void testRecord() throws KissMetricsException {
        KissMetricsResponse resp = client.record("loggedin").getResponse();
        Assert.assertEquals(200, resp.getStatus());

        resp = client.record("purchased", new KissMetricsProperties().put("sku", "abcedfg"))
                     .getResponse();
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testAlias() throws KissMetricsException {
        KissMetricsResponse resp = client.alias("arin@jeraff.com").getResponse();
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testSet() throws KissMetricsException {
        KissMetricsResponse resp = client.set(new KissMetricsProperties().put("age", 33)).getResponse();
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testURLEncoding() throws KissMetricsException {
        KissMetricsResponse resp = client
                .set(new KissMetricsProperties().put("encodeDeez", "This is cool")).getResponse();
        Assert.assertEquals(200, resp.getStatus());

        resp = client.set(new KissMetricsProperties().put("2 things", "This & that")).getResponse();
        Assert.assertEquals(200, resp.getStatus());

        resp = client.set(new KissMetricsProperties().put("some symbols?", "? question & and % percent"))
                     .getResponse();

        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testInternationalCharacters() throws KissMetricsException {
        String chineseString = "??/??";

        KissMetricsResponse resp = client
                .set(new KissMetricsProperties().put("intlChars", chineseString)).getResponse();
        Assert.assertEquals(200, resp.getStatus());
    }
}
