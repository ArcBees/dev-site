var backTopArrowAppear = 200;

$(function () {
    $("body")
        .on("mouseenter", "> #nav", function () {
            $(this).removeClass("closed");
        })
        .on("mouseleave", "> #nav", function () {
            $(this).addClass("closed");
        });

    // Arrow back to top in the footer
    $("#backToTop").hide();

    $(function () {
        $(window).scroll(function () {
            if ($(this).scrollTop() > backTopArrowAppear) {
                $('#backToTop').fadeIn('slow');
            } else {
                $('#backToTop').fadeOut('slow');
            }
        });
        $('#backToTop').click(function () {
            $("html, body").animate({ scrollTop: 0 }, 'slow');
            return false;
        });
    });

    // Product hover on home page
    $('.productsCarousel a').on('mouseenter', function () {
        $(".productsCarousel a").removeClass("active");
        $(this).addClass("active");
    });

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
