@ECHO OFF

mvn -q compile generate-resources exec:java -Dexec.mainClass=com.google.gwt.site.uploader.Uploader -Dexec.args="target/arcbees-markdown-unpack %1"
