# Proyecto 12: Contenerización Docker

## ¿Qué construimos?
Dockerizar un stack completo: Angular + Spring Boot + PostgreSQL + Redis + Nginx.

## Conceptos demostrados
- Multi-stage build (imagen final pequeña)
- Docker Compose (orquestar 5 servicios)
- Health checks (saber cuándo un servicio está listo)
- Redes Docker (contenedores se comunican por nombre)
- Volúmenes (datos persisten entre reinicios)
- Nginx como reverse proxy (un solo puerto expuesto)
- Usuario no-root (seguridad)

## Ejecutar
```bash
cd academia-profesional/proyecto-12-docker
docker compose up --build -d
# Frontend: http://localhost
# API: http://localhost:8080
# PgAdmin: http://localhost:5050
```

## Tamaños de imagen
- Backend (multi-stage): ~200MB
- Frontend (nginx): ~25MB
- PostgreSQL: ~80MB
- Redis: ~30MB
