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
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.gwt.site.uploader.model.LocalResource;

/**
 * Test for {@link FileTraverserFileSystemImpl}.
 */
public class FileTraverserTest {

    private File baseDir;
    private FileTraverserFileSystemImpl fileTraverser;
    private HashCalculator hashCalculatorMock;
    private ArrayList<LocalResource> expectedFiles;

    @Before
    public void setup() {
        baseDir = new File(getClass().getResource("/com/google/gwt/site/uploader/").getFile());
        hashCalculatorMock = Mockito.mock(HashCalculator.class);
        fileTraverser = new FileTraverserFileSystemImpl(baseDir, hashCalculatorMock);

        expectedFiles = new ArrayList<>();
        expectedFiles.add(new LocalResource("cssfile2.css", "1", new File(baseDir, "cssfile2.css")));
        expectedFiles
                .add(new LocalResource("htmlfile1.html", "2", new File(baseDir, "htmlfile1.html")));
        expectedFiles.add(new LocalResource("newfile.html", "3", new File(baseDir, "newfile.html")));
        expectedFiles.add(new LocalResource("folder1/cssfile1.css", "4", new File(baseDir,
                "folder1/cssfile1.css")));
        expectedFiles.add(new LocalResource("folder1/gwt-logo.png", "5", new File(baseDir,
                "folder1/gwt-logo.png")));
        expectedFiles.add(new LocalResource("folder1/htmlfile2.html", "6", new File(baseDir,
                "folder1/htmlfile2.html")));
        expectedFiles.add(new LocalResource("folder1/jsfile1.js", "7", new File(baseDir,
                "folder1/jsfile1.js")));
        expectedFiles.add(new LocalResource("package-list", "8", new File(baseDir, "package-list")));
    }

    @Test
    public void testTraverse() throws IOException {

        for (LocalResource resource : expectedFiles) {
            Mockito.when(hashCalculatorMock.calculateHash(resource.getFile())).thenReturn(
                    resource.getHash());
        }

        List<LocalResource> files = fileTraverser.getLocalResources();

        Assert.assertEquals(expectedFiles.size(), files.size());
        Assert.assertTrue(files.containsAll(expectedFiles));
    }
}
