# Rest Dispatch

REST Dispatch is a GWT client library introduced in GWTP 1.0. It allows your client code to send HTTP requests in a REST fashion to any server. It aims to support JSR-311 (or JAX-RS) annotations.

*Only JSON serialization is supported!*

##Reference
Many example snippets are taken from the [Car Store](https://github.com/ArcBees/GWTP-Samples/tree/master/carstore), a sample project used for integration tests within GWTP.

The serialization depends on the [gwt-jackson](https://github.com/nmorel/gwt-jackson) project. Whenever you create a pojo, you can use the [Jackson 2 annotations](https://github.com/FasterXML/jackson-annotations) to configure the serialization process!

##Setup
Setting up your application to use REST Dispatch requires a few steps:

###1. Add REST Dispatch to your maven configuration
To access the REST Dispatch classes, you need to add the following dependency to your pom file.

```xml
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-dispatch-rest</artifactId>
    <version>1.4</version>
    <scope>provided</scope>
</dependency>
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-dispatch-rest-shared</artifactId>
    <version>1.4</version>
    <scope>compile</scope>
</dependency>
```

###Add REST Dispatch to your GWT module
You need to add an inherits clause to your project's **gwt.xml** file

```xml
<inherits name="com.gwtplatform.dispatch.rest.DispatchRest"/>
```

**Very Important**
* If you use `MvpWithFormFactor` or `MvpWithEntryPoint`, the previous line _**needs**_ to be _**before**_ the `MvpWith*` inherits tag. ie:

```xml
<inherits name="com.gwtplatform.dispatch.rest.DispatchRest"/>
<inherits name="com.gwtplatform.mvp.MvpWithEntryPoint" />
```
* If you are using a generated GINjector and have something similar to

```xml
<set-configuration-property name="gin.ginjector.modules"
    value="com.gwtplatform.carstore.client.gin.SharedModule"/>
```
You need to change `set-configuration-property` to `extend-configuration-property`

### Installing with your own Ginjector
When you are using your own Ginjector -- that is if you are _not_ using GWTP's generated ginjector -- you need to add `gin.ginjector.modules` to `GinModules`'s properties attribute. This will ensure that generated GIN modules are installed.

```java
@GinModules(properties = {"gin.ginjector.modules"})
public interface MyGinjector extends Ginjector {
}
```

###Install the REST Dispatch module
To ensure the REST Dispatch can work properly, you need to install the `RestDispatchAsyncModule` in your Gin module as well as bind `RestApplicationPath` to your server API end-point.

```java
public class DispatchModule extends AbstractGinModule {
    @Override
    protected void configure() {
        RestDispatchAsyncModule.Builder dispatchBuilder =
            new RestDispatchAsyncModule.Builder();
        install(dispatchBuilder.build());

        bindConstant().annotatedWith(RestApplicationPath.class).to("/api/v1");
    }
}
```

The `RestDispatchAsyncModuleBuilder` has the following configuration methods:

* `addGlobalHeaderParam`: See [Global Parameters][gp];
* `addGlobalQueryParam`: See [Global Parameters][gp];
* `interceptorRegistry`: Needs some documentation, see [Client Action Handlers][ca] in the meanwhile;
* `exceptionHandler`: See [Exception Handler][eh];
* `requestTimeout`: The number of milliseconds to wait for a request to complete before throwing an exception. Defaults to _0 (no timeout)_;
* `serialization`: The serialization implementation to use. Defaults to _JsonSerialization_;
* `sessionAccessorType`: The class used to retrieve the value of your security cookie. Defaults to _DefaultSecurityCookieAccessor_;
* `xsrfTokenHeaderName`: See [CSRF Protection][csrf].

## Use REST Dispatch
REST Dispatch tries to reduce the amount of boilerplate required by doing code generation. The customization is done through interfaces and annotations. Most annotations come from [JSR 311 (Jax RS)](https://jsr311.java.net/nonav/releases/1.0/javax/ws/rs/package-summary.html)'s packages.

###Write resources
*See files under [com.gwtplatform.carstore.shared.api](https://github.com/ArcBees/GWTP-Samples/tree/master/carstore/src/main/java/com/gwtplatform/carstore/shared/api/) for resource examples.*

####Resources
You can create resources by following the steps below:
* All resources must be interfaces;
* You _must_ annotate your resource with `@Path`. All methods under this resource will be prepended with this path.
* Methods must have one of the following return type:
    * If the return type is an interface (excluding collections and maps), the method will return a sub-resource;
    * If the return type is a `RestAction<R>`, the method will return and end-point. You will be able to pass the
    returned instance to [RestDispatch][use]. The expected result is represented by the generic `R`.

####Endpoints
In [RPC Dispatch][rpc], you needed to create custom implementations for every single action. In REST Dispatch, you
return a parameterized interface and the implementation will be generated at compile-time.

Following the steps below, you can create and customize your endpoints:
* All methods can be annotated by `@Path("/anypath/{pathparam}")`. The string parameter will be appended to your resource's path. As demonstrated, you can optionally specify path parameters by enclosing them between `{`curly braces`}`. If a method is not annotated with `@Path`, the resource's path will be used directly.

* The HTTP Method is specified by annotating your method with the annotations provided by JSR 311. As of 1.4, REST Dispatch only supports `@GET`, `@PUT`, `@POST`, `@DELETE` and `@HEAD`. One and only one of these annotation must be set on end-point methods.

* For all the supported HTTP methods, you can use one or many of the following declarations:
    * The result is specified in the `RestAction<R>`'s generic. The following example will deserialize the response and give you a `List<Car>` instance.

    ```java
    RestAction<List<Car>> getCars();
    ```
    * You can specify header parameters via the `@HeaderParam` annotation. The following example will send the "Pragma" header along with any value you specify at runtime.

    ```java
    RestAction<Car> getCars(@HeaderParam("Pragma") String pragma);
    ```

    * You can add query parameters to your request URI via the `@QueryParam` annotation. The following example will create and send the request to a URI with the start and length parameters (ie.: `/cars?start=3&length=25`).

    ```java
    RestAction<List<Car>> getCars(@QueryParam("start") int start,
        @QueryParam("length") int length);
    ```

    * You can specify path parameters to your request URI via the `@PathParam` annotation. Your path must contain a parameter identical (case-sensitive) to the one given to `@PathParam`. The following example will replace the `{id}` parameter in your path by the specified id (`/cars/5`).

    ```java
    @Path("/{id}")
    RestAction<Car> getCar(@PathParam("id") int id);
    ```

* Additionally, for `@PUT` or `@POST`, you can use either of these parameters, but **not both**:
    * You can add multiple parameters annotated with `@FormParam`. The request body will then be formatted like a query string would be. The following example will generate this request body: `username=admin&password=s3cr3t`.

    ```java
    RestAction<Void> login(@FormParam("username") String username,
        @FormParam("password") String password);
    ```

    * Or you can provide a single object without any annotation and it will be serialized and sent as your request body.
        It must match the following rules to be a valid request body:
        + This parameter can be at any position in the signature;
        + There must be one and only one such parameter;
        + It must not be annotated by any of the previous annotations;
        + It must not be combined with any other `@FormParam` parameters.

    ```java
    @Path("/{id}")
    RestAction<Car> saveCar(@PathParam("id") int id, Car car);
    ```

####Sub-resources
If a resource returns another resource interface, then the returned resource will be a sub-resource. Methods returning a resource accept the same annotations and parameters then end-points. They will be carried all the way down to your endpoints.

###Use your REST resources {use-your-rest-resources}
Using your resources is probably the most straight-forward step:

1. You need to inject `RestDispatch` and your resource.

```java
@Inject
CarPresenter(
        RestDispatch dispatcher,
        CarsResource carsResource) {
    this.dispatcher = dispatcher;
    this.carsResource = carsResource;
}
```

2. Then you will pass the instance returned by your delegate to RestDispatch, with a callback. ie:

```java
dispatcher.execute(carsResource.car(mycarId).delete()
    new AsyncCallback<Car>() {
        @Override
        public void onSuccess(Void nothing) { /* snip */ }

        @Override
        public void onFailure(Throwable caught) { /* snip */ }
    });
```

Additionally, you can retrieve the raw [Response](http://www.gwtproject.org/javadoc/latest/com/google/gwt/http/client/Response.html) object received from the server. To do so, pass a `RestCallback` instead of an `AsyncCallback`. You will have to implement an additional `setResponse()` method. This method will be called before `onSuccess` or `onFailure`. Your endpoint call would then look like:

```java
dispatcher.execute(carsResource.car(mycarId).delete()
    new RestCallback<Car>() {
        @Override
        public void setResponse(Response response) { /* snip */ }

        @Override
        public void onSuccess(Void nothing) { /* snip */ }

        @Override
        public void onFailure(Throwable caught) { /* snip */ }
    });
```

## CSRF Protection {csrf-protection}
Rest-Dispatch offers a built-in way to secure your server calls from CSRF attacks through a security cookie. To enable CSRF protection, you must bind `@SecurityCookie` to the cookie name used to transport your security token.

The second configurable option is *xsrfTokenHeaderName*. It allows you to change the header used to transport your security token. The default value is **X-CSRF-Token**.

For example

```java
protected GinModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new RestDispatchAsyncModuleBuilder()
            .xsrfTokenHeaderName("Protection-Token")
            .build());
        bindConstant().annotatedWith(SecurityCookie.class).to("JSESSIONID");
    }
}

@Path("/myresource")
public interface MyResource {
    @GET
    RestAction<MyPojo> getMe();

    @NoXsrfHeader
    DetailsResource details();
}
```

will add this header for every `getMe()` request sent to the server: `Protection-Token: value_stored_in_jsessionid`.

You can also disable CSRF protection for specific endpoints or resources by using `@NoXsrfHeader`. In the example above, all requests sent after calling `details()` will not include the CSRF protection header. This annotation can be applied to resources, sub-resource methods and endpoint methods.

You can read [CSRF Protection][csrf] for more details.

## Global Parameters {global-parameters}
If you have header or query parameters you wish to add to every requests, you can save up on the boilerplate by configuring them through `RestDispatchAsyncModuleBuilder.addGlobalHeaderParam(String key)` and `RestDispatchAsyncModuleBuilder.addGlobalQueryParam(String key)`.

The builder also makes it possible to specify to which HTTP methods the configured parameters should be attached.

For example

```java
public class MyModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new RestDispatchAsyncModuleBuilder()
            .addGlobalHeaderParam("Pragma")
                .toHttpMethods(DELETE, POST, PUT)
                .withValue("NoCache")
            .addGlobalQueryParam("format")
                .toHttpMethods(GET)
                .withValue("xml")
            .build());
    }
}
```

would configure the header `Pragma: NoCache` for all DELETE, POST and PUT requests and the query parameter `format=xml` for all GET requests.

## Extensions
[Resource Delegates]({{#gwtp.doc.url.resource-delegate}}) will allow your end-point methods to return the result type directly. This is very useful when you want to reuse your interfaces and the annotations on server implementations of those resources. The downside is that you will lose type safety from your callbacks.

There are some extension points available through the dispatch code. You should not need them unless you have a very specific use case. If you do, feel free to browse the code: many classes are extendable and have protected methods that you can override to extend their functionality. The generators also support extensions.

[gp]: gwtp/communication/index.html#global-parameters "Global Parameters"
[ca]: gwtp/communication/Client-Action-Handlers.html "Client Action Handlers"
[eh]: gwtp/communication/Exception-Handler.html "Exception handler"
[csrf]: gwtp/communication/index.html#csrf-protection "CSRF Protection"
[rpc]: gwtp/communication/RPC-Dispatch.html "RPC Dispatch"
[use]: gwtp/communication/index.html#use-your-rest-resources "Use Your Rest Resources"
