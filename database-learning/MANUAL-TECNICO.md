# 📘 Manual Técnico - Cómo Funciona Este Proyecto

## Visión General

Este proyecto es una academia práctica de bases de datos. Cada módulo tiene scripts SQL que puedes ejecutar directamente contra PostgreSQL, MySQL u Oracle corriendo en Docker.

---

## Paso 1: Levantar el Entorno (UNA sola vez)

### Requisitos
- Docker Desktop instalado y corriendo
- Mínimo 6GB RAM asignados a Docker
- Puertos libres: 5432, 3306, 1521, 5050, 8081

### Comando
```bash
cd database-learning
docker compose up -d
```

### ¿Qué ocurre?
```
Segundo 0:   Docker lee docker-compose.yml
Segundo 2:   Crea red "db-network" (red virtual interna)
Segundo 3:   Descarga imágenes (primera vez: ~1.5GB)
Segundo 10:  Inicia PostgreSQL → crea BD "empresa_db"
             Ejecuta automáticamente:
               19-docker/init/postgres/01-schema.sql (16 tablas)
               19-docker/init/postgres/02-seed-data.sql (datos masivos)
Segundo 10:  Inicia MySQL → crea BD "empresa_db"
             Ejecuta: 19-docker/init/mysql/01-schema.sql
Segundo 15:  Inicia Oracle XE → crea usuario "empresa_user"
             Ejecuta: 19-docker/init/oracle/01-schema.sql
Segundo 20:  Inicia PgAdmin (GUI para PostgreSQL)
Segundo 20:  Inicia Adminer (GUI universal)
Segundo 60:  Oracle termina de inicializar (tarda más)
```

### Verificar que todo funciona
```bash
# Ver estado de contenedores
docker compose ps
# Todos deben decir "running" y "healthy"

# Probar PostgreSQL
docker exec -it learn-postgres psql -U postgres -d empresa_db -c "SELECT COUNT(*) FROM productos;"
# Resultado esperado: 500

# Probar MySQL
docker exec -it learn-mysql mysql -uroot -pmysql123 empresa_db -e "SELECT COUNT(*) FROM sucursales;"
# Resultado esperado: 5

# Acceder a PgAdmin (GUI)
# Abrir: http://localhost:5050
# Email: admin@admin.com | Password: admin123
# Agregar servidor: Host=postgres, Port=5432, User=postgres, Pass=postgres123

# Acceder a Adminer (GUI universal)
# Abrir: http://localhost:8081
# Sistema: PostgreSQL | Servidor: postgres | Usuario: postgres | Contraseña: postgres123 | BD: empresa_db
```

---

## Paso 2: Cómo Ejecutar los Ejercicios

### Opción A: Desde terminal (recomendado para aprender)
```bash
# Conectar a PostgreSQL interactivo
docker exec -it learn-postgres psql -U postgres -d empresa_db

# Dentro de psql puedes:
# - Escribir queries directamente
# - Ejecutar archivos: \i /ruta/archivo.sql
# - Ver tablas: \dt
# - Describir tabla: \d productos
# - Salir: \q
```

### Opción B: Ejecutar un archivo SQL completo
```bash
# Ejecutar un script desde tu máquina
docker exec -i learn-postgres psql -U postgres -d empresa_db < ejercicios/modulo-02/01-crear-tablas.sql
```

### Opción C: Desde PgAdmin o Adminer (GUI)
1. Abrir http://localhost:5050 (PgAdmin) o http://localhost:8081 (Adminer)
2. Conectar a la BD
3. Abrir el Query Tool
4. Copiar/pegar el SQL del ejercicio
5. Ejecutar (F5 o botón Play)

### Opción D: Desde DBeaver (IDE recomendado)
1. Descargar: https://dbeaver.io/download/
2. Nueva conexión → PostgreSQL → localhost:5432 → empresa_db → postgres/postgres123
3. Abrir SQL Editor → escribir queries → Ctrl+Enter para ejecutar

---

## Paso 3: Estructura de Datos Disponibles

Al levantar Docker, ya tienes estos datos para practicar:

