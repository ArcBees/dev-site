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

package com.google.gwt.site.webapp.client;

import com.google.gwt.regexp.shared.RegExp;

/**
 * Test class for the GWTProject entry-point for running in GWT
 */
public class ProjectEntryPointGwtTest extends ProjectEntryPointTest {

  public String getModuleName() {
    return "com.google.gwt.site.webapp.Test";
  }

  protected String getOrigin() {
    return GWTProjectEntryPoint.origin;
  }

  protected RegExp getSameOriginRegex() {
    return GWTProjectEntryPoint.isSameOriginRexp;
  }
}
