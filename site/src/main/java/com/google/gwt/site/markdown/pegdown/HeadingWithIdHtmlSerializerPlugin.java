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

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class HeadingWithIdHtmlSerializerPlugin implements ToHtmlSerializerPlugin {
    @Override
    public boolean visit(Node node, Visitor visitor, Printer printer) {
        boolean canVisit = node instanceof HeadingWithIdNode;

        if (canVisit) {
            HeadingWithIdNode idNode = (HeadingWithIdNode) node;

            String id = idNode.getId();

            String tag = "h" + idNode.getLevel();
            printer.print('<').print(tag).print(' ').print("id=\"" + id + "\"").print('>');
            for (Node child : node.getChildren()) {
                child.accept(visitor);
            }
            printer.print('<').print('/').print(tag).print('>');
        }

        return canVisit;
    }
}
