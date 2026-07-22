# Manual Técnico — Fullstack 2026

## Objetivo del Proyecto
Demostrar un stack Full Stack **moderno 2026** con las mejores prácticas enterprise: Java 25 + Spring Boot 3.3 + Angular 21 + PostgreSQL + Redis + Keycloak + Docker.

---

## Arquitectura

```
┌─────────────────────────────────────────────────────────────────────┐
│ FRONTEND (Angular 21)                                               │
│ Signals + Zoneless + @defer + TanStack Query + Keycloak JS         │
│ Puerto: 4200 (dev) / 80 (Docker+Nginx)                             │
└──────────────────────────────────┬──────────────────────────────────┘
                                   │ HTTP + JWT
                                   ▼
┌─────────────────────────────────────────────────────────────────────┐
│ BACKEND (Spring Boot 3.3 + Java 25)                                 │
│ Virtual Threads + OAuth2 Resource Server + Cache Redis + Flyway     │
│ Puerto: 8080                                                        │
├─────────────────┬──────────────────────┬────────────────────────────┤
│ PostgreSQL 16   │ Redis 7              │ Keycloak 25                │
│ :5432           │ :6379                │ :8180                      │
│ BD + Flyway     │ Caché + Rate Limit   │ OAuth2/OIDC + Usuarios     │
└─────────────────┴──────────────────────┴────────────────────────────┘
```

---

## Stack Tecnológico

| Capa | Tecnología | Versión | Para qué |
|------|-----------|---------|----------|
| Frontend | Angular | 21 | SPA con Signals, Zoneless rendering |
| Frontend | Angular Material | 21 | UI components (tabla, dialog, forms) |
| Frontend | TanStack Query | 5 | Server state + caching automático |
| Frontend | Keycloak JS | 25 | Autenticación OpenID Connect |
| Backend | Java | 25 (LTS) | Virtual Threads, Records, Sealed, Pattern Matching |
| Backend | Spring Boot | 3.3 | Auto-config, starters, Actuator |
| Backend | Spring Security | 6 | OAuth2 Resource Server (valida JWT de Keycloak) |
| Backend | Spring Data JPA | 3.3 | Repository pattern, derived queries |
| Backend | Flyway | 10 | Migraciones de BD versionadas |
| Backend | Spring Cache + Redis | - | Caché distribuido con TTL |
| BD | PostgreSQL | 16 | JSONB, full-text search, partial indexes |
| Cache | Redis | 7 | @Cacheable + rate limiting |
| Auth | Keycloak | 25 | Identity Provider (OAuth2/OIDC) |
| Infra | Docker Compose | v2 | Orquestación local |
| Observability | OpenTelemetry | 1.0 | Traces distribuidos |
| Tests | Testcontainers | 1.19 | Tests con BD real (no H2) |

---

## Estructura de Carpetas

```
fullstack-2026/
├── backend/
│   ├── src/main/java/com/modern/app/
│   │   ├── Application.java              ← @SpringBootApplication
│   │   ├── api/
│   │   │   ├── controller/
│   │   │   │   ├── ProductController.java ← REST endpoints
│   │   │   │   └── GlobalExceptionHandler.java ← Error handling
│   │   │   └── dto/
│   │   │       └── ProductDtos.java       ← Records (Request/Response)
│   │   ├── config/
│   │   │   └── SecurityConfig.java        ← OAuth2 + CORS
│   │   └── domain/
│   │       ├── entity/
│   │       │   ├── ProductEntity.java     ← JPA Entity
│   │       │   ├── UserEntity.java
│   │       │   └── RoleEntity.java
│   │       ├── repository/
│   │       │   ├── ProductRepository.java ← Spring Data JPA
│   │       │   └── UserRepository.java
│   │       └── service/
│   │           └── ProductService.java    ← Lógica + Caché
│   ├── src/main/resources/
│   │   ├── application.yml                ← Config Spring Boot
│   │   └── db/migration/
│   │       ├── V1__initial_schema.sql     ← Flyway: crear tablas
│   │       └── V2__seed_data.sql          ← Flyway: datos prueba
│   ├── src/test/                          ← Tests con Testcontainers
│   ├── Dockerfile                         ← Multi-stage build
│   └── pom.xml                            ← Maven dependencies
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── core/services/             ← Auth, Product services
│   │   │   ├── core/guards/              ← Auth guard
│   │   │   ├── core/interceptors/        ← JWT interceptor
│   │   │   ├── features/dashboard/       ← Dashboard page
│   │   │   ├── features/products/        ← Products CRUD
│   │   │   ├── features/users/           ← Users management
│   │   │   ├── app.routes.ts             ← Routing + lazy loading
│   │   │   └── app.config.ts             ← Providers
│   │   ├── index.html
│   │   └── main.ts
│   ├── Dockerfile                         ← Angular build + Nginx
│   ├── nginx.conf                         ← Reverse proxy a backend
│   └── package.json
├── docs/
│   ├── GUIA-LEVANTAR-PROYECTO-2026.md    ← Paso a paso COMPLETO
│   └── COMPARACION-2024-VS-2026.md       ← Por qué estas tecnologías
├── .env.example                           ← Variables de entorno template
├── docker-compose.yml                     ← 5 servicios orquestados
├── MANUAL-TECNICO.md                      ← Este archivo
└── README.md                              ← Descripción general
```

