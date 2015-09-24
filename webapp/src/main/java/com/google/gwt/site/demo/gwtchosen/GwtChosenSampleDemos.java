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

package com.google.gwt.site.demo.gwtchosen;

import com.google.gwt.site.demo.AbstractDemos;
import com.google.gwt.site.demo.gwtchosen.sample.AdvancedCLBOptionsWidget;
import com.google.gwt.site.demo.gwtchosen.sample.ChosenOptionsWidget;
import com.google.gwt.site.demo.gwtchosen.sample.CustomFilteringWidget;
import com.google.gwt.site.demo.gwtchosen.sample.OptgroupSupportWidget;
import com.google.gwt.site.demo.gwtchosen.sample.SelectedDisabledSupportWidget;
import com.google.gwt.site.demo.gwtchosen.sample.UpdatingDynamicallyWidget;
import com.google.gwt.site.demo.gwtchosen.sample.WhatIsChosenWidget;
import com.google.gwt.site.demo.gwtchosen.widget.EventsLogWidget;
import com.google.web.bindery.event.shared.EventBus;

public class GwtChosenSampleDemos extends AbstractDemos {
    public GwtChosenSampleDemos(EventBus eventBus) {
        super(eventBus, "gwtchosen-sample-");

        registerDemo(1, new WhatIsChosenWidget());
        registerDemo(2, new OptgroupSupportWidget());
        registerDemo(3, new SelectedDisabledSupportWidget());
        registerDemo(4, new AdvancedCLBOptionsWidget());
        registerDemo(5, new ChosenOptionsWidget());
        registerDemo(6, new com.google.gwt.site.demo.gwtchosen.sample.EventsWidget());
        registerDemo(7, new UpdatingDynamicallyWidget());
        registerDemo(8, new CustomFilteringWidget());
        registerDemo(9, new EventsLogWidget());
    }
}
