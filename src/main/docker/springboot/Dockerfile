FROM openjdk:8u302-jdk

# Update packages
RUN apt-get update -y && apt-get upgrade -y

RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring
ARG JAR_FILE=./target/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
