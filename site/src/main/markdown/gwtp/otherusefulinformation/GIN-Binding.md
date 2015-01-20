# GIN Binding

Using GIN dependency injection allows for a loosely decoupled application.

##Reference
[Google GIN](http://code.google.com/p/google-gin/)

## Application Module Bootstrapping
See more about [Bootstrapping or Application Initialization][boot]. The application starts by bootstrapping the first
gin module. Then the other modules can be chained from from it.

* Example of ClientModule. See an example [here](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/gin/ClientModule.java).

```
public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        // Sets up the PlaceManager
        install(new DefaultModule());
        // Sets up the RPC-Dispatch
        install(new DispatchAsyncModule.Builder().build());
        // Sets up the first Gin Module chaining from this.
        install(new ApplicationModule());

        // Sets up a constant for Home
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
        // Instantiates an object on binding.
        bind(CurrentUserDto.class).asEagerSingleton();
    }
}
```

* Example of a chained module. Find it [here](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationModule.java).

```
public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new HomeModule());
        install(new WidgetModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                      ApplicationPresenter.MyProxy.class);
    }
}
```

###DefaultModule
Installing the `DefaultModule` saves you from having to perform all the following bindings.


```
bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
bind(RootPresenter.class).asEagerSingleton();
bind(PlaceManager.class).to(MyPlaceManager.class).in(Singleton.class);
bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
```

## AbstractPresenterModule
Notice how the presenter knows nothing about its view's implementation. This is deliberate as it promotes a loosely coupled architecture. It also means that you have to bind things together. This is achieved via dependency injection using google-gin.

* The bindings need to appear in a class inheriting from AbstractPresenterModule. Here are the bindings you will need for our example.
* However, if you want to replace some of the above by your own custom implementations, feel free to remove the call to install and bind everything manually.

```
public class MyModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(MainPagePresenter.class,
            MainPagePresenter.MyView.class,
            MainPageView.class,
            MainPagePresenter.MyProxy.class);
    }
}
```

[boot]: gwtp/basicfeatures/Bootstrapping-or-Application-Initialization.html "Bootstrapping or Application Initialization"
