GWTP reduces the amount of code needed in order to get started with the MVP modules.
The `Ginjector` and `ProviderBundle`'s are no longer required to be created and updated by hand, all that needed to do is point GWTP to the [Gin](http://code.google.com/p/google-gin/) modules.

##Key Benefits
* No entry point has to be created which cuts down on the entry boilerplate code.
* Simply define the first gin module in `Project.gwt.xml` module.
* Custom application initialization logic can be described and initialized by specifying a Bootrapper and/or a PreBootstraper. 
* Initialization standardization helps keep focus on the business logic.  
* Using code splitting to optimize large applications load time. 

##Feature Availability
The provided options below have been available since `0.8-beta-1` release.

## Application Examples
* [GWTP Basic Archetype](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-basic)
* [GWTP Basic Sample](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-basic)

#Boostrapping Configuration Options

##Extending the generated Ginjector
The generated ginjector can extend already existing ginjector interface by adding .extensions property. 
```xml
<extend-configuration-property name="gin.ginjector.extensions" 
     value="com.gwtplatform.mvp.client.gin.MyGinjector"/>
```

##Client Gin Modules
Extend the `gin.ginjector.modules` property in your `*.gwt.xml` module to tell GWTP where your gin modules are:
```xml
<extend-configuration-property name="gin.ginjector.modules"
     value="com.gwtplatform.samples.basic.client.gin.ClientModule"/>
```

##EntryPoint
GWTP makes bootstrapping initialization even easier by taking care of the `EntryPoint`. This standardizes the application startup allowing the developer to concentrate more on the business logic.

* Adding GWTP's `EntryPoint` to the `Project.gwt.xml` to start the application.
```xml
<inherits name='com.gwtplatform.mvp.MvpWithEntryPoint'/>
```

