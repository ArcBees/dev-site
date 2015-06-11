# The base: PresenterWidget
GWT widgets are useful when UI components need to be reused multiple times across an application. GWTP's `PresenterWidget` is a great tool that leverages the power of regular GWT Widgets because it brings a good separation of concern to your UI components. Applications often contain complex UI widgets, and sometimes these elements are so complex that it is best to separate the application logic from the UI logic. This is where it comes handy.

A `PresenterWidget`:

* Should dictate *what* needs to be done to its view, but not *how* it has to be done
* Can be coupled to one or more view implementations
* Is added/removed to/from the DOM via a *SLOT* mechanism
* Can pass data to its views
* Can handle data that is pushed from the views
* Has a powerful and complete *lifecycle*
* Relies heavily on the *Inversion of Control* principle via Dependency Injection
* Helps producing loosely coupled UI components
* Is unit testable with any Java test framework

## Creating a PresenterWidget
Here's an example of a basic `PresenterWidget` and its view. Note that a basic view implementation is given here, but reading *View* and *ViewImpl* is strongly suggested to understand the whole example.

Let's assume `CurrentUserService` is an interface that's implemented by a client side service that performs HTTP requests to some REST API to get information.

*SimplePresenter.java*

```
public class SimplePresenter extends PresenterWidget<SimplePresenter.MyView> {
    interface MyView extends View {
        void displayCurrentUserName(String username);
    }

    private final CurrentUserService currentUserService;

    @Inject
    SimplePresenter(EventBus eventBus,
                    MyView view,
                    CurrentUserService currentUserService) {
        super(eventBus, view);

        this.CurrentUserService = currentUserService;
    }

    @Override
    public void onBind() {
        getView.displayCurrentUserName(currentUserService.getCurrentUsername());
    }
}
```

*SimpleViewImpl.java*

```
public class SimpleView extends ViewImpl implements SimplePresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, SimpleViewView> {
    }

    @UiField
    SpanElement username;

    @Inject
    SimpleView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }
    
    public void displayCurrentUserName(String username) {
        this.username.setText(username);
    }
}
```

*SimpleViewImpl.ui.xml*

```
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
        xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
        Current user's name: <span ui:field="username"></span>
    </g:HTMLPanel>
</ui:UiBinder>
```

As mentioned previously, `SimplePresenter` is loosely coupled to its view implementation, as it tells *what to do* instead of *how to do it*. A possibily that comes from doing this, is letting the developer implement multiple views for the same presenter; the presenter knows what to do, and the multiple views handle its requests specifically.
