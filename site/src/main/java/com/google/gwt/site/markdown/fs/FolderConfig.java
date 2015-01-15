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

package com.google.gwt.site.markdown.fs;

import java.util.List;

public class FolderConfig {
    public static class Entry {
        private final String name;
        private final String title;
        private final String displayName;
        private final String description;
        private final List<Entry> subEntries;

        public Entry(
                String name,
                String displayName,
                String title,
                String description,
                List<Entry> subEntries) {
            this.name = name;
            this.displayName = displayName;
            this.title = title;
            this.description = description;
            this.subEntries = subEntries;
        }

        public String getDescription() {
            return description;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getName() {
            return name;
        }

        public String getTitle() {
            return title;
        }

        public List<Entry> getSubEntries() {
            return subEntries;
        }
    }

    private final String displayName;
    private final String folderHref;
    private final String title;
    private final String description;
    private final String style;
    private final String logo;
    private final List<String> excludeList;
    private final List<Entry> folderEntries;

    public FolderConfig(
            String displayName,
            String folderHref,
            String title,
            String description,
            String style,
            String logo,
            List<String> excludeList,
            List<Entry> folderEntries) {
        this.displayName = displayName;
        this.folderHref = folderHref;
        this.title = title;
        this.description = description;
        this.style = style;
        this.logo = logo;
        this.excludeList = excludeList;
        this.folderEntries = folderEntries;
    }

    public String getFolderHref() {
        return folderHref;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getExcludeList() {
        return excludeList;
    }

    public List<Entry> getFolderEntries() {
        return folderEntries;
    }

    public String getStyle() {
        return style;
    }

    public String getLogo() {
        return logo;
    }

    public String getDisplayName() {
        return displayName;
    }
}
