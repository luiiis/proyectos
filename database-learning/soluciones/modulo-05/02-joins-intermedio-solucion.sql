-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 05: JOINs Nivel Intermedio (16-35)
-- ════════════════════════════════════════════════════════════════

-- 16. Top 10 productos más vendidos (por cantidad)
SELECT p.nombre, SUM(dv.cantidad) AS total_vendido, COUNT(DISTINCT dv.venta_id) AS en_ventas
FROM productos p
JOIN detalle_venta dv ON dv.producto_id = p.id
GROUP BY p.id, p.nombre
ORDER BY total_vendido DESC
LIMIT 10;

-- 17. Top 10 clientes por monto total de compras
SELECT cl.nombre || ' ' || cl.apellido AS cliente, cl.tipo,
       COUNT(v.id) AS compras, SUM(v.total) AS monto_total
FROM clientes cl
JOIN ventas v ON v.cliente_id = cl.id
WHERE v.estado = 'COMPLETADA'
GROUP BY cl.id, cl.nombre, cl.apellido, cl.tipo
ORDER BY monto_total DESC
LIMIT 10;

-- 18. Ventas por categoría de producto
SELECT c.nombre AS categoria,
       COUNT(DISTINCT dv.venta_id) AS ventas,
       SUM(dv.cantidad) AS unidades,
       SUM(dv.subtotal) AS monto
FROM categorias c
JOIN productos p ON p.categoria_id = c.id
JOIN detalle_venta dv ON dv.producto_id = p.id
JOIN ventas v ON dv.venta_id = v.id
WHERE v.estado = 'COMPLETADA'
GROUP BY c.nombre
ORDER BY monto DESC;

-- 19. Empleados que venden más que el promedio
WITH ventas_por_empleado AS (
    SELECT empleado_id, SUM(total) AS total_vendido
    FROM ventas WHERE estado = 'COMPLETADA'
    GROUP BY empleado_id
)
SELECT e.nombre, e.apellido, e.puesto, vpe.total_vendido
FROM empleados e
JOIN ventas_por_empleado vpe ON vpe.empleado_id = e.id
WHERE vpe.total_vendido > (SELECT AVG(total_vendido) FROM ventas_por_empleado)
ORDER BY vpe.total_vendido DESC;

-- 20. Productos que están en inventario en TODAS las sucursales
SELECT p.nombre, COUNT(DISTINCT i.sucursal_id) AS en_sucursales
FROM productos p
JOIN inventario i ON i.producto_id = p.id
WHERE i.cantidad > 0
GROUP BY p.id, p.nombre
HAVING COUNT(DISTINCT i.sucursal_id) = (SELECT COUNT(*) FROM sucursales WHERE activa = true);

-- 21. Sucursales donde NO hay stock de un producto específico (ej: producto_id = 1)
SELECT s.nombre AS sucursal
FROM sucursales s
WHERE s.activa = true
AND s.id NOT IN (
    SELECT i.sucursal_id FROM inventario i WHERE i.producto_id = 1 AND i.cantidad > 0
);

-- 22. Clientes que compraron en más de una sucursal
SELECT cl.nombre, cl.apellido, COUNT(DISTINCT v.sucursal_id) AS sucursales_distintas
FROM clientes cl
JOIN ventas v ON v.cliente_id = cl.id
GROUP BY cl.id, cl.nombre, cl.apellido
HAVING COUNT(DISTINCT v.sucursal_id) > 1
ORDER BY sucursales_distintas DESC;

-- 23. Productos que se venden por encima de su costo (margen positivo)
SELECT p.nombre, p.precio, p.costo,
       ROUND(p.precio - p.costo, 2) AS margen,
       ROUND((p.precio - p.costo) / p.costo * 100, 1) AS margen_pct
FROM productos p
WHERE p.costo > 0 AND p.precio > p.costo
ORDER BY margen_pct DESC;

-- 24. Empleados con su cadena jerárquica (empleado → jefe → director)
SELECT
    e.nombre || ' ' || e.apellido AS empleado,
    e.puesto,
    COALESCE(j.nombre || ' ' || j.apellido, '-') AS jefe,
    COALESCE(j.puesto, '-') AS puesto_jefe,
    COALESCE(d.nombre || ' ' || d.apellido, '-') AS director,
    COALESCE(d.puesto, '-') AS puesto_director
FROM empleados e
LEFT JOIN empleados j ON e.jefe_id = j.id
LEFT JOIN empleados d ON j.jefe_id = d.id
ORDER BY d.nombre NULLS FIRST, j.nombre NULLS FIRST, e.nombre;

-- 25. Ventas donde el descuento fue mayor al 10%
SELECT v.numero_venta, v.fecha, v.subtotal, v.descuento,
       ROUND(v.descuento / NULLIF(v.subtotal, 0) * 100, 1) AS pct_descuento,
       cl.nombre || ' ' || cl.apellido AS cliente
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
WHERE v.descuento > 0 AND v.descuento / NULLIF(v.subtotal, 0) > 0.10
ORDER BY pct_descuento DESC;

-- 26. Proveedores que NO han suministrado productos vendidos este año
SELECT pr.nombre AS proveedor, pr.contacto
FROM proveedores pr
WHERE pr.id NOT IN (
    SELECT DISTINCT p.proveedor_id
    FROM productos p
    JOIN detalle_venta dv ON dv.producto_id = p.id
    JOIN ventas v ON dv.venta_id = v.id
    WHERE EXTRACT(YEAR FROM v.fecha) = EXTRACT(YEAR FROM CURRENT_DATE)
    AND p.proveedor_id IS NOT NULL
);

