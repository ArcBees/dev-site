# Migrating from version 0.7 to 1.0

Before attempting migration, we recommend sample an archetype showcasing the latest and greatest additions to GWTP. This guide should help give you a feel for how things fit together. Most of the changes regard how the defaults are set up, making it easier to get off the ground quicker without affecting preexisting application designs.

## Notable Changes
There are a few breaking changes and some changes that are built by the generator to make life easier. Listed are a the changes that will affect migration to 1.0.

1. Default GWTP Bootstrapping
 - new project module inherits
 - removed project entrypoint
 - removed ginjector interface
 - added default placemanager
 - dispatch module configuration change
2. Changed Widget to IsWidget in Slots
3. Removed uber-jar gwtp-all
4. PlaceRequest upgraded to use builder

## Migration Changes
More defaults have been added to GWTP to eliminate boilerplate and get projects up and going more quickly. That said, it remains a flexible platform, and all defaults can be overridden according to your preference.

Migrating a project from GWTP 0.7 to 1.0 doesn't require any special modifications besides incrementing the
dependency and modifying the slot method signatures Widget to IsWidget. For more information and to take advantage of
 some of the new default features, check out [Bootstrapping or Application Initialization][boot].

Before upgrading your project, **branch it or back it up**. Users who do not step methodically through the migration process can get tangled up in errors. It's very important to follow the prescribed sequence of actions *in order*, if you hope to have a smooth upgrading experience.

### Dependencies
Start by updating the GWTP dependency to 1.0. Be sure to update GWT, gin and guice if needed. Don't forget to check
for other possibile dependency updates. Find out more on how to set up maven here [Maven-Configuration][mc].

### Entrypoint
The entry point can be changed to a default GWTP entry point. See [Bootstrapping or Application Initialization][boot]
for entry point options. Update the Client module if you update the entry point.

### ClientModule
The client module can be updated based on your configuration. See [Bootstrapping or Application Initialization][boot]
 for more information. Here is a good [example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-basic/src/main/java/com/arcbees/project/client/gin/ClientModule.java).

### PlaceManager
A simple place manager has been added by default, and can easily modified. Refer to the [PlaceManager][pm] guide for
more information.

### IsWidget
This is a breaking change and has to be updated in GWTP 1.0. The views slot method signatures have changed, requiring the use of IsWidget in the signature. This change will not affect any pre-existing designs, widgets or composites that are widgets; in other words, only the signature has to change. You also have the option to build your widgets as an IsWidget. Check out this [example](https://github.com/ArcBees/ArcBees-tools/blob/master/archetypes/gwtp-appengine-objectify/src/main/java/com/arcbees/project/client/application/ApplicationView.java#L43) here.

## Migration Support Services
If you are concerned about potential problems, migration specialists from ArcBees can be hired to upgrade your project with ease. Contact [ArcBees.com](http://arcbees.com) for a consultation, to clarify your needs and options, and obtain a quote for the service.

### Community Help
Never hesitate to ask for help in the [GWTP Community](https://plus.google.com/communities/113139554133824081251) if you get stuck!

[boot]: gwtp/basicfeatures/Bootstrapping-or-Application-Initialization.html "Bootstrapping or Application Initialization"
[mc]: gwtp/resources/index.html "Maven Configuration"
[pm]: gwtp/features/PlaceManager.html "PlaceManager"
