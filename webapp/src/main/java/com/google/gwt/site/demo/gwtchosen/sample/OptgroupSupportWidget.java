/*
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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

public class OptgroupSupportWidget implements IsWidget {
    @UiTemplate("OptgroupSupportWidget.ui.xml")
    interface Binder extends UiBinder<Widget, OptgroupSupportWidget> {
    }

    private static Binder uiBinder = GWT.create(Binder.class);

    @UiField
    SelectElement chosenMultiple;
    @UiField
    SelectElement chosenSingle;

    private final Widget widget;

    public OptgroupSupportWidget() {
        widget = uiBinder.createAndBindUi(this);

        widget.addAttachHandler(new AttachEvent.Handler() {
            @Override
            public void onAttachOrDetach(AttachEvent attachEvent) {
                if (attachEvent.isAttached()) {
                    $(chosenMultiple).as(Chosen).chosen();
                    $(chosenSingle).as(Chosen).chosen();
                }
            }
        });
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
