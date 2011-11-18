package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class ToadUser {
    private String id;
    private ArrayList<String> aliases = new ArrayList<String>();
    private KissMetricsProperties lastEventProps;
    private HashMap<String, KissMetricsProperties> propsMap = new HashMap<String, KissMetricsProperties>();

    public ToadUser(String id) {
        this.id = id;
    }

    /* set related methods */
    public ToadUser set(KissMetricsProperties props) {
        propsMap.put(id, props);
        return this;
    }

    private KissMetricsProperties getSetProps() {
        if (!propsMap.containsKey(id)) {
            propsMap.put(id, new KissMetricsProperties());
        }

        return propsMap.get(id);
    }

    public ToadUser set(String key, String value) {
        getSetProps().put(key, value);
        return this;
    }

    public ToadUser set(String key, int value) {
        getSetProps().put(key, value);
        return this;
    }

    public ToadUser set(String key, double value) {
        getSetProps().put(key, value);
        return this;
    }

    public ToadUser set(String key, float value) {
        getSetProps().put(key, value);
        return this;
    }

    public ToadUser set(String key, long value) {
        getSetProps().put(key, value);
        return this;
    }

    public ToadUser set(String key, boolean value) {
        getSetProps().put(key, value);
        return this;
    }

    /* record related methods */
    public ToadUser record(String event) {
        KissMetricsProperties eventProps = new KissMetricsProperties();
        lastEventProps = eventProps;
        record(event, eventProps);
        return this;
    }

    public ToadUser record(String event, KissMetricsProperties props) {
        lastEventProps = props;
        propsMap.put(event, props);
        return this;
    }

    /* NOTE: property will be applied to the most recent record event */
    public ToadUser props(String key, String value) {
        lastEventProps.put(key, value);
        return this;
    }

    public ToadUser props(String key, int value) {
        lastEventProps.put(key, value);
        return this;
    }

    public ToadUser props(String key, double value) {
        lastEventProps.put(key, value);
        return this;
    }

    public ToadUser props(String key, float value) {
        lastEventProps.put(key, value);
        return this;
    }

    public ToadUser props(String key, long value) {
        lastEventProps.put(key, value);
        return this;
    }

    public ToadUser props(String key, boolean value) {
        lastEventProps.put(key, value);
        return this;
    }

    /* alias */
    public ToadUser alias(String to) {
        if (!aliases.contains(to)) {
            aliases.add(to);
        }
        return this;
    }

    /* getters */
    public String getId() {
        return id;
    }

    public ArrayList<String> getAliases() {
        return aliases;
    }

    public HashMap<String, KissMetricsProperties> getPropsMap() {
        return propsMap;
    }
}
