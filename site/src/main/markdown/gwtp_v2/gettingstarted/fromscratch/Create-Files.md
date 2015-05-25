## Create the Files

GWTP tries to reduce the amount of boilerplate required for creating new projects. Still, you need to specify your configuration properties at some point. The creating of the required files can be separated into the follwing steps:

1. [Configure the Web Application](#webapp)
1. [Define your name tokens](#nametokens)
1. [Initialize GIN](#gin)
1. [Initialize GWT](#gwt)
1. [Create your first presenters](#presenters)
    1. [Application Presenter](#application-presenter)
    1. [Home Presenter](#home-presenter)

## Configure the Web Application {webapp}
In order to tell the servlet container how your application should be loaded, we need to create two files.

Create **src/main/webapp/WEB-INF/web.xml** and copy-paste the following code inside:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xmlns="http://java.sun.com/xml/ns/javaee"
        xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd"
        version="2.5">
    <welcome-file-list>
        <welcome-file>index.html</welcome-file>
    </welcome-file-list>
</web-app>
```

and create **src/main/webapp/index.html** with the following code:

```html
<!doctype html>
<html>
<head>
    <title>My Project</title>

    <script type="text/javascript" language="javascript" src="MyProject/MyProject.nocache.js"></script>

    <style type="text/css">
        #__gwt_historyFrame {
            position: absolute;
            width: 0;
            height: 0;
            border: 0;
        }
    </style>
</head>

<body>
<iframe src="javascript:''" id="__gwt_historyFrame" tabIndex="-1"></iframe>

<noscript>
    <p>
        Your web browser must have JavaScript enabled in order for this application to display correctly.
    </p>
</noscript>
</body>
</html>
```

Make sure you replace the occurrences of *My Project* with your actual project name.

## Define your name tokens {nametokens}
GWTP requires that your places are associated with a unique token that we call name token. This token is also used to navigate to each unique places. This is good practice to regroup all those tokens in a single class.

Let's create **client/NameTokens.java** and add a token for the home page:

```java
package com.mydomain.myproject.client;

public class NameTokens {
    public static final String HOME = "/";
}
```

> **Note**: To lighten the syntax, the next files will be create relative to **src/main/java/com/mydomain/myproject**. So NameTokens is actually located in **src/main/java/com/mydomain/myproject/client/**.

## Initialize GIN {gin}
GWTP uses GIN to remove the coupling between views and and presenters. To achieve this, we need to create a class with some basic configurations.

Create **client/ClientModule.java** and add the following code to it:

```java
package com.mydomain.myproject.client;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;
import com.mydomain.myproject.client.application.ApplicationModule;

public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new Builder()
                .tokenFormatter(RouteTokenFormatter.class)
                .defaultPlace(NameTokens.HOME)
                .errorPlace(NameTokens.HOME)
                .unauthorizedPlace(NameTokens.HOME)
                .build());

        install(new ApplicationModule());
    }
}
```

> *Note*: Your IDE will likely tell you that *ApplicationModule* does not exist. Don't worry, we are going to create it soon.
> *Note*: Even if you don't have an error place or an unauthorized place, GWTP requires a token for them. Navigate [here]() for more information about error places and unauthorized places.
<!--- TODO: Make sure this link works before release. -->

## Initialize GWT {gwt}
It's time to create our last configuration file and tell GWT how to load our application.

Create **MyProject.gwt.xml** and paste the following code:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit 2.7.0//EN"
        "http://gwtproject.org/doctype/2.7.0/gwt-module.dtd">
<module>
    <inherits name="com.gwtplatform.mvp.MvpWithEntryPoint"/>

    <extend-configuration-property name="gin.ginjector.modules"
            value="com.mydomain.myproject.client.ClientModule"/>

    <source path="client"/>
    <source path="shared"/>
</module>
```

## Create your first presenters {presenters}
Everything is finally wired up together. Before we are done, we need to create our first presenters and views.

### Application Presenter {application-presenter}
As a good practice, the application presenter (or root presenter) is not a place. Your home page, or your product page, will be revealed inside the application presenter. We do this because an application often requires extra components like a header, menu or footer. The application presenter is a good place to display those components.

Create your presenter **client/application/ApplicationPresenter.java**:

```java
package com.mydomain.myproject.client.application;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.mydomain.myproject.client.application.ApplicationPresenter.MyView;
import com.mydomain.myproject.client.application.ApplicationPresenter.MyProxy;

public class ApplicationPresenter extends Presenter<MyView, MyProxy> {
    interface MyView extends View {
    }

    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    public static final NestedSlot SLOT_MAIN = new NestedSlot();

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);
    }
}
```

Create the implementation of its view **client/application/ApplicationView.java**:

```java
package com.mydomain.myproject.client.application;

import com.google.gwt.user.client.ui.SimplePanel;
import com.gwtplatform.mvp.client.ViewImpl;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
    private final SimplePanel main;

    ApplicationView() {
        main = new SimplePanel();

        initWidget(main);
        bindSlot(ApplicationPresenter.SLOT_MAIN, main);
    }
}
```

And finally, configure this presenter / view couple through GIN. Create **client/application/ApplicationModule.java**:

```java
package com.mydomain.myproject.client.application;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.mydomain.myproject.client.application.home.HomeModule;

public class ApplicationModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new HomeModule());

        bindPresenter(ApplicationPresenter.class, ApplicationPresenter.MyView.class, ApplicationView.class,
                ApplicationPresenter.MyProxy.class);
    }
}
```

> **Note**: Is is a good practice to create a GIN module for every packages in your application. This allows you to reduce the visibility of your classes to package-private and avoid unintended uses of those classes.

### Home Presenter {home-presenter}

The last step! The home presenter is what will be displayed when someone first navigate to your application. To do so, we will need to configure the `HOME` token we configured earlier.

Create **client/application/home/HomePresenter.java**:

```java
package com.mydomain.myproject.client.application.home;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.mydomain.myproject.client.NameTokens;
import com.mydomain.myproject.client.application.ApplicationPresenter;

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

> **Note**: A common error for `MyProxy` is to extend `Proxy<>` like we did in `ApplicationPresenter`. When you want to attach a name token to your proxy, you need to extend `ProxyPlace<>`.

Now create its view implementation, **client/application/home/HomeView.java**:

```java
package com.mydomain.myproject.client.application.home;

import javax.inject.Inject;

import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomeView extends ViewImpl implements HomePresenter.MyView {
    HomeView() {
        initWidget(new Label("Hello World!"));
    }
}
```

Finally configure the presenter / view couple in GIN:

```java
package com.mydomain.myproject.client.application.home;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;

public class HomeModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        bindPresenter(HomePresenter.class, HomePresenter.MyView.class, HomeView.class,
                HomePresenter.MyProxy.class);
    }
}
```

## Conclusion
That's it! You created your first GWTP application from nothing. You can now improve it by adding more places, style it, and more. Read the content of [Core (MVP)](core/) for more information on other awesome GWTP features.
<!--- TODO: Make sure this link works before release. -->
