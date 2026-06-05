# Módulo 02: DDL - Data Definition Language

## ¿Qué es DDL?
DDL son los comandos SQL para CREAR, MODIFICAR y ELIMINAR la ESTRUCTURA de la base de datos (tablas, índices, constraints). No manipulan datos, manipulan la estructura.

```
DDL = Define la FORMA del contenedor
DML = Manipula el CONTENIDO del contenedor
```

---

## 1. CREATE DATABASE

```sql
-- PostgreSQL
CREATE DATABASE empresa_db
    ENCODING = 'UTF8'
    LC_COLLATE = 'es_MX.UTF-8';

-- MySQL
CREATE DATABASE empresa_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- Oracle (se crean tablespaces, no databases directamente)
-- En Oracle XE usas el PDB (Pluggable Database) existente: XEPDB1
```

---

## 2. CREATE TABLE

### Sintaxis básica:
```sql
CREATE TABLE nombre_tabla (
    columna1  TIPO_DATO  CONSTRAINTS,
    columna2  TIPO_DATO  CONSTRAINTS,
    ...
);
```

### Tipos de datos principales:

| Concepto | PostgreSQL | MySQL | Oracle |
|----------|-----------|-------|--------|
| Entero | INTEGER, BIGINT | INT, BIGINT | NUMBER(10) |
| Decimal | NUMERIC(10,2) | DECIMAL(10,2) | NUMBER(10,2) |
| Texto corto | VARCHAR(100) | VARCHAR(100) | VARCHAR2(100) |
| Texto largo | TEXT | TEXT | CLOB |
| Fecha | DATE | DATE | DATE |
| Fecha+hora | TIMESTAMP | DATETIME/TIMESTAMP | TIMESTAMP |
| Booleano | BOOLEAN | BOOLEAN/TINYINT(1) | NUMBER(1) |
| Auto-increment | SERIAL | AUTO_INCREMENT | SEQUENCE + DEFAULT |
| JSON | JSONB | JSON | CLOB/JSON |

### Ejemplo completo:
```sql
-- PostgreSQL
CREATE TABLE productos (
    id          SERIAL PRIMARY KEY,           -- Auto-increment + PK
    nombre      VARCHAR(150) NOT NULL,        -- Obligatorio
    precio      NUMERIC(10,2) NOT NULL CHECK (precio > 0),  -- Validación
    stock       INTEGER DEFAULT 0,            -- Valor por defecto
    sku         VARCHAR(50) UNIQUE,           -- No se puede repetir
    categoria_id INTEGER REFERENCES categorias(id),  -- FK
    activo      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT NOW()
);
```

---

## 3. Constraints (Restricciones)

| Constraint | Qué hace | Ejemplo |
|-----------|----------|---------|
| PRIMARY KEY | Identificador único, no null | `id SERIAL PRIMARY KEY` |
| NOT NULL | No permite valores vacíos | `nombre VARCHAR(50) NOT NULL` |
| UNIQUE | No permite duplicados | `email VARCHAR(100) UNIQUE` |
| CHECK | Valida una condición | `CHECK (precio > 0)` |
| DEFAULT | Valor si no se especifica | `activo BOOLEAN DEFAULT TRUE` |
| FOREIGN KEY | Referencia a otra tabla | `REFERENCES categorias(id)` |

---

## 4. ALTER TABLE

```sql
-- Agregar columna
ALTER TABLE productos ADD COLUMN peso NUMERIC(5,2);

-- Eliminar columna
ALTER TABLE productos DROP COLUMN peso;

-- Modificar tipo de dato
-- PostgreSQL:
ALTER TABLE productos ALTER COLUMN nombre TYPE VARCHAR(200);
-- MySQL:
ALTER TABLE productos MODIFY COLUMN nombre VARCHAR(200);
-- Oracle:
ALTER TABLE productos MODIFY nombre VARCHAR2(200);

-- Agregar constraint
ALTER TABLE productos ADD CONSTRAINT chk_stock CHECK (stock >= 0);

-- Agregar FK
ALTER TABLE productos ADD CONSTRAINT fk_categoria
    FOREIGN KEY (categoria_id) REFERENCES categorias(id);

-- Renombrar columna
-- PostgreSQL:
ALTER TABLE productos RENAME COLUMN nombre TO nombre_producto;
-- MySQL:
ALTER TABLE productos CHANGE nombre nombre_producto VARCHAR(150);
```

---

## 5. DROP y TRUNCATE

```sql
-- DROP: Elimina la tabla COMPLETA (estructura + datos)
DROP TABLE IF EXISTS productos;  -- IF EXISTS evita error si no existe

-- TRUNCATE: Elimina TODOS los datos pero mantiene la estructura
TRUNCATE TABLE productos;
-- Más rápido que DELETE FROM productos (no genera logs individuales)

-- DROP DATABASE
DROP DATABASE IF EXISTS empresa_db;  -- ¡CUIDADO! Irreversible
```

### Diferencia DROP vs TRUNCATE vs DELETE:

| Comando | Elimina estructura | Elimina datos | Reversible | Velocidad |
|---------|-------------------|---------------|------------|-----------|
| DROP | ✓ | ✓ | ✗ | Instantáneo |
| TRUNCATE | ✗ | ✓ (todos) | ✗ | Muy rápido |
| DELETE | ✗ | ✓ (selectivo) | ✓ (con ROLLBACK) | Lento |

---

## 6. Ejercicios

### Ejercicio 2.1 - Crear tablas
Crea las siguientes tablas con los tipos de datos y constraints apropiados:
- `departamentos` (id, nombre, presupuesto, gerente_id)
- `proyectos` (id, nombre, fecha_inicio, fecha_fin, presupuesto, departamento_id)
- `asignaciones` (empleado_id, proyecto_id, horas_asignadas, rol)

### Ejercicio 2.2 - Modificar estructura
Dado que la tabla `empleados` ya existe:
1. Agrega una columna `fecha_nacimiento` de tipo DATE
2. Agrega una columna `genero` que solo acepte 'M', 'F', 'NB'
3. Cambia el tamaño de `nombre` a 100 caracteres
4. Agrega un CHECK que el salario sea menor a 500000

### Ejercicio 2.3 - Comparación entre motores
Escribe el CREATE TABLE de una tabla `pedidos` en:
- PostgreSQL
- MySQL
- Oracle
Nota las diferencias en tipos de datos y auto-increment.

---

## Errores Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| "relation already exists" | La tabla ya existe | Usar `IF NOT EXISTS` |
| "cannot drop table, referenced by FK" | Otra tabla depende de esta | Eliminar FK primero o usar CASCADE |
| "null value in column violates not-null" | Agregaste NOT NULL a columna con datos NULL | Llenar datos primero o usar DEFAULT |
| "value too long for type varchar(50)" | El dato excede el tamaño | Aumentar tamaño con ALTER |

---

## Siguiente Módulo
→ [03-DML: Manipular Datos](../03-dml/README.md)
