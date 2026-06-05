-- ════════════════════════════════════════════════════════════════
-- MÓDULO 04 - EJERCICIO 1: Consultas SELECT Básicas
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Conecta a PostgreSQL y escribe las queries para cada ejercicio.
-- Usa la BD empresa_db que ya tiene datos cargados.
-- ════════════════════════════════════════════════════════════════

-- ═══════ NIVEL 1: SELECT simple ═══════

-- 4.1: Muestra TODOS los campos de la tabla sucursales
-- TU QUERY:


-- 4.2: Muestra solo nombre y precio de los productos
-- TU QUERY:


-- 4.3: Muestra nombre, apellido y salario de empleados
-- TU QUERY:


-- 4.4: Muestra nombre y precio de productos, pero renombra las columnas a "producto" y "costo"
-- TU QUERY:


-- 4.5: Muestra nombre y precio CON IVA (precio * 1.16) de cada producto
-- TU QUERY:


-- ═══════ NIVEL 2: WHERE (filtros) ═══════

-- 4.6: Productos con precio mayor a $10,000
-- TU QUERY:


-- 4.7: Empleados de la sucursal 1
-- TU QUERY:


-- 4.8: Clientes de tipo 'VIP'
-- TU QUERY:


-- 4.9: Productos con precio entre $1,000 y $5,000
-- TU QUERY:


-- 4.10: Empleados cuyo nombre empieza con 'M'
-- TU QUERY:


-- 4.11: Clientes de Ciudad de México, Monterrey o Guadalajara
-- TU QUERY:


-- 4.12: Empleados con salario mayor a $40,000 Y que sean de la sucursal 2
-- TU QUERY:


-- 4.13: Productos que NO son de la categoría 1 ni de la categoría 2
-- TU QUERY:


-- 4.14: Empleados sin jefe asignado (jefe_id es NULL)
-- TU QUERY:


-- 4.15: Clientes que tienen email registrado (email NO es NULL)
-- TU QUERY:


-- ═══════ NIVEL 3: ORDER BY + LIMIT ═══════

-- 4.16: Los 10 productos más caros (ordenados de mayor a menor precio)
-- TU QUERY:


-- 4.17: Los 5 empleados con menor salario
-- TU QUERY:


-- 4.18: Clientes ordenados por ciudad (A-Z) y dentro de cada ciudad por apellido (A-Z)
-- TU QUERY:


-- 4.19: Las últimas 20 ventas registradas (por fecha, más recientes primero)
-- TU QUERY:


-- 4.20: Productos ordenados por categoría y dentro de cada categoría por precio descendente
-- TU QUERY:


-- ═══════ NIVEL 4: GROUP BY + HAVING ═══════

-- 4.21: ¿Cuántos empleados hay en total?
-- TU QUERY:


-- 4.22: ¿Cuántos clientes hay por cada ciudad?
-- TU QUERY:


-- 4.23: ¿Cuál es el salario promedio, mínimo y máximo de los empleados?
-- TU QUERY:


-- 4.24: ¿Cuántos productos hay por categoría? Ordena de mayor a menor
-- TU QUERY:


-- 4.25: ¿Cuál es el total de ventas por cada método de pago?
-- TU QUERY:


-- 4.26: Ciudades que tienen MÁS de 50 clientes
-- TU QUERY:


-- 4.27: Categorías cuyo precio promedio de productos es mayor a $5,000
-- TU QUERY:


-- 4.28: Sucursales con más de 15 empleados
-- TU QUERY:


-- 4.29: Meses del año 2024 con más de 500 ventas
-- TU QUERY:


-- 4.30: Top 5 empleados con más ventas realizadas (cantidad, no monto)
-- TU QUERY:

