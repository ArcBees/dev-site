package com.google.gwt.site.demo.gsss.grid;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.site.demo.AbstractDemo;
import com.google.web.bindery.event.shared.EventBus;

public class GSSSGridDemo extends AbstractDemo {
    public GSSSGridDemo(EventBus eventBus) {
        super(eventBus, "gsss-grid-");
    }

    @Override
    protected Element getDemoToLoad(String id) {
        return Document.get().createButtonInputElement();
    }
}
