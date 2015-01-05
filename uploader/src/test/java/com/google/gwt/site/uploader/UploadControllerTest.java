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

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import com.google.gwt.site.uploader.model.LocalResource;
import com.google.gwt.site.uploader.model.Resource;

/**
 * Test for {@link UploadController}.
 */
public class UploadControllerTest {

    private ArrayList<LocalResource> localFiles;
    private ArrayList<Resource> remoteHashes;
    private FileTraverser fileTraverser;
    private ResourceUploader resourceUploader;
    private UploadController controller;
    private File mockFile;

    @Before
    public void setup() {
        mockFile = new File("");
        fileTraverser = Mockito.mock(FileTraverser.class);
        resourceUploader = Mockito.mock(ResourceUploader.class);
        controller = new UploadController(fileTraverser, resourceUploader);

        // setup mockdata
        File mockFile = new File("");
        localFiles = new ArrayList<>();

        localFiles.add(new LocalResource("fileName1", "hash1", mockFile));
        localFiles.add(new LocalResource("fileName2", "hash2", mockFile));
        localFiles.add(new LocalResource("fileName3", "hash3", mockFile));
        localFiles.add(new LocalResource("fileName4", "hash4", mockFile));
        localFiles.add(new LocalResource("fileName5", "hash5", mockFile));
        localFiles.add(new LocalResource("fileName6", "hash6", mockFile));

        remoteHashes = new ArrayList<>();
        remoteHashes.add(new Resource("fileName1", "hash1"));
        remoteHashes.add(new Resource("fileName2", "hash2"));
        remoteHashes.add(new Resource("fileName3", "hash3"));
        remoteHashes.add(new Resource("fileName4", "hash4"));
        remoteHashes.add(new Resource("fileName5", "hash5"));
        remoteHashes.add(new Resource("fileName6", "hash6"));
    }

    @Test
    public void testNothingToUpload() throws IOException {

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);

        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();
        Mockito.verify(resourceUploader, Mockito.times(0)).uploadResource(Mockito.anyString(),
                Mockito.anyString(), Mockito.<File>anyObject());
        Mockito.verify(resourceUploader, Mockito.times(0)).deleteResource(Mockito.anyString());
    }

    @Test
    public void testOneLocalFileChanged() throws IOException {

        localFiles.set(0, new LocalResource("fileName1", "changedHash1", mockFile));

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName1", "changedHash1",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(0)).deleteResource(Mockito.anyString());
    }

    @Test
    public void testAddedOneLocalFile() throws IOException {

        localFiles.add(new LocalResource("fileName7", "hash7", mockFile));

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName7", "hash7",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(0)).deleteResource(Mockito.anyString());
    }

    @Test
    public void testRemovedOneLocalFile() throws IOException {
        localFiles.remove(5);

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();
        Mockito.verify(resourceUploader, Mockito.times(0)).uploadResource(Mockito.anyString(),
                Mockito.anyString(), Mockito.<File>anyObject());
        Mockito.verify(resourceUploader, Mockito.times(1)).deleteResource("fileName6");
    }

    @Test
    public void testMovedOneLocalFile() throws IOException {

        localFiles.set(0, new LocalResource("changedPath", "hash1", mockFile));

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("changedPath", "hash1",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).deleteResource("fileName1");
    }

    @Test
    public void testFileOnServerWasRemoved() throws IOException {

        remoteHashes.remove(5);

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName6", "hash6",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(0)).deleteResource(Mockito.anyString());
    }

    @Test
    public void testFourLocalFilesChanged() throws IOException {

        localFiles.set(0, new LocalResource("fileName1", "hash11", mockFile));
        localFiles.set(1, new LocalResource("fileName2", "hash22", mockFile));
        localFiles.set(2, new LocalResource("fileName3", "hash33", mockFile));
        localFiles.set(3, new LocalResource("fileName4", "hash44", mockFile));

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();

        Mockito.verify(resourceUploader, Mockito.times(4)).uploadResource(Mockito.anyString(),
                Mockito.anyString(), Mockito.<File>anyObject());

        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName1", "hash11",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName2", "hash22",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName3", "hash33",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName4", "hash44",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(0)).deleteResource(Mockito.anyString());
    }

    @Test
    public void testFourLocalFilesChangedAndOneRemoved() throws IOException {

        localFiles.set(0, new LocalResource("fileName1", "hash11", mockFile));
        localFiles.set(1, new LocalResource("fileName2", "hash22", mockFile));
        localFiles.set(2, new LocalResource("fileName3", "hash33", mockFile));
        localFiles.set(3, new LocalResource("fileName4", "hash44", mockFile));
        localFiles.remove(5);

        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();

        Mockito.verify(resourceUploader, Mockito.times(4)).uploadResource(Mockito.anyString(),
                Mockito.anyString(), Mockito.<File>anyObject());

        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName1", "hash11",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName2", "hash22",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName3", "hash33",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).uploadResource("fileName4", "hash44",
                mockFile);
        Mockito.verify(resourceUploader, Mockito.times(1)).deleteResource(Mockito.anyString());
        Mockito.verify(resourceUploader, Mockito.times(1)).deleteResource("fileName6");

        InOrder inOrder = Mockito.inOrder(resourceUploader);
        inOrder.verify(resourceUploader, Mockito.times(4)).uploadResource(Mockito.anyString(),
                Mockito.anyString(), Mockito.<File>anyObject());
        inOrder.verify(resourceUploader, Mockito.times(1)).deleteResource(Mockito.anyString());
    }

    @Test
    public void testExecutionOfInitializeAndUnInitialize() throws IOException, InitializeException {
        Mockito.when(fileTraverser.getLocalResources()).thenReturn(localFiles);
        Mockito.when(resourceUploader.getRemoteHashes()).thenReturn(remoteHashes);

        controller.uploadOutdatedFiles();

        InOrder inOrder = Mockito.inOrder(resourceUploader);
        inOrder.verify(resourceUploader, Mockito.times(1)).initialize();
        inOrder.verify(resourceUploader, Mockito.times(1)).uninitialize();
    }

    @Test(expected = RuntimeException.class)
    public void testException() throws IOException {
        Mockito.doThrow(new IOException()).when(fileTraverser).getLocalResources();

        controller.uploadOutdatedFiles();
    }
}
