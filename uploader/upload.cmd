@ECHO OFF

mvn -q compile generate-resources exec:java -Dexec.mainClass=com.google.gwt.site.uploader.Uploader -Dexec.args="target/gwt-site-unpack %1"
