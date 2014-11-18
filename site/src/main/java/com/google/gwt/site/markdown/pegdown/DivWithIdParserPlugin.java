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
