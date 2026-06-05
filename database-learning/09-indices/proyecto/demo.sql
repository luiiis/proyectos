-- ════════════════════════════════════════════════════════════════
-- MÓDULO 09: Índices - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 09: Índices y Performance ═══'

\echo ''
\echo '── ANTES: Query sin índice optimizado ──'
EXPLAIN ANALYZE
SELECT * FROM ventas
WHERE fecha BETWEEN '2024-06-01' AND '2024-06-30'
AND estado = 'COMPLETADA';

\echo ''
\echo '── Creando índice compuesto ──'
CREATE INDEX IF NOT EXISTS idx_demo_ventas_fecha_estado
ON ventas(fecha, estado);

\echo ''
\echo '── DESPUÉS: Misma query con índice ──'
EXPLAIN ANALYZE
SELECT * FROM ventas
WHERE fecha BETWEEN '2024-06-01' AND '2024-06-30'
AND estado = 'COMPLETADA';

\echo ''
\echo '── Índices existentes en la tabla ventas ──'
SELECT indexname, indexdef FROM pg_indexes WHERE tablename = 'ventas';

\echo ''
\echo '── Tamaño de índices ──'
SELECT indexname, pg_size_pretty(pg_relation_size(indexname::text)) AS tamaño
FROM pg_indexes WHERE tablename = 'ventas';
