# work in progress

this is the start of a basic java client for kiss metrics...
its backed by AsyncHttpClient from https://github.com/sonatype/async-http-client


All you have to do is instanciate a KissMetricsClient - set the user's identity, set your api key then start making calls.

## simple use
    // since a AsyncHttpClient isn't passed in to KissMetricsClient it'll just make one
    KissMetricsClient client = new KissMetricsClient(apiKey, userIdentity);
    client.record("loggedin");
    client.record("purchased", new KissMetricsProperties().put("item", "latte"));
    client.alias("some alias for the dude");

## provide your own AsyncHttpClient

if you want to use connection pooling a proxy etc then you can pass in your own AsyncHttpClient.
more info can be found at https://github.com/sonatype/async-http-client (links at the top of their README).
but the basic idea would be something like this:

    Builder builder = new AsyncHttpClientConfig.Builder();
    builder.setCompressionEnabled(true)
        .setAllowPoolingConnection(true)
        .setRequestTimesout(30000)
        .build();
    AsyncHttpClient httpClient = new AsyncHttpClient(builder.build());
    KissMetricsClient client = new KissMetricsClient(apiKey, userIdentity, httpClient, false);
    client.alias("newAliashere");
    client.record("loggedin");

## its async...

as a result the record(), set() & alias() methods do NOT return the result of the API call to KissMetrics.
those method calls are fluent, so they actually return `this` which allows u to do stuff like:

    client.alias("newAliashere")
        .record("loggedin")
        .record("eat poop");

if you want the result of the HTTP call to KISS metrics you have to call `getResponse()` on the client.

    KissMetricsResponse resp = client.record("loggedin").getResponse();

our KissMetricsResponse class will get you the http status code and all the headers.
we dont bother storing the body b/c every time i've checked out the body its a GIF.


# unit tests

they're actually functional tests... nothing's mocked.
run the tests with -DKISS_API=[your_api_key]
you can use the Makefile too

    make KISS_API=<YOUR_API_KEY> test