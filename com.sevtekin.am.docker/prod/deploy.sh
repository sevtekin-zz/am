#!/bin/bash
echo "---> START APP" &&
docker-compose -p am stop;docker-compose -p am rm -f;docker-compose -p am up -d
