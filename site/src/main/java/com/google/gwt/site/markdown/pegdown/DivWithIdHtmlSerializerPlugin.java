/**
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

package com.google.gwt.site.markdown.pegdown;

import org.pegdown.Printer;
import org.pegdown.ast.Node;
import org.pegdown.ast.Visitor;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class DivWithIdHtmlSerializerPlugin implements ToHtmlSerializerPlugin {
    @Override
    public boolean visit(Node node, Visitor visitor, Printer printer) {
        boolean canVisit = node instanceof DivWithIdNode;

        if (canVisit) {
            DivWithIdNode idNode = (DivWithIdNode) node;

            printer.print('<').print("div");
            printAttribute(printer, "id", idNode.getId());
            printer.print('>').print("</div>");
        }

        return canVisit;
    }

    private void printAttribute(Printer printer, String name, String value) {
        printer.print(' ').print(name).print('=').print('"').print(value).print('"');
    }
}
