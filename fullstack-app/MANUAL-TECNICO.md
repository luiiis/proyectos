# Manual Técnico — Fullstack App (Sistema de Gestión Multi-BD)

## Objetivo
Sistema enterprise que demuestra conexión a **3 motores de BD diferentes** desde un solo backend Java, con microservicio de correos separado y frontend Angular.

---

## Arquitectura

```
┌────────────────────────────────────────────────────────────────────┐
│  FRONTEND (Angular 17+ / Material)  → Puerto :80 (Nginx)          │
│  Login, Register, Dashboard, Productos, Usuarios, Correos          │
└────────────────────────────────┬───────────────────────────────────┘
                                 │ HTTP + JWT
            ┌────────────────────┴────────────────────┐
            ▼                                         ▼
┌──────────────────────────┐            ┌──────────────────────────┐
│  backend-auth (:8080)    │            │  backend-mail (:8081)    │
│  • Auth (login/register) │            │  • Envío de correos      │
│  • Usuarios CRUD         │            │  • Reset password        │
│  • Productos CRUD        │            │  • Templates HTML        │
│  • Auditoría             │            │                          │
└──────┬──────┬──────┬─────┘            └──────────┬───────────────┘
       │      │      │                             │
       ▼      ▼      ▼                             ▼
┌────────┐┌────────┐┌──────────┐            ┌────────┐
│ MySQL  ││ Oracle ││SQL Server│            │ MySQL  │
│ :3306  ││ :1521  ││ :1433    │            │ (same) │
│Users/  ││Products││Logs/Mail │            │        │
│Auth    ││Inv.    ││Audit     │            │        │
└────────┘└────────┘└──────────┘            └────────┘
```

---

## Servicios y Puertos

| Servicio | Puerto | Tecnología | Responsabilidad |
|----------|--------|-----------|-----------------|
| Frontend | 80 | Angular + Nginx | UI completa |
| Backend Auth | 8080 | Spring Boot 3.2 | Auth + CRUD + Audit |
| Backend Mail | 8081 | Spring Boot 3.2 | Envío correos + recovery |
| MySQL | 3306 | MySQL 8 | Usuarios, Roles |
| Oracle XE | 1521 | Oracle 21c | Productos, Inventario |
| SQL Server | 1433 | MSSQL 2022 | Logs, Correos, Auditoría |

---

## Estructura del Código

```
fullstack-app/
├── backend-auth/src/main/java/com/fullstack/auth/
│   ├── BackendAuthApplication.java
│   ├── config/
│   │   ├── DataSourceConfig.java       ← Configura 3 DataSources
│   │   ├── SecurityConfig.java         ← Spring Security + JWT
│   │   ├── SwaggerConfig.java          ← OpenAPI docs
│   │   ├── RedisConfig.java            ← Caché
│   │   └── RateLimitFilter.java        ← Rate limiting
│   ├── controller/
│   │   ├── AuthController.java         ← /api/auth/login, /register
│   │   ├── UserController.java         ← /api/users CRUD
│   │   └── ProductController.java      ← /api/products CRUD
│   ├── dto/                             ← Request/Response DTOs
│   ├── entity/
│   │   ├── mysql/                       ← User, Role (MySQL)
│   │   ├── oracle/                      ← Product, Category (Oracle)
│   │   └── sqlserver/                   ← AuditLog (SQL Server)
│   ├── repository/
│   │   ├── mysql/                       ← UserRepository
│   │   ├── oracle/                      ← ProductRepository
│   │   └── sqlserver/                   ← AuditRepository
│   ├── service/
│   │   ├── AuthService.java
│   │   ├── UserService.java
│   │   ├── ProductService.java
│   │   └── AuditService.java
│   ├── security/
│   │   ├── JwtService.java             ← Generate/Validate JWT
│   │   ├── JwtAuthenticationFilter.java
│   │   └── JwtAuthEntryPoint.java
│   └── exception/
│       └── GlobalExceptionHandler.java
├── backend-mail/                        ← Microservicio de correos
├── frontend/src/app/
│   ├── core/services/                   ← auth, user, product, mail
│   ├── core/guards/                     ← auth.guard
│   ├── core/interceptors/               ← JWT interceptor
│   ├── features/auth/                   ← login, register, forgot, reset
│   ├── features/dashboard/
│   ├── features/products/
│   └── features/users/
├── docker/
│   ├── init-scripts/mysql/              ← Schema + seed MySQL
│   ├── init-scripts/oracle/             ← Schema + seed Oracle
│   ├── init-scripts/sqlserver/          ← Schema + seed SQL Server
│   ├── backend-auth.Dockerfile
│   ├── backend-mail.Dockerfile
│   ├── frontend.Dockerfile
│   └── nginx.conf
├── k8s/                                 ← Manifiestos Kubernetes
├── docs/                                ← 15 documentos de diseño
├── docker-compose.yml
└── docker-compose.monitoring.yml
```

