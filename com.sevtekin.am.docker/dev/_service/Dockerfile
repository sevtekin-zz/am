FROM tomcat
MAINTAINER Sefa Sevtekin
ENV DBUSER=amdbuser
ENV DBPASSWORD=amdb 
ENV DBDUMPLOCATION=/.am
ENV DBURL=jdbc:mysql://172.18.0.1:6000/amdb
COPY bin/amservice.war /usr/local/tomcat/webapps/.
COPY server.xml /usr/local/tomcat/conf/.
RUN mkdir -p /.am
RUN echo "DBUSER=$DBUSER">/etc/environment
RUN echo "DBPASSWORD=$DBPASSWORD">>/etc/environment
RUN echo "DBDUMPLOCATION=$DBDUMPLOCATION">>/etc/environment
RUN echo "DBURL=$DBURL">>/etc/environment
RUN apt-get update && apt-get install -y mysql-client
RUN $JAVA_HOME/bin/keytool -genkey -noprompt -alias tomcat -keyalg RSA -dname "CN=google.com, OU=ID, O=CGP, L=Hursley, S=Hants, C=US" -keystore /root/.keystore -storepass password -keypass password

