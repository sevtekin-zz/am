#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p am stop service;docker-compose -p am rm -f service;docker-compose -p am up -d service
