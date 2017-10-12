#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p amdev stop backend;docker-compose -p amdev rm -f backend;docker-compose -p amdev up -d backend
