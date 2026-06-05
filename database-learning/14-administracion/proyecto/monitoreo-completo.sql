-- ════════════════════════════════════════════════════════════════
-- MÓDULO 14 - PROYECTO: Sistema de Monitoreo PostgreSQL
-- ════════════════════════════════════════════════════════════════
-- Este script genera un reporte completo del estado de la BD.
-- Ejecútalo periódicamente para detectar problemas antes de que exploten.
-- ════════════════════════════════════════════════════════════════

-- ═══════ 1. INFORMACIÓN GENERAL DE LA BD ═══════
\echo '══════════════════════════════════════════════'
\echo '  REPORTE DE MONITOREO - PostgreSQL'
\echo '══════════════════════════════════════════════'
\echo ''

\echo '── 1. Información de la Base de Datos ──'
SELECT
    current_database() AS base_datos,
    pg_size_pretty(pg_database_size(current_database())) AS tamaño_total,
    (SELECT count(*) FROM pg_stat_activity WHERE datname = current_database()) AS conexiones_activas,
    (SELECT setting FROM pg_settings WHERE name = 'max_connections') AS max_conexiones,
    now() AS fecha_reporte;

-- ═══════ 2. TAMAÑO DE TABLAS ═══════
\echo ''
\echo '── 2. Top 10 Tablas por Tamaño ──'
SELECT
    tablename AS tabla,
    pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) AS tamaño_total,
    pg_size_pretty(pg_relation_size(schemaname || '.' || tablename)) AS datos,
    pg_size_pretty(pg_indexes_size(schemaname || '.' || tablename)) AS indices,
    (SELECT reltuples::bigint FROM pg_class WHERE relname = tablename) AS filas_aprox
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname || '.' || tablename) DESC
LIMIT 10;

-- ═══════ 3. CONEXIONES ACTIVAS ═══════
\echo ''
\echo '── 3. Conexiones Activas ──'
SELECT
    state AS estado,
    count(*) AS cantidad,
    CASE state
        WHEN 'active' THEN '⚡ Ejecutando query'
        WHEN 'idle' THEN '💤 Esperando'
        WHEN 'idle in transaction' THEN '⚠️ Transacción abierta sin actividad'
        ELSE state
    END AS descripcion
FROM pg_stat_activity
WHERE datname = current_database()
GROUP BY state
ORDER BY cantidad DESC;

-- ═══════ 4. QUERIES LENTAS (corriendo ahora) ═══════
\echo ''
\echo '── 4. Queries Corriendo > 5 segundos ──'
SELECT
    pid,
    usename AS usuario,
    now() - query_start AS duracion,
    state,
    LEFT(query, 80) AS query_truncada
FROM pg_stat_activity
WHERE state = 'active'
AND query_start < now() - interval '5 seconds'
AND query NOT LIKE '%pg_stat_activity%'
ORDER BY query_start;

-- ═══════ 5. TABLAS QUE NECESITAN VACUUM ═══════
\echo ''
\echo '── 5. Tablas con Dead Tuples (necesitan VACUUM) ──'
SELECT
    relname AS tabla,
    n_live_tup AS filas_vivas,
    n_dead_tup AS filas_muertas,
    CASE WHEN n_live_tup > 0
        THEN round(n_dead_tup::numeric / n_live_tup * 100, 1)
        ELSE 0
    END AS pct_muertas,
    last_vacuum,
    last_autovacuum
FROM pg_stat_user_tables
WHERE n_dead_tup > 100
ORDER BY n_dead_tup DESC
LIMIT 10;

-- ═══════ 6. ÍNDICES NO UTILIZADOS ═══════
\echo ''
\echo '── 6. Índices que NUNCA se han usado (candidatos a eliminar) ──'
SELECT
    indexrelname AS indice,
    relname AS tabla,
    idx_scan AS veces_usado,
    pg_size_pretty(pg_relation_size(indexrelid)) AS tamaño
FROM pg_stat_user_indexes
WHERE idx_scan = 0
AND schemaname = 'public'
ORDER BY pg_relation_size(indexrelid) DESC
LIMIT 10;

-- ═══════ 7. CACHE HIT RATIO ═══════
\echo ''
\echo '── 7. Cache Hit Ratio (debe ser > 99%) ──'
SELECT
    'Tablas' AS tipo,
    round(sum(heap_blks_hit)::numeric / NULLIF(sum(heap_blks_hit) + sum(heap_blks_read), 0) * 100, 2) AS hit_ratio_pct
FROM pg_statio_user_tables
UNION ALL
SELECT
    'Índices',
    round(sum(idx_blks_hit)::numeric / NULLIF(sum(idx_blks_hit) + sum(idx_blks_read), 0) * 100, 2)
FROM pg_statio_user_indexes;

-- ═══════ 8. TRANSACCIONES ABIERTAS MUCHO TIEMPO ═══════
\echo ''
\echo '── 8. Transacciones abiertas > 10 minutos (peligro de bloqueo) ──'
SELECT
    pid,
    usename,
    now() - xact_start AS duracion_transaccion,
    state,
    LEFT(query, 60) AS ultima_query
FROM pg_stat_activity
WHERE xact_start IS NOT NULL
AND xact_start < now() - interval '10 minutes'
AND state != 'idle';

-- ═══════ 9. ESPACIO EN DISCO ═══════
\echo ''
\echo '── 9. Uso de Disco por Tablespace ──'
SELECT
    spcname AS tablespace,
    pg_size_pretty(pg_tablespace_size(spcname)) AS tamaño
FROM pg_tablespace;

\echo ''
\echo '══════════════════════════════════════════════'
\echo '  FIN DEL REPORTE'
\echo '══════════════════════════════════════════════'
