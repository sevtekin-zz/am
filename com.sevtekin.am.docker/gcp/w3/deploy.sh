#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p amgcp stop w3;docker-compose -p amgcp rm -f w3;docker-compose -p amgcp up -d w3
