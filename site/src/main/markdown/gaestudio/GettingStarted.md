#Running GAE-Studio on AppEngine
1. Download the latest war of GAE Studio from our Drive folder
2. Extract the content of the war to your preferred location.
3. Download the latest version of Google Appengine SDK for Java.
4. Extract the content of the zip to your preferred location.
5. Using the command line, execute: `/appengine-java-sdk-x.y.z/bin/appcfg.sh -A __application_id__ -M gaestudio update /path/to/unpacked/gae_studio`
6. Replace `__application_id__` by your application ID. You may be asked to enter your email and password.

GAE Studio will be available at http://gaestudio.__application_id__.appspot.com

    klajdwkjhdalskjdwlkajd
    wdlkjawdaw
    daw wdw
    ad wdaw dwa dwa dwad wadl;kwdq;`k

#Running GAE-Studio locally (Java runtime)
## Using MAVEN
### App Engine configuration files
* Create a file named `appengine-application.xml` in `/src/META-INF`

        <?xml version="1.0" encoding="utf-8" standalone="no"?>
        <appengine-application xmlns="http://appengine.google.com/ns/1.0">
            <application>__application_id__</application>
        </appengine-application>
* Create a file named `application.xml` in `/src/META-INF`

        <?xml version="1.0" encoding="UTF-8"?>
        <application xmlns="http://java.sun.com/xml/ns/javaee"
                xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/application_5.xsd"
                version="5">

            <description>${project.name}</description>
            <display-name>${project.name}</display-name>

            <module>
                <web>
                    <web-uri>${project.build.finalName}.war</web-uri>
                    <context-root>/</context-root>
                </web>
            </module>

            <module>
                <web>
                    <web-uri>gae-studio-webapp-${gae-studio.version}-module.war</web-uri>
                    <context-root>/gaestudio</context-root>
                </web>
            </module>

            <library-directory>lib</library-directory>
        </application>
* In your `appengine-web.xml` file, add `<module>default</module>`

### POM file modifications
* Add a property : `<gae-studio.version>1.0-RC1</gae-studio.version>`
* In the **maven-war-plugin** configuration, add : `<packagingExcludes>%regex[.*?gae-studio-webapp-${gae-studio.version}.*],**/gaestudio/**</packagingExcludes>`
* Add the **appengine-maven-plugin** if you're not already using it

        <plugin>
            <groupId>com.google.appengine</groupId>
            <artifactId>appengine-maven-plugin</artifactId>
            <configuration>
                <enableJarSplitting>true</enableJarSplitting>
            </configuration>
        </plugin>
* Create a profile named **gae-studio-ear**

        <profile>
            <id>create-ear</id>

            <build>
                <resources>
                    <resource>;
                        <directory>src/META-INF</directory>
                        <targetPath>${webappDirectory}/META-INF</targetPath>
                        <filtering>true</filtering>

                        <includes>
                            <include>**/*.xml</include>
                        </includes>
                    </resource>
                </resources>

                <plugins>
                    <plugin>
                        <groupId>org.apache.maven.plugins</groupId>
                        <artifactId>maven-ear-plugin</artifactId>
                        <version>${maven-ear-plugin.version}</version>

                        <configuration>
                            <version>5</version>
                            <generateApplicationXml>false</generateApplicationXml>
                            <defaultLibBundleDir>lib</defaultLibBundleDir>
                            <packagingIncludes>META-INF/**, %regex[.*?\.war/.*]</packagingIncludes>
                            <unpackTypes>war</unpackTypes>
                        </configuration>
                    </plugin>
                </plugins>
            </build>

            <dependencies>
                <!-- GAE Studio -->
                <dependency>
                    <groupId>com.arcbees.gaestudio</groupId>
                    <artifactId>gae-studio-webapp</artifactId>
                    <version>${gae-studio.version}</version>
                    <classifier>module</classifier>
                    <type>war</type>
                </dependency>

                <dependency>
                    <groupId>${project.groupId}</groupId>
                    <artifactId>${project.artifactId}</artifactId>
                    <version>${project.version}</version>
                    <type>war</type>
                </dependency>
            </dependencies>
        </profile>
Simply run `mvn clean install appengine:devserver -Pgae-studio-ear`. This will start both your application and the GAE Studio module.

## Using the App Engine SDK
Running in development mode implies converting your application to a module. Then, you need to create an EAR application containing your app and GAE-Studio. The official App Engine documentation explains how to create the EAR application.
GAE-Studio is already declared as a module called 'gaestudio'.
You can run this EAR using the Java Appengine Tools (Google Appengine SDK for Java): /appengine-java-sdk-x.y.z/bin/dev_appserver.sh /path/to/my/ear.
The development server will start your app and GAE-Studio on two different ports. They will be displayed in the console output. ie: INFO: Module instance gaestudio is running at http://localhost:8081/

#Running GAE-Studio locally (Python runtime)

#Running GAE-Studio locally (Go runtime)

#Running GAE-Studio locally (PHP runtime)
