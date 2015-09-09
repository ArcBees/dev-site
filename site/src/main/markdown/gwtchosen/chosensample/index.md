# Chosen for GWT

## Introduction

Chosen is a javascript plugin (for jQuery and Prototype) that makes long, unwieldy select boxes much more user-friendly. GWT-Chosen is a port of the jquery version of Chosen for Google Web Toolkit. It is not a wrapper but a complete rewrite using the GWT standards. It is available as a GQuery plugin or as a widget.

## How to install {setup}
GWT-Chosen depends on [GQuery](http://dev.arcbees.com/gquery/). You need at least GQuery 1.2. Please follow the instructions to download GQuery jar file and put it in your classpath.

### With Maven
Add the following block to your pom.xml

```xml
<build>
    <dependencies>
        <dependency>
            <groupId>com.arcbees</groupId>
            <artifactId>gwtchosen</artifactId>
            <version>3.0-SNAPSHOT</version>
        </dependency>
    </dependencies>
</build>
```

Then you need to inherit the plugin in your Module.gwt.xml file:

```xml
<inherits name='com.arcbees.chosen.Chosen'/>
```

### Without Maven
Download the last stable jar file and put it in your classpath.

## What is GWT-Chosen {what-is-gwtchosen}

$[gwtchosen-sample-1]

With one line of java code :

```java
import static com.google.gwt.query.client.GQuery.$;
import static com.arcbees.chosen.client.Chosen.Chosen;
...
$(".selectToEnHance").as(Chosen).chosen();
```

## Working with Widgets and UiBinder {working-with-widgets}

In addition to the GQuery way to call the plugin, you have the possiblility to use the ChosenListBox widget either in your uibinder template :

```html
<!DOCTYPE ui:UiBinder SYSTEM "http://dl.google.com/gwt/DTD/xhtml.ent">
<ui:UiBinder ...
    xmlns:chzn="urn:import:com.arcbees.chosen.client.gwt">
    <chzn:ChosenListBox maxSelectedOptions="5" placeholderText="Choose your country...">
            <g:item value=""></g:item>
            <g:item value="United States">United States</g:item>
            <g:item value="United Kingdom">United Kingdom</g:item>
            ...
    </chzn:ChosenListBox>
```

or directly in your java code :

```java
import com.arcbees.chosen.client.gwt.ChosenListBox;
...

ChosenListBox chzn = new ChosenListBox(true);
chzn.addItem("United States");
chzn.addItem("United Kingdom");
...
chzn.setMaxSelectedOptions(5);
chzn.setPlaceholderText("Choose your country...");
```

Check this complete example for more code

## Optional Group Support {optgroup-support}

$[gwtchosen-sample-2]

ChosenListBox supports the optgroups as well:

```java
//add a optgroup
chzn.addGroup("NFC EAST");
//insert a optgroup in a specific position
chzn.insertGroup("NFC EAST", 2);
//add an item to the last optgroup
chzn.addItemToGroup("Dallas Cowboys");
//add an item at the end to the second optgroup
chzn.addItemToGroup("Chicago Bears", 1);
//insert the item in the 4th position of the second optgroup
chzn.insertItemToGroup("Dallas Cowboys", 1, 3);
```

## Selected and Disabled Support {selected-disabled-support}
Chosen automatically highlights selected options and removes disabled options.

$[gwtchosen-sample-3]

## Advanced Chosen ListBox Options {advanced-clb-options}

$[gwtchosen-sample-4]

Java:

```java
ChosenListBox chzn = new ChosenListBox();
chzn.setWidth( "350px" );
chzn.setPlaceholderText( "Navigate to..." );
chzn.setTabIndex( 9 );
chzn.addItem( "" );
chzn.addStyledItem( "Home", "home", null );
chzn.addGroup( "ABOUT US" ); // group 0
chzn.addStyledItemToGroup( "Press Releases", "press", null, 0 );
chzn.addStyledItemToGroup( "Contact Us", "about", null, 0 );
chzn.addGroup( "PRODUCTS" ); // group 1
chzn.addStyledItemToGroup( "Tera-Magic", "tm", null, 0, 1 );
chzn.addStyledItemToGroup( "Tera-Magic Pro", "tmpro", null, 1, 1 );
// Item will be custom-styled and inserted before "Tera-Magic Pro"
chzn.insertStyledItemToGroup( "Tera-Magic Standard", "tmstd", "youAreHere", 1, 1, 1 );
```

CSS:

```css
.youAreHere {
    border: dashed 1px lightGrey;
    border-left: solid 3px orange;
}
```

## Chosen Options {chosen-options}

The Chosen plugin can be configured by using an instance of the class ChosenOptions.

```java
ChosenOptions options = new ChosenOptions();
options.setAllowSingleDeselect(true);
//set the options you want...
$("select").as(Chosen).chosen(options);

//to retrieve the options later
ChosenOptions options = $("#myselect").as(Chosen).options();
```

If you are using the ChosenListBoxwidget, it offers needed methods for configuring it.

```java
ChosenListBox chzn = new ChosenListBox();
chzn.setAllowSingleDeselect(true);
//...
```

### Default Text Support
Chosen automatically sets the default field text ("Choose a country...") by reading the select element's data-placeholder value. If no data-placeholder value is present, it will default to "Select Some Option" or "Select Some Options" depending on whether the select is single or multiple. You can change these default texts via the options.

If you have access to the select html element:

```html
<select data-placeholder="Choose a country..." style="width:350px;" multiple class="chzn-select>
```

or via the ChosenOptions object:

```java
ChosenOptions options = new ChosenOptions();
options.setPlaceholderTextSingle("Choose a country...");
options.setPlaceholderTextMultiple("Choose countries...");

//using the widget
ChosenListBox chzn = new ChosenListBox();
chzn.setPlaceholderTextSingle("Choose a country...");
```

Note: on single selects, the first element is assumed to be selected by the browser. To take advantage of the default text support, you will need to include a blank option as the first element of your select list.

### List of options

The table below lists the different properties of the ChosenOptions. Each property is accessible via getter/setter methods.

$[gwtchosen-sample-5]

## Events {events}

$[gwtchosen-sample-6]

## Updating the component dynamically {updating-components}

If you need to update the options in your select field and want Chosen to pick up the changes, you'll need to prevent the component in order to re-build itself based on the updated content.

with GQuery:

```java
//add an option
$("#mySelect").append($("<option></option>").attr("value","myNewOption").text("My new option"));
$("#mySelect").as(Chosen).update();
```

with the ChosenListBox widget:

```java
//let's assume that myChosenListBox is an instance of ChosenListBox
myChosenListBox.addItem("My new option","myNewOption");
myChosenListBox.update();
```

$[gwtchosen-sample-7]

## Custom filtering {custom-filtering}

Sometimes it's useful to implement the filtering behavior of the component to filter for example the list of options on the server side. The `resultFilter` option allows you to give your own instance of a `ResultFilter` object that will take care to filter the list of options. Once the filtering is done, don't forget to call the method `ChosenImpl.rebuildResultItems()` to warn the chosen component that the filtering is done.

Example:

```java
public class ServerSideResultFilter implements ResultsFilter {
    private boolean initialized;
    private final TagProxy tagProxy;

    public ServerSideResultFilter(TagProxy tagProxy) {
        this.tagProxy = tagProxy;
    }

    @Override
    public void filter(final String searchText, final ChosenImpl chosen, boolean isShowing) {
        if (isShowing &amp;&amp; initialized) {
            return; // do nothing, keep the last results
        }
        initialized = true;

        tagProxy.filterTags(searchText, new MethodCallback&lt;List&lt;Tag>>() {
            @Override
            public void onSuccess(Method method, List&lt;Tag> tags) {
                List&lt;SelectItem> selectItems = chosen.getSelectItems();
                selectItems.clear();

                for (Tag tag : tags) {
                    OptionItem optionItem = new OptionItem();
                    optionItem.setHtml("<div>" + tag.getLabel() +"<span class=\"count\">" + tag.getCount() +"</span></div>");
                    optionItem.setText(tag.getLabel());
                    optionItem.setValue(tag.getLabel());
                    optionItem.setArrayIndex(tag.getId());
                    optionItem.setOptionsIndex(tag.getId());
                    optionItem.setDomId("tagOption_" + tag.getId());

                    selectItems.add(optionItem);
                }

                chosen.rebuildResultItems();
            }
        });
    }
}

//...

ChosenOptions options = new ChosenOptions();
options.setResultFilter(new ServerSideResultFilter(tagProxy));
ChosenListBox chzn = new ChosenListBox(true, options);
```

Example: Start typing some characters to see the list of items

$[gwtchosen-sample-8]

## Browsers support {browsers-support}

All modern browsers are supported (Firefox, Chrome, Safari and IE9). Legacy support for IE8 is also enabled. On IE6 and IE7, we fall back on normal html select element. To know if the use's browser is supported by the plugin, you can call the `isSupported()` method:

```java
boolean chosenSupport = Chosen.isSupported();
//or
boolean chosenSupport = ChosenListBox.isSupported();
```

## Credits

The initial chosen javascript plugin was built by Harvest. Concept and development by Patrick Filler. Design and CSS by Matthew Lettini. The GWT port of Chosen was built by Julien Dramaix