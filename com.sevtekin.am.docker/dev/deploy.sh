#!/bin/bash
echo "---> START APP" &&
docker-compose -p amdev stop;docker-compose -p amdev rm -f;docker-compose -p amdev up -d
