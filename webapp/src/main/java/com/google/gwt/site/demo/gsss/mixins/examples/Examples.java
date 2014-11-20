package com.google.gwt.site.demo.gsss.mixins.examples;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class Examples implements IsWidget {
    interface Binder extends UiBinder<Widget, Examples> {
    }

    private static Binder BINDER = GWT.create(Binder.class);

    private final Widget widget;

    @UiField
    DivElement className;

    protected Examples(String className) {
        widget = BINDER.createAndBindUi(this);

        this.className.addClassName(className);
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    public static IsWidget create(String className) {
        return new Examples(className);
    }
}
