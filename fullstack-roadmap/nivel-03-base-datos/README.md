# Nivel 3: Backend con Base de Datos — Proyecto 3: Productos y Categorías

## Objetivo
Conectar Spring Boot a una base de datos REAL usando MyBatis. Aprender SQL, migraciones y consultas avanzadas.

## Funcionalidades
- CRUD de categorías
- CRUD de productos
- Buscar productos por nombre
- Filtrar por categoría
- Controlar precio, existencia y estado
- Paginación

## Tecnologías
- Spring Boot 3.3
- MyBatis
- MySQL o PostgreSQL
- Flyway (migraciones)
- DBeaver (GUI)

## 5 Versiones

| Versión | Qué agrega |
|---------|------------|
| V1 | Crear tablas manualmente con SQL |
| V2 | Organizar scripts (ddl/, dml/, consultas/) |
| V3 | Flyway para migraciones automáticas |
| V4 | MyBatis básico (Mapper + XML + queries) |
| V5 | Consultas avanzadas (JOIN, paginación, filtros) |

## Tablas
```sql
categorias (id, nombre, descripcion, activa, created_at)
productos (id, nombre, descripcion, precio, existencia, categoria_id, activo, created_at)
```

## Documentación
- Modelo entidad-relación
- Diccionario de datos
- Scripts de instalación
- Scripts de datos de prueba
- Manual de conexión a BD
- Catálogo de consultas
- Pruebas CRUD con Postman
