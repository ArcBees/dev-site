package com.google.gwt.site.markdown.pegdown;

import java.util.ArrayList;
import java.util.List;

import org.pegdown.Extensions;
import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

public class MarkdownToHtmlUtil {
    private final PegDownProcessor pegDownProcessor;
    private final List<ToHtmlSerializerPlugin> plugins;

    public MarkdownToHtmlUtil() {
        this(new PegDownProcessor(Extensions.NONE, Long.MAX_VALUE, getPlugins()));
    }

    private static PegDownPlugins getPlugins() {
        return PegDownPlugins.builder()
                .withPlugin(DivWithIdParserPlugin.class)
                .build();
    }

    public MarkdownToHtmlUtil(PegDownProcessor pegDownProcessor) {
        this.pegDownProcessor = pegDownProcessor;
        plugins = new ArrayList<>();

        addPlugins();
    }

    public String toHtml(String markdown) {
        RootNode rootNode = pegDownProcessor.parseMarkdown(markdown.toCharArray());

        return new ToHtmlSerializer(new LinkRenderer(), plugins).toHtml(rootNode);
    }

    private void addPlugins() {
        plugins.add(new DivWithIdHtmlSerializerPlugin());
    }
}
