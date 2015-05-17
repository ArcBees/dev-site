#Overview

Programming web apps in GWT looks easy at first. However, if you just jump in and start coding, building a truly efficient and scalable app proves to be a lot harder. Your best bet is to design your application for scalability from the start, based on a proven architecture that leverages GWT's best features.

**GWTP** (we pronounce it "goo-teepee"), is a collection of components for this kind of architecture. No matter which approach you choose, GWT-optimized compilation will make sure only the features you really use are part of your final code.

At the heart of **GWTP** is a model-view-presenter architecture (MVP). Although this model [has been lauded as one of the best approaches to GWT development](https://youtu.be/PDuhR18-EdM), it is still hard to find an out-of-the-box MVP solution that supports all the requirements of modern web apps. **GWTP** aims to provide such a solution while reducing the amount of code required to reach it.

For example, adding history management and code splitting to your presenter is as simple as adding these lines to your class:

```
@ProxyCodeSplit
@NameToken("myToken")
interface MyProxy extends ProxyPlace<MyPresenter> {}
```

**GWTP** uses GWT's event bus in a clear and efficient way. Events are used to decouple loosely related objects, while program flow between strongly coupled components is kept clear using direct method invocations. The result is an easy to understand and debug application that can continually scale up.

## Goals and Features
The goal of **GWTP** is to offer an *easy-to-use MVP architecture* with *minimal boilerplate*, without compromising GWT's best features. Here are some of the core features of **GWTP**:

* Dependency injection through GIN and Guice;
* Simple and powerful history management mechanism;
* Support for nested presenters;
* Lifecycle events to manage presenters;
* Lazy instantiation for presenters and views;
* Effortless and efficient code splitting;
* Bootstrap tools to make the creation of new GWT applications dead simple.

In addition, **GWTP** offers optional components that let you:

* Easily communicate with server-side code, leveraging gwt-RPC and allowing you to use a powerful  command pattern;
* Simplify the use of web APIs with a library following javax standards (JSR-311);
* Nicely support search-engine crawling on your GWT application;
* Organize your internationalization strings.

##Notes
**GWTP** is a fork of gwt-dispatch and gwt-presenter, many thanks to the original authors of these libraries. If you're used to gwt-presenter, you might like to see how it compares to **GWTP**.
