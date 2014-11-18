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
