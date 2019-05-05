FROM openjdk:8-jre-slim
MAINTAINER mouilleron.cedric@gmail.com
COPY influxdb-storeservice/target/influxdb-storeservice-0.0.1-SNAPSHOT.jar /opt/gdaxapplication/influxdbstoreservice.jar
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/gdaxapplication/influxdbstoreservice.jar", "--spring.config.location=classpath:/opt/gdaxapplication/"]
