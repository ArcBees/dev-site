# The visual: View

The second piece of the MVP architectural pattern is the View. Its purpose is to separate the application logic from the "display" logic. By implementing the UI this way, multiple views can be used to represent the same application (ie: different views for different countries, different platforms, etc).

The views in GWTP aren't much different from GWT widgets, except that they talk to a presenter. So the syntax remains pretty similar to GWT's. The preferred way of using complex views in GWT would be with UiBinder. A view can be defined by a declarative syntax in a `.ui.xml` file. Let's continue the previous example as seen [here]({{#gwtp.doc.url.presenter}}).

_SimpleView.ui.xml_

```xml
<ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
        xmlns:g='urn:import:com.google.gwt.user.client.ui'>
    <g:HTMLPanel>
        Current user's name: <span ui:field="username"></span>
    </g:HTMLPanel>
</ui:UiBinder>
```


_SimpleViewImpl.java_

```java
public class SimpleViewImpl extends ViewImpl implements SimplePresenter.MyView {
    interface Binder extends UiBinder<HTMLPanel, SimpleView> {
    }

    @UiField
    SpanElement username;

    @Inject
    SimpleView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    public void displayCurrentUserName(String username) {
        this.username.setText(username);
    }
}
```

_The presenter will be omitted for now._

The first things to notice here are the `extends` and `implements` on the class. In GWTP, views have to extend an implementation of the `View` interface. There are multiple implementations available, but let's keep it simple with the regular `ViewImpl`. Also, a view should implement a Presenter's view contract. As mentioned in the Presenter section, a Presenter should declare the interface of its view, so this view implements its Presenter's view interface.

If the view uses the UiBinder (like here), it should declare its UiBinder's mapping. That's what the `Binder` interface is for. The result of `binder.createAndBindUi(this)` is passed to the initWidget method. In GWTP, when it is possible, it is preferred to inject an instance by constructor using dependency injection.

The elements declared in the `.ui.xml` file can be referenced in the view by annotating them with `@UiField`. See [GWT's UiBinder](http://www.gwtproject.org/doc/latest/DevGuideUiBinder.html) for more information.
