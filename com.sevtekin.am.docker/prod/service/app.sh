#!/bin/bash
echo "---> BUILDING APP BINARY" &&
mvn clean install -DskipTests -nsu -f ../../../com.sevtekin.am.build/build.service &&
cp ../../bin/service/am#service.war bin/.
