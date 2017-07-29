FROM tomcat
MAINTAINER Sefa Sevtekin
ENV DBUSER=amdbuser
ENV DBPASSWORD=Sw+2Wr8am4z4NXWwYcQ7/w\=\= 
ENV DBDUMPLOCATION=/snapshots
ENV ALLOWEDCONSUMERS=172.17.0.1,172.16.120.1,172.18.0.1,127.0.0.1,192.168.229.129
ENV DBURL=jdbc:mysql://127.0.0.1:3306/amdb
COPY bin/amservice.war /usr/local/tomcat/webapps/. 
#COPY sefa-am-project-mysql.json /tmp/
COPY server.xml /usr/local/tomcat/conf/.
RUN mkdir -p /root/am
RUN echo "DBUSER=$DBUSER">/etc/environment
RUN echo "DBPASSWORD=$DBPASSWORD">>/etc/environment
RUN echo "DBDUMPLOCATION=$DBDUMPLOCATION">>/etc/environment
#RUN echo "ALLOWEDCONSUMERS=$ALLOWEDCONSUMERS">>/etc/environment
RUN echo "DBURL=$DBURL">>/etc/environment
RUN apt-get update && apt-get install -y mysql-client
RUN $JAVA_HOME/bin/keytool -genkey -noprompt -alias tomcat -keyalg RSA -dname "CN=tomcat.com, OU=ID, O=IBM, L=Hursley, S=Hants, C=GB" -keystore .keystore -storepass password -keypass password
#RUN wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy && chmod +x cloud_sql_proxy
#CMD /usr/local/tomcat/bin/catalina.sh start;./cloud_sql_proxy -instances=sefa-am-project:us-central1:amdb=tcp:3306 -credential_file=/tmp/sefa-am-project-mysql.json
