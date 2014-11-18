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

import org.parboiled.Action;
import org.parboiled.BaseParser;
import org.parboiled.Context;
import org.parboiled.Rule;
import org.pegdown.plugins.BlockPluginParser;

public class DivWithIdParserPlugin extends BaseParser<Object> implements BlockPluginParser {
    private final Action createDivAction = new Action() {
        @Override
        public boolean run(Context context) {
            return push(new DivWithIdNode(match()));
        }
    };

    @Override
    public Rule[] blockPluginRules() {
        return toRules(
                Sequence("$[",
                        Sequence(
                                Letters(),
                                OneOrMore(FirstOf(Letters(), Number(), AnyOf("-_:.")))),
                        createDivAction,
                        "]"));
    }

    Rule Letters() {
        return OneOrMore(FirstOf(UpperCaseCharacter(), LowerCaseCharacter()));
    }

    Rule UpperCaseCharacter() {
        return CharRange('A', 'Z');
    }

    Rule LowerCaseCharacter() {
        return CharRange('a', 'z');
    }

    Rule Number() {
        return CharRange('0', '9');
    }
}
