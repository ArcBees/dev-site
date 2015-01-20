# URL Parameters

It's often useful to register part of the state of a presenter in the URL, so that a bookmark or navigation can return to the presenter in that given state. GWTP offers native support for such parameters which, by default, will lead to URLs looking like this: `http://domain.tld#!search;q=iphone`

##URL Parameters Encoding
The parameters are encoded like this `#nameToken(;key=value)*` and might look like `#nameToken;key=value;key2=value2`. The Name Token is mandatory when creating a request.

* Make sure your name token doesn't contain the following characters: "`;`", "`=`" or "`/`".

##Restful URL Parameter Encoding
While more fragile to upgrades because parameter order changes could break old links/bookmarks, it is possible to use more Restful URLs such as `http://domain.tld/#/search/iphone/`.

See the [Route-Place-Tokens][rpt] page for details on using these URL's.

##Getting URL Parameters
When the PlaceManager navigates to the presenter the prepareFromRequest(PlaceRequest request) is called. This is the time to get the url parameters.

* Example:

```
@Override
public void prepareFromRequest(PlaceRequest request) {
  super.prepareFromRequest(request);
  String id = request.getParameter("id", "");
}
```

* Example:

```
@Override
public void prepareFromRequest(PlaceRequest placeRequest) {
  super.prepareFromRequest(placeRequest);

  // In the next call, "view" is the default value,
  // returned if "action" is not found on the URL.
  String actionString = placeRequest.getParameter("action", "view");
  action = INVALID_ACTION;
  if( actionString.equals("view") )
    action = ACTION_VIEW;
  else if( actionString.equals("edit") )
    action = ACTION_EDIT;
  else if( actionString.equals("new") )
    action = ACTION_NEW;

  try {
    id = Long.valueOf( placeRequest.getParameter("id", null) );
  } catch( NumberFormatException e ) {
    id = INVALID_ID;
  }

  if( action == INVALID_ACTION || id == INVALID_ID && action != ACTION_NEW ) {
    placeManager.revealErrorPlace( placeRequest.getNameToken() );
    return;
  }
}
```

##Revealing Presenter with an Encoded URL
See the [Revealing a Presenter: Hyperlink](https://github.com/ArcBees/GWTP/wiki/Presenter-Lifecycle) example.

##Creating a PlaceManager Request
This describes how to setup a PlaceRequest with URL Parameters and then navigate to it using the PlaceManager.

* First inject the PlaceManager into the constructor and assign it to a class field.

```
private final PlaceManager placeManager;

@Inject
HomePagePresenter(EventBus eventBus,
                  MyView view, MyProxy proxy,
                  RestDispatch dispatcher,
                  PlaceManager placeManager) {
    super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

    this.dispatcher = dispatcher;
    this.placeManager = placeManager;
}
```

* Then create a request using the `PlaceRequest` Builder with a mandatory NameToken and add parameters using `.with`.

```
private void goSomeWhere() {
    PlaceRequest request = new PlaceRequest.Builder().
        .nameToken("nameToken")
        .with("key", "value")
        .with("key2", "value2")
        .build();
    placeManager.revealPlace(request);
}
```

* Sometimes you already have an existing `PlaceRequest` at hand and only want to add some additional parameters before revealing the place.

```
private void goSomeWhereElse() {
    PlaceRequest request = placeManager.getCurrentPlaceRequest();
    PlaceRequest newRequest = new PlaceRequest.Builder(request)
        .with("newParameter", "newValue")
        .build();
    placeManager.revealPlace(newRequest);
}
```

[rpt]: gwtp/features/Route-Place-Tokens.html "Route Place Tokens"
