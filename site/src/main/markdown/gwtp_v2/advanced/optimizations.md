# Optimizations
When developing a web application, the JavaScript part tends to become more heavy as it progresses and this puts significant load on the application startup. *Code Splitting* allows the JavaScript code to be split into multiple files that will be loaded depending on the application section being used. Separating your GWTP application into logical and independent sections will provide natural points for code splitting. See [GWT Code Splitting](http://www.gwtproject.org/doc/latest/DevGuideCodeSplitting.html) for more details.

## Code Splitting
To use Code Splitting, you need a [Proxy]({{#gwtp.doc.url.proxy}}) associated to the [Presenter]({{#gwtp.doc.url.presenter}}) you want to sit behind a split point. Code Splitting is done by simply annotating your Proxy with `@ProxyCodeSplit` instead of `@ProxyStandard`.

Example of usage:

```java
@ProxyCodeSplit
interface MyProxy extends ProxyPlace<ApplicationPresenter> {
}
```

The JavaScript code will now be compiled into a new JavaScript file and be loaded from the application server only when needed.

## Code Split Bundles
*Code Split Bundles* allow you to regroup compiled JavaScript files into a bundle separated from other JavaScript files. This is useful when multiple Presenters share the same code base (i.e. Nested Presenters). To use Code Split Bundles, you can either use an automatically generated [ProviderBundle](http://arcbees.github.io/GWTP/javadoc/apidocs/com/gwtplatform/common/client/ProviderBundle.html) or create your own implementation for it.

### Using generated ProviderBundles
If you use GWTP's [ApplicationController](http://arcbees.github.io/GWTP/javadoc/apidocs/com/gwtplatform/mvp/client/ApplicationController.html) to trigger your Ginjector generation, all bundles will be automatically generated for you. A good way to keep track of them is to create an interface with string constants identifying every bundle.

Identifying your Bundles:

```java
interface Bundles {
    String MAIN = "Main";
    String OTHER = "Other";
}
```

Here is an example of Code Split Bundles when using GWTP's generation feature:

```java
@ProxyCodeSplitBundle(Bundles.MAIN)
interface MyProxy extends ProxyPlace<Object1> {
}
```

### Using manually set ProviderBundles
For the manual approach, you need an implementation of `ProviderBundle`.

```java
public class MyPresenterBundle extends ProviderBundle {
    public final static int ID_Object1 = 0;
    public final static int ID_Object2 = 1;
    public final static int BUNDLE_SIZE = 2;

    @Inject
    MyPresenterBundle(Provider<Object1> object1Provider, Provider<Object2> object2Provider) {
        super(BUNDLE_SIZE);

        providers[ID_Object1] = object1Provider;
        providers[ID_Object2] = object2Provider;
    }
}
```

Then `ProxyCodeSplitBundle.bundleClass()` and `ProxyCodeSplitBundle.id()` methods need to be defined.

```java
@ProxyCodeSplitBundle(bundleClass = MyPresenterBundle.class, id = MyPresenterBundle.ID_Object1)
interface MyProxy extends ProxyPlace<Object1> {
}
```
