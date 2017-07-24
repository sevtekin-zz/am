FROM sevtekin/service
MAINTAINER Sefa Sevtekin
ENV DBUSER=amdbuser
ENV DBPASSWORD=amdb 
ENV DBDUMPLOCATION=/snapshots
ENV ALLOWEDCONSUMERS=172.17.0.1,172.16.120.1,127.0.0.1
ENV DBURL=jdbc:mysql://172.17.0.1:5005/amdb
COPY bin /opt/tomcat/webapps
ENTRYPOINT ["/bin/bash"]
