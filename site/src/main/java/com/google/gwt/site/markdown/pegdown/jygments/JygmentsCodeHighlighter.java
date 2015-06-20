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

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pegdown.Printer;

import com.google.gwt.site.markdown.pegdown.CodeHighlighter;
import com.threecrickets.jygments.Jygments;
import com.threecrickets.jygments.ResolutionException;
import com.threecrickets.jygments.format.Formatter;
import com.threecrickets.jygments.grammar.Lexer;
import com.threecrickets.jygments.style.Style;

public class JygmentsCodeHighlighter implements CodeHighlighter {
    private static final Logger LOGGER = Logger.getLogger(JygmentsCodeHighlighter.class.getName());

    private final Formatter codeFormatter = new CodeFormatter();
    private final StyleFormatter styleFormatter = new StyleFormatter();

    private String style;

    @Override
    public boolean highlight(String language, String code, Printer printer) {
        try {
            Lexer lexer = findLexer(language);
            PegdownPrinterWriter writer = new PegdownPrinterWriter(printer);

            Jygments.highlight(code, lexer, codeFormatter, writer);

            return true;
        } catch (ResolutionException e) {
            LOGGER.log(Level.INFO, e.getMessage());
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Could not parse " + language, e);
        }

        return false;
    }

    @Override
    public String getStyle() {
        if (style == null) {
            try {
                Style defaultStyle = Style.getByName("default");
                style = styleFormatter.formatStyle(defaultStyle);
            } catch (ResolutionException e) {
                LOGGER.log(Level.WARNING, "Could not find default highlight style.", e);
                style = "";
            }
        }

        return style;
    }

    private Lexer findLexer(String language) throws ResolutionException {
        Lexer lexer = Lexer.getByName(language);
        if (lexer == null) {
            throw new ResolutionException("No lexers for " + language);
        }

        return lexer;
    }
}
