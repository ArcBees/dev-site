/**
 * Copyright (c) 2014 by ArcBees Inc., All rights reserved.
 * This source code, and resulting software, is the confidential and proprietary information
 * ("Proprietary Information") and is the intellectual property ("Intellectual Property")
 * of ArcBees Inc. ("The Company"). You shall not disclose such Proprietary Information and
 * shall use it only in accordance with the terms and conditions of any and all license
 * agreements you have entered into with The Company.
 */

package com.google.gwt.site.markdown.velocity;

import javax.inject.Provider;

import org.apache.velocity.app.VelocityEngine;

public class VelocityWrapperFactory {
    private final VelocityEngine velocityEngine;

    public VelocityWrapperFactory(Provider<VelocityEngine> velocityEngineProvider) {
        velocityEngine = velocityEngineProvider.get();
    }

    public VelocityWrapper create(String templateLocation) {
        return new VelocityWrapper(velocityEngine, templateLocation);
    }
}
