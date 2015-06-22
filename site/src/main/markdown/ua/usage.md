Inject Analytics into the class you want to track events from.

**REMEMBER TO CALL GO()**

Analytics uses a version of the builder pattern where you call the type of tracking method you want, then chain the options you need to the call followed by go().

eg:

```java
analytics.sendPageView().go(); //track a pageview
analytics.sendPageView().documentPath("/foo").go(); //track a pageview for page /foo
analytics.sendEvent("button", "click").eventLabel("my label").go(); //send event with label
```

If you want to change the global settings call setGlobalSettings in the same way. 

```java
analytics.setGlobalSettings().anonymizeIp(true).go(); //anonymize ips
```
