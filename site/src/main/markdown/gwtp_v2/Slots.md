# Presenter Slots

Presenters in GWTP have a concept of slots. Slots are basically placeholder where the child presenters are put and associated to that parent presenter. GWTP is using setInSlot in it's hierarchy while revealing the presenters and a recursive strategy is used to call each lifecycle methods in the nested presenters hierarchy defined.

## Note on new slots
Typed slots are new in 1.5. If you are using an older version, make sure to read the [slots section](resources/Migrating-from-14-to-15.md#Slots) of the migrating from 1.4 to 1.5 guide.


## Slot types
There are different slot types depending on the intended use.
* [SingleSlot](#SingleSlot)
* [PermanentSlot](#PermanentSlot)
* [NestedSlot](#NestedSlot)
* [Slot](#Slot)
* [OrderedSlot](#OrderedSlot)
* [PopupSlot](#PopupSlot)

### SingleSlot
Slot that can take only one presenter.  Can call getChild(slot) to see what's in the slot.

### PermanentSlot
Same as `SingleSlot` but once a presenter goes in it can never be removed. Means that `getChild(slot)` will never be null.

### NestedSlot
Same as `SingleSlot` but can only take Presenters that have Proxies (no `PresenterWidget`).

### Slot
The most permissive slot, can contain multiple presenters. Can call getChildren(slot) to see which presenters it currently contains.

### OrderedSlot
Like `Slot` except the presenters you put in it must be comparable.  The view will automatically put them in order for you but you can only bind them to an IndexedPanel. `getChildren(slot)` returns the presenters the slot contains in order.

### PopupSlot
You can still use addToPopupSlot if you like but this allows you to also call getChildren(slot) if you want to see what's in your popup slot.


## Using slots
If you are adding ChildPresenter to a slot in CurrentPresenter, you can:
1. `addToSlot()`: Will append the presenter to the slot.
2. `setInSlot()`: Will replace existing presenter(s) in the slot.

If you are setting CurrentPresenter in ParentPresenter, set the slot in the constructor, like this:
```
@Inject
HomePagePresenter(
    EventBus eventBus,
    MyView view,
    MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
```


## Which slot to use?
If you want to set a single child presenter in a slot, use either `SingleSlot`, `PermanentSlot` or `PopupSlot` and set it using `setInSlot()`.

If you need multiple presenters in a slot, use either `Slot` or `OrderedSlot` and set them using `addToSlot()`.

If you want to expose a slot so that child can set themselves in it, use a nested slot and let the child set it in its constructor. A common use of this is make a root page (containing menus, header, footer, etc.) and expose a CONTENT slot.


## Setting the slot in the view
You have to bind your slot to a widget in the view. For most use cases, you can simply use the `bindSlot()` method:
```
@UiField
SimplePanel searchPanel;

@Inject
SomeView(
        Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(SomePresenter.SLOT_SEARCH, searchPanel);
}
```

If you need to do something when adding/setting the slot, you will need to override the `addToSlot()` or `setInSlot()` method in the view. As a simple example, let's say you want to add an `<hr>` tag before each presenter added in the slot, you would do something like this:
```
@Override
public void addToSlot(Object slot, IsWidget content) {
    if (slot == SomePresenter.SLOT_EXTENSIONS) {
        extensions.add(new HTML("<hr>"));
        extensions.add(content);
    } else {
        super.addToSlot(slot, content);
    }
}
```

Make sure to call `super()` so that you can continue to use the `bindSlot()` method for slots that don't require additional handling.





#Presenter Types
GWTP has a few different ways to setup presenters, but each presenter type uses the slot methods the same.

##Nested or Layout Presenters
The nested or layout presenters direct the application layout as in the header, footer and content. Layout presenters which can be nested in many different ways communicate via the Eventbus. The `@NameToken` can be used with the PlaceManager to handle url navigation. Please note that both parent and child presenters in this case are `com.gwtplatform.mvp.client.Presenter`

* Layout presenters (parent) initialize the slot by using the @ContentSlot annotation. See how it is used [here](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationPresenter.java).

```
@ContentSlot
public static final Type<RevealContentHandler<?>> SLOT_MAIN_CONTENT = new Type<RevealContentHandler<?>>();
```

* [Child presenter](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/home/HomePagePresenter.java) can specify its slot through its call to super constructor.

```
@Inject
HomePagePresenter(EventBus eventBus,
    MyView view,
    MyProxy proxy,
    DispatchAsync dispatcher) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_SetMainContent);
```

* The [parent view](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationView.java) now simply sets the widget to the specified slot.

```
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

```
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

```
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

```
addToPopupSlot(popupPresenter);
```

#Slot Methods

##addToSlot(presenter) - Adding Presenter
Add presenter with in a presenter, define the presenter slot in the parent presenter and override the view's slot method `addToSlot(...)` so that when the slot is added.

* Example of `addToSlot(presenter)` in the presenter:

```
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

```
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

```
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

```
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

```
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

```
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

```
// In the presenter
// Name of the slot
private Object MY_ITEMS_SLOT = new Object();

//...

private void clearChildPresenter() {
  clear(MY_ITEMS_SLOT);
}
```

* And add the `slot == Presenter && content == null` in the view:

```
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




=========================================================================

## Slot Types

A good way to understand these changes is to think of Presenter as acting more like a collection than it did before.  A collection that lets you add, remove, and retrieve the Presenters that you put in it.

You can learn these as you need them. To keep current slot functionality just use the `Slot` class where you used `Object` before:

All Slots are type safe.  So if only one kind of Presenter will go in a slot you can declare `Slot<PresenterWhichGoesInThisSlot>`.  Or `Slot<PresenterWidget<?>>` if lots of different presenters can be put in the slot.

* **SingleSlot** - Slot that can take only one presenter.  Can call getChild(slot) to see what's in the slot.

* **PermanentSlot** - Same as singleslot but once a presenter goes in it can never be removed.  Means that getChild(slot) will never be null.

* **NestedSlot** - Replaces the old ContentSlot annotation and is a kind of SingleSlot but can only take Presenters that have Proxies.

* **Slot** - The most permissive slot, use it like you've always used slots. Can call getChildren(slot) to see which presenters it currently contains.

* **OrderedSlot** - Like Slot except the presenters you put in it must be comparable.  The view will automatically put them in order for you but you can only bind them to an IndexedPanel. getChildren(slot) returns the presenters the slot contains in order.

* **PopupSlot** - You can still use addToPopupSlot if you like but this allows you to also call getChildren(slot) if you want to see what's in your popup slot.




=================================================================================



It's helpful when you call getChild / getChildren and get back the exact Presenter that is in the slot.

If you use a specific Presenter class then it does create tighter coupling between presenters and their children but it's optional.

It's not the only thing I use them for but  one of the most useful things I've found is that the slot on it's own can be used to represent state of a model.  If you create a Slot<ChildPresenter> in the parent then that slot can represent the collection.  So eg removing a child from the slot, removes it from the collection.

---

So lets say I have a model:
```
class Book {
   List<Chapter> chapters;
}
```
To represent that model I can create a BookPresenter with a `Slot<ChapterPresenter>`.
Then instead of having to keep track of the chapters and their ChapterPresenters separately the slot itself can manage the collection.
So to get the current List of chapters you might have a method in BookPresenter:

```
List<Chapter> getChapters() {
   List<Chapter> chapters = new ArrayList<>();
   for (ChapterPresenter chapterPresenter: getChildren(CHAPTER_SLOT)) {
        chapters.add(chapterPresenter.getChapter());
   }
   return chapters;
}
```

And so now if you have a `Delete Chapter` button in your ChapterView all you need to call when that button is clicked is removeFromParentSlot() and your list of chapters will update automatically.