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

import java.io.File;

import com.google.gwt.site.markdown.fs.FileSystemTraverser;
import com.google.gwt.site.markdown.fs.MDParent;
import com.google.gwt.site.markdown.pegdown.MarkdownToHtmlUtil;
import com.google.gwt.site.markdown.toc.TocCreator;
import com.google.gwt.site.markdown.toc.TocFromMdCreator;
import com.google.gwt.site.markdown.toc.TocFromTemplateCreator;
import com.google.gwt.site.markdown.velocity.VelocityEngineProvider;
import com.google.gwt.site.markdown.velocity.VelocityWrapperFactory;

public class MDHelper {
    private String sourceDirectory;
    private String outputDirectory;
    private String templateFile;
    private String templateTocFile;
    private String editRootUrl;

    private MarkdownToHtmlUtil markdownToHtmlUtil;
    private File sourceDirectoryFile;
    private MDTranslater translater;

    public MDHelper(MarkdownToHtmlUtil markdownToHtmlUtil) {
        this.markdownToHtmlUtil = markdownToHtmlUtil;
    }

    public MDHelper setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = Strings.emptyToNull(sourceDirectory);
        return this;
    }

    public MDHelper setOutputDirectory(String outputDirectory) {
        this.outputDirectory = Strings.emptyToNull(outputDirectory);
        return this;
    }

    public MDHelper setTemplateFile(String templateFile) {
        this.templateFile = Strings.emptyToNull(templateFile);
        return this;
    }

    public MDHelper setEditRootUrl(String editRootUrl) {
        this.editRootUrl = Strings.emptyToNull(editRootUrl);
        return this;
    }

    public MDHelper setTemplateToc(String templateTocFile) {
        this.templateTocFile = Strings.emptyToNull(templateTocFile);
        return this;
    }

    public MDHelper create() throws MDHelperException {
        sourceDirectoryFile = validateSourceDirectory();
        File outputDirectoryFile = validateOutputDirectory();
        validateTemplate();
        TocCreator tocCreator = validateTocTemplate();

        MarkupWriter writer = new MarkupWriter(outputDirectoryFile);
        VelocityWrapperFactory velocityFactory = new VelocityWrapperFactory(new VelocityEngineProvider());

        translater = new MDTranslater(tocCreator, writer, templateFile, editRootUrl, velocityFactory);

        return this;
    }

    private File validateSourceDirectory() throws MDHelperException {
        if (sourceDirectory == null) {
            throw new MDHelperException("no sourceDirectory set");
        }
        File directory = new File(sourceDirectory);

        if (!directory.exists()) {
            throw new MDHelperException("sourceDirectory ('" + sourceDirectory + "') does not exist");
        }
        if (!directory.isDirectory()) {
            throw new MDHelperException("sourceDirectory ('" + sourceDirectory + "') is no directory");
        }
        if (!directory.canRead()) {
            throw new MDHelperException("sourceDirectory ('" + sourceDirectory + "') can not read source directory");
        }

        return directory;
    }

    private File validateOutputDirectory() throws MDHelperException {
        if (outputDirectory == null) {
            throw new MDHelperException("no outputDirectory set");
        }
        File directory = new File(outputDirectory);

        if (!directory.exists() && !directory.mkdirs()) {
            throw new MDHelperException("outputDirectory ('" + directory + "') can not be created");
        }
        if (!directory.isDirectory()) {
            throw new MDHelperException("outputDirectory ('" + directory + "') is no directory");
        }
        if (!directory.canWrite()) {
            throw new MDHelperException("outputDirectory ('" + directory + "') can not write to output directory");
        }

        return directory;
    }

    private void validateTemplate() throws MDHelperException {
        if (templateFile == null) {
            throw new MDHelperException("no templateFile set");
        }
    }

    private TocCreator validateTocTemplate() throws MDHelperException {
        if (templateTocFile != null) {
            String templateToc = markdownToHtmlUtil.convert(readFile(templateTocFile)).getHtml();

            return new TocFromTemplateCreator(templateToc);
        }

        return new TocFromMdCreator();
    }

    private String readFile(String filePath) throws MDHelperException {
        File file = new File(filePath);
        try {
            return Util.getStringFromFile(file);
        } catch (Exception e) {
            throw new MDHelperException("can not read file" + filePath, e);
        }
    }

    public void translate() throws TranslaterException {
        if (translater == null) {
            throw new IllegalStateException();
        }

        FileSystemTraverser traverser = new FileSystemTraverser();
        MDParent mdRoot = traverser.traverse(sourceDirectoryFile);

        translater.render(mdRoot);
    }
}
