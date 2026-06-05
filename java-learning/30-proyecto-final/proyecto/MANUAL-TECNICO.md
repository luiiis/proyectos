# Módulo 30: Proyecto Final Enterprise - Manual Técnico

## ¿Qué construimos?
Sistema empresarial completo que integra TODO lo aprendido en los 29 módulos:

| Módulo | Qué se aplica |
|--------|--------------|
| 03-04 | Java básico + POO (entidades, herencia) |
| 05 | Colecciones (listas de productos, mapas de config) |
| 06 | Excepciones custom (BusinessException, NotFoundException) |
| 07 | Generics (ApiResponse<T>, Repository<T>) |
| 08-09 | Streams + Lambdas (transformar datos, filtros) |
| 14 | Design Patterns (Builder, Strategy, Factory) |
| 15 | Testing (JUnit 5 + Mockito, cobertura > 70%) |
| 16 | Maven (gestión de dependencias) |
| 18-19 | Spring Core + Boot (IoC, DI, auto-config) |
| 20 | JPA/Hibernate (entities, relationships) |
| 21 | REST API (CRUD, paginación, Swagger) |
| 22-23 | Security + JWT (login, roles, permisos) |
| 24 | Docker (Dockerfile + Compose) |
| 29 | Arquitectura (Hexagonal / Clean) |

## Arquitectura del Proyecto

```
src/main/java/com/empresa/
├── domain/                         ← NÚCLEO (sin dependencias de framework)
│   ├── model/
│   │   ├── Producto.java          ← Entidad de dominio
│   │   ├── Venta.java
│   │   ├── Usuario.java
│   │   └── valueobject/
│   │       └── Dinero.java        ← Value Object inmutable
│   ├── port/
│   │   ├── input/                 ← Casos de uso (interfaces)
│   │   │   ├── CrearProductoUseCase.java
│   │   │   └── RegistrarVentaUseCase.java
│   │   └── output/                ← Puertos de salida (interfaces)
│   │       ├── ProductoRepository.java
│   │       └── VentaRepository.java
│   └── exception/
│       ├── BusinessException.java
│       └── NotFoundException.java
│
├── application/                    ← CASOS DE USO (implementación)
│   ├── service/
│   │   ├── ProductoServiceImpl.java
│   │   └── VentaServiceImpl.java
│   └── dto/
│       ├── ProductoRequest.java
│       └── ProductoResponse.java
│
├── infrastructure/                 ← ADAPTADORES (frameworks)
│   ├── persistence/
│   │   ├── entity/                ← Entidades JPA (separadas del dominio)
│   │   │   └── ProductoJpaEntity.java
│   │   ├── repository/
│   │   │   └── ProductoJpaRepository.java
│   │   └── adapter/
│   │       └── ProductoRepositoryAdapter.java
│   ├── web/
│   │   ├── controller/
│   │   │   └── ProductoController.java
│   │   └── exception/
│   │       └── GlobalExceptionHandler.java
│   ├── security/
│   │   ├── JwtService.java
│   │   └── SecurityConfig.java
│   └── config/
│       └── AppConfig.java
│
└── shared/
    └── ApiResponse.java
```

## Cómo levantar

```bash
cd java-learning/30-proyecto-final/proyecto

# Opción 1: Con Docker (recomendado)
docker compose up --build -d
# API: http://localhost:8080
# Swagger: http://localhost:8080/swagger-ui.html
# PostgreSQL: localhost:5432

# Opción 2: Local (necesitas PostgreSQL corriendo)
cd app
mvn spring-boot:run
```

## Endpoints principales

```
POST   /api/auth/register    → Registrar usuario
POST   /api/auth/login       → Login (devuelve JWT)
GET    /api/productos        → Listar (paginado)
POST   /api/productos        → Crear (ADMIN/GERENTE)
PUT    /api/productos/{id}   → Actualizar
DELETE /api/productos/{id}   → Eliminar (soft delete)
POST   /api/ventas           → Registrar venta completa
GET    /api/reportes/ventas  → Reporte de ventas por período
GET    /api/inventario/alertas → Productos con stock bajo
```

## Criterios de completitud
- [ ] CRUD completo de productos con validaciones
- [ ] Registro de ventas con descuento de inventario
- [ ] Autenticación JWT (login + refresh token)
- [ ] Roles: ADMIN, GERENTE, VENDEDOR
- [ ] Paginación en todas las listas
- [ ] Swagger documentado
- [ ] Docker Compose funcional
- [ ] Tests con cobertura > 70%
- [ ] Arquitectura hexagonal respetada
- [ ] Manejo global de errores
