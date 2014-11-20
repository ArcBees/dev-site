package com.google.gwt.site.demo.gsss.mixins;

import com.google.gwt.core.client.GWT;
import com.google.gwt.site.demo.AbstractDemos;
import com.google.gwt.site.demo.gsss.mixins.examples.Examples;
import com.google.gwt.site.demo.gsss.mixins.resources.MixinsResources;
import com.google.web.bindery.event.shared.EventBus;

public class GSSSMixinsDemos extends AbstractDemos {
    private static final MixinsResources MIXINS = GWT.create(MixinsResources.class);

    public GSSSMixinsDemos(EventBus eventBus) {
        super(eventBus, "gsss-mixins-");

        MIXINS.style().ensureInjected();

        registerDemo(1, Examples.create(MIXINS.style().example1()));
        registerDemo(2, Examples.create(MIXINS.style().example2()));
        registerDemo(3, Examples.create(MIXINS.style().example3()));
        registerDemo(4, Examples.create(MIXINS.style().example4()));
        registerDemo(5, Examples.create(MIXINS.style().example5()));
        registerDemo(6, Examples.create(MIXINS.style().example6()));
        registerDemo(7, Examples.create(MIXINS.style().example7()));
        registerDemo(8, Examples.create(MIXINS.style().example8()));
        registerDemo(9, Examples.create(MIXINS.style().example9()));
        registerDemo(10, Examples.create(MIXINS.style().example10()));
        registerDemo(11, Examples.create(MIXINS.style().example11()));
        registerDemo(12, Examples.create(MIXINS.style().example12()));
        registerDemo(13, Examples.create(MIXINS.style().example13()));
        registerDemo(14, Examples.create(MIXINS.style().example14()));
        registerDemo(15, Examples.create(MIXINS.style().example15()));
        registerDemo(16, Examples.create(MIXINS.style().example16()));
        registerDemo(17, Examples.create(MIXINS.style().example17()));
        registerDemo(18, Examples.create(MIXINS.style().example18()));
        registerDemo(19, Examples.create(MIXINS.style().example19()));
        registerDemo(20, Examples.create(MIXINS.style().example20()));
        registerDemo(21, Examples.create(MIXINS.style().example21()));
        registerDemo(22, Examples.create(MIXINS.style().example22()));
        registerDemo(23, Examples.create(MIXINS.style().example23()));
        registerDemo(24, Examples.create(MIXINS.style().example24()));
        registerDemo(25, Examples.create(MIXINS.style().example25()));
        registerDemo(26, Examples.create(MIXINS.style().example26()));
        registerDemo(27, Examples.create(MIXINS.style().example27()));
        registerDemo(28, Examples.create(MIXINS.style().example28()));
        registerDemo(29, Examples.create(MIXINS.style().example29()));
    }
}
