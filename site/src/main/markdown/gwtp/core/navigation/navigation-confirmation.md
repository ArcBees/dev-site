# Navigation Confirmation
Losing the content of a complex form after accidentally closing a tab or navigating away from can be frustrating for users. To make this less likely to happen, some web applications will often display a confirmation dialog when the user tries to leave a page without saving its changes. GWTP offers a built-in mechanism to display such a confirmation.

The confirmation message can be enabled by calling `PlaceManager.setOnLeaveConfirmation("Do you really want to leave?")`. For example, this method can be called when the user edits something on the page. When the user saves his changes it is possible to disable this confirmation by calling `PlaceManager.setOnLeaveConfirmation(null)`.

When the navigation confirmation message is set, a pop-up dialog will ask the user to confirm the navigation.
