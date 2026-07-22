-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 09: Índices y Optimización
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. Ver índices existentes ═══
SELECT tablename, indexname, indexdef
FROM pg_indexes WHERE schemaname = 'public'
ORDER BY tablename, indexname;

-- ═══ 2. Análisis SIN índice (antes) ═══

-- Query lenta sin índice:
EXPLAIN ANALYZE
SELECT * FROM ventas WHERE fecha > '2026-01-01' AND estado = 'COMPLETADA';
-- Probablemente: Seq Scan (recorre toda la tabla)

-- ═══ 3. Crear índices estratégicos ═══

-- Índice simple: búsquedas por fecha
CREATE INDEX idx_ventas_fecha ON ventas(fecha);

-- Índice compuesto: búsquedas por fecha + estado (las queries más comunes)
CREATE INDEX idx_ventas_fecha_estado ON ventas(fecha, estado);

-- Índice parcial: solo ventas completadas (ignora canceladas)
CREATE INDEX idx_ventas_completadas ON ventas(fecha, total)
WHERE estado = 'COMPLETADA';

-- Índice para FK (PostgreSQL no los crea automáticamente)
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_ventas_empleado ON ventas(empleado_id);
CREATE INDEX idx_ventas_sucursal ON ventas(sucursal_id);
CREATE INDEX idx_detalle_venta ON detalle_venta(venta_id);
CREATE INDEX idx_detalle_producto ON detalle_venta(producto_id);
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_proveedor ON productos(proveedor_id);
CREATE INDEX idx_empleados_sucursal ON empleados(sucursal_id);
CREATE INDEX idx_inventario_producto ON inventario(producto_id);
CREATE INDEX idx_inventario_sucursal ON inventario(sucursal_id);

-- Índice para búsqueda de texto (ILIKE)
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE INDEX idx_productos_nombre_trgm ON productos USING GIN (nombre gin_trgm_ops);
-- Ahora: SELECT * FROM productos WHERE nombre ILIKE '%laptop%' usa el índice

-- Índice funcional: búsqueda case-insensitive
CREATE INDEX idx_clientes_email_lower ON clientes(LOWER(email));

-- Covering index: query que se resuelve SOLO con el índice
CREATE INDEX idx_productos_cat_covering ON productos(categoria_id) INCLUDE (nombre, precio);

-- ═══ 4. Análisis CON índice (después) ═══

EXPLAIN ANALYZE
SELECT * FROM ventas WHERE fecha > '2026-01-01' AND estado = 'COMPLETADA';
-- Ahora debería usar: Index Scan using idx_ventas_completadas

EXPLAIN ANALYZE
SELECT nombre, precio FROM productos WHERE categoria_id = 1;
-- Debería usar: Index Only Scan (covering index, no toca la tabla)

-- ═══ 5. Comparar tiempos ═══

-- Forzar Seq Scan (para comparar):
SET enable_indexscan = OFF;
SET enable_bitmapscan = OFF;
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha > '2026-06-01';
-- Resultado: Seq Scan ... actual time=X

-- Rehabilitar índices:
SET enable_indexscan = ON;
SET enable_bitmapscan = ON;
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha > '2026-06-01';
-- Resultado: Index Scan ... actual time=Y (debería ser mucho menor que X)

-- ═══ 6. Ver tamaño de índices ═══

SELECT
    indexrelname AS indice,
    pg_size_pretty(pg_relation_size(indexrelid)) AS tamano,
    idx_scan AS veces_usado,
    idx_tup_read AS tuplas_leidas
FROM pg_stat_user_indexes
ORDER BY pg_relation_size(indexrelid) DESC;

-- ═══ 7. Identificar índices NO usados (candidatos a eliminar) ═══

SELECT indexrelname AS indice,
       relname AS tabla,
       idx_scan AS veces_usado
FROM pg_stat_user_indexes
WHERE idx_scan = 0
AND indexrelname NOT LIKE '%_pkey'  -- No eliminar PKs
ORDER BY pg_relation_size(indexrelid) DESC;

-- ═══ 8. Verificar que el planner elige bien ═══

-- Ver estadísticas de tabla (el planner las usa para decidir):
ANALYZE ventas;  -- Actualizar estadísticas
ANALYZE productos;

-- Ver plan detallado:
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT p.nombre, SUM(dv.cantidad) AS vendidos
FROM productos p
JOIN detalle_venta dv ON dv.producto_id = p.id
JOIN ventas v ON dv.venta_id = v.id
WHERE v.fecha >= '2026-01-01'
GROUP BY p.nombre
ORDER BY vendidos DESC LIMIT 10;

-- ═══ 9. Reindexar (mantenimiento) ═══

-- Reindexar un índice (bloquea brevemente):
REINDEX INDEX idx_ventas_fecha;

-- Reindexar sin bloqueo (PostgreSQL 12+):
REINDEX INDEX CONCURRENTLY idx_ventas_fecha;

-- Reindexar toda la tabla:
REINDEX TABLE ventas;

-- ═══ 10. Eliminar índices innecesarios ═══

-- Siempre verificar que no se usa antes de eliminar:
DROP INDEX IF EXISTS idx_ejemplo_no_usado;

-- NOTA: eliminar índice mejora INSERT/UPDATE/DELETE a costa de SELECT más lento
-- Solo eliminar si el índice realmente no se usa (idx_scan = 0 por semanas)
