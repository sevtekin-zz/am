#!/bin/bash
echo "---> OVERALL DEPLOYMENT" &&
echo "---> STOP APP AND REMOVE CONTAINERS" &&
docker-compose -p amdev stop;docker-compose -p amdev rm -f &&
echo "---> W3" &&
cd w3;./all.sh;
echo "---> BACKEND" &&
cd ../backend;./all.sh;
echo "---> DB" &&
cd ../db;./all.sh;
