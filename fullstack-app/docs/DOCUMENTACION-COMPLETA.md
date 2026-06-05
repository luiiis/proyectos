# Documentación Completa del Sistema Fullstack

## 1. Visión General

Sistema de gestión empresarial fullstack que incluye:
- Autenticación con JWT y roles
- CRUD de usuarios con gestión de permisos
- CRUD de productos con control de inventario
- Servicio de correos para recuperación de contraseña
- Auditoría de acciones en SQL Server
- 3 bases de datos diferentes (MySQL, Oracle, SQL Server)

---

## 2. Arquitectura

### 2.1 Patrón Arquitectónico
- **Backend**: Arquitectura en capas (Controller → Service → Repository → Entity)
- **Frontend**: Standalone Components con lazy loading
- **Comunicación**: REST API con JSON + JWT Bearer Token
- **Bases de datos**: Multi-datasource con JPA

### 2.2 Distribución de Datos

| Base de Datos | Propósito | Entidades |
|---------------|-----------|-----------|
| MySQL 8 | Autenticación y usuarios | User, Role, PasswordResetToken, EmailLog |
| Oracle XE 21c | Productos e inventario | Product, Inventory |
| SQL Server 2022 | Auditoría y logs | AuditLog |

---

## 3. Backend Auth (Puerto 8080)

### 3.1 Estructura de Paquetes
```
com.fullstack.auth/
├── config/           → Configuración (DataSource, Security)
├── controller/       → REST Controllers
├── dto/              → Data Transfer Objects
├── entity/
│   ├── mysql/        → Entidades MySQL (User, Role)
│   ├── oracle/       → Entidades Oracle (Product, Inventory)
│   └── sqlserver/    → Entidades SQL Server (AuditLog)
├── exception/        → Manejo global de errores
├── repository/
│   ├── mysql/        → Repos MySQL
│   ├── oracle/       → Repos Oracle
│   └── sqlserver/    → Repos SQL Server
├── security/         → JWT Filter, Service, EntryPoint
└── service/          → Lógica de negocio
```

### 3.2 Seguridad

#### Flujo de Autenticación
1. Usuario envía credenciales a `POST /api/auth/login`
2. Spring Security valida con `DaoAuthenticationProvider`
3. Se genera JWT (access + refresh token)
4. Cliente almacena tokens en localStorage
5. Cada request incluye `Authorization: Bearer <token>`
6. `JwtAuthenticationFilter` valida el token en cada request

#### Encriptación
- **Contraseñas de usuario**: BCrypt con strength 12
- **Contraseñas de BD en properties**: Jasypt (PBEWithMD5AndDES)
- **JWT**: HMAC-SHA256 con clave de 256 bits

### 3.3 Endpoints

#### Autenticación (públicos)
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/auth/register | Registrar usuario |
| POST | /api/auth/login | Iniciar sesión |
| POST | /api/auth/refresh | Renovar token |

#### Usuarios (requiere autenticación)
| Método | Endpoint | Rol requerido |
|--------|----------|---------------|
| GET | /api/users | ADMIN |
| GET | /api/users/{id} | ADMIN o propio |
| PUT | /api/users/{id} | ADMIN o propio |
| DELETE | /api/users/{id} | ADMIN |
| POST | /api/users/{id}/roles | ADMIN |
| DELETE | /api/users/{id}/roles/{role} | ADMIN |
| POST | /api/users/{id}/change-password | Autenticado |

#### Productos (requiere autenticación)
| Método | Endpoint | Rol requerido |
|--------|----------|---------------|
| GET | /api/products | Autenticado |
| GET | /api/products/{id} | Autenticado |
| GET | /api/products/search?name= | Autenticado |
| GET | /api/products/category/{cat} | Autenticado |
| GET | /api/products/low-stock | ADMIN/MANAGER |
| POST | /api/products | ADMIN/MANAGER |
| PUT | /api/products/{id} | ADMIN/MANAGER |
| DELETE | /api/products/{id} | ADMIN |
| POST | /api/products/{id}/stock/add | ADMIN/MANAGER |
| POST | /api/products/{id}/stock/remove | ADMIN/MANAGER |

