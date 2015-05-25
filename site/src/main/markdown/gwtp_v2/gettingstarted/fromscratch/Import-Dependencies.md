## Import the Dependencies
In order to use GWTP, you need to import the source code. This can either be done with a dependency management tool (Maven, Gradle, etc.) or manually. We highly recommend using tool as it makes managing transitive dependencies a lot easier.

### Import the Dependencies with Maven
For more information on Maven, go to the [official project page](https://maven.apache.org/).

* First, you need a pom file so create `/pom.xml` at the root of your project.
* Next, paste the following code in your file:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.mydomain.myproject</groupId>
    <artifactId>myproject</artifactId>
    <version>0.1-SNAPSHOT</version>

    <packaging>war</packaging>

    <properties>
        <!-- client -->
        <gwt.version>{{#gwt.version}}</gwt.version>
        <gwtp.version>{{#gwtp.version}}</gwtp.version>

        <!-- plugins -->
        <maven-war-plugin.version>2.6</maven-war-plugin.version>
        <maven-compiler-plugin.version>3.3</maven-compiler-plugin.version>

        <target.jdk>1.7</target.jdk>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>

        <webappDirectory>${project.build.directory}/${project.build.finalName}</webappDirectory>
    </properties>

    <build>
        <outputDirectory>${webappDirectory}/WEB-INF/classes</outputDirectory>

        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                
                <configuration>
                    <source>${target.jdk}</source>
                    <target>${target.jdk}</target>
                    <encoding>${project.build.sourceEncoding}</encoding>
                </configuration>
            </plugin>

            <!-- GWT -->
            <plugin>
                <groupId>org.codehaus.mojo</groupId>
                <artifactId>gwt-maven-plugin</artifactId>
                <version>${gwt.version}</version>
                
                <configuration>
                    <strict>true</strict>

                    <testTimeOut>180</testTimeOut>
                    <mode>htmlunit</mode>
                    <includes>**/*GwtTest.java</includes>

                    <logLevel>INFO</logLevel>

                    <runTarget>index.html</runTarget>
                    <module>com.mydomain.myproject.MyProject</module>
                </configuration>
                
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>test</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <dependencies>
        <!-- GWTP -->
        <dependency>
            <groupId>com.gwtplatform</groupId>
            <artifactId>gwtp-mvp-client</artifactId>
            <version>${gwtp.version}</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

* Finally, update the content of the `groupId`, `artifactId` and `module` tags to your project's values.

That's it. Maven will transitively resolve the sub-dependencies for you.

#### Snapshots
To use the latest snapshot ({{#gwtp.snapshot}}), you need to add the following repository in your pom.xml:

```xml
<repositories>
    <repository>
        <id>sonatype.snapshots</id>
        <name>Sonatype snapshot repository</name>
        <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>
```

### Import the Dependencies with the JARs
Using GWTP without a dependency management tool is a bit more trouble.

* First, let's create a new directory at the root of your project to hold the JARs files: `mkdir libs`.
* Next, download the following JARs and add them to the newly created folder:
    * [GWTP MVP Client](http://search.maven.org/remotecontent?filepath=com/gwtplatform/gwtp-mvp-client/{{#gwtp.version}}/gwtp-mvp-client-{{#gwtp.version}}.jar)
    * [GWTP MVP Shared](http://search.maven.org/remotecontent?filepath=com/gwtplatform/gwtp-mvp-shared/{{#gwtp.version}}/gwtp-mvp-shared-{{#gwtp.version}}.jar)
    * [GWTP Clients Common](http://search.maven.org/remotecontent?filepath=com/gwtplatform/gwtp-clients-common/{{#gwtp.version}}/gwtp-clients-common-{{#gwtp.version}}.jar)
    * [GWT SDK](http://storage.googleapis.com/gwt-releases/gwt-{{#gwt.version}}.zip)
    * [GIN](https://code.google.com/p/google-gin/downloads/list)
    * [Guice](http://goo.gl/ba8VWa)
        * [AOP Alliance](http://goo.gl/j1Y4ZV)
    * [Velocity](http://goo.gl/UYxZBZ)
        * [Commons Collections](http://goo.gl/BXdbDP)
        * [Commons Lang](http://goo.gl/c2U1GH)

> **Note**: A [jar containing all GWTP code](http://goo.gl/7vxVK5) exists, however is is unlikely that you need everything from GWTP at this point. Also, at this time, that JAR does not include transitive dependencies, so you would still have to download them manually.

## Create Files

Once you are done, it is time to [create the files](Create-Files.html).
