# dev.arcbees.com
## Arcbees projects documentation

Based on http://www.gwtproject.org 's architecture.

## Contributing
If you want to contribute and add some documentation for one of the projects, see the `site` module instructions.

## Components

* `site`: a markdown -> html converting system
* `webapp`: the actual website to be hosted on app engine
* `uploader`: a simple program that pushes compiled html files to `webapp`'s datastore

## Deployment 1-liner
```
mvn clean install && cd webapp && mvn appengine:update -Pprod && cd ../uploader && touch credentials && ./save_credentials.sh && ./upload.sh credentials
```

When prompted, app id is `docs-site`.

## How to run it on localhost
In terminal #1

```
cd site && mvn clean install
```

In terminal #2

```
cd webapp && mvn clean appengine:devserver
```

In terminal #3 (you have to wait for the action in terminal #2 to be completed)

```
cd uploader && ./upload.sh localhost
```

If you want to see your new changes after that, reexecute `mvn clean install` in terminal #1 and `./upload.sh localhost` in terminal #3

##Thanks to
[![Arcbees.com](http://i.imgur.com/HDf1qfq.png)](http://arcbees.com)

[![Atlassian](http://i.imgur.com/BKkj8Rg.png)](https://www.atlassian.com/)

[![IntelliJ](https://lh6.googleusercontent.com/--QIIJfKrjSk/UJJ6X-UohII/AAAAAAAAAVM/cOW7EjnH778/s800/banner_IDEA.png)](http://www.jetbrains.com/idea/index.html)
