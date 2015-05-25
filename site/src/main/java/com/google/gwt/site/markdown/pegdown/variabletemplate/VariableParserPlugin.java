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

package com.google.gwt.site.markdown.pegdown.variabletemplate;

import java.util.Properties;

import org.parboiled.Rule;
import org.pegdown.plugins.InlinePluginParser;

import com.google.gwt.site.markdown.pegdown.CommonParser;

public class VariableParserPlugin extends CommonParser<Object> implements InlinePluginParser {
    public static final String VARIABLE_TEMPLATE_START = "{{#";
    public static final String VARIABLE_TEMPLATE_END = "}}";

    private final VariableAction variableAction;

    public VariableParserPlugin(Properties variables) {
        variableAction = new VariableAction(variables);
    }

    @Override
    public Rule[] inlinePluginRules() {
        return toRules(Sequence(
                VARIABLE_TEMPLATE_START,
                id(),
                variableAction,
                VARIABLE_TEMPLATE_END
        ));
    }
}
