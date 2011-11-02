package com.jeraff.kissmetrics.client;

import java.util.HashMap;

public class KissMetricsResponse {
    private int status;
    private HashMap<String, String> headers = new HashMap<String, String>();

    public KissMetricsResponse(int status, HashMap<String, String> headers) {
        this.status = status;
        this.headers = headers;
    }

    public KissMetricsResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String name, String value) {
        headers.put(name, value);
    }

    public boolean isOK() {
        return status >= 200 || status <= 299;
    }
}
