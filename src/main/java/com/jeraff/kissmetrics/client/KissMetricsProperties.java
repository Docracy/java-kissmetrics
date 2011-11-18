package com.jeraff.kissmetrics.client;

import com.jeraff.kissmetrics.client.util.URLUTF8Encoder;

public class KissMetricsProperties {
    private StringBuilder sb = new StringBuilder();

    private void _put(String key, Object value) {
        sb.append(URLUTF8Encoder.encode(key)).append("=").append(URLUTF8Encoder.encode(value.toString()))
          .append("&");
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

    public KissMetricsProperties put(String key, double value) {
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

    public KissMetricsProperties putSafe(String key, String value) {
        final int starts = sb.indexOf(key + "=");

        if (starts != -1) {
            final int ends = sb.indexOf("&", starts) + 1; // + 1 to remove the & sign too
            sb.delete(starts, ends);
        }

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
