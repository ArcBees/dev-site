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

package com.google.gwt.site.markdown.pegdown;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.pegdown.LinkRenderer;
import org.pegdown.PegDownProcessor;
import org.pegdown.ToHtmlSerializer;
import org.pegdown.ast.RootNode;
import org.pegdown.plugins.PegDownPlugins;
import org.pegdown.plugins.ToHtmlSerializerPlugin;

import com.google.gwt.site.markdown.pegdown.divwithid.DivWithIdHtmlSerializerPlugin;
import com.google.gwt.site.markdown.pegdown.divwithid.DivWithIdParserPlugin;
import com.google.gwt.site.markdown.pegdown.headingwithid.HeadingWithIdHtmlSerializerPlugin;
import com.google.gwt.site.markdown.pegdown.headingwithid.HeadingWithIdParserPlugin;
import com.google.gwt.site.markdown.pegdown.variabletemplate.VariableParserPlugin;
import com.google.gwt.site.markdown.pegdown.variabletemplate.VariableSerializerPlugin;

import static org.pegdown.Extensions.FENCED_CODE_BLOCKS;
import static org.pegdown.Extensions.STRIKETHROUGH;
import static org.pegdown.Extensions.TABLES;

public class MarkdownParser {
    private static final int OPTIONS = FENCED_CODE_BLOCKS + STRIKETHROUGH + TABLES;
    private static final long PARSING_TIME_IN_MILLIS = Long.MAX_VALUE;

    private final PegDownProcessor pegDownProcessor;
    private final List<ToHtmlSerializerPlugin> serializerPlugins;

    public MarkdownParser(Properties variables) {
        PegDownPlugins parserPlugins = createParserPlugins(variables);

        this.pegDownProcessor = new PegDownProcessor(OPTIONS, PARSING_TIME_IN_MILLIS, parserPlugins);
        this.serializerPlugins = createSerializerPlugins(variables);
    }

    public String toHtml(String markdown) {
        RootNode rootNode = pegDownProcessor.parseMarkdown(markdown.toCharArray());

        return new ToHtmlSerializer(new LinkRenderer(), serializerPlugins).toHtml(rootNode);
    }

    private PegDownPlugins createParserPlugins(Properties variables) {
        return PegDownPlugins.builder()
                .withPlugin(DivWithIdParserPlugin.class)
                .withPlugin(HeadingWithIdParserPlugin.class)
                .withPlugin(VariableParserPlugin.class, variables)
                .build();
    }

    private List<ToHtmlSerializerPlugin> createSerializerPlugins(Properties variables) {
        return Collections.unmodifiableList(Arrays.asList(
                new DivWithIdHtmlSerializerPlugin(),
                new HeadingWithIdHtmlSerializerPlugin(),
                new VariableSerializerPlugin(variables)
        ));
    }
}
