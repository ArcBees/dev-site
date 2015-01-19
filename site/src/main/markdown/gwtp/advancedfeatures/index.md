#Warning

Formfactors are a powerful feature but will add complexity to your project.  Formfactors will continue to be supported but we recommend you use [responsive design](http://en.wikipedia.org/wiki/Responsive_web_design) in your projects instead.  Making one view adapt to the screen size is easier than creating and maintaining 3 different design targets.

# Introduction
Building web applications for multiple devices while reusing most of the business logic you already have build for your desktop application can be challenging. Adopting good development practices as well as harnessing the power of dependency injection can help you achieve that with success and efficiency. Since GWTP is all about adopting good development practices, we found that we could make your live as developers a little bit easier by adding native form factors support.

Before going any further, you may be wondering what are form factors? Form factors are permutations of your application to specific devices and/or systems. Here's a couple of examples:
* Desktop
* Web Mobile
* Tablet
* Native mobile (Phonegap)

Not only that, we could have permutations for Wordpress, Facebook and even more!

Before going deeper into form factors, it is important that you use the application controller wich is used to generate the ginjector and all the required behind the scene goodies needed to be able to use the full power of this new feature. I invite you to read [[Bootstrapping-or-Application-Initialization]] before going any further.

##Reference
* [GWTP Mobile Sample Project](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-mobile)
* [GWTP Car Store Test Project](https://github.com/ArcBees/GWTP/tree/master/gwtp-carstore)

## Using FormFactors
The first thing that you need to do in order to be able to use the new FormFactor functionality is to inherits from the gwt module:
```xml
<inherits name="com.gwtplatform.mvp.MvpWithFormFactor" />
```
Once it's done, you need to specify what are the form factor based modules:
```xml
<!-- Form factor specific modules -->
<set-configuration-property name="gin.ginjector.module.desktop"
                    value="com.gwtplatform.samples.mobile.client.gin.DesktopModule" />
<set-configuration-property name="gin.ginjector.module.mobile"
                    value="com.gwtplatform.samples.mobile.client.gin.MobileModule" />
<set-configuration-property name="gin.ginjector.module.tablet"
                    value="com.gwtplatform.samples.mobile.client.gin.TabletModule" />
```
Depending on the the browser user agent used and browser screen size, it will use the right permutation. Usually using gin, you would bind different view per form factor if needed. Good usage of CSS can also avoid having to re-write some of the views and I strongly suggest to first start on this road. As Java developers, we often forget that we're in the web world and there's a lot that can be done with only CSS resources permutations and HTML.

## Presenter permutations
Permuting the view is really easy, but it's not as obvious with presenters that has proxies. If you don't pay attention, you could end up by adding each permutation of your presenters to each Ginjector generated for your application. To avoid that, you must create an abstract presenter that will share the same proxy for each of your presenter.
```java
public abstract class AbstractApplicationPresenter
        extends Presenter<AbstractApplicationPresenter.MyView, 
                          AbstractApplicationPresenter.MyProxy> {
    @ProxyCodeSplit
    @NameToken(NameTokens.homePage)
    @Title("Home")
    public interface MyProxy extends ProxyPlace<AbstractApplicationPresenter> {
    }

    public interface MyView extends View, HasUiHandlers<ApplicationUiHandlers> {
    }

    public AbstractApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            GwtEvent.Type<RevealContentHandler<?>> slot) {
        super(eventBus, view, proxy, slot);
    }
}
```

Then your real implementation can extend this newly create abstraction:
```java
public class ApplicationDesktopPresenter 
        extends AbstractApplicationPresenter implements ApplicationUiHandlers {
    private PlaceManager placeManager;

    @Inject
    public ApplicationDesktopPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy,
            PlaceManager placeManager) {
        super(eventBus, view, proxy, BreadcrumbsPresenter.TYPE_SetMainContent);

        this.placeManager = placeManager;

        view.setUiHandlers(this);
    }
}
```

And finally, you must bind the presenter in your gin module manually:
```java
// Application Presenters
bind(ApplicationDesktopPresenter.class).in(Singleton.class);
bind(ApplicationDesktopView.class).in(Singleton.class);
bind(AbstractApplicationPresenter.MyProxy.class).asEagerSingleton();
bind(AbstractApplicationPresenter.MyView.class).to(ApplicationDesktopView.class);
bind(AbstractApplicationPresenter.class).to(ApplicationDesktopPresenter.class);
```
TODO: In RC3 of GWTP there will be a convenience method to do all this for you.

## Going further
Now that you know how to use the new form factor feature, you can see it in action in our [mobile sample]
(https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-mobile) which is also displaying how to use the [[Phonegap support]] to export your mobile web application to a native mobile application.