#!/bin/bash
echo "---> END-TO-END DEPLOYMENT" &&
./app.sh;./image.sh;./deploy.sh
