# Maven Configuration

## Latest Version
[Find the lastest version listed here](https://github.com/ArcBees/GWTP)

## Maven Central Jars
[Download Jars from here](http://search.maven.org/#search%7Cga%7C1%7Ccom.gwtplatform) or follow the manual dependency download procedure.

## Manual Dependency Download Procedure
For those who want to download the dependencies manually, this can be most easily accomplished in the following manner:

* Create a Java project
* Install Maven in your IDE, if necessary.
* Download this: [pom.xml](https://gist.github.com/4666272)
* Run `mvn package` in the pom.xml directory.
* Copy the jars you want from the target/project/WEB-INF/lib directory to the desired location.

## Archetypes
Quickly start up a project with an GWTP Archetype or project template. Find them [here](https://github.com/ArcBees/ArcBees-tools/tree/master/archetypes).

# Dependencies

## GWTP Maven Dependencies
To enable GWTP in your pom.xml, add the dependencies below. For more information on the available components, see [library overview](https://github.com/ArcBees/GWTP/wiki/Overview-of-the-library). Define the `${gwtp.version}` property in your POM `<properties>`.

* Properties:

```
<properties>
    <gwtp.version>1.2.1</gwtp.version>
</properties>
```

* Dependencies:

```
<!-- MVP component -->
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-mvp-client</artifactId>
    <version>${gwtp.version}</version>
    <scope>provided</scope>
</dependency>

<!-- Dispatch component -->
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-dispatch-client</artifactId>
    <version>${gwtp.version}</version>
    <scope>provided</scope>
</dependency>

<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-dispatch-server-guice</artifactId>
    <!-- Or, if you use spring:
    <artifactId>gwtp-dispatch-server-spring</artifactId> -->
    <version>${gwtp.version}</version>
</dependency>

<!-- Crawler component -->
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-crawler</artifactId>
    <version>${gwtp.version}</version>
</dependency>

<!-- Annotation component -->
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-processors</artifactId>
    <version>${gwtp.version}</version>
    <scope>provided</scope>
</dependency>

<!-- Tester component -->
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-tester</artifactId>
    <version>${gwtp.version}</version>
    <scope>test</scope>
</dependency>
```

## Snapshots
Snapshots of the current version are regularly deployed. If you want to experiment with bleeding edge, evolving versions of the platform, feel free to collaborate in use of these snapshot dependencies.

* Here's how you use the snapshot dependencies. Get the lastest snapshot version from [here](https://github.com/ArcBees/GWTP).

```
<properties>
    <gwtp.version>1.3-SNAPSHOT</gwtp.version>
</properties>

<repositories>
    <repository>
        <id>sonatype.snapshots</id>
        <name>Sonatype snapshot repository</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
        <layout>default</layout>
    </repository>
</repositories>
```

# GWT Maven Plugin Configuration

## JUnit & GWT Testing
The purpose of this configuration is to skip over GWT Test when running `mvn test`; and to test GWT tests during the `mvn integration-test`. Configure your goals so that maven-surefire-plugin skips the GWT Tests.

Note that it's far faster to use the GWT Test Suite for multiple tests, so switching out the the GWT plugin configuration as noted below will speed up testing.

* Plugin configuration to skip GWT tests during Junit testing.

```
<plugins>
    <!-- JUnit Testing - skip *.GwtTest cases -->
    <!-- 'mvn test' - runs the Jukito tests -->
    <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-surefire-plugin</artifactId>
        <version>${maven-surefire-plugin.version}</version>
        <configuration>
            <includes>
                <include>**/*Test.java</include>
            </includes>
            <excludes>
                <exclude>**/*GwtTest.java</exclude>
            </excludes>
        </configuration>
    </plugin>
</plugins>
```

## GWT Plugin
Setting up the GWT Maven plugin can be done using the configuration described below. This configuration is an amalgam of the most common configuration options for the GWT plugin. It's important to note that when testing with multiple GWTTestCases, it is faster to replace the running of individual tests with the running of a test suite. For more information on this maven plugin see the docs [here](http://mojo.codehaus.org/gwt-maven-plugin/).

* Plugin configuration doesn't have to include all these options, but having a complete overview should help with the plugin setup.
* `mvn gwt:test` is the same as `mvn integration-test` if the above JUnit configuration is done.
* `mvn gwt:run` will run web mode from the terminal.
* `mvn gwt:debug` will run hosted mode from the terminal.
* `mvn integration-test` will run the gwt tests cases.
* Heap space errors often occur with large projects with many dependencies. Below the configuration allows for larger dependency collections by increasing the heap. The default is 512m.

* GWT properties needed for configuration

```
<properties>
    <gwt.version>2.6.1</gwt.version>
    <gwt.style>OBF</gwt.style>
    <gwt-maven-plugin.version>2.6.1</gwt-maven-plugin.version>
    <webappDirectory>${project.build.directory}/${project.build.finalName}</webappDirectory>

    <!-- When using Google App Engine include this -->
    <gae.version>1.9.6</gae.version>
    <gae.home>
        ${settings.localRepository}/com/google/appengine/appengine-java-sdk/${gae.version}/appengine-java-sdk-${gae.version}
    </gae.home>
</properties>

<build>
    <outputDirectory>${webappDirectory}/WEB-INF/classes</outputDirectory>
</build>
```

* GWT Plugin with in the pom.xml `<build/>` element.

```
<plugins>
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>gwt-maven-plugin</artifactId>
        <version>${gwt.version}</version>
        <configuration>
            <strict>true</strict>
            <testTimeOut>180</testTimeOut>
            <!-- With multiple tests use GwtTestSuite.java for speed -->
            <includes>**/*GwtTest.java</includes>
            <!-- <includes>**/*GwtTestSuite.java</includes> -->
            <mode>htmlunit</mode>

            <extraJvmArgs>-Xss1024k -Xmx1024m -XX:MaxPermSize=256m</extraJvmArgs>
            <logLevel>INFO</logLevel>
            <style>${gwt.style}</style>

            <copyWebapp>true</copyWebapp>
            <hostedWebapp>${webappDirectory}</hostedWebapp>

            <!-- When using Google App Engine, include this section -->
            <!-- First time GAE users and using the kindle lib, run 'mvn gae:unpack` -->
            <server>com.google.appengine.tools.development.gwt.AppEngineLauncher</server>
            <appEngineVersion>${gae.version}</appEngineVersion>
            <appEngineHome>${gae.home}</appEngineHome>
            <extraJvmArgs>-Dappengine.sdk.root=${gae.home}</extraJvmArgs>

            <runTarget>Project.html</runTarget>
            <module>com.arcbees.project.Project</module>
        </configuration>
        <executions>
            <execution>
                <goals>
                    <goal>resources</goal>
                    <goal>test</goal>
                    <goal>compile</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

## Google App Engine Maven Plugin
The Google App Engine plugin can automatically run App Engine and deploy it to App Engine. Google is also developing a maven plugin but as yet it doesn't cover all the necessary lifecycle features. To find out more about this plugin, see the docs [here](https://github.com/maven-gae-plugin/maven-gae-plugin).

* `mvn gae:run` runs the GAE application.
* `mvn gae:deploy` deploys to GAE.
* `mvn gae:unpack` will setup the library directory. This is a need for first time users!

* For automatic deployments the username and password can be stored in the ~/.m2/settings.xml file.

```
<!-- Appengine Credentials -->
<server>
    <id>appengine-credentials</id>
    <username>username@arcbees.com</username>
    <password>password</password>
</server>
```

* Google App Engine configuration with in the pom `<build/>` element.

```
<plugins>
    <!-- Google App Engine Deployment -->
    <plugin>
        <groupId>net.kindleit</groupId>
        <artifactId>maven-gae-plugin</artifactId>
        <version>${maven-gae-plugin.version}</version>
        <configuration>
            <sdkDir>${gae.home}</sdkDir>
            <!-- Add credentials to ~/.m2/settings.xml <id>appengine-credentials</id> -->
            <serverId>appengine-credentials</serverId>
            <splitJars>true</splitJars>
        </configuration>
        <executions>
            <!-- First time GAE users, run 'mvn gae:unpack' -->
            <execution>
                <id>install-server-jar</id>
                <phase>validate</phase>
                <goals>
                    <goal>unpack</goal>
                </goals>
            </execution>
            <execution>
                <id>deploy</id>
                <goals>
                    <goal>deploy</goal>
                </goals>
            </execution>
        </executions>
    </plugin>
</plugins>
```

# Custom Pom Configuration Options

## Resources
When putting files into the resources directory the resources will need to be defined in the pom.xml.

* Describe the resource directories something like this:

```
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
    </resources>

    <testResources>
        <testResource>
            <directory>src/test/resources</directory>
            <includes>
                <include>**/*.properties</include>
                <include>**/*.feature</include>
            </includes>
        </testResource>
    </testResources>
</build>
```

## Annotation Processor
Configuring maven to generate the @annotated classes for the boilerplate generation. [Boilerplate-Generation][bg] goes
over this features uses.

* Configure the resources:

```
<build>
    <resources>
        <resource>
            <directory>src/main/resources</directory>
        </resource>
        <resource>
            <directory>${project.build.directory}/generated-sources/apt</directory>
        </resource>
        <resource>
            <directory>${project.build.directory}/generated-sources/gwt</directory>
        </resource>
    </resources>
</build>
```

* Add the processor dependency:

```
<dependency>
    <groupId>com.gwtplatform</groupId>
    <artifactId>gwtp-processors</artifactId>
    <version>${gwtp.version}</version>
    <scope>provided</scope>
</dependency>
```

* Wire up the annotation processor plugin to run when `mvn clean generate-sources` is ran. Or use `mvn clean package`.

```
<plugins>
     <!-- Run annotation processors on src/home/java sources -->
    <plugin>
        <groupId>org.bsc.maven</groupId>
        <artifactId>maven-processor-plugin</artifactId>
        <version>2.2.4</version>
        <executions>
            <execution>
                <id>process</id>
                <goals>
                    <goal>process</goal>
                </goals>
                <phase>generate-sources</phase>
            </execution>
        </executions>
        <dependencies>
            <dependency>
                <groupId>com.gwtplatform</groupId>
                <artifactId>gwtp-processors</artifactId>
                <version>${gwtp.version}</version>
            </dependency>
        </dependencies>
    </plugin>

    <!-- Copy the generated classses -->
    <plugin>
        <groupId>org.codehaus.mojo</groupId>
        <artifactId>build-helper-maven-plugin</artifactId>
        <version>1.5</version>
        <executions>
            <execution>
                <id>add-source</id>
                <phase>generate-sources</phase>
                <goals>
                    <goal>add-source</goal>
                </goals>
                <configuration>
                    <sources>
                        <source>${project.build.directory}/generated-sources/apt</source>
                        <source>${project.build.directory}/generated-sources/gwt</source>
                    </sources>
                </configuration>
            </execution>
        </executions>
    </plugin>
</plugins>
```

### Generate Resources
To generate the resources simply run `mvn clean generate-resources` or `mvn clean package`.

### Eclipse Automated Resource Building
For the Eclipse users the lifecycle mapping plugin has to be added to the pom.xml. This will allow the Eclipse builder to automatically build the annotated resources when the project builds, although the project may need to be refreshed so that the class shows up. If this plugin is used selecting Project > Clean will update the resources directory. During the first project setup, Maven Update may need to be applied twice so the generated source directories link up automatically.

* Include this in the pom for Eclipse workspace building to update resources. *Refresh* by right clicking on project.

```
<build>
    <pluginManagement>
        <plugins>
            <plugin>
                <groupId>org.eclipse.m2e</groupId>
                <artifactId>lifecycle-mapping</artifactId>
                <version>1.0.0</version>
                <configuration>
                    <lifecycleMappingMetadata>
                        <pluginExecutions>

                            <pluginExecution>
                                <pluginExecutionFilter>
                                    <groupId>org.bsc.maven</groupId>
                                    <artifactId>maven-processor-plugin</artifactId>
                                    <versionRange>[2.0.5,)</versionRange>
                                    <goals>
                                        <goal>generate-sources</goal>
                                    </goals>
                                </pluginExecutionFilter>
                                <action>
                                    <execute />
                                </action>
                            </pluginExecution>

                        </pluginExecutions>
                    </lifecycleMappingMetadata>
                </configuration>
            </plugin>
        </plugins>
    </pluginManagement>
</build>
```

[bg]: gwtp/otherusefulinformation/Boilerplate-Generation.html "Boilerplate Generation"
