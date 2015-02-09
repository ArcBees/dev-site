# Migrating from v. 1.3 to 1.4

## Analytics
- The recommended replacement is located in a different artifact at [https://github.com/ArcBees/universal-analytics](https://github.com/ArcBees/universal-analytics).

## Client Action Handlers
- You are recommended to use the more flexible Interceptors.

## GinUiBinder
- GinUiBinder has been removed from the project. The library has been moved here [https://github.com/ArcBees/gwtp-extensions/tree/master/ginuibinder](https://github.com/ArcBees/gwtp-extensions/tree/master/ginuibinder).

## Popup Presenters
- PresenterWidget.addToPopupSlot(child, center): center has no effect anymore, call PresenterWidget.addToPopupSlot(child)
- PopupView.setPosition() and PopupView.center(): Use a PopupPositioner instead.
