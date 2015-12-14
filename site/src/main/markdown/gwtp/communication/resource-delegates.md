# Resource Delegates

This extension aims at making your resource interfaces (and annotations!) reusable by server implementations ([DRY principle](http://en.wikipedia.org/wiki/Don%27t_repeat_yourself)). To achieve this, the extension will generate a delegate that will wrap the call to the resource and to `RestDispatch`.

## Setup
1. Follow the setup instruction for Rest-Dispatch [here](http://dev.arcbees.com/gwtp/communication/Rest-Dispatch.html).
1. Add the jar to your classpath. If you are using maven:

    ```xml
    <dependency>
        <groupId>com.gwtplatform.extensions</groupId>
        <artifactId>dispatch-rest-delegates</artifactId>
        <version>${gwtp.version}</version>
        <scope>provided</scope>
    </dependency>
    ```

1. Inherit the extension in your GWT module file:

    ```xml
    <inherits name="com.gwtplatform.dispatch.rest.delegates.ResourceDelegate"/>
    ```

## Usage
### Write resources
The way you write resources and sub-resources doesn't change. However, the return type of your end-point is no longer limited to `RestAction<?>` or a sub-resource. Your end-points can return any concrete class or primitives. The returned type must be the type of the expected result. ie:

```java
@Path("/cars")
interface CarsResource {
    // You can use sub-resources:
    @Path("/{car-id}")
    CarResource car(@PathParam("car-id") long id);

     // You can return primitive or boxed types:
    @GET
    @Path("/count")
    int count();

    // You can return collections of concrete classes:
    @GET
    List<Car> allCars();

    // You can mix with methods that return RestAction<?>
    RestAction<Void> create(Car car);
}

interface CarResource {
    // You can return any concrete class, as long as they are serializable:
    @GET
    Car get();

    // Even void works:
    @DELETE
    void delete();
}
```

### Use resources
If you want to call a resource method that returns something else than a `RestAction<?>` or a sub-resource, then you must use the following syntax. Note that this syntax is fully compatible with `RestAction<?>` and sub-resource. It is also easier to unit test, so you are encouraged to use it in all cases.

Basically the difference with vanilla Rest-Dispatch is that you won't inject both your resource and `DispatchRest`. Instead, you will inject a `ResourceDelegate<R>` where **R** is your resource type. Then, through a fluent interface, you will pass a callback and call your end-point. ie:

```java
class MyClass {
    @Inject
    ResourceDelegate<CarsResource> carsDelegate;

    void loadCar(long carId) {
        carsDelegate
               .withCallback(new AsyncCallback<Car>() {
                    @Override
                    public void onSuccess(Car car) { /* snip */ }

                    @Override
                    public void onFailure(Throwable throwable) { /* snip */ }
                })
                .car(carId)
                .get();
    }
}
```

Note that you must call `withCallback()` or `withoutCallback()` to get access to the resource methods.

Additionally, you can pass a `DelegatingDispatchRequest` object to `withDelegatingDispatchRequest()` to get a reference of the underlying dispatch request. This may be useful if you make a long running call and may want to cancel it before it completes.

## Write unit tests
If you want to test a call or a return value of a call made to a `ResourceDelegate<R>`, you can use the utility test methods now available. You have to import `DelegateTestUtils` and all takes places in the static method `givenDelegate(...)`. With this set of methods, you can simulate multiple things on your ResourceDelegate:

* Successful call
* Failing call
* Call with callback
* Call with no callback

Here's a short example of a test written using JUnit and [Jukito](https://github.com/ArcBees/Jukito). If you don't use Jukito, everything that is annotated with `@Inject` in test code is a Mockito Mock. Let's now assume we have the following code.

```java
class MyClass {
    @Inject
    ResourceDelegate<CarsResource> carsDelegate;
    @Inject
    Collaborator collaborator;
    @Inject
    Logger logger;

    @Inject
    MyClass(ResourceDelegate<CarsResource> carsResourceDelegate,
            Collaborator collaborator,
            Logger logger) {
        this.carsResourceDelegate = carsResourceDelegate;
        this.collaborator = collaborator;
        this.logger = logger;
    }

    void loadCar(long carId) {
        carsDelegate
               .withCallback(new AsyncCallback<Car>() {
                    @Override
                    public void onSuccess(Car car) {
                        collaborator.notify(car.getId());
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        logger.log(throwable);
                    }
                })
                .car(carId)
                .get();
    }
}
```

And now we want to write a test that checks that the collaborator is notified when the car is fetched.

```java
@RunWith(JukitoRunner.class)
public class MyClassTest {
    @Inject
    MyClass sut;
    @Inject
    ResourceDelegate<CarsResource> carsResourceDelegate;
    @Inject
    Collaborator collaborator;
    @Inject
    Logger logger;

    @Test
    public void notifyCollaborator_whenCarIsLoadedSuccessfully() {
        // given
        long carId = 1337L
        Car expectedCar = new Car(carId);

        givenDelegate(carsResourceDelegate).useResource(CarsResource.class)
            .and().succeed().withResult(expectedCar)
            .when().get(carId);

        // when
        sut.load(carId);

        // then
        verify(collaborator).notify(carId);
    }
}
```

At first we have to setup the fake `ResourceDelegate` to use a resource by calling `useResource`. You can either use the class reference of the resource interface, or an instance of the interface. Then you can stub the result of the callback using `.succeed().withResult()`. If your method doesn't return any value, you can omit the `.withResult()` part. And then we match the result of the callback with a specific method call with `.when().methodCalled()`.

So then, when the actual method is called, the callback will also be called with whatever you've setup. So then you can test the actual behavior of the callback easily.

You can test a failure with a pretty similar syntax. Instead of `.and().succeed().withResult(...)` you can just write `and().fail().withThrowable(...)`.

### Nested resources
Sometimes you have nested resources, let's assume we have the following code.

```java
@Path("/cars")
interface CarsResource {
    @Path("/{car-id}")
    CarResource car(@PathParam("car-id") long id);
}

interface CarResource {
    @GET
    Car get();
}
```

So in order to stub the callback result of the `get()` method, you first have to stub the `CarResource car(@PathParam("car-id") long id)` method to return the correct `ResourceDelegate`. Here's an example of how to do it:

```java
@Inject
CarsResource  carsResource;

// ...

Car someCar = ...;

given(carsResource.car(carDto.getId())).willReturn(carResource);

givenDelegate(carsDelegate).useResource(carsResource)
    .and().succeed()
    .when(carResource).get();
```

Notice that we first mock the `.car(long id)` method to return the sub-resource, and then we use setup the `ResourceDelegate`, to use the parent resource and to match the call on the sub-resource.

## Gotchas
So you may wonder why this is not included in the core of Rest-Dispatch. The reason is that by using the delegates, _the type safety of your callback can no longer be verified at compile-time_. For example, if your end-point returns a `Car` and your callback expects a `Wheel`, you won't know there's something wrong until this code is executed at runtime. This problem can be leveraged by consistently writing unit tests that covers your dispatch calls (which we encourage you to write anyways). So that's why we felt this was a potential source of errors for some users and made this feature an extension. But rest assured, we use this in many internal projects and, by having a good unit test coverage, this has never caused us issues.