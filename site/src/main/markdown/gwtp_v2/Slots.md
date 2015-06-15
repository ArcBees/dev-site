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
### In the presenter
You have to define the slot as a field, like this:
```
static final Slot<PresenterWidget<?>> SLOT_CONTENT = new Slot<>();
```

Then, you have to add/set the presenter(s) that goes in the slot.

If you are adding a ChildPresenter to a slot in CurrentPresenter, use either of these methods:
1. `addToSlot()`: Will append the presenter to the slot.
1. `setInSlot()`: Will replace existing presenter(s) in the slot.

If you are setting CurrentPresenter in ParentPresenter, set the slot in the constructor, like this:
```
@Inject
HomePagePresenter(
        EventBus eventBus,
        MyView view,
        MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_CONTENT);
}
```

### In the view
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


## Layout example
It's common to have a base Presenter containing the layout of the application. For our example, we'll have 2 slots:  one for the menu and one for the content.
```
public static final NestedSlot SLOT_CONTENT = new NestedSlot();

static final PermanentSlot<MenuPresenter> SLOT_MENU = new PermanentSlot<>();
```
*Note that `SLOT_MENU` only needs to be available for the View, so we set it package-private. `SLOT_CONTENT` needs to be available for other presenters, so it's public.*

### Menu
We'll receive it in RootPresenter's constructor:
```
@Inject
RootPresenter(
        EventBus eventBus,
        MyView view,
        MyProxy proxy,
        MenuPresenter menuPresenter) {
    super(eventBus, view, proxy);

    this.menuPresenter = menuPresenter;
}
```

Bind it in `onBind()`:
```
@Override
protected void onBind() {
    setInSlot(SLOT_MENU, menuPresenter);
}
```

And finally bind the slot to a widget in the view:
```
@UiField
SimplePanel menu;

@Inject
RootView(
        Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(RootPresenter.SLOT_MENU, menu);
}
```

### Content
The content is a bit different. Once the `NestedSlot` field is created, you only need to bind it in the view:
```
@UiField
SimplePanel menu;
@UiField
SimplePanel content;

@Inject
RootView(
        Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(RootPresenter.SLOT_MENU, menu);
    bindSlot(RootPresenter.SLOT_CONTENT, content);
}
```

Then, a child presenter will set itself as the content of the slot through its constructor:
```
@Inject
HomePagePresenter(
        EventBus eventBus,
        MyView view,
        MyProxy proxy) {
    super(eventBus, view, proxy, RootPresenter.SLOT_CONTENT);
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