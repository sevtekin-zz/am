#!/bin/bash
echo "---> STOP SERVICE" &&
cd ..;docker-compose -p amdev stop backend;cd backend || true &&
echo "---> DELETING IMAGE" &&
docker rmi -f ci/backenddev
echo "---> BUILDING IMAGES" &&
docker build -t ci/backenddev . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
