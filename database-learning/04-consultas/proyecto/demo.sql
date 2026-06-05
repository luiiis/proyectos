-- ════════════════════════════════════════════════════════════════
-- MÓDULO 04: Consultas SELECT - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 04: SELECT - Consultas ═══'

\echo ''
\echo '── Top 5 productos más caros ──'
SELECT nombre, precio FROM productos ORDER BY precio DESC LIMIT 5;

\echo ''
\echo '── Empleados por sucursal ──'
SELECT s.nombre AS sucursal, COUNT(e.id) AS empleados
FROM sucursales s LEFT JOIN empleados e ON e.sucursal_id = s.id
GROUP BY s.nombre ORDER BY empleados DESC;

\echo ''
\echo '── Ventas por método de pago ──'
SELECT metodo_pago, COUNT(*) AS cantidad, ROUND(SUM(total)::numeric, 2) AS monto_total
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY metodo_pago ORDER BY monto_total DESC;

\echo ''
\echo '── Ciudades con más de 80 clientes ──'
SELECT ciudad, COUNT(*) AS total FROM clientes
GROUP BY ciudad HAVING COUNT(*) > 80 ORDER BY total DESC;

\echo ''
\echo '── Estadísticas de salarios ──'
SELECT
    ROUND(AVG(salario)::numeric, 2) AS promedio,
    MIN(salario) AS minimo,
    MAX(salario) AS maximo,
    COUNT(*) AS total_empleados
FROM empleados;
