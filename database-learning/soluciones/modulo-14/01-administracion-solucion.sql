-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 14: Administración de BD
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. BACKUP Y RESTORE ═══

-- Backup completo (ejecutar en terminal, no en psql):
-- pg_dump -U postgres -Fc empresa_db > backup_$(date +%Y%m%d).dump
-- pg_dump -U postgres --format=custom --compress=9 empresa_db > backup_comprimido.dump

-- Backup solo schema:
-- pg_dump -U postgres --schema-only empresa_db > schema.sql

-- Backup solo datos:
-- pg_dump -U postgres --data-only empresa_db > data.sql

-- Backup de una tabla específica:
-- pg_dump -U postgres -t ventas empresa_db > ventas_backup.sql

-- Restore:
-- pg_restore -U postgres -d empresa_db backup_20260721.dump
-- pg_restore -U postgres --clean --if-exists -d empresa_db backup.dump

-- ═══ 2. MONITOREO DE CONEXIONES ═══

-- Conexiones activas ahora:
SELECT pid, usename, datname, client_addr, state, 
       NOW() - query_start AS duracion, query
FROM pg_stat_activity
WHERE state != 'idle'
ORDER BY duracion DESC;

-- Resumen de conexiones por estado:
SELECT state, COUNT(*) FROM pg_stat_activity GROUP BY state;

-- Conexiones por usuario:
SELECT usename, COUNT(*) AS conexiones FROM pg_stat_activity GROUP BY usename;

-- Matar una query lenta (por PID):
-- SELECT pg_cancel_backend(12345);     -- Cancela la query (suave)
-- SELECT pg_terminate_backend(12345);  -- Termina la conexión (forzado)

-- ═══ 3. TAMAÑO DE LA BD ═══

-- Tamaño total de la BD:
SELECT pg_size_pretty(pg_database_size('empresa_db'));

-- Tamaño por tabla (incluyendo índices):
SELECT tablename,
       pg_size_pretty(pg_total_relation_size(tablename::regclass)) AS total,
       pg_size_pretty(pg_relation_size(tablename::regclass)) AS solo_datos,
       pg_size_pretty(pg_indexes_size(tablename::regclass)) AS indices
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(tablename::regclass) DESC;

-- Top 10 tablas más grandes:
SELECT relname AS tabla, 
       pg_size_pretty(pg_total_relation_size(relid)) AS tamano,
       n_live_tup AS filas_aprox
FROM pg_stat_user_tables
ORDER BY pg_total_relation_size(relid) DESC
LIMIT 10;

-- ═══ 4. VACUUM Y MANTENIMIENTO ═══

-- Ver estado de autovacuum por tabla:
SELECT relname, last_vacuum, last_autovacuum, last_analyze, 
       n_dead_tup, n_live_tup,
       ROUND(n_dead_tup::NUMERIC / NULLIF(n_live_tup, 0) * 100, 2) AS pct_muertos
FROM pg_stat_user_tables
ORDER BY n_dead_tup DESC;

-- Ejecutar VACUUM manualmente:
VACUUM ANALYZE ventas;          -- Limpia + actualiza estadísticas
VACUUM FULL ventas;             -- Compacta (bloquea la tabla)
VACUUM (VERBOSE) ventas;        -- Con detalle de lo que hizo

-- Actualizar estadísticas del planner:
ANALYZE;                        -- Todas las tablas
ANALYZE ventas;                 -- Solo una tabla

-- ═══ 5. LOCKS Y BLOQUEOS ═══

-- Ver locks activos:
SELECT l.pid, l.locktype, l.mode, l.granted,
       a.usename, a.query, a.state
FROM pg_locks l
JOIN pg_stat_activity a ON l.pid = a.pid
WHERE NOT l.granted
ORDER BY l.pid;

-- Ver quién bloquea a quién:
SELECT blocked.pid AS pid_bloqueado,
       blocked.query AS query_bloqueada,
       blocking.pid AS pid_bloqueante,
       blocking.query AS query_bloqueante
FROM pg_stat_activity blocked
JOIN pg_locks bl ON bl.pid = blocked.pid AND NOT bl.granted
JOIN pg_locks lk ON lk.locktype = bl.locktype 
     AND lk.relation = bl.relation AND lk.pid != bl.pid AND lk.granted
