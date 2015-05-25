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
import java.io.IOException;
import java.util.Properties;

import com.google.gwt.site.markdown.fs.FileSystemTraverser;
import com.google.gwt.site.markdown.fs.MDParent;
import com.google.gwt.site.markdown.pegdown.MarkdownParser;
import com.google.gwt.site.markdown.toc.TocCreator;
import com.google.gwt.site.markdown.toc.TocFromMdCreator;
import com.google.gwt.site.markdown.toc.TocFromTemplateCreator;
import com.google.gwt.site.markdown.velocity.VelocityEngineProvider;
import com.google.gwt.site.markdown.velocity.VelocityWrapperFactory;

import static com.google.gwt.site.markdown.Strings.emptyToNull;

public class MDHelper {
    private String sourceDirectory;
    private String outputDirectory;
    private String templateFile;
    private String templateTocFile;
    private String editRootUrl;
    private String markdownVariablesFile;

    private File sourceDirectoryFile;
    private MDTranslater translater;

    public MDHelper setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = emptyToNull(sourceDirectory);
        return this;
    }

    public MDHelper setOutputDirectory(String outputDirectory) {
        this.outputDirectory = emptyToNull(outputDirectory);
        return this;
    }

    public MDHelper setTemplateFile(String templateFile) {
        this.templateFile = emptyToNull(templateFile);
        return this;
    }

    public MDHelper setTemplateToc(String templateTocFile) {
        this.templateTocFile = emptyToNull(templateTocFile);
        return this;
    }

    public MDHelper setEditRootUrl(String editRootUrl) {
        this.editRootUrl = emptyToNull(editRootUrl);
        return this;
    }

    public MDHelper setMarkdownVariablesFile(String markdownVariablesFile) {
        this.markdownVariablesFile = emptyToNull(markdownVariablesFile);
        return this;
    }

    public MDHelper create() throws MDHelperException {
        validateTemplate();

        sourceDirectoryFile = resolveSourceDirectory();

        translater = buildTranslater();

        return this;
    }

    private File resolveSourceDirectory() throws MDHelperException {
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

    private MarkdownParser buildMarkdownParser() throws MDHelperException {
        Properties properties = new Properties();

        if (markdownVariablesFile != null) {
            try {
                properties.load(getClass().getResourceAsStream(markdownVariablesFile));
            } catch (IOException e) {
                throw new MDHelperException("Markdown Variables ('" + markdownVariablesFile + "') can not be loaded");
            }
        }

        return new MarkdownParser(properties);
    }

    private MDTranslater buildTranslater() throws MDHelperException {
        MarkdownParser markdownParser = buildMarkdownParser();
        TocCreator tocCreator = resolveTocTemplate(markdownParser);
        MarkupWriter writer = new MarkupWriter(resolveOutputDirectory());
        VelocityWrapperFactory velocityFactory = new VelocityWrapperFactory(new VelocityEngineProvider());

        return new MDTranslater(markdownParser, tocCreator, writer, templateFile, editRootUrl, velocityFactory);
    }

    private File resolveOutputDirectory() throws MDHelperException {
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

    private TocCreator resolveTocTemplate(MarkdownParser markdownParser) throws MDHelperException {
        if (templateTocFile != null) {
            String templateToc = markdownParser.toHtml(readFile(templateTocFile));

            return new TocFromTemplateCreator(templateToc);
        }

        return new TocFromMdCreator();
    }

    private String readFile(String filePath) throws MDHelperException {
        File file = new File(filePath);
        try {
            return FilesUtils.getStringFromFile(file);
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
