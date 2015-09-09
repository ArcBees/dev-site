package com.google.gwt.site.demo.gwtchosen.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class Events implements IsWidget {

    private static Binder uiBinder = GWT.create(Binder.class);

    private final Widget widget;

    @UiTemplate("Events.ui.xml")
    interface Binder extends UiBinder<Widget, Events> {
    }

    public Events() {
        widget = uiBinder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
