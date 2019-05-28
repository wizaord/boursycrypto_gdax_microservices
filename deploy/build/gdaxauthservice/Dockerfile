FROM openjdk:8-jre-slim
MAINTAINER mouilleron.cedric@gmail.com
WORKDIR /opt/gdaxapplication/
COPY gdaxauthservice/target/gdaxauthservice-0.0.1-SNAPSHOT.jar ./gdaxauthservice.jar
COPY deploy/conf/gdaxauthservice/application.properties ./
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "./gdaxauthservice.jar"]
EXPOSE 28081