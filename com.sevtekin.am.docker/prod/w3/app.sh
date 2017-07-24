#!/bin/bash
echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -f ../../../com.sevtekin.am.build/build.w3 &&
cp ../../bin/w3/am.war bin/.
