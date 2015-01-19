A popup presenter is a standard presenter with a view that extends PopupView.

##Reference
* [Popup Presenter Example](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/infopopup)
* [Add Popup Presenter](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/globaldialog/GlobalDialogSubTabPresenter.java#L79)

##Display a Popup Presenter
The Popup Presenter skips the view's slot methods all together. When it is used it adds directly to the DOM and requests displays in the center or location selected.

* Example of rendering a popup presenter.

```
addToPopupSlot(popupPresenter);
```

* Removing a popup presenter. Calling either of these methods will hide the popup presenter and remove it from it's parent.

```
popupPresenter.removeFromParent();
or
popupPresenter.getView().hide();
```

#Create a Popup Presenter

##Presenter

```
public class InfoPopupPresenterWidget extends PresenterWidget<InfoPopupPresenterWidget.MyView> {
    /**
     * {@link InfoPopupPresenterWidget}'s view.
     */
    public interface MyView extends PopupView {
    }

    @Inject
    InfoPopupPresenterWidget(EventBus eventBus,
                             MyView view) {
        super(eventBus, view);
    }
}
```

##View
The view extends `PopupViewImpl` or `PopupViewWithUiHandlers<PresentersUiHandlers>`.


```
public class InfoPopupView extends PopupViewImpl implements InfoPopupPresenterWidget.MyView {
    public interface Binder extends UiBinder<PopupPanel, InfoPopupView> {
    }

    @Inject
    InfoPopupView(Binder uiBinder,
                  EventBus eventBus) {
        super(eventBus);

        initWidget(uiBinder.createAndBindUi(this));
    }
}
```

##UiBinder

```
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder" xmlns:g="urn:import:com.google.gwt.user.client.ui">
    <ui:style>
        .popup {
            background-color: aqua;
            border: 3px double black;
            padding: 0 15px;
        }
    </ui:style>

    <g:PopupPanel autoHideOnHistoryEventsEnabled="true" autoHideEnabled="true" styleName="{style.popup}">
    </g:PopupPanel>
</ui:UiBinder>
```

## PopupPositioners

GWTP provides a PopupPositioner to automatically position your popup.

By default Popups use the CenterPopupPositioner which displays the Popup in the center of the screen.  Popups will automatically be repositioned when the window is Resized.

There are 3 provided Popup Presenters

* **CenterPopupPositioner** - The default, will center the popup

* **TopLeftPopupPositioner** - Will position the popup at the (x, y) coordinates provided.

* **RelativeToWidgetPopupPositioner** - Will position the popup relative to a widget.

To create your own PopupPositioner extend the PopupPositioner class.

You can set the popupPositioner by calling the super constructor:
```
super(eventBus, myPositioner);
```
or
```
getView().setPopupPositioner(myPositioner);
```

## Custom Reposition.

You can override `onReposition()` in your view to perform any operations before the popup is repositioned.
This is where you should perform any custom styling of your view.
