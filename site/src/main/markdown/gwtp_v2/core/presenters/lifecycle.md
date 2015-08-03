# Lifecycle

A PresenterWidget has multiple phases in its lifecycle. GWTP gives the user access to many handles to execute code during each phases.

Let's see what the phases are, and then describe each of them with a possible use case for each on of them.

<!--- show lifecycle diagram -->
![Diagram](http://i.imgur.com/kwjjGuv.png)


## Presenter's construction
The first phase (which is omitted in this diagram) is the Presenter's construction. This happens when Gin calls the constructor of the Presenter. The only things that usually happen in a Presenter's contructor are:
- Fields initialization
- View's UI handlers initialization

## onXXX()
For all the following lifecycle methods, one should *always* call the corresponding `super.onXXX()` method. Otherwise, the lifecycle chain will be broken.

## onBind()
`onBind()` will be called only once (unless you manually unbind and rebind a PresenterWidget).
A typical use of `onBind()` is to register event handlers and everything expensive in there.

## onReveal()
`onReveal()` will be called when the Presenter is being revealed. In GWTP's vocabulary, this means when a Presenter is added to a `Slot` <!--- TODO: Add link to SLOT documentation ---> object. Therefore, it doesn't necessarily mean that the object will be visible in the DOM. See [this issue](https://github.com/ArcBees/GWTP/issues/541) to understand the disambiguition. This should be overriden when one wants to update something on the Presenter when it's about to be added to a Slot.

## onHide()
This is the opposite of `onReveal()`, called when a Presenter is removed from a Slot.

## onReset()
This happens at the end of each user's navigation request. For example if one needs to execute some code when user navigates to a presenter that's already bound, already added to a Slot, this is the method that needs to be overriden. Be careful though, this is called often, so *nothing too heavy should be executed in this method*.

## Manual reveal
It is frequent that a presenter requires to fetch information from the server before it can be used efficiently. The standard behavior of GWTP is to display the Presenter right away, which will cause information received via an HTTP request to appear with a delay. Sometimes, however, this leads to an unpleasant user experience. For example, it is not natural to see an empty user information form being filled after some delay. In such situations it is possible to reveal a Presenter manually.

Manual reveal gives greater control over the precise moment at which a Presenter is revealed. It can be enabled on any Presenter that uses a ProxyPlace. Simply override the `useManualReveal()` method to return true. Once enabled, the Presenter is responsible to reveal itself. First the Presenter has to override `prepareFromRequest()`. Then there are 2 valid ways :

1- Use `ManualRevealCallback`
```java
@Override
public void prepareFromRequest(PlaceRequest request) {
    super.prepareFromRequest(request); // don't forget this

    // fetch data from the server and use and create a ManualRevealCallback as the callback.
    // this example uses Rest-Dispatch with the ResourceDelegates extension
    carsResource.withCallback(ManualRevealCallback.create(this, new AsyncCallback<Car>() {
            @Override
            public void onFailure(Throwable caught) {
               // show an error message 
            }

            @Override
            public void onSuccess(Car result) {
                // do something with the fetched Car
            }
    })).getCar(carId);
}
```

2- Call directly `ProxyPlace.manualReveal()` on success or `ProxyPlace.manualRevealFailed()` on failure in the overriden `prepareFromRequest(PlaceRequest request)` method. __*IMPORTANT: One of these 2 methods must be called, otherwise the application will remain locked*__.

