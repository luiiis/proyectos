# Módulo 19: Docker para Bases de Datos - Manual Técnico

## ¿Qué construimos?
El entorno completo de desarrollo con Docker Compose que levanta 5 servicios:
- PostgreSQL 16 (BD principal)
- MySQL 8 (BD secundaria para comparar)
- Oracle XE 21c (BD enterprise para comparar)
- PgAdmin (GUI para PostgreSQL)
- Adminer (GUI universal)

## ¿Cómo se construyó paso a paso?

### Paso 1: Elegir las imágenes Docker
```
postgres:16-alpine     → Oficial, ligera (alpine = ~50MB vs ~300MB normal)
mysql:8.0              → Oficial
gvenzl/oracle-xe:21-slim → Comunidad (Oracle no tiene imagen oficial ligera)
dpage/pgadmin4         → GUI oficial de PostgreSQL
adminer                → GUI universal ultra-ligera
```

### Paso 2: Definir la red
Todos los contenedores deben verse entre sí por NOMBRE (no por IP).
Docker Compose crea una red automáticamente donde:
- `postgres` resuelve a la IP del contenedor PostgreSQL
- `mysql` resuelve a la IP del contenedor MySQL
- etc.

### Paso 3: Definir volúmenes
Los datos deben PERSISTIR entre reinicios:
- `postgres_data` → /var/lib/postgresql/data
- `mysql_data` → /var/lib/mysql
- `oracle_data` → /opt/oracle/oradata

### Paso 4: Scripts de inicialización
Docker ejecuta automáticamente los .sql de estas carpetas la PRIMERA vez:
- PostgreSQL: `/docker-entrypoint-initdb.d/`
- MySQL: `/docker-entrypoint-initdb.d/`
- Oracle: `/container-entrypoint-initdb.d/`

### Paso 5: Health checks
Docker verifica que cada servicio está REALMENTE listo (no solo corriendo):
- PostgreSQL: `pg_isready` (¿acepta conexiones?)
- MySQL: `mysqladmin ping` (¿responde?)
- Oracle: `healthcheck.sh` (¿la BD está abierta?)

## Diagrama de la infraestructura

```
┌─────────────────────────────────────────────────────────────┐
│                    Docker Engine                              │
│                                                              │
│  ┌─────────────────── Red: db-network ──────────────────┐   │
│  │                                                       │   │
│  │  ┌──────────┐  ┌──────────┐  ┌──────────────────┐   │   │
│  │  │PostgreSQL│  │  MySQL   │  │    Oracle XE     │   │   │
│  │  │  :5432   │  │  :3306   │  │     :1521        │   │   │
│  │  └────┬─────┘  └────┬─────┘  └────────┬─────────┘   │   │
│  │       │              │                  │             │   │
│  │  ┌────┴─────┐  ┌────┴──────────────────┴─────┐      │   │
│  │  │ PgAdmin  │  │         Adminer              │      │   │
│  │  │  :5050   │  │          :8081               │      │   │
│  │  └──────────┘  └─────────────────────────────┘      │   │
│  │                                                       │   │
│  └───────────────────────────────────────────────────────┘   │
│                                                              │
│  Volúmenes:                                                  │
│  ├── postgres_data (persistente)                             │
│  ├── mysql_data (persistente)                                │
│  └── oracle_data (persistente)                               │
└─────────────────────────────────────────────────────────────┘

Puertos expuestos al host:
  localhost:5432 → PostgreSQL
  localhost:3306 → MySQL
  localhost:1521 → Oracle
  localhost:5050 → PgAdmin (web)
  localhost:8081 → Adminer (web)
```

## Comandos del día a día

```bash
# Levantar todo
docker compose up -d

# Ver estado
docker compose ps

# Ver logs de un servicio
docker compose logs -f postgres

# Conectar a PostgreSQL
docker exec -it learn-postgres psql -U postgres -d empresa_db

# Conectar a MySQL
docker exec -it learn-mysql mysql -uroot -pmysql123 empresa_db

# Conectar a Oracle
docker exec -it learn-oracle sqlplus empresa_user/empresa123@XEPDB1

# Detener (mantiene datos)
docker compose stop

# Reanudar
docker compose start

# Destruir todo (BORRA DATOS)
docker compose down -v

# Ver espacio usado
docker system df
```
