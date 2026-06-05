-- ════════════════════════════════════════════════════════════════
-- MÓDULO 03: DML - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 03: DML - INSERT, UPDATE, DELETE ═══'

-- INSERT: Agregar un producto
\echo ''
\echo '── INSERT ──'
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id, activo)
VALUES ('Producto Demo DML', 1234.56, 800.00, 'DEMO-DML-001', 1, 1, TRUE)
RETURNING id, nombre, precio;

-- UPDATE: Modificar precio
\echo ''
\echo '── UPDATE ──'
UPDATE productos SET precio = 1500.00 WHERE sku = 'DEMO-DML-001'
RETURNING id, nombre, precio;

-- UPSERT: Insertar o actualizar
\echo ''
\echo '── UPSERT (ON CONFLICT) ──'
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id)
VALUES ('Producto Demo DML', 1999.99, 1200.00, 'DEMO-DML-001', 1, 1)
ON CONFLICT (sku) DO UPDATE SET precio = EXCLUDED.precio
RETURNING id, nombre, precio, '(actualizado por UPSERT)' AS nota;

-- DELETE: Eliminar el producto de demo
\echo ''
\echo '── DELETE ──'
DELETE FROM productos WHERE sku = 'DEMO-DML-001'
RETURNING id, nombre, '(eliminado)' AS nota;

\echo ''
\echo '✓ Demo DML completada (datos de prueba limpiados)'
