-- ════════════════════════════════════════════════════════════════
-- MÓDULO 06: Funciones SQL - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 06: Funciones SQL ═══'

\echo ''
\echo '── Funciones de texto ──'
SELECT
    nombre,
    UPPER(nombre) AS mayusculas,
    LEFT(nombre, 10) || '...' AS truncado,
    LENGTH(nombre) AS longitud
FROM productos LIMIT 5;

\echo ''
\echo '── Funciones numéricas: Margen de ganancia ──'
SELECT nombre, precio, costo,
    ROUND((precio - costo), 2) AS ganancia,
    ROUND((precio - costo) / precio * 100, 1) AS margen_pct
FROM productos
WHERE costo IS NOT NULL AND costo > 0
ORDER BY margen_pct DESC LIMIT 10;

\echo ''
\echo '── Funciones de fecha: Antigüedad de empleados ──'
SELECT nombre, apellido, fecha_ingreso,
    AGE(CURRENT_DATE, fecha_ingreso) AS antiguedad,
    EXTRACT(YEAR FROM AGE(CURRENT_DATE, fecha_ingreso)) AS años
FROM empleados ORDER BY fecha_ingreso LIMIT 10;

\echo ''
\echo '── Window Functions: Ranking por sucursal ──'
SELECT nombre, apellido, salario, sucursal_id,
    ROW_NUMBER() OVER (PARTITION BY sucursal_id ORDER BY salario DESC) AS ranking
FROM empleados
WHERE sucursal_id IS NOT NULL
ORDER BY sucursal_id, ranking
LIMIT 15;

\echo ''
\echo '── Window: Ventas acumuladas por mes ──'
SELECT
    DATE_TRUNC('month', fecha)::date AS mes,
    COUNT(*) AS ventas,
    ROUND(SUM(total)::numeric, 0) AS total_mes,
    ROUND(SUM(SUM(total)) OVER (ORDER BY DATE_TRUNC('month', fecha))::numeric, 0) AS acumulado
FROM ventas WHERE estado = 'COMPLETADA' AND EXTRACT(YEAR FROM fecha) = 2024
GROUP BY DATE_TRUNC('month', fecha)
ORDER BY mes;
