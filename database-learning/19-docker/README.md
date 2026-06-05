# Módulo 19: Docker para Bases de Datos

## ¿Por qué Docker para BDs?
- Levantar PostgreSQL + MySQL + Oracle en 1 comando
- Mismo entorno en cualquier máquina
- Destruir y recrear en segundos (experimentar sin miedo)
- No contaminar tu sistema operativo con instalaciones

---

## 1. Levantar el Entorno Completo

```bash
# Desde la raíz del proyecto:
cd database-learning
docker compose up -d

# Ver estado
docker compose ps

# Ver logs
docker compose logs -f postgres
docker compose logs -f mysql
docker compose logs -f oracle

# Detener todo
docker compose down

# Detener Y borrar datos (reset completo)
docker compose down -v
```

## 2. Conectar a Cada BD

### PostgreSQL
```bash
# Desde terminal
docker exec -it learn-postgres psql -U postgres -d empresa_db

# Comandos útiles dentro de psql:
\l          -- Listar bases de datos
\dt         -- Listar tablas
\d productos -- Describir tabla
\q          -- Salir
```

### MySQL
```bash
docker exec -it learn-mysql mysql -uroot -pmysql123 empresa_db

# Comandos útiles:
SHOW DATABASES;
SHOW TABLES;
DESCRIBE productos;
EXIT;
```

### Oracle
```bash
docker exec -it learn-oracle sqlplus empresa_user/empresa123@XEPDB1

# Comandos útiles:
SELECT table_name FROM user_tables;
DESC productos;
EXIT;
```

## 3. Herramientas GUI

### PgAdmin (PostgreSQL)
```
URL: http://localhost:5050
Email: admin@admin.com
Password: admin123

Agregar servidor:
  Host: postgres (nombre del contenedor en la red Docker)
  Port: 5432
  Username: postgres
  Password: postgres123
  Database: empresa_db
```

### Adminer (Universal)
```
URL: http://localhost:8081

Para PostgreSQL:
  System: PostgreSQL
  Server: postgres
  Username: postgres
  Password: postgres123
  Database: empresa_db

Para MySQL:
  System: MySQL
  Server: mysql
  Username: root
  Password: mysql123
  Database: empresa_db
```

### DBeaver (Recomendado - instalar en tu máquina)
```
Descarga: https://dbeaver.io/download/
Soporta: PostgreSQL, MySQL, Oracle, SQL Server, y 50+ más

Conexión PostgreSQL:
  Host: localhost
  Port: 5432
  Database: empresa_db
  Username: postgres
  Password: postgres123
```

## 4. Docker Compose Explicado Línea por Línea

```yaml
services:
  postgres:
    image: postgres:16-alpine     # Imagen oficial, versión 16, variante ligera
    container_name: learn-postgres # Nombre fijo del contenedor
    environment:
      POSTGRES_USER: postgres      # Usuario admin
      POSTGRES_PASSWORD: postgres123  # Password (cambiar en producción)
      POSTGRES_DB: empresa_db      # BD creada automáticamente al arrancar
    ports:
      - "5432:5432"               # host:contenedor (accesible desde tu máquina)
    volumes:
      - postgres_data:/var/lib/postgresql/data  # Datos persisten entre reinicios
      - ./19-docker/init/postgres:/docker-entrypoint-initdb.d  # Scripts de inicio
      # Docker ejecuta TODOS los .sql de esta carpeta al crear la BD por primera vez
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]  # Verificar que está listo
      interval: 5s    # Cada 5 segundos
      timeout: 3s     # Si no responde en 3s, falla
      retries: 5      # Después de 5 fallos → unhealthy
```

## 5. Comandos Docker Útiles

```bash
# Ver uso de recursos
docker stats

# Entrar a un contenedor
docker exec -it learn-postgres bash

# Copiar archivo al contenedor
docker cp mi_script.sql learn-postgres:/tmp/

# Ejecutar script SQL
docker exec -i learn-postgres psql -U postgres -d empresa_db < mi_script.sql

# Backup desde Docker
docker exec learn-postgres pg_dump -U postgres empresa_db > backup.sql

# Restaurar
docker exec -i learn-postgres psql -U postgres -d empresa_db < backup.sql

# Ver tamaño de volúmenes
docker system df -v
```

## 6. Ejercicios

1. Levanta el entorno y verifica que las 3 BDs tienen datos
2. Conecta con DBeaver a las 3 BDs simultáneamente
3. Ejecuta la misma query en PostgreSQL, MySQL y Oracle. Nota las diferencias de sintaxis
4. Haz un backup de PostgreSQL y restáuralo en una BD nueva
5. Destruye el volumen de MySQL y verifica que se recrean los datos al levantar

---

## Siguiente Módulo
→ [20-Spring Boot](../20-springboot/README.md)
