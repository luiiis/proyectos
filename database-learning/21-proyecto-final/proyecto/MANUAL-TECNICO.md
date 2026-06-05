# Módulo 21: Proyecto Final - Manual Técnico

## ¿Qué es este proyecto?
Un sistema empresarial COMPLETO que integra todo lo aprendido en los 20 módulos anteriores:
- Base de datos PostgreSQL con 16 tablas (módulos 1-9)
- Triggers de auditoría automática (módulo 10)
- Stored procedures para operaciones complejas (módulo 11)
- Transacciones para consistencia (módulo 12)
- Seguridad con roles y permisos (módulo 13)
- Monitoreo y mantenimiento (módulo 14)
- Índices optimizados (módulo 15)
- Features avanzadas de PostgreSQL (módulo 16)
- API REST con Spring Boot (módulo 20)
- Docker para despliegue (módulo 19)

## Arquitectura

```
┌─────────────────────────────────────────────────────────┐
│                    DOCKER COMPOSE                         │
│                                                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐  │
│  │  Spring Boot │  │  PostgreSQL  │  │    Redis     │  │
│  │  (API REST)  │  │  (16 tablas) │  │   (caché)   │  │
│  │  :8080       │  │  :5432       │  │   :6379     │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────────┘  │
│         │                  │                             │
│         └──────────────────┘                             │
│                                                          │
│  Endpoints:                                              │
│  POST /api/auth/login     → Autenticación JWT            │
│  GET  /api/productos      → Listar con paginación        │
│  POST /api/ventas         → Registrar venta completa     │
│  GET  /api/reportes/...   → Reportes y dashboard         │
│  GET  /api/inventario/... → Stock y alertas              │
└─────────────────────────────────────────────────────────┘
```

## Cómo levantar el proyecto completo

```bash
cd database-learning/21-proyecto-final/proyecto

# 1. Levantar infraestructura
docker compose up -d

# 2. Esperar a que PostgreSQL esté listo (~10 segundos)
docker compose logs -f postgres

# 3. Verificar que la BD tiene datos
docker exec -it proyecto-final-postgres psql -U postgres -d empresa_db \
  -c "SELECT COUNT(*) FROM productos;"

# 4. Levantar la API (si no está en Docker)
cd app
mvn spring-boot:run

# 5. Probar
curl http://localhost:8080/api/productos?page=0&size=5
```

## Flujo de datos: Registrar una Venta

```
1. Frontend envía: POST /api/ventas
   Body: {cliente_id: 1, empleado_id: 7, productos: [{id: 1, cantidad: 2}, {id: 5, cantidad: 1}]}

2. VentaController recibe la petición

3. VentaService.registrar():
   a. Genera número de venta: "V-20260530-10001"
   b. Inserta encabezado en tabla "ventas"
   c. Para cada producto:
      - Obtiene precio actual de tabla "productos"
      - Inserta línea en "detalle_venta"
      - Descuenta inventario en tabla "inventario"
      - Si stock < mínimo → trigger inserta alerta
   d. Calcula totales (subtotal + IVA)
   e. Actualiza tabla "ventas" con totales
   f. COMMIT (todo o nada)

4. Trigger trg_auditoria_ventas:
   - Inserta registro en tabla "auditoria" automáticamente

5. Respuesta: HTTP 201
   {id: 10001, numero: "V-20260530-10001", total: 41158.78}
```

## Qué aprendiste construyendo esto

| Módulo | Qué se aplica aquí |
|--------|-------------------|
| 01-02 | Diseño de las 16 tablas con relaciones |
| 03 | INSERT/UPDATE en ventas, inventario |
| 04-05 | Queries de reportes con JOINs múltiples |
| 06 | Window functions en reportes de ranking |
| 07 | Subconsultas para stock bajo, clientes sin compras |
| 08 | Vistas para dashboard y reportes |
| 09 | Índices para que las queries sean rápidas |
| 10 | Triggers de auditoría automática |
| 11 | Procedures para registrar ventas complejas |
| 12 | Transacciones para consistencia |
| 13 | Roles y permisos en la API |
| 14 | Scripts de backup y monitoreo |
| 15 | Queries optimizadas con EXPLAIN |
| 16 | JSONB para auditoría, FTS para búsqueda |
| 19 | Docker para desplegar todo |
| 20 | Spring Boot como API REST |

## Criterios de completitud
- [ ] Las 16 tablas existen con datos
- [ ] Triggers de auditoría funcionan
- [ ] API REST con CRUD de productos
- [ ] Registrar venta descuenta inventario
- [ ] Reportes de ventas por período
- [ ] Docker Compose levanta todo
- [ ] Swagger documenta los endpoints
