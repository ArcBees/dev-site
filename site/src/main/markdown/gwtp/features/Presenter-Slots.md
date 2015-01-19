Presenters in GWTP have a concept of slots. Slots are basically placeholder where the child presenters are put and associated to that parent presenter. GWTP is using setInSlot in it's hierarchy while revealing the presenters and a recursive strategy is used to call each lifecycle methods in the nested presenters hierarchy defined.

#Presenter Types
GWTP has a few different ways to setup presenters, but each presenter type uses the slot methods the same.

##Nested or Layout Presenters
The nested or layout presenters direct the application layout as in the header, footer and content. Layout presenters which can be nested in many different ways communicate via the Eventbus. The `@NameToken` can be used with the PlaceManager to handle url navigation.

* Layout presenters initialize the slot by using the @ContentSlot annotation.
```java
@ContentSlot
public static final Type<RevealContentHandler<?>> SLOT_MAIN_CONTENT = new Type<RevealContentHandler<?>>();
```

* See how it is used [here](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationPresenter.java).
```java
public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> {
    public interface MyView extends View {
    }

    @ProxyStandard
    public interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    @ContentSlot
    public static final Type<RevealContentHandler<?>> MAIN_CONTENT_SLOT = new Type<RevealContentHandler<?>>();
    public static final Object HEADER_SLOT = new Object();

    private final HeaderPresenter headerPresenter;

    @Inject
    ApplicationPresenter(EventBus eventBus,
                         MyView view, MyProxy proxy,
                         HeaderPresenter headerPresenter) {
        super(eventBus, view, proxy, RevealType.Root);

        this.headerPresenter = headerPresenter;
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(MAIN_CONTENT_SLOT, headerPresenter);
    }
}
```

##Child Widget Presenters
The child presenters are children of the layout presenters. Setting up a child presenter requires a slot name which is referenced in the view.

* Example of a child presenter. Set the slot name and use a slot method to render the presenter in the view.
```java
// slot name
public static final Object LOGIN_SLOT = new Object();

@Override
protected void onBind() {
    super.onBind();

    setInSlot(LOGIN_SLOT, loginPresenter);
}
```

* See how it is used [here](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/widget/header/HeaderPresenter.java).
```java
public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView>
        implements HeaderUiHandlers {
    public interface MyView extends View, HasUiHandlers<HeaderUiHandlers> {
    }

    public static final Object LOGIN_SLOT = new Object();

    private final LoginPresenter loginPresenter;

    @Inject
    HeaderPresenter(EventBus eventBus,
                    MyView view,
                    LoginPresenter loginPresenter) {
        super(eventBus, view);

        this.loginPresenter = loginPresenter;

        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        setInSlot(LOGIN_SLOT, loginPresenter);
    }

    @Override
    public void onTestClick() {
        Window.alert("The Presenter says Hi test");
    }
}
```

##Popup Presenters
The Popup Presenter skips the view's slot methods all together. When it is used it adds directly to the DOM and
requests displays in the center or location selected. See more on [Popup Presenter][pp] here.

* Example of rendering a popup presenter.
```java
addToPopupSlot(popupPresenter);
```

#Slot Methods

##addToSlot(presenter) - Adding Presenter
Add presenter with in a presenter, define the presenter slot in the parent presenter and override the view's slot method `addToSlot(...)` so that when the slot is added.

* Example of `addToSlot(presenter)` in the presenter:
```java
// In the Presenter
// Define a slot name for presenter
private Object MY_PRESENTER_SLOT = new Object();

//...

private void displayMyPresenter() {
    // Instantiate presenter, use a factory or inject it the through constructor
    PresenterWidget instantiatedPresenter = //...
    addToSlot(MY_PRESENTER_SLOT, instantiatedPresenter);
}
```

* Example of the `addToSlot(presenter)` in the view:
```java
// In the view
@Override
public void addToSlot(Object slot, IsWidget content) {
    if (slot == ApplicationPresenter.MY_PRESENTER_SLOT) {
        main.setWidget(content);
    } else {
        super.addToSlot(slot, content);
    }
}
```

##removeFromSlot(presenter) - Removing Presenter
When removing a presenter.

* Example in of `removeFromSlot(presenter)` in the presenter:
```java
// In the Presenter
// Define a slot name for presenter
private Object MY_PRESENTER_SLOT = new Object();

//...

private void displayMyPresenter() {
    // Instantiate a presenter, use a factory or inject it the through constructor
    PresenterWidget instantiatedPresenter = null;
    removeFromSlot(MY_PRESENTER_SLOT, instantiatedPresenter);
}
```

* Example of `removeFromSlot(presenter)` in the view:
```java
@Override
public void removeFromSlot(Object slot, IsWidget content) {
    if (slot == ApplicationPresenter.MY_PRESENTER_SLOT) {
        main.remove(content);
    } else {
        super.removeFromSlot(slot, content);
    }
}
```

##setInSlot(presenter) - Adding Presenter
`setInSlot()` is a method used by GWTP in it's lifecycle to set the widget hierarchy that has to be shown to the user. Each time setInSlot is called, it will replace the previous presenter that was assigned to that slot. When a presenter is "replaced", it means that it's removed from the presenter child hierarchy and no lifecycle methods will be called on this removed presenter.

* Example of `setInSlot(presenter)` in the presenter:
```java
// In the Presenter
// Define a slot name for presenter
private Object MY_PRESENTER_SLOT = new Object();

//...

private void displayMyPresenter() {
    PresenterWidget instantiatedPresenter = null;
    setInSlot(MY_PRESENTER_SLOT, instantiatedPresenter);
}
```

* Example of `setInSlot(presenter)` in the view:
```java
// In the view
@Override
public void setInSlot(Object slot, IsWidget content) {
    if (slot == HomePagePresenter.MY_PRESENTER_SLOT) {
        panel.setWidget(content);
    } else {
        super.setInSlot(slot, content);
    }
}
```

##clearSlot(presenter) - Clears Presenters
Clear a child or nested presenter call `clearSlot(slot)`. Remember that calling clearSlot will in turn call setInSlot in the view with a "null" content. This will allow you to remove the widget from a specific panel that is coupled with a slot.

* clearSlot is rarely used and ask yourself the question if the scenario you're in isn't something where a addToSlot and removeFromSlot would be more appropriate.

* Call `clearSlot(presenter)` in the presenter:
```java
// In the presenter
// Name of the slot
private Object MY_ITEMS_SLOT = new Object();

//...

private void clearChildPresenter() {
  clear(MY_ITEMS_SLOT);
}
```

* And add the `slot == Presenter && content == null` in the view:
```java
// In the view
@Override
public void setInSlot(Object slot, IsWidget content) {
    if (slot == HomePagePresenter.MY_ITEMS_SLOT && content == null) {
        panel.clear();
    } else if (slot == HomePagePresenter.MY_ITEMS_SLOT) {
        panel.setWidget(content);
    } else {
        super.setInSlot(slot, content);
    }
}
```

[pp]: gwtp/features/Popup-Presenter.html "Popup Presenter"
