-- ════════════════════════════════════════════════════════════════
-- MÓDULO 07: Subconsultas - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 07: Subconsultas ═══'

\echo ''
\echo '── Escalar: Empleados que ganan más que el promedio ──'
SELECT nombre, apellido, salario
FROM empleados
WHERE salario > (SELECT AVG(salario) FROM empleados)
ORDER BY salario DESC LIMIT 10;

\echo ''
\echo '── IN: Productos nunca vendidos ──'
SELECT nombre, precio FROM productos
WHERE id NOT IN (SELECT DISTINCT producto_id FROM detalle_venta)
LIMIT 10;

\echo ''
\echo '── EXISTS: Clientes que sí han comprado ──'
SELECT nombre, apellido, ciudad FROM clientes cl
WHERE EXISTS (SELECT 1 FROM ventas v WHERE v.cliente_id = cl.id)
LIMIT 10;

\echo ''
\echo '── CTE: Top vendedores con porcentaje del total ──'
WITH ventas_por_empleado AS (
    SELECT empleado_id, SUM(total) AS total_vendido
    FROM ventas WHERE estado = 'COMPLETADA'
    GROUP BY empleado_id
),
gran_total AS (
    SELECT SUM(total_vendido) AS total FROM ventas_por_empleado
)
SELECT e.nombre, e.apellido,
    ROUND(vpe.total_vendido::numeric, 0) AS vendido,
    ROUND((vpe.total_vendido / gt.total * 100)::numeric, 2) AS porcentaje
FROM ventas_por_empleado vpe
JOIN empleados e ON e.id = vpe.empleado_id
CROSS JOIN gran_total gt
ORDER BY vpe.total_vendido DESC LIMIT 10;
