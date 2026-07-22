-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 05: JOINs Nivel Avanzado (36-50)
-- ════════════════════════════════════════════════════════════════

-- 36. Análisis de cohortes: retención por mes de primera compra
WITH primera_compra AS (
    SELECT cliente_id, DATE_TRUNC('month', MIN(fecha)) AS mes_cohorte
    FROM ventas GROUP BY cliente_id
),
compras_posteriores AS (
    SELECT v.cliente_id, pc.mes_cohorte,
           DATE_TRUNC('month', v.fecha) AS mes_compra
    FROM ventas v
    JOIN primera_compra pc ON pc.cliente_id = v.cliente_id
)
SELECT mes_cohorte::DATE,
       COUNT(DISTINCT cliente_id) AS clientes_cohorte,
       COUNT(DISTINCT CASE WHEN mes_compra = mes_cohorte + INTERVAL '1 month' THEN cliente_id END) AS mes_1,
       COUNT(DISTINCT CASE WHEN mes_compra = mes_cohorte + INTERVAL '2 months' THEN cliente_id END) AS mes_2,
       COUNT(DISTINCT CASE WHEN mes_compra = mes_cohorte + INTERVAL '3 months' THEN cliente_id END) AS mes_3
FROM compras_posteriores
GROUP BY mes_cohorte
ORDER BY mes_cohorte;

-- 37. Productos con tendencia de ventas creciente (últimos 3 meses)
WITH ventas_mensuales AS (
    SELECT p.id, p.nombre,
           DATE_TRUNC('month', v.fecha) AS mes,
           SUM(dv.cantidad) AS vendidos
    FROM productos p
    JOIN detalle_venta dv ON dv.producto_id = p.id
    JOIN ventas v ON dv.venta_id = v.id
    WHERE v.fecha >= CURRENT_DATE - INTERVAL '3 months'
    GROUP BY p.id, p.nombre, DATE_TRUNC('month', v.fecha)
),
con_lag AS (
    SELECT *, LAG(vendidos) OVER (PARTITION BY id ORDER BY mes) AS mes_anterior
    FROM ventas_mensuales
)
SELECT nombre, mes::DATE, vendidos, mes_anterior,
       CASE WHEN vendidos > COALESCE(mes_anterior, 0) THEN '📈 CRECIENDO' ELSE '📉' END AS tendencia
FROM con_lag
WHERE mes_anterior IS NOT NULL AND vendidos > mes_anterior
ORDER BY nombre, mes;

-- 38. Empleados con salario por encima del percentil 75 de su sucursal
WITH percentiles AS (
    SELECT sucursal_id,
           PERCENTILE_CONT(0.75) WITHIN GROUP (ORDER BY salario) AS p75
    FROM empleados WHERE activo = true
    GROUP BY sucursal_id
)
SELECT e.nombre, e.apellido, e.salario, s.nombre AS sucursal,
       ROUND(p.p75, 2) AS percentil_75_sucursal
FROM empleados e
JOIN sucursales s ON e.sucursal_id = s.id
JOIN percentiles p ON p.sucursal_id = e.sucursal_id
WHERE e.salario > p.p75
ORDER BY e.salario DESC;

-- 39. Clientes que compraron TODOS los productos de una categoría
-- (Division relacional - concepto avanzado)
SELECT cl.nombre, cl.apellido, c.nombre AS categoria
FROM clientes cl
CROSS JOIN categorias c
WHERE NOT EXISTS (
    -- Productos de esa categoría que el cliente NO compró
    SELECT p.id FROM productos p
    WHERE p.categoria_id = c.id
    AND NOT EXISTS (
        SELECT 1 FROM ventas v
        JOIN detalle_venta dv ON dv.venta_id = v.id
        WHERE v.cliente_id = cl.id AND dv.producto_id = p.id
    )
)
ORDER BY cl.nombre;

-- 40. Simulación de comisiones: 5% del total vendido
SELECT e.nombre || ' ' || e.apellido AS vendedor,
       e.puesto, e.salario,
       SUM(v.total) AS total_vendido,
       ROUND(SUM(v.total) * 0.05, 2) AS comision_5pct,
       ROUND(e.salario + SUM(v.total) * 0.05, 2) AS ingreso_total
