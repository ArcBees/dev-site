# Presenter Widget

Sometimes you will need a graphical object that can be instantiated multiple times (like a Widget) but that contain a lot of logic (like a Presenter). Implementing such an object as a Widget would force you to mix logic and UI code in the same class. Implementing it as a Presenter-Proxy-View triplet is not possible because a Presenter is a singleton: it's instantiated only once.

In this case, what you need is PresenterWidget-View pair. PresenterWidgets are simplified Presenters: they have no proxy and they don't need to implement the `revealInParent()` method. As a result, the parent presenter of a PresenterWidget is responsible for creating it and adding it to its view.

You can also use a PresenterWidget instead for a graphical object you do not plan to instantiate multiple times, but that it complex enough to justify a new class. For example, in a IDE-like application, you might want to have a main presenter responsible of the entire screen, but fill the different panes with various PresenterWidgets.

Instantiating a PresenterWidget is the responsibility of its parent. One way to proceed is simply to inject the desired PresenterWidget in its parent presenter's constructor. Then, when this parent is revealed, you can call `setInSlot` passing this PresenterWidget and the slot you want to place it in. Calling `setInSlot` will make sure that this presenter widget's lifecycle methods are correctly called.

## Reference
* [Presenter Widget Example](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/widget/header)

# Create a Presenter Widget
Below is a simple example of a `PresenterWidget`. Find the source [here](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/widget/header).

## Presenter

```
public class HeaderPresenter extends PresenterWidget<HeaderPresenter.MyView> implements
        HeaderUiHandlers {
    public interface MyView extends View, HasUiHandlers<HeaderUiHandlers> {
    }

    public static final Object TYPE_LoginPresenter = new Object();

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

        setInSlot(TYPE_LoginPresenter, loginPresenter);
    }

    @Override
    public void onTestClick() {
        Window.alert("The Presenter says Hi test");
    }
}
```

## View

```
public class HeaderView extends ViewWithUiHandlers<HeaderUiHandlers> implements HeaderPresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, HeaderView> {
    }

    @UiField
    SimplePanel login;

    @Inject
    HeaderView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void setInSlot(Object slot, IsWidget content) {
        if (slot == HeaderPresenter.TYPE_LoginPresenter) {
            login.setWidget(content);
        }
    }

    @UiHandler("test")
    public void onTestClick(ClickEvent event) {
        getUiHandlers().onTestClick();
    }
}
```

## UiBinder

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <ui:style>
    </ui:style>
    <g:HTMLPanel>
        Login:
        <g:Button ui:field="test">Test</g:Button>
        <g:SimplePanel ui:field="login"/>
    </g:HTMLPanel>
</ui:UiBinder>
```

## UiHandlers

```
public interface HeaderUiHandlers extends UiHandlers {
    void onTestClick();
}
```
