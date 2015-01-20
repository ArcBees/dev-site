# Website Embedding

Embedding GWTP application into a website.

##Embedding a GWTP app in a webpage
Say you have a standard HTML page and want the GWTP to appear within the `mainContent` element on that page. To do this, simply write your custom `RootPresenter` in this way:

```
public class MyRootPresenter extends RootPresenter {

  public static final class MyRootView extends RootView {
    @Override
    public void setInSlot(Object slot, IsWidget widget) {
      RootPanel.get("mainContent").add(widget);
    }
  }

  @Inject
  MyRootPresenter(EventBus eventBus, MyRootView myRootView) {
    super( eventBus, myRootView );
  }
}
```

Then, in your gin module, replace the following line: `bind(RootPresenter.class).asEagerSingleton();`
by: `bind(RootPresenter.class).to(MyRootPresenter.class).asEagerSingleton();`
