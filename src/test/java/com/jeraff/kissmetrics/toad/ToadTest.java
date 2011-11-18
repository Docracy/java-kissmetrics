package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsClient;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.ning.http.client.AsyncHttpClient;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ToadTest {
    private static Toad toad;

    @BeforeClass
    public static void setupToadTest() {
        AsyncHttpClient httpClient = new AsyncHttpClient();
        KissMetricsClient client = new KissMetricsClient(System.getProperty("KISS_API"), "arinTesting", httpClient, false);
        toad = new Toad(client);
    }

    @Test
    public void testAlias() {
        toad.user("ryan").alias("ryan@toodo.com").alias("jdsf;klsdfhhsf899yyy988");

        Assert.assertEquals("ryan", toad.user("ryan").getId());
        Assert.assertTrue(toad.user("ryan").getAliases().contains("ryan@toodo.com"));
        Assert.assertTrue(toad.user("ryan").getAliases().contains("jdsf;klsdfhhsf899yyy988"));

        toad.run();

        Assert.assertEquals(0, toad.getUsers().size());
    }

    @Test
    public void testRecordSimple() {
        toad.user("ryan").record("testEvent");

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("testEvent"));

        toad.run();
    }

    @Test
    public void testRecordProps() {
        final KissMetricsProperties props = new KissMetricsProperties().put("prop1", "foo");
        toad.user("ryan").record("testEvent", props);

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("testEvent"));
        Assert.assertEquals(props.getQueryString(), toad.user("ryan").getPropsMap().get("testEvent").getQueryString());

        toad.run();
    }

    @Test
    public void testRecordPropsFluent() {
        final KissMetricsProperties fluentProps = new KissMetricsProperties().put("fluentProp", 5);
        toad.user("ryan").record("testEvent").props("fluentProp", 5);

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("testEvent"));
        Assert.assertEquals(fluentProps.getQueryString(), toad.user("ryan").getPropsMap().get("testEvent").getQueryString());

        toad.run();
    }

    @Test
    public void testMultiRecord() {
        final KissMetricsProperties fluentProps = new KissMetricsProperties().put("fluentProp", 5);
        final KissMetricsProperties fluentProps2 = new KissMetricsProperties().put("fluentProp2", "yo");
        toad.user("ryan").record("testEvent").props("fluentProp", 5).record("testEvent2").props("fluentProp2", "yo");

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("testEvent"));
        Assert.assertEquals(fluentProps.getQueryString(), toad.user("ryan").getPropsMap().get("testEvent").getQueryString());
        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("testEvent2"));
        Assert.assertEquals(fluentProps2.getQueryString(), toad.user("ryan").getPropsMap().get("testEvent2").getQueryString());

        toad.run();
    }

    @Test
    public void testSetSimple() {
        toad.user("ryan").set(new KissMetricsProperties().put("prop1", "foo"));

        toad.run();
    }

    @Test
    public void testAbort() {

    }

    @Test
    public void testMixedFluent() {

    }
}
