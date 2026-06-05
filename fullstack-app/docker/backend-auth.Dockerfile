# ============ Build Stage ============
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY backend-auth/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend-auth/src ./src
RUN mvn package -DskipTests -B

# ============ Runtime Stage ============
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Crear usuario no-root por seguridad
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/*.jar app.jar

# Cambiar a usuario no-root
USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
