# Preguntas de Entrevista - Módulo 15: Optimización y Performance

## Nivel Junior

### 1. ¿Qué es un índice y para qué sirve?
**Respuesta:**
Estructura de datos (generalmente B-Tree) que acelera búsquedas:
- Sin índice: recorrer TODA la tabla (Sequential Scan) → O(n)
- Con índice: ir directo al dato → O(log n)

Analogía: índice del libro. Sin él, buscas "transacciones" leyendo todas las páginas. Con él, vas directo a la página 234.

---

### 2. ¿Qué es EXPLAIN ANALYZE?
**Respuesta:**
Muestra CÓMO la BD ejecuta tu query + cuánto tarda cada paso:
```sql
EXPLAIN ANALYZE SELECT * FROM productos WHERE precio > 10000;
-- Seq Scan on productos (cost=0.00..18.50 rows=3 width=64) (actual time=0.02..0.05 rows=3 loops=1)
-- Planning Time: 0.1 ms
-- Execution Time: 0.08 ms
```
Si ves "Seq Scan" en tabla grande → necesitas índice.

---

### 3. ¿Cuándo NO deberías crear un índice?
**Respuesta:**
- Tablas pequeñas (< 1000 filas): el overhead del índice > beneficio
- Columnas con baja cardinalidad (ej: boolean con solo true/false)
- Tablas con muchos INSERTs (el índice se actualiza en cada INSERT)
- Columnas que nunca se usan en WHERE/JOIN/ORDER BY

---

### 4. ¿Por qué una query puede ser lenta?
**Respuesta (checklist):**
1. No hay índice en columna del WHERE → Sequential Scan
2. SELECT * en vez de solo columnas necesarias → más datos transferidos
3. No hay LIMIT → devuelve millones de filas
4. JOINs sin índice en la FK → Nested Loop costoso
5. Funciones en WHERE → índice no se usa: `WHERE LOWER(nombre) = 'x'`
6. Estadísticas desactualizadas → el planner elige mal

---

### 5. ¿Qué es la paginación y por qué es importante?
**Respuesta:**
Devolver datos en "páginas" en vez de todo junto:
```sql
SELECT * FROM productos ORDER BY id LIMIT 20 OFFSET 40;  -- Página 3 (20 por página)
```
Sin paginación: `SELECT * FROM ventas` con 10 millones de filas = timeout, memoria agotada, red saturada.

---

## Nivel Mid

### 6. ¿Cuáles son los tipos de índices?
**Respuesta:**
- **B-Tree** (default): para =, <, >, BETWEEN, ORDER BY
- **Hash**: solo para =. Más rápido que B-Tree para igualdad exacta.
- **GIN**: para arrays, JSONB, full-text search
- **GiST**: para datos geoespaciales, rangos
- **BRIN**: para datos correlacionados con su posición física (fechas secuenciales)

---

### 7. ¿Qué es un índice parcial?
**Respuesta:**
Índice que solo cubre PARTE de la tabla (con condición WHERE):
```sql
CREATE INDEX idx_productos_activos ON productos(nombre) WHERE activo = true;
```
Si el 90% de queries filtran `WHERE activo = true`, este índice es más pequeño y más rápido que uno sin condición.

---

### 8. ¿Qué es un índice compuesto y cuándo usarlo?
**Respuesta:**
Índice sobre múltiples columnas:
```sql
CREATE INDEX idx_ventas_fecha_sucursal ON ventas(fecha, sucursal_id);
```
Útil cuando siempre filtras por ambas columnas. El ORDEN importa: funciona para queries que filtran por `fecha` o `fecha + sucursal_id`, pero NO solo por `sucursal_id`.

---

### 9. ¿Cómo identificas queries lentas en producción?
**Respuesta:**
```sql
-- PostgreSQL: activar log de queries lentas
-- postgresql.conf:
log_min_duration_statement = 200  -- Log queries > 200ms

-- Ver queries activas ahora:
SELECT pid, now() - pg_stat_activity.query_start AS duration, query
FROM pg_stat_activity
WHERE state != 'idle'
ORDER BY duration DESC;

-- Estadísticas de queries (extensión pg_stat_statements):
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY total_exec_time DESC LIMIT 10;
```

---

### 10. ¿Qué es una tabla particionada y cuándo usarla?
**Respuesta:**
Dividir una tabla grande en "sub-tablas" por algún criterio:
```sql
CREATE TABLE ventas (
    id BIGSERIAL,
    fecha DATE,
    total NUMERIC
) PARTITION BY RANGE (fecha);

CREATE TABLE ventas_2025 PARTITION OF ventas FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
CREATE TABLE ventas_2026 PARTITION OF ventas FOR VALUES FROM ('2026-01-01') TO ('2027-01-01');
```
Beneficio: `SELECT * FROM ventas WHERE fecha > '2026-06-01'` solo escanea `ventas_2026` (partition pruning). Útil con tablas de millones+ de filas.

---

## Nivel Senior

### 11. Tu base de datos tiene 100 millones de registros y las queries tardan. ¿Qué haces?
**Respuesta (plan de ataque):**
1. Identificar top 10 queries más costosas (pg_stat_statements)
2. EXPLAIN ANALYZE cada una → buscar Seq Scans, Nested Loops
3. Crear índices estratégicos (compuestos, parciales, covering)
4. Particionar tablas grandes por fecha/rango
5. Archivar datos históricos (mover a tabla fría)
6. Materializar consultas frecuentes (Materialized Views)
7. Configurar connection pool (no 1000 conexiones directas)
8. Vertical scaling: más RAM (shared_buffers = 25% RAM)
9. Read replicas para queries de lectura
10. Considerar caché (Redis) para datos que no cambian frecuentemente

---

### 12. ¿Qué es un vacuum en PostgreSQL y por qué es crucial?
**Respuesta:**
PostgreSQL usa MVCC: cuando haces UPDATE/DELETE, la fila vieja NO se borra físicamente (otros transactions pueden necesitarla). VACUUM limpia esas filas muertas.

Sin VACUUM: la tabla crece indefinidamente (table bloat), índices se degradan, queries se vuelven lentas.

`AUTOVACUUM` está activado por defecto. En tablas con muchos UPDATE/DELETE, puede necesitar ajuste:
```sql
ALTER TABLE ventas SET (autovacuum_vacuum_scale_factor = 0.05);  -- más agresivo
```
