# Bottom-Top Communication: ViewWithUiHandlers
In any MVP application, there will be a time when a user event needs to trigger some kind of computation or succession of other events. The way to link a view event to a Presenter is with the Bottom-Top Communication pattern.

When the need of a View-To-Presenter communication arises, it is now time to have the View implement `HasUiHandlers`. In GWTP, there's an abstract class that does that already, it is called `ViewWithUiHandlers`.

The first step is to define an interface that extends `UiHandlers` that describes what actions the current Presenter can do. Let's imagine a View-Presenter couple of a screen where a user can save a user's information on the system. First declare the UiHandlers:

```java
interface UserEditUiHandlers extends UiHandlers {
  void onSave(String username);
}
```

Then let the Presenter know it'll have to handle this action:

```java
class UserEditPresenter extends Presenter<UserEditPresenter.MyView> implements UserEditUiHandlers {
    // This Presenter now has to implement the onSave method
    @Override
    public void onSave(String username) {
        // call server with this username
    }
}
```

It is also necessary to let the View know it has access to the UiHandlers. So at the View interface declaration :

```java
interface MyView extends View, HasUiHandlers<UserEditUiHandlers> {
    // declare view methods as usual
}
```

Finally, when implementing the view, the class signature changes a little bit as well.

```java
class UserEditView extends ViewWithUiHandlers<UserEditUiHandlers> implements MyView {
 // normal view implementation
}
```

So now the class structure is well established. To get it working in a real case though, the Presenter has to set itself as the view's UiHandler at runtime by calling the `setUiHandlers` method in its constructor.

```java
class UserEditPresenter extends Presenter<UserEditPresenter.MyView> implements UserEditUiHandlers {
    @Inject
    UserEditPresenter(....) {
        getView().setUiHandlers(this);
    }
}
```

Voilà! The view can then communicate with its Presenter and send whatever data it needs to:

```java
class UserEditView // ..... {
    @UiHandler("saveButton")
    void onSaveButtonClick(ClickEvent event) {
        String username = ... ; // Get the username from a UI element like a textbox

        // and send the information to the Presenter
        getUiHandlers().onSave(username);
    }
}
```

