#!/bin/bash
echo "---> PUSH IMAGE TO GCR.IO" &&
gcloud docker -- push gcr.io/sefa-am-project/service &&
echo "---> DELETE POD" &&
kubectl delete pod am-service ||
echo "---> WAIT " &&
sleep 60;kubectl get pod && 
echo "---> RUN POD" &&
kubectl create -f service.yaml &&
echo "---> DELETE SERVICE" &&
kubectl delete service am-service ||
echo "---> WAIT " &&
sleep 60;kubectl get service &&
echo "---> EXPOSE DEPLOYMENT" &&
kubectl expose deployment am-service --type=LoadBalancer --port 9442 &&
kubectl get pod;kubectl get service
