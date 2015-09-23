# Drag and drop plugin for GWT

## Introduction
The GQuery draggable and droppable plugin lets you make any DOM element draggable and/or droppable. In addition, it offers an API which lets you add drag-and-drop support to your GWT widgets, including also the new data presentation widgets (aka cell widgets).

This article explains how to use this API in your GWT project.

## Configuration {configuration}
GQuery provides a bundle containing everything needed to use GQuery and its draggable and droppable plugin.

If you don't use maven, you will have to download the jar file related to the gwt version you are using:

[gquery-dnd-bundle 1.0.1](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gquery-dnd-bundle-1.0.1.jar) jar file for GWT 2.1.0
[gquery-dnd-bundle-1.0.2](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gquery-dnd-bundle-1.0.2.jar) jar file for GWT 2.1.X with X > 0
[gquery-dnd-bundle-1.0.3](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gquery-dnd-bundle-1.0.3.jar) jar file for GWT 2.2
[gquery-dnd-bundle-1.0.4](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gquery-dnd-bundle-1.0.4.jar) jar file for GWT 2.3
[gquery-dnd-bundle-1.0.5](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gquery-dnd-bundle-1.0.5.jar) jar file for GWT 2.4
[gquery-dnd-bundle-1.0.6](http://code.google.com/p/gwtquery-plugins/downloads/detail?name=gquery-dnd-bundle-1.0.6.jar) jar file for GWT 2.5 and above
and include it in the classpath of you project.

If you use maven, you have to add these blocks to your pom.xml

```xml
<repositories>
    <repository>
        <id>plugins</id>
        <url>http://gwtquery-plugins.googlecode.com/svn/mavenrepo</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.googlecode.gwtquery.bundles</groupId>
        <artifactId>gquery-dnd-bundle</artifactId>
        <!-- for gwt 2.1 -->
        <version>1.0.2</version>
        <!-- for gwt 2.2 -->
        <version>1.0.3</version>
        <!-- for gwt 2.3 -->
        <version>1.0.4</version>
        <!-- for gwt 2.4 -->
        <version>1.0.5</version>
        <!-- for gwt 2.4 -->
        <version>1.0.6</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Then you need inherit the plugin in your Module.gwt.xml file:

```xml
<inherits name='gwtquery.plugins.droppable.Droppable'/>
```

Please note that if you plan to add drag-and-drop support for CellTree and/or CellBrowser, the droppable plugins needs to override few gwt classes to open the API of CellTree and CellBrowser widget. So ensure that the gquery-dnd-bundle.jar are set before the gwt jar files in your compilation classpath.

## A note about touch events support {touch-events}
The GQuery draggable and droppable plugin can handle mobile device and react on touch events instead of mouse events only if it is used with GQuery 1.2.0 or later !

You can download the last stable version of GQuery [here]({{#gquery.doc.url.download}}) and add it in your compilation classpath before the gquery-dnd-bundle.jar.

## Add drag support to your widgets {drag-support}
### The DraggableWidget class
To make your widget draggable, you have two simple possibilities :

1. wrap your widget in a DraggableWidgetinstance
1. have your widget class extend the DraggableWidgetclass.

Example with wrapping:

```java
Label label = new Label("I want to become a draggable widget !!");
//wrap the original widget in a DraggableWidget
DraggableWidget<Label> draggableLabel = new DraggableWidget<Label>(label);
//configure the drag behavior (see next paragraph)
draggableLabel.setDraggingCursor(Cursor.MOVE);
draggableLabel.setDraggingOpacity((float)0.8);

...

//add the draggableLabel to the DOM
RootPanel.get().add(draggableLabel);

//if you want to do something in the original label, just call getOriginalWidget method
draggableLabel.getOriginalWidget().setText("I'm now draggable");
```

Example with subclassing :

```java
public class DraggableLabel extends DraggableWidget<Label> {

  public DraggableLabel() {
    //as DraggableWidget is a composite call initWidget() method to setup your widget
    initWidget(new Label("I'm a draggable label"));
    
    //configure the drag behavior (see next paragraphs)
    setDraggingCursor(Cursor.MOVE);
    setDraggingOpacity((float)0.8); 
  }

  public void setText(String text){
    getOriginalWidget().setText(text);
  }
}
```

### Configure the dragging behavior
The API of the DraggableWidget object proposes a set of methods allowing you to configure the drag behavior of your widget. You can find the list of all options and their related explanation in this table.

An example is always more understandable than a long text of explanation :

```java
Label label = new Label("I want to be a droppable widget !!");
//wrap the original widget in a DraggableWidget
DraggableWidget<Label> draggableLabel = new DraggableWidget<Label>(label);

//configure the drag behavior
//use a clone of the helper as dragging display
draggableLabel.useOriginalWidgetAsHelper();
//change the cursor during the drag
draggableLabel.setDraggingCursor(Cursor.MOVE);
//set the opacity of the dragging display
draggableLabel.setDraggingOpacity((float)0.8);
// the widget can only be dragged on horizontal axis
draggableLabel.setAxis(AxisOption.Y_AXIS);
//revert the dragging display on its original position is not drop occured
draggableLabel.setRevert(RevertOption.ON_INVALID_DROP);
//snap the dragging display to a 50x50px grid
draggableLabel.setGrid(new int[]{50,50});
...
```

Don't hesitate to try our ["options in action"][options-in-action] example to play around with most of the options of the DraggableWidget.

### The drag events
The drag-and-drop plugin fires different events during the drag operation that you can handle to implement your custom code.

Here's the list of drag events :

* gwtquery.plugins.draggable.client.events.BeforeDragStartEvent : fired before the before the initialization of the drag operation
* gwtquery.plugins.draggable.client.events.DragStartEvent : fired when the drag starts
* gwtquery.plugins.draggable.client.events.DragEvent : fired when the widget is being dragged
* gwtquery.plugins.draggable.client.events.DragStopEvent : fired when the drag operation stops

To handle an event, just register an associated event handler in your DraggableWidget :

```java
Label label = new Label("I want to be a droppable widget !!");
//wrap the original widget in a DraggableWidget
DraggableWidget<Label> draggableLabel = new DraggableWidget<Label>(label);

draggableLabel.addDragStartHandler(new DragStartEventHandler() {
  
  public void onDragStart(DragStartEvent event) {
    //retrieve the widget that is being dragged
    DraggableWidget<Label> draggableWidget = (DraggableWidget<Label>)event.getDraggableWidget();
    draggableWidget.getOriginalWidget().setText("I'm dragging");
    
  }
});

draggableLabel.addDragStopHandler(new DragStopEventHandler() {
  
  public void onDragStop(DragStopEvent event) {
    //retrieve the widget that was being dragged
    DraggableWidget<Label> draggableWidget = (DraggableWidget<Label>)event.getDraggableWidget();
    draggableWidget.getOriginalWidget().setText("I'm not dragging");
  }
});
```

All drag events provide useful methods to retrieve the dragging widget and the associated dragging display (called the helper) with the exception of the BeforeDragStartEventwhere the helper is null because the drag operation is not initialized yet.

## Add drop support to your widgets {drop-support}
### The DroppableWidget class
In the same way, to make your widget droppable, you have two possibilities :

1. wrap your widget in a DroppableWidgetinstance
1. extend you widget class with the DroppableWidgetclass

Example with wrapping:

```java
Label label = new Label("I want to be a droppable widget !!");
//wrap the original widget in a DroppableWidget
DroppableWidget<Label> droppableLabel = new DroppableWidget<Label>(label);
//configure the drop behaviour (see next paragraph)
droppableLabel.setTolerance(DroppableTolerance.POINTER);

//add the droppableLabel to the DOM
RootPanel.get().add(droppableLabel);

//if you want to do something in the original label, just call getOriginalWidget method
droppableLabel.getOriginalWidget().setText("I'm now droppable");
```

Example with subclassing :

```java
 public class DroppableLabel extends DroppableWidget<Label> {

  public DroppableLabel() {
    //as DroppableWidget is a Composite, call initWidget() method to setup your widget
    initWidget(new Label("I'm a droppable label"));
    
    //configure the drop behavior (see next chapters)
    setTolerance(DroppableTolerance.POINTER);
   
  }
  
  public void setText(String text){
    getOriginalWidget().setText(text);
  }
}
```

### Configure the dropping behavior
The DroppableWidget API offers a set of methods which allow you to configure the drop behavior of the widget. All available options are listed in the following [table]({{#gquery.doc.url.droppable_options}}).

Please refer to our ["options in action"][options-in-action] example to play around with most of the options of the DroppableWidget.

### The drop events
Different events are fired on the DroppableWidget during the drag-and-drop operation :

* gwtquery.plugins.droppable.client.events.DropEvent : fired when a acceptable draggable (see next paragraph to know what an "acceptable draggable" is) is dropped on the DroppableWidget
* gwtquery.plugins.droppable.client.events.OutDroppableEvent : fired when an acceptable draggable is being dragged out of the DroppableWidget
* gwtquery.plugins.droppable.client.events.OverDroppableEvent : fired when an acceptable draggable is being dragged over the DroppableWidget
* gwtquery.plugins.droppable.client.events.ActivateDroppableEvent : fired when the DroppableWidget is activated (i.e. when an acceptable draggable start being dragged)
* gwtquery.plugins.droppable.client.events.DeactivateDroppableEvent : fired when the DroppableWidget is deactivated (i.e. when an acceptable draggable stops being dragged)

To handle an event, just register an associated event handler in your DroppableWidget :

```java
droppableLabel.addDropHandler(new DropEventHandler() {
  
    public void onDrop(DropEvent event) {
        // retrieve the droppable widget
        DroppableWidget<Label> droppableLabel = (DroppableWidget<Label>)event.getDroppableWidget();
        // retrieve the dropped draggable widget (we assume it is a draggable label)
        DraggableWidget<Label> draggableLabel = (DraggableWidget<Label>)event.getDraggableWidget();
        
        droppableLabel.getOriginalWidget().setText("I ate : "+draggableLabel.getOriginalWidget().getText());
        
        //remove the draggabeLable
        draggableLabel.removeFromParent();
    }
});
```

### Scope and Accept options
We will define what we mean by an "acceptable draggable".

>A draggable element is accepted by a droppable if and only if they have the same scope value and if true is returned when we call the AcceptFunction of the droppable by passing the draggable. When we say that a draggable is accepted by a droppable, it means that draggable can be dropped on that droppable.
The scope options is a key used to group draggable and droppable items.

The Accept options is a kind of filter apply to group of draggable with the same scope as the droppable. It can be a css selector. All draggable matching this selector will be accepted :

```java
// all DraggableWidget with "draggableLabel" as css class name will be accepted
droppableLabel.setAccept(".draggableLabel");
```

or an AcceptFunction object

```java
droppableLabel.setAccept(new AcceptFunction() {
  
    public boolean acceptDrop(DragAndDropContext context) {
        DraggableWidget<?> draggabelWidget = context.getDraggableWidget();
        //accept only draggable Label
        return draggabelWidget.getOriginalWidget() instanceof Label;
    }

});
```

By default, the scope is defined to "default" and a droppable accepts all draggables with the same scope.

The [checkers example][checkers-example] use a AcceptFunction to authorize drop of a piece only in the authorized squares.

## The cell widgets {cell-widgets}
Since its 2.1 release, GWT proposes a new kind of widget, the data presentation widgets and introduces the concept of cells. The GWTQuery drag-and-drop plugins offers an API allowing to make any cell draggable and/or droppable.

### DragAndDropCellList class
To add drag-and-drop support in a CellList, you have simply to use a DragAndDropCellList widget.

```java
// Create a ConcactCell (normal cell displaying info a a contact)
ContactCell contactCell = new ContactCell(Images.INSTANCE.contact());

// Create a drag and drop cell list
DragAndDropCellList<ContactInfo> cellList = new DragAndDropCellList<ContactInfo>(
    contactCell, ContactDatabase.ContactInfo.KEY_PROVIDER);

// The cell of this CellList are only draggable
cellList.setCellDraggableOnly();

// setup the drag operation
DraggableOptions options = new DraggableOptions();
// use a clone of the original cell as drag helper
options.setHelper(HelperType.CLONE);
// set the opacity of the drag helper
options.setOpacity((float) 0.9);
// append the drag helper to the body element
options.setAppendTo("body");
// configure the drag operations of the cell list with this options
cellList.setDraggableOptions(options);
```

Look at the [contact cell example][contact-cell], to see a more concrete example in action.

### DragAndDropCellTree, DragAndDropCellBrowser and DragAndDropNodeInfo classes
In the same way, to add drag-and-drop in a CellTree (or a CellBrowser), just use a DragAndDropCellTree (or a DragAndDropCellBrowser) and use DragAndDropNodeInfo to define your tree model :

```java
   DragAndDropCellTree cellTree = new DragAndDropCellTree(
        new MemberTreeViewModel(), null, DroppableCellTreeResource.INSTANCE);
   ...
     
   public class MemberTreeViewModel implements TreeViewModel {
    ...

    public <T> NodeInfo<?> getNodeInfo(T value) {

     if (value == null) {
      // root node
      DragAndDropNodeInfo<Permission> permissionNodeInfo = new DragAndDropNodeInfo<Permission>(permissionDataProvider, permissionCell);
      
      // permission cell are only droppable
      permissionNodeInfo.setCellDroppableOnly();

      // setup drop operation
      DroppableOptions options = permissionNodeInfo.getDroppableOptions();
      options.setDroppableHoverClass(Resource.INSTANCE.css().droppableHover());
      // use a DroppableFunction to define when happens when a drop occurs. We can also add a DropHandler directly in the tree.
      options.setOnDrop(new DroppableFunction() {

        public void f(DragAndDropContext context) {
          //retrieve the Member data that was dropped
          MemberInfo droppedMember = context.getDraggableData();
          //retrieve the Permission where the member was dropped
          Permission dropPermission = context.getDroppableData();

          MemberDatabase.get().permissionChange(droppedMember, dropPermission);

        }
      });
      
      return permissionNodeInfo;

    } else if (value instanceof Permission) {
      // Permission Tree node. 
      Permission p = (Permission) value;
      
      //retrieve all members having this permission
      ListDataProvider<MemberInfo> dataProvider = MemberDatabase.get()
          .getDataProvider(p);

      DragAndDropNodeInfo<MemberInfo> memberNodeInfo = new DragAndDropNodeInfo<MemberInfo>(dataProvider, memberCell, new SingleSelectionModel<MemberInfo>(),null);

      // member cell are only draggable
      memberNodeInfo.setCellDraggableOnly(); 

      // setup the drag operation
      DraggableOptions options = memberNodeInfo.getDraggableOptions();
      // opacity of the drag helper
      options.setOpacity((float) 0.9);
      // cursor during the drag operation
      options.setCursor(Cursor.MOVE);
      ...

      return memberNodeInfo;
    }

    String type = value.getClass().getName();
    throw new IllegalArgumentException("Unsupported object type: " + type);
   }
  }  
```

Check out the [permission manager example][permission-manager] and the [GFinder example][gfinder] for more concrete examples.

Please note that the droppable plugins needs to override few gwt classes to open the API of CellTree and CellBrowser widget. So ensure that the gquery-dnd-bundle.jar are set before the gwt jar files in your compilation classpath.

### DragAndDropCellTable, DragAndDropDataGrid and DragAndDropColumn classes
To add drag-and-drop support in a CellTable :

* Use a DragAndDropCellTable instead of a CellTable
* Extends DragAndDropColumn instead of Column when you defines your table columns.

```java
// Create a DragAndDropCellTable.
DragAndDropCellTable<ContactInfo> table = new DragAndDropCellTable<ContactInfo>();

// Name.
DragAndDropColumn<ContactInfo, String> nameColumn = new DragAndDropColumn<ContactInfo, String>(
    new TextCell()) {
  @Override
  public String getValue(ContactInfo object) {
    return object.getLastName();
  }
};
//cells are only draggable
nameColumn.setCellDraggableOnly();
DraggableOptions nameOptions = nameColumn.getDraggableOptions();
// opacity of the helper
nameOptions.setOpacity((float) 0.8);
// set the revert option
nameOptions.setRevert(RevertOption.ON_INVALID_DROP);
table.addColumn(nameColumn,"Name");


// Address.
DragAndDropColumn<ContactInfo, String> addressColumn = new DragAndDropColumn<ContactInfo, String>(
    new TextCell()) {
  @Override
  public String getValue(ContactInfo object) {
    return object.getAddress();
  }
};
//setup drag operation
DraggableOptions addressDragOptions = addressColumn.getDraggableOptions();
// opacity of the helper
addressDragOptions.setOpacity((float) 0.8);
// only draggable on the vertical axis
addressDragOptions.setAxis(AxisOption.Y_AXIS);
// set the scope for the drag and drop operation
addressDragOptions.setScope("addressScope");

//setup the drop operation
DroppableOptions addressDropOptions = addressColumn.getDroppableOptions();
// set the scope for the drag and drop operation, btw the address cell accepts only an other address cell
addressDropOptions.setScope("addressScope");

//as the drop concerns only cells from this column, use a callback function instead of handle DropEvent from the CellTable
addressDropOptions.setOnDrop(new DroppableFunction() {
  
  public void f(DragAndDropContext context) {
    ContactInfo droppedContact = context.getDraggableData();
    ContactInfo dropTargetContact = context.getDroppableData();
    
    dropTargetContact.setAddress(droppedContact.getAddress());
    
  }
});

table.addColumn(nameColumn,"Address");
```

See this [example](http://gwtquery-plugins.googlecode.com/svn/branches/droppable_1_0/demo/CellTableSample/CellTableSample.html).

### DraggableOptions and DroppableOptions
As you can see in the examples above, we use [DraggableOptions]({{#gquery.doc.url.draggable_options}}) and [DroppableOptions]({{#gquery.doc.url.droppable_options}}) objects in order to configure respectively the drag and the drop behavior.

## Events {events}
All DragAndDrop cell widgets propose a set of methods allowing the registration of handlers for all drag and drop events.

```java
DragAndDropCellList<ContactInfo> cellList = new DragAndDropCellList<ContactInfo>(
    contactCell, ContactDatabase.ContactInfo.KEY_PROVIDER);

cellList.addDragStartHandler(new DragStartEventHandler() {
  
  public void onDragStart(DragStartEvent event) {
    //called when a cell of the cellList start to drag
    ...
    
  }
});

cellList.addDropHandler(new DropEventHandler() {
  
  public void onDrop(DropEvent event) {
    //called when a drop occurs on a cell of this CelList
    ...
  }
});
```

All drag events provide useful methods to retrieve the data associated with the cell that being dragged or playing the role of the drop target.

```java
public void onDrop(DropEvent event) {
  //assume that cell is displaying  a ContactInfo object
  ContactInfo draggedContact = event.getDraggableData();
  //assume that cell where the drop occurs is displaying Permission object
  Permission dropPermission = event.getDroppableData();
  
  draggedContact.setPermission(dropPermission);
}
```

[options-in-action]: http://gwtquery-plugins.googlecode.com/svn/branches/droppable_1_0/demo/TestOptionsSample/TestOptionsSample.html
[checkers-example]: http://gwtquery-plugins.googlecode.com/svn/branches/droppable_1_0/demo/DraughtsSample/DraughtsSample.html
[contact-cell]: http://gwtquery-plugins.googlecode.com/svn/branches/droppable_1_0/demo/ContactCellSample/ContactCellSample.html
[permission-manager]: http://gwtquery-plugins.googlecode.com/svn/branches/droppable_1_0/demo/PermissionManagerSample/PermissionManagerSample.html
[gfinder]: http://gwtquery-plugins.googlecode.com/svn/branches/droppable_1_0/demo/GFinderSample/GFinderSample.html