# Módulo 08: Vistas (Views)

## ¿Qué es una Vista?
Una vista es una "tabla virtual" basada en una consulta SQL. No almacena datos, ejecuta la query cada vez que se consulta.

**Analogía**: Es como guardar un filtro de Excel. Los datos siguen en la tabla original, pero tú ves solo lo que te interesa.

---

## 1. CREATE VIEW

```sql
-- Vista: resumen de ventas por empleado
CREATE VIEW v_ventas_empleado AS
SELECT 
    e.id AS empleado_id,
    e.nombre || ' ' || e.apellido AS vendedor,
    e.puesto,
    s.nombre AS sucursal,
    COUNT(v.id) AS total_ventas,
    COALESCE(SUM(v.total), 0) AS monto_total,
    COALESCE(AVG(v.total), 0) AS ticket_promedio
FROM empleados e
LEFT JOIN ventas v ON v.empleado_id = e.id AND v.estado = 'COMPLETADA'
LEFT JOIN sucursales s ON e.sucursal_id = s.id
GROUP BY e.id, e.nombre, e.apellido, e.puesto, s.nombre;

-- Usar la vista como si fuera una tabla
SELECT * FROM v_ventas_empleado WHERE total_ventas > 100 ORDER BY monto_total DESC;

-- Vista: productos con stock bajo
CREATE VIEW v_stock_bajo AS
SELECT p.nombre, p.sku, i.cantidad, i.cantidad_minima, s.nombre AS sucursal
FROM inventario i
JOIN productos p ON i.producto_id = p.id
JOIN sucursales s ON i.sucursal_id = s.id
WHERE i.cantidad < i.cantidad_minima;

-- Vista: dashboard de KPIs
CREATE VIEW v_dashboard AS
SELECT
    (SELECT COUNT(*) FROM ventas WHERE DATE_TRUNC('month', fecha) = DATE_TRUNC('month', NOW())) AS ventas_mes,
    (SELECT SUM(total) FROM ventas WHERE DATE_TRUNC('month', fecha) = DATE_TRUNC('month', NOW())) AS facturacion_mes,
    (SELECT COUNT(*) FROM clientes WHERE created_at >= DATE_TRUNC('month', NOW())) AS clientes_nuevos,
    (SELECT COUNT(*) FROM inventario WHERE cantidad < cantidad_minima) AS alertas_stock;
```

## 2. ¿Para qué sirven las Vistas?

| Uso | Ejemplo |
|-----|---------|
| Simplificar queries complejas | JOINs de 5 tablas → 1 vista simple |
| Seguridad | Mostrar solo ciertas columnas a ciertos usuarios |
| Abstracción | Si cambias la estructura, la vista sigue igual |
| Reportes | Vistas pre-calculadas para dashboards |

## 3. Seguridad con Vistas

```sql
-- El vendedor solo ve SUS ventas (no las de otros)
CREATE VIEW v_mis_ventas AS
SELECT v.numero_venta, v.fecha, v.total, cl.nombre AS cliente
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
WHERE v.empleado_id = current_setting('app.current_user_id')::integer;

-- Ocultar salarios: vista sin columna salario
CREATE VIEW v_directorio_empleados AS
SELECT id, nombre, apellido, email, puesto, sucursal_id
FROM empleados WHERE activo = TRUE;
-- GRANT SELECT ON v_directorio_empleados TO rol_vendedor;
-- (El vendedor ve el directorio pero NO los salarios)
```

## 4. Materialized Views (PostgreSQL)

```sql
-- Vista materializada: almacena el resultado (más rápida, pero datos pueden estar desactualizados)
CREATE MATERIALIZED VIEW mv_ventas_mensuales AS
SELECT DATE_TRUNC('month', fecha) AS mes, SUM(total) AS total, COUNT(*) AS cantidad
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY DATE_TRUNC('month', fecha);

-- Refrescar datos (ejecutar periódicamente)
REFRESH MATERIALIZED VIEW mv_ventas_mensuales;

-- Refrescar sin bloquear lecturas
REFRESH MATERIALIZED VIEW CONCURRENTLY mv_ventas_mensuales;
```

## 5. Ejercicios

1. Crea una vista `v_catalogo_productos` con: nombre, categoría, proveedor, precio, stock total
2. Crea una vista `v_clientes_vip` con clientes tipo VIP y su total de compras
3. Crea una vista materializada con el top 20 productos más vendidos
4. ¿Cuándo usarías una vista materializada vs una vista normal?
5. Diseña un sistema de vistas para un rol "Auditor" que solo puede ver, no modificar

---

## Siguiente Módulo
→ [09-Índices](../09-indices/README.md)
