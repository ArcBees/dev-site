# Client Action Handlers

Client action handlers or rpc-dispatch.

#Introduction
Introduced in GWTP 0.5, client action handlers are a powerful way to extend the command pattern.

Unlike "normal" action handlers which are run on the server, and receive requests over gwt-rpc, client action handlers are run on the client before the request is sent over the wire, and can provide a number of benefits :
  * The action can be modified before sending the action over gwt-rpc to the server.
  * A result can be returned without contacting the server (i.e. client cache).
  * The result can be modified or processed after it is returned from the server.
  * The client action handler can take over and communicate directly with the server, possibly using a different mechanism than GWT-RPC.

#Client Action Handlers HOWTO
Before you begin, make sure you have read and understand the [RPC-Dispatch](https://github.com/ArcBees/GWTP/wiki/RPC-Dispatch)

In the following example we will implement a ClientActionHandler that caches the result on the client so that subsequent calls don't have to contact the server as long as the fetched object is in the cache. Note that once the ClientActionHandler is implemented, you use the dispatcher normally. The code below will automatically 'inject' your ClientActionHandler into the dispatch call.

###Creating the ClientActionHandler
The first thing we need to do is to create the actual ClientActionHandler. Think of this as a client-side version of a normal server-side ActionHandler. Once this ClientActionHandler is installed, this class will be called instead of the ActionHandler on the server.


```
public class FooRetrieveClientActionHandler extends AbstractClientActionHandler<FooRetrieveAction, FooRetrieveResult> {
    private final Cache cache; // Use the built-in GWTP cache

    @Inject
    public FooRetrieveClientActionHandler(Cache cache) {
        super(FooRetrieveAction.class);
        this.cache = cache;
    }

    @Override
    public DispatchRequest execute(final FooRetrieveAction action,
                                   final AsyncCallback<FooRetrieveResult> resultCallback,
                                   ExecuteCommand<FooRetrieveAction, FooRetrieveResult> dispatch) {

        // Check cache
        FooRetrieveResult result = (FooRetrieveResult) this.cache.get(getActionType());

        if (result != null) {
            // We found a FooRetrieveResult in the cache, return without contacting the server
            resultCallback.onSuccess(result);
        } else {
            // Nothing found in cache, get result from server
            dispatch.execute(action, new AsyncCallback<FooRetrieveResult>() {
                public void onSuccess(FooRetrieveResult result) {
                    // Save FooRetrieveResult in cache for use the next time FooRetrieveActionHandler is called
                    cache.put(getActionType(), result);
                    resultCallback.onSuccess(result);
                }
                public void onFailure(Throwable caught) {
                    resultCallback.onFailure(caught);
                }
            });
        }

        return new CompletedDispatchRequest();
    }

    @Override
    public DispatchRequest undo(FooRetrieveAction action,
                                FooRetrieveResult result,
                                AsyncCallback<Void> callback,
                                UndoCommand<FooRetrieveAction, FooRetrieveResult> dispatch) {
        dispatch.undo(action, result, callback);
        return new CompletedDispatchRequest();
    }
}
```

### Registering client action handlers

To register a client action handler, extend the `DefaultClientActionHandlerRegistry` and call register in the constructor.   Your handlers should be injected into the constructor of your registry.
  * Pass a handler object.  The code for the client action handler will be part of the initial download, and handler construction overhead will occur at application startup.
  * Pass a handler Provider.  The code for the client action handler will be part of the initial download, and but the object constructor overhead will be delayed until the first time the client action handler is used.
  * Pass a handler `AsyncProvider`. The code for this client action handler will be in a separate split point. This code wont be retrieved and the handler wont be constructed until the first time the client action handler is used.

Below is an example of how to extend the ``DefaultClientActionHandlerRegistry``

```
public class MyClientActionHandlerRegistry extends DefaultClientActionHandlerRegistry {
    // Direct Inject
    @Inject
    public MyClientActionHandlerRegistry(FooRetrieveClientActionHandler fooRetrieveClientActionHandler) {
        register(fooRetrieveClientActionHandler);
    }

    // Example of Provider inject
/*
    @Inject
    public MyClientActionHandlerRegistry(Provider<FooRetrieveClientActionHandler> fooRetrieveClientActionHandlerProvider) {
        register(FooRetrieveAction.class, fooRetrieveClientActionHandlerProvider);
    }
*/

    // Example of AsyncProvider Inject
/*
    @Inject
    public MyClientActionHandlerRegistry(AsyncProvider<FooRetrieveClientActionHandler> fooRetrieveClientActionHandler) {
        register(FooRetrieveAction.class, fooRetrieveClientActionHandler);
    }
*/
}
```


Note that you can supply as many ClientActionHandlers as needed to the constructor and you can also mix in what way they are provided. Below is an example of a constructor with three different ClientActionHandlers, all provided in different ways

```
@Inject
public MyClientActionHandlerRegistry(
    MyFirstClientActionHandler handler
    Provider<MySecondClientActionHandler> handlerProvider,
    AsyncProvider<MyThirdClientActionHandler> asyncHandlerProvider) {

    register(handler);
    register(MySecondAction.class, handlerProvider);
    register(MyThirdAction.class, asyncHandlerProvider);
}
```

### Wiring up Gin to use your ClientActionHandlers
Now we have everything we need. All that is left is to wire up Gin to use your `ClientActionHandlerRegistry`

To do this, you create a custom DispatchModule that uses your ClientActionHandlerRegistry. This is how you create such a module:

```
public class ClientDispatchModule extends AbstractGinModule {
    @Override
    protected void configure() {
        install(new DispatchAsyncModule.Builder().clientActionHandlerRegistry(MyClientActionHandlerRegistry.class).build());
    }
}
```

Once this is done, replace the GWTP default DispatchModule with your custom-made ClientDispatchModule

```
public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule());
        install(new ApplicationModule());
//        install(new DispatchAsyncModule()); // Remove GWTP's default dispatch module
        install(new ClientDispatchModule()); // Use our own dispatch module (that uses ClientActionHandlers)

        // It's vitally important to bind GWTP's cache interface in a singleton
        bind(Cache.class).to(DefaultCacheImpl.class).in(Singleton.class);

        // DefaultPlaceManager Places
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.home);
    }
}
```
Note that since our example is all about caching the result from the server, we must bind GWTP's cache interface to an implementation. We must also take great care to bind the cache in a `Singleton`, otherwise the Cache will be re-instantiated every time the ClientActionHandler is executed and will thus not contain any cached results and the ClientActionHandler will always contact the server, making the caching completely useless.

Congratulations! Now calling the `FooRetrieveAction` will call `ClientActionHandler` instead.


##Testing Presenters that use client action handlers

```
@RunWith(MockitoJUnitRunner.class)
public class TestMyPresenter {
    @Mock
    private MyNonGwtRpcClientActionHandler myNonGwtRpcClientActionHandler;

    private MyGwtRpcClientActionHandler myGwtRpcClientActionHandler;

    //...

    @Before
    public void setUp() throws Exception {
        helper.setUp();

        myGwtRpcClientActionHandler = new MyGwtRpcClientActionHandler();

        Injector injector =
            Guice.createInjector(new DispatchHandlerModule(),
                new MockHandlerModule() {
                    @Override
                    protected void configureMockHandlers() {
                        bindMockActionHandler(
                            MyGwtRpcAction.class,
                            myGwtRpcClientActionHandler);

                        bindMockClientActionHandler(
                            MyNonGwtRpcAction.class,
                            myNonGwtRpcClientActionHandler);

                        bindMockClientActionHandler(MyGwtRpcAction.class, myGwtRpcClientActionHandler);
                    }
                });

    DispatchAsync dispatcher = injector.getInstance(DispatchAsync.class);

    presenter = new MyPresenterImpl(null, wizardView, proxy, null, placeManager, null, dispatcher);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPresenter() throws ActionException, ServiceException {
        // fake result for client action handler that doesn't call the server.

        final MyNonGwtRpcResult result = new MyNonGwtRpcResult(...);
        doAnswer(new Answer<Object>() {
            @SuppressWarnings("unchecked")
            public Object answer(InvocationOnMock invocation) {
                AsyncCallback<MyNonGwtRpcResult> callback = (AsyncCallback<MyNonGwtRpcResult>) invocation.getArguments()
             [1];
                callback.onSuccess(result);
                return null;
            }
        }).when(myNonGwtRpcClientActionHandler)
          .execute(
              eq(new MyNonGwtRpcAction(...)), any(AsyncCallback.class),
              any(ClientDispatchRequest.class), any(ExecuteCommand.class));

        final MyGwtRpcResult result2 =
            new MyGwtRpcResult(...);
        when(
            MyGwtRpcActionHandler.execute(
                eq(new MyGwtRpcAction(..)),
                any(ExecutionContext.class))).thenReturn(result2);
    }
}
```

#Examples

### Geocoding Example ###
The following code demonstrates a client action handler that communicate directly with a server using a different mechanism than gwt-rpc.

```
@GenDispatch
public class GeocodeAddress {
    @In(1) String address;
    @In(2) String region;
    @Optional @Out(1) PlacemarkDto[] placemarks;
}

@GenDto
public class Placemark {
    String address;
    PointDto point;
}

@GenDto
public class Point {
    double latitude;
    double longitude;
}

/**
 * Uses the Google Maps API v3 to geocode an address.
 *
 * @see <a href="http://code.google.com/apis/maps/documentation/javascript/services.html#Geocoding">http://code.google.com/apis/maps/documentation/javascript/services.html#Geocoding</a>
 */
public class GeocodeAddressClientActionHandler extends
    AbstractClientActionHandler<GeocodeAddressAction, GeocodeAddressResult> {
  private final AsyncProvider<Geocoder> geocoderProvider;

  @Inject
  protected GeocodeAddressClientActionHandler(
      AsyncProvider<Geocoder> geocoderProvider) {
    super(GeocodeAddressAction.class);
    this.geocoderProvider = geocoderProvider;
  }

  @Override
  public void execute(GeocodeAddressAction action,
      final AsyncCallback<GeocodeAddressResult> resultCallback,
      ClientDispatchRequest request,
      ExecuteCommand<GeocodeAddressAction, GeocodeAddressResult> dispatch) {

    final GeocoderRequest geocodeRequest = new GeocoderRequest();
    geocodeRequest.setAddress(action.getAddress());
    geocodeRequest.setRegion(action.getRegion());

    this.geocoderProvider.get(new AsyncCallback<Geocoder>() {

      @Override
      public void onSuccess(Geocoder geocoder) {
        geocoder.geocode(geocodeRequest, new GeocoderCallback() {

          @SuppressWarnings("deprecation")
          @Override
          public void callback(List<HasGeocoderResult> responses,
              String status) {
            if (status.equals("OK")) {

              PlacemarkDto[] placemarks =
                  new PlacemarkDto[responses.size()];
              for (int i = 0; i < responses.size(); i++) {
                HasGeocoderResult response = responses.get(i);
                HasLatLng location =
                    response.getGeometry().getLocation();
                placemarks[i] =
                    new PlacemarkDto(
                        response.getFormattedAddress(),
                        new PointDto(
                            location.getLatitude(),
                            location.getLongitude()));
              }

              resultCallback.onSuccess(new GeocodeAddressResult(
                  placemarks));
            } else if (status.equals("ZERO_RESULTS")) {
              resultCallback.onSuccess(new GeocodeAddressResult());
            } else {
              resultCallback.onFailure(new Exception("Geocoder: "
                  + status));
            }
          }
        });
      }

      @Override
      public void onFailure(Throwable caught) {
        resultCallback.onFailure(caught);
      }
    });
  }

  @Override
  public void undo(GeocodeAddressAction action, GeocodeAddressResult result,
      AsyncCallback<Void> callback, ClientDispatchRequest request,
      UndoCommand<GeocodeAddressAction, GeocodeAddressResult> dispatch) {
    // do nothing
  }
}
```

### Testing the Geocoding Example ###

```
@RunWith(MockitoJUnitRunner.class)
public class MyTest {
  @Mock
  private CreateEmployeeView view;

  //...

  private final LocalServiceTestHelper helper = new LocalServiceTestHelper();

  //...

  private CreateEmployeePresenterImpl;

  @Mock
  private GeocodeAddressClientActionHandler geocodeAddressClientActionHandler;

  @Before
  public void setUp() throws Exception {

    helper.setUp();

    Injector injector =
        Guice.createInjector(new DispatchHandlerModule(),
            new MockHandlerModule() {
              @Override
              protected void configureMockHandlers() {

                bindMockClientActionHandler(
                    GeocodeAddressAction.class,
                    geocodeAddressClientActionHandler);
              }
            });

    DispatchAsync dispatcher = injector.getInstance(DispatchAsync.class);

    // ... pass dispatcher to wizard
    presenter = new CreateEmployeePresenterImpl(null, view,
        proxy, null, null, null, dispatcher);
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testGeocode() throws ActionException, ServiceException {

    doAnswer(new Answer<Object>() {
      @SuppressWarnings("unchecked")
      public Object answer(InvocationOnMock invocation) {
        AsyncCallback<GeocodeAddressResult> callback = (AsyncCallback<GeocodeAddressResult>) invocation.getArguments()[1];
        callback.onSuccess(new GeocodeAddressResult(null));
        return null;
      }
    }).when(geocodeAddressClientActionHandler)
      .execute(
          eq(new GeocodeAddressAction("1 Google Way, New Zealand",
              "nz")), any(AsyncCallback.class),
          any(ClientDispatchRequest.class), any(ExecuteCommand.class));

    wizard.getAddresses("1 Google Way, New Zealand");
    verify(view).setError("Not found.");

    doAnswer(new Answer<Object>() {
      @SuppressWarnings("unchecked")
      public Object answer(InvocationOnMock invocation) {
        AsyncCallback<GeocodeAddressResult> callback = (AsyncCallback<GeocodeAddressResult>) invocation.getArguments()[1];
        PlacemarkDto[] placemarks = new PlacemarkDto[2];
        placemarks[0] =
            new PlacemarkDto("2 Google Way, Auckland, New Zealand",
                new PointDto(-36.84, 174.73));
        placemarks[1] =
            new PlacemarkDto("2 Google Way, Wellington, New Zealand",
                new PointDto(-41.29, 174.78));
        callback.onSuccess(new GeocodeAddressResult(placemarks));
        return null;
      }
    }).when(geocodeAddressClientActionHandler)
      .execute(
          eq(new GeocodeAddressAction("2 Google Way, New Zealand",
              "nz")), any(AsyncCallback.class),
          any(ClientDispatchRequest.class), any(ExecuteCommand.class));

    wizard.getAddresses("2 Google Way, New Zealand");

    String[] addresses =
        new String[] {
            "2 Google Way, Auckland, New Zealand",
            "2 Google Way, Wellington, New Zealand"};
    verify(view).setSearchResults(addresses);
    verify(view).setMapLocation(-36.84, 174.73);
  }
}
```
