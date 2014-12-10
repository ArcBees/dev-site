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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class CredentialsProvider {

    private static final Logger logger = Logger.getLogger(CredentialsProvider.class.getName());

    public RemoteApiOptions readCredentialsFromFile(String credentialsFile) throws IOException {

        String serialized = Files.toString(new File(credentialsFile), Charsets.UTF_8);
        Map<String, List<String>> props = parseProperties(serialized);

        checkOneProperty(props, "host");
        checkOneProperty(props, "email");

        String host = props.get("host").get(0);
        String email = props.get("email").get(0);

        int port = 443;
        try {
            if (props.containsKey("port")) {
                checkOneProperty(props, "port");
                port = Integer.parseInt(props.get("port").get(0));
            }
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "error while parsing port", e);
            throw new RuntimeException("error while parsing port");
        }

        return new RemoteApiOptions()
                .server(host, port)
                .reuseCredentials(email, serialized);
    }

    // taken from RemoteApiInstaller
    private static void checkOneProperty(Map<String, List<String>> props, String key)
            throws IOException {
        if (props.get(key).size() != 1) {
            String message = "invalid credential file (should have one property named '" + key + "')";
            throw new IOException(message);
        }
    }

    // taken from RemoteApiInstaller
    private static Map<String, List<String>> parseProperties(String serializedCredentials) {
        Map<String, List<String>> props = new HashMap<>();
        for (String line : serializedCredentials.split("\n")) {
            line = line.trim();
            if (!line.startsWith("#") && line.contains("=")) {
                int firstEqual = line.indexOf('=');
                String key = line.substring(0, firstEqual);
                String value = line.substring(firstEqual + 1);
                List<String> values = props.get(key);
                if (values == null) {
                    values = new ArrayList<>();
                    props.put(key, values);
                }
                values.add(value);
            }
        }
        return props;
    }
}
