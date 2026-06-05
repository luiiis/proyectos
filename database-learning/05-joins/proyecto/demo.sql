-- ════════════════════════════════════════════════════════════════
-- MÓDULO 05: JOINs - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 05: JOINs ═══'

\echo ''
\echo '── INNER JOIN: Productos con categoría y proveedor ──'
SELECT p.nombre, p.precio, c.nombre AS categoria, pr.nombre AS proveedor
FROM productos p
JOIN categorias c ON p.categoria_id = c.id
JOIN proveedores pr ON p.proveedor_id = pr.id
ORDER BY p.precio DESC LIMIT 10;

\echo ''
\echo '── LEFT JOIN: Productos sin ventas ──'
SELECT p.nombre, p.precio, COUNT(dv.id) AS veces_vendido
FROM productos p
LEFT JOIN detalle_venta dv ON dv.producto_id = p.id
GROUP BY p.id, p.nombre, p.precio
HAVING COUNT(dv.id) = 0
LIMIT 10;

\echo ''
\echo '── SELF JOIN: Empleados con su jefe ──'
SELECT
    e.nombre || ' ' || e.apellido AS empleado,
    e.puesto,
    COALESCE(j.nombre || ' ' || j.apellido, '(Sin jefe)') AS jefe
FROM empleados e
LEFT JOIN empleados j ON e.jefe_id = j.id
LIMIT 15;

\echo ''
\echo '── JOIN múltiple: Reporte de ventas completo ──'
SELECT
    v.numero_venta,
    cl.nombre || ' ' || cl.apellido AS cliente,
    e.nombre || ' ' || e.apellido AS vendedor,
    s.nombre AS sucursal,
    v.total
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
JOIN empleados e ON v.empleado_id = e.id
JOIN sucursales s ON v.sucursal_id = s.id
WHERE v.estado = 'COMPLETADA'
ORDER BY v.fecha DESC LIMIT 10;
