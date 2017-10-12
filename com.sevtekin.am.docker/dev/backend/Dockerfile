FROM node
COPY package.json .
COPY mockserver.key .
COPY mockserver.crt .
RUN npm install
COPY . .
EXPOSE 6001
CMD [ "npm", "start" ]


