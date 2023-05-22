#
# Build stage
#
FROM maven:3.6.0-jdk-11 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package

#
# Package stage
#
FROM openjdk:11
#WORKDIR /app
#COPY ./target/schedule-0.0.1-SNAPSHOT.jar schedule-0.0.1-SNAPSHOT.jar
ARG JAR_FILE=./schedule/target/schedule-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/schedule-0.0.1-SNAPSHOT.jar"]