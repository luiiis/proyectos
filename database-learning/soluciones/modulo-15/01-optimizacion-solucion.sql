-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 15: Optimización y Performance
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. EXPLAIN: entender planes de ejecución ═══

-- Query simple con EXPLAIN:
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT * FROM ventas WHERE fecha > '2026-06-01' AND estado = 'COMPLETADA';

-- Qué buscar en el output:
-- ✗ "Seq Scan" en tablas grandes → necesita índice
-- ✓ "Index Scan" o "Index Only Scan" → usa índice correctamente
-- ✗ "Nested Loop" con muchas filas → considerar Hash Join
-- ✗ "Sort" con mucha memoria → verificar work_mem
-- ✓ "Bitmap Index Scan" → eficiente para rangos

-- ═══ 2. OPTIMIZAR: Query con múltiples problemas ═══

-- ANTES (lento):
EXPLAIN ANALYZE
SELECT *
FROM ventas v, clientes c, empleados e, sucursales s, detalle_venta dv, productos p
WHERE v.cliente_id = c.id
AND v.empleado_id = e.id
AND v.sucursal_id = s.id
AND dv.venta_id = v.id
AND dv.producto_id = p.id
AND EXTRACT(YEAR FROM v.fecha) = 2026;
-- Problemas: SELECT *, old-style joins, función en WHERE

-- DESPUÉS (optimizado):
EXPLAIN ANALYZE
SELECT v.numero_venta, v.fecha, v.total,
       c.nombre || ' ' || c.apellido AS cliente,
       e.nombre || ' ' || e.apellido AS vendedor,
       s.nombre AS sucursal,
       p.nombre AS producto, dv.cantidad, dv.subtotal
FROM ventas v
JOIN clientes c ON v.cliente_id = c.id
JOIN empleados e ON v.empleado_id = e.id
JOIN sucursales s ON v.sucursal_id = s.id
JOIN detalle_venta dv ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
WHERE v.fecha >= '2026-01-01' AND v.fecha < '2027-01-01'  -- Usa índice en fecha
AND v.estado = 'COMPLETADA';
-- Mejoras: columnas específicas, JOIN explícito, rango en vez de EXTRACT

-- ═══ 3. ÍNDICES ESTRATÉGICOS para queries frecuentes ═══

-- Para búsquedas de ventas por fecha + estado (la query más común):
CREATE INDEX CONCURRENTLY idx_ventas_fecha_estado
ON ventas(fecha, estado) WHERE estado = 'COMPLETADA';

-- Para el JOIN ventas → detalle (se hace en cada query de ventas):
CREATE INDEX CONCURRENTLY idx_detalle_venta_id ON detalle_venta(venta_id);

-- Para búsqueda de productos por nombre (ILIKE):
CREATE INDEX CONCURRENTLY idx_productos_nombre_gin
ON productos USING GIN (nombre gin_trgm_ops);

-- Para reportes por sucursal + mes:
CREATE INDEX CONCURRENTLY idx_ventas_sucursal_fecha
ON ventas(sucursal_id, fecha) WHERE estado = 'COMPLETADA';

-- ═══ 4. PARTICIONAMIENTO: tabla de ventas por año ═══

-- Crear tabla particionada:
CREATE TABLE ventas_particionada (
    id BIGSERIAL,
    numero_venta VARCHAR(30),
    fecha TIMESTAMP NOT NULL,
    cliente_id INT,
    empleado_id INT,
    sucursal_id INT,
    total NUMERIC(12,2),
    estado VARCHAR(20),
    PRIMARY KEY (id, fecha)  -- Fecha debe ser parte de la PK en particionamiento
) PARTITION BY RANGE (fecha);

-- Crear particiones por año:
CREATE TABLE ventas_2024 PARTITION OF ventas_particionada
    FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
CREATE TABLE ventas_2025 PARTITION OF ventas_particionada
    FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
CREATE TABLE ventas_2026 PARTITION OF ventas_particionada
    FOR VALUES FROM ('2026-01-01') TO ('2027-01-01');

-- Query sobre partition: PostgreSQL solo escanea la partición relevante
EXPLAIN ANALYZE SELECT * FROM ventas_particionada WHERE fecha >= '2026-06-01';
-- Debería mostrar: "Pruned partitions: ventas_2024, ventas_2025"

-- ═══ 5. MATERIALIZED VIEWS para reportes pesados ═══

-- Reporte que tarda 10 segundos:
CREATE MATERIALIZED VIEW mv_dashboard_kpis AS
SELECT
    DATE_TRUNC('day', v.fecha)::DATE AS dia,
    s.nombre AS sucursal,
    COUNT(v.id) AS ventas,
    SUM(v.total) AS facturacion,
    COUNT(DISTINCT v.cliente_id) AS clientes,
    ROUND(AVG(v.total), 2) AS ticket_promedio
FROM ventas v
JOIN sucursales s ON v.sucursal_id = s.id
WHERE v.estado = 'COMPLETADA'
GROUP BY DATE_TRUNC('day', v.fecha), s.nombre;

