
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/notification-service.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
