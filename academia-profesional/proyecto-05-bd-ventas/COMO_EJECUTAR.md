# Cómo Ejecutar - Proyecto 05: Base de Datos de Ventas

## Requisitos
- Docker instalado (`docker --version`)
- Docker Compose (`docker compose version`)
- Cliente psql o DBeaver (opcional, para explorar)

## 1. Levantar PostgreSQL

```bash
cd academia-profesional/proyecto-05-bd-ventas

# Levantar el contenedor
docker compose up -d
```

El `docker-compose.yml` levanta PostgreSQL 16 en el puerto 5432.

## 2. Crear el esquema

```bash
# Ejecutar script de creación de tablas
docker exec -i postgres-ventas psql -U admin -d ventas_db < sql/01-schema.sql

# Crear triggers
docker exec -i postgres-ventas psql -U admin -d ventas_db < sql/02-triggers.sql

# Crear procedures
docker exec -i postgres-ventas psql -U admin -d ventas_db < sql/03-procedures.sql

# Crear views
docker exec -i postgres-ventas psql -U admin -d ventas_db < sql/04-views.sql

# Crear índices
docker exec -i postgres-ventas psql -U admin -d ventas_db < sql/05-indices.sql

# Insertar datos de prueba
docker exec -i postgres-ventas psql -U admin -d ventas_db < sql/06-seeds.sql
```

## 3. Conectarse a la base de datos

```bash
docker exec -it postgres-ventas psql -U admin -d ventas_db
```

## Credenciales
- Host: localhost
- Puerto: 5432
- Base de datos: ventas_db
- Usuario: admin
- Password: admin123

## Detener

```bash
docker compose down        # Detener (mantiene datos)
docker compose down -v     # Detener y borrar datos
```

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| `port already in use` | Puerto 5432 ocupado | Cambiar puerto en docker-compose.yml |
| `relation already exists` | Script ya ejecutado | Usar `DROP IF EXISTS` o recrear |
| `permission denied` | Usuario sin permisos | Verificar que usas usuario admin |
