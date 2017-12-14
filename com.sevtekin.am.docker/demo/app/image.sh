#!/bin/bash
echo "---> DELETING IMAGE" &&
docker rmi -f us.gcr.io/assetmanager-171616/nginxlb
echo "---> BUILDING IMAGES" &&
docker build -t us.gcr.io/assetmanager-171616/nginxlb . &&
gcloud docker -- push us.gcr.io/assetmanager-171616/nginxlb &&
echo "---> REMOVE DANGLING IMAGES" &&
docker rmi $(docker images -f "dangling=true" -q) 2>&1 >/dev/null
