-- ════════════════════════════════════════════════════════════════
-- MÓDULO 05 - EJERCICIO 1: JOINs
-- ════════════════════════════════════════════════════════════════

-- ═══════ INNER JOIN ═══════

-- 5.1: Muestra empleados con el NOMBRE de su sucursal (no el ID)
-- Columnas: nombre_empleado, apellido, puesto, nombre_sucursal
-- TU QUERY:


-- 5.2: Muestra productos con el NOMBRE de su categoría y proveedor
-- Columnas: producto, precio, categoria, proveedor
-- TU QUERY:


-- 5.3: Muestra las últimas 10 ventas con nombre del cliente y del vendedor
-- Columnas: numero_venta, fecha, total, cliente, vendedor, sucursal
-- TU QUERY:


-- ═══════ LEFT JOIN ═══════

-- 5.4: Muestra TODOS los empleados, incluso los que no tienen sucursal
-- Los que no tienen sucursal deben mostrar NULL en la columna sucursal
-- TU QUERY:


-- 5.5: Muestra productos que NUNCA se han vendido
-- (productos que no aparecen en detalle_venta)
-- TU QUERY:


-- 5.6: Muestra TODOS los clientes con su número de compras
-- Los que no han comprado deben mostrar 0
-- TU QUERY:


-- ═══════ SELF JOIN ═══════

-- 5.7: Muestra cada empleado con el nombre de su JEFE
-- Columnas: empleado, puesto_empleado, jefe, puesto_jefe
-- TU QUERY:


-- ═══════ JOINS MÚLTIPLES (3+ tablas) ═══════

-- 5.8: Reporte de ventas completo
-- Columnas: numero_venta, fecha, cliente, vendedor, sucursal, producto, cantidad, subtotal
-- Solo ventas COMPLETADAS del año 2024
-- Ordenar por fecha DESC, limitar a 50 registros
-- TU QUERY:


-- 5.9: ¿Cuánto ha vendido cada empleado por categoría de producto?
-- Columnas: vendedor, categoria, total_vendido, cantidad_items
-- Solo ventas completadas
-- Ordenar por total_vendido DESC
-- TU QUERY:


-- 5.10: Productos con stock total (suma de todas las sucursales) y cantidad vendida total
-- Columnas: producto, categoria, precio, stock_total, unidades_vendidas
-- Ordenar por unidades_vendidas DESC
-- TU QUERY:


-- ═══════ RETOS AVANZADOS ═══════

-- 5.11: Clientes que han comprado en MÁS DE UNA sucursal
-- Columnas: cliente, email, sucursales_diferentes
-- TU QUERY:


-- 5.12: Empleados que NO han realizado ninguna venta
-- TU QUERY:


-- 5.13: Top 3 productos más vendidos por CADA categoría
-- (Usa Window Functions: ROW_NUMBER() OVER (PARTITION BY...))
-- TU QUERY:


-- 5.14: Proveedores cuyos productos generaron más de $500,000 en ventas
-- Columnas: proveedor, total_ventas_generadas, productos_vendidos
-- TU QUERY:


-- 5.15: Para cada sucursal, muestra el empleado con más ventas
-- Columnas: sucursal, mejor_vendedor, total_ventas, monto_total
-- TU QUERY:

