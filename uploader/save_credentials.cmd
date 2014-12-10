@ECHO OFF

mvn -q compile exec:java -Dexec.mainClass=com.google.gwt.site.uploader.SaveCredentials
