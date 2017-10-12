FROM tomcat 
MAINTAINER Sefa Sevtekin
COPY bin/am.war /usr/local/tomcat/webapps/. 
RUN rm -rf /usr/local/tomcat/webapps/examples
COPY server.xml /usr/local/tomcat/conf/.
RUN $JAVA_HOME/bin/keytool -genkey -noprompt -alias tomcat -keyalg RSA -dname "CN=google.com, OU=ID, O=IBM, L=Hursley, S=Hants, C=US" -keystore /root/.keystore -storepass password -keypass password

