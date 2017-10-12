#!/bin/bash
echo "---> STOP SERVICE" &&
echo "---> DELETING IMAGE" &&
docker rmi -f gcr.io/sefa-am-project/w3 ||
#docker rmi -f sevtekin/w3 ||
echo "---> BUILDING IMAGE" &&
docker build -t gcr.io/sefa-am-project/w3 . &&
#docker tag gcr.io/sefa-am-project/w3 sevtekin/w3 &&
docker push gcr.io/sefa-am-project/w3 &&
#docker push sevtekin/w3 &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
