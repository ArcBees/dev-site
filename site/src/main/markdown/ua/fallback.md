This feature is still under development.

Analytics may not work for a significant portion of your users because https://www.googleanalytics.com/analytic.js is prevented from loading.  This can happen because the user has disabled 3rd party scripts or the user is in a country that blocks requests to Google properties.

In this case you can proxy your analytics calls via your server by setting a fallback path when installing analytics in your gin module:

```
    install(new AnalyticsModule.Builder("UA-XXXXXXXX-X").setFallbackPath("/collect").build());
```

Then on your server you need to install the AnalyticsProxyModule in addition to the AnalyticsServerModule in your Guice server module:
```
    install(new ServerAnalyticsModule("UA-XXXXXX-X"));
    install(new AnalyticsProxyModule("/collect"));  // this should match the path you set on the client.
```

The fallback will automatically kick in if analytics.js has not loaded within 10 seconds.  If analytics.js is simply slow to load for some reason, the fallback will turn itself off when the load completes.

Some features may not work exactly as normal.  The same limitations for server calls apply to fallback calls.

## Serving from a subdomain.

It is possible to setup analytics proxying to work from a different server from the site your app is served from.

You might do this if you have a large site or you don't want analytics calls to interfere with your primary service.

To ensure the same __ga cookie is available to the proxy you must serve the proxy from a subdomain of your main site:

eg if your site is served from `www.example.com` you can setup your proxy at `analytics.www.example.com`

Then when installing analytics into your client gin module, set the fallback path to be an absolute url that matches the proxy address:

```
    install(new AnalyticsModule.Builder("UA-XXXXXXXX-X").setFallbackPath("https:/analytics.www.example.com/collect").build());
```

