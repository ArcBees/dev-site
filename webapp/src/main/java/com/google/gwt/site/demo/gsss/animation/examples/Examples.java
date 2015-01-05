/**
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

package com.google.gwt.site.demo.gsss.animation.examples;

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
