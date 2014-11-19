package com.google.gwt.site.demo.gsss.grid.example2;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.site.demo.gsss.grid.resources.GridResources;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class Example2 implements IsWidget {
    interface Binder extends UiBinder<HTMLPanel, Example2> {
    }

    private static Binder binder = GWT.create(Binder.class);

    private final Widget widget;
    @UiField
    GridResources resources;

    public Example2() {
        widget = binder.createAndBindUi(this);
        resources.grid().ensureInjected();
        resources.gridSettings().ensureInjected();
        resources.exemple().ensureInjected();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
