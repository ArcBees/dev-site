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

import org.pegdown.Printer;

class PegdownPrinterWriter extends Writer {
    private final Printer printer;

    PegdownPrinterWriter(Printer printer) {
        this.printer = printer;
    }

    @Override
    public void write(char[] value, int offset, int length) throws IOException {
        printer.sb.append(value, offset, length);
    }

    @Override
    public void flush() throws IOException {
    }

    @Override
    public void close() throws IOException {
    }
}
