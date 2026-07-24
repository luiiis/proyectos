# ════════════════════════════════════════════════════════════════
# Dockerfile Backend — Multi-stage build
# Stage 1: Compilar con Maven (imagen pesada, solo para build)
# Stage 2: Ejecutar con JRE (imagen ligera, para producción)
# ════════════════════════════════════════════════════════════════

# ═══ STAGE 1: BUILD ═══
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Copiar pom.xml primero (cache de dependencias)
COPY pom.xml .
RUN apk add --no-cache maven && mvn dependency:go-offline -B

# Copiar código y compilar
COPY src ./src
RUN mvn package -DskipTests -B

# ═══ STAGE 2: RUNTIME ═══
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Seguridad: usuario no-root
RUN addgroup -S app && adduser -S app -G app

# Solo el JAR compilado
COPY --from=build /app/target/*.jar app.jar

USER app
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