| Tabla | Registros | Descripción |
|-------|-----------|-------------|
| sucursales | 5 | Tiendas físicas |
| categorias | 10 | Categorías de productos |
| proveedores | 10 | Proveedores |
| empleados | 100 | Personal (con jerarquía jefe→empleado) |
| clientes | 1,000 | Clientes (REGULAR, VIP, MAYORISTA) |
| roles | 5 | ADMIN, GERENTE, VENDEDOR, ALMACENISTA, AUDITOR |
| permisos | 10 | Permisos del sistema |
| usuarios | 5 | Usuarios del sistema |
| productos | 500 | Catálogo de productos |
| inventario | ~1,750 | Stock por producto por sucursal |
| ventas | 10,000 | Ventas históricas (2023-2025) |
| detalle_venta | ~24,000 | Líneas de detalle de ventas |
| compras | 0 | (Para que tú las crees en ejercicios) |
| detalle_compra | 0 | (Para que tú las crees) |
| auditoria | 0 | (Se llena con triggers) |

---

## Paso 4: Cómo Están Organizados los Ejercicios

```
database-learning/
├── ejercicios/
│   ├── modulo-02/          ← DDL: crear y modificar tablas
│   │   ├── 01-crear-tablas.sql
│   │   ├── 02-modificar-tablas.sql
│   │   └── 03-reto-diseño.sql
│   ├── modulo-03/          ← DML: insertar, actualizar, eliminar
│   ├── modulo-04/          ← Consultas SELECT
│   ├── modulo-05/          ← JOINs
│   ├── modulo-06/          ← Funciones
│   ├── modulo-07/          ← Subconsultas
│   ├── modulo-08/          ← Vistas
│   ├── modulo-09/          ← Índices y EXPLAIN
│   ├── modulo-10/          ← Triggers
│   ├── modulo-11/          ← Procedimientos
│   └── modulo-12/          ← Transacciones
└── soluciones/
    ├── modulo-02/
    ├── modulo-03/
    └── ...
```

---

## Paso 5: Flujo de Trabajo Recomendado

```
1. Leer el README.md del módulo (teoría + ejemplos)
2. Abrir el archivo de ejercicios del módulo
3. Intentar resolver CADA ejercicio (mínimo 15 min antes de ver solución)
4. Ejecutar tu solución contra la BD
5. Comparar con la solución oficial
6. Si no entiendes algo → releer la teoría → intentar de nuevo
7. Pasar al siguiente módulo solo cuando completes >80% de ejercicios
```

---

## Paso 6: Resetear la Base de Datos

Si arruinas los datos y quieres empezar de cero:

```bash
# Opción 1: Borrar volúmenes y recrear (BORRA TODO)
docker compose down -v
docker compose up -d
# Esperar 1-2 minutos a que se re-ejecuten los scripts de init

# Opción 2: Solo resetear PostgreSQL
docker exec -i learn-postgres psql -U postgres -c "DROP DATABASE empresa_db;"
docker exec -i learn-postgres psql -U postgres -c "CREATE DATABASE empresa_db;"
docker exec -i learn-postgres psql -U postgres -d empresa_db < 19-docker/init/postgres/01-schema.sql
docker exec -i learn-postgres psql -U postgres -d empresa_db < 19-docker/init/postgres/02-seed-data.sql
```

---

## Paso 7: Detener el Entorno

```bash
# Detener (mantiene datos para la próxima vez)
docker compose stop

# Reanudar
docker compose start

# Eliminar todo (incluyendo datos)
docker compose down -v
```

---

## Troubleshooting

| Problema | Causa | Solución |
|----------|-------|----------|
| "port already in use" | Otro servicio usa el puerto | Cambiar puerto en docker-compose.yml |
| "connection refused" | BD no ha terminado de arrancar | Esperar 30-60 segundos |
| Oracle no arranca | Poca RAM | Asignar 6GB+ a Docker Desktop |
| Datos no aparecen | Scripts de init no se ejecutaron | `docker compose down -v` y recrear |
| PgAdmin no conecta | Usar "postgres" como host (no localhost) | Host = nombre del contenedor |
