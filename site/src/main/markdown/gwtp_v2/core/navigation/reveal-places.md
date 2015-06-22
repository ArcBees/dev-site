# Revealing Places
Revealing places in GWTP can be achieved using many different means. All will have the same effect: display the presenter associated with the requested token and trigger the [lifecycle methods]({{#gwtp.doc.url.lifecycle}}) on that presenter.

1. [Configure Built-in Places](#built-in)
2. [Use the Place Manager](#place-manager)
3. [Use Native Anchors](#anchors)

## Configure Built-in Places {built-in}
In Get Started, we saw how to [initialize the root GIN module]({{#gwtp.doc.url.initialize_gin}}). The `DefaultModule.Builder` class the configuration of 3 built-in name tokens. Each one of these name tokens has to be associated with a `ProxyPlace`. These places are:

- `@DefaultPlace`: this place will be revealed when the user lands on your application without an initial token;
- `@ErrorPlace`: this place will be revealed when the user navigates to a token that doesn't exist within your application;
- `@UnauthorizedPlace`: this place will be revealed when the user navigates to a place blocked by a [Gatekeeper]({{#gwtp.doc.url.gatekeepers}});

Those places will be revealed automatically by GWTP when appropriate. Alternatively, as described in the next section, the Place Manager can be used to reveal them manually.

## Use the Place Manager {place-manager}
The *Place Manager* can be used to programmatically build a `PlaceRequest` for an existing place and then reveal it. Once built, `PlaceRequest`s are immutable and the values cannot be changed. You can call `new PlaceRequest.Builder(otherRequest)` to create a place request based on another request. Then optionally add or remove parameters and finally call `build()`.

```java
@Inject
CarsPresenter(
        EventBus eventBus,
        MyView view,
        MyProxy proxy,
        PlaceManager placeManager) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);

    this.placeManager = placeManager;
}

@Override
public void displayCar(String model) {
    PlaceRequest placeRequest = new PlaceRequest.Builder()
            .nameToken(NameTokens.CARS)
            .with(ParameterTokens.MODEL, model)
            .build();

    placeManager.revealPlace(placeRequest);
}
```

This will create a new request with the token `CARS` and a parameter `MODEL` with the value `model`. `placeManager.revealPlace()` will then reveal the associated presenter. Alternatively, `placeManager.revealPlace(request, false)` can be used to reveal the presenter without updating the URL. This can be useful if when a new entry in the browser history is not wanted.

The place manager also has a couple utilities methods. The most common methods are:

- `PlaceManager.getCurrentPlaceRequest()`: This will return a `PlaceRequest` corresponding to the currently revealed presenter. It will contain the name token and all parameters;
- `PlaceManager.revealDefaultPlace()`: This will reveal the `@DefaultPlace` mentioned in the [first section](#built-in);
- `PlaceManager.revealCurrentPlace()`: This will reveal the place associated with the current name token or the `@DefaultPlace` if there are none. Mostly, this will be used inside a [Bootstrapper]({{#gwtp.doc.url.bootstrapper}}) when the application is ready to be revealed.

## Use Native Anchors {anchors}
It is not always wanted to reveal places programmatically. As such, GWTP actually listens to changes in the hash. That's why *it just works* when someone manually changes the hash in the browser's URL bar. Following this behaviour, one could manually set the href of an anchor to the desired name token.

From a UiBinder file, this would look like this:

```xml
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
        xmlns:g="urn:import:com.google.gwt.user.client.ui">
    <ui:import field="com.roler.res.client.NameTokens.CARS"/>

    <g:HTMLPanel>
        Hello there. See our new car models
        <g:InlineHyperlink targetHistoryToken="{CARS}">right here</g:InlineHyperlink>
        or
        <a href="#{CARS}">here</a>!
    </g:HTMLPanel>
</ui:UiBinder>
```

Notice how the `<ui:import field="..."/>` tag is added to reused the constant defined in `NameTokens`.
