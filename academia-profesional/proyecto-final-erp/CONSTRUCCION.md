# Cómo se Construyó - Proyecto Final: ERP Empresarial

## Paso 1: Diseñar la base de datos
```sql
-- 11 tablas en sql/01-schema.sql:
roles → define permisos (ADMIN, GERENTE, VENDEDOR)
usuarios → login + JWT (password BCrypt)
sucursales → tiendas físicas
categorias → clasificación de productos
proveedores → quién suministra
clientes → a quién vendemos
productos → qué vendemos (precio, stock, sku)
inventario → stock por sucursal
ventas → encabezado (fecha, cliente, total)
detalle_venta → líneas (producto, cantidad, precio)
auditoria → log de cambios (JSONB)
```

## Paso 2: Construir el backend (Spring Boot)
```
1. Entities (mapean tablas): Usuario, Producto, Venta, DetalleVenta
2. Repositories (queries): findByActivoTrue, findStockBajo, reporteMensual
3. Services (lógica): ProductoService (@Cacheable), VentaService (transaccional)
4. Controllers (endpoints): AuthController, ProductoController, VentaController
5. Security (JWT): JwtService, JwtFilter, SecurityConfig
```

## Paso 3: Construir el frontend (Angular)
```
1. AuthService (signals): login, logout, getToken
2. Interceptor: agrega JWT a cada petición
3. Login page: formulario → POST /api/auth/login
4. Dashboard: KPIs estáticos (en producción vendrían de /api/reportes)
5. Productos: tabla con búsqueda → GET /api/productos
6. Ventas: historial → GET /api/ventas
```

## Paso 4: Dockerizar
```
docker-compose.yml orquesta 7 servicios:
- postgres (BD + scripts de init)
- redis (caché)
- kafka (eventos)
- backend (Spring Boot)
- frontend (Angular + Nginx)
- prometheus (métricas)
- grafana (dashboards)
```

## Paso 5: Ejecutar y probar
```bash
docker compose up --build -d

# Probar auth:
curl -X POST localhost:8080/api/auth/register -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","email":"admin@erp.com","nombre":"Admin"}'

TOKEN=$(curl -s -X POST localhost:8080/api/auth/login -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# Probar productos:
curl localhost:8080/api/productos -H "Authorization: Bearer $TOKEN"

# Crear producto:
curl -X POST localhost:8080/api/productos -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Test","precio":999,"stock":10,"sku":"TEST-001"}'

# Registrar venta:
curl -X POST localhost:8080/api/ventas -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"clienteId":1,"usuarioId":1,"productos":[{"productoId":1,"cantidad":2}]}'

# Frontend: http://localhost
# Grafana: http://localhost:3000
```
