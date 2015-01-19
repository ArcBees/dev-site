#Project Creation
Create a project by generating a template based on a Maven Archetype. 

##Reference
[Find GWTP Archetypes Here](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes)

##Using a Maven Archetype

* Start by editing two of the Archetype creation attributes below. 
* Replace `com.projectname` with your chosen domain. 
* Replace `new-project-name` with your actual project name. (This has nothing to do with the GWT module name.)
* Run the Maven command (example code is pasted below) in the terminal or console of your operating system.
* Additional Archetypes are available [here](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes). There are a few different configurations to start from.
```bash
mvn archetype:generate -DarchetypeGroupId=com.arcbees.archetypes \
-DarchetypeRepository=https://oss.sonatype.org/content/repositories/snapshots/ \
-DarchetypeArtifactId=gwtp-basic-archetype \
-DarchetypeVersion=1.0-SNAPSHOT \
-DgroupId=com.projectname \
-DartifactId=new-project-name
```

##IDE Project Import
Choose an IDE to import the Maven project for easy modification and debugging.

* Import the created project into an IDE.
  * [IntelliJ IDEA Import Instructions](http://c.gwt-examples.com/home/maven/ide-import/intellij-idea)
  * [Eclipse Import Instructions](http://c.gwt-examples.com/home/maven/ide-import/eclipse)
* Once the project is imported, be sure to setup the debugger and test it runs in hosted mode. 