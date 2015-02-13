Universal Analytics supports the default Analytics plugins.

To use a plugin disable auto create when installing Analytics in your client gin module:

`install(new AnalyticsModule.Builder("UA-XXXXXXXX-X").autoCreate(false).build());`

Then in your EntryPoint or Bootstrapper (GWTP) enable the plugins you want before making any other calls:

```
analytics.create().go();
analytics.enablePlugin(AnalyticsPlugin.DISPLAY); // (optional) Provides demographics information.
analytics.sendPageView().go(); // (recommended) track the initial pageview
```

Some plugins need to be enabled in the Google Analytics Console as well.

Currently supported plugins:

* AnalyticsPlugin.DISPLAY: Provides demographics information.

* AnalyticsPlugin.ENHANCED_LINK_ATTRIBUTION: Provides extra information on which links in your app are clicked on.

**Analytics Plugins can be created by third parties.  If you find a plugin that you think we should support please let us know.**