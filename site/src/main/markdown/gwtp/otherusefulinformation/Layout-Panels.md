# Layout Panels

Presenters can change the root layout using the `RevealRootLayoutContentEvent`.

## Using Layout Panels
GWT 2.0 offers new and very interesting [layout panels](http://code.google.com/webtoolkit/doc/latest/DevGuideUiPanels.html#LayoutPanels) that makes it easy to design web applications using the entire browser window, just like desktop applications. If you have a top-level presenter that behaves as a layout panel, then its `revealInParent()` method should look like this:

* Example:

```
@Override
protected void revealInParent() {
  RevealRootLayoutContentEvent.fire( eventBus, this );
}
```

Better yet, your application can contain both pages that behave as layout panels and others that behave like standard web pages (i.e. [basic panels](http://code.google.com/webtoolkit/doc/latest/DevGuideUiPanels.html#BasicPanels)). The latter simply have to fire a `RevealRootContentEvent`.
