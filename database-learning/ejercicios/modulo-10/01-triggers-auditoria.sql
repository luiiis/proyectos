-- ════════════════════════════════════════════════════════════════
-- MÓDULO 10 - EJERCICIO 1: Triggers y Auditoría
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Implementa un sistema de auditoría completo usando triggers.
-- Cada cambio en tablas críticas debe quedar registrado.
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 10.1: Trigger de auditoría genérico
-- ═══════════════════════════════════════════════════════════════
-- Crea una función fn_auditoria_generica() que:
-- 1. Se ejecute AFTER INSERT, UPDATE o DELETE
-- 2. Registre en la tabla "auditoria":
--    - tabla: nombre de la tabla afectada (TG_TABLE_NAME)
--    - operacion: INSERT, UPDATE o DELETE (TG_OP)
--    - registro_id: el ID del registro afectado
--    - datos_antes: JSON con los datos ANTES del cambio (OLD)
--    - datos_despues: JSON con los datos DESPUÉS del cambio (NEW)
--    - usuario: el usuario de BD actual (current_user)
--    - fecha: timestamp actual
-- 3. Retorne NEW para INSERT/UPDATE, OLD para DELETE

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 10.2: Aplicar trigger a tablas
-- ═══════════════════════════════════════════════════════════════
-- Aplica el trigger de auditoría a:
-- - productos (INSERT, UPDATE, DELETE)
-- - empleados (INSERT, UPDATE, DELETE)
-- - ventas (INSERT, UPDATE)

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 10.3: Trigger de validación de stock
-- ═══════════════════════════════════════════════════════════════
-- Crea un trigger BEFORE INSERT en detalle_venta que:
-- 1. Verifique que hay stock suficiente del producto en la sucursal de la venta
-- 2. Si NO hay stock suficiente, lance un error con RAISE EXCEPTION
-- 3. Si SÍ hay stock, descuente la cantidad del inventario

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 10.4: Trigger de actualización automática de totales
-- ═══════════════════════════════════════════════════════════════
-- Crea un trigger AFTER INSERT OR DELETE en detalle_venta que:
-- 1. Recalcule el subtotal de la venta (SUM de subtotales de detalles)
-- 2. Calcule el impuesto (subtotal * 0.16)
-- 3. Calcule el total (subtotal + impuesto)
-- 4. Actualice la tabla ventas con estos valores

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 10.5: Trigger de alerta de stock bajo
-- ═══════════════════════════════════════════════════════════════
-- Crea una tabla "alertas_stock" y un trigger que:
-- 1. Se ejecute AFTER UPDATE en inventario
-- 2. Si la cantidad nueva es MENOR que cantidad_minima
-- 3. Inserte un registro en alertas_stock con:
--    producto_id, sucursal_id, cantidad_actual, cantidad_minima, fecha

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- PRUEBAS: Ejecuta estas operaciones y verifica la auditoría
-- ═══════════════════════════════════════════════════════════════

-- Insertar un producto (debe generar registro en auditoria)
-- INSERT INTO productos (nombre, precio, stock, categoria_id, proveedor_id)
-- VALUES ('Producto Test Trigger', 999.99, 50, 1, 1);

-- Actualizar el precio (debe generar registro con datos_antes y datos_despues)
-- UPDATE productos SET precio = 1099.99 WHERE nombre = 'Producto Test Trigger';

-- Verificar auditoría
-- SELECT * FROM auditoria ORDER BY fecha DESC LIMIT 5;
