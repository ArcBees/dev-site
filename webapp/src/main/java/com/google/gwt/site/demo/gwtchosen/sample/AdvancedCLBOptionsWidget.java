package com.google.gwt.site.demo.gwtchosen.sample;

import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class AdvancedCLBOptionsWidget implements IsWidget {
    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    SimplePanel hierChosenSingle;

    private final Widget widget;

    @UiTemplate("AdvancedCLBOptionsWidget.ui.xml")
    interface Binder extends UiBinder<Widget, AdvancedCLBOptionsWidget> {
    }

    public AdvancedCLBOptionsWidget() {
        widget = uiBinder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    final ChosenListBox hcs = new ChosenListBox();
                    hcs.setPlaceholderText("Navigate to...");
                    hcs.setTabIndex(9);
                    hcs.addItem("");
                    hcs.addStyledItem("Home", "home", null);
                    hcs.addGroup("ABOUT US");
                    hcs.addStyledItemToGroup("Press Releases", "press", null, 0);
                    hcs.addStyledItemToGroup("Contact Us", "about", null, 0);
                    hcs.addGroup("PRODUCTS");
                    hcs.addStyledItemToGroup("Tera-Magic", "tm", null, 0, 1);
                    hcs.addStyledItemToGroup("Tera-Magic Pro", "tmpro", null, 1, 1);
                    // Will be inserted before "Tera-Magic Pro" and custom-styled
                    hcs.insertStyledItemToGroup("Tera-Magic Standard", "tmstd", "youAreHere", 1, 1, 1);
                    hierChosenSingle.setWidget(hcs);
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
