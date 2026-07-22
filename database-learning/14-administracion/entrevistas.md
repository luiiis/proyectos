# Preguntas de Entrevista - Módulo 14: Administración de BD

## Nivel Junior

### 1. ¿Cómo haces un backup de PostgreSQL?
**Respuesta:**
```bash
# Backup completo (custom format, comprimido):
pg_dump -U postgres -Fc empresa_db > backup_20260721.dump

# Backup solo schema (sin datos):
pg_dump -U postgres --schema-only empresa_db > schema.sql

# Backup solo datos:
pg_dump -U postgres --data-only empresa_db > data.sql

# Restore:
pg_restore -U postgres -d empresa_db backup_20260721.dump
```

### 2. ¿Cuál es la diferencia entre backup lógico y físico?
**Respuesta:**
- **Lógico** (`pg_dump`): exporta SQL (CREATE TABLE + INSERT). Portable, versionable, pero lento con BDs grandes.
- **Físico** (`pg_basebackup`): copia los archivos de datos raw. Muy rápido, pero solo funciona con la misma versión de PostgreSQL.

### 3. ¿Qué es un WAL (Write-Ahead Log)?
**Respuesta:**
Registro de TODAS las operaciones antes de escribirlas en disco. Garantiza durabilidad:
- Si hay crash durante un write → el WAL permite recuperar
- Base de replication y Point-in-Time Recovery (PITR)
- Sin WAL: crash = datos corruptos

### 4. ¿Cómo monitoreas el estado de la BD?
**Respuesta:**
```sql
-- Conexiones activas:
SELECT * FROM pg_stat_activity WHERE state != 'idle';

-- Tamaño de tablas:
SELECT tablename, pg_size_pretty(pg_total_relation_size(tablename::text))
FROM pg_tables WHERE schemaname = 'public' ORDER BY pg_total_relation_size(tablename::text) DESC;

-- Queries lentas activas:
SELECT pid, now() - query_start AS duration, query FROM pg_stat_activity
WHERE state = 'active' ORDER BY duration DESC;

-- Locks:
SELECT * FROM pg_locks WHERE NOT granted;
```

### 5. ¿Qué es connection pooling y por qué es importante?
**Respuesta:**
Reutilizar conexiones en vez de crear una nueva por cada request:
- Sin pool: 1000 requests = 1000 conexiones = PostgreSQL se satura (default max: 100)
- Con pool: 1000 requests reutilizan 20-30 conexiones

Herramientas: PgBouncer (externo), HikariCP (en la app Java).

---

## Nivel Mid

### 6. ¿Cómo configurarías replicación para alta disponibilidad?
**Respuesta:**
PostgreSQL streaming replication:
- **Primary:** recibe todas las escrituras, genera WAL
- **Replica(s):** reciben WAL en tiempo real, atienden lecturas
- Si el Primary muere → promover una réplica a Primary (failover)

Herramientas: Patroni (failover automático), pgpool-II (load balancing).

### 7. ¿Cuáles son los parámetros más importantes de postgresql.conf?
**Respuesta:**
```
shared_buffers = 25% de RAM (caché de datos en memoria)
effective_cache_size = 75% de RAM (hint para el planner)
work_mem = 256MB (memoria por operación de sort/hash)
maintenance_work_mem = 512MB (para VACUUM, CREATE INDEX)
max_connections = 200 (limitar, usar pooling)
wal_level = replica (para replicación)
```
