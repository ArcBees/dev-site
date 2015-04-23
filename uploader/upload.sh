#!/bin/bash -e
#
# Run with 'localhost' to deploy locally.
# Run with 'credentials' (after running save_credentials.sh) to deploy to prod.

mvn -q compile generate-resources
mvn -q exec:java -Dexec.mainClass=com.google.gwt.site.uploader.Uploader -Dexec.args="target/arcbees-markdown-unpack $1"
