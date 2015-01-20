# Events

Creating events fired on the `EventBus`. This helps decouple widgets and presenters.

###Global event without a data member.

* Example of a `GlobalEvent` without a data member:

```
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class GlobalEvent extends GwtEvent<GlobalEvent.GlobalHandler> {
    public static void fire(HasHandlers source) {
        source.fireEvent(new GlobalEvent());
    }

    public static void fire(HasHandlers source, GlobalEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    public interface HasGlobalHandlers extends HasHandlers {
        HandlerRegistration addGlobalHandler(GlobalHandler handler);
    }

    public interface GlobalHandler extends EventHandler {
        public void onGlobalEvent(GlobalEvent event);
    }

    private static final Type<GlobalHandler> TYPE = new Type<GlobalHandler>();

    public static Type<GlobalHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<GlobalHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(GlobalHandler handler) {
        handler.onGlobalEvent(this);
    }
}
```
* Fire the `GlobalEvent` from any presenter like this:

```
GlobalEvent.fire(this);
```

###Global event with a data member

* Example of a `GlobalDataEvent` with a data member:

```
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class GlobalDataEvent extends GwtEvent<GlobalDataEvent.GlobalDataHandler> {
    public interface HasGlobalDataHandlers extends HasHandlers {
        HandlerRegistration addGlobalHandler(GlobalDataHandler handler);
    }

    public interface GlobalDataHandler extends EventHandler {
        public void onGlobalEvent(GlobalDataEvent event);
    }
    public static void fire(HasHandlers source, String data) {
        source.fireEvent(new GlobalDataEvent(data));
    }

    public static void fire(HasHandlers source, GlobalDataEvent eventInstance) {
        source.fireEvent(eventInstance);
    }

    private static final Type<GlobalDataHandler> TYPE = new Type<GlobalDataHandler>();

    private String data;

    public GlobalDataEvent(String data) {
        this.data = data;
    }

    public static Type<GlobalDataHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<GlobalDataHandler> getAssociatedType() {
        return TYPE;
    }

    public String getData() {
        return this.data;
    }

    @Override
    protected void dispatch(GlobalDataHandler handler) {
        handler.onGlobalEvent(this);
    }
}
```
* Fire the `GlobalDataEvent` from any presenter like this:

```
String data = "Sending some data with the event";
GlobalDataEvent.fire(this, data);
```

### Event Source
Setting the source on fireEvent with your own objects.

* Example of sending the source of the event with the event:

```
public abstract class MyCustomCallback<T> implements AsyncCallback<T>, HasHandlers {
  @Inject
  private static EventBus eventBus;

  @Override
  public void onFailure(Throwable caught) {
      ShowMessageEvent.fire(this, "Oops! Something went wrong!");
  }

  @Override
  public void fireEvent(GwtEvent<?> event) {
      eventBus.fireEvent(this, event);
  }
}
```

`HasHandlers` here is important and also the static injection of the eventBus on your custom object.

##RegisterHandler in Presenters

###Register a `GlobalDataEvent`
Registering handlers can be tightly integrated into the presenter. The handler then becomes one of the method members of the presenter.

* Add `implements GlobalDataHandler` to the class.
* Add the unimplemented methods from GlobalDataHandler.
* Add `addRegisteredHandler(GlobalDataEvent.getType(), this);` to onBind.

* Example of registering a `GlobalDataEvent`:

```
public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> implements GlobalDataHandler {
    public interface MyView extends View {
    }

    @ContentSlot
    static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();
    static final Object TYPE_HeaderPresenter = new Object();

    private final HeaderPresenter headerPresenter;

    @ProxyStandard
    public interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    @Inject
    ApplicationPresenter(EventBus eventBus, MyView view, MyProxy proxy, HeaderPresenter headerPresenter) {
        super(eventBus, view, proxy, RevealType.Root);

        this.headerPresenter = headerPresenter;
    }

    @Override
    public void onGlobalEvent(GlobalDataEvent event) {
        Window.alert("Global Event fired");
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(TYPE_HeaderPresenter, headerPresenter);

        addRegisteredHandler(GlobalDataEvent.getType(), this);
    }
}
```

###Handle an event only when the presenter is visible
Sometimes several presenters listen to a same event. But you may want that when the event fires, only the current visible presenter handles the event. GWTP offers the ability to define handlers that will automatically register when the presenter is revealed and unregister when the presenter is hidden :
 * Use `addVisibleHandler(GlobalDataEvent.getType(), this);` to onBind.

The onBind method of the example above becomes :


```
@Override
protected void onBind() {
    super.onBind();

    setInSlot(TYPE_HeaderPresenter, headerPresenter);

    addVisibleHandler(GlobalDataEvent.getType(), this);
}
```

#The Observer

