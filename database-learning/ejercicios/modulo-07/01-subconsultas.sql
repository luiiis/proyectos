-- ════════════════════════════════════════════════════════════════
-- MÓDULO 07 - EJERCICIO 1: Subconsultas
-- ════════════════════════════════════════════════════════════════

-- ═══════ SUBCONSULTAS ESCALARES (devuelven 1 valor) ═══════

-- 7.1: Empleados que ganan MÁS que el salario promedio general
-- TU QUERY:


-- 7.2: El producto más caro de toda la tienda (sin ORDER BY + LIMIT)
--      PISTA: WHERE precio = (SELECT MAX(precio) FROM productos)
-- TU QUERY:


-- 7.3: Ventas cuyo total es mayor al DOBLE del ticket promedio
-- TU QUERY:


-- ═══════ SUBCONSULTAS CON IN / NOT IN ═══════

-- 7.4: Clientes que HAN comprado al menos una vez
--      (su ID aparece en la tabla ventas)
-- TU QUERY:


-- 7.5: Productos que NUNCA se han vendido
--      (su ID NO aparece en detalle_venta)
-- TU QUERY:


-- 7.6: Empleados que trabajan en sucursales de ciudades con más de 100 clientes
-- TU QUERY:


-- 7.7: Categorías que tienen al menos un producto con precio > $20,000
-- TU QUERY:


-- ═══════ SUBCONSULTAS CORRELACIONADAS ═══════

-- 7.8: Empleados que ganan más que el PROMEDIO DE SU SUCURSAL
--      (la subconsulta usa datos de la query externa)
-- TU QUERY:


-- 7.9: Para cada cliente, su ÚLTIMA compra (fecha más reciente)
--      Columnas: nombre, apellido, fecha_ultima_compra, monto
-- TU QUERY:


-- 7.10: Productos cuyo precio es el MÁXIMO de su categoría
-- TU QUERY:


-- ═══════ EXISTS / NOT EXISTS ═══════

-- 7.11: Clientes que SÍ han comprado (usando EXISTS, no IN)
--       ¿Por qué EXISTS puede ser más rápido que IN en tablas grandes?
-- TU QUERY:


-- 7.12: Proveedores que NO tienen ningún producto vendido este año
-- TU QUERY:


-- 7.13: Sucursales donde TODOS los productos tienen stock > 0
--       PISTA: NOT EXISTS (... WHERE cantidad = 0)
-- TU QUERY:


-- ═══════ CTEs (Common Table Expressions) ═══════

-- 7.14: Usando CTE, muestra el top 5 vendedores con su porcentaje del total de ventas
--       Paso 1 (CTE): calcular total por vendedor
--       Paso 2 (CTE): calcular el gran total
--       Paso 3: dividir y mostrar porcentaje
-- TU QUERY:


-- 7.15: CTE Recursiva - Muestra la jerarquía completa de empleados
--       Formato: nivel, nombre, puesto, jefe
--       Empezando por el Director (jefe_id IS NULL) hasta el último nivel
-- TU QUERY:


-- ═══════ RETOS AVANZADOS ═══════

-- 7.16: Segundo producto más caro de CADA categoría
--       (no el más caro, el SEGUNDO)
-- TU QUERY:


-- 7.17: Clientes que han comprado en TODAS las sucursales (división relacional)
--       PISTA: contar sucursales distintas del cliente = total de sucursales
-- TU QUERY:


-- 7.18: Meses donde las ventas CAYERON respecto al mes anterior
--       Columnas: mes, ventas_mes, ventas_mes_anterior, caida_pct
-- TU QUERY:

