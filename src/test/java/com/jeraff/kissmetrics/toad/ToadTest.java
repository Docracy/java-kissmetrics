package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsClient;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.ning.http.client.AsyncHttpClient;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Note: these tests only verify that the Toad object is being populated properly via its public methods.  You should
 * check your kissmetrics live dashboard to make sure the calls are actually going through.  You'll need to set the
 * KISS_API jvm variable in your runtime configuration in order for the kissmetrics api to actually get called:
 * -DKISS_API=your_api_key
 */
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
        final KissMetricsProperties props = new KissMetricsProperties().put("prop1", "foo");
        toad.user("ryan").set(props);

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("ryan"));
        Assert.assertEquals(props.getQueryString(), toad.user("ryan").getPropsMap().get("ryan").getQueryString());

        toad.run();
    }

    @Test
    public void testSetFluent() {
        final KissMetricsProperties props = new KissMetricsProperties().put("prop1", "foo");
        toad.user("ryan").set("prop1", "foo");

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("ryan"));
        Assert.assertEquals(props.getQueryString(), toad.user("ryan").getPropsMap().get("ryan").getQueryString());

        toad.run();
    }

    @Test
    public void testAbort() {
        // check your kissmetrics live feed to make sure this doesn't go through
        toad.user("ryan").record("blah");
        toad.abort();
        toad.run();
    }

    @Test
    public void testMixedFluent() {
        KissMetricsProperties visitedProps = new KissMetricsProperties().put("location", "San Mateo");
        KissMetricsProperties purchasedProps = new KissMetricsProperties().put("item", "latte").put("cost", 3);
        KissMetricsProperties userProps = new KissMetricsProperties().put("age", 25).put("hasRewards", true);

        toad.user("ryan").set("age", 25).record("visited").props("location", "San Mateo")
            .record("purchased").props("item", "latte").props("cost", 3)
            .set("hasRewards", true)
            .alias("ryan@toodo.com").alias("sdklfjksd;fh2833332323");

        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("visited"));
        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("purchased"));
        Assert.assertNotNull(toad.user("ryan").getPropsMap().containsKey("ryan"));
        Assert.assertTrue(toad.user("ryan").getAliases().contains("ryan@toodo.com"));
        Assert.assertTrue(toad.user("ryan").getAliases().contains("sdklfjksd;fh2833332323"));
        Assert.assertEquals(visitedProps.getQueryString(), toad.user("ryan").getPropsMap().get("visited").getQueryString());
        Assert.assertEquals(purchasedProps.getQueryString(), toad.user("ryan").getPropsMap().get("purchased").getQueryString());
        Assert.assertEquals(userProps.getQueryString(), toad.user("ryan").getPropsMap().get("ryan").getQueryString());

        toad.run();
    }
}
