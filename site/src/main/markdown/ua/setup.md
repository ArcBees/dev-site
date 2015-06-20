# How to Setup

Add the following to your pom

```xml
<dependency>
    <groupId>com.arcbees.analytics</groupId>
    <artifactId>universal-analytics</artifactId>
    <version>2.1</version>
</dependency>
```

Add the following to your gwt.xml

```xml
<inherits name="com.arcbees.analytics.Analytics"/>
```

Then in your gin module:

```java
install(new AnalyticsModule.Builder("UA-XXXXXXXX-X").build());
```

##Advanced Install

By default AnalyticsModule.Builder will automatically create a tracker for you.

If you want to set up your tracker manually (eg because you want to use plugins) call:

```java
install(new AnalyticsModule.Builder("UA-XXXXXXXX-X").autoCreate(false).build());
```

Then in your entry point or bootstrapper(GWTP) call the following:

```java
analytics.create().go();
analytics.enablePlugin(AnalyticsPlugin.DISPLAY); // (optional) Provides demographics information.
analytics.sendPageView().go(); // (recommended) track the initial pageview
```
