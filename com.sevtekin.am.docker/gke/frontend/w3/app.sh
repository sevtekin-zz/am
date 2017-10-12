#!/bin/bash
echo "---> BUILDING APP BINARY" &&
mvn clean install -nsu -DskipTests -f ../../../../com.sevtekin.am.build/build.w3 &&
cp ../../../../com.sevtekin.am.w3/target/am.war bin/.
