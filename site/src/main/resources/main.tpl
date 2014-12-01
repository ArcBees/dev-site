<!DOCTYPE html>
<html>
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
        <meta name="viewport" content="width=device-width, initial-scale=1"/>

        <title>GWT Project</title>

        <link rel="stylesheet" href="assets/build/css/style.css">
        <link rel="stylesheet" href="assets/build/fonts/icons/icons.css">
        <link rel="stylesheet" href="assets/build/fonts/geometria/geometria.css">
        <link rel="shortcut icon" type="image/x-icon" href="/images/favicon.png">

        <script type="text/javascript" src="/gwtproject/gwtproject.nocache.js"></script>
        <script type="text/javascript">
            var _gaq = _gaq || [];
            _gaq.push(['_setAccount', 'UA-40673139-1']);
            _gaq.push(['_setDomainName', 'gwtproject.org']);
            _gaq.push(['_setAllowLinker', true]);
            _gaq.push(['_trackPageview']);

            (function () {
                var ga = document.createElement('script');
                ga.type = 'text/javascript';
                ga.async = true;
                ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(ga, s);
            })();

        </script>
        <script>
            (function () {
                var cx = '008698606704604874043:or32ypbnrfu';
                var gcse = document.createElement('script');
                gcse.type = 'text/javascript';
                gcse.async = true;
                gcse.src = (document.location.protocol == 'https:' ? 'https:' : 'http:') +
                        '//www.google.com/cse/cse.js?cx=' + cx;
                var s = document.getElementsByTagName('script')[0];
                s.parentNode.insertBefore(gcse, s);
            })();
        </script>
    </head>

    <body class="jukito">

        <!--[if lt IE 7]>
            <p class="browsehappy">You are using an <strong>outdated</strong> browser. Please <a href="http://browsehappy.com/">upgrade your browser</a> to improve your experience.</p>
        <![endif]-->

        <nav role="navigation" id="nav" class="closed">
            <div class="wrapper">
                <ul>
                    <li class="logo"><a href="/">GWT homepage</a></li>
                    <li><a href="jukito/Home.html"><i class="icon_menu"></i>Jukito</a></li>
                </ul>
            </div>
        </nav>

        <div class="holder">
            <aside id="submenu">
                <div class="logo">

                </div>
                <nav>
                    $toc
                </nav>
            </aside>

            <a id="logo-mobile" href="#"><span>>Back to GWT homepage</span></a>

            <div class="menu-button">Menu</div>

            <nav id="nav-mobile" class="flexnav">
                $toc
            </nav>

            <div id="search">
                <form action="#">
                    <input type="text"/>
                    <button type="submit"><i class="icon_search"></i> <span>Search</span></button>
                </form>
            </div>

            <div id="content" role="main">
                <div class="container">

                    $content

                </div>
            </div>
        </div>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
        <script>window.jQuery || document.write('<script src="assets/build/js/vendor/jquery-1.11.0.min.js"><\/script>')</script>

        <script src="/assets/build/js/vendor/jquery.flexnav.js"></script>
        <script src="/assets/build/js/global.min.js"></script>

    </body>
</html>
