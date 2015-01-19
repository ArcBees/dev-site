#Simple Nested Sample

##Reference
* [Nested Presenter Slots](https://github.com/ArcBees/GWTP/wiki/Presenter-%22Slots%22)
* [Nested Sample](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-nested)

#Introduction
The goal of this tutorial is to help you get a complete overview using GWTP's Model-View-Presenter architecture. Part one will focus mainly on nested presenters. For a complete example on how to use GWTP in Ecplise, take a look at the GettingStarted tutorial.

After reading this guide, you should get a better understanding of nested presenters and history management.  We also cover presenter lifecycles in more detail.

##Getting the sample application
The sample application for this tutorial can be <a href="http://code.google.com/p/gwt-platform/source/browse/#hg%2Fgwtp-samples%2Fgwtp-sample-nested%2Fsrc%2Fmain%2Fjava%2Fcom%2Fgwtplatform%2Fsamples%2Fnested%2Fclient">found here</a>. You can get it if you install the examples as explained in the [Getting Started](http://code.google.com/p/gwt-platform/wiki/GettingStarted#Getting_the_sample_applications) page.<br />

##Not using !AppEngine?
This sample is created with !AppEngine SDK but doesn't depend on it. If you don't need this feature, you can remove any !AppEngine SDK dependencies inside Eclipse, then remove those files :

```
gwtpnestedsample/war/WEB-INF/appengine-web.xml
gwtpnestedsample/war/WEB-INF/logging.properties
```

If you're not using eclipse, don't forget to remove the dependency to !AppEngine from the build path.

#Getting started
In this section, we're going to create a new skeleton project and do some minor structural preparations.

##Initial setup
Let's take a look at the project tree:

As you can see, there's no server logic at this point. We're only focusing on simple nested presenter logic. Here is a brief description of the various packages:

* `com.gwtplatform.samples.nested.client` contains our entry point and base classes for navigation logic and navigation error handling.
* `com.gwtplatform.samples.nested.client.gin` contains gin's module, gingector definition and annotation.
* `com.gwtplatform.samples.nested.client.presenter` contains every presenter we use.
* `com.gwtplatform.samples.nested.client.ui` contains custom widgets without presenter and compatible with !UiBinder.
* `com.gwtplatform.samples.nested.client.view` contains every presenter's view.

#The sample

##Step 1: Adding dependencies
Adding dependencies to `Gwtpnestedsamples.gwt.xml`

Before starting to write anything, don't forget to add every dependencies we need inside `Gwtpnestedsamples.gwt.xml` :
* See more on [Bootstrapping or Application Initialization][boot].

```
<inherits name="com.google.gwt.uibinder.UiBinder" />
<inherits name="com.google.gwt.inject.Inject" />
<inherits name='com.gwtplatform.mvp.MvpWithEntryPoint'/>
```

Those two lines are used by GWTP proxy generator.

##Step 2: Setting things up
Adding navigation's logic classes and Gin classes definition.

`class com.gwtplatform.samples.nested.client.gin.DefaultPlace`:

```
@BindingAnnotation
@Target( { FIELD, PARAMETER, METHOD })
@Retention(RUNTIME)

public @interface DefaultPlace {}
```

This annotation is used inside our `PlaceManager` implementation to link the default presenter's proxy.

`class com.gwtplatform.samples.nested.client.GwtpnestedsamplePlaceManager`:

```
public class GwtpnestedsamplePlaceManager extends PlaceManagerImpl {
  private final PlaceRequest defaultPlaceRequest;

  @Inject
  public GwtpnestedsamplePlaceManager(
                    final EventBus eventBus,
                    final TokenFormatter tokenFormatter,
                    @DefaultPlace String defaultNameToken) {
    super(eventBus, tokenFormatter);

    this.defaultPlaceRequest = new PlaceRequest.Builder().nameToken(defaultNameToken).build();
  }

  @Override
  public void revealDefaultPlace() {
    revealPlace( defaultPlaceRequest );
  }
}
```

We'll use this class to tell which presenter to load by default. By default, when an error occurs while requesting a new place, this page will be displayed. If we want to change for a custom error page, we could create another annotation and link it to an error presenter, or use other scheme as needed. Then you override this method :

```
  @Override
  public void revealErrorPlace(String invalidHistoryToken) {
    super.revealErrorPlace(invalidHistoryToken);
  }
```

`class com.gwtplatform.samples.nested.client.NameTokens`:

```
public class NameTokens {
  public static final String homePage = "!homePage";
  public static String getHomePage() {
    return homePage;
  }

  public static final String aboutUsPage = "!aboutUsPage";
  public static String getAboutUsPage() {
    return aboutUsPage;
  }

  public static final String contactPage = "!contactPage";
  public static String getContactPage() {
    return contactPage;
  }
}
```

You're probably asking yourself why we're defining both a `getHomePage` method and an `homePage` field. This is because `@NameToken` annotations don't work with methods and !UiBinder doesn't work with fields. So if we want to use our token both in annotations and in !UiBinder, we need to define both versions.

`class com.gwtplatform.samples.nested.client.gin.ClientModule`:

```
public class ClientModule extends AbstractPresenterModule {
  @Override
  protected void configure() {
    //Singletons
    install(new DefaultModule(GwtpnestedsamplePlaceManager.class));

    //Constants
    bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.homePage);

    //Presenters
    bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class, MainPageView.class, MainPagePresenter.MyProxy.class);
    bindPresenter(HomePresenter.class, HomePresenter.MyView.class, HomeView.class, HomePresenter.MyProxy.class);
    bindPresenter(AboutUsPresenter.class, AboutUsPresenter.MyView.class, AboutUsView.class, AboutUsPresenter.MyProxy.class);
    bindPresenter(ContactPresenter.class, ContactPresenter.MyView.class, ContactView.class, ContactPresenter.MyProxy.class);
  }
}
```

Time to bind everything. Presenters will be explained bellow. GWTP needs a couple of components bound in the `DefaultModule`. You can also see that we bind the `DefaultPlace` annotation we defined before, we're binding it to the constant `NameTokens.homePage` to indicate we want to reveal this page by default.

##Step 3: Creating a custom widget
I've written a simple menu widget and since there's no complex logic at all I got rid of the presenter. Try to keep it simple and rely on the powerful of !UiBinder.

`class com.gwtplatform.samples.nested.client.ui.MainMenu`:

```
public class MainMenu extends Composite {
  private static MainMenuUiBinder uiBinder = GWT.create(MainMenuUiBinder.class);
  interface MainMenuUiBinder extends UiBinder<Widget, MainMenu> {}

  public MainMenu() {
    initWidget(uiBinder.createAndBindUi(this));
  }
}
```

Simple, it's the default template when you create a !UiBinder class.

`com.gwtplatform.samples.nested.client.ui.MainMenu.ui.xml`:

```
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
    xmlns:g="urn:import:com.google.gwt.user.client.ui">
  <ui:with type="com.gwtplatform.samples.nested.client.NameTokens" field="nameTokens"></ui:with>
  <g:HTMLPanel>
    <g:InlineHyperlink targetHistoryToken="{nameTokens.getHomePage}">Home</g:InlineHyperlink> |
    <g:InlineHyperlink targetHistoryToken="{nameTokens.getAboutUsPage}">About Us</g:InlineHyperlink> |
    <g:InlineHyperlink targetHistoryToken="{nameTokens.getContactPage}">Contact</g:InlineHyperlink>
  </g:HTMLPanel>
</ui:UiBinder>
```

And you can see now why we needed these static methods in the `NameTokens` class.

##Step 4: Writing the presenters
Now is the time to create our presenters and views. Since `ContactPresenter`,`AboutUsPresenter` and `HomePresenter` are very similar, I'll only talk about `HomePresenter` and `MainPagePresenter` here.

`class com.gwtplatform.samples.nested.client.presenter.MainPagePresenter`:

```
public class MainPagePresenter extends Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> {
  /**
   * Child presenters can fire a RevealContentEvent with TYPE_SetMainContent to set themselves
   * as children of this presenter.
   */
  @ContentSlot
  public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

  public interface MyView extends View {}

  @ProxyStandard
  public interface MyProxy extends Proxy<MainPagePresenter> {}

  @Inject
  public MainPagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy) {
    super(eventBus, view, proxy);
  }

  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire(this, this);
  }
}
```

Simple and easy. `@ContentSlot` is used to define a type to use in child presenters when you want to include them inside `MainPage`. `@ProxyStandard` is there to create our proxy via GWTP proxy generator. `Standard` means we don't need code splitting on this presenter. Also, since `MainPagePresenter` is the root of our application, we rely on GWTP `RevealRootContentEvent` to add it to the web page.

`class com.gwtplatform.samples.nested.client.view.MainPageView`:

```
public class MainPageView extends ViewImpl implements MyView {
  private static MainPageViewUiBinder uiBinder = GWT.create(MainPageViewUiBinder.class);
  interface MainPageViewUiBinder extends UiBinder<Widget, MainPageView> {}

  @UiField SimplePanel mainContentPanel;

  public MainPageView() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  @Override
  public void setInSlot(Object slot, IsWidget content) {
    if (slot == MainPagePresenter.TYPE_SetMainContent) {
      mainContentPanel.setWidget(content);
    } else {
      super.setInSlot(slot, content);
    }
  }
}
```

GWTP will call `setInSlot` when a child presenter asks to be added under this view. To support inheritance in your views it is good practice to call `super.setInSlot` when you can't handle the call. Who knows, maybe the parent class knows what to do with this slot.

`com.gwtplatform.samples.nested.client.view.MainPageView.ui.xml`:

```
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder"
    xmlns:g="urn:import:com.google.gwt.user.client.ui"
    xmlns:npui="urn:import:com.gwtplatform.samples.nested.client.ui">
  <g:HTMLPanel>
    <npui:MainMenu />
        <g:SimplePanel ui:field="mainContentPanel" />
    <npui:MainMenu />
  </g:HTMLPanel>
</ui:UiBinder>
```

As you can see, I used my `MainMenu` twice here. This is not a problem, since `MainMenu` acts as a widget in exactly the same way as typical GWT widgets.

`class com.gwtplatform.samples.nested.client.presenter.HomePresenter`:

```
public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy> {
  public interface MyView extends View {}

  @ProxyCodeSplit
  @NameToken(NameTokens.homePage)
  public interface MyProxy extends ProxyPlace<HomePresenter>  {}

  @Inject
  public HomePresenter(
      final EventBus eventBus,
      final MyView view,
      final MyProxy proxy) {
    super(eventBus, view, proxy);
  }

  @Override
  protected void revealInParent() {
    RevealContentEvent.fire(this, MainPagePresenter.TYPE_SetMainContent, this);
  }
}
```

We added two new annotations, `@ProxyCodeSplit`, used by GWTP proxy generator to create a proxy that uses code splitting, and `@NameToken(NameTokens.homePage)` to bind this presenter to the `"!homePage"` history token. When `"#!homePage"` is requested on the URL, this presenter will be revealed.

`class com.gwtplatform.samples.nested.client.view.HomeView`:

```
public class HomeView extends ViewImpl implements MyView {
  private static HomeViewUiBinder uiBinder = GWT.create(HomeViewUiBinder.class);
  interface HomeViewUiBinder extends UiBinder<Widget, HomeView> {}

  public HomeView() {
    initWidget(uiBinder.createAndBindUi(this));
  }
}
```

There's no need for `setInSlot` is this view, because we never insert another presenter inside it. `ViewImpl` is an abstract class that already implements empty versions of `setInSlot` and `addToSlot`, so if you don't override them.

#Conclusion
There's a lot of stuff and a lot more to dig in, this is only the beginning of your journey through GWTP. We'll update this tutorial to take into consideration your comments, so feel free to ask questions on the development forum.  Upcoming parts will include Dispatch, secure content and complex IDE-like applications with exchangeable components. We hope to cover every feature with as much informations as possible.

[boot]: gwtp/basicfeatures/Bootstrapping-or-Application-Initialization.html "Bootstrapping or Application Initialization"
