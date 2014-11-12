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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.repackaged.com.google.api.client.util.Base64;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.google.gwt.site.uploader.model.Resource;

public class ResourceUploaderAppEngineImpl implements ResourceUploader {

  interface KeyProvider {
    public Key createKey(String kind, String name);
  }

  public static class KeyProviderImpl implements KeyProvider {

    @Override
    public Key createKey(String kind, String name) {
      return KeyFactory.createKey(kind, name);
    }
  }

  private static final String DOC_HASH = "DocHash";
  private static final String DOC_MODEL = "DocModel";
  private static final Logger logger = Logger.getLogger(ResourceUploaderAppEngineImpl.class.getName());
  private final RemoteApiOptions credentials;
  private final DatastoreService ds;
  private final RemoteApiInstaller installer;
  private boolean isInitialized;
  private final KeyProvider keyProvider;

  public ResourceUploaderAppEngineImpl(DatastoreService ds, RemoteApiOptions credentials,
      RemoteApiInstaller installer, KeyProvider keyProvider) {
    this.ds = ds;
    this.credentials = credentials;
    this.installer = installer;
    this.keyProvider = keyProvider;
  }

  @Override
  public void uploadResource(String key, String hash, File data) throws IOException {

    throwIfNotInitialized();

    if (logger.isLoggable(Level.FINE)) {
      logger.fine("uploading file: '" + key + "'");
    }

    Entity entityModel = new Entity(keyProvider.createKey(DOC_MODEL, key));

    Entity entityHash = new Entity(keyProvider.createKey(DOC_HASH, key));

    FileInputStream fileInputStream = null;
    try {
      String text = null;
      fileInputStream = new FileInputStream(data);
      if (isBinaryFile(key)) {
        byte[] byteArray = IOUtils.toByteArray(fileInputStream);
        text = Base64.encodeBase64String(byteArray);
      } else {
        text = IOUtils.toString(fileInputStream, "UTF-8");
      }

      entityModel.setProperty("html", new Text(text));
      entityHash.setProperty("hash", hash);

      // following two puts are not transactional. first put content, then hash.
      // in case the hash value would be put first and something goes wrong before
      // putting the content, the updated content will be lost
      ds.put(entityModel);
      ds.put(entityHash);

    } finally {
      IOUtils.closeQuietly(fileInputStream);
    }
  }

  @Override
  public void deleteResource(String key) {

    throwIfNotInitialized();
    ds.delete(keyProvider.createKey(DOC_MODEL, key));
    ds.delete(keyProvider.createKey(DOC_HASH, key));
  }

  @Override
  public List<Resource> getRemoteHashes() throws IOException {

    throwIfNotInitialized();

    List<Resource> hashes = new ArrayList<>();

    while (true) {
      try {

        String data = getJsonFromServer(hashes);

        List<Resource> resources = parseJson(data);

        // we are done when the server returns no more resources
        if (resources.size() == 0) {
          break;
        }
        hashes.addAll(resources);

      } catch (MalformedURLException | ProtocolException e) {
        logger.log(Level.SEVERE, "error building url", e);
        throw new IOException("error building url", e);
      } catch (IOException e) {
        throw e;
      } catch (JSONException e) {
        logger.log(Level.SEVERE, "error parsing json", e);
        throw new IOException("error parsing json", e);
      }
    }
    return hashes;
  }

  private List<Resource> parseJson(String data) throws JSONException {
    List<Resource> hashes = new ArrayList<>();
    JSONObject root = new JSONObject(data);
    JSONArray array = (JSONArray) root.get("hashes");

    for (int i = 0; i < array.length(); i++) {
      JSONObject hashEntity = array.getJSONObject(i);
      String key = hashEntity.getString("key");
      String hash = hashEntity.getString("hash");
      hashes.add(new Resource(key, hash));
    }

    return hashes;
  }

  private String getJsonFromServer(List<Resource> hashes) throws MalformedURLException,
      IOException, ProtocolException {
    String urlString = buildUrl(hashes.size());

    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");

    if (conn.getResponseCode() != 200) {
      throw new IOException("Failed : HTTP error code : " + conn.getResponseCode());
    }

    InputStream inputStream = conn.getInputStream();
    String data = IOUtils.toString(inputStream);
    conn.disconnect();
    return data;
  }

  private String buildUrl(int count) {
    String protocol = "http://";

    // a bit hacky, but okay for now
    if (credentials.getPort() == 443) {
      protocol = "https://";
    }

    return protocol + credentials.getHostname() + ":" + credentials.getPort() + "/hash?count=" + count;
  }

  @Override
  public void initialize() throws InitializeException {
    if (isInitialized) {
      throw new IllegalStateException("app engine remote api was already initialized");
    }
    try {
      installer.install(credentials);
      isInitialized = true;
    } catch (IOException e) {
      logger.log(Level.SEVERE, "can not initialize app engine", e);
      throw new InitializeException("can not initialize app engine", e);
    }
  }

  @Override
  public void uninitialize() {
    if (!isInitialized) {
      throw new IllegalStateException("app engine remote api was not initialized");
    }
    installer.uninstall();
    isInitialized = false;
  }

  private boolean isBinaryFile(String path) {
    return path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg")
        || path.endsWith(".gif");
  }

  private void throwIfNotInitialized() {
    if (!isInitialized) {
      throw new IllegalStateException(
          "tried uploading resource but app engine remote api was not initialized");
    }
  }
}
