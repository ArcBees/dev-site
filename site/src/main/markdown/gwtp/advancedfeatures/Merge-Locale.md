#Merge Locale

#Introduction
Using [UIBinder internationalisation features](http://code.google.com/webtoolkit/doc/latest/DevGuideUiBinderI18n.html) can be difficult. This has been discussed extensively on GWT [Issue 4355](http://code.google.com/p/google-web-toolkit/issues/detail?id=4355).

#Solution
To overcome this problem, GWTP makes it easy for you to store all your translations in:
  * `src/com/google/gwt/i18n/client/LocalizableResource.properties`
  * `src/com/google/gwt/i18n/client/LocalizableResource_fr.properties`
  * etc.

However, it is still necessary to keep all these files synchronized with one another. In addition, you want to make sure they contain any message included in a UIBinder xml file. To do so, GWTP lets you use the python script `mergelocales.py`. The script can be found in the [Downloads section](http://code.google.com/p/gwt-platform/downloads/list).

The exact process is as follows:
  * Compile GWT code by adding parameter `-extra extras`
  * Run `mergelocales extras/YourProjectName src/com/google/gwt/i18n/client/`
  * Go through the `LocalizableResource_xxxx.properties` files and solve the TODO comments.

For more details on the script, run `mergelocales --help`.

#Additional information
GWT looks for the `LocalizableResource???.properties` files in the 
directory of your class implementing Constants or Messages, and in 
the directory of any super interfaces. The problem with `UiBinder` 
translations is that the associated `LocalizableResource` files are 
generated in the folder of the `UiBinder` file, so your resources are 
spread across your directories. mergelocale looks for all of these and 
merge them intelligently in a central location: com.google.gwt.i18n.client. This is legit since this package is the 
directory of the `LocalizableResource`, a superinterface common to both 
Constants and Messages.

When compiling a localized GWT project, one has to 
 * compile 1st time, to generate extras locales 
 * run mergelocale and update translations 
 * compile 2nd time, so translations are included in the results