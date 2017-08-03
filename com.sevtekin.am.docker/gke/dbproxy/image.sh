#!/bin/bash
echo "---> STOP SERVICE" &&
echo "---> DELETING IMAGE" &&
docker rmi -f gcr.io/sefa-am-project/dbproxy ||
docker rmi -f sevtekin/dbproxy ||
echo "---> BUILDING IMAGE" &&
docker build -t gcr.io/sefa-am-project/dbproxy . &&
docker tag gcr.io/sefa-am-project/dbproxy sevtekin/dbproxy &&
docker push gcr.io/sefa-am-project/dbproxy &&
docker push sevtekin/dbproxy &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
