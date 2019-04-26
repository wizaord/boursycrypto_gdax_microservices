FROM openjdk:8-jre-slim
MAINTAINER mouilleron.cedric@gmail.com
COPY gdaxauthservice/target/gdaxauthservice-0.0.1-SNAPSHOT.jar /opt/gdaxapplication/gdaxauthservice.jar
COPY deploy/conf/gdaxauthservice/application.properties /opt/gdaxapplication/
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar", "/opt/gdaxapplication/gdaxauthservice.jar"]
EXPOSE 28081