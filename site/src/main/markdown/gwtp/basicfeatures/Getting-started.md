# Getting Started

The goal of this page is to help you get your first project started using GWTP's Model-View-Presenter architecture. Although it focuses on how to use GWTP in Eclipse, it should be easy to adapt within your own development environment.

After reading this guide you should have a better idea whether or not GWTP is the tool you need for your next project. If you used gwt-presenter before, you might first want to take a look at how it [compares to GWTP](http://code.google.com/p/gwt-platform/wiki/ComparisonWithGwtPresenter).

For more information about the Model-View-Presenter architecture, check out Ray Ryan's [Google IO talk](https://www.youtube.com/watch?v=PDuhR18-EdM).

## Starting with the GWTP Eclipse Plugin

The rest of this document describes how to create a GWTP project manually step-by-step and will give you a deep knowledge of GWTP's fundamental principles. To get started quickly however, head over to the [GWTP Eclipse Plugin](https://github.com/ArcBees/gwtp-eclipse-plugin/wiki) and follow the screencasts presented there.

## Getting the sample applications

A good way to learn advanced GWTP features is to look at the included sample applications. Getting and compiling
them, either from the command line or from Eclipse, is described in details in [Running GWTP samples][rs]. These
samples are partly meant to test GWTP so they are a bit hard to setup to run comfortably within Eclipse and their configuration files are a bit complex. Your first GWTP projects can be much simpler, and this is what this page guides you though.

## External examples

You can take a look to this great enterprise level project named [Kiahu CX](http://kiahu.com/) done using GWTP. You can have the [source code](https://code.google.com/p/gwt-cx/) and can read the [blog](http://uptick.com.au/blog).

You can also take a look at this awesome time management application named [Karaka](https://github.com/hpehl/karaka).

Another example of web-page-like applications built with GWTP is [ArcBees website](https://github.com/ArcBees/arcbees-website), [GWTP website](https://github.com/ArcBees/gwtp), [Jukito website](https://github.com/ArcBees/jukito) and [GAE-Studio website](https://github.com/ArcBees/gae-studio).

[Back to top](#top)

# Set-up

This section explains how to setup your first GWTP project within Eclipse. If you're only interested in browsing the sample source code, [http://code.google.com/p/gwt-platform/wiki/GettingStarted#Using_GWTP skip right to the next section] where we take you on a step-by-step journey through the basic features of GWTP.

## Creating a GWTP project
Find the [Project Creation][pc] notes for both IntelliJ IDEA and Eclipse [here](https://github
.com/ArcBees/GWTP/wiki/Project-Creation).

## Getting GWTP
You can get the current release of GWTP from the [download section](https://github
.com/ArcBees/GWTP/tree/master/distribution/downloads), the latest snapshot is available on our [continuous
integration server](http://teamcity.codebetter.com/viewType.html?buildTypeId=bt225&tab=buildTypeStatusDiv), simply
create an account and get it from the artifact column. You can also get the latest release or snapshot with
[Maven Configuration][mc],  or you can elect to [clone out git repository](https://github.com/ArcBees/GWTP).

## Required libraries
Any GWTP project depends on a couple of libraries. The rest of this section lists these and explains how to obtain
them. If your project is using Maven then all the dependencies will be taken care of automatically. See [Maven Configuration][mc] for more details. For the complete dependency hierarchy take a look at DependencyHierarchy.

### Gin and Guice

GWTP makes heavy use of dependency injection through [google-guice](http://code.google.com/p/google-guice/) and [google-gin](http://code.google.com/p/google-gin/). You will therefore need to add these libraries to your project.

Add the jars to your build path by doing the following:
* First, copy these jars to the `war/WEB-INF/lib` directory of your project ;
* Then, from Eclipse, right-click on each of these files and select `Build Path > Add to Build Path`.

### GWTP

Naturally, you will need GWTP itself. Simply copy GWTP's compound jar `gwtp-all-1.0.jar` into your project's `war/WEB-INF/lib` directory and add it to your build path, as explained above.

If you want to minimize the size of your jars it's possible to cherry-pick the components you want from GWTP. See the
 [Overview of the library][ol] for details. This comes in handy when using AppEngine, to keep cold start-up time to a
 minimum.

[Back to top](#top)

# Using GWTP

At this point, your application should compile without build errors and you should be able to run it. It's time to start writing code!

## Your first View
To create a the first view visit [Presenter View Creation][pv].

## Your first Presenter
See more on [Layout Presenter][lpr]

## Your first Proxy
See more on [Presenter-Proxy][pproxy]

## Default page
See more on [place manager](PlaceManager)

## Binding everything together
See more on [GIN Binding][gb]

## Setting the entry point
See more on [bootstrapping](Bootstrapping-or-Application-Initialization)

## Configuring your GWT module
See more on [bootstrapping](Bootstrapping-or-Application-Initialization)

[Back to top](#top)

# Advanced features

This section presents a number of advanced features of GWTP. You will need many of these when building a complete GWTP application.

## Presenter lifecycle
[Presenter Lifecycle][pl]

## Using URL parameters
[URL Parameters][up]

## Revealing a presenter
See more on [Presenter Lifecycle][pl]

## Nested presenters
See more on [Presenter "Slots"][ps]

## Tabbed presenters
See more on [Tabbed Presenters][tp]

## Presenter widgets
See more on [Presenter Widget][pw]

## Dialog box and popup panels
See more on [Popup Presenter][pp]

## Navigation confirmation
See more on [Navigation Confirmation][nc]

## Blocking some presenters
See more on [Presenter Gatekeeper][pg]

## Using layout panels
See more on [Layout Panels][lp]

## Delegate some actions from the view to the presenter
See how to use [UiHandlers][uh]

## Why Proxies?

Proxies are light-weight classes whose size doesn't depend on the complexity of the underlying presenter and view. They are instantiated as soon as the application loads and are responsible for listening to any event that would require their associated presenter and view to be created. Proxies are the key to a fast MVP web application, they enable code splitting and lazy instantiation of the largest part of your code.

## Provider bundles

Provider bundles are an advanced way to optimize code splitting in your application. It is used by PuzzleBazar in `AdminTabPresenter` and `UserSettingsTabPresenter`.

## Automatic boilerplate generation

GWTP offers some annotation processors to reduce the burden of creating simple classes, like events. Check out the BoilerplateGeneration page for more details.

[rs]: gwtp/Running-GWTP-samples.html
[pc]: gwtp/basicfeatures/ "Project Creation"
[mc]: gwtp/resources/index.html "Maven Configuration"
[pl]: gwtp/features/Presenter-Lifecycle.html "Presenter Lifecycle"
[up]: gwtp/features/URL-Parameters.html "URL Parameters"
[ps]: gwtp/features/Presenter-Slots.html "Presenter Slots"
[tp]: gwtp/features/Tabbed-Presenters.html "Tabbed Presenters"
[pw]: gwtp/features/Presenter-Widget.html "Presenter Widget"
[pp]: gwtp/features/Popup-Presenter.html "Popup Presenter"
[nc]: gwtp/features/Navigation-Confirmation.html "Navigation Confirmation"
[pg]: gwtp/features/Presenter-Gatekeeper.html "Presenter Gatekeeper"
[lp]: gwtp/otherusefulinformation/Layout-Panels.html "Layout Panels"
[uh]: gwtp/features/UiHandlers.html "UiHandlers"
[ol]: gwtp/about/ "Overview of the library"
[pv]: gwtp/features/ "Presenter View Creation"
[lpr]: gwtp/features/Layout-Presenter.html "Layout Presenter"
[pproxy]: gwtp/features/Presenter-Proxy.html "Presenter Proxy"
[gb]: gwtp/otherusefulinformation/GIN-Binding.html "GIN Binding"
