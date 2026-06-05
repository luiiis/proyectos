# Proyecto 04: Sistema de Clientes con JDBC

## ¿Qué problema resuelve?
Aprender conexión directa a base de datos desde Java usando JDBC puro. CRUD completo de clientes con PostgreSQL, PreparedStatement para seguridad y transacciones para integridad.

## Tecnologías
- Java 21
- JDBC (java.sql)
- PostgreSQL 16
- Docker (para la base de datos)
- PreparedStatement (prevención SQL injection)
- Connection pooling manual
- Patrón DAO (Data Access Object)

## Funcionalidades
- Conexión a PostgreSQL desde Java
- CRUD completo de clientes
- Búsqueda con filtros (nombre, email, ciudad)
- Paginación de resultados
- Transacciones (commit/rollback)
- Manejo de excepciones SQL
- Pool de conexiones básico
- Exportar listado a consola formateada

## Estructura del Proyecto
- `DatabaseConnection` - singleton de conexión
- `Cliente` - modelo de datos
- `ClienteDAO` - operaciones CRUD
- `ClienteService` - lógica de negocio
- `Main` - menú interactivo

## Conceptos Clave
- PreparedStatement vs Statement (seguridad)
- Transacciones ACID en Java
- Patrón DAO para separar acceso a datos
- Manejo correcto de recursos (try-with-resources)
- Connection string y configuración JDBC
