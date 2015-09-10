# GwtChosen: UiBinder, ChosenListBox and events...

This example shows you how to use `ChosenListBox` widget. you can use in collaboration with UiBinder:

```xml
<chzn:ChosenListBox ui:field="countriesChosen"
    allowSingleDeselect="true">
    <g:item value=""></g:item>
    <g:item value="United States">United States</g:item>
    <g:item value="United Kingdom">United Kingdom</g:item>
    ...
</chzn:ChosenListBox>
```

or directly in your java class:

```java
ChosenListBox teamChosen = new ChosenListBox(true); //multiple
// init options for teamchosen
for (String team : teams) {
    teamChosen.addItem(team);
}

teamChosen.setPlaceholderText("Choose your favourite teams");
```

$[gwtchosen-sample-9]