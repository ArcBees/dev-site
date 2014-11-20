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

package com.google.gwt.site.demo.gsss.grid.resources;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.GssResource;

public interface GridResources extends ClientBundle {
    interface Grid extends com.arcbees.gsss.grid.client.GridResources.Grid {
    }

    interface GridSettings extends com.arcbees.gsss.grid.client.GridResources.Grid {
    }

    interface Default extends GssResource {
        String colored();
    }

    @Source({"com/arcbees/gsss/grid/client/gridsettings.gss", "com/arcbees/gsss/grid/client/grid.gss"})
    Grid grid();

    @Source({"com/google/gwt/site/demo/gsss/grid/gridSettings.gss", "com/arcbees/gsss/grid/client/grid.gss"})
    GridSettings gridSettings();

    @Source("com/google/gwt/site/demo/gsss/grid/example.gss")
    Default style();
}