---

## Cómo Ejecutar

### Opción 1: Todo con Docker (recomendado)

```bash
cd fullstack-app

# Crear .env desde template
cp .env.example .env

# Levantar todo (primera vez tarda ~5 min por Oracle)
docker compose up --build -d

# Verificar estado
docker compose ps

# Esperar a que Oracle esté healthy (~60-90 segundos)
docker compose logs oracle

# Acceder:
# Frontend: http://localhost
# API Auth: http://localhost:8080/swagger-ui.html
# API Mail: http://localhost:8081
```

### Opción 2: Desarrollo local (BDs en Docker, código local)

```bash
# Solo bases de datos
docker compose up mysql oracle sqlserver -d

# Backend auth (en otra terminal)
cd backend-auth
mvn spring-boot:run

# Backend mail (en otra terminal)
cd backend-mail
mvn spring-boot:run

# Frontend (en otra terminal)
cd frontend
npm install && ng serve
# → http://localhost:4200
```

---

## Credenciales de Prueba

| Usuario | Password | Rol | Creado en |
|---------|----------|-----|-----------|
| admin | Admin123! | ADMIN | MySQL (seed) |
| manager | Manager123! | MANAGER | MySQL (seed) |
| user1 | User123! | USER | MySQL (seed) |

---

## Endpoints del API

### Auth Controller (/api/auth)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| POST | /login | No | Login → JWT token |
| POST | /register | No | Registrar usuario |
| POST | /refresh | JWT | Refresh token |
| POST | /forgot-password | No | Enviar email de recovery |
| POST | /reset-password | Token | Cambiar password |
| GET | /me | JWT | Datos del usuario actual |

### Users Controller (/api/users)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | / | ADMIN | Listar usuarios |
| GET | /{id} | ADMIN | Usuario por ID |
| PUT | /{id} | ADMIN | Actualizar |
| DELETE | /{id} | ADMIN | Desactivar |
| PATCH | /{id}/role | ADMIN | Cambiar rol |

### Products Controller (/api/products)
| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | / | USER+ | Listar (paginado) |
| GET | /{id} | USER+ | Producto por ID |
| POST | / | MANAGER+ | Crear |
| PUT | /{id} | MANAGER+ | Actualizar |
| DELETE | /{id} | ADMIN | Eliminar |

---

## Documentación Adicional

La carpeta `docs/` contiene 15 documentos detallados:

| Archivo | Contenido |
|---------|-----------|
| 00-INDICE-DOCUMENTACION.md | Índice de todo |
| 05-FRONTEND-ANGULAR.md | Arquitectura Angular detallada |
| 06-DOCKER-EXPLICADO.md | Docker línea por línea |
| 07-TESTS-EXPLICADOS.md | Estrategia de testing |
| 08-FLUJO-URL-A-PANTALLA.md | Request completo paso a paso |
| 09-GUIA-LEVANTAR-PROYECTO.md | Setup paso a paso |
| 10-BACKEND-MAIL-EXPLICADO.md | Servicio de correos |
| 11-FRONTEND-2026-QUE-SABER.md | Tendencias frontend |
| 12-BACKEND-JAVA-2026-QUE-SABER.md | Tendencias backend |
| API-REFERENCE.md | Referencia de endpoints |
| BASE-DE-DATOS.md | Esquemas de las 3 BDs |
| DOCUMENTACION-COMPLETA.md | Manual extenso |
| GUIA-DOCKER.md | Docker desde cero |
| GUIA-MENTOR-SENIOR.md | Consejos de carrera |
| MEJORAS-IMPLEMENTADAS.md | Changelog de mejoras |

---

## Diferencia con `fullstack-2026`

| Aspecto | fullstack-app | fullstack-2026 |
|---------|--------------|----------------|
| BDs | 3 (MySQL + Oracle + SQL Server) | 1 (PostgreSQL) |
| Auth | JWT custom | Keycloak (OAuth2) |
| Java | 17 | 25 (LTS) |
| Angular | 17 | 21 |
| Microservicios | 2 (auth + mail) | 1 (monolito) |
| Enfoque | Enterprise multi-BD | Stack moderno minimalista |
| Complejidad | Alta (3 DataSources, Jasypt) | Media (conventions) |
| Para aprender | Cómo conectar múltiples BDs | Mejores prácticas 2026 |
