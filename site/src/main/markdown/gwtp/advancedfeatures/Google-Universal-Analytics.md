# Google Universal Analytics

GWTP is designed for compatibility with Arcbees' Universal Analytics library.

The Universal Analytics library lets you make calls to Google Analytics directly from Java code.

Learn more at: https://github.com/ArcBees/universal-analytics

## Automatically tracking navigation events.

It's common to automatically track navigation events, so that you can see the places users visit on your site:

You can do this by creating a NavigationTracker class:

```
package YOUR_PACKAGE_NAME
 
import javax.inject.Inject;
 
import com.arcbees.analytics.shared.Analytics;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.NavigationEvent;
import com.gwtplatform.mvp.client.proxy.NavigationHandler;
import com.gwtplatform.mvp.shared.proxy.TokenFormatter;
 
public class NavigationTracker implements NavigationHandler {
  private final Analytics analytics;
  private final TokenFormatter tokenFormatter;
 
  @Inject
  NavigationTracker(TokenFormatter tokenFormatter, EventBus eventBus, Analytics analytics) {
    this.analytics = analytics;
    this.tokenFormatter = tokenFormatter;
    eventBus.addHandler(NavigationEvent.getType(), this);
  }
 
  @Override
  public void onNavigation(final NavigationEvent navigationEvent) {
    analytics.sendPageView().documentPath(tokenFormatter.toPlaceToken(navigationEvent.getRequest())).go();
  }
} 
``` 
and then binding it in your gin module:
```
  bind(NavigationTracker.class).asEagerSingleton();
```