# work in progress

this is the start of a basic java client for kiss metrics...
check out KissMetricsClientTest to see how to use the the client.

its backed by AsyncHttpClient from https://github.com/sonatype/async-http-client

All you have to do is instanciate a KissMetricsClient - set the user's identity, set your api key then start making calls.

## example w/o connection manager
    KissMetricsClient client = new KissMetricsClient(apiKey, userIdentity);
    client.setHttpClient(new AsyncHttpClient());
    client.record("loggedin");
    client.record("purchased", new KissMetricsProperties().put("item", "latte"));
    client.alias("some alias for the dude");

## example w/ connection manager

# unit tests

they're actually funcational tests...

run the tests with -DKISS_API=[your_api_key]