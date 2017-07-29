#!/bin/bash
echo "---> PUSH IMAGE TO GCR.IO" &&
gcloud docker -- push gcr.io/sefa-am-project/w3 &&
echo "---> DELETE POD" &&
kubectl delete pod am-w3 ||
echo "---> WAIT " &&
sleep 30;kubectl get pod && 
echo "---> RUN POD" &&
kubectl create -f w3.yaml &&
echo "---> DELETE SERVICE" &&
kubectl delete service am-w3 ||
echo "---> WAIT " &&
sleep 30;kubectl get service &&
echo "---> EXPOSE DEPLOYMENT" &&
kubectl expose deployment am-w3 --type=LoadBalancer --port 9443 &&
kubectl get pod;kubectl get service
