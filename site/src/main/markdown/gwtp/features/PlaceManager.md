##Builtin DefaultPlaceManager
Binding the `DefaultModule()` will initialize the `DefaultPlaceManager`. But it's important to note that the default places have to be bound for `@DefaultPlace`, `@ErrorPlace` and `@UnauthorizedPlace` depending on use of them.

* Example of setting up the `DefaultPlaceManger` through the `DefaultModule`

```
public class ClientModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    // DefaultModule initializes the DefaultPlaceManger
    install(new DefaultModule());
    install(new ApplicationModule());

    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
    bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
    bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.unauthorized);
  }
}
```

##Custom PlaceManager
Setting up a custom `PlaceManager` can be done by setting up the `DefaultModule` with the application's `PlaceManager.class`.

* Example extending `PlaceManagerImpl` to build the application's `PlaceManager`:

```
package com.arcbees.project.client.place;

import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.annotations.DefaultPlace;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class PlaceManager extends PlaceManagerImpl {
    private final PlaceRequest defaultPlaceRequest;

    @Inject
    PlaceManager(EventBus eventBus,
                 TokenFormatter tokenFormatter,
                 @DefaultPlace String defaultPlaceNameToken) {
        super(eventBus, tokenFormatter);
        this.defaultPlaceRequest = new PlaceRequest.Builder().nameToken(defaultPlaceNameToken).build();
    }

    @Override
    public void revealDefaultPlace() {
        // Using false as a second parameter ensures that the URL in the browser bar
        // is not updated, so the user is able to leave the application using the
        // browser's back navigation button.
        revealPlace(defaultPlaceRequest, false);
    }
}
```

* When using a custom `PlaceManager` instantiate the DefaultModule with the `PlaceManager.class` like `DefaultModue(PlaceManager.class)`.

```
public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        // Sets up DefaultPlaceManger in the DefaultModule
        install(new DefaultModule());
        install(new DispatchAsyncModule.Builder().build());
        install(new ApplicationModule());

        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
        bind(CurrentUserDto.class).asEagerSingleton();
    }
}
```

###RevealPlace Recursion
To explain that how revealDefaultPlace works a bit more, a situation that could occur if `revealDefaultPlace` redirected the user and changed the URL would be to make it impossible for the user to go back to where he was before using the back button of his browser. That is, if you're at #!invalid and get redirected to `#!valid` then hitting back gets you back to #!invalid which redirects you again to `#!valid`. To make sure this doesn't happen we use the two parameter variant of revealPlace and pass false as a second parameter so that you get redirected to the page corresponding to `#!valid` but the browser's history is not updated and the URL stays at `#!invalid`.

* Set to false:

```
revealPlace(defaultPlaceRequest, false);
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

##Creating a PlaceManager Request
See [URL Parameters][up]

#Annotations
The `DefaultPlaceManager` has three default annotations that are setup by default. Be sure to
 setup presenters for these places to land.

##@DefaultPlace
The `@DefaultPlace` has been built into the `DefaultPlaceManager`. This will tell the the PlaceManager the first presenter to reveal on initialization.

* The `@DefaultPlace` can be set like this:

```
bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
```

##@ErrorPlace
This is the landing place when a 404 occurs.

* The `@ErrorPlace` can be set like this:

```
bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.error);
```

##@UnauthorizedPlace
The `@UnauthorizedPlace` is the landing place for unauthorized users.

* The `@UnauthorizedPlace` can be set like this:

```
bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.unauthorized);
```

[up]: gwtp/features/URL-Parameters.html "URL Parameters"
