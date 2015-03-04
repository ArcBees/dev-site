#Maven

Jukito is released on Maven central. If you use Maven you can configure add this dependency:

```
<dependency>
    <groupId>org.jukito</groupId>
    <artifactId>jukito</artifactId>
    <version>${jukito.version}</version>
    <scope>test</scope>
</dependency>
```

Replace `${jukito.version}` with the current version of Jukito, which you should find on the [main page](https://github.com/ArcBees/Jukito).

If you want to use a jukito SNAPSHOT you will have to include Sonatype's repository in your POM:

```
<repository>
    <id>sonatype.snapshots</id>
    <name>Sonatype snapshot repository</name>
    <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
    <layout>default</layout>
</repository>
```

You can also [download jars directly from Maven Central](http://search.maven.org/#search%7Cga%7C1%7Corg.jukito).