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
package com.google.gwt.site.markdown.toc;

import java.util.List;

import org.parboiled.common.StringUtils;

import com.google.gwt.site.markdown.fs.FolderConfig;
import com.google.gwt.site.markdown.fs.MDNode;
import com.google.gwt.site.markdown.fs.MDParent;

public class TocFromMdCreator implements TocCreator {

    public String createTocForNode(MDParent root, MDNode node) {
        MDNode tmpNode = node;
        while (tmpNode.getParent() != null && tmpNode.getDepth() > 1) {
            tmpNode = tmpNode.getParent();
        }

        MDNode parentNode = tmpNode;

        StringBuffer buffer = new StringBuffer();
        buffer.append("  <ul>\n");
        render(parentNode, buffer, node);
        buffer.append("  </ul>\n");

        return buffer.toString();
    }

    private void render(MDNode node, StringBuffer buffer, MDNode tocNode) {
        MDNode tmpNode = tocNode;
        while (tmpNode.getParent() != null) {
            if (tmpNode.isExcludeFromToc())
                return;
            tmpNode = tmpNode.getParent();
        }

        tmpNode = node;
        while (tmpNode.getParent() != null) {
            if (tmpNode.isExcludeFromToc())
                return;
            tmpNode = tmpNode.getParent();
        }

        // Use 4 spaces to indent <li>'s, so as we have room for indenting <ul>'s
        String margin = StringUtils.repeat(' ', 4 * node.getDepth());

        if (node.isFolder()) {
            MDParent mdParent = node.asFolder();

            FolderConfig config = mdParent.getConfig();
            List<FolderConfig.Entry> entries = config.getFolderEntries();
            List<MDNode> children = mdParent.getChildren();

            if (children.size() >= entries.size()) {
                writeFromNodes(node, buffer, tocNode, margin, children);
            } else {
                openNode(node, buffer, margin);
                writeFromConfig(node, buffer, margin, entries);
                closeNode(buffer, margin);
            }
        } else {
            StringBuilder relativeUrl = new StringBuilder();
            if (tocNode.getDepth() > 0) {
                for (int i = 1; i < tocNode.getDepth(); i++) {
                    relativeUrl.append("../");
                }
            }

            StringBuilder absoluteUrl = new StringBuilder();
            absoluteUrl.append("/");
            String htmlFileName = node.getParent().getHref() + ".html";
            String relativePath = node.getRelativePath().replace(htmlFileName, "");
            absoluteUrl.append(relativePath);

            relativeUrl.append(relativePath);

            buffer.append(margin).append("<li class='file'>");
            // TODO escape HTML
            buffer.append(
                    "<a href='" + relativeUrl.toString() + "' title='" + node.getDescription() + "'>"
                            + node.getDisplayName() + "</a>");
            buffer.append("</li>\n");
        }
    }

    private void writeFromConfig(
            MDNode node,
            StringBuffer buffer,
            String margin,
            List<FolderConfig.Entry> entries) {

        for (FolderConfig.Entry entry : entries) {
            StringBuilder relativeUrl = new StringBuilder();
            if (node.getDepth() > 0) {
                for (int i = 1; i < node.getDepth(); i++) {
                    relativeUrl.append("../");
                }
            }

            if (entry.getSubEntries().isEmpty()) {
                StringBuilder absoluteUrl = new StringBuilder();
                absoluteUrl.append("/");
                absoluteUrl.append(node.getRelativePath()).append(entry.getName());

                relativeUrl.append(node.getRelativePath()).append(entry.getName());

                buffer.append(margin).append("<li class='file'>");

                buffer.append(
                        "<a href='" + relativeUrl.toString() + "' title='" + entry.getDescription() + "'>"
                                + entry.getDisplayName() + "</a>");
                buffer.append("</li>\n");
            } else {
                openNode("#", entry.getDisplayName(), buffer, margin);
                writeFromConfig(node, buffer, margin, entry.getSubEntries());
                closeNode(buffer, margin);
            }
        }
    }

    private void writeFromNodes(
            MDNode node,
            StringBuffer buffer,
            MDNode tocNode,
            String margin,
            List<MDNode> children) {

        boolean writeNode = node.getDepth() > 1 || node.getDepth() == 1 && children.size() == 1;

        if (writeNode) {
            openNode(node, buffer, margin);
        }

        if (children.size() > 1) {
            for (MDNode child : children) {
                render(child, buffer, tocNode);
            }
        }

        if (writeNode) {
            closeNode(buffer, margin);
        }
    }

    private void closeNode(StringBuffer buffer, String margin) {
        buffer.append(margin).append("  </ul>\n");
        buffer.append(margin).append("</li>\n");
    }

    private void openNode(MDNode node, StringBuffer buffer, String margin) {
        openNode(node.getRelativePath(), node.getDisplayName(), buffer, margin);
    }

    private void openNode(String relativePath, String displayName, StringBuffer buffer, String margin) {
        buffer.append(margin).append("<li class='folder'>");
        buffer.append("<a href='").append(relativePath).append("'>");
        buffer.append(displayName);
        buffer.append("</a>\n");
        buffer.append(margin).append("  <ul>\n");
    }
}
