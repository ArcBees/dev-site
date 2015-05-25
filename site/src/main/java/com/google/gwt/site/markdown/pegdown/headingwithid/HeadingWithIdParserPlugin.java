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

import org.parboiled.Action;
import org.parboiled.Context;
import org.parboiled.Rule;
import org.pegdown.ast.HeaderNode;
import org.pegdown.ast.TextNode;
import org.pegdown.plugins.InlinePluginParser;

import com.google.gwt.site.markdown.pegdown.CommonParser;

public class HeadingWithIdParserPlugin extends CommonParser<Object> implements InlinePluginParser {
    private final Action<Object> createHeadingWithIdAction = new Action<Object>() {
        @Override
        public boolean run(Context<Object> context) {
            Object peek = peek(1);

            if (peek instanceof HeaderNode) {
                HeaderNode headerNode = (HeaderNode) peek;

                int level = headerNode.getLevel();
                String id = match();

                HeadingWithIdNode headingWithIdNode = new HeadingWithIdNode(level, id, headerNode.getChildren());

                return poke(1, headingWithIdNode);
            }

            return false;
        }
    };

    protected final Action<Object> createTextNodeAction = new Action<Object>() {
        @Override
        public boolean run(Context<Object> context) {
            String text = match();
            return push(new TextNode(text));
        }
    };

    @Override
    public Rule[] inlinePluginRules() {
        return toRules(
                Sequence(
                        OneOrMore(normalCharacter()),
                        createTextNodeAction,
                        Optional(optionalWhiteSpaces(), ZeroOrMore('#'), optionalWhiteSpaces()),
                        '{',
                        id(),
                        createHeadingWithIdAction,
                        '}'
                )
        );
    }
}
