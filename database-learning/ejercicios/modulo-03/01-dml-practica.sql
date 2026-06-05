-- ════════════════════════════════════════════════════════════════
-- MÓDULO 03 - EJERCICIO 1: DML (INSERT, UPDATE, DELETE)
-- ════════════════════════════════════════════════════════════════

-- ═══════ INSERT ═══════

-- 3.1: Inserta 3 clientes nuevos de tipo VIP en Querétaro
-- TU QUERY:


-- 3.2: Inserta un producto nuevo: "Monitor Ultrawide 34 pulgadas", precio $15,999, 
--      categoría 1, proveedor 2, SKU 'MON-UW34-2026'
-- TU QUERY:


-- 3.3: Inserta una venta completa:
--   a) Primero el encabezado (ventas): cliente_id=1, empleado_id=7, sucursal_id=1
--   b) Luego 2 líneas de detalle (detalle_venta): producto 1 qty 2, producto 5 qty 1
--   PISTA: Usa RETURNING id en el INSERT de ventas para obtener el ID generado
-- TU QUERY:


-- ═══════ UPDATE ═══════

-- 3.4: Aumenta 15% el salario de empleados con más de 3 años de antigüedad
--   PISTA: fecha_ingreso < CURRENT_DATE - INTERVAL '3 years'
-- TU QUERY:


-- 3.5: Cambia a 'CANCELADA' todas las ventas con estado 'PENDIENTE' 
--      que tengan más de 30 días de antigüedad
-- TU QUERY:


-- 3.6: Actualiza el stock mínimo a 20 para todos los productos de categoría 1
--      en la tabla inventario
-- TU QUERY:


-- ═══════ DELETE ═══════

-- 3.7: Elimina (soft delete: activo=false) los clientes que NUNCA han comprado
--   PISTA: clientes cuyo id NO aparece en ventas.cliente_id
-- TU QUERY:


-- 3.8: Elimina registros de auditoría con más de 1 año de antigüedad
-- TU QUERY:


-- ═══════ UPSERT ═══════

-- 3.9: Inserta o actualiza inventario: producto 1, sucursal 1, cantidad 100
--   Si ya existe esa combinación (producto_id, sucursal_id), actualiza la cantidad
--   Si no existe, inserta el registro nuevo
--   PISTA: ON CONFLICT (producto_id, sucursal_id) DO UPDATE
-- TU QUERY:


-- 3.10: Crea un UPSERT para la tabla productos usando el SKU como clave única
--   Si el SKU ya existe → actualizar precio y nombre
--   Si no existe → insertar completo
-- TU QUERY:

