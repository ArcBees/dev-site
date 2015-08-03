# Creating places: Presenter Proxy
A *Place* represents a particular 'bookmark' or location inside the application. A Place is stateful - it may represent a location with it's current settings, such as a particular ID value, or other unique indicator that will allow a user to track back to that location later, either via a browser bookmark, or by clicking the 'back' button.

## Proxy
Proxies are light-weight classes whose size doesn't depend on the complexity of the underlying Presenter and View. They are instantiated as soon as the application loads and are responsible for listening to any event that would require their associated Presenter and View to be created. Proxies are the key to a fast MVP web application, they enable code splitting and lazy instantiation of the largest part of your code.

Chances are you will never have to write a Proxy class. That's because most of the time Proxies can be created automatically by GWT's generators. All you have to do is use special annotations above your `MyProxy` interface.

## CodeSplit
The first annotation you will need is either `@ProxyStandard` or `@ProxyCodeSplit`, depending on whether or not you want your Presenter and View to sit behind a split point (see [here](http://code.google.com/webtoolkit/doc/latest/DevGuideCodeSplitting.html) for details on GWT's code splitting feature).

## NameToken
The second annotation you might want to use is the `@NameToken("MyPlaceName")`, so that the current page can use the browser history. This will let you navigate to the current Presenter by entering the name token in the URL, or via an Hyperlink widget. This will also let you use the back and forward button of your browser to navigate in your application.

## Proxy vs ProxyPlace
A *ProxyPlace* is a Proxy that is also a *Place*. It is used when a Presenter should be associated to a Place. Here's an example:

```java
@ProxyCodeSplit
@NameToken(NameTokens.MAIN)
public interface MyProxy extends ProxyPlace<MainPagePresenter> {
}
```

A *Proxy* alone is often used on a Presenter that is _not_ a Place and is _not_ a PresenterWidget. For instance, a root Presenter used as a layout for an application would use a Proxy instead of a ProxyPlace.

Example:

```java
public class RootPresenter extends Presenter<RootPresenter.MyView, RootPresenter.MyProxy> {
    @ProxyStandard
    interface MyProxy extends Proxy<RootPresenter> {
    }

    ...
}
```

## Notifying the user of code split requests
If you'd like to notify the user when an asynchronous request is performed by GWTP as a result of code splitting, you can do it by listening to the following events:

```java
AsyncCallStartEvent
AsyncCallSuccessEvent
AsyncCallFailEvent
```

For example, you can display a `Loading...` message when the first event is handled, and clear it when one of the other two is received. Check out the [events]({{#gwtp.doc.url.events_home}}) for details.

## Attaching events to proxies
Proxy Events are often used to respond to custom events before a Presenter has been initialized (see [Proxy Events]({{#gwtp.doc.url.proxy-events}}) for more details).
