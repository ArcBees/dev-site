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

package com.google.gwt.site.markdown;

import junit.framework.TestCase;

/**
 * Test class for MDTranslate
 */
public class MDTranslaterTest extends TestCase {

  private void assertAdjustUrl(MDTranslater md, String relativePath, String tag, String attr,
      String url, String match) {
    String ini = "<foo>\n </foo>";
    String end = "<bar>\n </bar>";
    String html = ini + "<" + tag + " " + attr + "='" + url + "' foo='bar'><" + tag + "/>" + end;
    String expected =
        ini + "<" + tag + " " + attr + "='" + match + "' foo='bar'><" + tag + "/>" + end;
    String actual = md.adjustRelativePath(html, relativePath);
    assertEquals(expected, actual);
  }

  public void testAdjustRelativePath() throws Exception {
    MDTranslater mdt = new MDTranslater(null, null, null);

    // empty url
    assertAdjustUrl(mdt, "../", "a", "href", "", "../");
    // absolute url
    assertAdjustUrl(mdt, "../", "a", "href", "/index.html", "../index.html");
    // relative url
    assertAdjustUrl(mdt, "../", "a", "href", "index.html", "../index.html");
    assertAdjustUrl(mdt, "../", "a", "href", "./index.html", ".././index.html");

    // Do not change protocol urls
    assertAdjustUrl(mdt, "../", "a", "href", "mailto:foo@bar", "mailto:foo@bar");
    assertAdjustUrl(mdt, "../", "a", "href", "http://foo.bar/whatever", "http://foo.bar/whatever");
    assertAdjustUrl(mdt, "../", "a", "href", "https://foo.bar/whatever", "https://foo.bar/whatever");

    // Do not change hashes
    assertAdjustUrl(mdt, "../", "a", "href", "#", "#");
    assertAdjustUrl(mdt, "../", "a", "href", "#foo", "#foo");

    // Check script and link
    assertAdjustUrl(mdt, "../../", "script", "src", "/gwtproject/gwtproject.nocache.js",
        "../../gwtproject/gwtproject.nocache.js");
    assertAdjustUrl(mdt, "../", "link", "href", "/main.css", "../main.css");

    // Check quotes inside quotes.
    assertAdjustUrl(mdt, "../", "a", "href", "a\"b\"c", "../a\"b\"c");
    assertAdjustUrl(mdt, "../", "a", "href",
        "javascript:alert(\"hello\");", "javascript:alert(\"hello\");");
  }
}
