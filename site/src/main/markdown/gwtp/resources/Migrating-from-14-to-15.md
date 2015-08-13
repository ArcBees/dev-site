# Migrating from version 1.4 to 1.5

## Slots
For migrating between versions, you can use [gwtp-type-slot-upgrader]({{#gwtp.doc.url.slot_migration_tool}}) or you can do it manually by following these instructions:

* Replace every `@ContentSlot` by `NestedSlot`
* You can replace other slots with the `Slot<PresenterWidget<?>>` type and it will work. However, you should use the right slot type when possible since it will clarify the intent of the slot.
* In the view, if your code looked like this:
```
@Override
public void setInSlot(Object slot, IsWidget content) {
    if (slot == ApplicationPresenter.SLOT_HEADER) {
        header.setWidget(content);
    } else if (slot == ApplicationPresenter.SLOT_CONTENT) {
        main.setWidget(content);
    } else {
        super.setInSlot(slot, content);
    }
}
```
You can simply replace it with:
```
ApplicationView(...) {
	(...)
    bindSlot(ApplicationPresenter.SLOT_HEADER, header);
    bindSlot(ApplicationPresenter.SLOT_CONTENT, content);
}
```
It also works for `addToSlot`. You can still use addToSlot/setInSlot if you need to do more, but make sure you have `super.setInSlot()` in a else clause.
