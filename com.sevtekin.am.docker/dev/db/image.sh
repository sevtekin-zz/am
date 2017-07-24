#!/bin/bash
echo "---> STOP SERVICE" &&
cd ..;docker-compose -p amdev stop db;cd db || true &&
echo "---> DELETING IMAGE" &&
docker rmi -f ci/dbdev
echo "---> BUILDING IMAGES" &&
docker build -t ci/dbdev . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
