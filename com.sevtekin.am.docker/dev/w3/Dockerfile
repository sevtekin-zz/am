FROM websphere-liberty
MAINTAINER Sefa Sevtekin
ENV SERVICEURIROOT=https://172.18.0.1:6001/amservice
COPY bin/am.war /opt/ibm/wlp/usr/servers/defaultServer/dropins/ 
COPY server.xml /opt/ibm/wlp/usr/servers/defaultServer/
RUN mkdir -p ~/.am
RUN echo "SERVICEURIROOT=$SERVICEURIROOT">~/.am/am.properties
