In your guice server module:

```
install(new ServerAnalyticsModule("UA-XXXXXX-X"));
```

You can use analytics on the server exactly the same as you do on the client.

Server side calls work via the [Measurement Protocol](https://developers.google.com/analytics/devguides/collection/protocol/v1/)

A filter will automatically fill out the Measurement Protocol required fields for you from the _ga cookie or create the cookie if it doesn't exist.

`setGlobalOptions()` and `enablePlugin()` have no effect on server calls.

If you're using multiple trackers then you should call `create().trackerName("My Tracker").go()` once at the beginning of each request to create your secondary trackers before calling any analytics methods.  All other options sent to create() on the server will be ignored.

If you're setting a custom cookie name on the client then the automatic filter will not work correctly because it assumes that the cookie name is : _ga.  Raise an issue on this project if you need to set the cookie name for some reason.