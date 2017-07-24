FROM gold/tomcat
MAINTAINER Sefa Sevtekin
ENV DBUSER=amdbuser
ENV DBPASSWORD=amdb 
ENV DBDUMPLOCATION=/snapshots
ENV ALLOWEDCONSUMERS=172.17.0.1,172.16.120.1,127.0.0.1
ENV DBURL=jdbc:mysql://172.17.0.1:5005/amdb
COPY bin /opt/tomcat/webapps
COPY entry.sh /root/
RUN chmod 744 /root/entry.sh
ENTRYPOINT exec /root/entry.sh
