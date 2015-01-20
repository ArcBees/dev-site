**A complete Model-View-Presenter framework to simplify your next GWT project.**

##Reference
* [GWTP Custom Google Search](http://www.google.com/cse/home?cx=011138278718949652927:5yuja8xc600) - Search GWTP documentation, wiki and thread collections.
* [GWTP Documentation](https://github.com/arcbees/gwtp/wiki) - Find out how to use GWT-Platform here.
* [GWTP Samples](https://github.com/ArcBees/GWTP-Samples) - Find Sample GWT-Platform projects here.
* [GWTP Archetypes](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes) - Start a project from a template here.

##Community
* [Join the GWT-Platform G+ Community](https://plus.google.com/communities/113139554133824081251) - See whats happening in the community.
* [GWTP Google Group](https://groups.google.com/forum/?fromgroups#!forum/gwt-platform) - Ask for help here.

##External contributions related to GWTP
https://github.com/growbit

##News
* 08/25/2014 - GWTP 1.3.1 was released
* 08/04/2014 - GWTP 1.3 was released
* 03/06/2014 - GWTP 1.2.1 was released
* 01/21/2014 - GWTP 1.1.1 was released
* 12/17/2013 - GWTP 1.1 was released
* 11/16/2013 - GWTP 1.0.3 was released
* 10/21/2013 - GWTP 1.0.2 was released
* 08/12/2013 - GWTP 1.0.1 was released
* 06/11/2013 - GWTP 1.0 was released
Get it from the download section or Maven central. See the Release Notes and the migration details.

##Downloads
[Maven Configuration](/resources/index.html)

[Build server](http://teamcity.arcbees.com/)

##Plugins
[Eclipse Plugin source](https://github.com/ArcBees/gwtp-eclipse-plugin) - Un-official

##Why Use GWTP
Programming web apps in GWT looks easy at first. However, if you just jump in and start coding, building a truly efficient and scalable app proves to be a lot harder. Your best bet is to design your application for scalability from the start, based on a proven architecture that leverages GWT's best features.

**GWTP** (we pronounce it "goo-teepee"), is a collection of components for this kind of architecture.
You can pick and choose the components you need as you work, or build your new project from the ground up using the entire package. No matter which approach you choose, GWT-optimized compilation will make sure only the features you really use are part of your final code. Read on for more details or get started right away!

At the heart of **GWTP** is a model-view-presenter architecture (MVP). Although this model [has been lauded as one of the best approaches to GWT development](http://code.google.com/events/io/2009/sessions/GoogleWebToolkitBestPractices.html), it is still hard to find an out-of-the-box MVP solution that supports all the requirements of modern web apps. **GWTP** aims to provide such a solution.

For example, adding history management and code splitting to your presenter is as simple as adding these lines
to your class:

```
  @ProxyCodeSplit
  @NameToken("myToken")
  public interface MyProxy extends ProxyPlace<MyPresenter> {}
```

##Goals
The goal of **GWTP** is: *to offer an easy-to-use MVP architecture with minimal boilerplate, without compromising GWT's best features*. Here are some of the features currently supported by **GWTP**:

* Dependency injection through GIN and Guice;
* Simple but powerful history management mechanism;
* Support for nested presenters;
* Lazy instantiation for presenter and view;
* Effortless and efficient code splitting;
* Integrated command pattern supporting undo/redo;
* Plus many other cool Planned Features, coming soon!

##Features
**GWTP** strives to use the event bus in a clear and efficient way. Events are used to decouple loosely related objects, while program flow between strongly coupled components is kept clear using direct method invocations. The result is an application that is easy to understand, and can be scaled up over time.

In addition, **GWTP** offers components that let you:

* Efficiently implement a Command pattern in your application ;
* Organize your internationalization strings ;
* Use annotation processors to generate event, actions & responses, and DTOs ;
* Easily support search-engine crawling on your GWT application (in development);
* Simplify and clean-up testing code when using GIN and Guice.
* To learn more about these components, check out the LibraryOverview.

##Notes
See the [GettingStarted](https://github.com/ArcBees/GWTP/wiki/Getting-started) page for details. You can also get plenty of support from developers and fellow users
in the [Forum](https://groups.google.com/forum/?fromgroups#!forum/gwt-platform), cheer for us on [ohloh](https://www.ohloh.net/p/gwt-platform), or follow us on [Google+](https://plus.google.com/communities/113139554133824081251)!

**GWTP** is a fork of gwt-dispatch and gwt-presenter, many thanks to the original authors of these packages. If you're used to gwt-presenter, you might like to see how it compares to **GWTP**.

**GWTP** is actively used in various projects, including the open source PuzzleBazar and large-scale commercial products. If you like this project and would like to contribute, send an email to philippe.beaudoin@gmail.com or christian.goudreau@arcbees.com. You can also take a look at good starting issues for new contributors. In all cases, join the discussion.

##Moved From
We have moved GWTP from [here](http://gwt-platform.googlecode.com).
