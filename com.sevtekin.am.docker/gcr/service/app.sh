#!/bin/bash
echo "---> BUILDING APP BINARY" &&
mvn clean install -nsu -DskipTests -f ../../../com.sevtekin.am.build/build.service &&
cp ../../../com.sevtekin.am.service/target/amservice.war bin/.
