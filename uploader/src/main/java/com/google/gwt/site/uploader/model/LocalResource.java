/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.site.uploader.model;

import java.io.File;

public class LocalResource extends Resource {

    private final File file;

    public LocalResource(String path, String hash, File file) {
        super(path, hash);

        if (file == null) {
            throw new IllegalArgumentException("file cannot be null");
        }

        this.file = file;
    }

    public File getFile() {
        return file;
    }

    @Override
    public String toString() {
        return "LocalResource [ key=" + getKey() + " file=" + file + ", hash=" + getHash() + "]";
    }
}
