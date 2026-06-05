-- ════════════════════════════════════════════════════════════════
-- MÓDULO 08 - EJERCICIO 1: Vistas
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 8.1: Vista de catálogo de productos
-- ═══════════════════════════════════════════════════════════════
-- Crea una vista v_catalogo_productos que muestre:
-- nombre_producto, descripcion, precio, precio_con_iva, categoria, proveedor, stock_total
-- (stock_total = suma de inventario en todas las sucursales)
-- Solo productos activos

-- TU CÓDIGO:


-- Prueba: SELECT * FROM v_catalogo_productos WHERE precio > 10000;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 8.2: Vista de rendimiento de vendedores
-- ═══════════════════════════════════════════════════════════════
-- Crea v_rendimiento_vendedores con:
-- vendedor (nombre completo), sucursal, total_ventas (cantidad), 
-- monto_total, ticket_promedio, mejor_venta, peor_venta
-- Solo ventas COMPLETADAS

-- TU CÓDIGO:


-- Prueba: SELECT * FROM v_rendimiento_vendedores ORDER BY monto_total DESC LIMIT 10;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 8.3: Vista de alertas de stock
-- ═══════════════════════════════════════════════════════════════
-- Crea v_alertas_stock que muestre productos con stock BAJO el mínimo:
-- producto, sku, sucursal, stock_actual, stock_minimo, deficit, proveedor_contacto

-- TU CÓDIGO:


-- Prueba: SELECT * FROM v_alertas_stock ORDER BY deficit DESC;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 8.4: Vista de dashboard (KPIs)
-- ═══════════════════════════════════════════════════════════════
-- Crea v_dashboard que muestre EN UNA SOLA FILA:
-- ventas_hoy, ventas_mes, ventas_año, 
-- clientes_nuevos_mes, productos_stock_bajo, ticket_promedio_mes

-- TU CÓDIGO:


-- Prueba: SELECT * FROM v_dashboard;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 8.5: Vista materializada de ventas mensuales
-- ═══════════════════════════════════════════════════════════════
-- Crea una MATERIALIZED VIEW mv_ventas_mensuales con:
-- año, mes, sucursal, total_ventas, monto, ticket_promedio
-- Incluye índice único para poder hacer REFRESH CONCURRENTLY

-- TU CÓDIGO:


-- Prueba: SELECT * FROM mv_ventas_mensuales WHERE año = 2024 ORDER BY mes;
-- Refrescar: REFRESH MATERIALIZED VIEW CONCURRENTLY mv_ventas_mensuales;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 8.6: Vista de seguridad (ocultar datos sensibles)
-- ═══════════════════════════════════════════════════════════════
-- Crea v_directorio_publico que muestre empleados SIN salario ni datos sensibles:
-- nombre, apellido, puesto, sucursal, email (con dominio oculto: "car***@empresa.com")
-- Solo empleados activos

-- TU CÓDIGO:


-- Prueba: SELECT * FROM v_directorio_publico;
