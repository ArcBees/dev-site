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

package com.google.gwt.site.markdown.pegdown.jygments;

import java.util.List;
import java.util.Map;

import com.google.gwt.site.markdown.pegdown.CodeHighlighter;
import com.threecrickets.jygments.grammar.TokenType;
import com.threecrickets.jygments.style.ColorStyleElement;
import com.threecrickets.jygments.style.EffectStyleElement;
import com.threecrickets.jygments.style.Style;
import com.threecrickets.jygments.style.StyleElement;

/**
 * Formats a Jygments {@link Style}. The result will be enclosed inside {@code <style></style>}. Code is
 * adapted from {@link com.threecrickets.jygments.contrib.HtmlFormatter HtmlFormatter}.
 */
class StyleFormatter {
    public String formatStyle(Style style) {
        StringBuilder styleBuilder = new StringBuilder("<style>\n");

        for (Map.Entry<TokenType, List<StyleElement>> entry : style.getStyleElements().entrySet()) {
            TokenType tokenType = entry.getKey();

            openDeclaration(styleBuilder, tokenType);
            writeRules(styleBuilder, entry.getValue());
            closeDeclaration(styleBuilder, tokenType);
        }

        return styleBuilder.append("</style>").toString();
    }

    private void openDeclaration(StringBuilder styleBuilder, TokenType tokenType) {
        styleBuilder.append("    .")
                .append(CodeHighlighter.DECLARATION_PREFIX)
                .append(tokenType.getShortName())
                .append(" { ");
    }

    private void writeRules(StringBuilder styleBuilder, List<StyleElement> styleElements) {
        for (StyleElement styleElement : styleElements) {
            if (styleElement instanceof ColorStyleElement) {
                writeColorRules(styleBuilder, (ColorStyleElement) styleElement);
            } else if (styleElement instanceof EffectStyleElement) {
                writeEffectRules(styleBuilder, (EffectStyleElement) styleElement);
            }
        }
    }

    private void writeEffectRules(StringBuilder styleBuilder, EffectStyleElement styleElement) {
        if (styleElement == EffectStyleElement.Bold) {
            styleBuilder.append("font-weight: bold; ");
        } else if (styleElement == EffectStyleElement.Italic) {
            styleBuilder.append("font-style: italic; ");
        } else if (styleElement == EffectStyleElement.Underline) {
            styleBuilder.append("text-decoration: underline; ");
        }
    }

    private void writeColorRules(StringBuilder styleBuilder, ColorStyleElement styleElement) {
        if (styleElement.getType() == ColorStyleElement.Type.Foreground) {
            styleBuilder.append("color: ");
        } else if (styleElement.getType() == ColorStyleElement.Type.Background) {
            styleBuilder.append("background-color: ");
        } else if (styleElement.getType() == ColorStyleElement.Type.Border) {
            styleBuilder.append("border: 1px solid ");
        }

        styleBuilder.append(styleElement.getColor())
                .append("; ");
    }

    private void closeDeclaration(StringBuilder styleBuilder, TokenType tokenType) {
        styleBuilder.append("} /* ")
                .append(tokenType.getName())
                .append(" */\n");
    }
}
