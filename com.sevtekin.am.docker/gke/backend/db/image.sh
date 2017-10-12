#!/bin/bash
echo "---> STOP SERVICE" &&
echo "---> DELETING IMAGE" &&
docker rmi -f gcr.io/sefa-am-project/db ||
#docker rmi -f sevtekin/db ||
echo "---> BUILDING IMAGE" &&
docker build -t gcr.io/sefa-am-project/db . &&
docker tag gcr.io/sefa-am-project/w3 sevtekin/db &&
docker push gcr.io/sefa-am-project/db &&
#docker push sevtekin/db &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
