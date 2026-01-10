# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# ---------- RUNTIME ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=builder /build/target/entry-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]
