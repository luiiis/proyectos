# ════════════════════════════════════════════════════════════════
# Dockerfile Backend - Java 25 + Spring Boot 3.3
# Multi-stage build para imagen mínima
# ════════════════════════════════════════════════════════════════

# Stage 1: Build con Maven
FROM eclipse-temurin:25-jdk-alpine AS build
WORKDIR /app

# Cachear dependencias (solo se re-descarga si pom.xml cambia)
COPY pom.xml .
RUN apk add --no-cache maven && mvn dependency:go-offline -B

# Compilar
COPY src ./src
RUN mvn package -DskipTests -B

# Stage 2: Runtime mínimo
FROM eclipse-temurin:25-jre-alpine
WORKDIR /app

# Usuario no-root (seguridad)
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /app/target/*.jar app.jar
USER app

EXPOSE 8080

# Optimizaciones JVM para contenedores:
# -XX:+UseZGC: Garbage Collector de baja latencia (pausas < 1ms)
# -XX:MaxRAMPercentage=75: usar hasta 75% de la RAM del contenedor
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
