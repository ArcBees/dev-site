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

```java
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

```java
public class EventsModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bind(ArchetypeObserver.class).in(Singleton.class);
        bind(EventBus.class).annotatedWith(Names.named("ArchetypeEventBus")).to(SimpleEventBus.class).in(Singleton.class);
    }
}
```

* Example of a event used in the Observer:

```java
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

```java
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

```java
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
