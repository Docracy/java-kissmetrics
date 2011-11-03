package com.jeraff.kissmetrics.client;

public enum ApiEndpoint {
    RECORD_EVENT {
        public String path() {
            return "/e";
        }
    },
    SET_PROPERTIES {
        public String path() {
            return "/s";
        }
    },
    ALIAS_USER {
        public String path() {
            return "/a";
        }
    };

    public abstract String path();
}
