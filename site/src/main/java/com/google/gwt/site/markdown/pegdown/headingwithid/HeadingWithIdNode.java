/*
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

package com.google.gwt.site.markdown.pegdown.headingwithid;

import java.util.List;

import org.pegdown.ast.Node;
import org.pegdown.ast.SuperNode;
import org.pegdown.ast.Visitor;

class HeadingWithIdNode extends SuperNode {
    private final int level;
    private final String id;

    public HeadingWithIdNode(int level, String id, List<Node> children) {
        super(children);
        this.level = level;
        this.id = id;
    }

    public int getLevel() {
        return level;
    }

    public String getId() {
        return id;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit((Node) this);
    }
}
