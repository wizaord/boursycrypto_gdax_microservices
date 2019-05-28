FROM openjdk:8-jre-slim
MAINTAINER mouilleron.cedric@gmail.com
WORKDIR /opt/gdaxapplication/
COPY influxdb-storeservice/target/influxdb-storeservice-0.0.1-SNAPSHOT.jar ./influxdbstoreservice.jar
COPY deploy/conf/influxdb-store/* ./
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "./influxdbstoreservice.jar"]
