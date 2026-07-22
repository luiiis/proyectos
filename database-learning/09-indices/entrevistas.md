# Preguntas de Entrevista - Módulo 09: Índices

## Nivel Junior

### 1. ¿Qué es un índice en base de datos?
**Respuesta:**
Estructura de datos (B-Tree por defecto) que permite encontrar filas SIN recorrer toda la tabla. Como el índice de un libro: en vez de leer 500 páginas buscando "transacciones", vas directo a la página correcta.

---

### 2. ¿Por qué no poner índice en TODAS las columnas?
**Respuesta:**
- Cada índice ocupa espacio en disco
- Cada INSERT/UPDATE/DELETE debe actualizar TODOS los índices de esa tabla
- Con 10 índices, un INSERT es 10x más costoso
- Solo crear índices en columnas que se usan en WHERE, JOIN, ORDER BY frecuentemente

---

### 3. ¿Las Primary Keys y Foreign Keys ya tienen índice?
**Respuesta:**
- PK: SÍ, automáticamente (siempre tiene índice)
- FK: DEPENDE. PostgreSQL NO crea índice automático en FK. MySQL SÍ.
- En PostgreSQL debes crear el índice manualmente en las FK:
```sql
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
```

---

### 4. ¿Cómo sabes si una query usa un índice?
**Respuesta:**
Con `EXPLAIN`:
```sql
EXPLAIN SELECT * FROM productos WHERE precio > 10000;
-- "Index Scan using idx_productos_precio" → SÍ usa índice ✓
-- "Seq Scan on productos" → NO usa índice ✗ (recorre toda la tabla)
```

---

### 5. ¿Por qué un índice puede NO usarse aunque exista?
**Respuesta:**
- Tabla muy pequeña (seq scan es más rápido)
- Función aplicada a la columna: `WHERE LOWER(nombre) = 'x'` → el índice de `nombre` no sirve
- Estadísticas desactualizadas: el planner cree que hay pocas filas
- LIKE con `%` al inicio: `WHERE nombre LIKE '%laptop'` → no puede usar índice B-Tree
- El query devuelve >20% de la tabla → seq scan es más eficiente

---

## Nivel Mid

### 6. ¿Qué es un Covering Index?
**Respuesta:**
Índice que INCLUYE todas las columnas que la query necesita → la BD no necesita ir a la tabla (Index-Only Scan):
```sql
-- Query frecuente:
SELECT nombre, precio FROM productos WHERE categoria_id = 5;

-- Covering index:
CREATE INDEX idx_productos_cat_covering ON productos(categoria_id) INCLUDE (nombre, precio);
-- Ahora la query se resuelve SOLO con el índice, sin tocar la tabla
```

---

### 7. ¿Cuál es la diferencia entre B-Tree y GIN?
**Respuesta:**
- **B-Tree**: para operadores de comparación (=, <, >, BETWEEN, LIKE 'abc%'). El default.
- **GIN**: para búsqueda "dentro de" estructuras complejas (arrays, JSONB, full-text search).

```sql
-- B-Tree: buscar por precio
CREATE INDEX idx_precio ON productos(precio);
SELECT * FROM productos WHERE precio > 5000;

-- GIN: buscar dentro de JSONB
CREATE INDEX idx_metadata ON productos USING GIN (metadata);
SELECT * FROM productos WHERE metadata @> '{"color": "rojo"}';
```

---

### 8. ¿Cómo crearías un índice para una búsqueda ILIKE?
**Respuesta:**
B-Tree no soporta ILIKE. Opciones:
```sql
-- Opción 1: Índice funcional
CREATE INDEX idx_nombre_lower ON productos(LOWER(nombre));
-- Usar con: WHERE LOWER(nombre) LIKE 'laptop%'

-- Opción 2: Extensión pg_trgm (trigramas)
CREATE EXTENSION pg_trgm;
CREATE INDEX idx_nombre_trgm ON productos USING GIN (nombre gin_trgm_ops);
-- Soporta: WHERE nombre ILIKE '%laptop%' (incluso con % al inicio)
```

---

### 9. ¿Qué es el bloat de índices y cómo se soluciona?
**Respuesta:**
Con muchos UPDATE/DELETE, los índices acumulan entradas muertas (bloat). El índice crece pero mucho espacio está desperdiciado.

Solución:
```sql
-- Reindexar (bloquea la tabla):
REINDEX INDEX idx_productos_precio;

-- Reindexar sin bloqueo (PostgreSQL 12+):
REINDEX INDEX CONCURRENTLY idx_productos_precio;

-- Ver bloat:
SELECT schemaname, tablename, indexname, pg_size_pretty(pg_relation_size(indexrelid))
FROM pg_stat_user_indexes ORDER BY pg_relation_size(indexrelid) DESC;
```

---

### 10. ¿Cómo decides qué índices crear en una tabla nueva?
**Respuesta:**
1. PK ya tiene índice (automático)
2. Todas las FK: crear índice (PostgreSQL no lo hace solo)
3. Columnas de filtro frecuente (WHERE): agregar después de ver queries reales
4. Columnas de ORDER BY frecuente
5. Columnas de JOIN que no son FK

Regla: NO adivinar. Monitorear queries reales → `pg_stat_statements` → indexar lo que se usa.
