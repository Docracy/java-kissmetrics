package com.jeraff.kissmetrics.toad;

import com.jeraff.kissmetrics.client.KissMetricsClient;
import com.jeraff.kissmetrics.client.KissMetricsException;

import java.util.HashMap;

public class Toad {
    private KissMetricsClient client;
    private HashMap<String, ToadUser> users = new HashMap<String, ToadUser>();
    private boolean shouldAbort = false;

    public Toad(KissMetricsClient client) {
        this.client = client;
    }

    private void setupELContext() {

    }

    public ToadUser user(String id) {
        if (users.containsKey(id)) {
            return users.get(id);
        }

        ToadUser user = new ToadUser(id);
        users.put(id, user);
        return user;
    }

    public void abort() {
        shouldAbort = true;
    }

    public Object resolve(String expression) {
        return null;
    }

    public void run() {
        if (!shouldAbort) {
            for (ToadUser user : users.values()) {
                try {
                    final String userId = user.getId();
                    client.setId(userId);

                    // do alias calls
                    for (String alias : user.getAliases()) {
                        client.alias(alias);
                    }

                    // do set calls
                    if (user.getPropsMap().containsKey(userId)) {
                        client.set(user.getPropsMap().get(userId));
                    }

                    // do record calls
                    for (String event : user.getPropsMap().keySet()) {
                        if (event.equals(userId)) {
                            continue;
                        }

                        client.record(event, user.getPropsMap().get(event));
                    }
                }
                catch (KissMetricsException e) {
                    // TODO: log
                }
            }

            users.clear();
        }
    }

    public HashMap<String, ToadUser> getUsers() {
        return users;
    }

    public void setUsers(HashMap<String, ToadUser> users) {
        this.users = users;
    }
}