FROM empleados e
JOIN ventas v ON v.empleado_id = e.id
WHERE v.estado = 'COMPLETADA'
  AND v.fecha >= DATE_TRUNC('month', CURRENT_DATE)
GROUP BY e.id, e.nombre, e.apellido, e.puesto, e.salario
ORDER BY comision_5pct DESC;

-- 41. Análisis ABC (Pareto 80/20)
WITH producto_ventas AS (
    SELECT p.nombre, SUM(dv.subtotal) AS total,
           SUM(SUM(dv.subtotal)) OVER (ORDER BY SUM(dv.subtotal) DESC) AS acumulado,
           SUM(SUM(dv.subtotal)) OVER () AS gran_total
    FROM productos p
    JOIN detalle_venta dv ON dv.producto_id = p.id
    GROUP BY p.id, p.nombre
)
SELECT nombre, total,
       ROUND(acumulado / gran_total * 100, 1) AS pct_acumulado,
       CASE
           WHEN acumulado / gran_total <= 0.80 THEN 'A (80% del ingreso)'
           WHEN acumulado / gran_total <= 0.95 THEN 'B (15% del ingreso)'
           ELSE 'C (5% del ingreso)'
       END AS clasificacion_abc
FROM producto_ventas
ORDER BY total DESC;

-- 42. Detección de anomalías: ventas inusualmente altas (>3 desviaciones estándar)
WITH stats AS (
    SELECT AVG(total) AS media, STDDEV(total) AS desv FROM ventas WHERE estado = 'COMPLETADA'
)
SELECT v.numero_venta, v.fecha, v.total,
       cl.nombre || ' ' || cl.apellido AS cliente,
       ROUND((v.total - s.media) / s.desv, 2) AS z_score
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
CROSS JOIN stats s
WHERE v.estado = 'COMPLETADA' AND v.total > s.media + 3 * s.desv
ORDER BY v.total DESC;

-- 43. Cross-selling: clientes que compraron A también compraron B
-- (Quienes compraron producto 1, ¿qué más compraron?)
WITH compradores_producto_1 AS (
    SELECT DISTINCT v.cliente_id
    FROM detalle_venta dv JOIN ventas v ON dv.venta_id = v.id
    WHERE dv.producto_id = 1
)
SELECT p.nombre AS tambien_compraron, COUNT(DISTINCT v.cliente_id) AS clientes
FROM detalle_venta dv
JOIN ventas v ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
WHERE v.cliente_id IN (SELECT cliente_id FROM compradores_producto_1)
  AND dv.producto_id != 1
GROUP BY p.nombre
ORDER BY clientes DESC
LIMIT 10;

-- 44. Forecast: promedio móvil de ventas últimos 3 meses
WITH ventas_diarias AS (
    SELECT DATE(fecha) AS dia, SUM(total) AS total_dia
    FROM ventas WHERE estado = 'COMPLETADA'
    GROUP BY DATE(fecha)
)
SELECT dia, total_dia,
       ROUND(AVG(total_dia) OVER (ORDER BY dia ROWS BETWEEN 89 PRECEDING AND CURRENT ROW), 2) AS promedio_movil_90d,
       ROUND(AVG(total_dia) OVER (ORDER BY dia ROWS BETWEEN 29 PRECEDING AND CURRENT ROW), 2) AS promedio_movil_30d
FROM ventas_diarias
ORDER BY dia DESC LIMIT 30;

-- 45. Rotación de inventario (ventas / stock promedio)
SELECT p.nombre,
       SUM(dv.cantidad) AS vendidos_periodo,
       AVG(i.cantidad) AS stock_promedio,
       ROUND(SUM(dv.cantidad)::NUMERIC / NULLIF(AVG(i.cantidad), 0), 2) AS rotacion
FROM productos p
JOIN detalle_venta dv ON dv.producto_id = p.id
JOIN ventas v ON dv.venta_id = v.id
JOIN inventario i ON i.producto_id = p.id
WHERE v.fecha >= CURRENT_DATE - INTERVAL '3 months'
GROUP BY p.id, p.nombre
ORDER BY rotacion DESC NULLS LAST;

