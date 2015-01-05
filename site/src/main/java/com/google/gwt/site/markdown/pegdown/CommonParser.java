/**
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

import org.parboiled.BaseParser;
import org.parboiled.Rule;

public abstract class CommonParser<V> extends BaseParser<V> {
    /**
     * From HTML spec:
     *
     * ID and NAME tokens must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits
     * ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     *
     * See http://www.w3.org/TR/html4/types.html
     */
    Rule Id() {
        return Sequence(
                FirstOf(UpperCaseCharacter(), LowerCaseCharacter()),
                Optional(OneOrMore(FirstOf(Letters(), Number(), AnyOf("-_:.")))));
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

    Rule Sp() {
        return ZeroOrMore(Spacechar());
    }

    Rule Spacechar() {
        return AnyOf(" \t");
    }

    Rule NormalChar() {
        return Sequence(TestNot(SpecialChar()), TestNot(Spacechar()), NotNewline(), ANY);
    }

    Rule SpecialChar() {
        return AnyOf("*_`&[]<>!#\\");
    }

    Rule NotNewline() {
        return TestNot(AnyOf("\n\r"));
    }
}
