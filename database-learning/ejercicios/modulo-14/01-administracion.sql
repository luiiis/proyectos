-- ════════════════════════════════════════════════════════════════
-- MÓDULO 14 - EJERCICIO 1: Administración de BD
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Estos ejercicios se ejecutan tanto dentro de psql como desde terminal bash.
-- Los comandos bash se ejecutan con: docker exec -it learn-postgres bash
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 14.1: Backup y Restore
-- ═══════════════════════════════════════════════════════════════
-- Desde TERMINAL (no psql):

-- a) Backup completo en formato SQL:
-- docker exec learn-postgres pg_dump -U postgres -d empresa_db > backup_empresa.sql

-- b) Backup comprimido (custom format):
-- docker exec learn-postgres pg_dump -U postgres -Fc -d empresa_db > backup_empresa.dump

-- c) Backup solo de la tabla productos:
-- docker exec learn-postgres pg_dump -U postgres -t productos -d empresa_db > backup_productos.sql

-- d) Restaurar en una BD nueva:
-- docker exec learn-postgres psql -U postgres -c "CREATE DATABASE empresa_restore;"
-- docker exec -i learn-postgres psql -U postgres -d empresa_restore < backup_empresa.sql

-- TAREA: Ejecuta los 4 comandos y verifica que el restore funciona.
-- Conéctate a empresa_restore y verifica: SELECT COUNT(*) FROM productos;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 14.2: Monitoreo de la BD
-- ═══════════════════════════════════════════════════════════════

-- a) Ver conexiones activas:
SELECT pid, usename, application_name, state, query_start, query
FROM pg_stat_activity
WHERE datname = 'empresa_db'
ORDER BY query_start DESC;

-- b) Ver tamaño de cada tabla:
SELECT
    tablename,
    pg_size_pretty(pg_total_relation_size(schemaname || '.' || tablename)) AS tamaño_total,
    pg_size_pretty(pg_relation_size(schemaname || '.' || tablename)) AS tamaño_datos,
    pg_size_pretty(pg_indexes_size(schemaname || '.' || tablename)) AS tamaño_indices
FROM pg_tables
WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(schemaname || '.' || tablename) DESC;

-- c) Ver queries más lentas (necesita pg_stat_statements):
-- CREATE EXTENSION IF NOT EXISTS pg_stat_statements;
-- SELECT query, calls, mean_exec_time, total_exec_time
-- FROM pg_stat_statements ORDER BY mean_exec_time DESC LIMIT 10;

-- TAREA: Ejecuta a) y b). ¿Cuál es la tabla más grande? ¿Cuánto ocupan los índices?


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 14.3: Mantenimiento
-- ═══════════════════════════════════════════════════════════════

-- a) VACUUM: recuperar espacio de filas eliminadas
-- Primero, elimina muchas filas para generar "dead tuples":
DELETE FROM auditoria WHERE fecha < '2024-01-01';

-- Ver dead tuples ANTES del vacuum:
SELECT relname, n_dead_tup, n_live_tup, last_vacuum, last_autovacuum
FROM pg_stat_user_tables
WHERE relname = 'auditoria';

-- Ejecutar VACUUM:
VACUUM ANALYZE auditoria;

-- Ver dead tuples DESPUÉS:
SELECT relname, n_dead_tup, n_live_tup, last_vacuum
FROM pg_stat_user_tables
WHERE relname = 'auditoria';

-- b) REINDEX: reconstruir índices fragmentados
REINDEX TABLE ventas;

-- c) Actualizar estadísticas para el query planner:
ANALYZE;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 14.4: Configuración de PostgreSQL
-- ═══════════════════════════════════════════════════════════════

-- Ver configuración actual:
SHOW max_connections;
SHOW shared_buffers;
SHOW work_mem;
SHOW effective_cache_size;
SHOW maintenance_work_mem;

-- PREGUNTAS:
-- a) ¿Cuántas conexiones máximas permite?
-- b) ¿Cuánta memoria tiene asignada para caché?
-- c) Si tuvieras un servidor con 16GB RAM, ¿qué valores pondrías?
--    shared_buffers = ? (recomendado: 25% de RAM)
--    effective_cache_size = ? (recomendado: 75% de RAM)
--    work_mem = ? (recomendado: RAM / max_connections / 4)


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 14.5: Automatizar backup con script
-- ═══════════════════════════════════════════════════════════════
-- Crea un script bash que:
-- 1. Haga backup con fecha en el nombre (backup_20260530.sql)
-- 2. Comprima el backup con gzip
-- 3. Elimine backups de más de 7 días
-- 4. Muestre un mensaje de confirmación

-- Crea el archivo: database-learning/scripts/backup-diario.sh
-- Contenido sugerido:
-- #!/bin/bash
-- FECHA=$(date +%Y%m%d)
-- docker exec learn-postgres pg_dump -U postgres -Fc empresa_db > backups/backup_${FECHA}.dump
-- find backups/ -name "*.dump" -mtime +7 -delete
-- echo "Backup completado: backup_${FECHA}.dump"
