This explains bootstrapping and using resources. This goes over resource types Messages, Constants, CSS and Images.

##Reference
The code snippets are taken from GWTP-Samples-Tab sample.

* [Gin Directory](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/gin)
* [Gwtptabsample.gwt.xml](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/Gwtptabsample.gwt.xml)
* [ClientModule](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/gin/ClientModule.java)
* [HomeNewsView](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/homenews/HomeNewsView.java)
* [HomeNewsView.ui.xml](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/homenews/HomeNewsView.ui.xml)

###Resources Directory
The properties, images and css file types are located in the resources folder. The resources need to exist in the same package or in the resource folder matching package to be used.

* [Resource Folder Location](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-tab/src/main/resources/com/gwtplatform/samples/tab/client/resources)

##CSS Injection
Loading resources is as simple as binding a eager singleton in the ClientModule.

* Example of binding resource loader in the client module. [ClientModule](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/gin/ClientModule.java)

```
public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new DefaultModule(DefaultPlaceManager.class));
        install(new ApplicationModule());
        //...

        // Load and inject CSS resources
        bind(ResourceLoader.class).asEagerSingleton();
    }
}
```

* Example of the ResourceLoader class using `ensureInjected()`: [ResourceLoader](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/gin/ResourceLoader.java)

```
public class ResourceLoader {
    @Inject
    ResourceLoader(AppResources resources) {
        resources.styles().ensureInjected();
        resources.sprites().ensureInjected();

        //... Inject more css into the document here on boot
    }
}
```

###Demo Css Injection
* And Resources might be used like this: [HomeNewsView](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/homenews/HomeNewsView.java)

```
//...
    @UiField(provided = true)
    AppResources resources;

//...

    private final AppConstants appConstants;
    private final AppMessages appMessages;

    @Inject
    HomeNewsView(Binder uiBinder,
                 AppResources resources,
                 AppConstants appConstants,
                 AppMessages appMessages) {
        this.resources = resources;
        this.appConstants = appConstants;
        this.appMessages = appMessages;

        initWidget(uiBinder.createAndBindUi(this));
    }
//...
```
* Example of using resources in UiBinder: [HomeNewsView.ui.xml](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/homenews/HomeNewsView.ui.xml)

```
<ui:UiBinder xmlns:ui="urn:ui:com.google.gwt.uibinder" xmlns:g="urn:import:com.google.gwt.user.client.ui">

    <ui:with field="resources"
             type="com.gwtplatform.samples.tab.client.resources.AppResources"/>

    <g:HTMLPanel>
        <!-- Resource Examples Below -->
        <div>Example use of Messages:</div>
        <g:HTML ui:field="orderText"/>

        <div>Example use of Constants:</div>
        <g:HTML ui:field="gwtpTitle"/>

        <div>Example use of ImageResource:</div>
        <g:Image resource="{resources.logo}" styleName="{resources.styles.logo}"/>

        <div class="{resources.styles.divBorder}">Example use of Css Resource:</div>
    </g:HTMLPanel>
</ui:UiBinder>
```

###Demo AppMessages
* AppMessages might be setup like this: [AppMessages](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/resources/AppMessages.java)

```
@LocalizableResource.DefaultLocale("en")
public interface AppMessages extends Messages {
     String iWillOrderNumberOf(Integer number);
}
```
* And AppMessages might be used like this: [HomeNewsView](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-tab/src/main/java/com/gwtplatform/samples/tab/client/application/homenews/HomeNewsView.java)

```
//...
    private final AppConstants appConstants;
    private final AppMessages appMessages;

    @Inject
    HomeNewsView(Binder uiBinder,
                 AppResources resources,
                 AppConstants appConstants,
                 AppMessages appMessages) {
        this.resources = resources;
        this.appConstants = appConstants;
        this.appMessages = appMessages;

        initWidget(uiBinder.createAndBindUi(this));
    }
//...
    @Override
    public void display() {
        String orderNumberOf = appMessages.iWillOrderNumberOf(5);
        orderText.setText(orderNumberOf);

        String gwtpTitle = appConstants.gwtpPlatformTitle();
        this.gwtpTitle.setText(gwtpTitle);
    }
//...
```
