-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Ejercicios Básicos B011 a B050 (SELECT avanzado)
-- ════════════════════════════════════════════════════════════════

-- B011: Productos con precio NULL (si los hay)
SELECT nombre, precio FROM productos WHERE precio IS NULL;

-- B012: Empleados que NO tienen jefe (directores)
SELECT nombre, apellido, puesto FROM empleados WHERE jefe_id IS NULL;

-- B013: Clientes que se registraron este año
SELECT nombre, apellido, created_at FROM clientes
WHERE EXTRACT(YEAR FROM created_at) = EXTRACT(YEAR FROM CURRENT_DATE);

-- B014: Top 5 empleados mejor pagados
SELECT nombre, apellido, puesto, salario FROM empleados
ORDER BY salario DESC LIMIT 5;

-- B015: Producto más caro y más barato
SELECT 'Más caro' AS tipo, nombre, precio FROM productos ORDER BY precio DESC LIMIT 1
UNION ALL
SELECT 'Más barato', nombre, precio FROM productos WHERE precio > 0 ORDER BY precio LIMIT 1;

-- B016: Empleados con salario entre 30000 y 50000
SELECT nombre, apellido, salario FROM empleados
WHERE salario BETWEEN 30000 AND 50000 ORDER BY salario;

-- B017: Clientes de CDMX o Monterrey o Guadalajara
SELECT nombre, apellido, ciudad FROM clientes
WHERE ciudad IN ('Ciudad de México', 'Monterrey', 'Guadalajara');

-- B018: Productos que contienen "laptop" en el nombre (case-insensitive)
SELECT nombre, precio FROM productos WHERE nombre ILIKE '%laptop%';

-- B019: Cantidad de empleados activos vs inactivos
SELECT activo, COUNT(*) AS total FROM empleados GROUP BY activo;

-- B020: Salario promedio por puesto
SELECT puesto, ROUND(AVG(salario), 2) AS salario_promedio, COUNT(*) AS empleados
FROM empleados GROUP BY puesto ORDER BY salario_promedio DESC;

-- B021: Total de ventas por estado
SELECT estado, COUNT(*) AS cantidad, SUM(total) AS monto_total
FROM ventas GROUP BY estado ORDER BY monto_total DESC;

-- B022: Mes con más ventas
SELECT EXTRACT(MONTH FROM fecha) AS mes, COUNT(*) AS ventas, SUM(total) AS monto
FROM ventas GROUP BY mes ORDER BY monto DESC LIMIT 1;

-- B023: Clientes que empiezan con A o M
SELECT nombre, apellido FROM clientes WHERE nombre LIKE 'A%' OR nombre LIKE 'M%' ORDER BY nombre;

-- B024: Producto con el SKU más largo
SELECT nombre, sku, LENGTH(sku) AS largo_sku FROM productos ORDER BY largo_sku DESC LIMIT 5;

-- B025: Empleados con más de 3 años de antigüedad
SELECT nombre, apellido, fecha_ingreso,
       AGE(fecha_ingreso) AS antiguedad
FROM empleados WHERE fecha_ingreso < CURRENT_DATE - INTERVAL '3 years';

-- B026: Nombre completo en mayúsculas + email
SELECT UPPER(nombre || ' ' || apellido) AS nombre_completo, email FROM empleados;

-- B027: Precio con IVA (16%)
SELECT nombre, precio, ROUND(precio * 1.16, 2) AS precio_con_iva FROM productos LIMIT 20;

-- B028: Ventas del último mes
SELECT * FROM ventas WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
AND fecha < DATE_TRUNC('month', CURRENT_DATE);

-- B029: Clientes sin email
SELECT nombre, apellido FROM clientes WHERE email IS NULL;

-- B030: Sucursales ordenadas por ciudad
SELECT nombre, ciudad, telefono FROM sucursales WHERE activa = true ORDER BY ciudad, nombre;

-- B031: Total de inventario por sucursal
SELECT s.nombre, SUM(i.cantidad) AS total_items
FROM sucursales s JOIN inventario i ON i.sucursal_id = s.id
GROUP BY s.nombre ORDER BY total_items DESC;

-- B032: Empleados contratados por año
SELECT EXTRACT(YEAR FROM fecha_ingreso) AS anio, COUNT(*) AS contrataciones
FROM empleados GROUP BY anio ORDER BY anio;

