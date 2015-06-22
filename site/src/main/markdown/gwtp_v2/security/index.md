## Presenter Gatekeeper
Gatekeepers are used to protect places from unauthorized access.

This is a Gatekeeper example class:

```java
public class LoggedInGatekeeper implements Gatekeeper {
    private final CurrentSession currentSession;

    @Inject
    LoggedInGatekeeper(CurrentSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public boolean canReveal() {
        return currentSession.isLoggedIn();
    }
}
```

## Gatekeeper Annotations
### @DefaultGatekeeper
Used to set a class as the default Gatekeeper. This is done simply by annotating your Gatekeeper class with `@DefaultGatekeeper`.

```java
@DefaultGatekeeper
public class LoggedInGatekeeper implements Gatekeeper {
    private final CurrentSession currentSession;

    @Inject
    LoggedInGatekeeper(CurrentSession currentSession) {
        this.currentSession = currentSession;
    }

    @Override
    public boolean canReveal() {
        return currentSession.isLoggedIn();
    }
}
```

### @UseGatekeeper
Using a gatekeeper is done by specifying `@UseGatekeeper()` annotation on the presenter proxy.

```java
@ProxyStandard
@NameToken(NameTokens.LOGIN)
@UseGatekeeper(LoggedInGatekeeper.class)
public interface MyProxy extends ProxyPlace<MainPagePresenter> {}
```

### @NoGatekeeper
This annotation is used to gain anonymous access. It will bypass the Gatekeeper.

```java
@ProxyStandard
@NameToken(NameTokens.SPLASH)
@NoGatekeeper()
public interface MyProxy extends ProxyPlace<SplashPresenter> {}
```

## Inject parameters to a Gatekeeper
### @GatekeeperParams
This annotation lets you define the parameters for a `GatekeeperWithParams` to use for the Place associated with your proxy. Your custom Ginjector must have a method returning the `GatekeeperWithParams` specified in this annotation.

### @GatekeeperWithParams
Specialized `Gatekeeper` which needs additional parameters in order to find out if the protected Place can be revealed.
