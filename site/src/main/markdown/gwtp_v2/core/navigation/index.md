# Linking Places to a Token
Some presenters can be associated to a specific URL. GWTP makes it possible by managing the hash section of the URL. This is whatever comes after the "#" (ie: in `https://www.arcbees.com/#!/products/gwtp`, the hash is `!/products/gwtp`).

## Prepare the presenter
In order for a presenter to be a place, a regular Presenter with a [proxy]({{#gwtp.doc.url.proxy}}) has to be created first. From there the following changes are required:

1. The super-interface has to be changed to `ProxyPlace`;
2. It has to be annotated with `@NameToken("a-unique-token")`. It is possible to configure more than one name token on a single presenter by passing more than one argument to `@NameToken`.

```java
package com.gwtplatform.sample.application.cars;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.sample.NameTokens;
import com.gwtplatform.sample.application.cars.CarsPresenter.MyProxy;
import com.gwtplatform.sample.application.cars.CarsPresenter.MyView;

public class CarsPresenter extends Presenter<MyView, MyProxy> {
    interface MyView extends View {}

    @NameToken(NameTokens.CARS)
    @ProxyStandard
    interface MyProxy extends ProxyPlace {}

    /* snip */
}
```

Notice that the name token is not hard-coded. Instead the value is extracted to another class named `NameTokens`. This is to facilitate reusability and maintainability of name tokens across the application:

```java
package com.gwtplatform.sample;

public class NameTokens {
    public static final String CARS = "/cars";
}
```
