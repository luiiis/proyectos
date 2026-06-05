-- ════════════════════════════════════════════════════════════════
-- MÓDULO 11 - EJERCICIO 1: Stored Procedures y Functions
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 11.1: Función - Total de compras de un cliente
-- ═══════════════════════════════════════════════════════════════
-- Crea una función fn_total_compras(p_cliente_id INTEGER)
-- que devuelva el monto total de compras de un cliente (solo ventas COMPLETADAS)
-- Tipo de retorno: NUMERIC

-- TU CÓDIGO AQUÍ:



-- Prueba: SELECT fn_total_compras(1);
-- Prueba: SELECT nombre, apellido, fn_total_compras(id) AS total FROM clientes LIMIT 10;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 11.2: Función - Clasificar cliente por nivel de compras
-- ═══════════════════════════════════════════════════════════════
-- Crea fn_nivel_cliente(p_cliente_id INTEGER) que devuelva:
-- 'DIAMANTE' si total > 500,000
-- 'ORO' si total > 200,000
-- 'PLATA' si total > 50,000
-- 'BRONCE' si total > 0
-- 'SIN COMPRAS' si total = 0

-- TU CÓDIGO AQUÍ:



-- Prueba: SELECT nombre, fn_nivel_cliente(id) AS nivel FROM clientes LIMIT 20;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 11.3: Procedimiento - Registrar venta completa
-- ═══════════════════════════════════════════════════════════════
-- Crea sp_registrar_venta(
--   p_cliente_id INTEGER,
--   p_empleado_id INTEGER,
--   p_sucursal_id INTEGER,
--   p_productos JSONB  -- formato: [{"producto_id": 1, "cantidad": 2}, ...]
-- )
-- El procedimiento debe:
-- 1. Generar número de venta automático
-- 2. Insertar encabezado en ventas
-- 3. Para cada producto del JSON:
--    a. Obtener precio actual del producto
--    b. Insertar línea en detalle_venta
--    c. Descontar inventario
-- 4. Calcular y actualizar totales (subtotal, impuesto, total)
-- 5. Si algún producto no tiene stock → RAISE EXCEPTION y ROLLBACK

-- TU CÓDIGO AQUÍ:



-- Prueba:
-- CALL sp_registrar_venta(1, 7, 1, '[{"producto_id": 1, "cantidad": 1}, {"producto_id": 5, "cantidad": 2}]'::jsonb);


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 11.4: Procedimiento - Cierre mensual
-- ═══════════════════════════════════════════════════════════════
-- Crea sp_cierre_mensual(p_año INTEGER, p_mes INTEGER) que:
-- 1. Calcule ventas totales del mes por sucursal
-- 2. Calcule el top 5 vendedores del mes
-- 3. Calcule productos más vendidos
-- 4. Inserte un resumen en una tabla "cierres_mensuales"
-- 5. Muestre los resultados con RAISE NOTICE

-- Primero crea la tabla:
CREATE TABLE IF NOT EXISTS cierres_mensuales (
    id SERIAL PRIMARY KEY,
    año INTEGER NOT NULL,
    mes INTEGER NOT NULL,
    sucursal_id INTEGER REFERENCES sucursales(id),
    total_ventas NUMERIC(14,2),
    cantidad_ventas INTEGER,
    ticket_promedio NUMERIC(10,2),
    fecha_cierre TIMESTAMP DEFAULT NOW(),
    UNIQUE(año, mes, sucursal_id)
);

-- TU CÓDIGO AQUÍ:



-- Prueba: CALL sp_cierre_mensual(2024, 6);
-- Verificar: SELECT * FROM cierres_mensuales;


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 11.5: Función - Reporte de inventario valorizado
-- ═══════════════════════════════════════════════════════════════
-- Crea una función fn_inventario_valorizado(p_sucursal_id INTEGER)
-- que devuelva una TABLA con:
-- producto, categoria, cantidad, precio, valor_total (cantidad * precio)
-- Ordenado por valor_total DESC
-- Tipo retorno: TABLE(producto TEXT, categoria TEXT, cantidad INT, precio NUMERIC, valor_total NUMERIC)

-- TU CÓDIGO AQUÍ:



-- Prueba: SELECT * FROM fn_inventario_valorizado(1) LIMIT 20;
