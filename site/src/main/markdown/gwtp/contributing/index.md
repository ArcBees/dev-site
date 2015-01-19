How to help make this project better...

##Reference
* [Contributing Guidelines][cg]

#Cloning
Cloning will download the entire project including all the large files we have messed around with in the past. There are some options to exclude the history to save on space and time to clone.

* run 'git clone --depth 1 git@github.com:ArcBees/GWTP.git' - exclude history
* run 'git clone --depth 30 git@github.com:ArcBees/GWTP.git' - include last 30 commits history
* run 'git clone git@github.com:ArcBees/GWTP.git' - include all history

#IDE Setup
We are using maven to setup the project for both eclipse and IntelliJ.

##After IDE Setup
1. run 'mvn generate-sources' - this will get the checkstyle working

##IntelliJ
TODO

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
Be sure to test the project before pulling your code on github.

* run 'mvn clean integration-test' - will test all the gwt client side tests
* run 'mvn clean test' - will test all the junit tests

#Formatting
TODO


#Building

##Builds
[![Build Status](https://buildhive.cloudbees.com/job/ArcBees/job/GWTP/badge/icon)](https://buildhive.cloudbees.com/job/ArcBees/job/GWTP/)

##Build Server
* [TeamCity Build Server](http://teamcity.gonevertical.org) - Snapshots and releases building here
* [CloudBees GWTP Build](https://buildhive.cloudbees.com/job/ArcBees/job/GWTP/) - Github Notifications
* [Old TeamCity Buid Server](http://teamcity.codebetter.com/project.html?projectId=project92)

##Snapshot Release
These steps build GWTP for Github downloads and release a snapshot

1. run 'mvn clean integration-test'
2. run 'mvn clean test'
3. run 'mvn clean deploy -Prelease'
4. run 'sh ./release-zip.sh /root/.m2/repository/com/gwtplatform'

##Sonatype Release
To officially release the library to sonatype.org.

1. run 'mvn clean deploy'
2. run 'mvn release:clean'
3. run 'mvn release:prepare -Dgpg.passphrase=yourpassphrase'
4. Answer all the questions. Following that, Maven will automatically modify the POM and commit, tag and push the code a couple of times. If things go wrong at that point, you should hg revert --all --no-backup or even hg strip any automatic commits by Maven (if they have not yet been pushed), you may have to clean up any remaining .orig or .backup files. Then start back at release:clean.
5. run 'mvn release:perform -Dgpg.passphrase=yourpassphrase'
6. Test the staged library before releasing
7. goto [sonatype.org](http://oss.sonatype.org/) select gwtp stage and click close. (See directions for howto.)

###Releasing Notes
* If something goes wrong and you can't Close the release, you will have to backout any change pushed by maven to the repository, including tags. To do this, update to the version before the automatic maven commit, and update with the tip indicating that you want to ignore all the changes for the branch with which you're merging.
* Next, the artifacts should be downloaded and tested.
* When everything works, click the Release button.
To see the release, go to Sonatype's Nexus at http://oss.sonatype.org/ and navigate to Repositories > Releases > com > gwtplatform.

###Directions
* [Maven Repo Usage Guide](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide)
* [Closing a Release Sonatype Directions](https://docs.sonatype.org/display/Repository/Sonatype+OSS+Maven+Repository+Usage+Guide#SonatypeOSSMavenRepositoryUsageGuide-8a.ReleaseIt)

###Notes
* Important to close eclipse before releasing

##Manual Release
* run 'mvn -Prelease -DaltDeploymentRepository=release::default::file://$DISTRIBUTION/release clean deploy'

##Zips Release
* run 'sh ./release-zip.sh'

[cg]: gwtp/contributing/Contributing-Guidelines.html "Contributing Guidelines"
