# Introduction
GWTP comes with built-in protection against [CSRF attacks](http://goo.gl/TJFBw). To protect your application against CSRF attacks, as described in [Security for GWT Applications](http://www.gwtproject.org/articles/security_for_gwt_applications.html) you need only follow these simple steps

# Setup
You have to specify the name of the security cookie you want to use. Do this by binding a string constant annotated with @SecurityCookie both on the client and on the server and make sure the cookie is sent across your app.

## Configuring the Client
Setup gin to bind the name of the security cookie to use by adding the following line in your `ClientModule`
```java
bindConstant().annotatedWith( SecurityCookie.class ).to("MYCOOKIE");
```

## Configuring the Server
Setup Guice to bind the same security cookie name in your `DispatchServletModule`
```java
bindConstant().annotatedWith( SecurityCookie.class ).to("MYCOOKIE");
```
## Configuring your RPC Actions
To protect an Action against CSRF attacks, extend the `ActionImpl` class like so:
```java
public class MyAction extends ActionImpl<MyResult> {
    public MyAction() {
    }
}
```

Note: If you have implemented your own Base Action that implements `com.gwtplatform.dispatch.shared.Action` make sure that the `isSecured()` method returns true, like so:
```java
    @Override
    public boolean isSecured() {
        return true;
    }
```
## Send the security cookie
The cookie should contain a session-dependent random number that cannot be easily guessed by an attacker. You can set this cookie yourself as soon as the page is loaded, or you can use the "JSESSIONID" cookie, which can be easily enabled on a Tomcat server or on Google AppEngine.

If you don't want to use the "JSESSIONID" cookie, say because you don't want to enable it on AppEngine, then you can add either HttpSessionSecurityCookieFilter or RandomSessionSecurityCookieFilter to your list of filters. To do so, add the following line at the top of your configureServlets method:
```java
 filter("/*").through( HttpSessionSecurityCookieFilter.class );
```
Note if you are using appengine all your host pages must be .jsp for the filter to work because appengine serves .html files statically.

## Not securing against CSRF attacks
Some public actions do not have to be secured against CSRF attacks. For example, an action to establish a session, or to obtain the welcome message of your site. To indicate that such actions do not have to be secured against CSRF attacks your action isSecured method should return false. Alternatively you can extend UnsecuredActionImpl.
