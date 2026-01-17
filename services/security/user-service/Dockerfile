# Multi-stage build para seguridad y eficiencia
FROM eclipse-temurin:17-jdk-alpine AS builder

# Crear directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR (con patrón para manejar versiones)
COPY target/user-service-*.jar app.jar

# Verificar integridad del JAR
RUN ls -la /app/app.jar && \
    echo "JAR size: $(du -h /app/app.jar | cut -f1)" && \
    echo "JAR checksum: $(sha256sum /app/app.jar | cut -d' ' -f1)"

# Runtime image más pequeña y segura
FROM eclipse-temurin:17-jre-alpine

# Añadir usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Crear directorio para logs con permisos correctos
RUN mkdir -p /logs && chown spring:spring /logs

# Copiar JAR desde la etapa de builder
COPY --from=builder /app/app.jar app.jar

# Exponer puerto
EXPOSE 8080

# Configuración de seguridad de JVM
ENV JAVA_OPTS="-Djava.security.egd=file:/dev/./urandom -Dfile.encoding=UTF-8"

# Punto de entrada
ENTRYPOINT ["java", "-jar", "/app.jar"]