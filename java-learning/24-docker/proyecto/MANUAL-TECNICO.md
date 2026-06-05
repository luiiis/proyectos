# Módulo 24: Docker para Java - Manual Técnico

## ¿Qué construimos?
Dockerfile optimizado para Spring Boot + Docker Compose con PostgreSQL.

## Cómo ejecutar
```bash
cd java-learning/24-docker/proyecto

# Construir y levantar
docker compose up --build -d

# Ver logs
docker compose logs -f app

# Probar
curl http://localhost:8080/api/productos

# Detener
docker compose down
```

## Dockerfile explicado línea por línea
Ver archivo `Dockerfile` con comentarios detallados.
