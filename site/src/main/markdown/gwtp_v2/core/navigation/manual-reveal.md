# Manual Reveal (Presenters)
A presenter may require some information from the server before it can be used efficiently. The standard behavior of GWTP is to display the presenter right away, which will cause information received from the server to appear with a delay. However, this does not lead to a pleasant user experience. For example, it is not natural to see an empty form being filled after a couple seconds. In such situations it is possible to use GWTP's manual reveal feature.

## Configure Manual Reveal
Manual reveal gives you greater control over the precise moment at which a presenter is revealed. It can be enabled on any `Presenter` that uses a `ProxyPlace`. In order to us it, `useManualReveal()` must be overridden and return true. Once enabled the only method that will be called is `prepareFromRequest(PlaceRequest)`. From there, the presenter can do anything and has total control over its reveal process.

```java
@Override
public boolean useManualReveal() {
    return true;
}
```

## Reveal the presenter
When the presenter is ready, it is imperative that the *proxy is notified*. This can be done by calling either  `ProxyPlace.manualReveal(Presenter)` or `ProxyPlace.manualRevealFailed()`. This will respectively reveal the current presenter or cancel the navigation to it.

```java
@Override
public void prepareFromRequest(PlaceRequest request) {
    carsResource.withCallback(new AsyncCallback<Car>() {
            @Override
            public void onSuccess(Car car) {
                displayCar(car);
                getProxy().manualReveal(CarPresenter.this);
            }

            @Override
            public void onFailure(Throwable caught) {
                DisplayMessageEvent.fire(CarPresenter.this, "Can't display this car.");
                getProxy().manualRevealFailed();
            }
        })
        .car(request.getParameter(CAR_MODEL, ""))
        .get();
}
```

Because the common use case is quite common, GWTP also provides a `ManualRevealCallback` abstraction that notifies the proxy automatically. `ManualRevealCallback` is usable with either [Rest-Dispatch]({{#gwtp.doc.url.rest_dispatch_home}}) and [RPC-Dispatch]({{#gwtp.doc.url.rpc_dispatch_home}}).

```java
@Override
public void prepareFromRequest(PlaceRequest request) {
    carsResource.withCallback(ManualRevealCallback.create(this,
         new AsyncCallback<Car>() {
             @Override
             public void onSuccess(Car car) {
                 displayCar(car);
             }

             @Override
             public void onFailure(Throwable caught) {
                DisplayMessageEvent.fire(CarPresenter.this, "Can't display this car.");
             }
         }))
        .car(request.getParameter(CAR_MODEL, ""))
        .get();
}
```

**Remember that the Proxy has to be notified with either `ProxyPlace.manualReveal(Presenter)` or `ProxyPlace.manualRevealFailed()`. An application that fails to do so will remain in a locked state and become unusable.**
