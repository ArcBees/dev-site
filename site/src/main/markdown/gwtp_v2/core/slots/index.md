# Presenter Slots
Presenters in GWTP have a concept of slots. Slots are basically placeholders where the child presenters are put and associated to their parent presenter. GWTP is using `setInSlot()` in it's hierarchy while revealing the presenters and a recursive strategy is used to call each lifecycle methods in the nested presenters hierarchy defined.


## Note on new slots
Typed slots are new in 1.5. If you are using an older version, make sure to read the [slots section](TODO-LINK) of the migrating from 1.4 to 1.5 guide.


## Slot types
There are different slot types depending on the intended use.
* **SingleSlot**: Slot that can take only one presenter.  Can call `getChild(slot)` to see what's in the slot.
* **PermanentSlot**: Same as `SingleSlot`, but once a presenter is added it can never be removed. Means that `getChild(slot)` will never be null.
* **NestedSlot**: Same as `SingleSlot`, but can only take Presenters that have Proxies (no `PresenterWidget`).
* **Slot**: The most permissive slot, can contain multiple presenters. Can call `getChildren(slot)` to see which presenters it currently contains.
* **OrderedSlot**: Like `Slot` except the presenters added to it must be comparable. The view will automatically put them in order but they can only be binded to an IndexedPanel. `getChildren(slot)` returns the presenters the slot contains in order.
* **PopupSlot**: A slot used for popup presenters. See [Popup presenters](TODO-LINK) for more details.


## Using slots
### In the presenter
The slot needs to be defined as a field, like this:
```
static final Slot<PresenterWidget<?>> SLOT_CONTENT = new Slot<>();
```

Then, you have to add/set the presenter(s) that goes in the slot.

If you are adding a ChildPresenter to a slot in CurrentPresenter, use either of these methods:
1. `addToSlot()`: Will append the presenter to the slot.
2. `setInSlot()`: Will replace existing presenter(s) in the slot.

If you are setting CurrentPresenter in ParentPresenter, set the slot in the constructor by using a `NestedSlot`, like this:

```
@Inject
CurrentPresenter(
        EventBus eventBus,
        MyView view,
        MyProxy proxy) {
    super(eventBus, view, proxy, ParentPresenter.SLOT_CONTENT);
}
```

### In the view
You have to bind your slot to a widget in the view. For most use cases, you can simply use the `bindSlot()` method:
```
@UiField
SimplePanel searchPanel;

@Inject
ApplicationView(
        Binder uiBinder) {
    initWidget(uiBinder.createAndBindUi(this));

    bindSlot(ApplicationPresenter.SLOT_CONTENT, searchPanel);
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
It's common to have a base Presenter containing the layout of the application. For our example, we'll have 2 slots: one for the menu and one for the content.
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


## Popup Presenters
The Popup Presenter skips the view's slot methods altogether. When it is used it adds directly to the DOM and
requests displays in the center or location selected.

You can define your own popup slot if you want to use methods like `getChildren(SLOT)`. It works the same way as other slots (with setInSlot/addToSlot). There is also a built-in popup slot that you can use if you only want to show a popup presenter. This slot is only a shortcut, it's not different than a manually created popup slot. You can use this built-in slot with `addToPopupSlot(popupPresenter)`.

See [here](TODO-LINK) for more details on popup presenters.


## Slot Methods

### addToSlot(slot, presenter) - Adding Presenter
Adds the presenter to the slot.
When a presenter is added to a slot, it means that it's added to the parent presenter's hierarchy and lifecycle methods will be called on this child presenter.

### removeFromSlot(slot, presenter) - Removing Presenter
Removes the presenter from the slot.

### setInSlot(slot, presenter) - Setting Presenter
Sets the presenter in the slot. It will replace the previous presenters that were assigned to that slot.

### clearSlot(slot) - Clears Presenters
Removes all presenters from this slot.

### getChildren(slot) - Get Presenters
Returns all children presenters contained in the slot or an empty collection if the slot is empty.
If slot is of type `Slot`, returns a Set.
If slot is of type `OrderedSlot`, returns a sorted List.

### getChild(slot) - Get Presenter
Returns the child presenter contained in the slot or null if the slot is empty.
