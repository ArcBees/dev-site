You can use Universal Analytics in native apps built with Cordova and other tools.

There are two extra configuration steps:

1. You need to provide a clientId.
  * Since native apps don't have cookies you need to provide a unique id for the user.  You can use `device.uuid` or your own identifier.
  
2.  You need to turn off the CHECK_PROTOCOL task.

So disable autoCreate when creating your module:

```java
install(new AnalyticsModule.Builder("UA-XXXXXXXX-X").autoCreate(false).build());
```

Then in your entry point or bootstrapper(GWTP) call the following:

```java
analytics.create().clientId(UNIQUE_ID_SINCE_COOKIES_ARENT_AVAILABLE).storage(Storage.NONE).go();
analytics.setGlobalSettings().disableTask(Task.CHECK_PROTOCOL).go();
analytics.sendPageView().go();  //recommended tracks the first page view.
```
