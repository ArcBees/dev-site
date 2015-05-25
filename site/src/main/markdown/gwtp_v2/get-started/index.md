# Create an App from the Archetype
Generate a project structure by using a Maven Archetype. The Archetype will generate all the boilerplate required to get a fresh project started.

## References
Find out all GWTP Archetypes [here](https://github.com/ArcBees/Arcbees-Archetypes/tree/master/archetypes).
More information about Maven Archetypes [here](https://maven.apache.org/guides/introduction/introduction-to-archetypes.html).

## Using a Maven Archetype
* Create a variable `groupId` and assign your domain to it. ie: `groupId="com.mydomain"` on Linux or Mac. By convention, a group ID only contains lowercase letters and dots.
* Create a variable `artifactId` and assign your project name. ie: `artifactId="myprojectname` on Linux or Mac. By convention, a group ID only contains lowercase letters.
* Create a variable `moduleName` and assign it a GWT module name. ie: `moduleName="MyProjectName"` on Linux or Mac. The name should use an upper CamelCase syntax.
* Navigate to the *parent directory* in which you want to create new project.
* Run the following Maven command in your terminal:

```
mvn archetype:generate -DarchetypeGroupId=com.arcbees.archetypes \
-DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots/ \
-DarchetypeArtifactId=gwtp-basic-archetype \
-DarchetypeVersion={{#gwtp.version}} \
-DgroupId=$groupId \
-DartifactId=$artifactId \
-DmoduleName=$moduleName
```

## IDE Project Import
Choose an IDE to import the Maven project and get started with development.

For more information on importing a GWT project into an IDE, follow the following links:

* [Instructions on importing a GWT project in IntelliJ IDEA](http://c.gwt-examples.com/home/maven/ide-import/intellij-idea)
* [Instructions on importing a GWT project in Eclipse](http://c.gwt-examples.com/home/maven/ide-import/eclipse)
