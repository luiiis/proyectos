# Cómo se Construyó - Proyecto 05: BD Ventas

## Paso 1: Diseñar el modelo de datos
```
¿Qué necesita un sistema de ventas?
- Quién vende (empleados, sucursales)
- Qué vende (productos, categorías, proveedores)
- A quién vende (clientes)
- Registro de ventas (encabezado + detalle)
- Auditoría (quién hizo qué)
```

## Paso 2: Crear las tablas (en orden de dependencias)
```
1. categorias, proveedores, sucursales (sin FK, se crean primero)
2. empleados (FK → sucursales)
3. clientes (independiente)
4. productos (FK → categorias, proveedores)
5. inventario (FK → productos, sucursales)
6. ventas (FK → clientes, empleados, sucursales)
7. detalle_venta (FK → ventas, productos)
8. auditoria (independiente, recibe datos de triggers)
```

## Paso 3: Ejecutar
```bash
cd academia-profesional/proyecto-05-bd-ventas

# Levantar PostgreSQL + Adminer
docker compose up -d

# Conectar y explorar
docker exec -it ventas-postgres psql -U postgres -d ventas_db

# Dentro de psql:
\dt                    -- ver tablas
\d productos           -- ver estructura
SELECT * FROM productos;
SELECT * FROM ventas JOIN detalle_venta ON detalle_venta.venta_id = ventas.id;

# O usar Adminer (GUI web):
# http://localhost:8081
# Sistema: PostgreSQL | Servidor: postgres | User: postgres | Pass: postgres123 | BD: ventas_db
```

## Paso 4: Probar queries
```sql
-- Top productos por ventas
SELECT p.nombre, SUM(dv.cantidad) AS vendidos
FROM productos p JOIN detalle_venta dv ON dv.producto_id = p.id
GROUP BY p.nombre ORDER BY vendidos DESC;

-- Ventas por empleado
SELECT e.nombre, COUNT(v.id) AS ventas, SUM(v.total) AS monto
FROM empleados e JOIN ventas v ON v.empleado_id = e.id
GROUP BY e.nombre ORDER BY monto DESC;
```
