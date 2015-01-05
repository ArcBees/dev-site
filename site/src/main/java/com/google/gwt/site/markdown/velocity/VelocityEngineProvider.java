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

package com.google.gwt.site.markdown.velocity;

import javax.inject.Provider;

import org.apache.velocity.app.VelocityEngine;

public class VelocityEngineProvider implements Provider<VelocityEngine> {
    @Override
    public VelocityEngine get() {
        VelocityEngine velocityEngine = new VelocityEngine();

        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("input.encoding", "UTF-8");
        velocityEngine.setProperty("class.resource.loader.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

        velocityEngine.init();

        return velocityEngine;
    }
}
