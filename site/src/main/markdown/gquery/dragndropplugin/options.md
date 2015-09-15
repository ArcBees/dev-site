# Options
## Draggable Options {draggable-options}

To configure the plugin, just set the properties you wish to configure in the DraggableOptionsinstance you pass to the plugin. The table below lists the different properties of the DraggableOptionsobject. Each property is accessible via getter/setter methods.

Property | Type | Description | Default value | Comments
:--- | :--- | :--- | :--- | :---
disabled | boolean | Disables (true) or enables (false) the drag operation. | false | \-
delay | int | Time in milliseconds to define when the drag should start. | 1 | \-
distance | int | Tolerance, in pixels, for when the drag should start. If specified, drag will not start until after mouse is dragged beyond distance. | 0 | \-
appendTo | String | The element selected by the appendTo option will be used as the draggable helper's container during dragging. By default, the helper is appended to the same container as the draggable. | null | \-
axis | AxisOption | Constrains dragging to either the horizontal (AxisOption.X_AXIS) or vertical (AxisOption.Y_AXIS) axis. | AxisOption.NONE | \-
containment | String or int | Constrains dragging to within the bounds of the specified region or element | null | \-
cursor | Cursor | Specify the css cursor to use during the drag operation. | Cursor.AUTO | \-
cursorAt | CursorAt | Moves the dragging helper so the cursor always appears to drag from the same position. | null | \-
grid | int | Snaps the dragging helper to a grid. The array of int defining the dimension of the cell of the snap grid. | null | \-
handle | String | Restricts drag start when the user clicks on the specified element(s). | null | \-
helper | String or Element or GQuery or HelperType | Allows to use a dom element or a clone of the draggable or the draggable itself for dragging display | HelperType.ORIGNAL | By default, the draggable itself will be used. Using clone or dom element will not move the original draggable. These options are useful in combination with the droppable plug-in.
onBeforeDragStart | DragFunction | The callback function called before the initialization of the drag operation | null | \-
onDrag | DragFunction | The callback function called when the drag operation is dragging | null | \-
onDragStart | DragFunction | The callback function called when the drag operation starts | null | \-
onDragStop | DragFunction | The callback function called when the drag operation ends | null | \-
opacity | Float | Specify the opacity of the helper during the drag. | null | If null, the opacity of the helper is not changed
revert | RevertOption | Determine if the helper will return to its starts position when dragging stops | RevertOption.NEVER | the two following options RevertOption.ON_VALID_DROP and RevertOption.ON_INVALID_DROP are useful in combination with the droppable plugin
revertDuration | int | The duration of the revert animation, in milliseconds. | null | \-
scope | String | Used to group sets of draggable and droppable items, in addition to droppable's accept option. A draggable with the same scope value as a droppable will be accepted by the droppable. | 'default' | \-
scroll | boolean | Define if the container scroll while dragging | true | \-
scrollSensitivity | int | Distance in pixels from the edge of the viewport after which the viewport should scroll. Distance is relative to pointer, not to the draggable. | 20 | \-
scrollSpeed | int | The speed at which the window should scroll once the mouse pointer gets within the scrollSensitivity distance. | 20 | \-
snap | boolean | Define if the draggable will snap to the edges of the other draggable elements when it is near an edge of these elements. | false | \-
snap | GQuery or String | The draggable will snap to the edges of the selected elements when near an edge of the element. | null | \-
snapMode | SnapMode | Determines which edges of snap elements the draggable will snap to. | SnapMode.BOTH | Possible values: SnapMode.INNER, SnapMode.OUTER, SnapMode.BOTH
snapTolerance | int | The distance in pixels from the snap element edges at which snapping should occur. | 20 | \-
stack | GQuery or String | Controls the z-Index of the selected elements, always brings to front the dragged item. | null |
zIndex | Integer | z-index for the helper while being dragged. | null | \-

Please note that all properties can be modified after the plugin is instantiated.

## Droppable Options {droppable-options}

To configure the plugin, just set the properties you wish to configure in the DroppableOptionsinstance you pass to the plugin. The table below lists the different properties of the DroppableOptionsobject. Each property is accessible via getter/setter methods.

Property | Type | Description | Default value | Comments
:--- | :--- | :--- | :--- | :---
disabled | boolean | Disables (true) or enables (false) the drop operation. | false | \-
accept | String or AcceptFunction | Define which draggable elements will be accepted by the droppable | null | If this option is null, the droppable accepts all draggables.
activeClass | String | Css class added to the droppable when it is active (i.e. when an acceptable draggable is being dragged)) | null | If this option is null, no class is added.
draggableHoverClass | String | Css class that will be added to an acceptable draggable when it is being dragged over the droppable | null | If this option is null, no class is added.
droppableHoverClass	| String  | Css class that will be added to a droppable when an acceptable draggable is being dragged over it | null | If this option is null, no class is added.
greedy | boolean | When set to true, prevents events propagation of droppable parents of the droppable | false | \-
onActivate | DroppableFunction | Callback function called when a droppable is activated (i.e. when an acceptable draggable starts being dragged) | null	| \-
onDeactivate | DroppableFunction | Callback function called when a droppable is deactivated (when an acceptable draggable stops being dragged) | null | \-
onDrop | DroppableFunction | Callback function called when an acceptable draggable is dropped on the droppable | null | \-
onOut | DroppableFunction | Callback function called when an acceptable draggable is being dragged out of the droppable | null | \-
onOver | DroppableFunction	Callback function called when an acceptable draggable is being dragged over the droppable | null | \-
scope | String | Used to group sets of draggable and droppable widget, in addition to droppable's accept option. A droppable will accept any draggable with the same scope value as itself. | "default" | \-
tolerance | DroppableTolerance | Specifies which mode to use for testing whether a draggable is 'over' a droppable. | DroppableTolerance.INTERSECT | \-

Please note that all properties can be modified after the plugin is instantiated. See this example to test the different options.