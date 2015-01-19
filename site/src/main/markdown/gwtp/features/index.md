# Presenter View Creation

A typical view could consist of two classes, one UiBinder and maybe one gin module.

* A package might look something like [this](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-basic/src/main/java/com/arcbees/project/client/application/home):

```
// com.arcbees.project.client.application.home
HomeModule.java
HomePagePresenter.java
HomePageView.java
HomePageView.ui.xml
```

### Creating a View
A view is considered to be a place or page in which a user can go in the application. Creating a view takes 3 parts or Presenter-Proxy-View triplet. In this example I'll reference the classes from the created Archetype. [Source](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-basic/src/main/java/com/arcbees/project/client/application/home)

* Start by creating the UiBinder [HomePageView.ui.xml](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-basic/src/main/java/com/arcbees/project/client/application/home/HomePageView.ui.xml) resource for the widget.

```
<!-- HomePageView.ui.xml -->
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
             xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>Hello World!</g:HTMLPanel>
</ui:UiBinder>
```

* Next setup the view which contains the widget and uses the UiBinder like [this](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-basic/src/main/java/com/arcbees/project/client/application/home/HomePageView.java):

```
// HomePageView.java
package com.arcbees.project.client.application.home;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class HomePageView extends ViewImpl implements HomePagePresenter.MyView {
    interface Binder extends UiBinder<Widget, HomePageView> {
    }

    @Inject
    HomePageView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }
}
```

### Creating the Presenter
Create the presenter in which its purpose is to display the view.

* Create a basic presenter like [this](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-basic/src/main/java/com/arcbees/project/client/application/home/HomePagePresenter.java):

```
// HomePagePresenter.java
package com.arcbees.project.client.application.home;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.arcbees.project.client.application.ApplicationPresenter;
import com.arcbees.project.client.place.NameTokens;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;

public class HomePagePresenter extends Presenter<HomePagePresenter.MyView, HomePagePresenter.MyProxy> {
    public interface MyView extends View {
    }

    @ProxyStandard
    @NameToken(NameTokens.home)
    public interface MyProxy extends ProxyPlace<HomePagePresenter> {
    }

    @Inject
    HomePagePresenter(EventBus eventBus,
                      MyView view,
                      MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN_CONTENT);
    }
}
```
