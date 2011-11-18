package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsClient;
import com.jeraff.kissmetrics.client.KissMetricsException;
import com.jeraff.kissmetrics.client.KissMetricsResponse;

import java.util.HashMap;

public class Toad {
    private KissMetricsClient client;
    private HashMap<String, ToadUser> users = new HashMap<String, ToadUser>();
    private boolean shouldAbort = false;

    public Toad(KissMetricsClient client) {
        this.client = client;
    }

    public ToadUser user(String id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }

        return users.put(id, new ToadUser(id));
    }

    public void abort() {
        shouldAbort = true;
    }

    public void process() {
        if (!shouldAbort) {
            KissMetricsResponse response = null;

            for (ToadUser user : users.values()) {
                try {
                    final String userId = user.getId();
                    client.setId(userId);

                    // do alias calls
                    for (String alias : user.getAliases()) {
                        response = client.alias(alias);
                        if (!response.isOK()) {
                            // TODO: log
                        }
                    }

                    // do set calls
                    if (user.getPropsMap().containsKey(userId)) {
                        response = client.set(user.getPropsMap().get(userId));
                        if (!response.isOK()) {
                            // TODO: log
                        }
                    }

                    // do record calls
                    for (String event : user.getPropsMap().keySet()) {
                        if (event.equals(userId)) {
                            continue;
                        }

                        response = client.record(event, user.getPropsMap().get(event));
                        if (!response.isOK()) {
                            // TODO: log
                        }
                    }
                }
                catch (KissMetricsException e) {
                    // TODO: log
                }
            }

            users.clear();
        }
    }
}
