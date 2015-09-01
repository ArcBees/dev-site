# Beginner's Tutorial - Part 1
This tutorial is intended for someone having no prior knowledge of GWTP who wants to get started right away writing some code. This first part will show you how a basic Presenter can interact with a View by using UiHandler and UiBinder. Chances are that you will need to replicate this over and over again for almost every Presenter you create. This tutorial won't go much into the details of explaining how GWTP works (we have excellent documentation for that), but rather how the features interact together.

## Covered features:
Presenter, View, PresenterModule, UiHandler, UiBinder, NameToken and CodeSplit.

## Prerequisites
1. [Generating the project]({{#generating_project}})
1. [Executing the project]({{#executing_project}})

## Application Structure

```
+- application
|   \- home
|      +- HomeModule
|      +- HomePresenter
|      +- HomeUiHandlers
|      \- HomeView
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

## Overview of the main files

* **HomeModule**: This is a [GIN module]({{#gwtp.doc.url.gin_bindings}}) that is used to bind the HomePresenter together.

* **HomePresenter**: This is a child Presenter of ApplicationPresenter. It uses its parent Presenter's [Slot]({{#gwtp.doc.url.slots}}) to reveal itself.

* **HomeView**: This is where HTML and widgets values for the HomeView.ui.xml can be accessed.

* **HomeView.ui.xml**: This is where HTML and widgets are declared for the HomePresenter.

* **ApplicationModule**: This is a GIN module that is used to bind ApplicationPresenter together as well as installing any other sub modules (HomeModule for instance).

* **ApplicationPresenter**: Contains all of the ApplicationPresenter logic.

* **ApplicationUiHandlers**: Used to delegate some of the ApplicationView actions to the ApplicationPresenter usually in response of events.

* **ApplicationView**: This is where HTML and widgets values for the ApplicationView.ui.xml can be accessed.

* **ApplicationView.ui.xml**: This is where HTML and widgets are declared for the ApplicationPresenter.

* **ClientModule**: This is your main GIN module from which all of the child modules are loaded. It is also where the `DefaultPlaceManager` is setup.

* **NameTokens**: Contains String constants identifying your [Places]({{#gwtp.doc.url.proxy}}).

## Views and Presenters
In GWTP, the "View" and "Presenter" terms refer to the [MVP architecture]({{#mvp_architecture}}). The [Presenter]({{#gwtp.doc.url.presenter}}) is where all of the client-side logic should be written (i.e. validation, manipulation to the model layer, etc). The [View]({{#gwtp.doc.url.view}}) only displays what it's told to by the Presenter and should not contain any logic. It takes care of browser specific events and is the only layer aware of the DOM elements.

### View
This is the default View for the project:

```java
import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements HomePresenter.MyView {
    interface Binder extends UiBinder<Widget, HomeView> {
    }

    @Inject
    HomeView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
```

It doesn't look like much at first, but there are a couple of key points that we need to explain before we continue.

* HomeView extends `ViewImpl` which gives us access to the `initWidget()` method. This method will take care of initializing any DOM specific element for you.

* The View needs to implement the interface `MyView` which is contained in the Presenter. This will allow the Presenter to talk to the View using the interface.

* In this tutorial, we use the [UiBinder]({{#uibinder}}) framework to build the UI. This `interface Binder extends UiBinder<Widget, HomeView> {}` is how we tell GWTP to use the UiBinder XML file associated to the View. We will talk about this later with a concrete example.

* GWTP is heavily relying on [google-gin]({{#gin}}) for [dependency injection]({{#dependency_injection}}), thus the presence of the `@Inject` annotation on the constructor method.


### Presenter
In this example, we have what is called a nested Presenter. This means other Presenters can be nested inside a parent Presenter using a [slot mechanism]({{#gwtp.doc.url.slots}}). In this case, the ApplicationPresenter is the root Presenter of the application and HomePresenter is nested inside it.

This is the HomePresenter:

```java
import com.projectname.project.client.application.ApplicationPresenter;
import com.projectname.project.client.place.NameTokens;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> {
    interface MyView extends View {
    }

    @ProxyStandard
    @NameToken(NameTokens.HOME)
    interface MyProxy extends ProxyPlace<HomePresenter> {
    }

    @Inject
    HomePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
    }
}
```

Just like we did in the View, we're going to explain the critical points of a Presenter:

* `extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>` defines the ApplicationPresenter superclass. This means the interfaces `MyView` and `MyProxy` need to be defined into the class. `MyView` is the interface we implemented in the View earlier and `MyProxy` is responsible for listening to any event that would require the Presenter and the View to be created.

* `@ProxyStandard` annotation is used to specify whether or not your Presenter should use GWT's code splitting feature. In this case, code splitting is not used but we could have simply used the `@ProxyCodeSplit` annotation instead of this one to use code split. You can learn more on code splitting [here]({{#gwtp.doc.url.proxy}}).

* `@NameToken` is used to easily refer to a page in your application. Each page you want to navigate to should have a nameToken associated with it. For instance, this page is called home because it should be the first page a user will see. This will also let you use the back and forward button of your browser to navigate in your application. We generally refer to a Presenter having a NameToken as a [Place]({{#gwtp.doc.url.creating-places}}).

* `ApplicationPresenter.SLOT_MAIN` tells the Presenter to *reveal* itself into the ApplicationPresenter's slot using the [slot mechanism]({{#gwtp.doc.url.slots}}).

## UiBinder
The [UiBinder]({{#uibinder}}) framework is a declarative way to declare both your HTML and any GWT specific widgets from XML markup.

Let's take a look back at what we saw earlier in the View section:

```java
interface Binder extends UiBinder<Widget, HomeView> {
}
```

This interface is used to bind the View to its associated UiBinder XML file. In this case, this file:

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
        xmlns:g='urn:import:com.google.gwt.user.client.ui'>

    <g:HTMLPanel>
        <p>Hello world!</p>
    </g:HTMLPanel>
</ui:UiBinder>
```

This will display a simple HTML Panel containing a "Hello World!".

A great way to identify your HTML elements is to use the `ui:field="someField"` attribute. This will allow you to access the values and attributes of any HTML elements or widgets directly from the View.

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
         xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
	    <g:TextBox ui:field="nameField"/>
    </g:HTMLPanel>
</ui:UiBinder>
```

Here we've created a TextBox and identified it with the `ui:field` attribute. To access the TextBox's values and attributes from the View, we need to declare a TextBox type variable with a name corresponding to the value that you gave to the `ui:field` attribute. You also need to annotate the variable with `@UiField` in order to bind it to the HTML element. Like this:

```java
@UiField
TextBox nameField;
```

Then, a nameField attribute could be accessed by using a simple getter method, for instance.

```java
public String getTextFromNameField() {
    return nameField.getText();
}
```

## UiHandler
UiHandlers is a GWTP feature that delegate some of the View actions to the Presenter. A good use case for that would be for handling a click event. The UiHandler will listen to a specific browser event and tell GWTP which method to call when the event is triggered.

### Creating the UiHandler
Here are the 5 things you need to do before using UiHandlers:

* Create a UiHandler interface:

```java
import com.gwtplatform.mvp.client.UiHandlers;

public interface HomeUiHandlers extends UiHandlers {
}
```

* Implement the UiHandler to the Presenter:

```java
public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> implements HomeUiHandlers {
    ...
}
```

* Extend HomePresenter.MyView with `HasUiHandlers<HomePresenter>`:

```java
interface MyView extends View, HasUiHandlers<HomePresenter> {
}
```

* Extend the View with `ViewWithUiHandlers<HomePresenter>` instead of `ViewImpl`:

```java
public class HomeView extends ViewWithUiHandlers<HomePresenter> implements HomePresenter.MyView {
    ...
}
```

* Set the UiHandler for the View:

```java
@Inject
HomePresenter(
        EventBus eventBus,
        MyView view,
        MyProxy proxy) {
    super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);

    getView().setUiHandlers(this);
}
```

Once it's done, we're going to create a button that, when clicked, will send the content of the TextBox to the Presenter.

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>

    <g:HTMLPanel>
        <g:TextBox ui:field="nameField"/>
        <g:Button ui:field="sendButton">Submit</g:Button>
    </g:HTMLPanel>
</ui:UiBinder>

```

Then we declare the method we want the UiHandler to call when the event will be triggered.

```java
public interface HomeUiHandlers extends UiHandlers {
    void onSend(String name);  // The method we want the Presenter to implement.
}
```

### Handling the event
Now that the HomeUiHandlers is created and correctly set, we can bind it to an event. We can do this by using the `@UiHandlers("sendButton")` annotation on a method. In order to listen to a specific event, it must be passed as a method parameter.

```java
@UiHandler("sendButton")
void onSend(ClickEvent event) {
    getUiHandlers().onSend(nameField.getText());
}
```

Our view now looks like this:

```java
import javax.inject.Inject;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;

public class HomeView extends ViewWithUiHandlers<HomePresenter> implements HomePresenter.MyView {
    interface Binder extends UiBinder<Widget, HomeView> {
    }

    @UiField
    TextBox nameField;

    @Inject
    HomeView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("sendButton")
    void onSend(ClickEvent event) {
        getUiHandlers().onSend(nameField.getText());
    }
}
```

Finally, the Presenter needs to implement the method declared in the HomeUiHandlers interface.

```java
@Override
public void onSend(String name) {
    logger.info(name + " got passed from the view to the presenter!");
}
```

## Presenter Module
Modules are used for [GIN Bindings]({{#gwtp.doc.url.gin_bindings}}) which is the process of linking the Presenters to their View. Every Presenter needs to be bound, but we can bind more than one Presenter per module. We usually have one Module per Java package. For instance, we could have something like this:

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

These Presenters would be contained in the same package that could be named "error", for instance.

Currently in our project we have 3 modules: ClientModule, ApplicationModule and HomeModule. The ClientModule is the root of all the other modules. HomeModule is installed in ApplicationModule, which in turn is installed in ClientModule. Modules should respect the same hierarchy as your Java packages.

## Conclusion
You are now able to create a Presenter and its associated View and delegate some of its action to the Presenter using UiHandlers. You also saw how to create HTML elements and access their values from the View using UiBinder. This conclude the first part of this tutorial. In the next part, we will take a look at PresenterWidgets, Gatekeepers, the PlaceManager and RestDispatch.

Stay tuned for the next part!
