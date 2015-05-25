/**
 * Copyright 2015 ArcBees Inc.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package com.google.gwt.site.markdown.pegdown;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static com.google.gwt.site.markdown.pegdown.variabletemplate.VariableParserPlugin.VARIABLE_TEMPLATE_END;
import static com.google.gwt.site.markdown.pegdown.variabletemplate.VariableParserPlugin.VARIABLE_TEMPLATE_START;

public class MarkdownParserTest {
    private static final String SOME_VERSION_VALUE = "1.1.6";
    private static final String SOME_VERSION_NAME = "someVersion";

    MarkdownParser markdownParser;

    @Before
    public void setUp() {
        Properties variables = new Properties();
        variables.put(SOME_VERSION_NAME, SOME_VERSION_VALUE);
        variables.put("someOtherVersion", "203645");

        markdownParser = new MarkdownParser(variables);
    }

    @Test
    public void variable_exists() {
        String variable = VARIABLE_TEMPLATE_START + SOME_VERSION_NAME + VARIABLE_TEMPLATE_END;
        String html = markdownParser.toHtml("blah " + variable + " blah");

        assertThat(html).isEqualTo("<p>blah " + SOME_VERSION_VALUE + " blah</p>");
    }

    @Test
    public void variable_doesNotExist() {
        String variable = VARIABLE_TEMPLATE_START + SOME_VERSION_NAME.toLowerCase() + VARIABLE_TEMPLATE_END;
        String html = markdownParser.toHtml("blah " + variable + " blah");

        assertThat(html).isEqualTo("<p>blah " + variable + " blah</p>");
    }

    @Test
    public void variable_escaped() {
        String variable = VARIABLE_TEMPLATE_START + SOME_VERSION_NAME + VARIABLE_TEMPLATE_END;
        String html = markdownParser.toHtml("blah \\" + variable + " blah");

        assertThat(html).isEqualTo("<p>blah " + variable + " blah</p>");
    }

    @Test
    public void simpleString_isParagraph() {
        String html = markdownParser.toHtml("blah");

        assertThat(html).isEqualTo("<p>blah</p>");
    }

    @Test
    public void h1() {
        String html = markdownParser.toHtml("#blah");

        assertThat(html).isEqualTo("<h1>blah</h1>");
    }

    @Test
    public void h1_withNumbers() {
        String html = markdownParser.toHtml("#blah6");

        assertThat(html).isEqualTo("<h1>blah6</h1>");
    }

    @Test
    public void h1_withId() {
        String html = markdownParser.toHtml("#blah {myId}");

        assertThat(html).isEqualTo("<h1 id=\"myId\">blah</h1>");
    }

    @Test
    public void h1_withId_complexText() {
        String html = markdownParser.toHtml("#blah6? bon6?jour http://www.google.com {myId}");

        assertThat(html).isEqualTo("<h1 id=\"myId\">blah6? bon6?jour http://www.google.com</h1>");
    }

    @Test
    public void h1_withIdAndClosingHashes() {
        String html = markdownParser.toHtml("#blah # {myId}");

        assertThat(html).isEqualTo("<h1 id=\"myId\">blah</h1>");
    }

    @Test
    public void h1_withClosingHashes() {
        String html = markdownParser.toHtml("#blah #");

        assertThat(html).isEqualTo("<h1>blah</h1>");
    }

    @Test
    public void h2() {
        String html = markdownParser.toHtml("##blah");

        assertThat(html).isEqualTo("<h2>blah</h2>");
    }

    @Test
    public void h2_withId_randomNumberOfClosingHashes() {
        String html = markdownParser.toHtml("##blah ########## {myId}");

        assertThat(html).isEqualTo("<h2 id=\"myId\">blah</h2>");
    }

    @Test
    public void idWithHyphen() {
        String html = markdownParser.toHtml("#blah {my-Id}");

        assertThat(html).isEqualTo("<h1 id=\"my-Id\">blah</h1>");
    }

    @Test
    public void idWithUnderscore() {
        String html = markdownParser.toHtml("#blah {my_Id}");

        assertThat(html).isEqualTo("<h1 id=\"my_Id\">blah</h1>");
    }

    @Test
    public void idWithColon() {
        String html = markdownParser.toHtml("#blah {my:Id}");

        assertThat(html).isEqualTo("<h1 id=\"my:Id\">blah</h1>");
    }

    @Test
    public void idWithPeriod() {
        String html = markdownParser.toHtml("#blah {my.Id}");

        assertThat(html).isEqualTo("<h1 id=\"my.Id\">blah</h1>");
    }

    @Test
    public void divWithId() {
        String html = markdownParser.toHtml("$[myId]");

        assertThat(html).isEqualTo("<div id=\"myId\"></div>");
    }

    @Test
    public void divWithId_hyphen() {
        String html = markdownParser.toHtml("$[my-Id]");

        assertThat(html).isEqualTo("<div id=\"my-Id\"></div>");
    }

    @Test
    public void divWithId_underscore() {
        String html = markdownParser.toHtml("$[my_Id]");

        assertThat(html).isEqualTo("<div id=\"my_Id\"></div>");
    }

    @Test
    public void divWithId_colon() {
        String html = markdownParser.toHtml("$[my:Id]");

        assertThat(html).isEqualTo("<div id=\"my:Id\"></div>");
    }

    @Test
    public void divWithId_period() {
        String html = markdownParser.toHtml("$[my.Id]");

        assertThat(html).isEqualTo("<div id=\"my.Id\"></div>");
    }
}
