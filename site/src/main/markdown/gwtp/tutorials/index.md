# Beginner's Tutorial - Part 1
This tutorial is intended for someone having no prior knowledge of GWTP who wants to get started right away writing some code. This first part will show you how a basic Presenter can interact with a View by using UiHandler and UiBinder. Chances are that you will need to replicate this over and over again for almost every Presenter you create. This tutorial won't go much into the details of explaining how GWTP works (we have excellent documentation for that), but rather how the features interact together.


## Covered features:
Presenter, View, PresenterModule, UiHandler, UiBinder, NameToken and CodeSplit.


## Prerequisites
1. [Running a project]({{#gwtp.doc.url.running_project}})
1. TODO: Provide a pom.xml for this tutorial.


## Application Structure

```
src/main/java/com/arcbees/demo/client/
+- application
|   +- ApplicationModule.java
|   +- ApplicationPresenter.java
|   +- ApplicationUiHandlers.java
|   +- ApplicationView.java
|   \- ApplicationView.ui.xml
+- gin
|   \- ClientModule.java
+- place
    \- NameTokens.java
```

## Main files and their purpose

### ApplicationModule
Used to bind everything together using dependency injection.

### ApplicationPresenter
Contains all of your client-side logic.

### ApplicationUiHandlers
Used to delegate some of the View actions to the Presenter usually in response of events.

### ApplicationView
Views handle all the UI-related code for a Presenter.

### ApplicationView.ui.xml
Used to write html in your view.

### ClientModule
This is where you setup your PlaceManager.

### NameTokens
Contains the constants defining your places.


## Views and Presenters
In a GWTP application, all pages correspond to a View-Presenter pair. The Presenter contains the client side logic and the View only display what it is told to by the Presenter. It is often said that the view should be as dumb as possible.

### View
Here's our basic ApplicationView:

```java
public class ApplicationView extends ViewWithUiHandlers<ApplicationUiHandlers> implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @Inject
    ApplicationView(
            Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
```

We're going to break it down into simpler part. First, let's take a look at the class declaration. We have an ApplicationView extending a ViewWithUiHandlers. This specify that ApplicationView will be a View and will also use UiHandlers which we will talk about further in this tutorial. Then the View implements an interface that is declared in the ApplicationPresenter class. This will allow the View to talk to the Presenter.

GWTP is heavily relying on [google-gin](https://code.google.com/p/google-gin/) for [dependency injection](https://en.wikipedia.org/wiki/Dependency_injection), thus the presence of an `@Inject` annotation on the constructor.

### Presenter
This is the ApplicationPresenter:

```java
public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> implements ApplicationUiHandlers {
    @ProxyStandard
    @NameToken(NameTokens.home)
    interface MyProxy extends ProxyPlace<ApplicationPresenter> {
    }

    interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
    }

    private final PlaceManager placeManager;

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, RevealType.Root);

        this.placeManager = placeManager;

        getView().setUiHandlers(this);
    }
}
```

Let's take a look at the class declaration. `Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy>` defines the ApplicationPresenter superclass. This means we have to define the interfaces `MyView` and `MyProxy` inside our class, like this:

```java
interface MyProxy extends ProxyPlace<ApplicationPresenter> {
}
interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
}
```

`@ProxyStandard` annotation is used to specify whether or not your Presenter should use GWT's code splitting feature. In this case, code splitting is not used but we could have simply used the `@ProxyCodeSplit` annotation instead of this one to use code split. You can learn more on [code splitting](http://www.gwtproject.org/doc/latest/DevGuideCodeSplitting.html) at GWT Project website.

`@NameToken` is used to easily refer to a page in your application. Each page you want to navigate to should have a nameToken associated with it. For instance, this page is called home because it should be the first page a user will see. This will also let you use the back and forward button of your browser to navigate in your application. We generally refer to a Presenter having a NameToken as a **Place**.


## UiBinder
UiBinder is a declarative way to declare both your HTML and any GWT specific widgets.

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>Hello World!</g:HTMLPanel>
</ui:UiBinder>
```

This will create a simple HTML panel containing `Hello World!`. By adding the `ui:field="something"` attribute on an HTML element, you can access this element from the application's view. Now we're going to add a TextBox to the panel.

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
         xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
	    <g:TextBox ui:field="nameField"/>
    </g:HTMLPanel>
</ui:UiBinder>
```

Then in your view, you will be able to access the textbox's values and attributes by using accessors. First, you want to declare a variable that will contain your HTML element. You can do this by using `@UiField` annotation on your variable, like this:

```java
@UiField
TextBox nameField;
```

Then, for instance, we could create a simple getter method to retrieve an attribute from nameField.

```java
public String getTextFromNameField() {
    return nameField.getText();
}
```

You are now able to create HTML elements and access their values and attributes from the view like any other fields.


## UiHandler
UiHandlers are used to delegate some of the view events to the Presenter. Remember that what we really want here is to put as much logic as possible in the Presenter. So let's say we add a button to our view that will return the text given in the nameField TextBox. First, we add the new button:

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
         xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
	    <g:TextBox ui:field="nameField"/>
	    <g:Button ui:field="sendButton">Submit</g:Button>  // Our new button !
    </g:HTMLPanel>
</ui:UiBinder>
```

Then to create our UiHandler, we make a new java file that will contain an interface extending UiHandlers. We are going to name our interface and our file `ApplicationUiHandlers`.

```java
import com.gwtplatform.mvp.client.UiHandlers;

public interface ApplicationUiHandlers extends UiHandlers {
    void onSend(String name);  // Method we want our presenter to implement.
}
```

If we go back to the view, we can now declare a method that will return the textBox's value. It's now time to use the `@UiHandlers` annotation that will allow us to bind a ClickEvent to the UiField. Then, we have to specify which type of events we want our method to listen to by passing an Event as parameter.

```java
@UiHandler("sendButton")
void onSend(ClickEvent event) {
    getUiHandlers().onSend(nameField.getText());
}
```

Our view now looks like this:

```java
public class ApplicationView extends ViewWithUiHandlers<ApplicationUiHandlers> implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @UiField
    TextBox nameField;
    @UiField
    Button sendButton;

    @Inject
    ApplicationView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("sendButton")
    void onSend(ClickEvent event) {
        getUiHandlers().onSend(nameField.getText());
    }
}
```

Now let's get back to the ApplicationPresenter. Because our class implements ApplicationUiHandlers, we are able to call Presenter methods from our View. It is also important to set the UiHandler for the View by adding this line to the class constructor:

```java
@Inject
ApplicationPresenter(
    EventBus eventBus,
    MyView view,
    MyProxy proxy,
    PlaceManager placeManager) {
    super(eventBus, view, proxy, RevealType.Root);

    this.placeManager = placeManager;

    getView().setUiHandlers(this);  // This is how we set the UiHandlers for the View
}
```

And then we override the method that will be called when our button is clicked.

```java
@Override
public void onSend(String name) {
    logger.info(name + " got passed from the view to the presenter!");
}
```


## Presenter Module
A module is used to bind everything together. Every Presenter needs to be bound, but we can bind more than one Presenter per module. We usually have one Module per java package. For instance, we could have something like this:

```java
public class ErrorModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(NotfoundPresenter.class, NotfoundPresenter.MyView.class, NotfoundView.class,
                NotfoundPresenter.MyProxy.class);
        bindPresenter(UnauthorizedPresenter.class, UnauthorizedPresenter.MyView.class, UnauthorizedView.class,
                UnauthorizedPresenter.MyProxy.class);
        bindPresenter(AlreadyLoggedInPresenter.class, AlreadyLoggedInPresenter.MyView.class, AlreadyLoggedInView.class,
                AlreadyLoggedInPresenter.MyProxy.class);
    }
}
```

And a package named error, for example, would contain all the Presenters mentioned in the snippet.

For now, let's create a new file named ApplicationModule.java that will contain the class ApplicationModule. Then, we are going to override the configure method.

Your ApplicationModule should now look like this:

```java
public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class, ApplicationPresenter.MyProxy.class);
    }
}
```


## Conclusion
You are now able to create a Presenter and its associated View and delegate some of its action to the Presenter using UiHandlers. You also saw how to create HTML elements and access their values from the View using UiBinder. This conclude the first part of this tutorial. In the next part, we will take a look at PresenterWidgets, Gatekeepers, the PlaceManager and RestDispatch.

[Next part]({{#gwtp.doc.url.beginner_tutorial_part2}})
