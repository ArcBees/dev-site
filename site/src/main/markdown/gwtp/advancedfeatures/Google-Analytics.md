# Google Analytics

#Introduction
We worked on the seamless integration of your presenter-based application with Google Analytics. This new feature will automatically track any navigation event and presenter change provided you add the following bindings to your gin module:

```java
  bind(GoogleAnalytics.class).to(GoogleAnalyticsImpl.class).in(Singleton.class);
  bindConstant().annotatedWith(GaAccount.class).to("UA-XXXXXXX-X");
  bind(GoogleAnalyticsNavigationTracker.class).asEagerSingleton();
```

IMPORTANT NOTE, If you install the default module, GoogleAnalytics.class will already be bound.

The first line let's you bind the various google Analytics function to our implementation, this line can be avoided by installing the new [PortingV1#Added_a_DefaultModule default module] that we introduce with GWTP 0.6. The second line is where you bind your Google Analytics account to this application and the last line is where the tracker is actually instanciated alongside your application. As you can see, these three little lines will take care of everything and you'll even be able to go further by injecting the GoogleAnalytics interface wherever you need it, so you can easily track custom events right from your GWT code.

##Tracking events

It is also possible to tracks event by injecting `GoogleAnalytics` in whereever you want and by calling manually `googleAnalytics.trackEvent(category, action);` where category is a string name you supply for the group of objects you want to track and action is a string that is uniquely paired with each category, and commonly used to define the type of user interaction for the web object. 

You can also call the methods with two additionnal arguments: 
 * optLabel An string to provide additional dimensions to the event data;
 * optValue An integer that you can use to provide numerical data about the user event.

##Adding multiple Google Analytics account

The last piece that you can do by injecting `GoogleAnalytics` is to track events and pages to multiple tracker. The first thing you need to do before being able to track something on another account is to add that account to your tracker by calling `googleAnalytics.addAccount(trackerName, gaAccount);` where tracker name is obviously the tracker name that you want to add and gaAccount is your Google Analytics google analytics that you want to use. After that, you can then call trackEvent and trackPage by adding the trackerName in your call to be able to track pages and events.