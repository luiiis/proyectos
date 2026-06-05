-- ════════════════════════════════════════════════════════════════
-- SOLUCIÓN - MÓDULO 04: Consultas SELECT Básicas
-- ════════════════════════════════════════════════════════════════

-- 4.1: Todos los campos de sucursales
SELECT * FROM sucursales;

-- 4.2: Nombre y precio de productos
SELECT nombre, precio FROM productos;

-- 4.3: Nombre, apellido y salario de empleados
SELECT nombre, apellido, salario FROM empleados;

-- 4.4: Renombrar columnas con alias
SELECT nombre AS producto, precio AS costo FROM productos;

-- 4.5: Precio con IVA (columna calculada)
SELECT nombre, precio, ROUND(precio * 1.16, 2) AS precio_con_iva FROM productos;

-- 4.6: Productos con precio > $10,000
SELECT nombre, precio FROM productos WHERE precio > 10000 ORDER BY precio DESC;

-- 4.7: Empleados de sucursal 1
SELECT nombre, apellido, puesto FROM empleados WHERE sucursal_id = 1;

-- 4.8: Clientes VIP
SELECT nombre, apellido, email, ciudad FROM clientes WHERE tipo = 'VIP';

-- 4.9: Productos entre $1,000 y $5,000
SELECT nombre, precio FROM productos WHERE precio BETWEEN 1000 AND 5000 ORDER BY precio;

-- 4.10: Empleados cuyo nombre empieza con 'M'
SELECT nombre, apellido FROM empleados WHERE nombre LIKE 'M%';
-- ILIKE para case-insensitive: WHERE nombre ILIKE 'm%'

-- 4.11: Clientes de 3 ciudades
SELECT nombre, apellido, ciudad FROM clientes
WHERE ciudad IN ('Ciudad de México', 'Monterrey', 'Guadalajara');

-- 4.12: Salario > 40K Y sucursal 2
SELECT nombre, apellido, salario FROM empleados
WHERE salario > 40000 AND sucursal_id = 2;

-- 4.13: Productos que NO son categoría 1 ni 2
SELECT nombre, categoria_id FROM productos
WHERE categoria_id NOT IN (1, 2);
-- Alternativa: WHERE categoria_id != 1 AND categoria_id != 2

-- 4.14: Empleados sin jefe (NULL)
SELECT nombre, apellido, puesto FROM empleados WHERE jefe_id IS NULL;
-- IMPORTANTE: usar IS NULL, no = NULL (= NULL siempre es false)

-- 4.15: Clientes con email
SELECT nombre, apellido, email FROM clientes WHERE email IS NOT NULL;

-- 4.16: Top 10 productos más caros
SELECT nombre, precio FROM productos ORDER BY precio DESC LIMIT 10;

-- 4.17: 5 empleados con menor salario
SELECT nombre, apellido, salario FROM empleados ORDER BY salario ASC LIMIT 5;

-- 4.18: Clientes por ciudad y apellido
SELECT nombre, apellido, ciudad FROM clientes
ORDER BY ciudad ASC, apellido ASC;

-- 4.19: Últimas 20 ventas
SELECT numero_venta, fecha, total, estado FROM ventas
ORDER BY fecha DESC LIMIT 20;

-- 4.20: Productos por categoría y precio
SELECT nombre, categoria_id, precio FROM productos
ORDER BY categoria_id ASC, precio DESC;

-- 4.21: Total de empleados
SELECT COUNT(*) AS total_empleados FROM empleados;

-- 4.22: Clientes por ciudad
SELECT ciudad, COUNT(*) AS total_clientes
FROM clientes
GROUP BY ciudad
ORDER BY total_clientes DESC;

-- 4.23: Estadísticas de salario
SELECT
    ROUND(AVG(salario), 2) AS salario_promedio,
    MIN(salario) AS salario_minimo,
    MAX(salario) AS salario_maximo,
    ROUND(STDDEV(salario), 2) AS desviacion_estandar
FROM empleados;

-- 4.24: Productos por categoría
SELECT categoria_id, COUNT(*) AS total_productos
FROM productos
GROUP BY categoria_id
ORDER BY total_productos DESC;

-- 4.25: Ventas por método de pago
SELECT metodo_pago, COUNT(*) AS cantidad, SUM(total) AS monto_total
FROM ventas
WHERE estado = 'COMPLETADA'
GROUP BY metodo_pago
ORDER BY monto_total DESC;

-- 4.26: Ciudades con más de 50 clientes (HAVING)
SELECT ciudad, COUNT(*) AS total
FROM clientes
GROUP BY ciudad
HAVING COUNT(*) > 50
ORDER BY total DESC;

-- 4.27: Categorías con precio promedio > $5,000
SELECT categoria_id, ROUND(AVG(precio), 2) AS precio_promedio
FROM productos
GROUP BY categoria_id
HAVING AVG(precio) > 5000
ORDER BY precio_promedio DESC;

-- 4.28: Sucursales con más de 15 empleados
SELECT sucursal_id, COUNT(*) AS total_empleados
FROM empleados
GROUP BY sucursal_id
HAVING COUNT(*) > 15;

-- 4.29: Meses de 2024 con más de 500 ventas
SELECT
    EXTRACT(MONTH FROM fecha) AS mes,
    COUNT(*) AS total_ventas,
    ROUND(SUM(total), 2) AS facturacion
FROM ventas
WHERE EXTRACT(YEAR FROM fecha) = 2024
GROUP BY EXTRACT(MONTH FROM fecha)
HAVING COUNT(*) > 500
ORDER BY mes;

-- 4.30: Top 5 empleados con más ventas
SELECT
    e.nombre || ' ' || e.apellido AS vendedor,
    COUNT(v.id) AS total_ventas,
    ROUND(SUM(v.total), 2) AS monto_total
FROM empleados e
JOIN ventas v ON v.empleado_id = e.id
WHERE v.estado = 'COMPLETADA'
GROUP BY e.nombre, e.apellido
ORDER BY total_ventas DESC
LIMIT 5;
