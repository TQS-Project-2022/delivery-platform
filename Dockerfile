FROM amazoncorretto:11-alpine-jdk
EXPOSE 8081
MAINTAINER ua.pt
ADD target/*.jar app2.jar
ENTRYPOINT ["java","-jar","app2.jar"]