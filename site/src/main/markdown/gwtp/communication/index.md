#Introduction
GWTP makes it possible to link each of your `ActionHandler`s with a server-side `ActionValidator` that determines whether or not the current client can execute the action. Validation logic is up to you and is not limited to inspecting the users, for example you could use it to deny access to some actions at certain times of the day. In this document, however, we focus to the most frequent use case of session-based validation.

##Changes from gwt-dispatch
This section details how GWTP deviates from [gwt-dispatch](http://code.google.com/p/gwt-dispatch/). If you never used gwt-dispatch before, feel free to skip this.

In gwt-dispatch, you either chose a secure dispatcher or a non-secure one. It was difficult to control security on a case by case basis. In GWTP you can precisely control security settings of each of your actions independently. One of your action is open to the world and does not need protection against XSRF attacks? You can do this. One of your action should only be used by the administrator? You can package that right into the `ActionValidator`.

To keep things as simple as possible, we've deleted a lot of the "secure" classes available in gwt-dispatch. This is because security features (such as foiling XSRF attacks) are built in the base classes themselves. The result is an API that should be simpler to use and understand.

Another addition to gwt-dispatch is the ability to map actions to different urls. This lets you cleanly organize your application and can lead to clearer reports. This should not be used for security though, since the actual path the action comes in is not verified server-side.

#Actions
Actions deviate slightly from gwt-dispatch. You can keep the default by extending `ActionImpl`, this will configure the action url to "dispatch/", as before. It will also make your action secure against XSRF attacks, at the cost of having to define a security cookie (more on this later). If you don't care about XSRF attacks or just want to quickly put something together, you can inherit from `UnsecuredActionImpl` instead. Don't worry, this decision can easily be reverted later.

##Manually defining an action url
If you want a custom url for your action, simply implement the `Action` interface and make sure your `getServiceName` method returns the desired url. You must also override `isSecured` to return `true` or `false` depending whether or not you want security against XSRF attacks.

Here's a little trick that will let you automatically build urls of the form `"dispatch/MY_ACTION_CLASS"`, at the cost of some java reflexion:

```java
public abstract class AbstractAction<R extends Result> implements Action<R> {
  @Override
  public String getServiceName() {
    String className = this.getClass().getName();
    int namePos = className.lastIndexOf(".") + 1;
    className = ActionImpl.DEFAULT_SERVICE_NAME + className.substring(namePos); 

    return className;
  }

  @Override
  public boolean isSecured() {
    return true;
  }
} 
```

Here's an example of an action built on this scheme, this action will use the `"dispatch/GetProducts"` url:

```java
public class GetProducts extends AbstractAction<GetProductsResult> {
    private String categoryId;

    public GetProducts() {}

    public GetProducts(final String categoryId) {
      this.categoryId = categoryId;
    }

    public String getCategoryId() {
      return categoryId;
    }
} 
```

#Guice/gin configurations
I will assume here that you're using the default. Advanced configurations will be discussed in another page. Just so you know, you can make your own `DispatchModule` with your own `ExceptionHandler` that will override `onFailure`. Anyway, gin configurations for dispatch almost always only need to add the default to your Ginjector class like this : 

`@GinModules({ DispatchAsyncModule.class, YourClientModule.class})`

Then don't forget to add a deffinition : 

`DispatchAsync getDispatcher();`

No big changes here.

For guice
The only change here is that we don't use hard coded dispatch string in your module that extends `ServletModule`. 

`serve("/yourappname/" + ActionImpl.DEFAULT_SERVICE_NAME + "*").with(DispatchServiceImpl.class);`

#!ActionValidators
Here's the big change. `ActionValidators` are classes bound to an `ActionHandler` that evaluates if the action can or cannot be executed, typically using the user logged into the current session. They are server-side, secure and reusable.

That change introduced a new overload of `bindHandler` : `bindHandler(action.class, actionHandler.class, actionValidator.class)`. 

An action that can be executed by all users, logged-in or not, will be bound to an `ActionValidator` of looking like this : 
```java
public class PublicActionValidator implements ActionValidator  {
  @Override
  public boolean isValid(Action<? extends Result> action) {
    return true;
  }
} 
```

The `isValid` method should return `true` when the user can execute the action, and `false` otherwise. The `Action` itself is also passed as a parameter so you can inspect it if needed.

You never have to write such a trivial `PublicActionValidator`, however, since this is exactly what you will get by using the 2-parameter version of `bindHandler`: `bindHandler(action.class, actionHandler.class)`. Let's therefore look at a more interesting example that uses the `UserServiceFactory` of !AppEngine to determine whether the user is an admin:
```java
public class AdminActionValidator implements ActionValidator  {
  @Override
  public boolean isValid(Action<? extends Result> action) {
    UserService user = UserServiceFactory.getUserService();
    
    return user.isUserAdmin();
  }
} 
```

Short and simple. Now only admin of your appdomain can use actions that are bound to this `AdminActionValidator`. You could also use action validators to check that a user is logged in, that he has some specific rights, that the action can only be accomplished at a given time of the day (i.e. for cleanup operations), etc.

##What happens when `isValid` returns false ?

Dispatch will return a new service exception : 
```java
throw new ServiceException( actionValidator.getClass().getName() + actionValidatorMessage + action.getClass().getName() );
```

and the message : 
```java
private final static String actionValidatorMessage = " couldn't allow access to action : ";
```

#Conclusion
`ActionValidator` is one of the many features that makes Gwt-Platform one of the best and secure command pattern frameworks out there and we are eager to give you more and more features. Feel free to make comments, ask questions, this document wouldn't exist without your feedback.