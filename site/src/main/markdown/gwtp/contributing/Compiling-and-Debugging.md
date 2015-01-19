#Compiling and Debugging

#Introduction

This page is meant for GWTP contributors and details all the steps needed to build and debug GWTP comfortably within Eclipse. If you're only interested in running the samples, check out RunningGwtpSamples.

Thanks out to [Yannis Gonianakis](http://jgonian.wordpress.com/) for his amazing work on Mavenizing GWTP.

#Downloading the GWTP sources from mercurial

Follow the steps [RunningGwtpSamples#From_mercurial detailed here].

#Getting the tools

You'll need [RunningGwtpSamples#Getting_Maven Maven], Eclipse Indigo for Java Developer (The Java EE version may not work), the [RunningGwtpSamples#Google_Plugin_for_Eclipse Google Plugin for Eclipse], and the [RunningGwtpSamples#Install_the_Eclipse-Maven_mappers Eclipse-Maven mappers].

##Compiling using Maven

To compile the GWTP libraries from a command line prompt go to the top-level directory of the GWTP sources and type:
```
> cd gwt-platform
> mvn clean install
```
This can take a while since maven will download all required dependencies, compile the sources, run the tests, package and install the libraries into your local maven repository. Once the process is done you should see `BUILD SUCCESS`. If you don't, it may be time to [http://groups.google.com/group/gwt-platform hit the forum]!

#Importing GWTP into Eclipse and Running Samples

Follow the [RunningGwtpSamples#Compiling_and_running_within_Eclipse steps given here].

##Using an external debugger

If you prefer to use Eclipse as an external debugger, just right-click on the sample in the package explorer and select `Debug As > Maven Build...`. This will bring up a dialog. In `Goals` type `gwt:debug`. Once you get a message that the system is listening for a debugger on port 8000, right-click on the same sample, select `Debug As > Debug Configurations > Remote Java Application` and click on the `New Launch Configuration` button. All the fields should be correct, but double check the port. Once it's done simply click `Debug`.

#Starting over when things go wrong

Sometimes things go wrong, and you end up with a bunch of files that can mess-up your development environment. At this point, the first thing to try is to reload your Maven configuration. Right-click on the main project, `gwtp`, and select `Maven > Update Project Configuration`.

If this fails, in the package explorer click `Collapse All`, then select all the projects by shift-clicking, then hit `delete`. Make sure you _don't_ delete the projects from disk. Once this is done, in a command prompt go to the root `gwt-platform` directory and issue:
```
> mvn clean eclipse:clean
```
Then import the projects again.

In extreme cases, you can commit all your work and wipe the entire source directory save for the `.hg` directory. You can then use mercurial to update to an earlier revision then back to head to restore all your files.

#Tips and advanced operations

This section contains tips or rarely used features. They are kept here for completeness, in order to help contributors perform advanced maintenance operations.

##Tip: Compiling only what you need

When working from the command line, recompiling everything as soon as you change a line of code can be quite costly. If you're only interested in recompiling GWTP, but not the samples, then you simply have to run `mvn install` from the `gwtp-core` directory. You can do the same thing from the `gwtp-samples` directory to compile only the samples. In fact, you can do that from any subdirectory to generate all the artifacts lying under that directory. Keep in mind that you have to run `mvn install` once from the root before these partial compilations will work.

##Getting the jars

Compiling everything produces a bunch of jars, which you can find under the various `gwtp-core/*/target` directories . For example, the client-side jar for the MVP components is:
```
gwtp-core/gwtp-mvp-client/target/gwtp-mvp-client-[version].jar
```
See the DescriptionOfIndividualJars for more details on each of these jars.

In most cases, you don't really care about optimizing the size of the jars. In such situations you can simply grab the compound jar that includes all of the above. 
To create the compound jar, package the `gwtp-all` module. From a terminal window type:
```
> cd gwtp-core/gwtp-all
> mvn package
```
and look for the compound artifact in the target directory: 
```
gwtp-core/gwtp-all/target/gwtp-all-[version].jar
```

Please note that the compound jar for the current SNAPSHOT version of gwt-platform can be found on the [ContinuousIntegration Continuous Integration server] and snapshot versions are regularly uploaded to a [UsingGwtpWithMaven maven repository]. Once you're there, login (creating an account is easy), find the `gwt-platform trunk` project, click on `Artifact` and look for `gwtp-core/gwtp-all/target/gwtp-all-[version].jar`.

##Generating the javadoc

If you want to build the javadoc yourself, all you have to do is to activate the `release` maven profile. This can be easily done by passing the `-Prelease` argument along with the rest of your maven goals. For example, to build every module inside gwtp-core with javadoc and sources artifacts attached to the build you should type:
```
> cd gwtp-core
> mvn package -Prelease
```

The javadoc will be under `gwtp-core/*/target/apidocs`, simply open `index.html` in there. You can also find the javadoc for the current SNAPSHOT version of gwt-platform on the [ContinuousIntegration Continuous Integration server].

##Making sure the Eclipse checkstyle plugin is running

*TODO: Explain how this should work for Eclipse 3.7*

##Custom maven repository

In snapshot versions, GWTP sometimes makes use of recent versions of various libraries that are not yet available in any Maven repository. To that effect, we maintain our own custom Maven repository with these versions. You can obtain the repository by doing:
```
hg clone http://maven.gwt-platform.googlecode.com/hg/ gwt-platform-maven
```
In order to add a library to this repository use `mvn deploy:deploy-file`. For example, to deploy a `gin` snapshot, from the directory containing `gin-1.0-r170.jar` type:
```
mvn deploy:deploy-file -DgroupId=com.google.gwt.inject -DartifactId=gin -Dversion=1.0-r170 -Dpackaging=jar -Dfile=gin-1.0-r170.jar -Durl=file://c:/users/beaudoin/workspace/gwt-platform-maven/
```