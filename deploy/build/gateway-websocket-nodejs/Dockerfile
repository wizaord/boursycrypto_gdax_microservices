FROM node:12-slim
MAINTAINER mouilleron.cedric@gmail.com
WORKDIR /opt/gdaxapplication/gateway-websocket
COPY gateway-websocket-nodejs/package* ./
COPY gateway-websocket-nodejs/src ./src/
RUN npm install
CMD ["npm", "start"]
