# Contributing
To update a documentation page, you can just edit the corresponding file in the `markdown` directory.

To add a new documentation page, you have to:

* Create a `.md` file under the corresponding project
* Edit the `config.xml` file of the project to add an entry of your new page
* Update the `resources/toc.md` file to add your new page in the general table of content

##Reference
* Markdown processor: https://github.com/sirthias/pegdown

##Building
* simply run: `mvn clean install`
* after that you will find the generated documentation in `target/generated-site/`.

###Running locally
Run the site locally for easy visual testing

* Run: `mvn install`
* Run: `mvn jetty:run`
* Goto: http://127.0.0.1:8080