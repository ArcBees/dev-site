# Create an App from Scratch
If you can't, or prefer not to, use a Maven Archetype, the following steps will guide you through the creation of a new application from scratch.

<!---
TODO: > For a more in-depth guide on creating a new GWTP application, you can read the [Beginner's Tutorial]().
-->

## Folder Structure
First, start off by creating the initial folder structure. This structure is going to help us separate logical components.

```
/
└─ src
    └─ main
        ├─ java
        │  └─ com
        │      └─ mydomain
        │          └─ myproject
        │              └─ application
        │                  └─ home
        └─ webapp
            └─ WEB-INF
```

Using your terminal, you can easily create all the directories with the following commands:

```sh
mkdir -p src/main/java/com/mydomain/myproject/application/home
mkdir -p src/main/java/webapp/WEB-INF
```

## Import GWTP
In order to use GWTP, you need to import the source code. This can either be done with a dependency management tool (Maven, Gradle, etc.) or manually. We highly recommend using tool as it makes managing transitive dependencies a lot easier.

### Import GWTP using Maven
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
        <gwt.version>2.7.0</gwt.version>
        <gwtp.version>1.5</gwtp.version>

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

### Import GWTP using the JARs
Using GWTP without a dependency management tool is a bit more trouble.

* First, let's create a new directory at the root of your project to hold the JARs files: `mkdir libs`.
* Next, download the following JARs and add them to the newly created folder:
    * [GWTP MVP Client](http://goo.gl/ExQWjA)
    * [GWTP MVP Shared](http://goo.gl/gmlkAr)
    * [GWTP Clients Common](http://goo.gl/lVyqL9)
    * [GWT SDK](http://goo.gl/t7FQSn)
    * [GIN](https://code.google.com/p/google-gin/downloads/list)
    * [Guice](http://goo.gl/ba8VWahttp://goo.gl/ba8VWa)
        * [AOP Alliance](http://goo.gl/j1Y4ZV)
    * [Velocity](http://goo.gl/UYxZBZ)
        * [Commons Collections](http://goo.gl/BXdbDP)
        * [Commons Lang](http://goo.gl/c2U1GH)

> **Note**: A [jar containing all GWTP code](http://goo.gl/7vxVK5) exists, however is is unlikely that you need everything from GWTP at this point. Also, at this time, that JAR does not include transitive dependencies, so you would still have to download them manually.

## Start Coding

<!--- TODO:
* web.xml
* index.html
* NameTokens -- point to unauthorized/error place documentation
* ClientModule
* GWT module
* ApplicationPresenter
* HomePresenter
-->
