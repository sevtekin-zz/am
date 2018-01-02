#!/bin/bash
echo "---> STOP SERVICE" &&
echo "---> DELETING IMAGE" &&
docker rmi -f us.gcr.io/assetmanager-171616/frontend ||
echo "---> BUILDING IMAGE" &&
docker build -t us.gcr.io/assetmanager-171616/frontend . &&
gcloud docker -- push us.gcr.io/assetmanager-171616/frontend &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
