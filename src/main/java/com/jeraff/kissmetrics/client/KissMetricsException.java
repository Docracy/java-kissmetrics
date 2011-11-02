package com.jeraff.kissmetrics.client;

public class KissMetricsException extends Throwable {
    public KissMetricsException(String s, Throwable e) {
        super(s, e);
    }

    public KissMetricsException() {
    }

    public KissMetricsException(String s) {
        super(s);
    }

    public KissMetricsException(Throwable throwable) {
        super(throwable);
    }
}
