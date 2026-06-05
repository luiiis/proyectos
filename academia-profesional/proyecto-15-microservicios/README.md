# Proyecto 15: Microservicios

## ¿Qué construimos?
3 servicios independientes + API Gateway. Cada servicio tiene su propia BD y se comunican via HTTP.

## Arquitectura
```
Gateway (:8080) → rutea según path:
  /api/usuarios/**  → usuarios-service (:8081)
  /api/productos/** → productos-service (:8082)
  /api/ventas/**    → ventas-service (:8083)
```

## Tecnologías
- Spring Cloud Gateway (routing)
- 3 Spring Boot services (independientes)
- 3 PostgreSQL (1 por servicio)
- Redis (caché compartido)

## Ejecutar
```bash
cd academia-profesional/proyecto-15-microservicios
docker compose up --build -d

# Todo via Gateway:
curl localhost:8080/api/productos
curl localhost:8080/api/usuarios

# Directo a un servicio:
curl localhost:8082/api/productos
```

## Código clave
- `gateway/src/.../application.yml` → rutas de routing
- `productos-service/src/.../ProductosServiceApplication.java` → servicio completo en 1 archivo
