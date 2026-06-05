# Módulo 09: Índices y Performance

## ¿Qué es un Índice?
Un índice es una estructura de datos que acelera las búsquedas en una tabla. Sin índice, la BD debe leer TODA la tabla (Full Table Scan). Con índice, va directo al dato.

**Analogía**: El índice de un libro. Sin él, buscas página por página. Con él, vas directo al tema.

---

## 1. Tipos de Índices

### B-Tree (el más común, default)
```sql
-- Ideal para: =, <, >, <=, >=, BETWEEN, ORDER BY
CREATE INDEX idx_productos_precio ON productos(precio);
CREATE INDEX idx_empleados_nombre ON empleados(nombre);

-- Índice compuesto (múltiples columnas)
CREATE INDEX idx_ventas_fecha_sucursal ON ventas(fecha, sucursal_id);
-- Útil para: WHERE fecha = X AND sucursal_id = Y
-- También para: WHERE fecha = X (usa la primera columna)
-- NO útil para: WHERE sucursal_id = Y (sin la primera columna)
```

### Hash (solo igualdad exacta)
```sql
-- PostgreSQL: solo para operador =
CREATE INDEX idx_productos_sku_hash ON productos USING hash(sku);
-- Más rápido que B-Tree para = pero NO sirve para rangos (<, >)
```

### GIN (Generalized Inverted Index)
```sql
-- Para: arrays, JSONB, full-text search
CREATE INDEX idx_auditoria_datos ON auditoria USING gin(datos_despues);
-- Permite buscar dentro de JSON: WHERE datos_despues @> '{"precio": 999}'

-- Full-text search
CREATE INDEX idx_productos_busqueda ON productos USING gin(to_tsvector('spanish', nombre || ' ' || COALESCE(descripcion, '')));
```

### Partial Index (índice condicional)
```sql
-- Solo indexa productos activos (más pequeño, más rápido)
CREATE INDEX idx_productos_activos ON productos(nombre, precio) WHERE activo = TRUE;
-- La mayoría de queries filtran por activo=TRUE, así que este índice es perfecto
```

### Unique Index
```sql
-- Garantiza unicidad + acelera búsquedas
CREATE UNIQUE INDEX idx_productos_sku ON productos(sku);
-- Equivale a: ALTER TABLE productos ADD CONSTRAINT uq_sku UNIQUE(sku);
```

---

## 2. EXPLAIN - Analizar Queries

```sql
-- Ver el plan de ejecución (cómo la BD resuelve tu query)
EXPLAIN SELECT * FROM productos WHERE precio > 10000;

-- Con tiempos reales (ejecuta la query)
EXPLAIN ANALYZE SELECT * FROM productos WHERE precio > 10000;

-- Resultado ejemplo:
-- Seq Scan on productos  (cost=0.00..12.50 rows=100 width=200) (actual time=0.01..0.15 rows=95 loops=1)
--   Filter: (precio > 10000)
--   Rows Removed by Filter: 405
-- Planning Time: 0.05 ms
-- Execution Time: 0.20 ms
```

### Interpretar EXPLAIN:

| Operación | Significado | ¿Bueno o malo? |
|-----------|-------------|----------------|
| Seq Scan | Lee TODA la tabla | ❌ Malo en tablas grandes |
| Index Scan | Usa un índice | ✅ Bueno |
| Index Only Scan | Todo desde el índice (no toca tabla) | ✅✅ Excelente |
| Bitmap Index Scan | Combina múltiples índices | ✅ Bueno |
| Hash Join | Une tablas con hash | ✅ Bueno para tablas grandes |
| Nested Loop | Une tablas iterando | ✅ Bueno para tablas pequeñas |
| Sort | Ordena resultados | ⚠️ Costoso si no hay índice |

---

## 3. Cuándo Crear Índices

### SÍ crear índice:
- Columnas en WHERE frecuentes
- Columnas en JOIN (FK)
- Columnas en ORDER BY
- Columnas con alta cardinalidad (muchos valores únicos)

### NO crear índice:
- Tablas pequeñas (< 1000 filas)
- Columnas con pocos valores únicos (ej: boolean, estado)
- Tablas con muchos INSERT/UPDATE (los índices se actualizan en cada escritura)

---

## 4. Ejercicios de Performance

```sql
-- Ejercicio: Ejecuta estas queries CON y SIN índice, compara tiempos

-- 1. Sin índice
DROP INDEX IF EXISTS idx_ventas_fecha;
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha BETWEEN '2024-01-01' AND '2024-01-31';

-- 2. Con índice
CREATE INDEX idx_ventas_fecha ON ventas(fecha);
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha BETWEEN '2024-01-01' AND '2024-01-31';

-- 3. Compara: ¿cuánto más rápido es con índice?
```

### Ejercicios:
1. ¿Qué índices crearías para optimizar: `SELECT * FROM ventas WHERE cliente_id = 500 AND fecha > '2024-01-01'`?
2. Analiza con EXPLAIN la query más lenta de tu aplicación
3. ¿Por qué un índice en `activo` (boolean) NO es útil?
4. Crea un partial index para ventas completadas del último año
5. ¿Cuál es el trade-off de tener muchos índices?

---

## Siguiente Módulo
→ [10-Triggers](../10-triggers/README.md)
