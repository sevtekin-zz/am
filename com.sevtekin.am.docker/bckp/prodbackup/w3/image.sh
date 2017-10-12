#!/bin/bash
echo "---> DELETING IMAGE" &&
docker rmi -f ci/w3
echo "---> BUILDING IMAGE" &&
docker build -t ci/w3 . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
