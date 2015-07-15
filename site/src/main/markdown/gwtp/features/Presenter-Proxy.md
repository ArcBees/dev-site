# Presenter Proxy

At that point you're probably thinking "Do I really have to create three classes for every page in my application?" Fear not! Chances are you will never have to write a proxy class. That's because most of the time proxys can be created automatically by GWT's generators. All you have to do is use special annotations above your MyProxy interface.

The first annotation you will need is either `@ProxyStandard` or `@ProxyCodeSplit`, depending whether or not you want your presenter and view to sit behind a split point (see [here](http://code.google.com/webtoolkit/doc/latest/DevGuideCodeSplitting.html) for details on GWT's code splitting feature). That's right, using code splitting is that simple! (Although there are some gotchas, see for example [Provider bundles](http://code.google.com/p/gwt-platform/wiki/GettingStarted#Provider_bundles ) below.)

The second annotation you might want to use is the @NameToken( "MyPlaceName" ), so that this page can use the browser history. This will let you navigate to this presenter by entering the name token in the URL, or via an Hyperlink widget. This will also let you use the back and forward button of your browser to navigate in your application.

* In the end, this is how the MyProxy interface should be defined in your presenter:

```java
@ProxyCodeSplit
@NameToken("main")
public interface MyProxy extends ProxyPlace<MainPagePresenter> {}
```

## Notifying the user of code split requests
If you'd like to notify the user when an asynchronous request is performed by GWTP as a result of code splitting, you can do so easily simply by listening to the following events:

```java
AsyncCallStartEvent
AsyncCallSuccessEvent
AsyncCallFailEvent
```
For example, you can display a `Loading...` message when the first event is handled, and clear it when one of the other two is received. Check out the events javadoc for details.

## Custom Proxy
See more on the [Custom Proxy Class][cpc] here.

## Attaching events to proxies
It is often useful to let a presenter respond to custom events even before it has been initialized. To do this it is necessary for the proxy to listen to the events. Then, whenever the proxy receives the event, it should initialize its presenter and forward the call. To make this entire process simple, GWTP provides the `@ProxyEvent` annotation. To use this feature, first define your custom `GwtEvent` class, for example:

```java
public class RevealDefaultLinkColumnEvent extends GwtEvent<RevealDefaultLinkColumnHandler> {

    private static final Type<RevealDefaultLinkColumnHandler> TYPE = new Type<RevealDefaultLinkColumnHandler>();

    public static Type<RevealDefaultLinkColumnHandler> getType() {
        return TYPE;
    }

    public static void fire(HasEventBus source) {
        source.fireEvent(new RevealDefaultLinkColumnEvent());
    }

    public RevealDefaultLinkColumnEvent() {
    }

    @Override
    protected void dispatch( RevealDefaultLinkColumnHandler handler ) {
        handler.onRevealDefaultLinkColumn( this );
    }

    @Override
    public Type<RevealDefaultLinkColumnHandler> getAssociatedType() {
        return getType();
    }
}
```

You will need to provide a static `getType` method in order for the `@ProxyEvent` to work. Once you have the event class, you should provide the handler interface:

```java
public interface RevealDefaultLinkColumnHandler extends EventHandler {
    void onRevealDefaultLinkColumn( RevealDefaultLinkColumnEvent event );
}
```

Make sure that this interface has a single method and that the method accepts only one parameter: the event. Armed with these classes, you can have your presenter handle the event simply by having it implement the `RevealDefaultLinkColumnHandler` interface and by defining the handler method in this way:

```java
@ProxyEvent
@Override
public void onRevealDefaultLinkColumn(RevealDefaultLinkColumnEvent event) {
    // Do anything you want in there. If you want to reveal the presenter:
    forceReveal();
}
```

Calling `forceReveal()` in this way should only be done for leaf presenters that do not have a name token. In the case where the presenter is associated to a place, use a method from the `PlaceManager` instead.

[cpc]: gwtp/advancedfeatures/Custom-Proxy-Class.html "Custom Proxy Class"
