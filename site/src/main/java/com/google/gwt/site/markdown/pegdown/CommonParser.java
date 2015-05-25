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

import org.parboiled.BaseParser;
import org.parboiled.Rule;

public abstract class CommonParser<V> extends BaseParser<V> {
    /**
     * From HTML spec:
     * <p/>
     * ID and NAME tokens must begin with a letter ([A-Za-z]) and may be followed by any number of letters, digits
     * ([0-9]), hyphens ("-"), underscores ("_"), colons (":"), and periods (".").
     * <p/>
     * See http://www.w3.org/TR/html4/types.html
     */
    protected Rule id() {
        return Sequence(
                FirstOf(upperCaseLetter(), lowerCaseLetter()),
                Optional(OneOrMore(FirstOf(letters(), number(), AnyOf("-_:.")))));
    }

    protected Rule letters() {
        return OneOrMore(FirstOf(upperCaseLetter(), lowerCaseLetter()));
    }

    protected Rule upperCaseLetter() {
        return CharRange('A', 'Z');
    }

    protected Rule lowerCaseLetter() {
        return CharRange('a', 'z');
    }

    protected Rule number() {
        return CharRange('0', '9');
    }

    protected Rule optionalWhiteSpaces() {
        return ZeroOrMore(whiteSpace());
    }

    protected Rule whiteSpace() {
        return AnyOf(" \t");
    }

    protected Rule normalCharacter() {
        return Sequence(TestNot(specialCharacter()), TestNot(whiteSpace()), notNewline(), ANY);
    }

    protected Rule specialCharacter() {
        return AnyOf("*_`&[]<>!#\\");
    }

    protected Rule notNewline() {
        return TestNot(AnyOf("\n\r"));
    }
}