-- 46. Empleados que nunca han vendido a clientes VIP
SELECT e.nombre, e.apellido, e.puesto
FROM empleados e
WHERE e.id NOT IN (
    SELECT DISTINCT v.empleado_id FROM ventas v
    JOIN clientes cl ON v.cliente_id = cl.id
    WHERE cl.tipo = 'VIP'
)
AND e.puesto LIKE '%Vendedor%';

-- 47. Productos que se venden mejor en una sucursal que en otras
WITH ventas_por_sucursal AS (
    SELECT p.nombre AS producto, s.nombre AS sucursal, SUM(dv.cantidad) AS vendidos
    FROM detalle_venta dv
    JOIN ventas v ON dv.venta_id = v.id
    JOIN productos p ON dv.producto_id = p.id
    JOIN sucursales s ON v.sucursal_id = s.id
    GROUP BY p.id, p.nombre, s.id, s.nombre
)
SELECT producto, sucursal, vendidos,
       ROUND(vendidos * 100.0 / SUM(vendidos) OVER (PARTITION BY producto), 1) AS pct_del_total
FROM ventas_por_sucursal
ORDER BY producto, pct_del_total DESC;

-- 48. Productos comprados juntos frecuentemente (market basket)
SELECT p1.nombre AS producto_a, p2.nombre AS producto_b, COUNT(*) AS veces_juntos
FROM detalle_venta dv1
JOIN detalle_venta dv2 ON dv1.venta_id = dv2.venta_id AND dv1.producto_id < dv2.producto_id
JOIN productos p1 ON dv1.producto_id = p1.id
JOIN productos p2 ON dv2.producto_id = p2.id
GROUP BY p1.nombre, p2.nombre
HAVING COUNT(*) > 5
ORDER BY veces_juntos DESC
LIMIT 20;

-- 49. Customer Lifetime Value (CLV)
WITH cliente_stats AS (
    SELECT cl.id, cl.nombre, cl.apellido, cl.tipo,
           COUNT(v.id) AS compras,
           SUM(v.total) AS total_gastado,
           MIN(v.fecha) AS primera_compra,
           MAX(v.fecha) AS ultima_compra,
           EXTRACT(DAYS FROM MAX(v.fecha) - MIN(v.fecha)) / NULLIF(COUNT(v.id) - 1, 0) AS dias_entre_compras
    FROM clientes cl
    JOIN ventas v ON v.cliente_id = cl.id
    WHERE v.estado = 'COMPLETADA'
    GROUP BY cl.id, cl.nombre, cl.apellido, cl.tipo
)
SELECT nombre, apellido, tipo, compras, total_gastado,
       ROUND(total_gastado / compras, 2) AS ticket_promedio,
       ROUND(dias_entre_compras) AS dias_entre_compras,
       -- CLV estimado: ticket_promedio * frecuencia_anual * años_estimados
       ROUND((total_gastado / compras) * (365 / NULLIF(dias_entre_compras, 0)) * 3, 2) AS clv_3_anios
FROM cliente_stats
WHERE compras > 1
ORDER BY clv_3_anios DESC NULLS LAST
LIMIT 20;

-- 50. Dashboard KPIs en una sola consulta
SELECT
    (SELECT COUNT(*) FROM ventas WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE)) AS ventas_mes,
    (SELECT SUM(total) FROM ventas WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE) AND estado = 'COMPLETADA') AS facturacion_mes,
    (SELECT ROUND(AVG(total), 2) FROM ventas WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE)) AS ticket_promedio,
    (SELECT COUNT(DISTINCT cliente_id) FROM ventas WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE)) AS clientes_activos,
    (SELECT COUNT(*) FROM productos p JOIN inventario i ON i.producto_id = p.id WHERE i.cantidad < i.cantidad_minima) AS alertas_stock,
    (SELECT COUNT(*) FROM empleados WHERE activo = true) AS empleados_activos,
    (SELECT nombre || ': $' || SUM(total)::TEXT FROM ventas v JOIN sucursales s ON v.sucursal_id = s.id WHERE v.fecha >= DATE_TRUNC('month', CURRENT_DATE) GROUP BY s.nombre ORDER BY SUM(total) DESC LIMIT 1) AS mejor_sucursal;
