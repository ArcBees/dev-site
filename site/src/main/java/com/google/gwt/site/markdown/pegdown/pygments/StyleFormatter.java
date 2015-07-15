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

class StyleFormatter {
    private final PythonInterpreter interpreter;
    private final String styleName;

    StyleFormatter(String styleName) {
        this.styleName = styleName;
        this.interpreter = new PythonInterpreter();
    }

    public String format() throws PyException {
        interpreter.set("style_name", styleName);
        interpreter.exec("from pygments.styles import get_style_by_name\n"
                + "from pygments.formatters import HtmlFormatter\n\n"
                + "result = HtmlFormatter(style=style_name).get_style_defs()");

        return interpreter.get("result", String.class);
    }
}