### 3.4 Configuración Multi-DataSource

El sistema usa `@Configuration` separadas para cada BD:
- Cada DataSource tiene su propio `EntityManagerFactory`
- Cada uno tiene su propio `TransactionManager`
- Los repositorios se separan por paquete (`repository.mysql`, `repository.oracle`, etc.)
- Las entidades se separan por paquete (`entity.mysql`, `entity.oracle`, etc.)

---

## 4. Backend Mail (Puerto 8081)

### 4.1 Funcionalidades
- Envío de correos HTML via SMTP
- Generación de tokens de recuperación de contraseña
- Validación y expiración de tokens
- Log de todos los correos enviados

### 4.2 Endpoints
| Método | Endpoint | Descripción |
|--------|----------|-------------|
| POST | /api/mail/send | Enviar correo (autenticado) |
| POST | /api/mail/password-reset/request | Solicitar recuperación |
| POST | /api/mail/password-reset/validate | Validar token |
| POST | /api/mail/password-reset/confirm | Confirmar reset |
| POST | /api/mail/welcome | Enviar bienvenida |

### 4.3 Configuración de Gmail
Para usar Gmail como SMTP:
1. Activar verificación en 2 pasos en tu cuenta Google
2. Generar una "Contraseña de aplicación" en https://myaccount.google.com/apppasswords
3. Usar esa contraseña de 16 caracteres como `MAIL_PASSWORD`

---

## 5. Frontend Angular

### 5.1 Estructura
```
src/app/
├── app.component.ts      → Componente raíz
├── app.config.ts         → Configuración (providers)
├── app.routes.ts         → Rutas con lazy loading
├── core/
│   ├── guards/           → AuthGuard
│   ├── interceptors/     → JWT Interceptor
│   ├── models/           → Interfaces TypeScript
│   └── services/         → Servicios HTTP
└── features/
    ├── auth/
    │   ├── login/
    │   ├── register/
    │   ├── forgot-password/
    │   └── reset-password/
    ├── dashboard/
    ├── users/
    └── products/
```

### 5.2 Características
- **Standalone Components**: Sin NgModules, cada componente es independiente
- **Lazy Loading**: Cada ruta carga su componente bajo demanda
- **JWT Interceptor**: Agrega automáticamente el token a cada request
- **Auth Guard**: Protege rutas que requieren autenticación
- **Angular Material**: UI consistente y accesible
- **Reactive Forms**: Validación en formularios

### 5.3 Flujo de Autenticación (Frontend)
1. Login → AuthService.login() → Almacena token en localStorage
2. Interceptor agrega `Authorization: Bearer <token>` a cada request
3. Si recibe 401 → Logout automático → Redirige a /login
4. AuthGuard verifica token antes de acceder a rutas protegidas

---

## 6. Docker y Despliegue

### 6.1 Requisitos
- Docker 24+
- Docker Compose v2+
- Mínimo 8GB RAM (Oracle consume bastante)

### 6.2 Comandos de Despliegue

```bash
# 1. Clonar y entrar al directorio
cd fullstack-app

# 2. Crear archivo .env desde el ejemplo
cp .env.example .env
# Editar .env con tus valores reales

# 3. Construir y levantar todo
docker compose up --build -d

# 4. Ver logs
docker compose logs -f

# 5. Ver estado de servicios
docker compose ps

# 6. Detener todo
docker compose down

# 7. Detener y eliminar volúmenes (CUIDADO: borra datos)
docker compose down -v
```

### 6.3 Puertos Expuestos
| Servicio | Puerto |
|----------|--------|
| Frontend (Nginx) | 80 |
| Backend Auth | 8080 |
| Backend Mail | 8081 |
| MySQL | 3306 |
| Oracle | 1521 |
| SQL Server | 1433 |

