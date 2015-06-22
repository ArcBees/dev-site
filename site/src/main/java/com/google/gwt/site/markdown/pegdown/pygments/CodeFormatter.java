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

import org.python.core.PyException;
import org.python.util.PythonInterpreter;

class CodeFormatter {
    private final PythonInterpreter interpreter;

    CodeFormatter() {
        this.interpreter = new PythonInterpreter();
    }

    public String format(String language, String code) throws PyException {
        interpreter.set("language", language);
        interpreter.set("code", code);
        interpreter.exec("from pygments import highlight\n"
                + "from pygments.lexers import get_lexer_by_name\n"
                + "from pygments.formatters import HtmlFormatter\n\n"
                + "lexer = get_lexer_by_name(language)\n"
                + "formatter = HtmlFormatter(nowrap = True)\n"
                + "result = highlight(code, lexer, formatter)");

        String result = interpreter.get("result", String.class);
        result = "<pre><code>" + result + "</code></pre>";

        return result;
    }
}
