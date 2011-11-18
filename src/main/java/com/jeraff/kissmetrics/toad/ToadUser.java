package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsProperties;

import java.util.ArrayList;
import java.util.HashMap;

public class ToadUser {
    private String id;
    private ArrayList<String> aliases;
    private HashMap<String, KissMetricsProperties> propsMap;

    public ToadUser(String id) {
        this.id = id;
    }

    public ToadUser set(KissMetricsProperties props) {
        propsMap.put(id, props);
        return this;
    }

    public ToadUser record(String event, KissMetricsProperties props) {
        propsMap.put(event, props);
        return this;
    }

    public ToadUser alias(String to) {
        if (!aliases.contains(to)) {
            aliases.add(to);
        }
        return this;
    }

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
