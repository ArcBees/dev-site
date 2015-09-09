package com.google.gwt.site.demo.gwtchosen.sample;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.SelectElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

import static com.arcbees.chosen.client.Chosen.Chosen;
import static com.google.gwt.query.client.GQuery.$;

public class OptgroupSupport implements IsWidget {

    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    SelectElement chosenMultiple;

    private final Widget widget;

    @UiTemplate("OptgroupSupport.ui.xml")
    interface Binder extends UiBinder<Widget, OptgroupSupport> {
    }

    public OptgroupSupport() {
        widget = uiBinder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    $(chosenMultiple).as(Chosen).chosen();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
