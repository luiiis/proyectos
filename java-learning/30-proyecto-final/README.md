# Módulo 30: Proyecto Final - Sistema Enterprise Completo

## Objetivo
Construir un sistema de gestión empresarial que demuestre dominio de TODO lo aprendido en los 29 módulos anteriores.

## Stack Tecnológico
- Java 21+ (Records, Virtual Threads, Pattern Matching)
- Spring Boot 3.3
- PostgreSQL 16
- Redis 7 (caché)
- Docker + Docker Compose
- JWT + Spring Security
- Swagger/OpenAPI
- JUnit 5 + Mockito + Testcontainers
- Hexagonal Architecture

## Módulos del Sistema

### 1. Autenticación
- Login/Register con JWT
- Refresh tokens
- Roles: ADMIN, GERENTE, VENDEDOR, ALMACENISTA
- Permisos granulares

### 2. Usuarios y Roles
- CRUD de usuarios
- Asignación de roles
- Activar/desactivar

### 3. Productos
- CRUD con validaciones
- Categorías y proveedores
- Búsqueda y filtros
- Paginación

### 4. Inventario
- Stock por sucursal
- Entradas/salidas con motivo
- Alertas de stock bajo
- Transferencias entre sucursales

### 5. Ventas
- Registrar venta completa
- Calcular IVA automáticamente
- Descontar inventario
- Cancelar/devolver

### 6. Reportes
- Ventas por período
- Top productos/clientes/vendedores
- Dashboard con KPIs

### 7. Auditoría
- Log de todas las operaciones
- Quién, qué, cuándo, desde dónde

## Arquitectura
```
src/main/java/com/empresa/
├── domain/                    ← NÚCLEO (sin dependencias externas)
│   ├── model/                 ← Entidades y Value Objects
│   ├── port/
│   │   ├── input/             ← Use Cases (interfaces)
│   │   └── output/            ← Repository interfaces
│   └── exception/             ← Excepciones de dominio
├── application/               ← CASOS DE USO
│   ├── service/               ← Implementación de use cases
│   └── dto/                   ← Request/Response DTOs
├── infrastructure/            ← ADAPTADORES
│   ├── persistence/           ← JPA entities + repositories
│   ├── web/                   ← Controllers REST
│   ├── security/              ← JWT + Spring Security
│   └── config/                ← Configuración Spring
└── shared/                    ← Utilidades compartidas
```

## Criterios de Evaluación
- [ ] Funcionalidad completa (todos los módulos)
- [ ] Clean Architecture / Hexagonal
- [ ] Tests con cobertura > 70%
- [ ] Docker Compose funcional
- [ ] Swagger documentado
- [ ] Manejo de errores consistente
- [ ] Seguridad implementada correctamente
- [ ] Código limpio (SOLID, naming, sin duplicación)
- [ ] Performance (caché, índices, paginación)
- [ ] README con instrucciones claras

## Entregables
1. Código en repositorio Git
2. Docker Compose que levante todo
3. Documentación Swagger
4. Colección Postman
5. Al menos 100 tests pasando
6. README con setup instructions

## ¡Felicidades!
Si completaste este proyecto, tienes el nivel de un Java Backend Developer Mid-Senior.
Para Senior/Architect: experiencia en producción, sistemas distribuidos a escala, y mentoring.
