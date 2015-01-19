#Unit Testing Cookbook

One of the most important premises behind the Model-View-Presenter (MVP) pattern is to allow for better testability of your application.

GWT provides testing facilities through GWTTestCase. However, you may have experienced that running GWTTestCase's are significantly slower than running JRE-based unit tests. That's because GWTTestCase's require to fire up a headless browser to run each test. Each test might take tens of seconds to run. This strategy does not scale well to a large test suite.

In this article, I'll show you how to write unit tests for your GWTP Presenters, using the power of Jukito.

Leveraging the MVP pattern, the unit tests you'll write for your Presenters will be way, way faster than GWTTestCases
. Furthermore, unit-testing your Presenters will force you to migrate most of your app's logic from the View to the
Presenter, hence creating what I'll call a "dumb" View.

All code samples below will be found in the gwtp-sample-unit-testing project of gwtp-samples. Feel free to clone the code and run the unit tests yourself.

Without further ado, let's get started with a simple example.

##Verifying that your Presenter calls some method of your View

Let's create our root `ApplicationPresenter`:

```
package com.gwtplatform.samples.unittesting.client.application;

import javax.inject.Inject;

import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.samples.unittesting.client.place.NameTokens;

public class ApplicationPresenter extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> {
    interface MyView extends View {
        void setTitle(String title);
    }

    @ProxyStandard
    @NameToken(NameTokens.home)
    interface MyProxy extends ProxyPlace<ApplicationPresenter> {
    }

    @Inject
    ApplicationPresenter(EventBus eventBus,
                         MyView view,
                         MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);
    }

    @Override
    protected void onReveal() {
        super.onReveal();

        getView().setTitle("GWTP Samples - Unit Testing");
    }
}

```
What we'll want here is to verify that the title is sent to the `MyView` when the `ApplicationPresenter` is revealed. Here's our unit test for this:

```
package com.gwtplatform.samples.unittesting.client.application;

import javax.inject.Inject;

import org.jukito.JukitoRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;

@RunWith(JukitoRunner.class)
public class ApplicationPresenterTest {
    @Inject
    ApplicationPresenter applicationPresenter; // an actual ApplicationPresenter instance (not a mock!)

    @Test
    public void onReveal_anytime_setsTitleIntoView(ApplicationPresenter.MyView myView) { // A mock. The same instance is shared by applicationPresenter
        //given
        String title = "GWTP Samples - Unit Testing";

        //when
        applicationPresenter.onReveal();

        //then
        verify(myView).setTitle(title);
    }
}
```
Here are a few things to know in order to understand how Jukito handles this test.

First of all, Jukito will inject an instance of `ApplicationPresenter` for you. You might notice the `@Inject`annotation above the `ApplicationPresenter` declaration. Jukito will find those injectable fields in your test and populate them, using Guice to resolve all dependencies needed to create the field. The `ApplicationPresenter` instance we have here is an actual (not a mock!) instance that has been injected with mocks.

The weird thing in this test is probably the `MyView` instance that is found in my test method's parameters. By default, Jukito will automatically mock interfaces (e.g. `MyView`, `MyProxy` and `EventBus`) and bind them as **singletons**. What this means is that the `MyView` instance we receive in the test method is **the same** `MyView` instance that will be injected in the `ApplicationPresenter`. So, when I call `applicationPresenter.onReveal()`, the `MyView` instance found in our test method will be used.

Here's our `ApplicationView`:

```
package com.gwtplatform.samples.unittesting.client.application;

import javax.inject.Inject;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {
    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @UiField
    Label titleLabel;

    @Inject
    ApplicationView(Binder binder) {
        initWidget(binder.createAndBindUi(this));
    }

    @Override
    public void setTitle(String title) {
        titleLabel.setText(title);
    }
}
```

As you can see, the `ApplicationView`'s code is kept to a bare minimum (hence the "dumb" View pattern I mentioned earlier). All the Javascript runtime dependent parts (here, the `Label` widget) are kept in the View, so we don't have to use a GWTTestCase.

##Testing that a PresenterWidget is assigned to a slot during onBind()
One common pattern with GWTP is to add a `PresenterWidget` to the page during the `onBind` lifecycle phase. Let's see how we can verify that our `HeaderPresenter` has been added. Here's the code we added to our `ApplicationPresenter`:

