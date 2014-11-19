package com.google.gwt.site.demo.gsss.grid;

import com.google.gwt.site.demo.AbstractDemos;
import com.google.gwt.site.demo.gsss.grid.example1.Example1;
import com.google.web.bindery.event.shared.EventBus;

public class GSSSGridDemos extends AbstractDemos {
    public GSSSGridDemos(EventBus eventBus) {
        super(eventBus, "gsss-grid-");

        registerDemo(1, new Example1());
    }
}
