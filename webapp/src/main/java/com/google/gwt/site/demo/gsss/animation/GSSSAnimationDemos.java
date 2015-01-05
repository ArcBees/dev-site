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

package com.google.gwt.site.demo.gsss.animation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.site.demo.AbstractDemos;
import com.google.gwt.site.demo.gsss.animation.examples.Examples;
import com.google.gwt.site.demo.gsss.animation.resources.AnimationResources;
import com.google.web.bindery.event.shared.EventBus;

public class GSSSAnimationDemos extends AbstractDemos {
    private static final AnimationResources MIXINS = GWT.create(AnimationResources.class);

    public GSSSAnimationDemos(EventBus eventBus) {
        super(eventBus, "gsss-animation-");

        MIXINS.style().ensureInjected();

        registerDemo(1, Examples.create(MIXINS.style().example1()));
        registerDemo(2, Examples.create(MIXINS.style().example2()));
    }
}
