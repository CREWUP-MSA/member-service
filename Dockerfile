FROM openjdk:17-jdk-slim

COPY build/libs/member-service-0.0.1-SNAPSHOT.jar /member-service.jar

ENTRYPOINT ["java", "-jar", "/member-service.jar"]