-- 27. Comparar ventas por sucursal: este mes vs mes anterior
WITH ventas_mes_actual AS (
    SELECT sucursal_id, SUM(total) AS total_actual
    FROM ventas
    WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE)
    GROUP BY sucursal_id
),
ventas_mes_anterior AS (
    SELECT sucursal_id, SUM(total) AS total_anterior
    FROM ventas
    WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
      AND fecha < DATE_TRUNC('month', CURRENT_DATE)
    GROUP BY sucursal_id
)
SELECT s.nombre,
       COALESCE(va.total_actual, 0) AS mes_actual,
       COALESCE(vp.total_anterior, 0) AS mes_anterior,
       ROUND(COALESCE(va.total_actual, 0) - COALESCE(vp.total_anterior, 0), 2) AS diferencia
FROM sucursales s
LEFT JOIN ventas_mes_actual va ON va.sucursal_id = s.id
LEFT JOIN ventas_mes_anterior vp ON vp.sucursal_id = s.id
ORDER BY diferencia DESC;

-- 28. Productos con stock menor al mínimo en alguna sucursal
SELECT p.nombre, s.nombre AS sucursal, i.cantidad AS stock_actual, i.cantidad_minima
FROM inventario i
JOIN productos p ON i.producto_id = p.id
JOIN sucursales s ON i.sucursal_id = s.id
WHERE i.cantidad < i.cantidad_minima
ORDER BY (i.cantidad_minima - i.cantidad) DESC;

-- 29. Clientes VIP que no han comprado en los últimos 3 meses
SELECT cl.nombre, cl.apellido, cl.email, MAX(v.fecha) AS ultima_compra
FROM clientes cl
LEFT JOIN ventas v ON v.cliente_id = cl.id
WHERE cl.tipo = 'VIP' AND cl.activo = true
GROUP BY cl.id, cl.nombre, cl.apellido, cl.email
HAVING MAX(v.fecha) IS NULL OR MAX(v.fecha) < CURRENT_DATE - INTERVAL '3 months';

-- 30. Ranking de vendedores por sucursal
SELECT s.nombre AS sucursal,
       e.nombre || ' ' || e.apellido AS vendedor,
       SUM(v.total) AS total_vendido,
       RANK() OVER (PARTITION BY s.id ORDER BY SUM(v.total) DESC) AS ranking
FROM ventas v
JOIN empleados e ON v.empleado_id = e.id
JOIN sucursales s ON v.sucursal_id = s.id
WHERE v.estado = 'COMPLETADA'
GROUP BY s.id, s.nombre, e.id, e.nombre, e.apellido
ORDER BY s.nombre, ranking;

-- 31. Top 3 productos más vendidos por categoría
WITH ranking_por_categoria AS (
    SELECT c.nombre AS categoria, p.nombre AS producto,
           SUM(dv.cantidad) AS vendidos,
           ROW_NUMBER() OVER (PARTITION BY c.id ORDER BY SUM(dv.cantidad) DESC) AS rn
    FROM categorias c
    JOIN productos p ON p.categoria_id = c.id
    JOIN detalle_venta dv ON dv.producto_id = p.id
    GROUP BY c.id, c.nombre, p.id, p.nombre
)
SELECT categoria, producto, vendidos
FROM ranking_por_categoria
WHERE rn <= 3
ORDER BY categoria, rn;

-- 32. Meses con mayor y menor facturación
(SELECT 'MAYOR' AS tipo, TO_CHAR(fecha, 'YYYY-MM') AS mes, SUM(total) AS facturacion
 FROM ventas WHERE estado = 'COMPLETADA'
 GROUP BY TO_CHAR(fecha, 'YYYY-MM')
 ORDER BY facturacion DESC LIMIT 1)
UNION ALL
(SELECT 'MENOR', TO_CHAR(fecha, 'YYYY-MM'), SUM(total)
 FROM ventas WHERE estado = 'COMPLETADA'
 GROUP BY TO_CHAR(fecha, 'YYYY-MM')
 ORDER BY facturacion ASC LIMIT 1);

-- 33. Clientes que compraron el mismo producto más de una vez
SELECT cl.nombre, cl.apellido, p.nombre AS producto, SUM(dv.cantidad) AS veces_comprado
FROM clientes cl
JOIN ventas v ON v.cliente_id = cl.id
JOIN detalle_venta dv ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
GROUP BY cl.id, cl.nombre, cl.apellido, p.id, p.nombre
HAVING COUNT(dv.id) > 1
ORDER BY veces_comprado DESC;

-- 34. Empleados que trabajan en la misma sucursal que su jefe
SELECT e.nombre || ' ' || e.apellido AS empleado,
       j.nombre || ' ' || j.apellido AS jefe,
       s.nombre AS sucursal_compartida
FROM empleados e
JOIN empleados j ON e.jefe_id = j.id
JOIN sucursales s ON e.sucursal_id = s.id
WHERE e.sucursal_id = j.sucursal_id;

-- 35. Ventas con más de 5 productos diferentes
SELECT v.numero_venta, v.fecha, v.total, COUNT(dv.id) AS items_diferentes
FROM ventas v
JOIN detalle_venta dv ON dv.venta_id = v.id
GROUP BY v.id, v.numero_venta, v.fecha, v.total
HAVING COUNT(dv.id) > 5
ORDER BY items_diferentes DESC;
