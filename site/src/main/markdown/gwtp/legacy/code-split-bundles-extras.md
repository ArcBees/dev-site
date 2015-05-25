<!-- TODO: Review this file before release. Move the important stuff in the new doc. Then delete it. -->

##@ProxyCodeSplitBundle
`ProviderBundle`'s are an advanced way to optimize [code splitting](http://code.google.com/webtoolkit/doc/latest/DevGuideCodeSplitting.html) in your application by bundling multiple presenters together. This can reduce round-trips with the server when using asynchronous presenters and may improve application performance.

For instance, if your application has an admin section, a normal user won't have access to it. There's no reason to load this admin page until it is needed. By using code splitting, we allow initial javascript payload reduction and decrease application load time making for a more enjoyable user experience. All you need to do is supply a string parameter to the `@ProxyCodeSplitBundle` annotation that identifies the bundle.

* Example use of the @ProxyCodeSplitBundle annotation. Setup the bundle.

```
@ProxyCodeSplitBundle("MyBundle1")
public interface MyProxy extends ProxyPlace<Presenter1> {
}
```

* To keep your bundles in order an interface with all possible bundle id's can be created and than used with the `@ProxyCodeSplitBundle` annotation:

```
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

```
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

```
@ProxyCodeSplitBundle(bundleClass = MyPresenterBundle.class, id = MyPresenterBundle.ID_PRESENTER1)
public interface MyProxy extends ProxyPlace<Presenter1> {
}
```

##@TabInfo
Methods marked with `@TabInfo` now accept multiple parameters. Parameter types may include the Ginjector or any type that is provided by the Ginjector.

* Example use of @TabInfo annotation.

```
@TabInfo(container = AdminPresenter.class)
static TabData getTabLabel(MyConstants constants, IsAdminGateKeeper gateKeeper) {
    return new TabDataExt(constants.admin(), 0, gateKeeper);
}
```

##Resources
Injecting CSS and binding resources docs can be foud here: [Resources][res].

#FAQ

##Important Notes
* Projects that update to GWTP 1.0 need to remove the following line from `Module.gwt.xml` files

```
<define-configuration-property name="gin.ginjector" is-multi-valued="false"/>
```

* if the default `EntryPoint` is used or `GWT.create(ApplicationController.class)` is called, any value set for `gin.ginjector` is disregarded and it is assumed that the Ginjector should be generated
* manual `ProviderBundle`'s may not be used in conjunction with the new bootstrapping process, generated `ProviderBundle`'s are mandatory
* generated `ProviderBundle`'s imply the use of the new bootstrapping process, they may not be used without a call to `GWT.create(ApplicationController.class)` or the default `EntryPoint`

[res]: gwtp/otherusefulinformation/Resources.html