-- B033: Categorías con su producto más caro
SELECT c.nombre AS categoria, MAX(p.precio) AS precio_max
FROM categorias c JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre ORDER BY precio_max DESC;

-- B034: Ventas por día de la semana
SELECT TO_CHAR(fecha, 'Day') AS dia, COUNT(*) AS ventas
FROM ventas GROUP BY dia ORDER BY ventas DESC;

-- B035: Clientes VIP con sus datos completos
SELECT nombre, apellido, email, telefono, ciudad FROM clientes
WHERE tipo = 'VIP' AND activo = true ORDER BY nombre;

-- B036: Proveedores con más de 10 productos
SELECT pr.nombre, COUNT(p.id) AS productos
FROM proveedores pr JOIN productos p ON p.proveedor_id = pr.id
GROUP BY pr.nombre HAVING COUNT(p.id) > 10 ORDER BY productos DESC;

-- B037: Rango de salarios por sucursal
SELECT s.nombre, MIN(e.salario) AS min_salario, MAX(e.salario) AS max_salario,
       MAX(e.salario) - MIN(e.salario) AS rango
FROM sucursales s JOIN empleados e ON e.sucursal_id = s.id
GROUP BY s.nombre;

-- B038: Productos con stock 0
SELECT p.nombre, p.sku FROM productos p
JOIN inventario i ON i.producto_id = p.id WHERE i.cantidad = 0;

-- B039: Ticket promedio (promedio de total por venta)
SELECT ROUND(AVG(total), 2) AS ticket_promedio FROM ventas WHERE estado = 'COMPLETADA';

-- B040: Clientes por tipo con porcentaje
SELECT tipo, COUNT(*) AS cantidad,
       ROUND(COUNT(*) * 100.0 / (SELECT COUNT(*) FROM clientes), 1) AS porcentaje
FROM clientes GROUP BY tipo ORDER BY cantidad DESC;

-- B041: Productos nunca vendidos (subconsulta simple)
SELECT nombre, precio FROM productos
WHERE id NOT IN (SELECT DISTINCT producto_id FROM detalle_venta);

-- B042: Último empleado contratado
SELECT nombre, apellido, puesto, fecha_ingreso FROM empleados
ORDER BY fecha_ingreso DESC LIMIT 1;

-- B043: Ventas con descuento aplicado
SELECT numero_venta, total, descuento FROM ventas WHERE descuento > 0 ORDER BY descuento DESC;

-- B044: Empleados sin email
SELECT nombre, apellido, puesto FROM empleados WHERE email IS NULL OR email = '';

-- B045: Productos entre percentil 25 y 75 de precio
SELECT nombre, precio FROM productos
WHERE precio BETWEEN
    (SELECT PERCENTILE_CONT(0.25) WITHIN GROUP (ORDER BY precio) FROM productos)
    AND
    (SELECT PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY precio) FROM productos)
ORDER BY precio;

-- B046: Nombre del mes de la venta + total
SELECT TO_CHAR(fecha, 'Month YYYY') AS mes, SUM(total) AS total_mes
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY TO_CHAR(fecha, 'Month YYYY'), DATE_TRUNC('month', fecha)
ORDER BY DATE_TRUNC('month', fecha) DESC;

-- B047: Sucursales sin empleados
SELECT s.nombre FROM sucursales s
LEFT JOIN empleados e ON e.sucursal_id = s.id
WHERE e.id IS NULL;

-- B048: Empleados con salario mayor al promedio general
SELECT nombre, apellido, salario FROM empleados
WHERE salario > (SELECT AVG(salario) FROM empleados) ORDER BY salario DESC;

-- B049: Top 3 clientes que más han comprado (monto)
SELECT cl.nombre, cl.apellido, SUM(v.total) AS total_comprado
FROM clientes cl JOIN ventas v ON v.cliente_id = cl.id
WHERE v.estado = 'COMPLETADA'
GROUP BY cl.id, cl.nombre, cl.apellido
ORDER BY total_comprado DESC LIMIT 3;

-- B050: Productos con margen de ganancia (si hay columna costo)
SELECT nombre, precio, costo, ROUND(precio - costo, 2) AS margen,
       ROUND((precio - costo) / costo * 100, 1) AS margen_pct
FROM productos WHERE costo > 0 ORDER BY margen_pct DESC LIMIT 10;
