# Migrating from version 1.1 to 1.2

There is very little breaking changes between 1.1 and 1.2, but some classes got moved from one package to another.

## Moved classes
You will want to go through all your files and update your imports for the following classes. Using modern IDE, a simple search and replace across all files may be enough to save you the hassle.

Old package | New package
--- | ---
com.gwtplatform.mvp.client.proxy.PlaceRequest | com.gwtplatform.mvp.shared.proxy.PlaceRequest
com.gwtplatform.mvp.client.proxy.TokenFormatter | com.gwtplatform.mvp.shared.proxy.TokenFormatter
com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter | com.gwtplatform.mvp.shared.proxy.ParameterTokenFormatter
com.gwtplatform.mvp.client.proxy.RouteTokenFormatter | com.gwtplatform.mvp.shared.proxy.RouteTokenFormatter

## New mandatory GIN bindings
If you don't install `DefaultModule` or `RestDispatchAsyncModule`, you will likely end up with missing binding issues. To fix them, add this line in one of your GIN module:

```
install(new CommonGinModule());
```
