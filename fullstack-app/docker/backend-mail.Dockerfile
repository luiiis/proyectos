# ============ Build Stage ============
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY backend-mail/pom.xml .
RUN mvn dependency:go-offline -B
COPY backend-mail/src ./src
RUN mvn package -DskipTests -B

# ============ Runtime Stage ============
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

COPY --from=build /app/target/*.jar app.jar

USER appuser

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]
