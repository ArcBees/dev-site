## Presenter Gatekeeper
Gatekeepers are used to protect places from unauthorized access.

Example of use:

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
This annotation lets you define a `Gatekeeper` to use for the `Place` associated with your proxy.

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
### GatekeeperWithParams
Specialized Gatekeeper which needs additional parameters in order to find out if the protected Place can be revealed.

 Example of use:
 
```java
public class HasAllRolesGatekeeper implements GatekeeperWithParams {
	private final CurrentUser currentUser;
	private String[] requiredRoles;

	@Inject
	public HasAllRolesGatekeeper(CurrentUser currentUser) {
		this.currentUser = currentUser;
	}
	
	@Override
	public GatekeeperWithParams withParams(String[] params) {
		requiredRoles = params;
		return this;
	}
	
	@Override
	public boolean canReveal() {
		return currentUser.getRoles().containsAll(Arrays.asList(requiredRoles);
	}
}
```

### @GatekeeperParams
This annotation lets you define the parameters for a `GatekeeperWithParams` to use for the Place associated with your proxy.

```java
@ProxyStandard
@NameToken(NameTokens.SETTINGS_DASHBOARD)
@UseGatekeeper(HasAllRolesGatekeeper.class)
@GatekeeperParams(USER_ROLES)
interface MyProxy extends ProxyPlace<DashboardSettingsPresenter> {}
```