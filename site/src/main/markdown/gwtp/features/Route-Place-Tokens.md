# Route Place Tokens

## Introduction

It isn't possible to assign REST like URLs to your presenters with the default hierarchical-place mechanism. This can be achieved with the **route-place system**.

Instead of wiring a hierarchy of several places to multiple presenters, a flat structure is built. Every place-token is bound to a single presenter. JAX-RS like path parameters are supported.

For example:

```
/users/{userId}/photos/{albumId}/{photoId}
```

## Setup

Replace the default GIN binding to `ParameterTokenFormatter` with `RouteTokenFormatter`.

In case you use the ``DefaultModule``:

```
install(new DefaultModule(DefaultPlaceManager.class, RouteTokenFormatter.class));
```

Now all `@NameToken's` are treated as routes. Please note that every route has to start with an `/`. A missing slash will result in an error during code generation.


## Parameters

Routes can contain path and query-string parameters and can be extracted from the given  `PlaceRequest` in `prepareFromRequest`.

### Path parameters

You can treat parts of the place-token as parameters which is useful to embed parameter information to requests. The value within `{` and `}` represents a parameter-name and can be access with `PlaceRequest.getParameter()`. For example: "`/users/{userId}`"

### Query parameters

Every parameter which is not part of the route will be treated as query parameter and will be attached to the browsers history-token.

## Revealing

Revealing works the same way as with hierarchical-places. By creating a `PlaceRequest` with the target place-token and it's parameters. For example:


```
placeManager.revealPlace(
    new PlaceRequest.Builder()
        .nameToken("/users/{userId}/photos/{albumId}/{photoId}")
        .with("userId", targetUserId)
        .with("albumId", targetAlbumId)
        .with("photoId", targetPhotoId)
        .build());
```

## Many to One Mapping
`GWT-P 1.3+` supports mapping multiple tokens to the same presenter. This can be useful when several very similar states are grouped to a single presenter.

For example, you might want to have a CRUD editor form that can display both a blank form (creating a new item) and allow the editing of an existing item. Previously, you would need to map `/item/edit` to the blank form presenter and `/item/edit/55` to a presenter that fetches item 55 and displays it in the form. Or, you could could have made and ugly hack like `/item/edit/0` to tell your presenter to display a new blank item form for the invalid id number of `0`.

Now you would simple write

```
@NameToken({"/item/edit/{id}","/item/edit"})
public interface MyProxy extends ProxyPlace<ItemEditorPresenter> {
  ...
}

@Override
public void prepareFromRequest(final PlaceRequest placeRequest) {
    super.prepareFromRequest(placeRequest);

    String itemId = request.getParameter("id", "");

    if(itemId.isEmpty()) {
        // Display blank form for item editing
        ...
    }
    else {
        // Fetch item DTO and populate form
        ...
    }
}
```

## Resulting URL
Note that, due to the fact that GWT is a webapp the final URL will contain a # before the place token. Therefore, the place token `/users` will become `#/users`
or, a full example URL: `http://www.example.com/#/users`
