FROM openjdk:22-jdk-slim
MAINTAINER FKSNerdVend
COPY target/*.jar nerdvend.jar
ENTRYPOINT ["java", "-jar", "/nerdvend.jar"]
