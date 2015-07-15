# Token Formats

By default, GWTP allows two different token format to be used. They can be configured when the `DefaultModule` is installed in the [root GIN module]({{#gwtp.doc.url.initialize_gin}}) by calling `tokenFormatter()`. Both of these token formatters support "crawlable tokens".

1. [Configure the desired Token Formatter](#configure)
2. [Crawlable Tokens](#hashbang)
3. [Default Token Formatter](#default)
4. [Route Token Formatter](#route)
5. [Read Parameters](#parameters)

## Configure the desired Token Formatter {configure}
The Token Formatter can be configured when the DefaultModule is installed, in the [root GIN module]({{#gwtp.doc.url.initialize_gin}}). For example, installing the Route Token Formatter would look like this:

```java
package com.roler.res.client;

import com.google.gwt.inject.client.AbstractGinModule;
import com.gwtplatform.mvp.client.gin.DefaultModule.Builder;
import com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter;

public class ClientModule extends AbstractPresenterModule {
    @Override
    protected void configure() {
        install(new Builder()
                .tokenFormatter(RouteTokenFormatter.class)
                /* snip: configure default tokens */
                .build());

        /* snip: other installs */
    }
}
```

## Crawlable Tokens {hashbang}
Both the Default and Route Token Formatters support crawlable tokens, more commonly called *hashbangs*. Hashbangs are URL hashes that start with a "!". For example, `http://www.arcbees.com/#!/products/gwtp` contains a hashbang while `http://www.arcbees.com/#/products/gwtp` does not. The [Google Webmaster documentation](https://developers.google.com/webmasters/ajax-crawling/docs/getting-started) explains in details how it works. The [crawler documentation]({{#gwtp.doc.url.crawler}}) also explains how to set up a crawler service on Google AppEngine for your application.

## Default Token Formatter {default}
If there are no token formatters configured with the `DefaultModule`, the `ParameterTokenFormatter` will be used by default. This formatter supports name tokens and parameters separated by semicolons.

The pattern for formatted tokens is `#nameToken(;key=value)*`, leading to tokens like this: `#nameToken;key=value;key2=value2`. To avoid parsing issues, make sure your name token doesn't contain the following characters: ";", "=" or "/".

## Route Token Formatter {route}
The `RouteTokenFormatter` allows your application to use REST-like URLs with support for path and query parameters. Instead of wiring a hierarchy of several places to multiple presenters, a flat structure is built. Every place-token is bound to a single presenter.

Name Tokens must either start with "/" or "!/" and contains one or many segment separated by "/". For example, `http://www.arcbees.com/#!/products/gwtp` is a route token using a *hashbang*.

Routes can contain path and query-string parameters that can then be extracted from the `PlaceRequest` object.

### Path Parameters
A route token can contain path parameters. Those parameters are defined between curly braces and take up a whole path segment. For example, `/users/{userId}/photos/{albumId}/{photoId}` defines 3 path parameters: "userId", "albumId" and "photoId".

### Query Parameters
Every parameters given to a route token that is not a path parameter will be added as a query parameter. For example,

```java
new PlaceRequest.Builder()
        .nameToken("/users/{userId}/photos/{albumId}/{photoId}")
        .with("userId", "homer")
        .with("albumId", "hawaii")
        .with("photoId", "coconut")
        .with("size", "original")
        .with("color", "b&w")
        .build());
```

will lead to the following url: `https://www.mydomain.tld/#/users/homer/photos/hawaii/coconut?size=original&color=b%26w`.

**Note** that the parameters are encoded, so a call to `PlaceRequest.getParameter("color", "defaultValue")` will return "b&w".

## Read Parameters {parameters}
`PlaceManager.getCurrentPlaceRequest()` or `Presenter.prepareFromRequest(PlaceRequest)` can be used to access the current place request and the associated parameters.

Token Parameters can be accessed with `PlaceRequest.getParameter("key", "defaultValue")`. "defaultValue" is a value that will be returned if the parameter "key" is missing from the request in order to avoid unnecessary null-checks.

```java
// Override Presenter.prepareFromRequest() to get access to
// the current PlaceRequest before a presenter is revealed.
@Override
public void prepareFromRequest(PlaceRequest request) {
    // Access the "id" parameter in the current place request.
    // "0" is returned if "id" is missing.
    String id = request.getParameter("id", "0");
}
```
