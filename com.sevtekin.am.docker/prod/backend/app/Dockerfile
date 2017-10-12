FROM ubuntu
RUN mkdir -p /app/snapshots
COPY package.json /app/.
COPY ambackend.js /app/.
COPY mockserver.key /app/.
COPY mockserver.crt /app/.
COPY amgcs.json /app/.
COPY entry.sh /app/.
RUN chmod 744 /app/entry.sh
RUN apt-get update -y  && apt-get upgrade -y
RUN apt-get install curl -y
RUN echo "deb http://packages.cloud.google.com/apt gcsfuse-xenial main" | tee /etc/apt/sources.list.d/gcsfuse.list
RUN curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | apt-key add -
RUN apt-get update -y
RUN apt-get install nodejs -y
RUN apt-get install npm -y
RUN apt-get install gcsfuse -y
#RUN gcsfuse am_snapshots /app/snapshots
#RUN apt-get install vim -y
#RUN apt-get install iputils-ping -y
WORKDIR /app
RUN npm install
RUN apt-get install mysql-client -y
EXPOSE 443
ENTRYPOINT [ "sh", "-c","/app/entry.sh"]
