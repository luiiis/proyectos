# Cómo Ejecutar — Proyecto 3: API de Productos con MyBatis

## Requisitos
- Java 21 + Maven 3.9+
- MySQL 8 corriendo en localhost:3306 (o Docker)

## Paso 1: Crear la base de datos

### Opción A: MySQL instalado
```sql
mysql -u root -p
CREATE DATABASE productos_db;
exit;
```

### Opción B: MySQL con Docker
```cmd
docker run --name mysql-productos -e MYSQL_ROOT_PASSWORD=Root123! -e MYSQL_DATABASE=productos_db -p 3306:3306 -d mysql:8.0
```

## Paso 2: Ejecutar la aplicación

```cmd
cd nivel-03-base-datos/proyecto/backend
mvn spring-boot:run
```

**Flyway automáticamente:**
1. Crea las tablas (V1__crear_tablas.sql)
2. Inserta datos de prueba (V2__datos_iniciales.sql)

## Paso 3: Probar

### Endpoints de Productos

```bash
# Listar todos
curl http://localhost:8080/api/productos

# Buscar por ID
curl http://localhost:8080/api/productos/1

# Buscar por nombre
curl "http://localhost:8080/api/productos/buscar?nombre=laptop"

# Filtrar por categoría (1=Electrónica)
curl http://localhost:8080/api/productos/categoria/1

# Stock bajo
curl "http://localhost:8080/api/productos/stock-bajo?minimo=10"

# Paginación (página 0, 5 por página)
curl "http://localhost:8080/api/productos/paginado?page=0&size=5"

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Tablet Samsung","precio":8999,"existencia":12,"categoriaId":1,"sku":"TAB-SAM-001"}'

# Actualizar
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop HP ProBook (actualizada)","precio":19999,"existencia":20,"categoriaId":1,"sku":"LAP-HP-001"}'

# Eliminar (soft delete)
curl -X DELETE http://localhost:8080/api/productos/15
```

### Endpoints de Categorías

```bash
# Listar todas las categorías
curl http://localhost:8080/api/categorias

# Buscar categoría por ID
curl http://localhost:8080/api/categorias/1

# Buscar por nombre
curl "http://localhost:8080/api/categorias/buscar?nombre=electr"

# Crear categoría
curl -X POST http://localhost:8080/api/categorias \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Redes","descripcion":"Routers, switches y cables de red"}'

# Actualizar categoría
curl -X PUT http://localhost:8080/api/categorias/1 \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Electrónica y Gadgets","descripcion":"Dispositivos electrónicos modernos"}'

# Eliminar categoría (soft delete)
curl -X DELETE http://localhost:8080/api/categorias/5
```

## Estructura

```
src/main/
├── java/com/softwarelee/productos/
│   ├── Application.java
│   ├── model/
│   │   ├── Producto.java              ← Entidad
│   │   └── Categoria.java             ← Entidad
│   ├── mapper/
│   │   ├── ProductoMapper.java        ← Interface MyBatis
│   │   └── CategoriaMapper.java       ← Interface MyBatis
│   ├── service/
│   │   ├── ProductoService.java       ← Lógica de productos
│   │   └── CategoriaService.java      ← Lógica de categorías
│   ├── controller/
│   │   ├── ProductoController.java    ← Endpoints productos
│   │   └── CategoriaController.java   ← Endpoints categorías
│   └── exception/
│       └── GlobalExceptionHandler.java ← Manejo de errores
└── resources/
    ├── application.yml                 ← Config BD + MyBatis
    ├── db/migration/
    │   ├── V1__crear_tablas.sql        ← Flyway: schema
    │   └── V2__datos_iniciales.sql     ← Flyway: seed
    └── mapper/
        ├── ProductoMapper.xml          ← Queries SQL productos
        └── CategoriaMapper.xml         ← Queries SQL categorías
```

## Conceptos nuevos en este nivel

| Concepto | Qué es |
|----------|--------|
| MyBatis | Framework que mapea SQL ↔ Java (tú escribes el SQL) |
| Mapper (interface) | Define métodos que ejecutan queries |
| Mapper (XML) | Contiene el SQL real de cada método |
| Flyway | Ejecuta migraciones SQL automáticamente al arrancar |
| JOIN | Combinar datos de 2 tablas en una query |
| Paginación | Devolver datos en páginas (LIMIT + OFFSET) |
| Soft delete | Marcar como inactivo en vez de borrar |
