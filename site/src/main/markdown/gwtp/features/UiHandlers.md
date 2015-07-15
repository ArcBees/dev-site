# UiHandlers

## Reference
[Example UiHandler Use](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/widget/login)

##Supervising Controller
It's often useful for the view to delegate some of its actions to the presenter using a pattern sometimes referred to as a "supervising controller".

![Supervising Controller](http://4.bp.blogspot.com/_lUDLhPj-dyg/SYsogEymUWI/AAAAAAAAAB0/CSSLkHiRTMo/s1600/mvp.png)

This has also been [advocated by Google](http://code.google.com/webtoolkit/articles/mvp-architecture-2.html#complex_ui) as a good way to benefit from the nice @UiHandler annotation at your disposal when you use @UiBinder. The main difference with the original presenter pattern is that the view keeps a link back to the presenter in order to invoke some of its methods, instead of the presenter registering callbacks towards the view.

* The "supervising controller" pattern can easily be implemented using the tools provided by GWTP. First you need to create an interface that extends UiHandlers and add all the methods your view needs to call. For example:

```java
public interface UserProfileUiHandlers extends UiHandlers {
    void onSave();
}
```

It would be nice to define this class directly within UserProfileView and call it MyUiHandlers. Unfortunately, this introduces a dependency cycle according to the [Java Language Specification 8.1.4](http://java.sun.com/docs/books/jls/third_edition/html/classes.html). (We have advocated such an inner declaration in the past as it often works, but it seems due to a bug in Eclipse batch builder and [javac](http://bugs.sun.com/view_bug.do?bug_id=6695838). It is therefore better to avoid the cycle.)

* Your presenter then needs to implement this interface:

```java
public class UserProfilePresenter extends Presenter<UserProfilePresenter.MyView, UserProfilePresenter.MyProxy>
    implements UserProfileUiHandlers {
    //...
    @Override
    public void onSave() {
        doSomething();
    }
    //...
}
```

* Then you have to connect these methods to your view. This is done by letting MyView extend HasUiHandlers and by calling setUiHandlers() within your presenter’s constructor to finalize the connection:

```java
public class UserProfilePresenter extends Presenter<UserProfilePresenter.MyView, UserProfilePresenter.MyProxy>
    implements UserProfileUiHandlers {
    public interface MyView extends View, HasUiHandlers<UserProfileUiHandlers>{
    }

    @Inject
    ExamplePresenter(EventBus eventBus,
                     MyView view,
                     MyProxy proxy) {
        super(eventBus, view, proxy);
        getView().setUiHandlers(this);
    }
    //...
}
```

Be careful: since the view is instantiated before the presenter, the setUiHandlers() method will be called after the view’s constructor has executed. This means you cannot refer to the presenter within your view’s constructor. Also, it’s important to call setUiHandlers() early, otherwise you might run into situations where your view needs to access a ui handler method before it even has a reference to it. Just to be on the safe side, you should probably check for null before invoking any ui handler method.

* The last step is to let your view extends ViewWithUiHandlers or PopupViewWithUiHandlers instead of ViewImpl or PopupViewImpl. Then you’re ready to use your controls via getUiHandlers(). As a result, using the great @UiHandler annotation is now very easy:

```java
public class ExampleView extends ViewWithUiHandlers<UserProfileUiHandlers> implements MyView {
    //...
    @UiHandler("saveButton")
    void onSaveButtonClicked(ClickEvent event) {
        if (getUiHandlers() != null) {
            getUiHandlers().onSave();
        }
    }
```

##More Information
For more details, an article can be found on our blog: [Reversing the MVP pattern and using @UiHandler](http://arcbees.wordpress.com/2010/09/03/reversing-the-mvp-pattern-and-using-uihandler/)
