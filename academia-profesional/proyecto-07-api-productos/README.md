# Proyecto 07: API de Productos con Spring Boot + JPA + PostgreSQL

## ¿Qué problema resuelve?
Construir una API REST avanzada con persistencia real en PostgreSQL usando JPA/Hibernate. Incluye paginación, filtros dinámicos, caché y relaciones entre entidades.

## Tecnologías
- Java 21
- Spring Boot 3.2
- Spring Data JPA (Hibernate)
- PostgreSQL 16
- Spring Cache (Caffeine)
- Docker (base de datos)
- Flyway (migraciones)
- MapStruct (mapeo DTO ↔ Entity)
- Maven

## Funcionalidades
- CRUD completo de productos y categorías
- Paginación y ordenamiento configurable
- Filtros dinámicos (nombre, categoría, precio, stock)
- Caché de consultas frecuentes
- Relaciones JPA: @OneToMany, @ManyToOne
- Migraciones de base de datos con Flyway
- Specification API para queries dinámicas
- Auditoría automática (createdAt, updatedAt)

## Endpoints Principales
- `GET /api/productos?page=0&size=10&sort=nombre` - Paginado
- `GET /api/productos/filtrar?categoria=X&precioMin=10` - Filtros
- `GET /api/categorias/{id}/productos` - Por categoría
- `POST /api/productos` - Crear con validaciones
- `PUT /api/productos/{id}` - Actualizar
- `DELETE /api/productos/{id}` - Eliminar (soft delete)

## Conceptos Clave
- JPA Entities y relaciones (Lazy vs Eager)
- Paginación con Pageable y Page<T>
- Specification pattern para filtros dinámicos
- Caché L1 (Hibernate) y L2 (Caffeine)
- Migraciones versionadas con Flyway
