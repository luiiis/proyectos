# Módulo 24: Docker para Java

## Dockerfile para Spring Boot
```dockerfile
FROM eclipse-temurin:21-jdk-alpine AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
RUN addgroup -S app && adduser -S app -G app
COPY --from=build /app/target/*.jar app.jar
USER app
EXPOSE 8080
ENTRYPOINT ["java", "-XX:+UseZGC", "-jar", "app.jar"]
```

## Docker Compose (App + BD + Redis)
```yaml
services:
  app:
    build: .
    ports: ["8080:8080"]
    environment:
      DB_HOST: postgres
      REDIS_HOST: redis
    depends_on:
      postgres: { condition: service_healthy }

  postgres:
    image: postgres:16-alpine
    environment:
      POSTGRES_DB: empresa_db
      POSTGRES_PASSWORD: postgres123
    ports: ["5432:5432"]
    volumes: [postgres_data:/var/lib/postgresql/data]
    healthcheck:
      test: ["CMD", "pg_isready"]
      interval: 5s

  redis:
    image: redis:7-alpine
    ports: ["6379:6379"]

volumes:
  postgres_data:
```

## Ejercicios
1. Dockeriza tu aplicación Spring Boot
2. Crea un docker-compose con app + PostgreSQL + Redis
3. Optimiza el Dockerfile para cachear dependencias Maven
4. Configura health checks para todos los servicios
