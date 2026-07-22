# Ejercicios y Retos - Proyecto 12: Docker

## Reto 1: Optimizar Dockerfile con multi-stage
Reduce el tamaño de tu imagen backend:
```dockerfile
# Stage 1: Compilar
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline    # Cache de dependencias
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: Solo el JAR (imagen final ~200MB en vez de 800MB)
FROM eclipse-temurin:21-jre-alpine
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

Compara tamaños: `docker images` antes y después.

**Lo que practicas:** Multi-stage builds, optimización de imágenes

---

## Reto 2: Health checks
Agrega health checks a tu docker-compose para que los servicios arranquen en orden:
```yaml
backend:
  healthcheck:
    test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
    interval: 10s
    timeout: 5s
    retries: 3
  depends_on:
    postgres:
      condition: service_healthy
```

**Lo que practicas:** Health checks, dependencias entre servicios, resiliencia

---

## Reto 3: Variables de entorno
Externaliza TODA la configuración con variables de entorno:
- `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER`, `DB_PASS`
- `JWT_SECRET`, `JWT_EXPIRATION`
- No hardcodear NADA en el código

Usa un `.env` file para desarrollo:
```env
DB_HOST=postgres
DB_PORT=5432
JWT_SECRET=mi-secreto-super-largo-de-256-bits
```

**Lo que practicas:** 12-Factor App, configuración externa, seguridad

---

## Reto 4: Docker networking
Crea 2 redes separadas:
- `frontend-net`: nginx + backend (el frontend habla con el backend)
- `backend-net`: backend + postgres + redis (la BD no es accesible desde fuera)

```yaml
networks:
  frontend-net:
  backend-net:

services:
  postgres:
    networks: [backend-net]  # Solo accesible desde backend
  backend:
    networks: [frontend-net, backend-net]  # Puente entre ambas
  nginx:
    networks: [frontend-net]
```

**Lo que practicas:** Network isolation, seguridad, arquitectura

---

## Reto 5: Volúmenes y persistencia
Configura volúmenes para:
- PostgreSQL: datos persisten entre `docker compose down` y `up`
- Logs del backend: accesibles desde el host para debugging
- Archivos subidos: persistentes y compartidos

```yaml
volumes:
  postgres-data:
  app-logs:
```

**Lo que practicas:** Docker volumes, persistencia, named volumes vs bind mounts

---

## Reto 6: Docker Compose profiles
Usa profiles para diferentes ambientes:
```yaml
services:
  pgadmin:
    profiles: ["dev"]  # Solo en desarrollo
  
  prometheus:
    profiles: ["monitoring"]  # Solo si quieres monitoreo
```

```bash
docker compose --profile dev up -d       # Dev con pgadmin
docker compose --profile monitoring up -d # Con monitoreo
docker compose up -d                      # Solo lo esencial
```

**Lo que practicas:** Profiles, ambientes, configuración flexible

---

## Reto 7: Construir imagen y subir a Docker Hub
1. Crea cuenta en Docker Hub
2. `docker build -t tu-usuario/mi-api:1.0.0 .`
3. `docker push tu-usuario/mi-api:1.0.0`
4. En otra máquina: `docker pull tu-usuario/mi-api:1.0.0` y funciona

**Lo que practicas:** Docker Hub, registry, versionado de imágenes, distribución

---

## Reto 8 (Avanzado): Docker sin root
Ejecuta tu contenedor con usuario no-root (seguridad):
```dockerfile
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
```

Verifica que funciona y que no puede escribir en directorios protegidos.

**Lo que practicas:** Seguridad en contenedores, principio de menor privilegio
