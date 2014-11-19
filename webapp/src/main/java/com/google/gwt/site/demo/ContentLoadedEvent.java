package com.google.gwt.site.demo;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.web.bindery.event.shared.EventBus;

public class ContentLoadedEvent extends GwtEvent<ContentLoadedEvent.ContentLoadedHandler> {
    public interface ContentLoadedHandler extends EventHandler {
        void onContentLoaded();
    }

    public static void fire(EventBus eventBus) {
        eventBus.fireEvent(new ContentLoadedEvent());
    }

    public static Type<ContentLoadedHandler> getType() {
        return TYPE;
    }

    private static Type<ContentLoadedHandler> TYPE = new Type<>();

    private ContentLoadedEvent() {
    }

    @Override
    public Type<ContentLoadedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ContentLoadedHandler handler) {
        handler.onContentLoaded();
    }
}
