-- ════════════════════════════════════════════════════════════════
-- MÓDULO 08: Vistas - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 08: Vistas ═══'

-- Crear vista de catálogo
CREATE OR REPLACE VIEW v_catalogo AS
SELECT p.id, p.nombre, p.precio, c.nombre AS categoria, pr.nombre AS proveedor,
    COALESCE(SUM(i.cantidad), 0) AS stock_total
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
LEFT JOIN inventario i ON i.producto_id = p.id
WHERE p.activo = TRUE
GROUP BY p.id, p.nombre, p.precio, c.nombre, pr.nombre;

\echo '✓ Vista v_catalogo creada'

-- Crear vista de rendimiento vendedores
CREATE OR REPLACE VIEW v_vendedores AS
SELECT e.id, e.nombre || ' ' || e.apellido AS vendedor, s.nombre AS sucursal,
    COUNT(v.id) AS total_ventas, ROUND(COALESCE(SUM(v.total), 0)::numeric, 2) AS monto
FROM empleados e
LEFT JOIN ventas v ON v.empleado_id = e.id AND v.estado = 'COMPLETADA'
LEFT JOIN sucursales s ON e.sucursal_id = s.id
GROUP BY e.id, e.nombre, e.apellido, s.nombre;

\echo '✓ Vista v_vendedores creada'

-- Usar las vistas
\echo ''
\echo '── v_catalogo: Top 5 productos con más stock ──'
SELECT nombre, precio, categoria, stock_total FROM v_catalogo ORDER BY stock_total DESC LIMIT 5;

\echo ''
\echo '── v_vendedores: Top 5 vendedores ──'
SELECT vendedor, sucursal, total_ventas, monto FROM v_vendedores ORDER BY monto DESC LIMIT 5;
