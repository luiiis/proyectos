# Módulo 14: Administración - Manual Técnico del Proyecto

## ¿Qué construimos aquí?
Un sistema completo de administración de PostgreSQL que incluye:
- Scripts de backup automático (completo, incremental, por tabla)
- Sistema de monitoreo (queries lentas, conexiones, espacio)
- Plan de mantenimiento (VACUUM, REINDEX, estadísticas)
- Alertas automáticas cuando algo está mal

## ¿Por qué existe este proyecto?
En producción, la BD no se cuida sola. Sin administración:
- Los backups no existen → pierdes datos
- Las tablas se fragmentan → todo se vuelve lento
- Las conexiones se agotan → la app se cae
- El disco se llena → la BD se corrompe

## Cómo se construyó (paso a paso)

### Paso 1: Entender qué necesita monitoreo
Antes de escribir código, un DBA Senior identifica:
1. ¿Qué puede fallar? → Disco lleno, conexiones agotadas, queries lentas
2. ¿Qué datos necesito? → pg_stat_activity, pg_stat_user_tables, pg_database
3. ¿Cada cuánto verifico? → Cada 5 min (monitoreo), diario (backup), semanal (mantenimiento)

### Paso 2: Crear los scripts de monitoreo
Se crearon queries que consultan las vistas de sistema de PostgreSQL.

### Paso 3: Automatizar con scripts bash
Los scripts se ejecutan con cron (Linux) o Task Scheduler (Windows).

### Paso 4: Crear alertas
Si algo supera un umbral → se registra en una tabla de alertas.

## Cómo ejecutar
```bash
# Desde la raíz del proyecto database-learning:
cd 14-administracion/proyecto

# Ejecutar monitoreo completo
docker exec -i learn-postgres psql -U postgres -d empresa_db < monitoreo-completo.sql

# Ejecutar mantenimiento
docker exec -i learn-postgres psql -U postgres -d empresa_db < mantenimiento.sql

# Backup
bash ../../scripts/backup-diario.sh
```
