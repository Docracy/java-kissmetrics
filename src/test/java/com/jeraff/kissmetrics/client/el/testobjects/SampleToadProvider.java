package com.jeraff.kissmetrics.client.el.testobjects;

import com.jeraff.kissmetrics.client.KissMetricsClient;
import com.jeraff.kissmetrics.client.KissMetricsProperties;
import com.jeraff.kissmetrics.client.el.JeraffELResolver;
import com.jeraff.kissmetrics.toad.Toad;
import com.jeraff.kissmetrics.toad.ToadProvider;
import com.jeraff.kissmetrics.toad.aop.Alias;
import com.jeraff.kissmetrics.toad.aop.Kissmetrics;
import com.jeraff.kissmetrics.toad.aop.Record;
import com.jeraff.kissmetrics.toad.aop.Set;
import com.ning.http.client.AsyncHttpClient;

import javax.el.ExpressionFactory;
import java.util.HashMap;

public class SampleToadProvider implements ToadProvider {
    private Toad toad;
    private String testString = "testing 1 2";
    private KissMetricsProperties props = new KissMetricsProperties();
    private HashMap<String, Object> hashProps = new HashMap<String, Object>();

    public SampleToadProvider() {
    }

    @Override
    public Toad getToad() {
        if (toad == null) {
            AsyncHttpClient httpClient = new AsyncHttpClient();
            KissMetricsClient client = new KissMetricsClient(System.getProperty("KISS_API"), "arinTesting", httpClient, false);
            toad = new Toad(client);

            ClassLoader cl = JeraffELResolver.class.getClassLoader();
            try {
                Class<?> expressionFactoryClass = cl.loadClass("de.odysseus.el.ExpressionFactoryImpl");
                toad.setExpressionFactory((ExpressionFactory) expressionFactoryClass.newInstance());
            } catch (Exception e) {
            }
        }

        return toad;
    }

    @Kissmetrics(alias =  @Alias(id = "ryan", to = "ryan@toodo.com"))
    public void simpleAlias() {}

    @Kissmetrics(alias =  {@Alias(id = "ryan", to = "ryan@toodo.com"), @Alias(id="ryan", to="123456abcde")})
    public void multiAlias() {}

    @Kissmetrics(set = @Set(id = "ryan", props = "${props}"))
    public void setKissProps() {}

    @Kissmetrics(set = @Set(id = "ryan", props = "${hashProps}"))
    public void setHashProps() {}

    @Kissmetrics(set = {@Set(id = "ryan", key = "purchased", value = "${testString}"),
                        @Set(id = "arin", key = "purchased", value = "${testString}")})
    public void setMultiUser() {}

    @Kissmetrics(set = @Set(id = "ryan", key = "purchased", value = "${testString}"))
    public void setKeyValProps() {}

    @Kissmetrics(record = @Record(id = "ryan", event = "purchased"))
    public void simpleRecord() {}

    @Kissmetrics(record = @Record(id = "ryan", event = "purchased", props="${props}"))
    public void recordWithProps() {}

    @Kissmetrics(record = {@Record(id = "ryan", event = "purchased", props="${props}"),
                           @Record(id = "arin", event = "returned", props="${props}")})
    public void multiRecord() {}

    @Kissmetrics(record = {@Record(id = "ryan", event = "purchased", props="${props}"),
                           @Record(id = "arin", event = "returned", props="${props}")},
                 set = @Set(id="ryan", props="${props}"),
                 alias = @Alias(id="ryan", to="ryan@toodo.com"))
    public void multiAnnotate() {}

    public String getTestString() {
        return testString;
    }

    public void setTestString(String testString) {
        this.testString = testString;
    }

    public KissMetricsProperties getProps() {
        return props;
    }

    public void setProps(KissMetricsProperties props) {
        this.props = props;
    }

    public HashMap<String, Object> getHashProps() {
        return hashProps;
    }

    public void setHashProps(HashMap<String, Object> hashProps) {
        this.hashProps = hashProps;
    }
}