# Cómo Ejecutar - Proyecto 07: API Productos

## Opción 1: Todo con Docker (más fácil)
```bash
cd academia-profesional/proyecto-07-api-productos
docker compose up --build -d

# Verificar
curl http://localhost:8080/api/productos

# Swagger UI
# Abrir: http://localhost:8080/swagger-ui.html
```

## Opción 2: Desarrollo local
```bash
# 1. Levantar solo PostgreSQL
docker compose up postgres -d

# 2. Crear la BD (si no existe)
docker exec -it productos-postgres psql -U postgres -c "CREATE DATABASE productos_db;" 2>/dev/null

# 3. Ejecutar la app
mvn spring-boot:run

# API: http://localhost:8080/api/productos
# Swagger: http://localhost:8080/swagger-ui.html
```

## Probar con curl
```bash
# Listar (paginado)
curl -s http://localhost:8080/api/productos?size=5 | python -m json.tool

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop Test","precio":18999.99,"stock":10,"sku":"TEST-001","categoria":"Electrónica"}'

# Buscar
curl http://localhost:8080/api/productos/buscar?q=laptop

# Eliminar
curl -X DELETE http://localhost:8080/api/productos/1
```

## Detener
```bash
docker compose down      # Mantiene datos
docker compose down -v   # Borra datos (reset)
```
