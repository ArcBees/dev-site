# Presenter Lifecycle

This describes the Presenter's lifecycle. Five lifecycle events occur during the lifecycle of the presenter onBind(), onUnbind(), onReveal(), onHide() and onReset().

##Diagram
![Presenter lifecycle diagram](http://i.imgur.com/kwjjGuv.png)

[Visio Diagram](https://dl.dropboxusercontent.com/u/49948294/wiki/presenter_lifecycle/Presenter-LifeCycle.vsd) 62KB

##onBind
When the presenter is bound the onBind() is called. This is a great place to register handlers and initialize anything expensive in this lifecycle.

* Example:

```
@Override
protected void onBind() {
  super.onBind();
  //... do something in presenter
}
```

##onUnbind
When the Presenter's unbound the onUnbind() is called. This is a good place to do some object house cleaning if needed.

* Example:

```
@Override
protected void onUnbind() {
  super.onUnbind();
  //... do something in presenter
}
```

##onReveal
When the Presenter is about to be revealed the onReveal() is called. Another way to describe this is when the Presenter is about to be displayed this is called.

* Example:

```
@Override
protected void onReveal() {
  super.onReveal();
  //... do something in presenter
}
```

##onHide
When the Presenter is about to be hidden the onHide() is called.

* Example:

```
@Override
protected void onHide() {
  super.onHide();
  //... do something in presenter
}
```

##onReset
When any Presenter with in the Presenter's hierarchy has used onReveal() the onReset() is called on the entire hierarchy of the presenter.

* Example:

```
@Override
protected void onReset() {
  super.onReset();
  //... do something in presenter
}
```

#Revealing

##Revealing a Presenter
In GWTP, navigating to a new page of your application is done by revealing a new presenter. There are many ways to do this.

###1) By manually modifying the URL in the user's browser

This is what happens when the user navigates to a bookmark or uses the back/forward button of his browser. In this case, the request is internally handled by the place manager and the correct presenter will be revealed, you don't have to do anything.

###2) By using a GWT `Hyperlink`

This is a nice way to implement navigation as it lets the user bookmark the link, open it in a new tab, etc. It also makes your application easier to discover by a search engine (when this is supported, see Issue 1). For this to work, simply add an `Hyperlink` widget to your page with the correct history token.

If you need to build a complex history token, for example when you use parameters or hierarchical places, then you may want to rely on the `buildHistoryToken` or one of the `buildRelativeHistoryToken` methods found in `PlaceManager`.

###3) By manually building a request and revealing the presenter

Sometimes, however, using an `Hyperlink` is not possible and you will need to reveal a proxy manually, within your code. For example, this is what you need to do if you want to reveal a presenter when the user clicks a button.

You do this by calling the `revealPlace` or one of the `revealRelativePlace` method of `PlaceManager`. To do this you need to build a `PlaceRequest` with the desired name token:

```
PlaceRequest.Builder myRequestBuilder = new PlaceRequest.Builder().nameToken("desiredNameToken");
// If needed, add URL parameters in this way:
myRequestBuilder = myRequestBuilder.with( "key1", "param1" ).with( "key2", "param2" );
placeManager.revealPlace( myRequest.build() );
```
For an example of this, see `MainPagePresenter.sendNameToServer()` in the sample application.

Revealing a place in this way will update the browser's URL, inserting a new element in its history which lets the user click "back" to go back to the previous page, or bookmark the current page. In cases where you don't want such a behavior you can pass `false` as the second parameter:`placeManager.revealPlace( myRequest, false );`

#FAQ

##Important
Always call the superclass method on an override. Otherwise the lifecycle chain will break between presenters.
