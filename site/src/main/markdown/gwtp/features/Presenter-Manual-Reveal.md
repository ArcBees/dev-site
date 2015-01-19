It is frequent that a presenter requires to fetch information from the server before it can be used efficiently. The standard behavior of GWTP is to display the presenter right away, which will cause information received via RPC to appear with a delay. Sometimes, however, this does not lead to a pleasant user experience. For example, it is not natural to see an empty user information form being filled after some delay. In such situations you might want to use GWTP's manual reveal feature.

##Override useManualReveal
Manual reveal gives you greater control over the precise moment at which your presenter is revealed. It can be enabled on any `Presenter` that uses a `ProxyPlace`. Simply override the `useManualReveal()` method to return `true`. Once enabled you will need to make sure you manually reveal your presenter within it's `prepareFromRequest` method. There are two ways to do this.

* First add this to the Presenter with ProxyPlace.

```
@Override
public boolean useManualReveal() {
  return true;
}
```

##Using ManualRevealCallback
This will allow the callback to reveal the presenter when the callback happens.

* Example using the `ManualRevealCallback`

```
@Override
public void prepareFromRequest(PlaceRequest request) {
  super.prepareFromRequest(request);
  dispatcher.execute( new DelayAction(), ManualRevealCallback.create(this,
      new AsyncCallback<NoResult>() {
        @Override
        public void onSuccess(NoResult result) {
          // Do something with the data
        }
        @Override
        public void onFailure(Throwable caught) {
          // Display an error message
        }
      } ) );
}
```

##Using manualReveal
Alternatively, you can directly call `ProxyPlace.manualReveal()` or `ProxyPlace.manualRevealFailed`.

* Example using `getProxy.manualReveal(ThePresenter.this)`

```
@Override
public void prepareFromRequest(PlaceRequest request) {
  super.prepareFromRequest(request);
  dispatcher.execute( new DelayAction(),
      new AsyncCallback<NoResult>() {
        @Override
        public void onSuccess(NoResult result) {
          // Do something with the data
          getProxy().manualReveal(this);
        }
        @Override
        public void onFailure(Throwable caught) {
          // Display an error message
          getProxy().manualRevealFailed();
        }
      } );
}
```

#FAQ
* Remember that you *must* call one of these two methods, otherwise your application will remain locked and become unusable.
