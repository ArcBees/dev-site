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
import java.io.Writer;

import com.google.gwt.site.markdown.pegdown.CodeHighlighter;
import com.threecrickets.jygments.Util;
import com.threecrickets.jygments.format.Formatter;
import com.threecrickets.jygments.grammar.Token;

/**
 * Formats Jygments tokens with appropriate CSS classes. The result will be enclosed inside {@code
 * <pre><code></code></pre>}. Code is adapted from {@link com.threecrickets.jygments.contrib.HtmlFormatter
 * HtmlFormatter}.
 */
class CodeFormatter extends Formatter {
    public CodeFormatter() {
        super(null, false, null, null);
    }

    @Override
    public void format(Iterable<Token> tokens, Writer writer) throws IOException {
        writer.write("<pre><code>");

        StringBuilder line = new StringBuilder();
        for (Token token : tokens) {
            line = writePartialTokens(writer, line, token);
        }

        if (line.length() > 0) {
            writeLine(line, writer);
        }

        writer.append("</code></pre>\n").flush();
    }

    private StringBuilder writePartialTokens(Writer writer, StringBuilder line, Token token) throws IOException {
        String[] partialTokens = token.getValue().split("\n", -1);
        for (int i = 0; i < partialTokens.length - 1; i++) {
            writePartialToken(line, token, partialTokens[i]);
            writeLine(line, writer);

            line = new StringBuilder();
        }

        writePartialToken(line, token, partialTokens[partialTokens.length - 1]);

        return line;
    }

    private void writePartialToken(StringBuilder line, Token token, String partialToken) {
        String escapeHtml = Util.escapeHtml(partialToken);

        if (token.getType().getShortName().length() > 0) {
            line.append("<span class=\"")
                    .append(CodeHighlighter.DECLARATION_PREFIX)
                    .append(token.getType().getShortName())
                    .append("\">")
                    .append(escapeHtml)
                    .append("</span>");
        } else {
            line.append(escapeHtml);
        }
    }

    public void writeLine(StringBuilder line, Writer writer) throws IOException {
        writer.append(line).append("\n");
    }
}
