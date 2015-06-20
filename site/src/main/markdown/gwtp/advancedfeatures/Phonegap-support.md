# Phonegap Support

Deploying an application to PhoneGap can be challenging while using GWTP-Dispatch since it's configured by default so that the web page used is from the same origin than the server. To help you design applications that can be deployed to a phonegap build, we introduced two new things in GWTP. The first is the ability to prepare a service by changed the serviceEntryPoint just before the call is executed. This is done through prepareService from the DefaultDispatchAsync. The second thing is the addition of PhoneGapDispatchAsync that use that new extension point added to DefaultDispatchAsync.

##Reference
* [GWTP Mobile Sample Project](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-mobile)
* [GWTP Car Store Test Project](https://github.com/ArcBees/GWTP/tree/master/gwtp-carstore)

## How to setup

The first thing to do is to create a .gwt.xml for your Phonegap config to be used only when you want to build for phonegap.

### GWT-RPC
If you use the GWT-RPC dispatcher, you will then need to bind the RemoteServerUrl constant in your Gin Phonegap module as well as to bind the right DispatchAsyncModule:

```java
protected void configure() {
    bindConstant().annotatedWith(RemoteServerUrl.class)
        .to("http://gwtp-carstore.appspot.com/");

    install(new PhoneGapDispatchAsyncModule());
}
```

### REST
If you use the REST dispatcher, you simply need to bind the RestApplicationPath to your remote url instead of relative path in your Gin Phonegap module:

```java
protected void configure() {
    bindConstant().annotatedWith(RestApplicationPath.class)
        .to("http://gwtp-carstore.appspot.com/rest");
}
```

And you're all done! For more information please view [GWTP-Carstore] (https://github.com/ArcBees/GWTP/tree/master/gwtp-carstore) example inside GWTP
