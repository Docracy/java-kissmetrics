package com.jeraff.kissmetrics.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class KissMetricsProperties {
    private static final String ENCODING = "UTF-8";

    private StringBuilder sb = new StringBuilder();

    private void _put(String key, Object value) {
        try {
            sb.append(URLEncoder.encode(key, ENCODING)).append("=")
              .append(URLEncoder.encode(value.toString(), ENCODING)).append("&");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            // todo - deal with this - but it shouldn't ever happen
        }
    }

    public KissMetricsProperties put(String key, String value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, int value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, float value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, long value) {
        _put(key, value);
        return this;
    }

    public KissMetricsProperties put(String key, boolean value) {
        _put(key, value);
        return this;
    }

    public void clear() {
        sb = new StringBuilder();
    }

    public String getQueryString() {
        return sb.toString();
    }
}
