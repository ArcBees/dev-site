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
import java.util.logging.Logger;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;

public class Uploader {

    private static final Logger logger = Logger.getLogger(Uploader.class.getName());

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("Usage Uploader <filesDir> <credentialsFile>|localhost");
            throw new IllegalArgumentException("Usage Uploader <filesDir> <credentialsFile>|localhost");
        }

        String filesDir = args[0];
        logger.info("files directory: '" + filesDir + "'");

        RemoteApiOptions credentials = loadCredentials(args[1]);

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        RemoteApiInstaller installer = new RemoteApiInstaller();

        HashCalculator hashCalculator = new HashCalculatorSha1Impl();
        FileTraverser fileTraverser =
                new FileTraverserFileSystemImpl(new File(filesDir), hashCalculator);
        ResourceUploader fileUploader =
                new ResourceUploaderAppEngineImpl(ds, credentials, installer,
                        new ResourceUploaderAppEngineImpl.KeyProviderImpl());

        UploadController uploadController = new UploadController(fileTraverser, fileUploader);
        uploadController.uploadOutdatedFiles();
    }

    private static RemoteApiOptions loadCredentials(String fileOrLocalhost) throws IOException {
        if (fileOrLocalhost.equals("localhost")) {
            // special case for dev server
            return new RemoteApiOptions().server("localhost", 8080).credentials("nobody@google.com", "ignored");
        } else {
            logger.info("credentials file: '" + fileOrLocalhost + "'");
            return new CredentialsProvider().readCredentialsFromFile(fileOrLocalhost);
        }
    }
}
