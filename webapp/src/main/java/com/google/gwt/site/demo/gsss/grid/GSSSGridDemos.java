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

package com.google.gwt.site.demo.gsss.grid;

import com.google.gwt.site.demo.AbstractDemos;
import com.google.gwt.site.demo.gsss.grid.examples.Examples;
import com.google.web.bindery.event.shared.EventBus;

public class GSSSGridDemos extends AbstractDemos {
    public GSSSGridDemos(EventBus eventBus) {
        super(eventBus, "gsss-grid-");

        registerDemo(1, Examples.createExample1());
        registerDemo(2, Examples.createExample2());
    }
}
