FROM ubuntu
MAINTAINER Sefa Sevtekin
COPY sefa-am-project-mysql.json /tmp/
RUN apt-get update && apt-get install -y mysql-client
RUN apt-get install -y wget && wget https://dl.google.com/cloudsql/cloud_sql_proxy.linux.amd64 -O cloud_sql_proxy
RUN chmod +x cloud_sql_proxy
CMD ./cloud_sql_proxy -instances=sefa-am-project:us-central1:amdb=tcp:3306 -credential_file=/tmp/sefa-am-project-mysql.json

