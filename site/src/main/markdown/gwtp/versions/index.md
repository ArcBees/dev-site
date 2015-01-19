#Version 1.3.1 (08/25/2014)
## GWTP
* Optimized version checking

## MVP
* [#497](https://github.com/ArcBees/GWTP/issues/497) Fixed PopupPresenter setCloseHandler
* [#542](https://github.com/ArcBees/GWTP/issues/542) Added IE 11 mobile user agent to formfactor tests

## REST Dispatcher
* [#545](https://github.com/ArcBees/GWTP/issues/545) Null parameters are now removed from the payload

#Version 1.3 (08/04/2014)
##GWTP
* Improved Javadoc site
* [#14](https://github.com/ArcBees/GWTP/issues/14) : ActionException & ServiceException now implement IsSerializable
* [#282](https://github.com/ArcBees/GWTP/issues/282) : LazyActionHandlerValidatorRegistryImpl is now threadsafe
* [#467](https://github.com/ArcBees/GWTP/issues/467) : Allow REST and RPC dispatchers to be used at the same time
* [#517](https://github.com/ArcBees/GWTP/issues/517) : Updated to GWT 2.6.1 and updated DTDs (Thanks [rdwallis](https://github.com/rdwallis))

## MVP
* Improved form-factor detection (Thanks [rdwallis](https://github.com/rdwallis))
* [#184](https://github.com/ArcBees/GWTP/issues/184) : DefaultModule now has the Builder pattern
* [#284](https://github.com/ArcBees/GWTP/issues/284) : Add toString() to PlaceRequest
* [#346](https://github.com/ArcBees/GWTP/issues/346) : Map more than one name token to presenter
* [#489](https://github.com/ArcBees/GWTP/issues/489) : Added `.without()` to PlaceRequest.Builder to remove parameter from PlaceRequest
* [#492](https://github.com/ArcBees/GWTP/issues/492) : PlaceRequest.Builder(PlaceRequest request) now creates a deep copy
* [#499](https://github.com/ArcBees/GWTP/issues/499) : Decode embedded paramaters of RouteTokenFormatter (Thanks [rdwallis](https://github.com/rdwallis))

## RPC
* [#484](https://github.com/ArcBees/GWTP/issues/484) : Deprecated HttpSessionSecurityCookieFilter (Thanks [bradcupit ](https://github.com/bradcupit))

## REST Dispatcher
* Updated [gwt-jackson](https://github.com/nmorel/gwt-jackson) to 0.6.1
* [#468](https://github.com/ArcBees/GWTP/issues/468) : Generate REST services based on the presence of @Path. Deprecate RestService interface
* [#498](https://github.com/ArcBees/GWTP/issues/498) : De/Serializing null/empty should result to null

#Version 1.2.1 (03/05/2014)
## GWTP
* Display a warning at compilation if there is a newer version of GWTP available

## MVP
* **This is not backward compatible.**. `PlaceRequest`, `TokenFormatter`, `ParameterTokenFormatter` and `RouteTokenFormatter` have moved from `com.gwtplatform.mvp.client.proxy` to `com.gwtplatform.mvp.shared.proxy`.
* PresenterWidget methods are no longer final, making it easier to test
* Allow "#!" as delimiter for crawlable route-like place tokens.
* Fixed a bug in encoding of query parameters in route-like place tokens.
* Fixed Analytics' logged URL when using.
* Enabled Analytics in Dev Mode.

## REST Dispatcher
* Added a `@NoXsrfHeader` annotation to disable CSRF protection on specific calls.
* Added a configuration method to the Gin Module Builder to change the requests timeout.
* Added a configuration methods to the Gin Module Builder to automatically add parameters.
* Added a configuration method to the Gin Module Builder to set a date format for Query, Form and Header parameters.
    * Parameters can be configured for specific HTTP methods (POST, GET, etc.)
* Added a `@DateFormat` annotation to set the date format of specific fields in your service interface.
* Fixed an issue with composition of service interface.

#Version 1.1.1 (01/21/2014)
## REST Dispatcher
- Fix mapper provider imports when there are no mappers (https://github.com/ArcBees/GWTP/pull/393)
- Added support for @Consumes and absolute REST url (https://github.com/ArcBees/GWTP/pull/396)
- Updated [gwt-jackson](https://github.com/nmorel/gwt-jackson) to 0.4.0

#Version 1.1 (12/17/2013)
## REST Dispatcher
**gwtp-dispatch-rest**
- Replaced Piriti by [gwt-jackson](https://github.com/nmorel/gwt-jackson)
- Removed missing jax-rs annotation from the compile report
- Added extension points and interfaces
- Moved some code from the `RestDispatch` implementation to `RestDispatchCall`

**gwtp-dispatch-client**
- Renamed artifact to *gwtp-dispatch-rpc-client*
- Moved classes to `com.gwtplatform.dispatch.rpc.client.*`
- The old classes are still there but are deprecated. If you wish to stay up to date and prevent future migration issues, make sure you use the classes available in `com.gwtplatform.dispatch.rpc.client.*`
- Added extension points and interfaces
- Moved some code from the `DispatchAsync` implementation to `RpcDispatchExecuteCall` and `RpcDispatchUndoCall`. They have the same extensions points `DispatchAsync` previously had

**gwtp-dispatch-server**
- Renamed artifact to *gwtp-dispatch-rpc-server*
- Moved classes to `com.gwtplatform.dispatch.rpc.server.*`
- The old classes are still there but are deprecated. If you wish to stay up to date and prevent future migration issues, make sure you use the classes available in `com.gwtplatform.dispatch.rpc.server.*`

**gwtp-dispatch-server-guice**
- Renamed artifact to *gwtp-dispatch-rpc-server-guice*
- Moved classes to `com.gwtplatform.dispatch.rpc.server.guice.*`
- The old classes are still there but are deprecated. If you wish to stay up to date and prevent future migration issues, make sure you use the classes available in `com.gwtplatform.dispatch.rpc.server.guice.*`

**gwtp-dispatch-server-spring**
- Renamed artifact to *gwtp-dispatch-rpc-server-spring*
- Moved classes to `com.gwtplatform.dispatch.rpc.server.spring.*`
- The old classes are still there but are deprecated. If you wish to stay up to date and prevent future migration issues, make sure you use the classes available in `com.gwtplatform.dispatch.rpc.server.spring.*`

**gwtp-dispatch-shared**
- Renamed artifact to *gwtp-dispatch-rpc-shared*
- Moved classes to `com.gwtplatform.dispatch.rpc.shared.*`
- The old classes are still there but are deprecated. If you wish to stay up to date and prevent future migration issues, make sure you use the classes available in `com.gwtplatform.dispatch.rpc.shared.*`

**Additions**
- Created 2 new artifacts *gwtp-dispatch-common-shared* and *gwtp-dispatch-common-client*. They contain code (ie: `DispatchCall` abstraction) shared by both the REST and RPC dispatch projects.

#Version 1.0.3 (11/15/2013)
 * Fixed regression caused by https://github.com/ArcBees/GWTP/issues/312 - https://github.com/ArcBees/GWTP/issues/340

#Version 1.0.2 (10/21/2013)
 * Fixed various issues in Crawler Service - https://github.com/ArcBees/GWTP/issues/326
 * Removed illegal call to Throwable#initCause - https://github.com/ArcBees/GWTP/issues/312

#Version 1.0.1 (8/14/2013)
 * Extended PlaceRequest builder to be able to build a PlaceRequest from an already existing place request - https://github.com/ArcBees/GWTP/issues/300
 * Fixed doCenter method that wasn't centering correctly the popup when the popup is hidden - https://github.com/ArcBees/GWTP/pull/302
 * Fixed a NPE in Objectify for the crawler - https://github.com/ArcBees/GWTP/pull/299
 * Enhanced the crawler to use latest HTMLUnit version as well as a more recent browser as default - https://github.com/ArcBees/GWTP/pull/290
 * Fixed a problem with unlockScreen that was throwing a javascript exception - https://github.com/ArcBees/GWTP/pull/294
 * Added extension points to RootPresenter and getters - https://github.com/ArcBees/GWTP/pull/287
 * Fixed a bug with GWTP-Dispatch where CPU usage could go up to 100% - https://github.com/ArcBees/GWTP/pull/281
 * Fixed issue while using lists with GenProxyProcessor - https://github.com/ArcBees/GWTP/issues/201
 * Extracted crawler service from the core - https://github.com/ArcBees/GWTP/pull/277

Here's a list of changes for GWTP Spring:

 * We're now giving a default value for cookie name while using secure action - https://github.com/ArcBees/GWTP/pull/303
 * Updated to Spring 3.2.3 - https://github.com/ArcBees/GWTP/pull/298
 * GWTP-Dispatch handlers can now be registered using @RegisterActionHandler- https://github.com/ArcBees/GWTP/pull/292

#Version 1.0 (6/11/2013)
 * Added new [visibility handler](https://github.com/ArcBees/GWTP/wiki/Events#registerhandler-in-presenters)
 * Added an experimental API [[Rest-Dispatch]] 
 * Updated GinUiBinder to work with GWT 2.5+
 * Fixed https://github.com/ArcBees/GWTP/issues/252
 * Fixed https://github.com/ArcBees/GWTP/issues/243

#Version 1.0-RC-3 (4/13/2013)
 * Testing application carstore was added
 * More documentation has been added. 
 * IsWidget support. This is breaking in overriding classes.
 * [[Route-Place-Tokens]]
 * Unlock place manager when async calls fail fix.
 * @Bootstrap replaced with property module. Breaking Change. https://groups.google.com/forum/?fromgroups=#!topic/gwt-platform/UUAUpnvjxkE - [[Bootstrapping]]

#Version 1.0-RC-2 (2/21/2013)
 * Updated default rpc dispatch path in ActionImpl, this simplifies using rpc dispatch defaults.
 * Phonegap build support added.
 * FormFactors for Mobile, Tablet, Desktop added. 
 * Removed a bug where the compiler would wrongly say that DefaultClientModule wasn't found.
 * More docs have been added and updated. 
 * Mobile sample updated and added Phonegap support to i. 

#Version 1.0-RC-1 (1/30/2013)
 * Code formatting to 4 spaces.
 * Enhanced Bootstrapping.
 * More documentation added to wiki.
 * ViewImpl change, by adding widget to it. Allows for initWidget(widget...).
 * You can now use the DefaultPlaceManager instead of your own.
 * Default annotations added @DefaultPlace, @ErrorPlace, @UnauthorizedPlace.
 * [[Maven Configuration]] enhanced. 
 * Ginjector collision fixed.

#Version 0.8-beta-1,2
 * Started beta line as we're near an official release
 * Enhancements to the Ginjector generation
 * Annotated Bootstrapper
 * Annotated PreBootstrapper added
 * GWTP entry point added
 * Annotated Ginjector enhancements
 * Moved documentation from Google code repository
 * Remove Uber Jar. Dependency links have been made to Maven Central in ReadMe

#Version 0.8 Alpha 1,2,3 (01/08/2013)
 * Upgraded and compiled with GWT 2.5.0
 * You can now set your content slot in your presenter constructor and remove revealInParent method.
 * Annotated ginjector generation
 * Separated GWTP-Samples from core
 * Dispatch handler discovery
 * Moved source to Github
 * Release to sonatype for maven dependency integration
 * Removed UiBinder Ginjector injection to a further release
 * Created G+ GWTP Community
 * Favicon created

#Version 0.7 Beta (02/01/2012)
 * Support for dynamic tabbed presenters with user data
 * More robust token formatter, tokens now survive in email
 * Support for GWT 2.4
 * Migrated the event system to com.google.web.bindery
 * Improved error reporting during code generation and for dispatcher
 * Improved documentation
 * Many bug fixes to the eclipse plugin
 * Numerous bug fixes and enhancements. See the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:milestone-V1.0-7 complete list of closed issues], and the [PortingV1#V0.7 migration details].

#Version 0.6 Beta (06/06/2011)
 * Introduction of the [EclipsePlugin GWTP Eclipse Plugin].
 * Support for [CrawlerSupport search engine crawling].
 * Integration with [GoogleAnalytics Google Analytics].
 * Major refactoring of GWT generators, resulting in better error messages and more robustness. (See [http://code.google.com/p/gwt-platform/issues/detail?id=6 Issue 6].)
 * Fixed a bug with the !TokenFormatter. (See [http://code.google.com/p/gwt-platform/issues/detail?id=286 Issue 286].)
 * Fixed a bug with navigation confirmation mechanism. (See [http://code.google.com/p/gwt-platform/issues/detail?id=291 Issue 291].)
 * Made it possible to reveal a place without changing the URL token, so the browser _back_ button works when revealing an error place. (See [http://code.google.com/p/gwt-platform/issues/detail?id=273 Issue 274].)
 * More powerful notification system for internal asynchronous calls. (See [http://code.google.com/p/gwt-platform/issues/detail?id=308 Issue 308].)
 * Cleaned-up projects and Maven dependencies, resulting in a cleaner dependency graph and total separation of Guice and Spring dependent code.
 * Increased the quality of errors reported by annotation processors. (See [http://code.google.com/p/gwt-platform/issues/detail?id=318 Issue 318].)
 * Numerous bug fixes and enhancements. See the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:milestone-V1.0-6 complete list of closed issues], and the [PortingV1#V0.6 migration details].

#Version 0.5.1 Beta (03/11/2011)
 * Compatible with GWT 2.2, no longer compatible with GWT 2.1
 * Uses official GIN release 1.5.
 * See the [PortingV1#V0.5.1 migration details].

#Version 0.5 Beta (01/31/2011)
 * Full support for Spring on the backend for the dispatcher.
 * Dispatcher now supports client-side queuing, caching and handling of actions.
 * More flexible tabs with support for user-defined properties.
 * Various improvements to the annotation processors for automatic generation of events, actions and DTOs.
 * The project is now entirely built with Maven.
 * Separate jars for the different components of GWTP.
 * Numerous bug fixes and enhancements. See the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:milestone-V1.0-5 complete list of closed issues], and the [PortingV1#V0.5 migration details].

#Version 0.4 Beta (09/10/2010)
 * Automatic generation of boilerplate code via annotation processors. See the [http://code.google.com/p/gwt-platform/wiki/BoilerplateGeneration documentation] or the `hplacesample` for more information.
 * New manual reveal makes it possible to reveal pages only when the data they need has been fetched.
 * Dispatched actions can now be interrupted.
 * Major overhaul of the `Presenter` class hierarchy that gets rid of many problems when UnitTesting, improved naming convention.
 * The coding style is now clearly documented and the project uses checkstyle to enforce it.
 * The `HasEventBus` interface makes it possible to track the source of events when calling `fireEvent`.
 * Setup of a ContinuousIntegration server, and improved build process for releasing key versions and snapshots.
 * Other bug fixes and enhancements. See the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:milestone-V1.0-4 complete list of closed issues], and the [PortingV1#V0.4 migration details].

#Version 0.3 Beta (07/21/2010)
  * Renamed package from `com.philbeaudoin.gwtp` to `com.gwtplatform`
  * Support for hierarchical places and breadcrumbs (including a small sample app)
  * Support for registering custom events in the proxy using `@ProxyEvent`, taking care of most cases that required custom proxys
  * Support for centralized and simplified internationalization via the `mergelocales` script
  * Replaced `@PlaceInstance` by `@UseGatekeeper` with a type-safe syntax.
  * Support for individually removing `PresenterWidget` from their parent.
  * Other bug fixes and enhancements. See the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:milestone-V1.0-3 complete list of closed issues], and the [PortingV1#V0.3 migration details].

#Version 0.2 Beta (05/28/2010)
  * Major overhaul of the command pattern
    * Renamed packages and classes and simplified the hierarchy, see PortingV1 for migration details
    * Support for simple server-side action validation, see IntroductionActionValidator
    * Support for per-action dispatch URLs
    * Integrated and simple protection against XSRF attacks
    * Support for undo operations
  * Support for nested tab presenters
  * Support for MVP dialog boxes and popup panels
  * Sample applications for nested presenters, tab presenters, dialog boxes and popups
  * Support for binding the same View class to multiple presenters
  * GWTP is now published on a [http://code.google.com/p/gwt-platform/source/browse?repo=maven Maven repository]
  * Other bug fixes and enhancements. See the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:milestone-V1.0-2 complete list of closed issues] and the [PortingV1#V0.2 migration details].

#Version 0.1 Beta (04/16/2010)
  * ~~Stable interfaces, these shouldn't change for V1.0.~~ Changes will be documented in PortingV1.
  * Now using the standard Gin library (r137 with !AsyncProvider).
  * Supports lifecycle hook `PresenterWidgetImpl#onReset()`.
  * Presenter lifecycle hooks (`onReveal`, `onHide`, etc.) no longer exposed in the interface.
  * Released sample application available on `https://samples.gwt-platform.googlecode.com/hg/`.
  * Other bug fixes, see the [http://code.google.com/p/gwt-platform/issues/list?can=1&q=label:Milestone-V1.0-1 complete list of closed issues].

#Version 0.0.1 Alpha (04/01/2010)
  * First library available outside !PuzzleBazar.