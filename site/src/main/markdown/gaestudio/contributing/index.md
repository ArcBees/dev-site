# Contributing

How to help make this project better...

##Reference
* [Contributing Guidelines][cg]

#Cloning
* run `git clone git@github.com:ArcBees/gae-studio.git`

#IDE Setup
We are using maven to setup the project for both eclipse and IntelliJ.

##IntelliJ
Open the project directory with IntelliJ and choose to import the project as a Maven project.

##Eclipse
1. Be sure to have EGit
2. Clone project using egit
3. On working directory import existing maven projects
4. Install CheckStyle - http://eclipse-cs.sourceforge.net/downloads.html
5. Use CheckStyle by following these directions - http://eclipse-cs.sourceforge.net/basic_setup_project.html
6. Activate checkstyle on the projects your working on by right clicking on project then properties then CheckStyle. Or right click then CheckStyle then activate CheckStyle

##Eclipse Notes
1. Don't run mvn eclipse:eclipse
2. If you do run mvn eclipse:eclipse, run eclipse:clean, and then close projects and import them as maven projects.

#Unit Testing
When you build new features please add build unit tests for it. If you have a problem with building unit tests reach out to one of the other contributors for help in the forum.

##Testing
Be sure to test the project before pulling your code on GitHub.

* run `mvn clean integration-test` - will test all the gwt client side tests
* run `mvn clean test` - will test all the junit tests

#Formatting
See [Contributing Guidelines][cg]

#Building
run `mvn clean install` in the `/gae-studio` directory.

##Build Server
* [TeamCity Build Server](http://teamcity-private.arcbees.com/project.html?tab=projectOverview&projectId=GaeStudio/) - Snapshots and releases building here

###Directions
* [Maven Repo Usage Guide](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)
* [Closing a Release Sonatype Directions](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-8a.ReleaseIt)

###Notes
* Important to close eclipse before releasing

[cg]: gaestudio/contributing/Contributing-Guidelines.html "Contributing Guidelines"
