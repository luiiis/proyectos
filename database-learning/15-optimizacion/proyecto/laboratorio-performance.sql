-- ════════════════════════════════════════════════════════════════
-- MÓDULO 15 - PROYECTO: Laboratorio de Performance
-- ════════════════════════════════════════════════════════════════
-- Cada sección tiene: QUERY LENTA → DIAGNÓSTICO → SOLUCIÓN → VERIFICACIÓN
-- ════════════════════════════════════════════════════════════════

\echo '══════════════════════════════════════════════'
\echo '  LABORATORIO DE PERFORMANCE'
\echo '══════════════════════════════════════════════'

-- ═══════════════════════════════════════════════════════════════
-- CASO 1: Búsqueda por rango de fechas sin índice
-- ═══════════════════════════════════════════════════════════════
\echo ''
\echo '── CASO 1: Ventas por rango de fecha ──'
\echo '   ANTES (sin índice optimizado):'

EXPLAIN ANALYZE
SELECT v.numero_venta, v.fecha, v.total, 
       e.nombre || ' ' || e.apellido AS vendedor
FROM ventas v
JOIN empleados e ON v.empleado_id = e.id
WHERE v.fecha BETWEEN '2024-03-01' AND '2024-03-31'
AND v.estado = 'COMPLETADA'
ORDER BY v.total DESC;

-- DIAGNÓSTICO: Probablemente Seq Scan en ventas (10,000 filas)
-- SOLUCIÓN: Índice compuesto en (fecha, estado) con INCLUDE para covering

\echo '   Creando índice optimizado...'
CREATE INDEX IF NOT EXISTS idx_ventas_fecha_estado_opt
ON ventas(fecha, estado) INCLUDE (total, empleado_id, numero_venta);

\echo '   DESPUÉS (con índice):'
EXPLAIN ANALYZE
SELECT v.numero_venta, v.fecha, v.total,
       e.nombre || ' ' || e.apellido AS vendedor
FROM ventas v
JOIN empleados e ON v.empleado_id = e.id
WHERE v.fecha BETWEEN '2024-03-01' AND '2024-03-31'
AND v.estado = 'COMPLETADA'
ORDER BY v.total DESC;

-- ═══════════════════════════════════════════════════════════════
-- CASO 2: Función en WHERE que impide uso de índice
-- ═══════════════════════════════════════════════════════════════
\echo ''
\echo '── CASO 2: Función en WHERE (anti-patrón) ──'
\echo '   ANTES (EXTRACT impide usar índice):'

EXPLAIN ANALYZE
SELECT COUNT(*), SUM(total)
FROM ventas
WHERE EXTRACT(YEAR FROM fecha) = 2024
AND EXTRACT(MONTH FROM fecha) = 6;

-- DIAGNÓSTICO: Seq Scan porque EXTRACT() se aplica a CADA fila
-- SOLUCIÓN: Reescribir con rango de fechas (puede usar índice)

\echo '   DESPUÉS (reescrito con rango):'
EXPLAIN ANALYZE
SELECT COUNT(*), SUM(total)
FROM ventas
WHERE fecha >= '2024-06-01' AND fecha < '2024-07-01';

-- ═══════════════════════════════════════════════════════════════
-- CASO 3: Subconsulta correlacionada (N+1)
-- ═══════════════════════════════════════════════════════════════
\echo ''
\echo '── CASO 3: Subconsulta correlacionada vs JOIN ──'
\echo '   ANTES (subconsulta se ejecuta 500 veces):'

EXPLAIN ANALYZE
SELECT p.nombre, p.precio,
    (SELECT COALESCE(SUM(dv.cantidad), 0) 
     FROM detalle_venta dv WHERE dv.producto_id = p.id) AS vendidos
FROM productos p
WHERE p.activo = TRUE
ORDER BY vendidos DESC
LIMIT 20;

-- DIAGNÓSTICO: SubPlan se ejecuta 1 vez por cada producto (500 ejecuciones)
-- SOLUCIÓN: Reescribir con LEFT JOIN + GROUP BY

\echo '   DESPUÉS (con JOIN, 1 sola ejecución):'
EXPLAIN ANALYZE
SELECT p.nombre, p.precio, COALESCE(SUM(dv.cantidad), 0) AS vendidos
FROM productos p
LEFT JOIN detalle_venta dv ON dv.producto_id = p.id
WHERE p.activo = TRUE
GROUP BY p.id, p.nombre, p.precio
ORDER BY vendidos DESC
LIMIT 20;

-- ═══════════════════════════════════════════════════════════════
-- CASO 4: Paginación con OFFSET alto
-- ═══════════════════════════════════════════════════════════════
\echo ''
\echo '── CASO 4: OFFSET alto vs Keyset Pagination ──'
\echo '   ANTES (OFFSET 8000 = leer y descartar 8000 filas):'

EXPLAIN ANALYZE
SELECT id, numero_venta, fecha, total
FROM ventas
ORDER BY id
LIMIT 20 OFFSET 8000;

-- DIAGNÓSTICO: Debe leer 8020 filas para devolver 20
-- SOLUCIÓN: Keyset pagination (WHERE id > último_id_visto)

\echo '   DESPUÉS (keyset, va directo al punto):'
EXPLAIN ANALYZE
SELECT id, numero_venta, fecha, total
FROM ventas
WHERE id > 8000
ORDER BY id
LIMIT 20;

-- ═══════════════════════════════════════════════════════════════
-- CASO 5: JOIN sin índice en FK
-- ═══════════════════════════════════════════════════════════════
\echo ''
\echo '── CASO 5: Reporte complejo multi-tabla ──'

EXPLAIN ANALYZE
SELECT
    s.nombre AS sucursal,
    c.nombre AS categoria,
    COUNT(DISTINCT v.id) AS num_ventas,
    SUM(dv.subtotal) AS total_vendido,
    AVG(dv.precio_unitario) AS precio_promedio
FROM ventas v
JOIN detalle_venta dv ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
JOIN categorias c ON p.categoria_id = c.id
JOIN sucursales s ON v.sucursal_id = s.id
WHERE v.fecha >= '2024-01-01'
AND v.estado = 'COMPLETADA'
GROUP BY s.nombre, c.nombre
ORDER BY total_vendido DESC;

-- VERIFICAR: ¿Todos los JOINs usan Index Scan?
-- Si alguno usa Seq Scan → crear índice en la FK correspondiente

\echo ''
\echo '══════════════════════════════════════════════'
\echo '  LABORATORIO COMPLETADO'
\echo '  Compara los tiempos ANTES vs DESPUÉS'
\echo '══════════════════════════════════════════════'
