#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p am stop db;docker-compose -p am rm -f db;docker-compose -p am up -d db

