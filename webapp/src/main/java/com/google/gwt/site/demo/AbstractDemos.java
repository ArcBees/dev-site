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

package com.google.gwt.site.demo;

import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;

import static com.google.gwt.query.client.GQuery.$;

public abstract class AbstractDemos implements ContentLoadedEvent.ContentLoadedHandler {
    private final String prefix;
    private final Map<String, Widget> demos;

    protected AbstractDemos(EventBus eventBus, String prefix) {
        this.prefix = prefix;
        demos = Maps.newHashMap();

        eventBus.addHandler(ContentLoadedEvent.getType(), this);
    }

    @Override
    public void onContentLoaded() {
        GQuery demos = $("//div[@id='content']//div[starts-with(@id, '" + prefix + "')]");

        NodeList<Element> nodeList = demos.get();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = nodeList.getItem(i);

            Widget demo = getDemoToLoad(item.getId().replace(prefix, ""));
            if (demo != null) {
                HTMLPanel panel = HTMLPanel.wrap(item);
                panel.add(demo);
            }
        }
    }

    protected void registerDemo(Integer id, IsWidget demo) {
        Preconditions.checkNotNull(id);

        demos.put(id.toString(), demo.asWidget());
    }

    protected Widget getDemoToLoad(String exampleNumber) {
        Widget demo = demos.get(exampleNumber);

        Preconditions.checkNotNull(demo, "No demo registered for " + prefix + exampleNumber);

        return demo;
    }
}
