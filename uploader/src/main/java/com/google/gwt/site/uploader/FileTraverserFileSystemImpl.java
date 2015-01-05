/**
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

package com.google.gwt.site.uploader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gwt.site.uploader.model.LocalResource;

public class FileTraverserFileSystemImpl implements FileTraverser {

    private final File filesDir;
    private final HashCalculator hashCalculator;

    public FileTraverserFileSystemImpl(File filesDir, HashCalculator hashCalculator) {
        this.filesDir = filesDir;
        this.hashCalculator = hashCalculator;
    }

    @Override
    public List<LocalResource> getLocalResources() throws IOException {
        List<LocalResource> resources = new LinkedList<>();
        traverse(filesDir, filesDir, resources);
        return resources;
    }

    private void traverse(File root, File file, List<LocalResource> resources) throws IOException {

        if (file.isDirectory()) {

            File[] listFiles = file.listFiles();
            for (File f : listFiles) {
                traverse(root, f, resources);
            }
        } else if (file.isFile() && shouldFileBeUploaded(file.getName())) {

            String hash = hashCalculator.calculateHash(file);

            String filePath = file.getAbsolutePath();
            String basePath = root.getAbsolutePath();

            // make sure that basePath has a slash at the end
            if (!basePath.endsWith(File.separator)) {
                basePath += File.separator;
            }

            String url = filePath.replace(basePath, "");
            // for windows replace backslashes
            url = url.replace("\\", "/");
            resources.add(new LocalResource(url, hash, file));
        }
    }

    private boolean shouldFileBeUploaded(String fileName) {
        return fileName.endsWith(".html") || fileName.endsWith(".css") || fileName.endsWith(".js")
                || fileName.endsWith(".png") || fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")
                || fileName.endsWith(".gif") || fileName.equals("package-list") || fileName.endsWith(".eot")
                || fileName.endsWith(".svg") || fileName.endsWith(".ttf") || fileName.endsWith(".woff");
    }
}
