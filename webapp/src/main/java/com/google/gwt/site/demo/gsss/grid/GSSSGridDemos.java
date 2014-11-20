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