##Grouped Events Observing
Setting up a `SimpleEventBus` to Observe a specific set of events which can be grouped together and they can be subscribed to by injecting the Observer, registering the event type and adding the handler. For instance this can be useful in a deep child presenter widget in which it wants to fire an menu change event and the menu observes for that event. In the examples below, I took snippets of a Archetype directory widgets in which a selection list item selection causes a display event to happen.

* Setting up an observer takes a few steps which are listed below:
 * Setup the Observer with annotated `EventBus`.
 * Instantiate the Observer through binding.
 * Create some events for the Observer to use.
 * Fire an event.
 * Handle an event.

* Example of the Observer with annotated `EventBus`:

```
public class ArchetypeObserver {
  private final EventBus eventBus;

  @Inject
  ArchetypeObserver(@Named("ArchetypeEventBus") EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void fireEvent(GwtEvent<?> event) {
    eventBus.fireEventFromSource(event, this);
  }

  public final <H extends EventHandler> HandlerRegistration addHandler(GwtEvent.Type<H> type, H handler) {
    return eventBus.addHandler(type, handler);
  }

  public EventBus getEventBus() {
    return eventBus;
  }

  public void display(ArchetypeJso archetypeJso) {
    eventBus.fireEvent(new ArchetypeDisplayEvent(archetypeJso));
  }
}
```

* Example of instantiating the Observer through binding:

```
public class EventsModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    bind(ArchetypeObserver.class).in(Singleton.class);
   bind(EventBus.class).annotatedWith(Names.named("ArchetypeEventBus")).to(SimpleEventBus.class).in(Singleton.class);
  }
}
```

* Example of a event used in the Observer:

```
public class ArchetypeDisplayEvent extends GwtEvent<ArchetypeDisplayEvent.DisplayArchetypeHandler> {
  public interface DisplayArchetypeHandler extends EventHandler {
      void onDisplay(ArchetypeDisplayEvent event);
  }

  private static final Type<DisplayArchetypeHandler> TYPE = new Type<DisplayArchetypeHandler>();

  private final ArchetypeJso archetype;

  public static Type<DisplayArchetypeHandler> getType() {
    return TYPE;
  }

  public ArchetypeDisplayEvent(final ArchetypeJso archetypeJso) {
    this.archetype = archetypeJso;
  }

  public ArchetypeJso get() {
    return archetype;
  }

  @Override
  public Type<DisplayArchetypeHandler> getAssociatedType() {
    return TYPE;
  }

  @Override
  protected void dispatch(DisplayArchetypeHandler handler) {
    handler.onDisplay(this);
  }
}
```

* Example of firing the `ArchetypeDisplayEvent`:

```
public class ArchetypeListPresenter extends PresenterWidget<ArchetypeListPresenter.MyView> implements
    ArchtypeListUiHandlers {
  //...

  // Inject the Observer
  @Inject
  ArchetypeListPresenter(EventBus eventBus, MyView view, ArchetypeObserver archetypeObserver,
      LoginPresenter loginPresenter, ArchetypeJsoDao archetypeJsoDao) {
    super(eventBus, view);

    this.archetypeObserver = archetypeObserver;
    this.archetypeJsoDao = archetypeJsoDao;

    getView().setUiHandlers(this);
  }
  //...

  @Override
  public void gotoEdit(ArchetypeJso selectedArchetype) {
    // Fire the event
    archetypeObserver.display(selectedArchetype);
  }
  //...
}
```

* Example of handling the `ArchetypeDisplayEvent`:

```
// Implement `ArchetypeDisplayEvent.DisplayArchetypeHandler` handler.
public class ArchetypeDisplayPresenter extends PresenterWidget<ArchetypeDisplayPresenter.MyView> implements
    ArchetypeDisplayUiHandlers, ArchetypeDisplayEvent.DisplayArchetypeHandler {
  public interface MyView extends View, HasUiHandlers<ArchetypeDisplayUiHandlers> {
  }

  private final ArchetypeObserver archetypeObserver;
  private final ArchetypeJsoDao archetypeJsoDao;

  // Inject the Observer
  @Inject
  ArchetypeDisplayPresenter(EventBus eventBus, MyView view, ArchetypeObserver archetypeObserver,
      LoginPresenter loginPresenter, final ArchetypeJsoDao archetypeJsoDao) {
    super(eventBus, view);

    this.archetypeObserver = archetypeObserver;
    this.archetypeJsoDao = archetypeJsoDao;

    getView().setUiHandlers(this);
  }

  @Override
  protected void onBind() {
    super.onBind();

    // Register the event
    registerHandler(archetypeObserver.addHandler(ArchetypeDisplayEvent.getType(), this));
  }

  // Event Handler for the event.
  @Override
  public void onDisplay(ArchetypeDisplayEvent event) {
    // TODO do something
  }
}
```
