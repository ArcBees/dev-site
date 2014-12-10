var speed = 200;
var isSameOriginRexp = new RegExp("^(?!(#|[a-z#]+:))(?!.*(|/)javadoc/)(?!.*\\.(jpe?g|png|mpe?g|mp[34]|avi)$)", "i");

$(function () {
    $("body")
        .on("mouseenter", "> #nav:not(.alwaysOpen)", function () {
            $(this).removeClass("closed");
        })
        .on("mouseleave", "> #nav:not(.alwaysOpen)", function () {
            $(this).addClass("closed");
        });

    // FlexNav
    $(".flexnav").flexNav();

    $("#content").bind("DOMSubtreeModified", function () {
        // Homepage
        if ($('#content').hasClass('home')) {
            styleHome();
            $(window).resize(function () {
                styleHome();
            });

            // Pager
            $('.next, .pager a').click(function (e) {
                e.preventDefault();
                $('html, body').animate({
                    scrollTop: $($(this).attr('href')).offset().top
                }, 600);
            });

            $('.pager a').click(function (e) {
                e.preventDefault();
                $('.pager a').removeClass("active");
                $(this).addClass("active");
            });

            $(window).scroll(function () {
                $('.pager a').removeClass("active");
                if ($(window).scrollTop() + 100 > $('#letsbegin').offset().top) {
                    $('.pager a:nth-child(3)').addClass("active");
                } else if ($(window).scrollTop() + 100 > $('#gwt').offset().top) {
                    $('.pager a:nth-child(2)').addClass("active");
                } else {
                    $('.pager a:nth-child(1)').addClass("active");
                }
            });
        } else {
            $(window).unbind();
        }
    });

    $("#content").trigger("DOMSubtreeModified");
});

function styleHome() {
    var wh = $(window).height();
    var sh = $("#letsbegin").height();

    if (wh > sh) {
        $(".home section").each(function () {
            $(this).css({
                height: wh,
                padding: 0
            });
            var c = $(this).find(".container");
            c.css("padding-top", (wh - c.height()) / 2);
        });
    }
}

function handleMenu() {
    // Replace relative paths in anchors by absolute ones
    // exclude all anchors in the content area.
    $("a").not($("#content a")).each(function () {
        var link = $(this);
        if (shouldEnhanceLink(link)) {
            enhanceLink(link);
        }
    });

    var submenu = $("#submenu");
    var item = submenu.find("a[href='" + window.location.pathname + "']").first();

    submenu.find("li ul").hide();

    // Only collapse unrelated entries in mobile
    if ($("#nav-mobile").is(':visible')) {
        hideUnrelatedBranches(item);
    }

    showBranch(item);

    submenu.find("a.selected").removeClass("selected");
    item.addClass("selected");

    // Replace relative paths in anchors by absolute ones
    // exclude all anchors in the content area.
    $("a").not($("#content a")).each(function () {
        var link = $(this);
        if (shouldEnhanceLink(link)) {
            enhanceLink(link);
        }
    });
}

function enhanceLink(link) {
    // No need to make complicated things for computing
    // the absolute path: anchor.pathname is the way
    var pathname = link.prop("pathname");
    var hash = link.prop("hash");
    link.attr("href", pathname + (hash ? hash : ""));
}

function shouldEnhanceLink(link) {
    // Enhance only local links
    return isSameOriginRexp.test(link.attr("href")) &&
            // Do not load links that are marked as full page reload
        !link.attr("data-full-load");
}

function hideUnrelatedBranches(item) {
    $("#submenu").find("li.open")
        .not(item).not(item.parents())
        .removeClass("open")
        .children("ul")
        .slideUp(0);
}

function showBranch(item) {
    item.parents("li")
        .addClass("open")
        .children("ul")
        .slideDown(0);
}

handleMenu();
