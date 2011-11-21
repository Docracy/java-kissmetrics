package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsException;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.jeraff.kissmetrics.client.el.testobjects.SampleToadProvider;
import com.jeraff.kissmetrics.toad.aop.Kissmetrics;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ToadELHelperTest {
    private SampleToadProvider sampleToadProvider;
    private ToadELHelper toadELHelper;

    @Before
    public void setupToadELHelper() {
        sampleToadProvider = new SampleToadProvider();
        toadELHelper = new ToadELHelper(sampleToadProvider);
    }

    @Test
    public void testSimpleAliasPopulate() throws KissMetricsException, NoSuchMethodException {
        final Kissmetrics annotation =
            sampleToadProvider.getClass().getMethod("simpleAlias").getAnnotation(Kissmetrics.class);

        final String userId = annotation.alias()[0].id();
        final String to = annotation.alias()[0].to();

        toadELHelper.populateToad(annotation);

        final ToadUser toadUser = sampleToadProvider.getToad().getUsers().get(userId);
        Assert.assertNotNull(toadUser);
        Assert.assertTrue(toadUser.getAliases().contains(to));
    }

    @Test
    public void testMultiAliasPopulate() throws KissMetricsException, NoSuchMethodException {
        final Kissmetrics annotation =
            sampleToadProvider.getClass().getMethod("multiAlias").getAnnotation(Kissmetrics.class);

        final String userId = annotation.alias()[0].id();
        final String to = annotation.alias()[0].to();
        final String to2 = annotation.alias()[1].to();

        toadELHelper.populateToad(annotation);

        final ToadUser toadUser = sampleToadProvider.getToad().getUsers().get(userId);
        Assert.assertNotNull(toadUser);

        Assert.assertTrue(toadUser.getAliases().contains(to));
        Assert.assertTrue(toadUser.getAliases().contains(to2));
    }

    @Test
    public void testSetKissProps() throws KissMetricsException, NoSuchMethodException {
        final Kissmetrics annotation =
            sampleToadProvider.getClass().getMethod("setKissProps").getAnnotation(Kissmetrics.class);

        final KissMetricsProperties kissProps = new KissMetricsProperties().put("purchased", "latte").put("cost", 5);
        sampleToadProvider.setProps(kissProps);

        final String userId = annotation.set()[0].id();

        toadELHelper.populateToad(annotation);

        final ToadUser toadUser = sampleToadProvider.getToad().getUsers().get(userId);
        Assert.assertNotNull(toadUser);

        Assert.assertTrue(toadUser.getPropsMap().containsKey(userId));
        Assert.assertEquals(kissProps.getQueryString(), toadUser.getPropsMap().get(userId).getQueryString());
    }

    @Test
    public void testSetHashProps() throws KissMetricsException, NoSuchMethodException {
        final Kissmetrics annotation =
            sampleToadProvider.getClass().getMethod("setHashProps").getAnnotation(Kissmetrics.class);

        final KissMetricsProperties kissProps = new KissMetricsProperties().put("purchased", "latte").put("cost", 5);
        final HashMap<String, Object> hashProps = new LinkedHashMap<String, Object> ();
        hashProps.put("purchased", "latte");
        hashProps.put("cost", 5);
        sampleToadProvider.setHashProps(hashProps);

        final String userId = annotation.set()[0].id();

        toadELHelper.populateToad(annotation);

        final ToadUser toadUser = sampleToadProvider.getToad().getUsers().get(userId);
        Assert.assertNotNull(toadUser);

        Assert.assertTrue(toadUser.getPropsMap().containsKey(userId));
        Assert.assertEquals(kissProps.getQueryString(), toadUser.getPropsMap().get(userId).getQueryString());
    }

    @Test
    public void testSetMultiUser() throws NoSuchMethodException, KissMetricsException {
        final Kissmetrics annotation =
            sampleToadProvider.getClass().getMethod("setMultiUser").getAnnotation(Kissmetrics.class);

        final String userId = annotation.set()[0].id();
        final String userId2 = annotation.set()[1].id();
        final String key = annotation.set()[0].key();
        final String value = (String)toadELHelper.resolve(annotation.set()[0].value());
        final KissMetricsProperties kissProps = new KissMetricsProperties().put(key, value);

        toadELHelper.populateToad(annotation);

        final ToadUser toadUser = sampleToadProvider.getToad().getUsers().get(userId);
        final ToadUser toadUser2 = sampleToadProvider.getToad().getUsers().get(userId2);
        Assert.assertNotNull(toadUser);
        Assert.assertNotNull(toadUser2);

        Assert.assertTrue(toadUser.getPropsMap().containsKey(userId));
        Assert.assertEquals(kissProps.getQueryString(), toadUser.getPropsMap().get(userId).getQueryString());
        Assert.assertEquals(kissProps.getQueryString(), toadUser2.getPropsMap().get(userId2).getQueryString());
    }

    @Test
    public void testSetKeyValProps() throws NoSuchMethodException, KissMetricsException {
        final Kissmetrics annotation =
            sampleToadProvider.getClass().getMethod("setKeyValProps").getAnnotation(Kissmetrics.class);

        final String userId = annotation.set()[0].id();
        final String key = annotation.set()[0].key();
        final String value = (String)toadELHelper.resolve(annotation.set()[0].value());
        final KissMetricsProperties kissProps = new KissMetricsProperties().put(key, value);

        toadELHelper.populateToad(annotation);

        final ToadUser toadUser = sampleToadProvider.getToad().getUsers().get(userId);
        Assert.assertNotNull(toadUser);

        Assert.assertTrue(toadUser.getPropsMap().containsKey(userId));
        Assert.assertEquals(kissProps.getQueryString(), toadUser.getPropsMap().get(userId).getQueryString());
    }

    @Test
    /*
     * Note: this is just to test the distinction between el and non-el values, the JeraffELResolverTest does tests
     * to resolve different types of objects
     */
    public void testResolve() throws Exception {
        // non el string, just returns its input
        Assert.assertEquals(sampleToadProvider.getTestString(), (String)toadELHelper.resolve(sampleToadProvider.getTestString()));

        // el string should resolve
        Assert.assertEquals(sampleToadProvider.getTestString(), (String)toadELHelper.resolve("${testString}"));

        final HashMap<String, Object> hashProps = new LinkedHashMap<String, Object> ();
        final KissMetricsProperties kissProps = new KissMetricsProperties().put("purchased", "latte")
                .put("cost", 3)
                .put("change", new Float(.2))
                .put("tax", 0.50)
                .put("hasRewards", true);
        hashProps.put("purchased", "latte");
        hashProps.put("cost", 3);
        hashProps.put("change", new Float(.2));
        hashProps.put("tax", 0.50);
        hashProps.put("hasRewards", true);
        sampleToadProvider.setHashProps(hashProps);

        final Object resolvedMap = toadELHelper.resolve("${hashProps}");
        Assert.assertTrue(resolvedMap instanceof KissMetricsProperties);
        Assert.assertEquals(kissProps.getQueryString(), ((KissMetricsProperties)resolvedMap).getQueryString());
    }
}
