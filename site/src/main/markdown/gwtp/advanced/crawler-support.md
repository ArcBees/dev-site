# Crawler Support
Since version 0.6, GWTP makes it easy for your website to be crawlable by search engines that follows Google's [AJAX crawling](https://developers.google.com/webmasters/ajax-crawling/) specification, so they can be well-indexed by search engines.

##Before you begin...
Make sure you read [Google's specification](https://developers.google.com/webmasters/ajax-crawling/). In particular, your page's name tokens should all start with a bang (i.e. `http://myapp.myself.com#!homePage` rather than `http://myapp.myself.com#homePage`). Also, you will probably need to add fragment tag to your HTML page.

```html
<meta name="fragment" content="!">
```

Once you've read the documentation, take a look at http://gwtp-sample-mobile.appspot.com/ and [try using](http://gwtp-sample-mobile.appspot.com/?_escaped_fragment_=!homePage/!productList;type=all) `_escaped_fragment_` URLs (they may not work all the time since the application is not using a resident AppEngine instance). This application is the deployed version of [gwtp-sample-mobile](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-mobile).

##Crawl service
In order to make your app crawlable by a search engine you will first need a service that is able to render your page, running any javascript it contains. GWTP makes it trivial to setup such a service on [AppEngine](https://cloud.google.com/appengine/) using the `gwtp-crawler-service` project. Once the service is setup it should respond to requests of that form:

```
http://crawlservice.appspot.com/?key=123456&url=http://google.com
```

by rendering the page `http://google.com` and returning the result as text.

Note that the above URL works since `crawlservice.appspot.com` is the deployed version of the corresponding GWTP sample. You can try to use this particular service for debugging, but since it's a free [AppEngine](https://cloud.google.com/appengine/) application it will likely not work well. (See _Maximizing efficiency_ below.)

###Using `gwtp-crawler-service`
It's easy to roll out your own personalized crawl service. Simply take a look at [gwtp-sample-crawler-service](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-crawler-service). In particular you want to look at [CrawlerModule.java](https://github.com/ArcBees/GWTP-Samples/blob/master/gwtp-samples/gwtp-sample-crawler-service/src/main/java/com/gwtplatform/samples/crawlerservice/server/CrawlerModule.java) which simply sets-up your service's key and installs GWTP's `CrawlServiceModule`. Your version should look somewhat like this:

```java
public class MyCrawlerModule extends AbstractModule {
    @Override
    protected void configure() {
        bindConstant().annotatedWith(ServiceKey.class).to("123456");
        install(new CrawlServiceModule());
    }
}
```

Naturally you'll have to setup your [AppEngine](https://cloud.google.com/appengine/) application name and version in `appengine-web.xml`, but that's about all you need to do. Just deploy and you're done!

(Careful: in the code snippet above you need to bind `com.gwtplatform.crawlerservice.server.ServiceKey` not `com.gwtplatform.crawler.server.ServiceKey`.)

###Crawler service dependencies
Even though creating your own service is simple, a number of things go on behind the scenes, so you need a few dependencies. If you're using Maven it's really simple, just use this in your POM:

```xml
<dependencies>
    <dependency>
        <groupId>com.gwtplatform</groupId>
        <artifactId>gwtp-crawler-service</artifactId>
        <version>${gwtp.version}</version>
    </dependency>
</dependencies>
```

Maven's transitive dependencies will do the rest. If you don't have Maven here's the dependency tree:

```
+- com.gwtplatform:gwtp-build-tools:jar:1.4:provided
+- javax.servlet:servlet-api:jar:2.5:compile
+- javax.inject:javax.inject:jar:1:compile
+- net.sourceforge.htmlunit:htmlunit:jar:2.14:compile
|  +- xalan:xalan:jar:2.7.1:compile
|  |  \- xalan:serializer:jar:2.7.1:compile
|  +- commons-collections:commons-collections:jar:3.2.1:compile
|  +- org.apache.commons:commons-lang3:jar:3.2.1:compile
|  +- org.apache.httpcomponents:httpclient:jar:4.3.2:compile
|  |  \- org.apache.httpcomponents:httpcore:jar:4.3.1:compile
|  +- org.apache.httpcomponents:httpmime:jar:4.3.2:compile
|  +- commons-codec:commons-codec:jar:1.9:compile
|  +- net.sourceforge.htmlunit:htmlunit-core-js:jar:2.14:compile
|  +- xerces:xercesImpl:jar:2.11.0:compile
|  |  \- xml-apis:xml-apis:jar:1.4.01:compile
|  +- net.sourceforge.nekohtml:nekohtml:jar:1.9.20:compile
|  +- net.sourceforge.cssparser:cssparser:jar:0.9.13:compile
|  |  \- org.w3c.css:sac:jar:1.3:compile
|  +- commons-io:commons-io:jar:2.4:compile
|  +- commons-logging:commons-logging:jar:1.1.3:compile
|  \- org.eclipse.jetty:jetty-websocket:jar:8.1.14.v20131031:compile
|     +- org.eclipse.jetty:jetty-util:jar:8.1.14.v20131031:compile
|     +- org.eclipse.jetty:jetty-io:jar:8.1.14.v20131031:compile
|     \- org.eclipse.jetty:jetty-http:jar:8.1.14.v20131031:compile
+- com.google.appengine:appengine-api-1.0-sdk:jar:1.9.7:compile
+- com.google.inject:guice:jar:3.0:compile
|  \- aopalliance:aopalliance:jar:1.0:compile
+- com.google.inject.extensions:guice-servlet:jar:3.0:compile
+- com.googlecode.objectify:objectify:jar:5.0.2:compile
|  \- com.google.guava:guava:jar:14.0.1:compile
+- javax.persistence:persistence-api:jar:1.0:compile
```

Some of these are likely included in other jars so you'll likely be able to get away with less than this entire set... But needless to say, we highly recommend using Maven, even if you're not using it for your main project. After all, the crawler service is totally independent from your main application.

###Testing the crawler service
Once you've deployed your crawler service, simply test it with that request:

```
http://crawlservice.appspot.com/?key=123456&url=http://myapp.myself.com
```

Using you own service key and your main application's URL. You should get as a result a text version of your page's HTML. If it doesn't work after a few tries, use `http://google.com` instead, then see _Maximizing efficiency_ below.

##Crawl filter
The crawl service is able to render any webpage, but you're interested in using it to render the pages of one (or more) of your GWTP applications. For that purpose, GWTP includes a simple filter that will automatically make use of the crawler service. The filter works on any servlet container, including [AppEngine](https://cloud.google.com/appengine/).

###Setting up the filter

A sample use of the crawl filter is available in [gwtp-hplace-sample](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-mobile). To use the filter, you simply need to bind a couple of things using Guice:

```java
bindConstant().annotatedWith(ServiceKey.class).to("123456");
bindConstant().annotatedWith(ServiceUrl.class).to("http://crawlservice.appspot.com/");
filter("/*").through(CrawlFilter.class);
```

Naturally, use your own service key and url. (Careful here, you need to bind `com.gwtplatform.crawler.server.ServiceKey` not `com.gwtplatform.crawlerservice.server.ServiceKey`.)

###Ensuring HTML pages are not served as static
Since you're running every page through a filter that identifies those `escaped_fragment`, you will need to make sure your HTML pages are not served statically. If you application runs on [AppEngine](https://cloud.google.com/appengine/) this is done by adding this to your `appengine-web.xml`, at the end of the `<static-files>` section:

```xml
<static-files>
    [...]
    <exclude path="**.html" />
</static-files>
```

###Testing the crawl filter
To test your crawl filter, simply deploy your application and access:

```
http://myapp.myself.com?_escaped_fragment_=home
```

Using your real application URL and replacing `home` with the name token of a page you want to render, *without* the bang. You should see a version of the page similar to your real dynamic page. Don't worry if some images are missing, the crawler is only interested in the text. If you run into problems, see _Maximizing efficiency_ below.

##Maximizing efficiency
You may notice that your `escaped_fragment` pages don't always render correctly, something even giving completely blank pages. This is often due to [AppEngine](https://cloud.google.com/appengine/)'s stringent constraints on maximal request durations and timeouts for external url fetches. GWTP filter's retry mechanism combined to caching on the crawler service usually get rid of the problem, but for complex pages it might not be enough. In this case you have a number of options:

* Ensure [AppEngine](https://cloud.google.com/appengine/)'s _always on_ feature is enabled on your crawler service and on your app (if it's using [AppEngine](https://cloud.google.com/appengine/)). *Highly recommended!*
* Pages on the crawler service are cached for 15 minutes by default, you can increase this via `bindConstant().annotatedWith(CachedPageTimeoutSec.class).to(3600)` or more if your content changes infrequently.
* With very long cache times, you can prime the cache by visiting the `escaped_fragment` versions manually, so that the robot sees a cached page when he visits.
