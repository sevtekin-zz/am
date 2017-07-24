#!/bin/bash
export JAVA_HOME=/opt/jdk1.8.0
echo "DBUSER=$DBUSER">~/.am/am.properties
echo "DBPASSWORD=$DBPASSWORD">>~/.am/am.properties
echo "DBDUMPLOCATION=$DBDUMPLOCATION">>~/.am/am.properties
echo "ALLOWEDCONSUMERS=$ALLOWEDCONSUMERS">>~/.am/am.properties
echo "DBURL=$DBURL">>~/.am/am.properties
>/opt/tomcat/logs/catalina.out &&
/opt/tomcat/bin/catalina.sh stop;/opt/tomcat/bin/catalina.sh start;sleep 10;curl http://127.0.0.1:8080/am/service/encryptpropfile;/opt/tomcat/bin/catalina.sh stop;/opt/tomcat/bin/catalina.sh run
