package com.jeraff.kissmetrics.client;

public interface KissMetricsConnection {
    public void connect() throws ConnectionException;
    public void close() throws ConnectionException;
    public void call(String url, String QueryString) throws APIException;

    public class ConnectionException extends Exception {

    }

    public class APIException extends Exception {

    }
}
