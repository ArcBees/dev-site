# Layout Presenter

Now that you have a View, you can start working on an associated Presenter. Create `MainPagePresenter` with `Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy>` as a superclass. This means you have to define the inner interfaces `MyView` and `MyProxy`. We'll look at the proxy in more details later, but for now just add this to your class:

```
  public interface MyView extends View {}
  public interface MyProxy extends ProxyPlace<MainPagePresenter> {}
```

Again, add an injectable constructor (with the `@Inject` annotation) that simply forward its parameters to the superclass.

You will also need to define the `revealInParent()` method. This is where a presenter performs the operations required to become visible. The reason this is called revealIn _Parent_ is due to the hierarchical nature of presenters, which is [explained below](https://github.com/ArcBees/GWTP/wiki/Presenter-%22Slots%22). For now, our presenter will simply notify the top-level parent (a special Presenter built in GWTP) that it wants to be revealed. This is done by firing a `RevealRootContentEvent`.

The presenter class should now look like this:

```
public class MainPagePresenter extends
Presenter<MainPagePresenter.MyView, MainPagePresenter.MyProxy> {
  public interface MyView extends View {}

  public interface MyProxy extends ProxyPlace<MainPagePresenter> {}

  @Inject
  public MainPagePresenter(EventBus eventBus, MyView view, MyProxy proxy) {
    super(eventBus, view, proxy);
  }

  @Override
  protected void revealInParent() {
    RevealRootContentEvent.fire( this, this );
  }
}
```