```
static final Object HEADER_SLOT = new Object();

private final HeaderPresenter headerPresenter; // a PresenterWidget

@Inject
ApplicationPresenter(EventBus eventBus,
                     MyView view,
                     MyProxy proxy,
                     HeaderPresenter headerPresenter) {
    super(eventBus, view, proxy, RevealType.Root);

    this.headerPresenter = headerPresenter;
}

// ...

@Override
protected void onBind() {
    super.onBind();

    setInSlot(HEADER_SLOT, headerPresenter);
}
```

The simplest test we can write to make sure `HeaderPresenter` has been set in the `HEADER_SLOT` looks like this:

```
@Test
public void onBind_anytime_setsHeaderPresenterInSlot(HeaderPresenter headerPresenter,
                                                     ApplicationPresenter.MyView myView) {
    verify(myView).setInSlot(ApplicationPresenter.HEADER_SLOT, headerPresenter);
}
```

As you can see, we're letting Jukito inject the `HeaderPresenter` in our test. If you remember correctly, Jukito will bind the `HeaderPresenter` as a singleton, making `ApplicationPresenter` and this test share the same `HeaderPresenter` instance.

One thing I don't like about the test above is that the test hides the `onBind` call. The `onBind` method of `ApplicationPresenter` is called just after `ApplicationPresenter`'s constructor. Here, we don't have to call `onBind` manually, since it's called just after our `ApplicationPresenter` has been field-injected in our test.

What I'll do instead is modify our test slightly to use Guice `Provider` and have better control on the `ApplicationPresenter`'s instatiation process:

```
@Inject
Provider<ApplicationPresenter> provider;

// ...

@Test
public void onBind_anytime_setsHeaderPresenterInSlot(HeaderPresenter headerPresenter,
                                                     ApplicationPresenter.MyView myView) {
    //when
    provider.get(); // this call will instantiate the ApplicationPresenter, then call onBind()

    //then
    verify(myView).setInSlot(ApplicationPresenter.HEADER_SLOT, headerPresenter);
}
```

##Stubbing Async service response
Asynchronous service calls (ie: REST, RPC) are common in web applications, but running your tests agains't a server is not ideal. Using Mockito, we can simulate calls and invoke the callback to test the behaviour of the `onSuccess` and `onFailure` methods. We first need to create the Stubber

```
public class AsyncStubber {
    public static <T, C extends AsyncCallback> Stubber callSuccessWith(final T data) {
        return Mockito.doAnswer(new Answer<T>() {
            @Override
            @SuppressWarnings("unchecked")
            public T answer(InvocationOnMock invocationOnMock) throws Throwable {
                final Object[] args = invocationOnMock.getArguments();
                ((C) args[args.length - 1]).onSuccess(data);
                return null;
            }
        });
    }

    public static <C extends AsyncCallback> Stubber callFailureWith(final Throwable caught) {
        return Mockito.doAnswer(new Answer<Throwable>() {
            @Override
            @SuppressWarnings("unchecked")
            public Throwable answer(InvocationOnMock invocationOnMock) throws Throwable {
                final Object[] args = invocationOnMock.getArguments();
                ((C) args[args.length - 1]).onFailure(caught);
                return null;
            }
        });
    }
}
```

Then let's say we have this service interface and this presenter :

```
public interface AppleServiceAsync {
    void getApples(AsyncCallback<List<Apple>> callback);
}

public class ApplesPresenter extends PresenterWidget<ApplesPresenter.MyView> {
    // ...
    @Override
    protected void onReveal() {
        appleService.getApples(new AsyncCallback<List<Apple>>() {
        @Override
        public void onFailure(Throwable caught) {
        // ...
        }

        @Override
        public void onSuccess(List<Apple> apples) {
            getView().displayApples(apples);
        }
    }
    // ...
}
```

We can use our AsyncStubber like this :

```
@Test
public void getApples_success_displaysApples(ApplesPresenter applesPresenter,
                                             AppleServiceAsync appleService,
                                             ApplesPresenter.MyView view) {
    // given
    List<Apple> apples = createSomeApples();
    AsyncStubber.callSuccessWith(apples)
                    .when(appleService)
                    .getApples((AsyncCallback) any());

    // when
    applesPresenter.onReveal();

    // then
    verify(view).displayApples(apples);
}
```