* `Project.gwt.xml` example. [Here's the actual file](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-basic/src/main/java/com/arcbees/project/Project.gwt.xml). 
```xml
<?xml version='1.0' encoding='UTF-8'?>
<module rename-to='project'>
    <!-- Inherit the core Web Toolkit stuff. -->
    <inherits name='com.google.gwt.user.User' />
    <inherits name='com.google.gwt.inject.Inject' />

    <!-- Default css -->
    <inherits name='com.google.gwt.user.theme.standard.Standard' />
    <!-- <inherits name='com.google.gwt.user.theme.chrome.Chrome'/> -->
    <!-- <inherits name='com.google.gwt.user.theme.dark.Dark'/> -->

    <!-- Other module inherits -->
    <inherits name='com.gwtplatform.dispatch.Dispatch' />
    
    <inherits name='com.gwtplatform.mvp.MvpWithEntryPoint' />

    <source path='client' />
    <source path='shared' />

    <set-configuration-property name="gin.ginjector.modules"  
         value="com.arcbees.project.client.gin.ClientModule"/>
</module>
```

* For `Project.gwt.xml` manual `EntryPoint` configuration, inherit `com.gwtplatform.mvp.Mvp` add an entry-point element and add the `EntryPoint` class. 
```xml
<inherits name='com.gwtplatform.mvp.Mvp'/>
<entry-point class='tld.domain.project.client.MyApplicationEntryPoint'/>
```
```java
public class MyApplicationEntryPoint implements EntryPoint {
    private static final ApplicationController controller = GWT.create(ApplicationController.class);
    public void onModuleLoad() {
        controller.init();
    }
}
```

###Previous GWTP Versions EntryPoint Initialization
Previously the bootstrapped process was created by the Ginjector with a call to `GWT.create()`. After that the `DelayedBindRegistry` is initialized and finally a call to `PlaceManager.revealCurrentPlace()` is made.

* Previously this is how the application was initialized. This is no longer needed if the above two formats are used.
```java
public void onModuleLoad() {
    ClientGinjector ginjector = GWT.create(ClientGinjector.class);
    DelayedBindRegistry.bind(ginjector);
    ginjector.getPlaceManager().revealCurrentPlace();
}
```

##Creating a Bootstrapper
`Bootstrapper`'s are really useful if you want to do anything before the `PlaceManager` calls revealCurrentPlace(). By default, when you don't have any custom `Bootstrapper` defined, GWTP will simply use it's `DefaultBootstrapper` which does exactly that. If you don't want this to happen, you can provide a custom `Bootstrapper` by creating a class that implements the `Bootstrapper` interface. By setting     
```xml
<set-configuration-property name="gwtp.bootstrapper"  
         value="com.arcbees.project.client.BootstrapperImpl"/> 
```
GWTP will then substitute it's `DefaultBootstrapper` for your custom version. `Bootstrapper`'s can make use of [Gin's](http://code.google.com/p/google-gin/) dependency injection.

* Example use of the Boostrapper. This will override the `DefaultBootstrapper`.
```java
public class BootstrapperImpl implements Bootstrapper {
    private final PlaceManager placeManager;

    @Inject
    public BootstrapperImpl(PlaceManager placeManager) {
        this.placeManager = placeManager;
    }

    @Override
    public void onBootstrap() {
        doSomeCustomLogic();
        placeManager.revealCurrentPlace();
    }

    private void doSomeCustomLogic() {
       // ...
    }
}
```

##Creating a PreBootstrapper
A `PreBootstrapper` allows you to hook into the GWTP bootstrapping process right before it starts. This is particularly useful if you need something done before GWTP starts up. In general the use of a `Bootstrapper` is advised but there are cases where that is not enough, for example when setting up an `UncaughtExceptionHandler` for [gwt-log](http://code.google.com/p/gwt-log/wiki/GettingStarted#Setup_an_UncaughtExceptionHandler).

```xml
<set-configuration-property name="gwtp.prebootstrapper"  
         value="com.arcbees.project.client.PreBootstrapperImpl"/> 
```

* Example use of the PreBootstrapper class. It is important to note that **no dependency injection** is provided for `PreBootstrapper`'s.
```java
public class PreBootstrapperImpl implements PreBootstrapper {
    @Override
    public void onPreBootstrap() {
        GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
            @Override
            public void onUncaughtException(final Throwable e) {
                Window.alert("There was a problem loading your application");
            }
        });
    }
}
```

##@ProxyCodeSplitBundle
`ProviderBundle`'s are an advanced way to optimize [code splitting](http://code.google.com/webtoolkit/doc/latest/DevGuideCodeSplitting.html) in your application by bundling multiple presenters together. This can reduce round-trips with the server when using asynchronous presenters and may improve application performance.

For instance, if your application has an admin section, a normal user won't have access to it. There's no reason to load this admin page until it is needed. By using code splitting, we allow initial javascript payload reduction and decrease application load time making for a more enjoyable user experience. All you need to do is supply a string parameter to the `@ProxyCodeSplitBundle` annotation that identifies the bundle.

* Example use of the @ProxyCodeSplitBundle annotation. Setup the bundle. 
```java
@ProxyCodeSplitBundle("MyBundle1")
public interface MyProxy extends ProxyPlace<Presenter1> {
}
```
* To keep your bundles in order an interface with all possible bundle id's can be created and than used with the `@ProxyCodeSplitBundle` annotation:
```java
public interface Bundles {
    String BUNDLE1 = "MyBundle1";
    String BUNDLE2 = "MyBundle2";
}

@ProxyCodeSplitBundle(Bundles.BUNDLE1)
public interface MyProxy extends ProxyPlace<Presenter1> {
}
```

###Previous GWTP Versions Code Splitting
With large bundles keeping all the id's and bundle sizes in order can be quite hard. That is why GWTP now generates bundles for you with the new features above. Now the above annotations are available to help with this.

* `ProviderBundle`'s is used and created like this:
```java
public class MyPresenterBundle extends ProviderBundle {
   public final static int ID_PRESENTER1 = 0;
   public final static int ID_PRESENTER2 = 1;
   public final static int BUNDLE_SIZE = 2;

   @Inject
   MyPresenterBundle(Provider<Presenter1> presenter1Provider, Provider<Presenter2> presenter2Provider) {
     super(BUNDLE_SIZE);
     providers[ID_PRESENTER1] = presenter1Provider;
     providers[ID_PRESENTER2] = presenter2Provider;
   }
}
```
* After that the bundle can be used when declaring a proxy by using `@ProxyCodeSplitBundle`:
```java
@ProxyCodeSplitBundle(bundleClass = MyPresenterBundle.class, id = MyPresenterBundle.ID_PRESENTER1)
public interface MyProxy extends ProxyPlace<Presenter1> {
}
```

##@TabInfo
Methods marked with `@TabInfo` now accept multiple parameters. Parameter types may include the Ginjector or any type that is provided by the Ginjector.

* Example use of @TabInfo annotation.
```java
@TabInfo(container = AdminPresenter.class)
static TabData getTabLabel(MyConstants constants, IsAdminGateKeeper gateKeeper) {
    return new TabDataExt(constants.admin(), 0, gateKeeper);
}
```

##Resources
Injecting CSS and binding resources docs can be foud here: [[Resources]].

#FAQ

##Important Notes
* Projects that update to GWTP 1.0 need to remove the following line from `Module.gwt.xml` files
```xml
<define-configuration-property name="gin.ginjector" is-multi-valued="false"/>
```
* if the default `EntryPoint` is used or `GWT.create(ApplicationController.class)` is called, any value set for `gin.ginjector` is disregarded and it is assumed that the Ginjector should be generated
* manual `ProviderBundle`'s may not be used in conjunction with the new bootstrapping process, generated `ProviderBundle`'s are mandatory
* generated `ProviderBundle`'s imply the use of the new bootstrapping process, they may not be used without a call to `GWT.create(ApplicationController.class)` or the default `EntryPoint`