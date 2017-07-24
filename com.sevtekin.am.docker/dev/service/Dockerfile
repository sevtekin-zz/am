FROM websphere-liberty
MAINTAINER Sefa Sevtekin
ENV DBUSER=amdbuser
ENV DBPASSWORD=amdb 
ENV DBDUMPLOCATION=/snapshots
ENV ALLOWEDCONSUMERS=172.17.0.1,172.16.120.1,172.18.0.1,127.0.0.1,192.168.229.129
ENV DBURL=jdbc:mysql://172.18.0.1:6000/amdb
COPY bin/amservice.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/ 
COPY server.xml /opt/ibm/wlp/usr/servers/defaultServer/
RUN mkdir -p ~/.am
RUN echo "DBUSER=$DBUSER">~/.am/am.properties
RUN echo "DBPASSWORD=$DBPASSWORD">>~/.am/am.properties
RUN echo "DBDUMPLOCATION=$DBDUMPLOCATION">>~/.am/am.properties
RUN echo "ALLOWEDCONSUMERS=$ALLOWEDCONSUMERS">>~/.am/am.properties
RUN echo "DBURL=$DBURL">>~/.am/am.properties
RUN apt-get update && apt-get install -y mysql-client

