# Create an App from Scratch
If you can't, or prefer not to use a Maven Archetype, the following steps will guide you through the creation of a new application from scratch.

> **Note**: For a more in-depth guide on creating a new GWTP application, you can read the [Beginner's Tutorial]().
<!--- TODO: Fix this link before release -->

## Folder Structure
First, start off by creating the initial folder structure. This structure is going to help us separate logical components.

<pre>
project_root
&boxur;&boxh; src
   &boxur;&boxh; main
      &boxvr;&boxh; java
      &boxv; &boxur;&boxh; com
      &boxv;    &boxur;&boxh; mydomain
      &boxv;       &boxur;&boxh; myproject
      &boxv;          &boxur;&boxh; client
      &boxv;             &boxur;&boxh; application
      &boxv;                &boxur;&boxh; home
      &boxur;&boxh; webapp
         &boxur;&boxh; WEB-INF
</pre>

Using your terminal, you can easily create all the directories with the following commands:

```bash
mkdir -p src/main/java/com/mydomain/myproject/client/application/home
mkdir -p src/main/java/webapp/WEB-INF
```

# Import the Dependencies
In order to use GWTP, you need to import the source code. This can either be done with a dependency management tool (Maven, Gradle, etc.) or manually. We highly recommend using such a tool as it makes managing transitive dependencies a lot easier.

If using Maven you should really skip through the boilerplate and [use an archetype]({{#gwtp.doc_home_url}}/get-started/index.html).

* [Import the Dependencies with Maven]({{#gwtp.doc_home_url}}/get-started/from-scratch/Import-Dependencies.html#maven)
* [Import the Dependencies with the JARs]({{#gwtp.doc_home_url}}/get-started/from-scratch/Import-Dependencies.html#jars)
