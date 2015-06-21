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

package com.google.gwt.site.markdown.pegdown.pygments;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.pegdown.Printer;
import org.python.core.PyException;

import com.google.gwt.site.markdown.pegdown.CodeHighlighter;

public class PygmentsCodeHighlighter implements CodeHighlighter {
    private static final Logger LOGGER = Logger.getLogger(PygmentsCodeHighlighter.class.getName());
    private static final String STYLE_NAME = "default";

    private final CodeFormatter codeFormatter;
    private final StyleFormatter styleFormatter;
    private String style;

    public PygmentsCodeHighlighter() {
        codeFormatter = new CodeFormatter();
        styleFormatter = new StyleFormatter(STYLE_NAME);
    }

    @Override
    public boolean highlight(String language, String code, Printer printer) {
        try {
            String formattedCode = codeFormatter.format(language, code);
            printer.print(formattedCode);

            return true;
        } catch (PyException e) {
            LOGGER.log(Level.WARNING, "Could not highlight language '" + language + "'.");
            return false;
        }
    }

    @Override
    public String getStyle() {
        if (style == null) {
            try {
                style = "<style>\n" + styleFormatter.format() + "\n</style>";
            } catch (PyException e) {
                LOGGER.log(Level.WARNING, "Could not format '" + STYLE_NAME + "' style.", e);
                style = "";
            }
        }

        return style;
    }
}
