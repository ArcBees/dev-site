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

package com.google.gwt.site.markdown.pegdown;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MarkdownToHtmlUtilTest {
    MarkdownToHtmlUtil markdownToHtmlUtil = new MarkdownToHtmlUtil();

    @Test
    public void simpleString_isParagraph() {
        String html = markdownToHtmlUtil.toHtml("blah");

        assertThat(html).isEqualTo("<p>blah</p>");
    }

    @Test
    public void h1() {
        String html = markdownToHtmlUtil.toHtml("#blah");

        assertThat(html).isEqualTo("<h1>blah</h1>");
    }

    @Test
    public void h1_withNumbers() {
        String html = markdownToHtmlUtil.toHtml("#blah6");

        assertThat(html).isEqualTo("<h1>blah6</h1>");
    }

    @Test
    public void h1_withId() {
        String html = markdownToHtmlUtil.toHtml("#blah {myId}");

        assertThat(html).isEqualTo("<h1 id=\"myId\">blah</h1>");
    }

    @Test
    public void h1_withId_complexText() {
        String html = markdownToHtmlUtil.toHtml("#blah6? bon6?jour http://www.google.com {myId}");

        assertThat(html).isEqualTo("<h1 id=\"myId\">blah6? bon6?jour http://www.google.com</h1>");
    }

    @Test
    public void h1_withIdAndClosingHashes() {
        String html = markdownToHtmlUtil.toHtml("#blah # {myId}");

        assertThat(html).isEqualTo("<h1 id=\"myId\">blah</h1>");
    }

    @Test
    public void h1_withClosingHashes() {
        String html = markdownToHtmlUtil.toHtml("#blah #");

        assertThat(html).isEqualTo("<h1>blah</h1>");
    }

    @Test
    public void h2() {
        String html = markdownToHtmlUtil.toHtml("##blah");

        assertThat(html).isEqualTo("<h2>blah</h2>");
    }

    @Test
    public void h2_withId_randomNumberOfClosingHashes() {
        String html = markdownToHtmlUtil.toHtml("##blah ########## {myId}");

        assertThat(html).isEqualTo("<h2 id=\"myId\">blah</h2>");
    }

    @Test
    public void idWithHyphen() {
        String html = markdownToHtmlUtil.toHtml("#blah {my-Id}");

        assertThat(html).isEqualTo("<h1 id=\"my-Id\">blah</h1>");
    }

    @Test
    public void idWithUnderscore() {
        String html = markdownToHtmlUtil.toHtml("#blah {my_Id}");

        assertThat(html).isEqualTo("<h1 id=\"my_Id\">blah</h1>");
    }

    @Test
    public void idWithColon() {
        String html = markdownToHtmlUtil.toHtml("#blah {my:Id}");

        assertThat(html).isEqualTo("<h1 id=\"my:Id\">blah</h1>");
    }

    @Test
    public void idWithPeriod() {
        String html = markdownToHtmlUtil.toHtml("#blah {my.Id}");

        assertThat(html).isEqualTo("<h1 id=\"my.Id\">blah</h1>");
    }

    @Test
    public void divWithId() {
        String html = markdownToHtmlUtil.toHtml("$[myId]");

        assertThat(html).isEqualTo("<div id=\"myId\"></div>");
    }

    @Test
    public void divWithId_hyphen() {
        String html = markdownToHtmlUtil.toHtml("$[my-Id]");

        assertThat(html).isEqualTo("<div id=\"my-Id\"></div>");
    }

    @Test
    public void divWithId_underscore() {
        String html = markdownToHtmlUtil.toHtml("$[my_Id]");

        assertThat(html).isEqualTo("<div id=\"my_Id\"></div>");
    }

    @Test
    public void divWithId_colon() {
        String html = markdownToHtmlUtil.toHtml("$[my:Id]");

        assertThat(html).isEqualTo("<div id=\"my:Id\"></div>");
    }

    @Test
    public void divWithId_period() {
        String html = markdownToHtmlUtil.toHtml("$[my.Id]");

        assertThat(html).isEqualTo("<div id=\"my.Id\"></div>");
    }
}
