#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p amdev stop db;docker-compose -p amdev rm -f db;docker-compose -p amdev up -d db
