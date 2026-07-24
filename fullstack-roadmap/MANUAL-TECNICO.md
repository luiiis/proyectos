# Manual Técnico — Fullstack Roadmap

## Arquitectura General (aplica a niveles 3-14)

```
┌─────────────────────────────────────────────────────────────┐
│  FRONTEND (Angular 20)                                       │
│  Standalone Components + Signals + Material + Reactive Forms │
│  Puerto: 4200 (dev) / 80 (Docker con Nginx)                │
└──────────────────────────┬──────────────────────────────────┘
                           │ HTTP + JSON + JWT en header
                           ▼
┌─────────────────────────────────────────────────────────────┐
│  BACKEND (Spring Boot 3.3 + Java 21)                         │
│  Controller → Service → Repository (MyBatis) → MySQL         │
│  + Spring Security + JWT + Validaciones + Manejo de errores  │
│  Puerto: 8080                                                │
└──────────────────────────┬──────────────────────────────────┘
                           │ JDBC (MyBatis)
                           ▼
┌─────────────────────────────────────────────────────────────┐
│  BASE DE DATOS (MySQL 8)                                     │
│  Flyway para migraciones | Tablas normalizadas | Índices     │
│  Puerto: 3306                                                │
└─────────────────────────────────────────────────────────────┘
```

---

## Stack Tecnológico

| Capa | Tecnología | Versión | Justificación |
|------|-----------|---------|---------------|
| Lenguaje | Java | 21 LTS | Estable, enterprise, virtual threads |
| Framework | Spring Boot | 3.3 | Auto-config, ecosistema maduro |
| Build | Maven | 3.9+ | Estándar en enterprise Java |
| Persistencia | MyBatis | 3.0.3 | Control total del SQL, no ORM |
| Migraciones | Flyway | 10+ | BD versionada en Git |
| BD | MySQL | 8.0 | Popular, buena documentación |
| Seguridad | Spring Security | 6 | Estándar, JWT + OAuth2 |
| JWT | jjwt | 0.12.5 | Librería más usada |
| Frontend | Angular | 20 | Signals, standalone, Material |
| UI | Angular Material | 20 | Componentes listos |
| Contenedor | Docker | 24+ | Portabilidad |
| CI/CD | GitHub Actions | — | Integrado con GitHub |
| Tests | JUnit 5 + Mockito | — | Estándar de testing Java |

---

## Estructura de Cada Proyecto (Niveles 2-14)

```
nivel-XX-nombre/
├── README.md              ← Descripción + conceptos + versiones
├── COMO_EJECUTAR.md       ← Paso a paso para levantarlo
├── proyecto/
│   ├── backend/
│   │   ├── pom.xml
│   │   ├── Dockerfile
│   │   └── src/
│   │       ├── main/java/com/softwarelee/...
│   │       │   ├── controller/    ← Endpoints REST
│   │       │   ├── service/       ← Lógica de negocio
│   │       │   ├── mapper/        ← MyBatis (queries SQL)
│   │       │   ├── model/         ← Entidades
│   │       │   ├── dto/           ← Request/Response objects
│   │       │   ├── security/      ← JWT + filtros (nivel 5+)
│   │       │   └── exception/     ← Manejo de errores
│   │       └── main/resources/
│   │           ├── application.yml
│   │           ├── mapper/*.xml          ← SQL de MyBatis
│   │           └── db/migration/*.sql    ← Flyway
│   ├── frontend/ (nivel 8+)
│   │   ├── angular.json
│   │   ├── package.json
│   │   ├── Dockerfile
│   │   ├── nginx.conf
│   │   └── src/app/
│   │       ├── core/services/
│   │       ├── core/guards/
│   │       ├── core/interceptors/
│   │       └── features/
│   ├── docker-compose.yml (nivel 14)
│   └── COMO_EJECUTAR.md
```

---

## Convenciones del Proyecto

