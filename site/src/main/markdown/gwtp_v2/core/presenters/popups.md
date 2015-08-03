# Popup Presenter
A popup presenter is a standard presenter with a view that extends PopupView.

##Reference
* [Popup Presenter Example](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/infopopup)
* [Add Popup Presenter](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/globaldialog/GlobalDialogSubTabPresenter.java#L79)
* [Popup slot](<!--- TODO: add link to popup slot -->)

##Display a Popup Presenter
The Popup Presenter skips the view's slot methods all together. When it is used it adds directly to the DOM and requests displays in the center or location selected.

* Example of rendering a popup presenter.

```java
addToPopupSlot(popupPresenter);
```

* Removing a popup presenter. Calling either of these methods will hide the popup presenter and remove it from it's parent. _Note that these methods should only be called from the Popup Presenter._

```java
popupPresenter.removeFromParent();
or
popupPresenter.getView().hide();
```

#Create a Popup Presenter

##Presenter

```java
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
The view extends `PopupViewImpl` or `PopupViewWithUiHandlers<PresentersUiHandlers>`. If the content of the view is defined using `UiBinder`, make sure that the type of the host object is a `PopopPanel`.

```java
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

```java
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

GWTP provides a PopupPositioner to automatically position a popup.

By default Popups use the CenterPopupPositioner which displays the Popup in the center of the screen. Popups will automatically be repositioned when the window is Resized.

There are 3 provided Popup Presenters

* **CenterPopupPositioner** - The default, will center the popup

* **TopLeftPopupPositioner** - Will position the popup at the (x, y) coordinates provided.

* **RelativeToWidgetPopupPositioner** - Will position the popup relative to a widget.

To create a PopupPositioner, extend the PopupPositioner class.

The popupPositioner can be set by calling the super constructor:
```java
super(eventBus, myPositioner);
```
or
```java
getView().setPopupPositioner(myPositioner);
```

## Custom Reposition.

To perform any operation before the popup is repositioned, The method `onReposition()` has to be overriden in the popup view.
This is where any custom styling of the Popup view should happen.

