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

package com.google.gwt.site.markdown.pegdown;

import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.Visitor;

public class DivWithIdNode extends SuperNode {
    private final String id;

    public DivWithIdNode(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public void accept(Visitor visitor) {
        // Need to cast to Node to triggers correct ToHtmlSerializerPlugin call
        visitor.visit((Node) this);
    }
}
