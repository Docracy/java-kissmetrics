# work in progress

this is the start of a basic java client for kiss metrics...
check out KissMetricsClientTest to see how to use the the client.

its all pretty simple - only twist is that you can pass in a ClientConnectionManager.
if you pass one in the KissMetricsClient class will use it when creating an HttpClient otherwise it'll just
get a client via DefaultHttpClient

so just instanciate a KissMetricsClient - set the user's identity, set your api key then start making calls.

## example w/o connection manager
    KissMetricsClient client = new KissMetricsClient(apiKey, userIdentity);
    client.record("loggedin");
    client.record("purchased", new KissMetricsProperties().put("item", "latte"));
    client.alias("some alias for the dude");

## example w/ connection manager
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));

    ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(schemeRegistry);
    cm.setMaxTotal(200);
    cm.setDefaultMaxPerRoute(20);

    HttpHost kissMetricsHost = new HttpHost(KissMetricsClient.API_HOST, 80);
    cm.setMaxForRoute(new HttpRoute(kissMetricsHost), 50);

    KissMetricsClient client = new KissMetricsClient(apiKey, userIdentity);
    client.setConnectionManager(cm);

    client.record("loggedin");
    client.record("purchased", new KissMetricsProperties().put("item", "latte"));
    client.alias("some alias for the dude");

# unit tests

they're actually funcational tests...

run the tests with -DKISS_API=[your_api_key]