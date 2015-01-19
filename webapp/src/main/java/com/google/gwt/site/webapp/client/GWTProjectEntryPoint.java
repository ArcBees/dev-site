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

import com.arcbees.analytics.client.ClientAnalyticsFactory;
import com.arcbees.analytics.shared.Analytics;
import com.google.common.base.MoreObjects;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.query.client.Properties;
import com.google.gwt.query.client.js.JsUtils;
import com.google.gwt.query.client.plugins.ajax.Ajax;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.site.demo.ContentLoadedEvent;
import com.google.gwt.site.demo.gsss.grid.GSSSGridDemos;
import com.google.gwt.site.demo.gsss.mixins.GSSSMixinsDemos;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.impl.HyperlinkImpl;
import com.google.web.bindery.event.shared.EventBus;
import com.google.web.bindery.event.shared.SimpleEventBus;

import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.body;
import static com.google.gwt.query.client.GQuery.window;

public class GWTProjectEntryPoint implements EntryPoint {

    private static final int ANIMATION_TIME = 200;

    private static final HyperlinkImpl clickHelper = GWT.create(HyperlinkImpl.class);

    private static Properties history = JsUtils.prop(window, "history");

    // Visible for testing
    // The absolute path to the url root (http://gwtproject.com)
    static String origin = GWT.getModuleBaseForStaticFiles()
            .replaceFirst("^(\\w+://.+?)/.*", "$1").toLowerCase();

    // Visible for testing
    // We discard links with different origin, hashes, starting with protocol, javadocs, and media links.
    static final RegExp isSameOriginRexp = RegExp.compile("^" + origin
            + "|^(?!(#|[a-z#]+:))(?!.*(|/)javadoc/)(?!.*\\.(jpe?g|png|mpe?g|mp[34]|avi)$)", "i");

    private static boolean isPushstateCapable = history.get("pushState") != null;
    private static boolean ajaxEnabled = isPushstateCapable && origin.startsWith("http");
    private static String currentPage = Window.Location.getPath();
    private static EventBus eventBus = new SimpleEventBus();

    private final RegExp titleTagMatcher = RegExp.compile("<title>(.*?)</title>");

    private Analytics analytics;

    @Override
    public void onModuleLoad() {
        createAnalytics();

        registerDemos();

        enhancePage();

        History.addValueChangeHandler(new ValueChangeHandler<String>() {
            @Override
            public void onValueChange(ValueChangeEvent<String> event) {
                openMenu();
            }
        });
    }

    private void createAnalytics() {
        if (shouldTrackAnalytics()) {
            ClientAnalyticsFactory analyticsFactory = new ClientAnalyticsFactory();
            analytics = analyticsFactory.create("UA-41550930-12", false);
            analytics.create()
                    .cookieDomain("arcbees.com")
                    .allowLinkerParameters(true)
                    .go();

            trackPageView(Window.Location.getPath());
        }
    }

    private void registerDemos() {
        new GSSSGridDemos(eventBus);
        new GSSSMixinsDemos(eventBus);

        ContentLoadedEvent.fire(eventBus);
    }

