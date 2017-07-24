#!/bin/bash
echo "---> STOP SERVICE" &&
cd ..;docker-compose -p amdev stop w3;cd w3 || true &&
echo "---> DELETING IMAGE" &&
docker rmi -f ci/w3dev
echo "---> BUILDING IMAGE" &&
docker build -t ci/w3dev . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
