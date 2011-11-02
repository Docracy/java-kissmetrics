package com.jeraff.kissmetrics.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class KissMetricsClient {
    private String apiKey;
    private String id;
    private ClientConnectionManager connectionManager;
    private boolean secure;

    public static final String API_HOST = "trk.kissmetrics.com";

    public static final String PROP_API_KEY = "_k";
    public static final String PROP_EVENT_NAME = "_n";
    public static final String PROP_ALIAS_TO = "_n";
    public static final String PROP_IDENTITY = "_p";
    public static final String PROP_TIMESTAMP = "_t";
    public static final String PROP_USER_TIME = "_d";

    public KissMetricsClient() {
    }

    public KissMetricsClient(String apiKey, String id) {
        this.apiKey = apiKey;
        this.id = id;
    }

    public KissMetricsClient(
            String apiKey, String id, ClientConnectionManager connectionManager, boolean secure) {
        this.apiKey = apiKey;
        this.id = id;
        this.connectionManager = connectionManager;
        this.secure = secure;
    }

    //////////////////////////////////////////////////////////////////////
    // API endpoints
    //////////////////////////////////////////////////////////////////////

    public KissMetricsResponse record(String eventName, KissMetricsProperties properties)
            throws KissMetricsException {
        properties.put(PROP_EVENT_NAME, eventName);
        return call(ApiEndpoint.RECORD_EVENT, properties);
    }

    public KissMetricsResponse record(String eventName) throws KissMetricsException {
        return record(eventName, new KissMetricsProperties());
    }

    public KissMetricsResponse set(KissMetricsProperties properties) throws KissMetricsException {
        return call(ApiEndpoint.SET_PROPERTIES, properties);
    }

    public KissMetricsResponse alias(String aliasTo) throws KissMetricsException {
        final KissMetricsProperties props = new KissMetricsProperties().put(PROP_ALIAS_TO, aliasTo);
        return call(ApiEndpoint.ALIAS_USER, props);
    }

    //////////////////////////////////////////////////////////////////////
    // helpers
    //////////////////////////////////////////////////////////////////////

    public KissMetricsResponse call(ApiEndpoint endpoint, KissMetricsProperties properties)
            throws KissMetricsException {
        if (!isReady()) {
            throw new KissMetricsException();
        }

        properties.put(PROP_API_KEY, apiKey).put(PROP_IDENTITY, id)
                  .put(PROP_TIMESTAMP, (System.currentTimeMillis() / 1000L));

        HttpClient httpClient = getHttpClient();
        HttpGet httpget = new HttpGet(constructUrl(endpoint, properties));

        try {
            HttpResponse response = httpClient.execute(httpget);
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);

            KissMetricsResponse kissMetricsResponse = new KissMetricsResponse();
            kissMetricsResponse.setStatus(response.getStatusLine().getStatusCode());

            final Header[] allHeaders = response.getAllHeaders();
            for (int i = 0; i < allHeaders.length; i++) {
                Header header = allHeaders[i];
                kissMetricsResponse.addHeader(header.getName(), header.getValue());
            }

            return kissMetricsResponse;
        } catch (IOException e) {
            httpget.abort();
            throw new KissMetricsException(e);
        }
    }

    private DefaultHttpClient getHttpClient() {
        return connectionManager != null
                ? new DefaultHttpClient(connectionManager)
                : new DefaultHttpClient();
    }

    public String constructUrl(ApiEndpoint endpoint, KissMetricsProperties properties) {
        StringBuilder sb = new StringBuilder();
        if (secure) {
            sb.append("https://");
        } else {
            sb.append("http://");
        }

        return sb.append(API_HOST).append("/").append(endpoint.path()).append("?")
                 .append(properties.getQueryString()).toString();
    }

    public boolean isReady() {
        return id != null && apiKey != null;
    }

    //////////////////////////////////////////////////////////////////////
    // getters and setters
    //////////////////////////////////////////////////////////////////////

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ClientConnectionManager getConnectionManager() {
        return connectionManager;
    }

    public void setConnectionManager(ClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }
}
