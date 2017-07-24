#!/bin/bash
echo "---> BRINGING UP DOCKER APP" &&
cd ..;docker-compose -p amdev stop service;docker-compose -p amdev rm -f service;docker-compose -p amdev up -d service && sleep 15 &&
curl -kv https://127.0.0.1:6001/amservice/encryptpropfile
