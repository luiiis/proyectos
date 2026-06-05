-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Ejercicios Básicos B001 a B010
-- ════════════════════════════════════════════════════════════════

-- B001: Seleccionar todos los empleados
SELECT * FROM empleados;

-- B002: Seleccionar nombres de clientes
SELECT nombre, apellido FROM clientes;

-- B003: Filtrar por ciudad
SELECT * FROM clientes WHERE ciudad = 'Ciudad de México';

-- B004: Ordenar por salario (mayor a menor)
SELECT nombre, apellido, puesto, salario
FROM empleados
ORDER BY salario DESC;

-- B005: Contar productos
SELECT COUNT(*) AS total_productos FROM productos;

-- B006: Filtrar por rango de precio
SELECT nombre, precio
FROM productos
WHERE precio BETWEEN 1000 AND 5000
ORDER BY precio;

-- B007: Buscar por patrón (nombre empieza con M)
SELECT nombre, apellido, puesto
FROM empleados
WHERE nombre LIKE 'M%';

-- B008: Ciudades únicas de clientes
SELECT COUNT(DISTINCT ciudad) AS ciudades_diferentes
FROM clientes;
-- O para ver cuáles son:
SELECT DISTINCT ciudad FROM clientes ORDER BY ciudad;

-- B009: Empleados por sucursal
SELECT s.nombre AS sucursal, COUNT(e.id) AS total_empleados
FROM sucursales s
LEFT JOIN empleados e ON e.sucursal_id = s.id
GROUP BY s.nombre
ORDER BY total_empleados DESC;

-- B010: Categorías con más de 30 productos
SELECT c.nombre AS categoria, COUNT(p.id) AS total_productos
FROM categorias c
JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre
HAVING COUNT(p.id) > 30
ORDER BY total_productos DESC;
