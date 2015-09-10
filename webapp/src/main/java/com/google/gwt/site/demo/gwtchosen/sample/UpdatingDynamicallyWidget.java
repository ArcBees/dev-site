package com.google.gwt.site.demo.gwtchosen.sample;

import com.arcbees.chosen.client.gwt.ChosenListBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.query.client.Function;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class UpdatingDynamicallyWidget implements IsWidget {

    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    ButtonElement updateButton;
    @UiField
    SimplePanel updateChosen;

    private final Widget widget;

    @UiTemplate("UpdatingDynamicallyWidget.ui.xml")
    interface Binder extends UiBinder<Widget, UpdatingDynamicallyWidget> {
    }

    public UpdatingDynamicallyWidget() {
        widget = uiBinder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {

                    final ChosenListBox chzn = new ChosenListBox();
                    chzn.addItem("item 1");

                    updateChosen.setWidget(chzn);

                    $(updateButton).click(new Function() {
                        int i = 2;

                        @Override
                        public void f() {
                            for (int j = 0; j < 100; j++) {
                                chzn.addItem("item " + i);
                                i++;
                            }

                            chzn.update();
                        }
                    });
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
