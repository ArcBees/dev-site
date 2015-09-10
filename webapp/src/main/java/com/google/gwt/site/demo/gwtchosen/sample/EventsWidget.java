package com.google.gwt.site.demo.gwtchosen.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class EventsWidget implements IsWidget {

    private static Binder uiBinder = GWT.create(Binder.class);

    private final Widget widget;

    @UiTemplate("EventsWidget.ui.xml")
    interface Binder extends UiBinder<Widget, EventsWidget> {
    }

    public EventsWidget() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
