# Cómo se Construyó - Proyecto 04: Clientes JDBC

## Paso 1: ¿Qué problema resuelve?
Conectar Java directamente a PostgreSQL sin frameworks (entender qué hace JPA por debajo).

## Paso 2: Componentes
```
Docker Compose → levanta PostgreSQL con tabla "clientes" pre-creada
ClientesJDBC.java → se conecta y hace CRUD con PreparedStatement
```

## Paso 3: Flujo
```
1. docker compose up -d (PostgreSQL + tabla + datos)
2. Java se conecta via JDBC URL: jdbc:postgresql://localhost:5432/clientes_db
3. PreparedStatement previene SQL Injection (NUNCA concatenar strings)
4. ResultSet itera sobre los resultados
5. Connection se cierra con try-with-resources (no hay memory leaks)
```

## Paso 4: Ejecutar
```bash
cd academia-profesional/proyecto-04-clientes-jdbc

# 1. Levantar BD
docker compose up -d

# 2. Verificar que la tabla existe
docker exec -it jdbc-postgres psql -U postgres -d clientes_db -c "SELECT * FROM clientes;"

# 3. Ejecutar Java (necesitas el driver PostgreSQL en classpath)
cd src
javac ClientesJDBC.java
java -cp .:../lib/postgresql-42.7.3.jar ClientesJDBC
# O más fácil: copiar este código al proyecto Maven del módulo 06/07
```

## Base de datos
La tabla se crea automáticamente al levantar Docker (script en `sql/schema.sql`):
```sql
CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    ciudad VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);
```
