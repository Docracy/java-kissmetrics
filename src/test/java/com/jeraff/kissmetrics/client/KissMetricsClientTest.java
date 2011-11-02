package com.jeraff.kissmetrics.client;

import junit.framework.Assert;
import org.apache.http.HttpHost;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.junit.BeforeClass;
import org.junit.Test;

public class KissMetricsClientTest {
    private static KissMetricsClient client;

    @BeforeClass
    public static void setupClient() {
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
        cm.setMaxTotal(200);
        cm.setDefaultMaxPerRoute(20);

        HttpHost kissMetricsHost = new HttpHost(KissMetricsClient.API_HOST, 80);
        cm.setMaxForRoute(new HttpRoute(kissMetricsHost), 50);

        client = new KissMetricsClient("1dcdc0b311b41e32f117b482daad40b0f7d67a93",
                "arinTesting",
                cm,
                false);
    }

    @Test
    public void testRecord() throws KissMetricsException {
        KissMetricsResponse resp = client.record("loggedin");
        Assert.assertEquals(200, resp.getStatus());

        resp = client.record("purchased", new KissMetricsProperties().put("sku", "abcedfg"));
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testAlias() throws KissMetricsException {
        KissMetricsResponse resp = client.alias("arin@jeraff.com");
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testSet() throws KissMetricsException {
        KissMetricsResponse resp = client.set(new KissMetricsProperties().put("age", 33));
        Assert.assertEquals(200, resp.getStatus());
    }

    @Test
    public void testURLEncoding() throws KissMetricsException {
        KissMetricsResponse resp = client
                .set(new KissMetricsProperties().put("encodeDeez", "This is cool"));
        Assert.assertEquals(200, resp.getStatus());

        resp = client.set(new KissMetricsProperties().put("2 things", "This & that"));
        Assert.assertEquals(200, resp.getStatus());

        resp = client
                .set(new KissMetricsProperties().put("some symbols?", "? question & and % percent"));
        Assert.assertEquals(200, resp.getStatus());
    }
}
