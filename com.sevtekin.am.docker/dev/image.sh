#!/bin/bash
echo "---> OVERALL IMAGE BUILD" &&
echo "---> W3" &&
cd w3;./image.sh;
echo "---> SERVICE" &&
cd ../service;./image.sh;
echo "---> DB" &&
cd ../db;./image.sh;
