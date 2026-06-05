-- ════════════════════════════════════════════════════════════════
-- MÓDULO 15 - EJERCICIO 1: Optimización y Tuning
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Cada ejercicio tiene una query LENTA. Tu trabajo es:
-- 1. Ejecutar EXPLAIN ANALYZE para ver el plan actual
-- 2. Identificar el cuello de botella
-- 3. Aplicar la optimización (índice, reescritura, etc.)
-- 4. Ejecutar EXPLAIN ANALYZE de nuevo y documentar la mejora
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 15.1: Query lenta por falta de índice
-- ═══════════════════════════════════════════════════════════════
-- Esta query busca ventas de un cliente específico en un rango de fechas:
EXPLAIN ANALYZE
SELECT v.numero_venta, v.fecha, v.total, e.nombre AS vendedor
FROM ventas v
JOIN empleados e ON v.empleado_id = e.id
WHERE v.cliente_id = 500
AND v.fecha BETWEEN '2024-01-01' AND '2024-12-31'
AND v.estado = 'COMPLETADA'
ORDER BY v.fecha DESC;

-- DIAGNÓSTICO: ¿Qué scan usa? ¿Cuántas filas examina?
-- TU OPTIMIZACIÓN:
-- TU EXPLAIN DESPUÉS:
-- MEJORA: de ___ ms a ___ ms


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 15.2: Query lenta por función en WHERE
-- ═══════════════════════════════════════════════════════════════
-- PROBLEMA: Usar una función en WHERE impide usar índices
EXPLAIN ANALYZE
SELECT * FROM ventas
WHERE EXTRACT(YEAR FROM fecha) = 2024
AND EXTRACT(MONTH FROM fecha) = 6;

-- ¿Por qué es lento? Porque EXTRACT() se aplica a CADA fila (no puede usar índice)
-- SOLUCIÓN: Reescribir sin función en la columna
-- TU QUERY OPTIMIZADA:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 15.3: Query lenta por N+1 (subconsulta correlacionada)
-- ═══════════════════════════════════════════════════════════════
-- PROBLEMA: La subconsulta se ejecuta 1 vez POR CADA producto (500 veces)
EXPLAIN ANALYZE
SELECT p.nombre, p.precio,
    (SELECT SUM(dv.cantidad) FROM detalle_venta dv WHERE dv.producto_id = p.id) AS total_vendido
FROM productos p
WHERE p.activo = TRUE;

-- SOLUCIÓN: Reescribir con JOIN + GROUP BY (1 sola ejecución)
-- TU QUERY OPTIMIZADA:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 15.4: Paginación eficiente
-- ═══════════════════════════════════════════════════════════════
-- PROBLEMA: OFFSET alto es lento (debe contar N filas para saltarlas)
EXPLAIN ANALYZE
SELECT * FROM ventas ORDER BY id LIMIT 20 OFFSET 9000;
-- Con 10,000 ventas, OFFSET 9000 debe leer 9000 filas para descartarlas

-- SOLUCIÓN: Keyset pagination (usar el último ID visto)
-- Si la página anterior terminó en id=9000:
-- TU QUERY OPTIMIZADA:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 15.5: Particionamiento de tabla grande
-- ═══════════════════════════════════════════════════════════════
-- La tabla ventas tiene 10,000 registros. Imagina que tiene 10 MILLONES.
-- Particiona por rango de fecha (una partición por año).

-- Paso 1: Crear tabla particionada
-- TU CÓDIGO:


-- Paso 2: Crear particiones (2023, 2024, 2025)
-- TU CÓDIGO:


-- Paso 3: Migrar datos de la tabla original
-- TU CÓDIGO:


-- Paso 4: Verificar partition pruning
-- EXPLAIN ANALYZE SELECT * FROM ventas_part WHERE fecha >= '2024-06-01' AND fecha < '2024-07-01';
-- ¿Solo escanea la partición 2024?


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 15.6: Análisis completo de performance
-- ═══════════════════════════════════════════════════════════════
-- Ejecuta este reporte de "salud" de la BD y responde las preguntas:

-- a) Tablas sin VACUUM reciente (posible bloat):
SELECT relname, n_dead_tup, last_vacuum, last_autovacuum
FROM pg_stat_user_tables
WHERE n_dead_tup > 100
ORDER BY n_dead_tup DESC;

-- b) Índices no utilizados (candidatos a eliminar):
SELECT indexrelname, idx_scan, pg_size_pretty(pg_relation_size(indexrelid))
FROM pg_stat_user_indexes
WHERE idx_scan = 0 AND schemaname = 'public';

-- c) Ratio de cache hit (debe ser > 99%):
SELECT
    sum(heap_blks_hit) / (sum(heap_blks_hit) + sum(heap_blks_read)) * 100 AS cache_hit_ratio
FROM pg_statio_user_tables;

-- PREGUNTAS:
-- 1. ¿Hay tablas con muchos dead tuples? ¿Necesitan VACUUM?
-- 2. ¿Hay índices que nunca se usan? ¿Los eliminarías?
-- 3. ¿El cache hit ratio es > 99%? Si no, ¿qué harías?
