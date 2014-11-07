#!/bin/bash -e
#
# Run this command to save your account's cookie to 'credentials'

mvn -q compile
mvn -q exec:java -Dexec.mainClass=com.google.gwt.site.uploader.SaveCredentials
