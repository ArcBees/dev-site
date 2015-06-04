# Events

There are several ways of writing events but this is based on the GWT way.

## Example of an event and its handler

### Simple event

```
public class SimpleEvent extends GwtEvent<SimpleEvent.SimpleHandler> {
    public interface SimpleHandler extends EventHandler {
        void onSimpleEvent(SimpleEvent event);
    }
 
    public static final Type<SimpleHandler> TYPE = new Type<>();
 
    SimpleEvent() {
    }
 
    public static void fire(HasHandlers source) {
        source.fireEvent(new SimpleEvent());
    }
 
    @Override
    public Type<SimpleHandler> getAssociatedType() {
        return TYPE;
    }
 
    @Override
    protected void dispatch(SimpleHandler handler) {
        handler.onSimpleEvent(this);
    }
}
```

### Carrying data in the events

A common use of events is to pass information between loosely coupled components. Events can be more complex and can carry data. Here's an example :

```
public class ComplexEvent extends GwtEvent<ComplexEvent.ComplexHandler> {
    public interface ComplexHandler extends EventHandler {
        void onComplexEvent(ComplexEvent event);
    }
 
    public static final Type<ComplexHandler> TYPE = new Type<>();
    
    private final String data;
 
    public ComplexEvent(String data) {
        this.data = data;
    }
 
    public static void fire(HasHandlers source, String data) {
        source.fireEvent(new ComplexEvent(data));
    }
 
    @Override
    public Type<ComplexHandler> getAssociatedType() {
        return TYPE;
    }
 
    @Override
    protected void dispatch(ComplexHandler handler) {
        handler.onComplexEvent(this);
    }
}
```

### Fire an event

Let's assume an application structure with Presenter/View couple where UI handlers are set. Imagine there's a button and a textbox in the view, and data has to be sent to another component of the application. The data has to be sent through the presenter via UI handlers on the button click event.
TODO: Add UiHandlers Link

```
public class SimpleView extends ViewWithUiHandlers<SimpleUiHandlers>
        implements SimplePresenter.MyView {
    interface Binder extends UiBinder<Widget, SimpleView> {
    }
 
    @UiField
    TextBox userName;
 
    @Inject
    SimpleView(Binder binder) {
        initWidget(binder.createAndBindUi(this);
    }
 
    @UiHandler("submitButton")
    public void onSubmitClick(ClickEvent event) {
        getUiHandlers().onSubmitButtonClicked(userName.getText()); // Pass the view data to the presenter
    }
}

public class SimplePresenter extends PresenterWidget<MyView>
        implements SimpleUiHandlers {
    public interface MyView extends View, HasUiHandlers<SimpleUiHandlers> {
    }

    @Inject
    SimplePresenter(
            EventBus eventBus,
            MyView view) {
        super(eventBus, view);

        getView().setUiHandlers(this);
    }
 
    @Override
    public void onSubmitButtonClicked(String userName) {
        ComplexEvent.fire(this, userName); // fire the event so another component can handle it
    }
}
```

### Handle an event
If a component wants to handle an event, it has to:

* Implement the Handler class that was defined with an Event.
* Register to the event through an event bus.

In GWTP, the easiest way to handle events in a Presenter is by using `PresenterWidget`'s `addXXXHandler` methods. By doing so, the event registration will be bound to GWTP's lifecycle and unbind the event handlers when presenters are unbound.

* If an handler is registered with `addRegisteredHandler()` the event will be handled until the Presenter is unbound (until the method `unbind()` is called).
* If an handler is registered with `addVisibleHandler()` the event will be handled only if the presenter is visible in GWTP's lifecycle. This means that when the method `onHide()` is called, the visibleHandlers are unregistered.

If a handler wants to handle events from a specific source, an interface called `HasXXXHandlers` should be created. This interface should give a convenience method to any implementer to specify the handling object and the source it's listening to.

Here's a concrete example:

*The interface:*

```
import com.google.web.bindery.event.shared.HandlerRegistration;
 
public interface HasComplexEventHandlers {
    HandlerRegistration addComplexEventHandler(ComplexEvent.ComplexHandler handler, Object source);
}

```

*The implementer (a presenter in this case):*
### Register an event handler

```
// Implement this on a presenter
public HandlerRegistration addComplexEventHandler(ComplexEvent.ComplexHandler handler, Object source) {
    HandlerRegistration hr = getEventBus().addHandlerToSource(ComplexEvent.TYPE, source, handler);
    registerHandler(hr);
    return hr;
}
```

## Proxy Event
In GWTP, it might happen to fire an event and that the Presenter that is supposed to handle it is not yet instantiated. This can happen when CodeSplitting is used. It possible for a proxy to listen for events using Proxy Events. This should be used, when a presenter should be notified of an event even before it is first instantiated.

Often Proxy events are used to reveal an uninstantiated Presenter on an event handling. Imagine a Presenter called `MessagePresenter` that's using code splitting, and therefore is not necessarily always instantiated. Let's say it needs to be revealed when `ShowMessageEvent` is fired. Here's an example of how it can be done:

```
public class MessagePresenter extends Presenter<MessagePresenter.MyView, MessagePresenter.MyProxy> 
        implements ShowMessageHandler {

    @ProxyCodeSplit
    @NameToken(NameTokens.messagePage)
    public interface MyProxy extends ProxyPlace<HomePresenter> {
    }

    public interface MyView extends View {
        void setMessage(String message);
    }

    final private PlaceManager placeManager;

    @Inject
    MessagePresenter(EventBus eventBus, MyView view, MyProxy proxy, PlaceManager placeManager) {
        super(eventBus, view, proxy);
        this.placeManager = placeManager;
    }

    @ProxyEvent // Will be called when ShowMessageEvent is fired.
    @Override
    public void onShowMessage(ShowMessageEvent event) {
        getView().setMessage();

        placeManager.revealPlace(new PlaceRequest(NameTokens.messagePage));
    }
}
```
