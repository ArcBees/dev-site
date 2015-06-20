/**
 * Copyright 2015 ArcBees Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.google.gwt.site.markdown.pegdown;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

import com.google.gwt.site.markdown.pegdown.jygments.JygmentsCodeHighlighter;

import static org.pegdown.Extensions.FENCED_CODE_BLOCKS;
import static org.pegdown.Extensions.STRIKETHROUGH;
import static org.pegdown.Extensions.TABLES;

public class MarkdownToHtmlUtil {
    private final PegDownProcessor pegDownProcessor;
    private final CodeHighlighter highlighter;
    private final Map<String, VerbatimSerializer> verbatimSerializers;
    private final List<ToHtmlSerializerPlugin> plugins;

    public MarkdownToHtmlUtil() {
        this(new PegDownProcessor(FENCED_CODE_BLOCKS + STRIKETHROUGH + TABLES, Long.MAX_VALUE, getPlugins()));
    }

    private static PegDownPlugins getPlugins() {
        return PegDownPlugins.builder()
                .withPlugin(DivWithIdParserPlugin.class)
                .withPlugin(HeadingWithIdParserPlugin.class)
                .build();
    }

    public MarkdownToHtmlUtil(PegDownProcessor pegDownProcessor) {
        this.pegDownProcessor = pegDownProcessor;
        this.highlighter = new JygmentsCodeHighlighter();
        this.verbatimSerializers = new HashMap<>();
        this.plugins = new ArrayList<>();

        addVerbatimSerializers();
        addPlugins();
    }

    public ParsedMarkdown convert(String markdown) {
        RootNode rootNode = pegDownProcessor.parseMarkdown(markdown.toCharArray());
        LinkRenderer linkRenderer = new LinkRenderer();

        String html = new ToHtmlSerializer(linkRenderer, verbatimSerializers, plugins).toHtml(rootNode);
        String style = highlighter.getStyle();

        return new ParsedMarkdown(html, style);
    }

    private void addVerbatimSerializers() {
        DefaultVerbatimSerializer serializer = new DefaultVerbatimSerializer(highlighter);

        verbatimSerializers.put(VerbatimSerializer.DEFAULT, serializer);
    }

    private void addPlugins() {
        plugins.add(new DivWithIdHtmlSerializerPlugin());
        plugins.add(new HeadingWithIdHtmlSerializerPlugin());
    }
}
