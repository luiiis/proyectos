# Índice de Documentación del Proyecto Fullstack

## Orden de Lectura Recomendado

### Para entender la arquitectura y conceptos:
1. **[GUIA-MENTOR-SENIOR.md](./GUIA-MENTOR-SENIOR.md)** - Arquitectura general, tecnologías, bases de datos explicadas línea por línea, backend auth completo
2. **[05-FRONTEND-ANGULAR.md](./05-FRONTEND-ANGULAR.md)** - Frontend: SPA, interceptors, guards, servicios, flujo de login
3. **[10-BACKEND-MAIL-EXPLICADO.md](./10-BACKEND-MAIL-EXPLICADO.md)** - Servicio de correos, SMTP, recuperación de contraseña
4. **[06-DOCKER-EXPLICADO.md](./06-DOCKER-EXPLICADO.md)** - Docker, Dockerfiles línea por línea, docker-compose, Nginx
5. **[07-TESTS-EXPLICADOS.md](./07-TESTS-EXPLICADOS.md)** - Qué detecta cada test, unitarios vs integración
6. **[08-FLUJO-URL-A-PANTALLA.md](./08-FLUJO-URL-A-PANTALLA.md)** - Flujo completo paso a paso desde URL hasta render

### Para levantar y usar el proyecto:
7. **[09-GUIA-LEVANTAR-PROYECTO.md](./09-GUIA-LEVANTAR-PROYECTO.md)** - Paso a paso para desarrollo y producción
8. **[GUIA-DOCKER.md](./GUIA-DOCKER.md)** - Comandos Docker, troubleshooting, producción
9. **[BASE-DE-DATOS.md](./BASE-DE-DATOS.md)** - Tablas, relaciones, credenciales, scripts SQL

### Referencia técnica:
10. **[API-REFERENCE.md](./API-REFERENCE.md)** - Todos los endpoints con ejemplos JSON
11. **[MEJORAS-IMPLEMENTADAS.md](./MEJORAS-IMPLEMENTADAS.md)** - Swagger, Redis, Rate Limiting, CI/CD, Kubernetes
12. **[DOCUMENTACION-COMPLETA.md](./DOCUMENTACION-COMPLETA.md)** - Resumen ejecutivo del proyecto

---

## Mapa de Archivos del Proyecto

```
fullstack-app/
│
├── backend-auth/                          ← SERVICIO PRINCIPAL
│   ├── pom.xml                            ← Dependencias Maven
│   └── src/main/java/com/fullstack/auth/
│       ├── BackendAuthApplication.java    ← Punto de entrada (main)
│       ├── config/
│       │   ├── DataSourceConfig.java      ← Conexión a 3 BDs
│       │   ├── SecurityConfig.java        ← Reglas de seguridad
│       │   ├── SwaggerConfig.java         ← Documentación API
│       │   ├── RedisConfig.java           ← Caché
│       │   ├── RateLimitConfig.java       ← Límites de peticiones
│       │   └── RateLimitFilter.java       ← Filtro HTTP rate limit
│       ├── security/
│       │   ├── JwtService.java            ← Generar/validar tokens
│       │   ├── JwtAuthenticationFilter.java ← Filtro que valida JWT
│       │   └── JwtAuthEntryPoint.java     ← Manejo de error 401
│       ├── entity/
│       │   ├── mysql/User.java            ← Tabla users
│       │   ├── mysql/Role.java            ← Tabla roles
│       │   ├── oracle/Product.java        ← Tabla products
│       │   ├── oracle/Inventory.java      ← Tabla inventory_movements
│       │   └── sqlserver/AuditLog.java    ← Tabla audit_logs
│       ├── repository/
│       │   ├── mysql/UserRepository.java  ← Queries de usuarios
│       │   ├── oracle/ProductRepository.java ← Queries de productos
│       │   └── sqlserver/AuditLogRepository.java ← Queries de auditoría
│       ├── dto/
│       │   ├── AuthRequest.java           ← JSON de login
│       │   ├── AuthResponse.java          ← JSON de respuesta login
│       │   ├── RegisterRequest.java       ← JSON de registro
│       │   ├── ProductDto.java            ← JSON de producto
│       │   └── ApiResponse.java           ← Wrapper estándar de respuesta
│       ├── service/
│       │   ├── AuthService.java           ← Lógica de login/register
│       │   ├── UserService.java           ← CRUD usuarios
│       │   ├── ProductService.java        ← CRUD productos + inventario
│       │   └── AuditService.java          ← Registrar acciones
│       ├── controller/
│       │   ├── AuthController.java        ← Endpoints /api/auth/*
│       │   ├── UserController.java        ← Endpoints /api/users/*
│       │   └── ProductController.java     ← Endpoints /api/products/*
│       └── exception/
│           └── GlobalExceptionHandler.java ← Manejo centralizado de errores
│
├── backend-mail/                          ← SERVICIO DE CORREOS
│   └── src/main/java/com/fullstack/mail/
│       ├── BackendMailApplication.java
│       ├── config/SecurityConfig.java
│       ├── entity/
│       │   ├── PasswordResetToken.java
│       │   └── EmailLog.java
│       ├── repository/
│       │   ├── PasswordResetTokenRepository.java
│       │   └── EmailLogRepository.java
│       ├── service/
│       │   ├── EmailService.java          ← Envío SMTP
│       │   └── PasswordResetService.java  ← Lógica de reset
│       └── controller/
│           └── MailController.java        ← Endpoints /api/mail/*
│
├── frontend/                              ← ANGULAR SPA
│   └── src/app/
│       ├── app.config.ts                  ← Providers globales
│       ├── app.routes.ts                  ← Rutas con lazy loading
│       ├── core/
│       │   ├── guards/auth.guard.ts       ← Protección de rutas
│       │   ├── interceptors/auth.interceptor.ts ← Agrega JWT
│       │   ├── models/                    ← Interfaces TypeScript
│       │   └── services/                  ← Comunicación HTTP
│       └── features/
│           ├── auth/login/                ← Pantalla de login
│           ├── auth/register/             ← Pantalla de registro
│           ├── auth/forgot-password/      ← Recuperar contraseña
│           ├── auth/reset-password/       ← Nueva contraseña
│           ├── dashboard/                 ← Página principal
│           ├── users/                     ← Gestión de usuarios
│           └── products/                  ← Gestión de productos
│
├── docker/
│   ├── backend-auth.Dockerfile            ← Imagen del backend auth
│   ├── backend-mail.Dockerfile            ← Imagen del backend mail
│   ├── frontend.Dockerfile                ← Imagen del frontend
│   ├── nginx.conf                         ← Configuración reverse proxy
│   ├── ssl/                               ← Certificados HTTPS
│   ├── monitoring/                        ← Prometheus + Grafana
│   └── init-scripts/
│       ├── mysql/01-schema.sql            ← Crear tablas MySQL
│       ├── mysql/02-seed-data.sql         ← Datos iniciales MySQL
│       ├── oracle/01-schema.sql           ← Crear tablas Oracle
│       ├── oracle/02-seed-data.sql        ← Datos iniciales Oracle
│       ├── sqlserver/01-schema.sql        ← Crear tablas SQL Server
│       └── sqlserver/02-seed-data.sql     ← Datos iniciales SQL Server
│
├── k8s/                                   ← Kubernetes manifests
├── .github/workflows/ci-cd.yml            ← Pipeline CI/CD
├── docker-compose.yml                     ← Orquestación principal
├── docker-compose.monitoring.yml          ← Redis + Prometheus + Grafana
├── .env.example                           ← Variables de entorno ejemplo
└── docs/                                  ← TODA la documentación
```
