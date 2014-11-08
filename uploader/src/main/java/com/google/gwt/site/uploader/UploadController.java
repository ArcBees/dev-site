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

package com.google.gwt.site.uploader;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.site.uploader.model.LocalResource;
import com.google.gwt.site.uploader.model.Resource;
import com.google.gwt.site.uploader.model.ResourceKey;

/**
 * Manages the upload of changed resources.
 */
public class UploadController {

  private static final Logger logger = Logger.getLogger(UploadController.class.getName());
  private ResourceUploader fileUploader;
  private FileTraverser fileTraverser;

  public UploadController(FileTraverser fileTraverser, ResourceUploader fileUploader) {
    this.fileTraverser = fileTraverser;
    this.fileUploader = fileUploader;
  }

  /**
   * Calculates, deletes and uploads changed resources.
   */
  public void uploadOutdatedFiles() {

    try {
      fileUploader.initialize();
    } catch (InitializeException e) {
      logger.log(Level.SEVERE, "could not initialize file uploader", e);
      throw new RuntimeException("could not initialize file uploader", e);
    }

    try {

      Set<Resource> remoteResources = new HashSet<>(fileUploader.getRemoteHashes());

      Set<LocalResource> localResources =
          new HashSet<>(fileTraverser.getLocalResources());

      logger.log(Level.INFO, "found " + localResources.size() + "resources");

      Set<Resource> toBeRemovedOrChanged = new HashSet<>(remoteResources);

      toBeRemovedOrChanged.removeAll(localResources);

      Set<LocalResource> toBeUploaded = new HashSet<>(localResources);

      toBeUploaded.removeAll(remoteResources);

      Set<ResourceKey> toBeRemoved = copyResources(toBeRemovedOrChanged);

      Set<ResourceKey> toBeUploadedKeys = copyResources(toBeUploaded);

      toBeRemoved.removeAll(toBeUploadedKeys);

      if (logger.isLoggable(Level.INFO)) {
        logger.log(Level.INFO, toBeUploaded.size() + " file(s) to upload and " + toBeRemoved.size()
            + " files(s) to delete");
      }

      uploadResources(toBeUploaded);

      removeResources(toBeRemoved);

    } catch (IOException e) {
      logger.log(Level.SEVERE, "can not upload files", e);
      throw new RuntimeException("can not upload files", e);
    } finally {
      fileUploader.uninitialize();
    }
  }

  private Set<ResourceKey> copyResources(Set<? extends Resource> toBeRemovedOrChanged) {
    Set<ResourceKey> toBeRemoved = new HashSet<>();
    for (Resource resource : toBeRemovedOrChanged) {
      toBeRemoved.add(new ResourceKey(resource));
    }
    return toBeRemoved;
  }

  private void removeResources(Set<ResourceKey> toBeRemoved) throws IOException {
    int count = 1;
    for (ResourceKey resource : toBeRemoved) {
      if (count % 10 == 1  && logger.isLoggable(Level.INFO)) {
        logger.info("uploading file " + count + " of " + toBeRemoved.size());
      }
      count++;
      fileUploader.deleteResource(resource.getResource().getKey());
    }
  }

  private void uploadResources(Set<LocalResource> toBeUploaded) throws IOException {
    int count = 1;
    for (LocalResource r : toBeUploaded) {
      if (count % 10 == 1  && logger.isLoggable(Level.INFO)) {
        logger.info("uploading file " + count + " of " + toBeUploaded.size());
      }
      count++;
      fileUploader.uploadResource(r.getKey(), r.getHash(), r.getFile());
    }
  }
}