CREATE UNIQUE INDEX idx_mv_dashboard ON mv_dashboard_kpis(dia, sucursal);

-- Ahora la query es instantánea:
SELECT * FROM mv_dashboard_kpis WHERE dia >= '2026-07-01';

-- Refrescar sin bloqueo (CONCURRENTLY requiere UNIQUE INDEX):
REFRESH MATERIALIZED VIEW CONCURRENTLY mv_dashboard_kpis;

-- ═══ 6. CONNECTION POOLING (configuración) ═══

-- PostgreSQL por defecto: max_connections = 100
-- Cada conexión usa ~10MB de RAM
-- 100 conexiones = 1GB solo en conexiones

-- Solución: PgBouncer (pool externo)
-- pgbouncer.ini:
-- [databases]
-- empresa_db = host=localhost port=5432 dbname=empresa_db
-- [pgbouncer]
-- pool_mode = transaction      ; conexión se devuelve al pool después de cada transacción
-- max_client_conn = 1000       ; 1000 clientes pueden conectarse
-- default_pool_size = 25       ; pero solo 25 conexiones reales a PostgreSQL

-- ═══ 7. OPTIMIZACIÓN DE CONFIGURACIÓN (postgresql.conf) ═══

-- Para servidor con 16GB RAM:
-- shared_buffers = 4GB                    ; 25% de RAM
-- effective_cache_size = 12GB             ; 75% de RAM (hint para planner)
-- work_mem = 256MB                        ; RAM por operación sort/hash
-- maintenance_work_mem = 1GB              ; RAM para VACUUM, CREATE INDEX
-- max_wal_size = 2GB                      ; WAL antes de checkpoint
-- checkpoint_completion_target = 0.9      ; Suavizar I/O de checkpoints
-- random_page_cost = 1.1                  ; SSD (default 4.0 para HDD)
-- effective_io_concurrency = 200          ; SSD (default 1 para HDD)

-- Verificar configuración actual:
SHOW shared_buffers;
SHOW work_mem;
SHOW effective_cache_size;

-- ═══ 8. QUERY LENTO: diagnóstico paso a paso ═══

-- Paso 1: Identificar queries lentas
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY total_exec_time DESC LIMIT 5;

-- Paso 2: EXPLAIN ANALYZE de la query lenta
EXPLAIN (ANALYZE, BUFFERS, COSTS)
SELECT ...;  -- tu query lenta aquí

-- Paso 3: Buscar problemas
-- ¿Seq Scan en tabla > 10K filas? → CREATE INDEX
-- ¿Nested Loop con muchas iteraciones? → Verificar índice en FK
-- ¿Sort con External Merge? → Aumentar work_mem
-- ¿Buffers: read = muchos? → Datos no están en caché, más shared_buffers

-- Paso 4: Aplicar fix y verificar
-- Crear índice → EXPLAIN ANALYZE de nuevo → comparar tiempos

-- ═══ 9. BATCH OPERATIONS optimizadas ═══

-- INSERT masivo: usar COPY (10x más rápido que INSERT individual)
-- COPY productos (nombre, precio, sku) FROM '/tmp/productos.csv' CSV HEADER;

-- UPDATE masivo: desactivar triggers temporalmente
ALTER TABLE productos DISABLE TRIGGER ALL;
UPDATE productos SET precio = precio * 1.1 WHERE categoria_id = 1;
ALTER TABLE productos ENABLE TRIGGER ALL;

-- DELETE masivo: usar batch con LIMIT
-- En vez de DELETE FROM logs WHERE fecha < '2025-01-01' (puede bloquear)
-- Hacer en batches:
DO $$
DECLARE
    filas_eliminadas INT;
BEGIN
    LOOP
        DELETE FROM auditoria WHERE fecha < '2025-01-01' AND id IN (
            SELECT id FROM auditoria WHERE fecha < '2025-01-01' LIMIT 10000
        );
        GET DIAGNOSTICS filas_eliminadas = ROW_COUNT;
        EXIT WHEN filas_eliminadas = 0;
        PERFORM pg_sleep(0.1);  -- Dar respiro al sistema
    END LOOP;
END $$;

-- ═══ 10. BENCHMARK: medir performance real ═══

-- Medir tiempo de una query:
\timing on
SELECT COUNT(*) FROM ventas WHERE fecha > '2026-01-01';
\timing off

-- Ejecutar N veces y promediar (con pgbench o script):
DO $$
DECLARE
    v_start TIMESTAMP;
    v_end TIMESTAMP;
    i INT;
BEGIN
    v_start := clock_timestamp();
    FOR i IN 1..100 LOOP
        PERFORM COUNT(*) FROM ventas WHERE fecha > '2026-06-01' AND estado = 'COMPLETADA';
    END LOOP;
    v_end := clock_timestamp();
    RAISE NOTICE 'Tiempo total: % | Promedio: %',
        v_end - v_start,
        (v_end - v_start) / 100;
END $$;