    /*
     * Open the branch and select the item corresponding to the current url.
     */
    private void openMenu() {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                String relativeLocation = Window.Location.getPath() + Window.Location.getHash();
                GQuery item = $("#submenu a[href$=\"" + relativeLocation + "\"]").eq(0);

                hideUnrelatedBranches(item);

                showBranch(item);

                $("#submenu a.selected").removeClass("selected");
                item.addClass("selected");

                // Replace relative paths in anchors by absolute ones
                // exclude all anchors in the content area.
                $("a").not($("#content a")).each(new Function() {
                    @Override
                    public void f(Element e) {
                        GQuery link = $(e);
                        if (shouldEnhanceLink(link)) {
                            enhanceLink(link);
                        }
                    }
                });
            }
        });
    }

    /*
     * Enhance the page adding handlers and replacing relative by absolute urls
     */
    private void enhancePage() {
        enhanceMenu();

        // Replace relative paths in anchors by absolute ones
        // exclude all anchors in the content area.
        $("a").not($("#content a")).each(new Function() {
            @Override
            public void f(Element e) {
                GQuery link = $(e);
                if (shouldEnhanceLink(link)) {
                    enhanceLink(link);
                }
            }
        });

        // In mobile have a link for opening/closing the menu
        $("#nav-mobile").on("click", new Function() {
            @Override
            public void f() {
                $("#submenu").toggleClass("show");
            }
        });

        // Do not continue enhancing if Ajax is disabled
        if (!ajaxEnabled) {
            // Select current item from the URL info
            loadPage(null, true);
            return;
        }

        // Use Ajax instead of default behaviour
        $(body).on("click", "a:not([href^=\"http\"])", new Function() {
            @Override
            public boolean f(Event e) {
                GQuery $e = $(e);
                String href = $e.attr("href");
                boolean containsHash = href.contains("#");

                if (shouldEnhanceLink($e) &&
                        // Is it a normal click (not ctrl/cmd/shift/right/middle click) ?
                        clickHelper.handleAsClick(e)) {
                    if (!containsHash) {
                        // In mobile, if menu is visible, close it
                        $("#submenu.show").removeClass("show");

                        // Load the page using Ajax
                        loadPage(href, !$e.parents("#nav").isEmpty());
                        return false;
                    } else {
                        openMenu();
                    }
                }

                return true;
            }
        });

        // Select the TOC item when URL changes
        $(window).on("popstate", new Function() {
            @Override
            public void f() {
                loadPage(null, true);
            }
        });
    }

    private void enhanceMenu() {
        // We add a span with the +/- icon so as the click area is well defined
        // this span is not rendered in server side because it is only needed
        // for enhancing the page.
        GQuery parentItems = $("#submenu li").has("ul").prepend("<span/>");

        // Toggle the branch when clicking on the arrow or anchor without content
        $(parentItems).children("span, a[href=\"#\"]").on("click", new Function() {
            @Override
            public boolean f(Event e) {
                toggleMenu($(e).parent());
                return false;
            }
        });

        $("#submenu li")
                .not(".open")
                .children("ul")
                .slideUp(0);
    }

    private void enhanceLink(GQuery link) {
        // No need to make complicated things for computing
        // the absolute path: anchor.pathname is the way
        Object pathname = link.prop("pathname");
        Object hash = link.prop("hash");
        link.attr("href", String.valueOf(pathname) + MoreObjects.firstNonNull(hash, ""));
    }

    private boolean shouldEnhanceLink(GQuery link) {
        return
                // Enhance only local links
                isSameOriginRexp.test(link.attr("href")) &&
                        // Do not load links that are marked as full page reload
                        !Boolean.parseBoolean(link.attr("data-full-load"));
    }

    private void toggleMenu(GQuery menu) {
        menu.toggleClass("open")
                .children("ul")
                .slideToggle(ANIMATION_TIME);
    }

    private void hideUnrelatedBranches(GQuery item) {
        $("#submenu li.open")
                .not(item).not(item.parents())
                .removeClass("open")
                .children("ul")
                .slideUp(ANIMATION_TIME);
    }

    private void showBranch(GQuery item) {
        item.parents()
                .filter("li")
                .addClass("open")
                .children("ul")
                .slideDown(ajaxEnabled ? ANIMATION_TIME : 0);
    }

    /*
     * Change URL via pushState and load the page via Ajax.
     */
    private void loadPage(String pageUrl, boolean replaceMenu) {
        if (!currentPage.equals(pageUrl)) {
            if (pageUrl != null) {
                // Preserve QueryString, useful for the gwt.codesvr parameter in dev-mode.
                pageUrl = pageUrl.replaceFirst("(#.*|)$", Window.Location.getQueryString() + "$1");
                // Set the page to load in the URL
                JsUtils.runJavascriptFunction(history, "pushState", null, null, pageUrl);
            }

            pageUrl = Window.Location.getPath();
            if (!currentPage.equals(pageUrl)) {
                ajaxLoad(pageUrl, replaceMenu);
            } else {
                scrollToHash();
            }

            currentPage = pageUrl;
        }
    }

    private void ajaxLoad(final String url, final boolean replaceMenu) {
        Ajax.Settings settings = Ajax.createSettings();
        settings.setUrl(url);
        settings.setDataType("html");
        settings.setType("get");
        settings.setSuccess(new Function() {
            @Override
            public void f() {
                GQuery content = $("<div>" + getArgument(0) + "</div>");

                String pageStyle = content.find("#holder").attr("class");
                $("#holder").attr("class", pageStyle);

                if (replaceMenu) {
                    $("#holder #submenu").replaceWith($(content).find("#holder #submenu"));
                    enhanceMenu();
                }

                $("#content").empty().append(content.find("#content > div"));

                $("meta").remove();
                $("head").append(content.find("meta"));

                $("head title").replaceWith(content.find("title"));

                onPageLoaded(url);
            }
        });

        Ajax.ajax(settings);
    }

    private void onPageLoaded(String pageUrl) {
        if (shouldTrackAnalytics()) {
            trackPageView(pageUrl);
        }

        ContentLoadedEvent.fire(eventBus);
        openMenu();
        scrollToHash();

        updateMenusForPage(pageUrl);
    }

    private void updateMenusForPage(final String pageUrl) {
        if ("/".equals(pageUrl) || "".equals(pageUrl)) {
            lockMenus();
        } else {
            unlockMenus();
        }
    }

    private void unlockMenus() {
        $("#holder").removeClass("home");
    }

    private void lockMenus() {
        $("#holder").addClass("home");
    }

    /*
     * Move the scroll to the hash fragment in the URL
     */
    private void scrollToHash() {
        String hash = Window.Location.getHash();
        GQuery anchor = hash.length() > 1 ? $(hash + ", [name=\"" + hash.substring(1) + "\"]") : $();
        if (anchor.isEmpty()) {
            Window.scrollTo(0, 0);
        } else {
            anchor.scrollIntoView();
        }
    }

    private void trackPageView(String pageUrl) {
        analytics.sendPageView().documentPath(pageUrl).go();
    }

    private boolean shouldTrackAnalytics() {
        String host = Window.Location.getHost();

        return host.endsWith(".arcbees.com");
    }
}