---

## Endpoints del API

| Método | Ruta | Rol requerido | Descripción |
|--------|------|---------------|-------------|
| GET | /api/products | USER | Listar productos (cached) |
| GET | /api/products/{id} | USER | Producto por ID |
| GET | /api/products/search?q= | USER | Buscar por nombre |
| GET | /api/products/categories | USER | Listar categorías |
| GET | /api/products/category/{cat} | USER | Filtrar por categoría |
| GET | /api/products/low-stock | ADMIN/MANAGER | Alertas de stock |
| POST | /api/products | ADMIN/MANAGER | Crear producto |
| PUT | /api/products/{id} | ADMIN/MANAGER | Actualizar |
| DELETE | /api/products/{id} | ADMIN | Eliminar (soft) |
| POST | /api/products/{id}/stock/add | ADMIN/MANAGER | Agregar stock |
| POST | /api/products/{id}/stock/remove | ADMIN/MANAGER | Descontar stock |
| GET | /actuator/health | - | Health check |
| GET | /actuator/prometheus | - | Métricas |

---

## Decisiones Técnicas

| Decisión | Razón | Alternativa descartada |
|----------|-------|------------------------|
| Virtual Threads | 100K req/s sin reactive | WebFlux (más complejo) |
| Keycloak (no JWT custom) | Auth enterprise lista, MFA, social login | Implementar JWT desde cero |
| Flyway (no ddl-auto) | Control, versionado, equipo sincronizado | hibernate.ddl-auto=update |
| Records para DTOs | Inmutables, sin Lombok, 1 línea | Clases + Lombok |
| Redis caché | Distribuido, TTL, persiste entre reinicios | Caffeine (solo local) |
| Soft delete | Nunca perder datos, auditoría | DELETE real |
| JSONB para audit | Flexibilidad, indexable | Tabla con 20 columnas |
| Testcontainers | Tests con BD REAL, no H2 | H2 in-memory (distinto a PostgreSQL) |

---

## Flujo de Autenticación

```
1. Frontend → Keycloak: login (redirect)
2. Keycloak → Frontend: access_token + refresh_token
3. Frontend → Backend: request + Authorization: Bearer <token>
4. Backend → Keycloak: descarga claves públicas (jwks endpoint)
5. Backend: valida firma JWT localmente (sin llamar a Keycloak cada vez)
6. Backend: extrae roles del token → @PreAuthorize los verifica
7. Backend → Frontend: respuesta JSON
```

---

## Cómo Ejecutar (versión rápida)

```bash
# TODO con Docker (más simple):
cd fullstack-2026
cp .env.example .env
docker compose up --build -d
# Esperar 60 segundos → http://localhost

# Solo BDs + desarrollo local:
docker compose up postgres redis keycloak -d
cd backend && mvn spring-boot:run        # → :8080
cd frontend && npm install && ng serve   # → :4200
```

Para la guía paso a paso con explicación detallada:
→ ver `docs/GUIA-LEVANTAR-PROYECTO-2026.md`
