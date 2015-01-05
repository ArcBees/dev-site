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
import java.util.List;

import com.google.gwt.site.uploader.model.Resource;

/**
 * A {@link ResourceUploader} communicates with the server.
 * <p/>
 * It deletes, uploads and gets the key and hash of the resources.
 * <p/>
 * <p>Note: Need to call {@link #initialize()} before uploading, deleting or getting remote Hashes. Need to call
 * {@link #uninitialize()} afterwards.
 */
public interface ResourceUploader {

    /**
     * Delete a given resource.
     */
    void deleteResource(String key) throws IOException;

    /**
     * Upload a given resource.
     */
    void uploadResource(String key, String hash, File data) throws IOException;

    /**
     * Get hashes for all remote resources.
     */
    List<Resource> getRemoteHashes() throws IOException;

    /**
     * Initializes the ResourceUploader.
     * <p/>
     * <p>Note: Needs to be called before uploading, deleting or getting remote Hashes. Need to call
     * {@link #uninitialize()} afterwards.
     */
    void initialize() throws InitializeException;

    /**
     * Uninitializes the ResourceUploader.
     */
    void uninitialize();
}
