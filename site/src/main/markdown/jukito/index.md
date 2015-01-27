#Overview
##Introduction
Jukito lets you use dependency injection in all your tests, be they unit tests, full integration tests, or anything in between. Its automocking power reduces boilerplate to a minimum, and you will quickly grow addicted to its nice annotation-based and typesafe syntax for parameterized tests.

You can get the latest Jukito release from the [Maven Central](https://code.google.com/p/jukito/wiki/MavenRepository) or from the [Releases page](https://github.com/ArcBees/Jukito/releases). For the latest build, fresh out of the oven, head over to our [continuous integration server](http://teamcity.codebetter.com/project.html?projectId=project103).

## Features
* DOC injection on test class fields and into test methods
* DOC and SUT reset before every test method call - new scope via `@TestSingleton` annotation
* installation of your custom Guice modules
* Environment Dependent Modules - one test, many environment configurations

## Configuration
* Put `@RunWith(JukitoRunner.class)` on test class
* Add inner class like  public static class A extends JukitoModule if you would like to install custom Guice modules or use `@UseModules` annotation

## Examples
* Ultra easy test - first shot - link
* `@UseModules` - link
* Installing custom modules - link
* Using methods factories to satisfy dependencies - link
