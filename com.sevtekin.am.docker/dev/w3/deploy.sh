#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p amdev stop w3;docker-compose -p amdev rm -f w3;docker-compose -p amdev up -d w3
