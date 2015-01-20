# Presenter Gatekeeper

Blocking some presenters using the gatekeeper.

* Setup example of a gatekeeper:

```
public class LoggedInGatekeeper implements Gatekeeper {
  private final CurrentUser currentUser;

  @Inject
  LoggedInGatekeeper(CurrentUser currentUser) {
    this.currentUser = currentUser;
  }

  @Override
  public boolean canReveal() {
    return currentUser.isLoggedIn();
  }
}
```

* Use of the gatekeeper by specifying `@UseGatekeeper(LoggedInGatekeeper.class)` annotation on the presenter proxy.

```
@ProxyCodeSplit
@NameToken("userSettings")
@UseGatekeeper(LoggedInGatekeeper.class)
public interface MyProxy extends ProxyPlace<MainPagePresenter> {}
```

#Gatekeeper Annotations
You must make sure that the presenter handling errors is not using a Gatekeeper otherwise you risk running in an error. For this reason, it's good practice to annotate this presenter's proxy with @NoGatekeeper. The presenter handling errors is the one revealed by your custom PlaceManager's revealErrorPlace method. If you do not override that method, then it's the one revealed by your revealDefaultPlace method.

##DefaultGatekeeper
Sets the default gatekeeper.

### If you're using the Ginjector generator
Simply annotate your Gatekeeper with @DefaultGatekeeper.

### If you're not using the Ginjector generator
You must make sure that your custom ginjector has a getter method for your Gatekeeper. In your ginjector you can also use the @DefaultGatekeeper method to annotate the get method returning the Gatekeeper class you want to use for any proxy that is not annotated with @UseGatekeeper. If you use a @DefaultGatekeeper and would like to specify that a proxy shouldn't use any gatekeeper, then use the @NoGatekeeper annotation on that proxy.

##@UseGatekeeper
Check the gatekeeper for access.

##@NoGatekeeper
Anonymous access.

##Gatekeeper on Method Level
You can also use Gatekeeper on presenter method level. Here is an example for that:

```
public class ManufacturerPresenter extends Presenter<MyView, MyProxy>
        implements ManufacturerUiHandlers, ActionBarEvent.ActionBarHandler {
    @ProxyCodeSplit
    @NameToken(NameTokens.manufacturer)
    public interface MyProxy extends ProxyPlace<ManufacturerPresenter> {
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
