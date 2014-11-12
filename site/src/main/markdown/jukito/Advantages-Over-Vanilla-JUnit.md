#Advantages over vanilla JUnit

Compared to vanilla JUnit tests, Jukito has a number of advantages:

* Greatly reduces boilerplate via automocking, leading to easier to read tests
* Leads to tests that are more resilient to API changes in the tested objects
* Fields annotated with `@Inject` are automatically injected, no risk to forget them
* Makes it easy to wire objects together, so you can scale a unit test to a partial integration test