package com.google.gwt.site.demo;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.query.client.GQuery;
import com.google.web.bindery.event.shared.EventBus;

import static com.google.gwt.query.client.GQuery.$;

public abstract class AbstractDemo implements ContentLoadedEvent.ContentLoadedHandler {
    private final String prefix;

    protected AbstractDemo(EventBus eventBus, String prefix) {
        this.prefix = prefix;

        eventBus.addHandler(ContentLoadedEvent.getType(), this);
    }

    @Override
    public void onContentLoaded() {
        GQuery demos = $("//div[@id='gwt-content']//div[starts-with(@id, '" + prefix + "')]");

        NodeList<Element> nodeList = demos.get();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element item = nodeList.getItem(i);

            Element demo = getDemoToLoad(item.getId());
            if (demo != null) {
                item.appendChild(demo);
            }
        }
    }

    protected abstract Element getDemoToLoad(String id);
}
