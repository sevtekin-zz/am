FROM tomcat
MAINTAINER Sefa Sevtekin
ENV DBUSER=amdbuser
ENV DBPASSWORD=Sw+2Wr8am4z4NXWwYcQ7/w\=\= 
ENV DBDUMPLOCATION=/am/snapshots
ENV DBURL=jdbc:mysql://127.0.0.1:3306/amdb
COPY bin/amservice.war /usr/local/tomcat/webapps/. 
COPY server.xml /usr/local/tomcat/conf/.
#RUN echo "tomcat.util.http.parser.HttpParser.requestTargetAllow=|'{ }<>=%#" >> /usr/local/tomcat/conf/catalina.properties
RUN mkdir -p /am/snapshots
COPY 20170802mn.sql /am/snapshots/.
RUN echo "DBUSER=$DBUSER">/etc/environment
RUN echo "DBPASSWORD=$DBPASSWORD">>/etc/environment
RUN echo "DBDUMPLOCATION=$DBDUMPLOCATION">>/etc/environment
RUN echo "DBURL=$DBURL">>/etc/environment
RUN apt-get update && apt-get install -y mysql-client
RUN $JAVA_HOME/bin/keytool -genkey -noprompt -alias tomcat -keyalg RSA -dname "CN=google.com, OU=ID, O=IBM, L=Hursley, S=Hants, C=US" -keystore /root/.keystore -storepass password -keypass password
