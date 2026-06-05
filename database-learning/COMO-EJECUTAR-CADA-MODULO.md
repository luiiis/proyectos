# 📋 Cómo Ejecutar Cada Módulo

## Prerrequisito: Levantar Docker

```bash
cd database-learning
docker compose up -d
# Esperar 30 segundos (Oracle tarda más)
docker compose ps  # Verificar que todo dice "healthy"
```

---

## Módulos SQL (01-18): Ejecutar scripts contra PostgreSQL

### Comando base:
```bash
docker exec -i learn-postgres psql -U postgres -d empresa_db < RUTA_DEL_ARCHIVO.sql
```

### Tabla de ejecución:

| Módulo | Comando | Qué hace |
|--------|---------|----------|
| 01 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 01-fundamentos/proyecto/demo.sql` | Explora tablas y relaciones |
| 02 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 02-ddl/proyecto/demo.sql` | Crea y modifica tablas |
| 03 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 03-dml/proyecto/demo.sql` | INSERT, UPDATE, DELETE, UPSERT |
| 04 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 04-consultas/proyecto/demo.sql` | SELECT con GROUP BY, HAVING |
| 05 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 05-joins/proyecto/demo.sql` | INNER, LEFT, SELF JOIN |
| 06 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 06-funciones/proyecto/demo.sql` | Texto, números, fechas, Window |
| 07 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 07-subconsultas/proyecto/demo.sql` | IN, EXISTS, CTEs |
| 08 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 08-vistas/proyecto/demo.sql` | CREATE VIEW, consultar vistas |
| 09 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 09-indices/proyecto/demo.sql` | EXPLAIN ANALYZE, crear índices |
| 10 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 10-triggers/proyecto/demo.sql` | Trigger de auditoría |
| 11 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 11-procedimientos/proyecto/demo.sql` | Functions y Procedures |
| 12 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 12-transacciones/proyecto/demo.sql` | BEGIN, COMMIT, ROLLBACK |
| 13 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 13-seguridad/proyecto/demo.sql` | Roles y permisos |
| 14 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 14-administracion/proyecto/monitoreo-completo.sql` | Monitoreo de la BD |
| 15 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 15-optimizacion/proyecto/laboratorio-performance.sql` | Tuning de queries |
| 16 | `docker exec -i learn-postgres psql -U postgres -d empresa_db < 16-postgresql/proyecto/features-avanzadas.sql` | JSONB, FTS, CTEs recursivas |
| 17 | `docker exec -i learn-mysql mysql -uroot -pmysql123 empresa_db < 17-mysql/proyecto/mysql-especifico.sql` | Features MySQL |
| 18 | `docker exec -it learn-oracle sqlplus empresa_user/empresa123@XEPDB1 @18-oracle/proyecto/oracle-especifico.sql` | PL/SQL Oracle |

---

## Módulo 19 (Docker): Ya está corriendo

El módulo 19 ES el docker-compose.yml que levantaste. Su documentación está en:
- `19-docker/proyecto/MANUAL-TECNICO.md`

---

## Módulo 20 (Spring Boot): Levantar Backend

```bash
cd database-learning/20-springboot/proyecto/app

# Asegúrate de que PostgreSQL está corriendo (docker compose up postgres -d)
mvn spring-boot:run

# Verificar:
# http://localhost:8080/api/productos
# http://localhost:8080/swagger-ui.html
```

---

## Módulo 21 (Proyecto Final): Levantar TODO

```bash
cd database-learning/21-proyecto-final/proyecto

# Opción 1: Todo con Docker
docker compose up --build -d

# Opción 2: BD en Docker + Backend local
docker compose up postgres redis -d
cd app && mvn spring-boot:run

# Opción 3: BD + Backend + Frontend
docker compose up postgres redis -d
cd app && mvn spring-boot:run &
cd ../frontend && npm install && ng serve

# Verificar:
# BD: docker exec -it proyecto-final-postgres psql -U postgres -d empresa_db
# API: http://localhost:8080/api/productos
# Swagger: http://localhost:8080/swagger-ui.html
# Frontend: http://localhost:4200
```

---

## Ejercicios (carpeta ejercicios/)

```bash
# Los ejercicios tienen espacios "-- TU QUERY:" para que escribas tu código.
# Ábrelos en un editor, escribe tu solución, y ejecútala:

docker exec -i learn-postgres psql -U postgres -d empresa_db < ejercicios/modulo-04/01-consultas-basicas.sql

# Las soluciones están en:
docker exec -i learn-postgres psql -U postgres -d empresa_db < soluciones/modulo-04/01-consultas-basicas-solucion.sql
```

---

## Resetear todo (empezar de cero)

```bash
cd database-learning
docker compose down -v    # Borra contenedores Y datos
docker compose up -d      # Recrea todo desde cero
# Los scripts de init se re-ejecutan automáticamente
```

---

## Resumen de puertos

| Servicio | Puerto | Acceso |
|----------|--------|--------|
| PostgreSQL | 5432 | `psql -h localhost -U postgres -d empresa_db` |
| MySQL | 3306 | `mysql -h localhost -uroot -pmysql123 empresa_db` |
| Oracle | 1521 | `sqlplus empresa_user/empresa123@localhost:1521/XEPDB1` |
| PgAdmin | 5050 | http://localhost:5050 (admin@admin.com / admin123) |
| Adminer | 8081 | http://localhost:8081 |
| Backend API | 8080 | http://localhost:8080/api/productos |
| Swagger | 8080 | http://localhost:8080/swagger-ui.html |
| Frontend | 4200 | http://localhost:4200 |