### Nombres
| Tipo | Convención | Ejemplo |
|------|-----------|---------|
| Clase Java | PascalCase | `ProductoService` |
| Método Java | camelCase | `buscarPorId()` |
| Variable | camelCase | `productoActivo` |
| Constante | UPPER_SNAKE | `MAX_INTENTOS` |
| Tabla BD | snake_case plural | `productos`, `usuarios_roles` |
| Columna BD | snake_case | `fecha_creacion`, `categoria_id` |
| Endpoint | kebab-case | `/api/productos/stock-bajo` |
| Componente Angular | kebab-case | `producto-lista.component.ts` |

### Códigos HTTP usados
| Código | Cuándo | Ejemplo |
|--------|--------|---------|
| 200 | Operación exitosa | GET, PUT exitoso |
| 201 | Recurso creado | POST exitoso |
| 204 | Éxito sin body | DELETE exitoso |
| 400 | Datos inválidos | Validación fallida |
| 401 | No autenticado | Sin token o token inválido |
| 403 | Sin permisos | Token válido pero sin rol |
| 404 | No encontrado | ID no existe |
| 500 | Error interno | Bug no manejado |

### Formato de respuestas JSON
```json
// Éxito:
{
  "success": true,
  "message": "Producto creado",
  "data": { ... }
}

// Error:
{
  "success": false,
  "message": "Producto no encontrado con ID: 99",
  "status": 404,
  "timestamp": "2026-07-23T10:30:00"
}

// Lista con paginación:
{
  "success": true,
  "data": [ ... ],
  "total": 150,
  "page": 0,
  "size": 10,
  "totalPages": 15
}
```

---

## Seguridad (Niveles 5-14)

### Flujo JWT:
```
1. POST /api/auth/login → {username, password}
2. Backend valida credenciales (BCrypt)
3. Genera accessToken (24h) + refreshToken (7 días)
4. Cliente guarda tokens
5. En cada request: Authorization: Bearer <accessToken>
6. JwtFilter valida firma + expiración
7. Si válido → extrae usuario + roles → SecurityContext
8. Controller accede al usuario autenticado
```

### Roles y permisos:
```
ADMIN → todo
SUPERVISOR → CRUD + reportes (sin eliminar usuarios)
CAJERO → ventas + consultar productos
ALMACENISTA → inventario + productos
```

---

## Base de Datos

### Migraciones con Flyway:
```
resources/db/migration/
├── V1__crear_tablas_base.sql        ← Se ejecuta primero
├── V2__datos_iniciales.sql          ← Seed data
├── V3__agregar_campo_imagen.sql     ← Cambio posterior
└── V4__crear_tabla_auditoria.sql    ← Otro cambio
```
**Regla**: NUNCA modificar un archivo V ya ejecutado. Siempre crear uno nuevo.

### MyBatis vs JPA:
| Aspecto | MyBatis (este roadmap) | JPA/Hibernate |
|---------|----------------------|---------------|
| SQL | TÚ lo escribes | Se genera automáticamente |
| Control | Total | Limitado |
| Performance | Óptima (tú optimizas) | Puede generar queries malos |
| Aprendizaje | Necesitas saber SQL | Menos SQL necesario |
| Uso enterprise | México/Latam muy común | Global más común |

---

## Docker (Nivel 14)

### Multi-stage build:
```
Stage 1 (BUILD): Maven + JDK → compila → genera JAR
Stage 2 (RUN): Solo JRE Alpine → ejecuta JAR (imagen ~200MB)
```

### docker-compose servicios:
```yaml
mysql:    → BD (puerto 3306, volumen persistente)
backend:  → Spring Boot (puerto 8080, depende de mysql)
frontend: → Angular + Nginx (puerto 80, depende de backend)
```

---

## Testing (Nivel 4)

### Pirámide:
```
        /  E2E  \              ← Pocos (flujos críticos)
       / Integración \         ← Medios (API + BD)
      /   Unitarios   \        ← Muchos (lógica aislada)
```

### Herramientas:
- **JUnit 5**: framework de testing
- **Mockito**: simular dependencias
- **MockMvc**: probar controllers sin servidor
- **Testcontainers**: BD real en Docker para tests
- **JaCoCo**: reporte de cobertura (meta: 80%)
