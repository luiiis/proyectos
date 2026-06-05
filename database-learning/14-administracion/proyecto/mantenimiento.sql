-- ════════════════════════════════════════════════════════════════
-- MÓDULO 14 - PROYECTO: Script de Mantenimiento Semanal
-- ════════════════════════════════════════════════════════════════
-- Ejecutar semanalmente para mantener la BD saludable.
-- ════════════════════════════════════════════════════════════════

\echo '══════════════════════════════════════════════'
\echo '  MANTENIMIENTO SEMANAL - PostgreSQL'
\echo '══════════════════════════════════════════════'

-- ═══════ 1. VACUUM ANALYZE en tablas con muchos dead tuples ═══════
\echo ''
\echo '── 1. VACUUM ANALYZE en tablas que lo necesitan ──'

-- Tablas con más de 1000 dead tuples
DO $$
DECLARE
    r RECORD;
BEGIN
    FOR r IN
        SELECT relname FROM pg_stat_user_tables
        WHERE n_dead_tup > 1000
        ORDER BY n_dead_tup DESC
    LOOP
        RAISE NOTICE 'VACUUM ANALYZE %...', r.relname;
        EXECUTE 'VACUUM ANALYZE ' || r.relname;
    END LOOP;
END $$;

-- ═══════ 2. REINDEX en índices fragmentados ═══════
\echo ''
\echo '── 2. REINDEX en tablas principales ──'

REINDEX TABLE ventas;
REINDEX TABLE detalle_venta;
REINDEX TABLE productos;
REINDEX TABLE inventario;

\echo '   ✓ Índices reconstruidos'

-- ═══════ 3. Actualizar estadísticas globales ═══════
\echo ''
\echo '── 3. Actualizar estadísticas del query planner ──'
ANALYZE;
\echo '   ✓ Estadísticas actualizadas'

-- ═══════ 4. Limpiar datos temporales/expirados ═══════
\echo ''
\echo '── 4. Limpieza de datos expirados ──'

-- Eliminar auditoría de más de 90 días
DELETE FROM auditoria WHERE fecha < NOW() - INTERVAL '90 days';

\echo '   ✓ Auditoría antigua eliminada'

-- ═══════ 5. Verificar integridad ═══════
\echo ''
\echo '── 5. Verificación de integridad ──'

-- Verificar que no hay ventas sin empleado
SELECT COUNT(*) AS ventas_sin_empleado
FROM ventas v
LEFT JOIN empleados e ON v.empleado_id = e.id
WHERE e.id IS NULL;

-- Verificar inventario negativo (no debería existir)
SELECT COUNT(*) AS inventario_negativo
FROM inventario WHERE cantidad < 0;

\echo ''
\echo '══════════════════════════════════════════════'
\echo '  MANTENIMIENTO COMPLETADO'
\echo '══════════════════════════════════════════════'
