# Presenter Slots

Presenters in GWTP have a concept of slots. Slots are basically placeholder where the child presenters are put and associated to that parent presenter. GWTP is using setInSlot in it's hierarchy while revealing the presenters and a recursive strategy is used to call each lifecycle methods in the nested presenters hierarchy defined.

#Presenter Types
GWTP has a few different ways to setup presenters, but each presenter type uses the slot methods the same.

##Nested or Layout Presenters
The nested or layout presenters direct the application layout as in the header, footer and content. Layout presenters which can be nested in many different ways communicate via the Eventbus. The `@NameToken` can be used with the PlaceManager to handle url navigation. Please note that both parent and child presenters in this case are `com.gwtplatform.mvp.client.Presenter`

* Layout presenters (parent) initialize the slot by using the @ContentSlot annotation. See how it is used [here](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationPresenter.java).

```java
@ContentSlot
public static final Type<RevealContentHandler<?>> SLOT_MAIN_CONTENT = new Type<RevealContentHandler<?>>();
```

* [Child presenter](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/home/HomePagePresenter.java) can specify its slot through its call to super constructor.

```java
@Inject
HomePagePresenter(EventBus eventBus,
    MyView view,
    MyProxy proxy,
    DispatchAsync dispatcher) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
```

* The [parent view](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationView.java) now simply sets the widget to the specified slot.

```java
@Override
public void setInSlot(Object slot, IsWidget content) {
    if (slot == ApplicationPresenter.SLOT_HeaderPresenter) {
        header.setWidget(content);
    } else if (slot == ApplicationPresenter.SLOT_SetMainContent) {
        main.setWidget(content);
    } else {
        super.setInSlot(slot, content);
    }
}
```

##Child Widget Presenters
A slot can also be used to embed a widget presenter (child) in a layout presenter (parent). A more generic way would be to inject the child widget in parent and call a setter in parent's view to set the child widget. We can instead use slots to keep presenter and view decoupled. The child in this case can be either of `com.gwtplatform.mvp.client.PresenterWidget` and `com.gwtplatform.mvp.client.Presenter` while the parent can only be `com.gwtplatform.mvp.client.Presenter`.

* In [parent presenter](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/widget/header/HeaderPresenter.java), create a slot object. Inject the child widget. 
* Use a slot method to render the presenter.

```java
// slot name
public static final Object LOGIN_SLOT = new Object();

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
```

* The [parent view](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/widget/header/HeaderView.java) simply handles how the slot defined in the parent should be placed.

```java
@UiField
SimplePanel login;

@Override
public void setInSlot(Object slot, IsWidget content) {
    if (slot == HeaderPresenter.SLOT_LoginPresenter) {
        login.setWidget(content);
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