### 6.4 Dockerfiles - Multi-stage Build
Cada Dockerfile usa multi-stage build:
1. **Build stage**: Compila el código (Maven para Java, Node para Angular)
2. **Runtime stage**: Solo incluye el artefacto final (JAR o archivos estáticos)

Beneficios:
- Imágenes más pequeñas
- No incluye herramientas de build en producción
- Más seguro (menos superficie de ataque)

### 6.5 Nginx como Reverse Proxy
El frontend Angular se sirve desde Nginx que también actúa como reverse proxy:
- `/` → Archivos estáticos de Angular
- `/api/` → Proxy a backend-auth:8080
- `/api/mail/` → Proxy a backend-mail:8081

---

## 7. Seguridad

### 7.1 Capas de Seguridad
1. **Red**: Docker network aislada (solo frontend expone puerto 80)
2. **Transporte**: HTTPS (configurar con certificado en producción)
3. **Autenticación**: JWT con expiración
4. **Autorización**: Roles (ADMIN, MANAGER, USER)
5. **Datos**: BCrypt para passwords, Jasypt para configs
6. **Contenedores**: Usuarios no-root en Dockerfiles

### 7.2 Encriptar Passwords de BD con Jasypt

```bash
# Generar password encriptado
java -cp jasypt-1.9.3.jar org.jasypt.intf.cli.JasyptPBEStringEncryptionCLI \
  input="tuPasswordReal" \
  password="masterEncryptionKey" \
  algorithm=PBEWithMD5AndDES

# Usar en application.yml
spring:
  datasource:
    password: ENC(resultadoEncriptado)
```

---

## 8. Guía de Desarrollo Local

### 8.1 Sin Docker (desarrollo)

```bash
# Backend Auth
cd backend-auth
mvn spring-boot:run

# Backend Mail (otra terminal)
cd backend-mail
mvn spring-boot:run

# Frontend (otra terminal)
cd frontend
npm install
ng serve
```

### 8.2 Variables de entorno necesarias
Configurar en tu IDE o terminal:
- `MYSQL_HOST=localhost`
- `ORACLE_HOST=localhost`
- `SQLSERVER_HOST=localhost`
- `JWT_SECRET=...`
- `MAIL_USERNAME=...`
- `MAIL_PASSWORD=...`

---

## 9. Tecnologías y Conceptos Cubiertos

### Backend
- [x] Spring Boot 3.2
- [x] Spring Security 6
- [x] JWT (JSON Web Tokens)
- [x] Spring Data JPA
- [x] Multi-DataSource (MySQL + Oracle + SQL Server)
- [x] BCrypt Password Encoding
- [x] Jasypt Property Encryption
- [x] REST API Design
- [x] DTO Pattern
- [x] Global Exception Handling
- [x] CORS Configuration
- [x] Role-Based Access Control (RBAC)
- [x] Audit Logging
- [x] Spring Mail (SMTP)
- [x] Password Reset Flow

### Frontend
- [x] Angular 17 Standalone Components
- [x] Lazy Loading Routes
- [x] HTTP Interceptors
- [x] Route Guards
- [x] Reactive Forms + Validation
- [x] Angular Material UI
- [x] Service Layer Pattern
- [x] Environment Configuration
- [x] JWT Token Management

### DevOps
- [x] Docker Multi-stage Builds
- [x] Docker Compose Orchestration
- [x] Nginx Reverse Proxy
- [x] Health Checks
- [x] Volume Persistence
- [x] Environment Variables
- [x] Network Isolation

---

## 10. Próximos Pasos / Mejoras

- [x] Agregar Swagger/OpenAPI para documentación de API
- [x] Implementar rate limiting
- [x] Agregar tests unitarios e integración
- [x] CI/CD con GitHub Actions
- [x] Monitoreo con Prometheus + Grafana
- [x] Certificados SSL con Let's Encrypt
- [x] Kubernetes deployment manifests
- [x] Redis para cache de sesiones

> Documentación detallada de cada mejora en: `docs/MEJORAS-IMPLEMENTADAS.md`
