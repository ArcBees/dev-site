# Contributing

How to help make this project better...

##Cloning
* run `git clone git@github.com:ArcBees/gae-studio.git`

##IDE Setup
We are using Maven to setup the project for both eclipse and IntelliJ.

##Unit Testing
When you build new features please add build unit tests for it. If you have a problem with building unit tests reach out to one of the other contributors for help in the forum.

###Testing
Be sure to test the project before pulling your code on GitHub.

* run `mvn clean integration-test` - will test all the gwt client side tests
* run `mvn clean test` - will test all the junit tests

##Formatting
See [Contributing Guidelines][cg]

##Building
run `mvn clean install` in the `/gae-studio` directory.

###Build Server
* [TeamCity Build Server](http://teamcity-private.arcbees.com/project.html?tab=projectOverview&projectId=GaeStudio/) - Snapshots and releases building here

###Directions
* [Maven Repo Usage Guide](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)
* [Closing a Release Sonatype Directions](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-8a.ReleaseIt)

###Reference
* [Contributing Guidelines][cg]

[cg]: gaestudio/contributing/Contributing-Guidelines.html "Contributing Guidelines"
