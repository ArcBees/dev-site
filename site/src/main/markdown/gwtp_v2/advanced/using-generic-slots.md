# Using Generic Slots

Generic slots are useful when you want to extract the exact Presenter contained in a slot by using `getChild()` / `getChildren()` methods. One of the most useful things to use generic slots for is to represent the state of a model. For instance, if you create a slot in the parent then that slot can represent the collection. So removing a child from the slot should also remove it from the collection.

## Example usage:

Here is a basic model:

```java
class Book {
   List<Chapter> chapters;
}
```

To represent that model you can create a BookPresenter with a `Slot<ChapterPresenter>`. Then instead of having to keep track of the chapters and their ChapterPresenters separately the slot itself can manage the collection. So to get the current List of chapters you might have a method in BookPresenter:

```java
List<Chapter> getChapters() {
   List<Chapter> chapters = new ArrayList<>();
   for (ChapterPresenter chapterPresenter: getChildren(CHAPTER_SLOT)) {
        chapters.add(chapterPresenter.getChapter());
   }
   return chapters;
}
```

And so now if you have a Delete Chapter button in your `ChapterView` all you need to call when that button is clicked is `removeFromParentSlot()` and your list of chapters will update automatically.