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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.tools.remoteapi.RemoteApiInstaller;
import com.google.appengine.tools.remoteapi.RemoteApiOptions;
import com.google.gwt.site.uploader.ResourceUploaderAppEngineImpl.KeyProvider;

/**
 * Test for {@link ResourceUploaderAppEngineImpl}.
 */
public class ResourceUploaderTest {

  private DatastoreService ds;
  private RemoteApiInstaller remoteApiInstaller;
  private ResourceUploaderAppEngineImpl resourceUploader;
  private KeyProvider keyProvider;

  @Before
  public void setup() {
    RemoteApiOptions credentials = new RemoteApiOptions();
    ds = Mockito.mock(DatastoreService.class);

    remoteApiInstaller = Mockito.mock(RemoteApiInstaller.class);

    keyProvider = Mockito.mock(ResourceUploaderAppEngineImpl.KeyProvider.class);

    resourceUploader = new ResourceUploaderAppEngineImpl(ds, credentials, remoteApiInstaller, keyProvider);
  }

  @Test(expected = IllegalStateException.class)
  public void testInitialize() throws InitializeException {
    resourceUploader.initialize();
    resourceUploader.initialize();
  }

  @Test
  public void testInstallIsCalled() throws IOException, InitializeException {
    resourceUploader.initialize();
    Mockito.verify(remoteApiInstaller, Mockito.times(1)).install(
        Mockito.<RemoteApiOptions> anyObject());
    resourceUploader.uninitialize();
    Mockito.verify(remoteApiInstaller, Mockito.times(1)).uninstall();
    try {
      resourceUploader.uploadResource(null, null, null);
      Assert.fail("expected exception did not occur");
    } catch (IllegalStateException e) {
    }
    try {
      resourceUploader.deleteResource(null);
      Assert.fail("expected exception did not occur");
    } catch (IllegalStateException e) {
    }
    try {
      resourceUploader.getRemoteHashes();
      Assert.fail("expected exception did not occur");
    } catch (IllegalStateException e) {
    }
  }

  @Test
  public void testDeleteResource() throws Exception {

    Key key1 = createKey();
    Key key2 = createKey();

    Mockito.when(keyProvider.createKey("DocModel", "resource1")).thenReturn(key1);
    Mockito.when(keyProvider.createKey("DocHash", "resource1")).thenReturn(key2);
    resourceUploader.initialize();

    resourceUploader.deleteResource("resource1");

    ArgumentCaptor<Key> argumentCaptor = ArgumentCaptor.forClass(Key.class);

    Mockito.verify(ds, Mockito.times(2)).delete(argumentCaptor.capture());

    List<Key> keys = argumentCaptor.getAllValues();

    Assert.assertSame(key1, keys.get(0));

    Assert.assertSame(key2, keys.get(1));
  }

  private Key createKey() throws InstantiationException, IllegalAccessException,
      IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
    Constructor<Key> declaredConstructor = Key.class.getDeclaredConstructor(new Class[0]);

    declaredConstructor.setAccessible(true);

    return declaredConstructor.newInstance();
  }

  @Test
  public void testUploadResource() throws IOException, InstantiationException,
      IllegalAccessException, IllegalArgumentException, InvocationTargetException,
      NoSuchMethodException, SecurityException, InitializeException {
    File file =
        new File(getClass().getResource("/com/google/gwt/site/uploader/newfile.html").getFile());
    Key key1 = createKey();
    Key key2 = createKey();

    Mockito.when(keyProvider.createKey("DocModel", "key1")).thenReturn(key1);
    Mockito.when(keyProvider.createKey("DocHash", "key1")).thenReturn(key2);
    resourceUploader.initialize();
    resourceUploader.uploadResource("key1", "hash1", file);

    ArgumentCaptor<Entity> argumentCaptor = ArgumentCaptor.forClass(Entity.class);

    Mockito.verify(ds, Mockito.times(2)).put(argumentCaptor.capture());

    List<Entity> entities = argumentCaptor.getAllValues();

    Assert.assertSame(key1, entities.get(0).getKey());
    Assert.assertEquals(IOUtils.toString(new FileInputStream(file)),
            ((Text)entities.get(0).getProperty("html")).getValue());

    Assert.assertSame(key2, entities.get(1).getKey());
    Assert.assertEquals("hash1" , entities.get(1).getProperty("hash"));
  }
}
