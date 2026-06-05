# ════════════════════════════════════════════════════════════════
# Dockerfile para Spring Boot - Explicado línea por línea
# ════════════════════════════════════════════════════════════════

# ═══════ STAGE 1: BUILD ═══════
# Imagen base con JDK completo (necesario para compilar)
# alpine = variante ligera de Linux (~5MB base)
FROM eclipse-temurin:21-jdk-alpine AS build

# Directorio de trabajo dentro del contenedor
WORKDIR /app

# TRUCO DE CACHÉ: copiar pom.xml PRIMERO
# Si pom.xml no cambia, Docker reutiliza esta capa (no re-descarga dependencias)
COPY ../19-spring-boot/proyecto/app/pom.xml .

# Instalar Maven y descargar dependencias (se cachea)
RUN apk add --no-cache maven && mvn dependency:go-offline -B

# Ahora copiar el código fuente (cambia frecuentemente)
COPY ../19-spring-boot/proyecto/app/src ./src

# Compilar: genera target/java-learning-springboot-1.0.0.jar
RUN mvn package -DskipTests -B

# ═══════ STAGE 2: RUNTIME ═══════
# Imagen MÍNIMA: solo JRE (no JDK, no Maven, no código fuente)
# Resultado: imagen de ~200MB en lugar de ~800MB
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# SEGURIDAD: crear usuario no-root
# Si un atacante explota la app, no tiene permisos de root
RUN addgroup -S app && adduser -S app -G app

# Copiar SOLO el .jar compilado desde el stage anterior
COPY --from=build /app/target/*.jar app.jar

# Cambiar a usuario no-root
USER app

# Documentar el puerto (informativo)
EXPOSE 8080

# Comando de ejecución con optimizaciones JVM:
# -XX:+UseZGC: Garbage Collector de baja latencia (pausas < 1ms)
# -XX:MaxRAMPercentage=75: usar hasta 75% de la RAM del contenedor
# -Djava.security.egd: fuente de entropía rápida (arranque más rápido)
ENTRYPOINT ["java", \
    "-XX:+UseZGC", \
    "-XX:MaxRAMPercentage=75.0", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", "app.jar"]
