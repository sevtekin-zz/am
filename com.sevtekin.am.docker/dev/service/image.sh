#!/bin/bash
echo "---> STOP SERVICE" &&
cd ..;docker-compose -p amdev stop service;cd service || true &&
echo "---> DELETING IMAGE" &&
docker rmi -f ci/servicedev
echo "---> BUILDING IMAGE" &&
docker build -t ci/servicedev . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
