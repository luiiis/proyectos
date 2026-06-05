# Proyecto 11: Sistema Ventas Full Stack

## ¿Qué construimos?
Sistema completo: Angular (frontend) + Spring Boot (backend) + PostgreSQL (BD) integrados con Docker Compose.

## Stack
- Frontend: Angular 17 + PrimeNG
- Backend: Spring Boot 3.3 + Security + JWT + JPA
- BD: PostgreSQL 16 (7 tablas)
- Docker: 3 servicios orquestados

## Funcionalidades
- Login con JWT
- Dashboard con KPIs
- CRUD de productos
- Registrar ventas (descuenta stock)
- Historial de ventas

## Ejecutar
```bash
cd academia-profesional/proyecto-11-ventas-fullstack
docker compose up --build -d
# Frontend: http://localhost
# API: http://localhost:8080/api
# Login: admin / admin123 (hash BCrypt en seed SQL)
```

## Base de datos
Se crea automáticamente con `backend/sql/01-schema.sql`:
- roles, usuarios (auth)
- categorias, productos (catálogo)
- clientes (CRM)
- ventas, detalle_venta (transacciones)
