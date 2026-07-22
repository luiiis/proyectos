# Cómo Ejecutar - Proyecto 12: Docker

## Requisitos
- Docker 24+ (`docker --version`)
- Docker Compose v2 (`docker compose version`)

## Ejecutar todo el stack

```bash
cd academia-profesional/proyecto-12-docker

# Construir y levantar 5 servicios
docker compose up --build -d

# Esperar ~60 segundos (build del backend)
```

## Verificar que funciona

```bash
# Ver estado de todos los servicios
docker compose ps

# Probar el backend
curl http://localhost:8080/api/productos

# Probar caché Redis (2da llamada es más rápida)
curl http://localhost:8080/api/productos   # 1ra: ~200ms (PostgreSQL)
curl http://localhost:8080/api/productos   # 2da: ~5ms (Redis cache)

# Ver info del contenedor
curl http://localhost:8080/api/info

# Health check
curl http://localhost:8080/actuator/health
```

## Accesos

| Servicio | URL | Descripción |
|----------|-----|-------------|
| Backend API | http://localhost:8080 | Spring Boot + Redis cache |
| PgAdmin | http://localhost:5050 | GUI para PostgreSQL |
| PostgreSQL | localhost:5432 | BD directa |
| Redis | localhost:6379 | Cache |

PgAdmin login: admin@admin.com / admin

## Comandos útiles

```bash
# Ver logs del backend
docker compose logs -f backend

# Entrar al contenedor
docker exec -it docker-backend sh

# Ver tamaño de imágenes
docker images | grep docker

# Detener todo
docker compose down

# Detener y borrar datos
docker compose down -v
```

## Errores comunes

| Error | Causa | Solución |
|-------|-------|----------|
| Port 5432 in use | PostgreSQL local corriendo | Detener PostgreSQL local |
| Build fails | Sin internet para descargar deps | Verificar conexión |
| OOMKilled | Poca RAM asignada a Docker | Docker Desktop → Settings → Resources → 4GB+ |
