#!/bin/bash
echo "---> STOP SERVICE" &&
echo "---> DELETING IMAGE" &&
docker rmi -f gcr.io/sefa-am-project/service
echo "---> BUILDING IMAGE" &&
docker build -t gcr.io/sefa-am-project/service . &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
