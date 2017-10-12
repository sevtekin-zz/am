#!/bin/bash
echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -f ../../../../com.sevtekin.am.build/build.w3 &&
cp ../../../../com.sevtekin.am.w3/target/am.war bin/.
