# Contributing
To update a documentation page, you can just edit the corresponding file in the `markdown` directory.

To add a new documentation page, you have to:

* Create a `.md` file under the corresponding project
* Edit the `config.xml` file of the project to add an entry of your new page
* Edit the `variables.properties` file under resources to update or create new variables

##Reference
* Markdown processor: https://github.com/sirthias/pegdown

##Building
* simply run: `mvn clean install`
* after that you will find the generated documentation in `target/generated-site/`.

###Running locally
Run the site locally for easy visual testing

* Run: `mvn install`
* Run: `mvn jetty:run`
* Run: `grunt build`
* Goto: http://127.0.0.1:8080

_You can also run `grunt` to always watch and rebuild your assets (CSS / JS / images)_
