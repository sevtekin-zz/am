FROM tomcat
MAINTAINER Sefa Sevtekin
ENV SERVICEURIROOT=https://172.18.0.1:6001
COPY bin/am.war /usr/local/tomcat/webapps/.
COPY server.xml /usr/local/tomcat/conf/.
COPY tomcat-users.xml /usr/local/tomcat/conf/.
RUN echo "SERVICEURIROOT=$SERVICEURIROOT">/etc/environment
RUN $JAVA_HOME/bin/keytool -genkey -noprompt -alias tomcat -keyalg RSA -dname "CN=google.com, OU=ID, O=IBM, L=Hursley, S=Hants, C=US" -keystore /root/.keystore -storepass password -keypass password

