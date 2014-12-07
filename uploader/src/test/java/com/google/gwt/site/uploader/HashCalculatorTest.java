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

import org.junit.Assert;
import org.junit.Test;

/**
 * Test for {@link HashCalculatorSha1Impl}.
 */
public class HashCalculatorTest {
    @Test
    public void testHashGeneration() throws IOException {

        String ap = getClass().getResource("/com/google/gwt/site/uploader/").getFile();
        HashCalculator hc = new HashCalculatorSha1Impl();

        // Compares generated hash with generated hash in Python
        Assert.assertEquals("9181aa55961dc273b3853dad70d378dfd5ee65c2", hc.calculateHash(new File(ap
                + "cssfile2.css")));
        Assert.assertEquals("6886c96032c24e3794dc75550124674e04a6f942", hc.calculateHash(new File(ap
                + "htmlfile1.html")));
        Assert.assertEquals("6392ad77081622b3ec2614b0fa639b8f79ee10f0", hc.calculateHash(new File(ap
                + "folder1/cssfile1.css")));
        Assert.assertEquals("d04a32ed79687bdebb7e1ab34015e46e7ab48a15", hc.calculateHash(new File(ap
                + "folder1/gwt-logo.png")));
        Assert.assertEquals("de68fb08fbc6a050d33bbb01a207dbf12c957c24", hc.calculateHash(new File(ap
                + "folder1/htmlfile2.html")));
        Assert.assertEquals("8eb33ec42bb23dd77731172ebda0d204dbe63b13", hc.calculateHash(new File(ap
                + "folder1/jsfile1.js")));
    }
}
