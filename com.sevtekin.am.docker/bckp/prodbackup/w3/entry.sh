#!/bin/bash
export JAVA_HOME=/opt/jdk1.8.0
echo "SERVICEURIROOT=$SERVICEURIROOT">~/.am/am.properties
>/opt/tomcat/logs/catalina.out
/opt/tomcat/bin/catalina.sh run
