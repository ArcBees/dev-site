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

public class Resource {

  private final String key;

  private final String hash;

  public Resource(String key, String hash) {

    if (key == null) {
      throw new IllegalArgumentException("key cannot be null");
    }
    if (hash == null) {
      throw new IllegalArgumentException("hash cannot be null");
    }

    this.key = key;
    this.hash = hash;
  }

  public String getHash() {
    return hash;
  }

  public String getKey() {
    return key;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + hash.hashCode();
    result = prime * result + key.hashCode();
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (!(obj instanceof Resource))
      return false;
    Resource other = (Resource) obj;
    if (!hash.equals(other.hash))
      return false;
    if (!key.equals(other.key))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "Resource [key=" + key + ", hash=" + hash + "]";
  }
}