JOIN pg_stat_activity blocking ON blocking.pid = lk.pid;

-- ═══ 6. RENDIMIENTO Y ESTADÍSTICAS ═══

-- Hits de caché (debería ser >99%):
SELECT
    sum(heap_blks_read) AS bloques_disco,
    sum(heap_blks_hit) AS bloques_cache,
    ROUND(sum(heap_blks_hit) * 100.0 / NULLIF(sum(heap_blks_hit) + sum(heap_blks_read), 0), 2) AS hit_ratio_pct
FROM pg_statio_user_tables;

-- Índices más usados:
SELECT indexrelname, idx_scan, idx_tup_read, idx_tup_fetch
FROM pg_stat_user_indexes
ORDER BY idx_scan DESC LIMIT 10;

-- Tablas con más operaciones:
SELECT relname,
       seq_scan, seq_tup_read,
       idx_scan, idx_tup_fetch,
       n_tup_ins AS inserts,
       n_tup_upd AS updates,
       n_tup_del AS deletes
FROM pg_stat_user_tables
ORDER BY (n_tup_ins + n_tup_upd + n_tup_del) DESC
LIMIT 10;

-- ═══ 7. REPLICACIÓN (verificar estado) ═══

-- Ver si soy primary o replica:
SELECT pg_is_in_recovery();  -- false = primary, true = replica

-- Ver estado de replicación (en el primary):
SELECT client_addr, state, sent_lsn, write_lsn, flush_lsn, replay_lsn,
       pg_size_pretty(pg_wal_lsn_diff(sent_lsn, replay_lsn)) AS replication_lag
FROM pg_stat_replication;

-- ═══ 8. CONFIGURACIÓN ACTUAL ═══

-- Ver parámetros importantes:
SELECT name, setting, unit, short_desc
FROM pg_settings
WHERE name IN ('shared_buffers', 'effective_cache_size', 'work_mem', 
               'maintenance_work_mem', 'max_connections', 'wal_level',
               'max_wal_size', 'checkpoint_completion_target');

-- Ver configuración que difiere del default:
SELECT name, setting, boot_val, reset_val
FROM pg_settings
WHERE setting != boot_val AND source != 'default';

-- ═══ 9. TABLESPACES (organizar almacenamiento) ═══

-- Ver tablespaces existentes:
SELECT spcname, pg_tablespace_location(oid), pg_size_pretty(pg_tablespace_size(spcname))
FROM pg_tablespace;

-- Crear tablespace en disco rápido (SSD) para tablas frecuentes:
-- CREATE TABLESPACE fast_storage LOCATION '/mnt/ssd/pgdata';
-- ALTER TABLE ventas SET TABLESPACE fast_storage;

-- ═══ 10. SCRIPT DE HEALTH CHECK ═══

-- Query que da un resumen rápido del estado de la BD:
SELECT
    'Versión' AS metrica, version() AS valor
UNION ALL
SELECT 'Uptime', (NOW() - pg_postmaster_start_time())::TEXT
UNION ALL
SELECT 'Tamaño BD', pg_size_pretty(pg_database_size(current_database()))
UNION ALL
SELECT 'Conexiones activas', (SELECT COUNT(*)::TEXT FROM pg_stat_activity WHERE state != 'idle')
UNION ALL
SELECT 'Conexiones totales', (SELECT COUNT(*)::TEXT FROM pg_stat_activity)
UNION ALL
SELECT 'Locks pendientes', (SELECT COUNT(*)::TEXT FROM pg_locks WHERE NOT granted)
UNION ALL
SELECT 'Cache hit ratio', (
    SELECT ROUND(sum(heap_blks_hit) * 100.0 / NULLIF(sum(heap_blks_hit) + sum(heap_blks_read), 0), 2)::TEXT || '%'
    FROM pg_statio_user_tables
)
UNION ALL
SELECT 'Dead tuples totales', (SELECT SUM(n_dead_tup)::TEXT FROM pg_stat_user_tables)
UNION ALL
SELECT 'Último VACUUM global', (SELECT MAX(last_autovacuum)::TEXT FROM pg_stat_user_tables);
