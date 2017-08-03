#!/bin/bash
echo "---> STOP SERVICE" &&
echo "---> DELETING IMAGE" &&
#docker rmi -f gcr.io/sefa-am-project/service ||
docker rmi -f sevtekin/service ||
echo "---> BUILDING IMAGE" &&
docker build -t gcr.io/sefa-am-project/service . &&
docker tag gcr.io/sefa-am-project/service sevtekin/service &&
#docker push gcr.io/sefa-am-project/service &&
docker push sevtekin/service &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
