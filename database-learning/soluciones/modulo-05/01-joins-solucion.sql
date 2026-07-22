-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 05: JOINs (Ejercicios 1-15 nivel básico)
-- ════════════════════════════════════════════════════════════════

-- 1. Empleados con el nombre de su sucursal
SELECT e.nombre, e.apellido, e.puesto, s.nombre AS sucursal
FROM empleados e
INNER JOIN sucursales s ON e.sucursal_id = s.id;

-- 2. Productos con el nombre de su categoría
SELECT p.nombre, p.precio, c.nombre AS categoria
FROM productos p
JOIN categorias c ON p.categoria_id = c.id
ORDER BY c.nombre, p.nombre;

-- 3. Ventas con nombre del cliente
SELECT v.numero_venta, v.fecha, v.total,
       cl.nombre || ' ' || cl.apellido AS cliente
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
ORDER BY v.fecha DESC;

-- 4. Productos con nombre del proveedor y categoría
SELECT p.nombre AS producto, p.precio,
       c.nombre AS categoria,
       pr.nombre AS proveedor
FROM productos p
JOIN categorias c ON p.categoria_id = c.id
JOIN proveedores pr ON p.proveedor_id = pr.id;

-- 5. Detalle de una venta específica con nombres de productos
SELECT v.numero_venta, p.nombre AS producto, dv.cantidad,
       dv.precio_unitario, dv.subtotal
FROM detalle_venta dv
JOIN ventas v ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
WHERE v.numero_venta = (SELECT numero_venta FROM ventas LIMIT 1);

-- 6. ¿Cuántos empleados tiene cada sucursal?
SELECT s.nombre AS sucursal, COUNT(e.id) AS empleados
FROM sucursales s
LEFT JOIN empleados e ON e.sucursal_id = s.id
GROUP BY s.nombre
ORDER BY empleados DESC;

-- 7. ¿Cuántos productos tiene cada categoría?
SELECT c.nombre AS categoria, COUNT(p.id) AS productos
FROM categorias c
LEFT JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre
ORDER BY productos DESC;

-- 8. Clientes que han comprado (al menos 1 venta)
SELECT DISTINCT cl.nombre, cl.apellido, cl.tipo
FROM clientes cl
INNER JOIN ventas v ON v.cliente_id = cl.id;

-- 9. Empleados con su jefe directo
SELECT
    e.nombre || ' ' || e.apellido AS empleado,
    e.puesto,
    COALESCE(j.nombre || ' ' || j.apellido, '(Director General)') AS jefe
FROM empleados e
LEFT JOIN empleados j ON e.jefe_id = j.id
ORDER BY j.nombre NULLS FIRST;

-- 10. Total vendido por cada empleado
SELECT e.nombre || ' ' || e.apellido AS vendedor,
       COUNT(v.id) AS num_ventas,
       COALESCE(SUM(v.total), 0) AS total_vendido
FROM empleados e
LEFT JOIN ventas v ON v.empleado_id = e.id
GROUP BY e.id, e.nombre, e.apellido
ORDER BY total_vendido DESC;

-- 11. Productos vendidos en una sucursal específica
SELECT p.nombre, SUM(dv.cantidad) AS total_vendido
FROM detalle_venta dv
JOIN ventas v ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
JOIN sucursales s ON v.sucursal_id = s.id
WHERE s.nombre LIKE '%Central%'
GROUP BY p.nombre
ORDER BY total_vendido DESC;

-- 12. Clientes que compraron productos de categoría 'Electrónica'
SELECT DISTINCT cl.nombre, cl.apellido
FROM clientes cl
JOIN ventas v ON v.cliente_id = cl.id
JOIN detalle_venta dv ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
JOIN categorias c ON p.categoria_id = c.id
WHERE c.nombre = 'Electrónica';

-- 13. Proveedores cuyos productos se han vendido
SELECT DISTINCT pr.nombre AS proveedor, pr.contacto
FROM proveedores pr
JOIN productos p ON p.proveedor_id = pr.id
JOIN detalle_venta dv ON dv.producto_id = p.id;

-- 14. Ventas del mes actual con detalle completo
SELECT v.numero_venta, v.fecha,
       cl.nombre || ' ' || cl.apellido AS cliente,
       e.nombre || ' ' || e.apellido AS vendedor,
       v.total
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
JOIN empleados e ON v.empleado_id = e.id
WHERE EXTRACT(MONTH FROM v.fecha) = EXTRACT(MONTH FROM CURRENT_DATE)
  AND EXTRACT(YEAR FROM v.fecha) = EXTRACT(YEAR FROM CURRENT_DATE)
ORDER BY v.fecha DESC;

-- 15. Empleados que NO han realizado ninguna venta
SELECT e.nombre, e.apellido, e.puesto
FROM empleados e
LEFT JOIN ventas v ON v.empleado_id = e.id
WHERE v.id IS NULL;
