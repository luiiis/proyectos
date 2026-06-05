# ════════════════════════════════════════════════════════════════
# Dockerfile Backend - Multi-stage optimizado
# Imagen final: ~200MB (vs ~800MB sin multi-stage)
# ════════════════════════════════════════════════════════════════

# ═══ STAGE 1: BUILD ═══
# Imagen pesada con JDK + Maven (solo para compilar)
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app

# Cachear dependencias (solo se re-descarga si pom.xml cambia)
COPY pom.xml .
RUN apk add --no-cache maven && mvn dependency:go-offline -B

# Compilar código
COPY src ./src
RUN mvn package -DskipTests -B

# ═══ STAGE 2: RUNTIME ═══
# Imagen ligera: solo JRE (sin compilador, sin Maven, sin código fuente)
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Seguridad: usuario no-root
RUN addgroup -S app && adduser -S app -G app

# Solo copiar el .jar compilado
COPY --from=build /app/target/*.jar app.jar

# Cambiar a usuario no-root
USER app

# Puerto documentado
EXPOSE 8080

# Health check interno
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health || exit 1

# Ejecutar con optimizaciones JVM para contenedores
ENTRYPOINT ["java", "-XX:+UseZGC", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
