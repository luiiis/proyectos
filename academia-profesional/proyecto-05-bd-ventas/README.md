# Proyecto 05: Base de Datos de Ventas

## ¿Qué problema resuelve?
Diseñar e implementar una base de datos relacional completa para un sistema de ventas. SQL puro con 16 tablas normalizadas, triggers, stored procedures, views e índices optimizados.

## Tecnologías
- PostgreSQL 16
- SQL DDL (CREATE, ALTER, DROP)
- SQL DML (INSERT, UPDATE, DELETE, SELECT)
- PL/pgSQL (procedimientos y funciones)
- Docker para el motor de base de datos

## Funcionalidades
- 16 tablas normalizadas (3FN)
- Triggers para auditoría automática
- Stored procedures para operaciones complejas
- Views para reportes predefinidos
- Índices para optimización de consultas
- Constraints (PK, FK, CHECK, UNIQUE)
- Datos de prueba (seeds)
- Scripts de backup y restore

## Tablas Principales
- `clientes`, `productos`, `categorias`
- `ventas`, `detalle_ventas`, `facturas`
- `empleados`, `sucursales`, `proveedores`
- `inventario`, `movimientos_inventario`
- `formas_pago`, `descuentos`
- `auditoria`, `configuracion`, `reportes_cache`

## Conceptos Clave
- Normalización hasta 3FN
- Integridad referencial con FK y CASCADE
- Triggers BEFORE/AFTER para lógica automática
- Procedures para transacciones complejas
- Views materializadas para reportes pesados
- Índices B-Tree, GIN y parciales
