# Módulo 14: Administración de Bases de Datos

## 1. Respaldos (Backup)

```bash
# ═══════ PostgreSQL ═══════
# Backup completo (SQL)
pg_dump -U postgres -d empresa_db > backup_empresa_$(date +%Y%m%d).sql

# Backup comprimido (custom format - más rápido para restaurar)
pg_dump -U postgres -Fc -d empresa_db > backup_empresa.dump

# Backup solo estructura (sin datos)
pg_dump -U postgres --schema-only -d empresa_db > schema_only.sql

# Backup solo datos
pg_dump -U postgres --data-only -d empresa_db > data_only.sql

# Backup de una tabla específica
pg_dump -U postgres -t ventas -d empresa_db > ventas_backup.sql

# ═══════ MySQL ═══════
mysqldump -u root -p empresa_db > backup_empresa.sql
mysqldump -u root -p --all-databases > backup_todo.sql

# ═══════ Oracle ═══════
expdp system/oracle123 schemas=empresa_user directory=DATA_PUMP_DIR dumpfile=backup.dmp
```

## 2. Restauración (Restore)

```bash
# PostgreSQL
psql -U postgres -d empresa_db < backup_empresa.sql
pg_restore -U postgres -d empresa_db backup_empresa.dump

# MySQL
mysql -u root -p empresa_db < backup_empresa.sql

# Oracle
impdp system/oracle123 schemas=empresa_user directory=DATA_PUMP_DIR dumpfile=backup.dmp
```

## 3. Monitoreo

```sql
-- PostgreSQL: queries activas
SELECT pid, usename, state, query, query_start
FROM pg_stat_activity
WHERE state = 'active';

-- Tamaño de tablas
SELECT tablename, pg_size_pretty(pg_total_relation_size(tablename::text))
FROM pg_tables WHERE schemaname = 'public'
ORDER BY pg_total_relation_size(tablename::text) DESC;

-- Conexiones activas
SELECT count(*) FROM pg_stat_activity;

-- Queries lentas (habilitar pg_stat_statements)
SELECT query, calls, mean_exec_time, total_exec_time
FROM pg_stat_statements
ORDER BY mean_exec_time DESC LIMIT 10;
```

## 4. Mantenimiento

```sql
-- VACUUM: recuperar espacio de filas eliminadas
VACUUM ANALYZE productos;  -- Actualiza estadísticas también
VACUUM FULL ventas;        -- Compacta la tabla (bloquea)

-- REINDEX: reconstruir índices fragmentados
REINDEX TABLE productos;
REINDEX DATABASE empresa_db;

-- Estadísticas para el query planner
ANALYZE productos;
ANALYZE;  -- Todas las tablas
```

## 5. Ejercicios
1. Configura un backup automático diario con cron
2. Practica restaurar un backup en una BD nueva
3. Identifica las 5 queries más lentas de tu sistema
4. ¿Cuánto espacio ocupa cada tabla? ¿Cuál necesita VACUUM?
5. Configura alertas cuando las conexiones superen el 80% del máximo
