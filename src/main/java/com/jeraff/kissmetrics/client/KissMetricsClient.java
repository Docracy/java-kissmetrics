package com.jeraff.kissmetrics.client;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class KissMetricsClient {
    private String apiKey;
    private String id;
    private HttpClient httpClient;
    private boolean secure;
    private boolean useClientTimestamp;

    public static final String API_HOST = "trk.kissmetrics.com";

    public static final String PROP_API_KEY = "_k";
    public static final String PROP_EVENT_NAME = "_n";
    public static final String PROP_ALIAS_TO = "_n";
    public static final String PROP_IDENTITY = "_p";
    public static final String PROP_TIMESTAMP = "_t";
    public static final String PROP_USE_CLIENT_TIME = "_d";

    private static final String SCHEME_HTTS = "https";
    private static final String SCHEME_HTTP = "http";

    public KissMetricsClient() {
    }

    public KissMetricsClient(String apiKey, String id) {
        this.apiKey = apiKey;
        this.id = id;
    }

    public KissMetricsClient(
            String apiKey, String id, HttpClient httpClient, boolean secure) {
        this.apiKey = apiKey;
        this.id = id;
        this.httpClient = httpClient;
        this.secure = secure;
    }

    public KissMetricsClient(String apiKey, String id, HttpClient httpClient) {
        this.apiKey = apiKey;
        this.id = id;
        this.httpClient = httpClient;
    }

    //////////////////////////////////////////////////////////////////////
    // API endpoints
    //////////////////////////////////////////////////////////////////////

    public KissMetricsResponse record(String eventName, KissMetricsProperties properties)
            throws KissMetricsException {
        properties.putSafe(PROP_EVENT_NAME, eventName);
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

        if (useClientTimestamp) {
            properties.put(PROP_USE_CLIENT_TIME, 1);
        }

        final URL url = constructUrl(endpoint, properties);
        HttpGet httpget = new HttpGet(url.toString());

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

    public URL constructUrl(ApiEndpoint endpoint, KissMetricsProperties properties)
            throws KissMetricsException {
        String scheme = secure
                ? SCHEME_HTTS
                : SCHEME_HTTP;

        try {
            URI uri = new URI(scheme,
                    null,
                    API_HOST,
                    getPort(),
                    endpoint.path(),
                    properties.getQueryString(),
                    null);

            URL url = uri.toURL();
            return url;
        } catch (Exception e) {
            throw new KissMetricsException("couldn't create uri", e);
        }
    }

    private int getPort() {
        return secure
                ? 443
                : 80;
    }

    public boolean isReady() {
        return id != null && apiKey != null && httpClient != null;
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

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public boolean isUseClientTimestamp() {
        return useClientTimestamp;
    }

    public void setUseClientTimestamp(boolean useClientTimestamp) {
        this.useClientTimestamp = useClientTimestamp;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
