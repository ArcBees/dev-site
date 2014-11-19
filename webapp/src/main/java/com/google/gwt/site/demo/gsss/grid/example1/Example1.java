package com.google.gwt.site.demo.gsss.grid.example1;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import javax.inject.Inject;

public class Example1 implements IsWidget {
    interface Binder extends UiBinder<HTMLPanel, Example1> {
    }

    private static Binder binder = GWT.create(Binder.class);

    private final Widget widget;

    Example1() {
        widget = binder.createAndBindUi(this);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
