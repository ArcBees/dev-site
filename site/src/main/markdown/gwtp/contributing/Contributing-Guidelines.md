# Contributing Guidelines
If you are interested in understanding the internals of GWT-Platform (GWTP), building from source, or contributing ideas or modifications to the project, then this document is for you.

#Licensing
All GWTP source and pre-built binaries are provided under the Apache 2.0 license.

#The GWTP Community

The GWTP community exists primarily through mailing lists, the issue tracker and, to a lesser extent, the source control repository. You are definitely encouraged to contribute to the discussion and you can also help us to keep communications effective by following and promoting the guidelines listed here.

##Please Be Friendly
We strongly encourage everyone participating in GWTP development to show courtesy and respect to others. Of course, being courteous should not prevent us from constructively disagreeing with each other. But if you are enumerating 42 technical reasons against a particular proposal, please don't make the criticism worse by ridiculing the person who proposed it. State your technical disagreement freely, but respect the person you disagree with. That person may be a great learner who soon will be making the best proposals of us all.

Respectful also doesn't mean "serious". Web application development may be hard work, but it's also a lot of fun! Being lighthearted and playful is welcome. Let's enjoy being one of the friendliest communities in the whole open source movement.

##Where to Discuss GWTP
As always, discuss issues about using GWTP in the official [http://groups.google.com/group/gwt-platform GWT-Platform developer discussion group].

##How to Report a Bug
See [IssueTracking Issue Tracking].

#Working with the Code#
If you want to get your hands dirty with GWTP code, this is the section for you.

##Checking Out the Source from Mercurial
Checking out GWTP source code is most useful if you plan to compile GWTP yourself. The pre-built GWTP distribution already contains all the Java source code, so you don't actually need to check it out from the repository just to go through and debug it. Simply tweak your IDE to read source from the GWTP jars.

GWTP is hosted on Google Code project hosting, so you check out the source for GWTP using a Mercurial client as you would for any other project hosted on Google Code:
```
hg clone https://gwt-platform.googlecode.com/hg/ gwt-platform
```

##Building from source

```
hg clone https://gwt-platform.googlecode.com/hg/ gwt-platform
hg clone https://libs.gwt-platform.googlecode.com/hg/ gwt-platform-libs
cd gwt-platform
ant clean all
```

#Contributing Code
As GWTP is open source, it is easy for our users to fix bugs and create new features, and we always welcome the great patches we get from the community. Before you fire up your favorite IDE and begin hammering away at that new feature, though, please take the time to read this section and understand the process. While it seems rigorous, we want to keep a high standard of quality in the code base.

##Code Style
To keep the source consistent, readable, diffable and easy to merge, we use a fairly rigid coding style, and all patches will be expected to conform to the style outlined here. To keep things as simple as possible, many of these style rules will be automatically verified by the GWTP build; style issues that can't be caught by the build system should be addressed during code review. If you're using Eclipse, we strongly recommend that you install and enable the [http://eclipse-cs.sourceforge.net/ Eclipse Checkstyle plugin] that is used by the provided eclipse configuration.

In general, the GWTP style is based on the [http://java.sun.com/docs/codeconv/html/CodeConvTOC.doc.html standard Java conventions], except for the differences spelled out below.

For the record, we know that coding style is often a controversial topic. We acknowledge that plenty of great approaches exist out there. We're simply trying to pick one that is at least somewhat consistent with Sun's Java coding conventions, to codify it well, and stick to it.

###Comments and Javadoc

Every file should have an Apache license header at the top, prefaced with a copyright notice. A package statement and import statements should follow, each block separated by a blank line. Next is the class or interface declaration. In the Javadoc comments, describe what the class or interface does.

```java
/**
 * Copyright 2010 ArcBees Inc.
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
```

###Class Structure and Member Sort Order
Java types should have the following member order:

* Nested Types (mixing inner and static classes is okay)
* Static Fields
* Static Initializers
* Static Methods
* Instance Fields
* Instance Initializers
* Constructors
* Instance Methods

Members that fall into the same category (e.g. static methods) should also be sorted in this order based on visibility:

* public
* protected
* default
* private

All methods should be sorted alphabetically. Sorting is optional but recommended for fields. For example, the following class excerpt is correctly sorted:

```java
public abstract class Foo {
    // Type declarations.
    public class FooBaz {
    }

    private class FooBar {
    }

    // Static field declarations.
    // Remember, fields do NOT need to be sorted.
    static String B;
    static String A;

    // Static initializer declarations.
    static {
    }

    // Static methods declarations.
    // Remember, methods do need to be sorted.
    static void aStatic() {
    }

    static void bStatic() {
    }

    // Instance field declaration.
    String bField;
    String aField;

    // Instance Initializer declarations.
    {
    }

    // Constructors declaration.
    public Foo() {
    }

    protected Foo(String s) {
    }

    Foo(int i) {
    }

    private Foo(boolean b) {
    }

    // Instance method declaration.
    public void b() {
    }

    public void c() {
    }

    protected void a() {
    }

    protected void d() {
    }

    protected void e() {
    }

    protected void f() {
    }

    String h() {
    }

    // The "abstract" keyword does not modify the position of the method.
    abstract String i();

    void j() {
    }

    private void g() {
    }
}
```

###Indentation
We use 2-space indents for blocks. No tabs at all, anywhere.

We use 4-space indents after line wraps, including function calls and assignments. For example, this is correct (4 spaces after the newline):

```java
Instrument i =
    new Instrument();
```

and this is not correct (2 spaces after the newline):

```java
Instrument i =
  new Instrument();
```

###Imports
The ordering of import statements is:

* GWT imports
* GWTP imports
* Imports from third parties (com, junit, net, org)
* java and javax

To exactly match the IDE settings, the imports should be:

* Alphabetical within each grouping. Capital letters are considered to come before lower case letter (e.g. Z before a).
* There should be a blank line between each major grouping (google, com, junit, net, org, java, javax).

###Line Length and Wrapping
Each line of text in your code should be at most 80 characters long. Use linefeed characters to break lines (Unix-style). There are some exceptions:

* Java identifiers referenced from within JSNI methods can get quite long and cannot be parsed if split across lines.
* Exception: If a comment line contains an example command or a literal URL longer than 80 characters, that line may be longer than 80  characters for ease of cut and paste.
* Exception: Import lines can go over the limit because humans rarely see them. This also simplifies tool writing.

###Acronyms in names
Treat acronyms and abbreviations as words. The names are much more readable:

|Good | Bad |
|---|---|
|`XmlHttpRequest` | `XMLHTTPRequest` |
|`getCustomerId` | `getCustomerID` |

This style rule also applies when an acronym or abbreviation is the entire name:

|Good|Bad|
|---|---|
|`class Html`|`class HTML`|
|`String url;`|`String URL;`|
|`long id;`|`long ID;`|

Note that much code in GWT violates this rule, but they have said "all new code should treat acronyms as words".
For further justifications of this style rule, see Effective Java Item 56 (Item 38 in 1st edition) and Java Puzzlers Number 68.

###Parameterized type names
Parameterized type names should be one capital letter. However, if readability demands longer names (particularly due to having multiple parameters), the name should be capitalized and have suffix "T". In a nutshell, prefer `<T>` or `<K, V>`, and devolve to `<KeyT, ValueT>` if necessary.

|Good|Bad|Tolerable|
|---|---|
|`Foo<T>`|`Foo<FooClientType>`| |
|`Bar<K, V>`|`Bar<KeyType, ValueType>`| |
|`Baz<V, E, X>`|`Baz<EventType, ErrorType, ExpressionType>`|`Baz<EventT, ErrorT, ExpressionT>`|

###Unit Testing
Unit tests are very important, and we strongly encourage submissions that include them, adding new unit tests for new functionality or updating existing unit tests for bug fixes.

Tests for Java classes should be placed in a parallel source tree under test and the test class name should be suffixed with Test. For example,
`src/com/google/gwt/core/client/EntryPoint.java` is tested in
`test/com/google/gwt/core/client/EntryPointTest.java`.
The use of the parallel test tree has two major advantages:
  * You can do package scope testing (vs. a tests subpackage).
  * There is a clear separation between production and test code making packaging easier. This reduces the number of build breaks and the amount of time spent updating build files.

###Submitting Patches
Please do submit code. Here's what you need to do:
  # Decide which code you want to submit. A submission should be a set of changes that addresses one issue in the GWTP issue tracker. Please don't mix more than one logical change per submission, because it makes the history hard to follow. If you want to make a change that doesn't correspond to any issue currently listed in the issue tracker, please create one.

Also, coordinate your submission with team members that are listed on the issue in question. This ensures that work isn't being duplicated, and communicating your plan early also generally leads to better patches.
* Ensure that your code adheres to the GWTP source code style.
* Ensure that there are unit tests for your code.
* Remember to hg add any new files, and then upload the patch (using [http://codereview.appspot.com/static/upload.py upload.py]) to the Rietveld instance at http://codereview.appspot.com/, Include the relevant issue tracker number in your Rietveld description.

Add your new http://codereview.appspot.com/ url to the issue tracker entry.

###GWTP Committers
The current members of the `Arcbees` engineering team are the only committers at present.
