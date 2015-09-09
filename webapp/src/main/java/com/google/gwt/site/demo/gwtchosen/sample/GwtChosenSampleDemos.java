/*
 * Copyright 2014 ArcBees Inc.
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

import com.google.gwt.site.demo.AbstractDemos;
import com.google.web.bindery.event.shared.EventBus;

public class GwtChosenSampleDemos extends AbstractDemos {
    public GwtChosenSampleDemos(EventBus eventBus) {
        super(eventBus, "gwtchosen-sample-");

        registerDemo(1, new WhatIsChosen());
        registerDemo(2, new OptgroupSupport());
        registerDemo(3, new SelectedDisabledSupport());
        registerDemo(4, new AdvancedCLBOptions());
        registerDemo(5, new ChosenOptionsView());
        registerDemo(6, new Events());
        registerDemo(7, new UpdatingDynamically());
        registerDemo(8, new CustomFiltering());
    }
}
