# RPC Dispatch
Remote Procedure Call Dispatcher is a client server communication protocol. It serializes and deserializes the objects being sent over the wire.

##Reference
* The example snippets are being taken from this Archetype [GWTP AppEngine Objectify](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-objectify).

##Overview
Setting up the dispatching requires a few parts:

* Guice or Spring
* Client request. [Example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/admin/AdminPresenter.java#L68).
* Action class. [Example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/shared/dispatch/FetchAdminTaskCountAction.java).
* Result class. [Example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/shared/dispatch/FetchAdminTaskCountResult.java).
* Server dispatch handler. [Example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/server/dispatch/FetchAdminTaskCountHandler.java).
* Optional server dispatch server handler validator. [Example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/server/dispatch/validators/AdminActionValidator.java).

###More Action and Result Examples
* [Action & Result Examples](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/shared/dispatch)

## Using RPC Dispatch with Guice
First of, you can start by reading Guice's [Getting Started](https://code.google.com/p/google-guice/wiki/GettingStarted) guide if you're unfamiliar with it.

Basice Guice setup with web.xml

```
    <listener>
        <listener-class>some.project.server.guice.GuiceServletConfig</listener-class>
    </listener>

    <filter>
        <filter-name>guiceFilter</filter-name>
        <filter-class>com.google.inject.servlet.GuiceFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>guiceFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
```

GuiceServletConfig

```
package some.project.server.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;

public class GuiceServletConfig extends GuiceServletContextListener {
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(..., new DispatchServletModule());
    }
}
```

Then you need to either create this class or at the *serve* call to your existing ServletModule

```
public class DispatchServletModule extends ServletModule {
    @Override
    public void configureServlets() {
        serve("/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);
    }
}
```

## Using RPC Dispatch with Spring
*Coming soon*

##Client Request
* Setting up the client request requires that the `DispatchAsync` be available which is injected in the constructor.
* In this example `AsyncCallbackImpl<T>` implements `AsyncCallback<T>`. Both can be used. The advantage of using `AsyncCallbackImpl<T>` allows the application to globally catch onFailures. See more below.
* [Source](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/home/HomePagePresenter.java#L47).

```
private DispatchAsync dispatcher;

@Inject
public HomePagePresenter(final EventBus eventBus, final MyView view, final MyProxy proxy,
        final DispatchAsync dispatcher) {
    super(eventBus, view, proxy, ApplicationPresenter.TYPE_SetMainContent);

    this.dispatcher = dispatcher;
}
```

* Then a request can be setup like this. [Source](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/home/HomePagePresenter.java#L61).

```
private void fetchTask() {
    FetchTaskAction action = new FetchTaskAction();
    action.setTaskId(1l);

    dispatcher.execute(action, new AsyncCallbackImpl<FetchTaskResult>() {
        @Override
        public void onSuccess(FetchTaskResult result) {
            displayTask(result.getTask());
        }
    });
}
```

##Shared Action
Action designates the call taken place and carries the object(s) to the server. The action is shared with both the client and server and the encapsulated objects must be serializable. The contained objects must implement `IsSerializable` or `Serializable`.

* Example Action. [Source](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/shared/dispatch/FetchAdminTaskCountAction.java).

```
public class FetchAdminTaskCountAction extends UnsecuredActionImpl<FetchAdminTaskCountResult> {
    public FetchAdminTaskCountAction() {
    }
}
```

##Shared Result
Result designates the response to the call and carries the objects(s) back to the client. The result is shared with both client and server and the encapsulated objects must be serializable. The contained objects must implement `IsSerializable` or `Serializable`.

* Example Result. [Source](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/shared/dispatch/FetchAdminTaskCountResult.java).

```
public class FetchAdminTaskCountResult implements Result {
    private Integer totalTasks;

    public FetchAdminTaskCountResult() {
    }

    public FetchAdminTaskCountResult(Integer totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Integer getTotalTasksCount() {
        return totalTasks;
    }
}
```

##Catching onSuccess or onFailure Globally
Catching the onSuccess or onFailure for each request can be done by extending `AsyncCallback<T>`.

* `AsyncCallback<T>` class can be used and it looks like this:

```
public interface AsyncCallback<T> {
  void onFailure(Throwable caught);

  void onSuccess(T result);
}
```

* Implementing `AsyncCallback<T>` will allow you to catch the onFailure and/or the onSuccess. In this example the onFailure is caught so it could be used to notify the user of the failure. [Source](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/dispatch/AsyncCallbackImpl.java).

```
public abstract class AsyncCallbackImpl<T> implements AsyncCallback<T> {
    @Override
    public void onFailure(Throwable caught) {
        Window.alert("Communication to the server has failed.");
    }
}
```

##Notes on implementing a ActionHandler
There are essentially two ways to implement an ActionHandler for your Actions/Results

* Method 1: Simply implement the com.gwtplatform.dispatch.server.actionhandler.ActionHandler interface

```
public class FetchAdminTaskCountHandler implements ActionHandler<FetchAdminTaskCountAction, FetchAdminTaskCountResult> {
    @Inject
    public FetchAdminTaskCountHandler() {
    }

    @Override
    public FetchAdminTaskCountResult execute(FetchAdminTaskCountAction action, ExecutionContext context) throws ActionException {
        return null;
    }

    @Override
    public void undo(FetchAdminTaskCountAction action, FetchAdminTaskCountResult result, ExecutionContext context) throws ActionException {
    }

    @Override
    public Class<FetchAdminTaskCountAction> getActionType() {
        return FetchAdminTaskCountAction.class;
    }
}
```

* Method 2: If you have been browsing the GWTP examples, you will notice that the ActionHandlers extends a AbstractAction class rather than implementing the ActionHandler like the above example. This is just a base class implementing the com.gwtplatform.dispatch.server.actionhandler.ActionHandler interface and doing some wire up to reduce boilerplate. Here is an example of creating a base class implementing the ActionHandler class: [Example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/server/dispatch/AbstractAction.java)

##Configuring Actions/ActionHandlers on the Server
Before you can start using your ActionHandlers on the server. You need to tell Guice which ActionHandler is responsible for responding to which Action. This is done in the Guice ServerModule

```
public class ServerModule extends HandlerModule {
    @Override
    protected void configureHandlers() {
        bindHandler(FetchAdminTaskCountAction.class, FetchAdminTaskCountHandler.class); // Bind Action: FetchAdminTaskCountAction to ActionHandler: FetchAdminTaskCountHandler
    }
}
```

##Configuring the Dispatch Module on the Client
Before you can start using the RPC Dispatcher you have to install an implementation of the Dispatcher in your client gin configuration. In most cases, the default `RpcDispatchAsyncModule` (included in GWTP) will be sufficient to install in the `ClientModule`

```
public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule());
        install(new ApplicationModule());
        install(new RpcDispatchAsyncModule()); // binds DispatchAsync to RpcDispatchAsync

        // DefaultPlaceManager Places
        bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(ErrorPlace.class).to(NameTokens.home);
        bindConstant().annotatedWith(UnauthorizedPlace.class).to(NameTokens.home);
    }
}
```
