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
