# Tabbed Presenters

A particularly useful type of nested presenter is one in which the parent presenter display a list of links and clicking on any link causes a child presenter to appear.

GWTP makes it easy to implement such presenters while maintaining a loosely coupled architecture. To use this feature the parent presenter needs to inherit from `TabContainerPresenter` and you will have to use the `@RequestTabs` and `@TabInfo` annotations. For an example, look at the [tabsample](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-tab) in the `samples` repository.

##Reference
[Tabbed Sample](https://github.com/ArcBees/GWTP-Samples/tree/master/gwtp-samples/gwtp-sample-tab)
[Tabbed Sample Demo](http://gwtp-sample-tab.appspot.com/)
