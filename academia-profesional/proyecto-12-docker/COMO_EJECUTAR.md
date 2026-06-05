# Cómo Ejecutar - Proyecto 12: Contenerización Docker

## ¿Qué construimos?
Dockerizar un stack completo: Angular + Spring Boot + PostgreSQL + Redis + Nginx.
Demuestra multi-stage builds, redes, volúmenes, health checks y optimización de imágenes.

## Ejecutar
```bash
cd academia-profesional/proyecto-12-docker
docker compose up --build -d

# Ver estado
docker compose ps

# Acceder:
# Frontend: http://localhost
# API: http://localhost:8080/api/productos
# Swagger: http://localhost:8080/swagger-ui.html
# PgAdmin: http://localhost:5050 (admin@admin.com / admin)
```

## Comandos útiles
```bash
# Ver logs
docker compose logs -f backend

# Reconstruir solo backend
docker compose up --build backend -d

# Entrar a un contenedor
docker exec -it docker-backend sh

# Ver tamaño de imágenes
docker images | grep docker

# Detener
docker compose down

# Detener + borrar datos
docker compose down -v
```

## Estructura
```
proyecto-12-docker/
├── docker-compose.yml          ← Orquesta 5 servicios
├── backend/
│   └── Dockerfile              ← Multi-stage (build + runtime)
├── frontend/
│   ├── Dockerfile              ← Multi-stage (node build + nginx)
│   └── nginx.conf              ← Reverse proxy config
├── CONSTRUCCION.md             ← Por qué cada decisión
└── COMO_EJECUTAR.md
```
