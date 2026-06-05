# Fullstack Application - Sistema de Gestión

## Arquitectura General

```
┌─────────────────────────────────────────────────────────────┐
│                        FRONTEND                              │
│                   Angular 17+ (SPA)                          │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────┐   │
│  │  Login   │ │ Usuarios │ │ Productos│ │   Correos    │   │
│  │  Module  │ │  Module  │ │  Module  │ │   Module     │   │
│  └──────────┘ └──────────┘ └──────────┘ └──────────────┘   │
└─────────────────────────┬───────────────────────────────────┘
                          │ HTTP/JSON (JWT)
┌─────────────────────────┼───────────────────────────────────┐
│                    BACKEND                                    │
│  ┌──────────────────────┴────────────────────────────────┐  │
│  │           API Gateway / Spring Boot Main               │  │
│  │         (Auth Service - Puerto 8080)                   │  │
│  │  • JWT Authentication                                  │  │
│  │  • User CRUD                                           │  │
│  │  • Roles Management                                    │  │
│  │  • Product/Inventory CRUD                              │  │
│  └────────────────────────────────────────────────────────┘  │
│  ┌────────────────────────────────────────────────────────┐  │
│  │         Mail Service - Spring Boot (Puerto 8081)       │  │
│  │  • Envío de correos                                    │  │
│  │  • Recuperación de contraseña                          │  │
│  │  • Gestión de usuarios de correo                       │  │
│  └────────────────────────────────────────────────────────┘  │
└─────────────────────────┬───────────────────────────────────┘
                          │
┌─────────────────────────┼───────────────────────────────────┐
│                   DATABASES                                   │
│  ┌──────────┐    ┌──────────┐    ┌──────────────────┐       │
│  │  MySQL   │    │  Oracle  │    │   SQL Server     │       │
│  │ (Users/  │    │(Products/│    │   (Mail/Logs)    │       │
│  │  Auth)   │    │Inventory)│    │                  │       │
│  └──────────┘    └──────────┘    └──────────────────┘       │
└─────────────────────────────────────────────────────────────┘
```

## Tecnologías

### Backend
- Java 17+
- Spring Boot 3.2+
- Spring Security + JWT
- Spring Data JPA
- Spring Mail
- Jasypt (encriptación de passwords en properties)
- Maven

### Frontend
- Angular 17+
- Angular Material
- RxJS
- JWT Interceptor

### Bases de Datos
- MySQL 8 (Usuarios, Roles, Autenticación)
- Oracle XE 21c (Productos, Inventario, Ventas)
- SQL Server 2022 (Correos, Logs, Auditoría)

### DevOps
- Docker & Docker Compose
- Nginx (reverse proxy para Angular)

## Estructura del Proyecto

```
fullstack-app/
├── backend-auth/          # Servicio principal (Auth + CRUD)
├── backend-mail/          # Servicio de correos
├── frontend/              # Angular App
├── docker/                # Dockerfiles y configs
├── docker-compose.yml     # Orquestación
├── docs/                  # Documentación detallada
└── README.md
```
