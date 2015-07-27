# Presenter Gatekeeper
Gatekeepers are used to protect places from unauthorized access. For example, a Gatekeeper could be set to protect a Presenter for which a user needs to log in.

Example of use:

```java
import javax.inject.Inject;

import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.project.shared.CurrentSession;

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

By using `canReveal()` when a certain condition is met, in this case if a user is logged in, GWTP will automatically use the Gatekeeper to ensure access to the Presenter is authorized.

## Gatekeeper Annotations
### @DefaultGatekeeper
Used to set a class as the default Gatekeeper for ProxyPlaces that are not annotated with `@UseGatekeeper`. This is done simply by annotating your Gatekeeper class with `@DefaultGatekeeper`.

```java
import javax.inject.Inject;

import com.gwtplatform.mvp.client.annotations.DefaultGatekeeper;
import com.gwtplatform.mvp.client.proxy.Gatekeeper;
import com.project.shared.CurrentSession;

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
This annotation lets you define a Gatekeeper to use for the ProxyPlace associated with your Presenter.

```java
@ProxyStandard
@NameToken(NameTokens.LOGIN)
@UseGatekeeper(LoggedInGatekeeper.class)
interface MyProxy extends ProxyPlace<MainPagePresenter> {}
```

### @NoGatekeeper
This annotation will tell a ProxyPlace to bypass a Gatekeeper even if one is set by default using `DefaultGatekeeper`.

```java
@ProxyStandard
@NameToken(NameTokens.SPLASH)
@NoGatekeeper
interface MyProxy extends ProxyPlace<SplashPresenter> {}
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
This annotation lets you define the parameters for a `GatekeeperWithParams` to use for the ProxyPlace associated with your Presenter.

```java
@ProxyStandard
@NameToken(NameTokens.SETTINGS_DASHBOARD)
@UseGatekeeper(HasAllRolesGatekeeper.class)
@GatekeeperParams(USER_ROLES)
interface MyProxy extends ProxyPlace<DashboardSettingsPresenter> {}
```


## Gatekeeper on method level
You can also use a Gatekeeper on the Presenter method level. Here is an example for that:

```java
public class ManufacturerPresenter extends Presenter<MyView, MyProxy>
        implements ManufacturerUiHandlers, ActionBarEvent.ActionBarHandler {
    @ProxyCodeSplit
    @NameToken(NameTokens.manufacturer)
    interface MyProxy extends ProxyPlace<ManufacturerPresenter> {
    }

    private final MyGatekeeper gatekeeper;

    @Inject
    ManufacturerPresenter(MyGatekeeper gatekeeper) {
        this.gatekeeper = gatekeeper;
    }

    // should be restricted
    @Override
    public void setRating(int value) {
        if(gatekeeper.canReveal()) { /* ... */ }
    }
}
```
