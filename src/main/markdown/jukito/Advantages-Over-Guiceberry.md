[Guiceberry](https://code.google.com/p/guiceberry/) is meant for full integration tests, whereas Jukito was devised for unit tests, keeping in mind the ability to scale all the way to full integration tests. As a result, Jukito leads to simpler and shorter code for unit tests, which should comprise most of the tests in a typical application. In particular:

* Guiceberry requires an external environment class, Jukito lets you place that class right in the test class.
* Guiceberry needs you to manually bind all your mocks, Jukito automatically identifies unbound interfaces and mock them.
* Binding to spies requires some scaffolding in Guiceberry, it's included natively in Jukito.
* Jukito supports injecting into test methods, Guiceberry doesn't.
* Jukito supports multibinding and the `@All` annotation for simple parametrized test cases, Guiceberry doesn't have anything similar.