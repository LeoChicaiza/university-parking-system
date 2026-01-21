#FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=target/*.jar
#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

# ===============================
# STAGE 1 - BUILD
# ===============================
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# Copiamos dependencias primero
COPY pom.xml .
RUN mvn dependency:go-offline

# Copiamos el código
COPY src ./src

# Construimos el JAR
RUN mvn clean package -DskipTests

# ===============================
# STAGE 2 - RUNTIME
# ===============================
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copiamos el JAR
COPY --from=build /app/target/*.jar app.jar

# Ejecutamos el microservicio
ENTRYPOINT ["java","-jar","app.jar"]
