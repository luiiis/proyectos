# Cómo Ejecutar - Proyecto 04: Sistema de Clientes JDBC

## Requisitos
- Java 21+ instalado (`java -version`)
- Docker instalado (`docker --version`)
- Driver PostgreSQL JDBC (postgresql-42.7.1.jar)

## 1. Levantar PostgreSQL con Docker

```bash
docker run -d \
  --name postgres-clientes \
  -e POSTGRES_DB=clientes_db \
  -e POSTGRES_USER=admin \
  -e POSTGRES_PASSWORD=admin123 \
  -p 5432:5432 \
  postgres:16
```

## 2. Crear las tablas

```bash
docker exec -i postgres-clientes psql -U admin -d clientes_db < sql/schema.sql
```

## 3. Compilar y Ejecutar

```bash
cd academia-profesional/proyecto-04-clientes-jdbc

# Compilar con el driver en classpath
javac -cp lib/postgresql-42.7.1.jar -d bin src/*.java

# Ejecutar
java -cp bin:lib/postgresql-42.7.1.jar Main
```

## Uso
```
═══ SISTEMA DE CLIENTES ═══
1. Registrar cliente
2. Listar clientes
3. Buscar por nombre
4. Actualizar cliente
5. Eliminar cliente
6. Salir
Conectado a PostgreSQL ✓
```

## Solución de errores

| Error | Causa | Solución |
|-------|-------|----------|
| `Connection refused` | PostgreSQL no levantado | `docker start postgres-clientes` |
| `ClassNotFoundException: org.postgresql` | Driver no en classpath | Verificar ruta del .jar |
| `PSQLException: authentication` | Credenciales incorrectas | Verificar user/password |

## Detener PostgreSQL

```bash
docker stop postgres-clientes
docker rm postgres-clientes
```
