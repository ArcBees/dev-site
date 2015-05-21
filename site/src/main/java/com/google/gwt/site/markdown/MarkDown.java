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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import com.google.gwt.site.markdown.pegdown.MarkdownToHtmlUtil;

public class MarkDown {
    private static final Options OPTIONS = new Options();
    private static final String HELP = "help";
    private static final String SOURCE = "source";
    private static final String OUTPUT = "output";
    private static final String TEMPLATE = "template";
    private static final String EDIT_URL = "edit-url";
    private static final String TEMPLATE_TOC = "template-toc";

    static {
        OPTIONS.addOption(Option.builder("s").required().hasArg().longOpt(SOURCE).desc("Source directory").build());
        OPTIONS.addOption(Option.builder("o").required().hasArg().longOpt(OUTPUT).desc("Output directory").build());
        OPTIONS.addOption(Option.builder("t").required().hasArg().longOpt(TEMPLATE).desc("Template file").build());
        OPTIONS.addOption(Option.builder("e").hasArg().longOpt(EDIT_URL).desc("Edit root URL").build());
        OPTIONS.addOption(Option.builder("c").hasArg().longOpt(TEMPLATE_TOC).desc("Template TOC file").build());
        OPTIONS.addOption(Option.builder("h").longOpt(HELP).desc("help").build());
    }

    public static void main(String[] args) throws MDHelperException, TranslaterException, ParseException {
        CommandLine commandLine = parseArgs(args);

        if (commandLine.hasOption(HELP)) {
            printHelp();
        } else {
            translateMarkDown(commandLine);
        }
    }

    private static CommandLine parseArgs(String[] args) throws ParseException {
        CommandLineParser parser = new DefaultParser();
        return parser.parse(OPTIONS, args);
    }

    private static void printHelp() {
        new HelpFormatter().printHelp(MarkDown.class.getSimpleName(), OPTIONS);
    }

    private static void translateMarkDown(CommandLine commandLine) throws TranslaterException, MDHelperException {
        MDHelper helper = new MDHelper(new MarkdownToHtmlUtil());

        helper.setOutputDirectory(commandLine.getOptionValue(OUTPUT))
                .setSourceDirectory(commandLine.getOptionValue(SOURCE))
                .setTemplateFile(commandLine.getOptionValue(TEMPLATE))
                .setTemplateToc(commandLine.getOptionValue(TEMPLATE_TOC))
                .setEditRootUrl(commandLine.getOptionValue(EDIT_URL))
                .create()
                .translate();
    }
}
