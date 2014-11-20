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
        GQuery demos = $("//div[@id='gwt-content']//div[starts-with(@id, '" + prefix + "')]");

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
