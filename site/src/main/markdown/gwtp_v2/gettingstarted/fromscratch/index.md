# Create an App from Scratch
If you can't, or prefer not to, use a Maven Archetype, the following steps will guide you through the creation of a new application from scratch.

<!---
TODO: > For a more in-depth guide on creating a new GWTP application, you can read the [Beginner's Tutorial]().
-->

## Folder Structure
First, start off by creating the initial folder structure. This structure is going to help us separate logical components.

```
project_root
└─ src
    └─ main
        ├─ java
        │  └─ com
        │      └─ mydomain
        │          └─ myproject
        │              └─ client
        │                  └─ application
        │                      └─ home
        └─ webapp
            └─ WEB-INF
```

Using your terminal, you can easily create all the directories with the following commands:

```sh
mkdir -p src/main/java/com/mydomain/myproject/client/application/home
mkdir -p src/main/java/webapp/WEB-INF
```

## Import the Dependencies
In order to use GWTP, you need to import the source code. This can either be done with a dependency management tool (Maven, Gradle, etc.) or manually. We highly recommend using tool as it makes managing transitive dependencies a lot easier.

[Import the Dependencies with Maven](Import-Dependencies).
[Import the Dependencies with the JARs](Create-Files).
