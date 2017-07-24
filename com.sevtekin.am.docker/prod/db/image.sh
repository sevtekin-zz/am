#!/bin/bash
echo "---> DELETING IMAGE" &&
docker rmi -f ci/db
echo "---> BUILDING IMAGE" &&
docker build -t ci/db . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
