-- ════════════════════════════════════════════════════════════════
-- SOLUCIÓN - MÓDULO 03: DML
-- ════════════════════════════════════════════════════════════════

-- 3.1: Insertar 3 clientes VIP de Querétaro
INSERT INTO clientes (nombre, apellido, email, telefono, ciudad, tipo) VALUES
('Roberto', 'Méndez Luna', 'roberto.mendez@email.com', '44-1234-5678', 'Querétaro', 'VIP'),
('Lucía', 'Fernández Ríos', 'lucia.fernandez@email.com', '44-2345-6789', 'Querétaro', 'VIP'),
('Andrés', 'Navarro Soto', 'andres.navarro@email.com', '44-3456-7890', 'Querétaro', 'VIP');

-- 3.2: Insertar producto
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id)
VALUES ('Monitor Ultrawide 34 pulgadas', 15999.00, 10500.00, 'MON-UW34-2026', 1, 2);

-- 3.3: Insertar venta completa (encabezado + detalle)
-- Paso a) Encabezado
INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id, estado, metodo_pago)
VALUES ('V-20260530-99999', 1, 7, 1, 'COMPLETADA', 'TARJETA')
RETURNING id;
-- Supongamos que devuelve id = 10001

-- Paso b) Detalles (usar el ID obtenido)
INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(10001, 1, 2, 18999.99, 37999.98),
(10001, 5, 1, 1899.00, 1899.00);

-- Actualizar total de la venta
UPDATE ventas SET
    subtotal = 39898.98,
    impuesto = ROUND(39898.98 * 0.16, 2),
    total = ROUND(39898.98 * 1.16, 2)
WHERE id = 10001;

-- 3.4: Aumento salarial por antigüedad
UPDATE empleados
SET salario = ROUND(salario * 1.15, 2)
WHERE fecha_ingreso < CURRENT_DATE - INTERVAL '3 years'
AND activo = TRUE;
-- Verificar: SELECT nombre, salario, fecha_ingreso FROM empleados WHERE fecha_ingreso < CURRENT_DATE - INTERVAL '3 years';

-- 3.5: Cancelar ventas pendientes viejas
UPDATE ventas
SET estado = 'CANCELADA'
WHERE estado = 'PENDIENTE'
AND fecha < NOW() - INTERVAL '30 days';

-- 3.6: Actualizar stock mínimo
UPDATE inventario
SET cantidad_minima = 20
WHERE producto_id IN (SELECT id FROM productos WHERE categoria_id = 1);

-- 3.7: Soft delete de clientes sin compras
UPDATE clientes
SET activo = FALSE
WHERE id NOT IN (SELECT DISTINCT cliente_id FROM ventas WHERE cliente_id IS NOT NULL);
-- NOTA: Usamos UPDATE (soft delete) en lugar de DELETE (hard delete)
-- porque las ventas futuras podrían referenciar estos clientes

-- 3.8: Eliminar auditoría vieja
DELETE FROM auditoria
WHERE fecha < NOW() - INTERVAL '1 year';

-- 3.9: UPSERT inventario
INSERT INTO inventario (producto_id, sucursal_id, cantidad, cantidad_minima)
VALUES (1, 1, 100, 10)
ON CONFLICT (producto_id, sucursal_id)
DO UPDATE SET
    cantidad = EXCLUDED.cantidad,
    ultima_actualizacion = NOW();
-- EXCLUDED = los valores que intentaste insertar

-- 3.10: UPSERT productos por SKU
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id)
VALUES ('Laptop Gaming 2026', 35000.00, 25000.00, 'SKU-00001', 2, 1)
ON CONFLICT (sku)
DO UPDATE SET
    nombre = EXCLUDED.nombre,
    precio = EXCLUDED.precio;
