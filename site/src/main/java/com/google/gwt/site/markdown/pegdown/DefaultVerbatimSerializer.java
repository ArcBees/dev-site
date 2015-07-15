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

import org.pegdown.Printer;
import org.pegdown.VerbatimSerializer;
import org.pegdown.ast.VerbatimNode;

public class DefaultVerbatimSerializer implements VerbatimSerializer {
    private static final VerbatimSerializer FALLBACK_SERIALIZER = new org.pegdown.DefaultVerbatimSerializer();

    private final CodeHighlighter highlighter;

    public DefaultVerbatimSerializer(CodeHighlighter highlighter) {
        this.highlighter = highlighter;
    }

    @Override
    public void serialize(VerbatimNode node, Printer printer) {
        boolean success = highlighter.highlight(node.getType(), node.getText(), printer);

        if (!success) {
            FALLBACK_SERIALIZER.serialize(node, printer);
        }
    }
}
