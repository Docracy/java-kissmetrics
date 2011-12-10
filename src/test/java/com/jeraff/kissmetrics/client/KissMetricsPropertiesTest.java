package com.jeraff.kissmetrics.client;

import org.junit.Assert;
import org.junit.Test;

public class KissMetricsPropertiesTest {
    @Test
    public void testPutSafe() throws Exception {
        KissMetricsProperties props = new KissMetricsProperties();
        props.put(KissMetricsClient.PROP_EVENT_NAME, "event1");
        props.putSafe(KissMetricsClient.PROP_EVENT_NAME, "event2");

        Assert.assertEquals(-1, props.getQueryString().indexOf("event1"));
        Assert.assertTrue(props.getQueryString().indexOf("event2") >= 0);
    }

    @Test
    public void testPutNulls() throws Exception {
        KissMetricsProperties props = new KissMetricsProperties();
        props.put("test", null);
    }
}
