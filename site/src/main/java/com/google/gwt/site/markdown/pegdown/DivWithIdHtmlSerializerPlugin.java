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
