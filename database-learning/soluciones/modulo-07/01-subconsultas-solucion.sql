-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 07: Subconsultas
-- ════════════════════════════════════════════════════════════════

-- ═══ SUBCONSULTAS EN WHERE ═══

-- 1. Productos más caros que el promedio
SELECT nombre, precio FROM productos
WHERE precio > (SELECT AVG(precio) FROM productos)
ORDER BY precio DESC;

-- 2. Empleados de la sucursal con más ventas
SELECT nombre, apellido, puesto FROM empleados
WHERE sucursal_id = (
    SELECT sucursal_id FROM ventas
    GROUP BY sucursal_id ORDER BY SUM(total) DESC LIMIT 1
);

-- 3. Clientes que compraron productos de más de $10,000
SELECT DISTINCT cl.nombre, cl.apellido FROM clientes cl
WHERE cl.id IN (
    SELECT v.cliente_id FROM ventas v
    JOIN detalle_venta dv ON dv.venta_id = v.id
    WHERE dv.precio_unitario > 10000
);

-- 4. Productos que nunca se han vendido (NOT IN)
SELECT nombre, precio FROM productos
WHERE id NOT IN (SELECT DISTINCT producto_id FROM detalle_venta);

-- 5. Empleados con salario mayor al promedio de SU sucursal (correlacionada)
SELECT e.nombre, e.apellido, e.salario, s.nombre AS sucursal
FROM empleados e
JOIN sucursales s ON e.sucursal_id = s.id
WHERE e.salario > (
    SELECT AVG(e2.salario) FROM empleados e2 WHERE e2.sucursal_id = e.sucursal_id
)
ORDER BY e.salario DESC;

-- ═══ SUBCONSULTAS CON EXISTS ═══

-- 6. Clientes que SÍ han comprado (EXISTS)
SELECT cl.nombre, cl.apellido FROM clientes cl
WHERE EXISTS (SELECT 1 FROM ventas v WHERE v.cliente_id = cl.id);

-- 7. Categorías sin productos
SELECT c.nombre FROM categorias c
WHERE NOT EXISTS (SELECT 1 FROM productos p WHERE p.categoria_id = c.id);

-- 8. Proveedores con al menos un producto vendido este año
SELECT pr.nombre FROM proveedores pr
WHERE EXISTS (
    SELECT 1 FROM productos p
    JOIN detalle_venta dv ON dv.producto_id = p.id
    JOIN ventas v ON dv.venta_id = v.id
    WHERE p.proveedor_id = pr.id
    AND EXTRACT(YEAR FROM v.fecha) = EXTRACT(YEAR FROM CURRENT_DATE)
);

-- ═══ SUBCONSULTAS EN SELECT (escalares) ═══

-- 9. Cada empleado con su cantidad de ventas y total vendido
SELECT e.nombre, e.apellido, e.puesto,
    (SELECT COUNT(*) FROM ventas v WHERE v.empleado_id = e.id) AS num_ventas,
    (SELECT COALESCE(SUM(total), 0) FROM ventas v WHERE v.empleado_id = e.id AND v.estado = 'COMPLETADA') AS total_vendido
FROM empleados e
ORDER BY total_vendido DESC;

-- 10. Productos con su ranking de ventas
SELECT p.nombre, p.precio,
    (SELECT SUM(dv.cantidad) FROM detalle_venta dv WHERE dv.producto_id = p.id) AS vendidos,
    (SELECT COUNT(DISTINCT dv2.producto_id) FROM detalle_venta dv2
     WHERE (SELECT SUM(cantidad) FROM detalle_venta WHERE producto_id = dv2.producto_id)
           > COALESCE((SELECT SUM(cantidad) FROM detalle_venta WHERE producto_id = p.id), 0)
    ) + 1 AS ranking
FROM productos p
ORDER BY vendidos DESC NULLS LAST LIMIT 10;

-- ═══ SUBCONSULTAS EN FROM (tabla derivada) ═══

-- 11. Top vendedores con sus métricas
SELECT ranked.* FROM (
    SELECT e.nombre || ' ' || e.apellido AS vendedor,
           COUNT(v.id) AS ventas,
           SUM(v.total) AS total,
           ROUND(AVG(v.total), 2) AS ticket_promedio,
           ROW_NUMBER() OVER (ORDER BY SUM(v.total) DESC) AS posicion
    FROM empleados e
    JOIN ventas v ON v.empleado_id = e.id
    WHERE v.estado = 'COMPLETADA'
    GROUP BY e.id, e.nombre, e.apellido
) ranked
WHERE posicion <= 5;

-- 12. Clientes con compras arriba del percentil 90
SELECT * FROM (
    SELECT cl.nombre, cl.apellido, SUM(v.total) AS total_gastado,
           PERCENT_RANK() OVER (ORDER BY SUM(v.total)) AS percentil
    FROM clientes cl JOIN ventas v ON v.cliente_id = cl.id
    WHERE v.estado = 'COMPLETADA'
    GROUP BY cl.id, cl.nombre, cl.apellido
) stats
WHERE percentil >= 0.90;

-- ═══ CTEs (Common Table Expressions) ═══

-- 13. Análisis de ventas con CTE
WITH ventas_mensuales AS (
    SELECT DATE_TRUNC('month', fecha) AS mes,
           SUM(total) AS total_mes, COUNT(*) AS num_ventas
    FROM ventas WHERE estado = 'COMPLETADA'
    GROUP BY DATE_TRUNC('month', fecha)
),
con_variacion AS (
    SELECT mes, total_mes, num_ventas,
           LAG(total_mes) OVER (ORDER BY mes) AS mes_anterior,
           ROUND((total_mes - LAG(total_mes) OVER (ORDER BY mes)) / LAG(total_mes) OVER (ORDER BY mes) * 100, 1) AS variacion_pct
    FROM ventas_mensuales
)
SELECT mes::DATE, total_mes, num_ventas, mes_anterior, variacion_pct,
       CASE WHEN variacion_pct > 0 THEN '📈' WHEN variacion_pct < 0 THEN '📉' ELSE '➡️' END AS tendencia
FROM con_variacion ORDER BY mes DESC;

-- 14. CTE recursivo: jerarquía de empleados
WITH RECURSIVE jerarquia AS (
    -- Base: empleados sin jefe (directores)
    SELECT id, nombre, apellido, puesto, jefe_id, 1 AS nivel,
           nombre || ' ' || apellido AS cadena
    FROM empleados WHERE jefe_id IS NULL

    UNION ALL

    -- Recursivo: empleados con jefe en el nivel anterior
    SELECT e.id, e.nombre, e.apellido, e.puesto, e.jefe_id, j.nivel + 1,
           j.cadena || ' → ' || e.nombre || ' ' || e.apellido
    FROM empleados e
    JOIN jerarquia j ON e.jefe_id = j.id
)
SELECT REPEAT('  ', nivel - 1) || nombre || ' ' || apellido AS organigrama,
       puesto, nivel
FROM jerarquia ORDER BY cadena;

-- 15. ALL: productos más caros que TODOS los de otra categoría
SELECT nombre, precio FROM productos
WHERE precio > ALL (
    SELECT precio FROM productos WHERE categoria_id = 2
)
ORDER BY precio;
