-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 08: Vistas (Views)
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. Vista básica: productos activos con categoría ═══

CREATE OR REPLACE VIEW v_productos_catalogo AS
SELECT p.id, p.nombre, p.precio, p.stock, p.sku,
       c.nombre AS categoria, pr.nombre AS proveedor,
       p.activo
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id
LEFT JOIN proveedores pr ON p.proveedor_id = pr.id
WHERE p.activo = true;

-- Uso:
-- SELECT * FROM v_productos_catalogo WHERE precio > 5000;
-- SELECT * FROM v_productos_catalogo WHERE categoria = 'Electrónica';

-- ═══ 2. Vista de seguridad: empleados sin datos sensibles ═══

CREATE OR REPLACE VIEW v_empleados_publico AS
SELECT id, nombre, apellido, puesto, sucursal_id, fecha_ingreso, activo
FROM empleados;
-- Oculta: salario, email, teléfono
-- Dar acceso: GRANT SELECT ON v_empleados_publico TO rol_consulta;

-- ═══ 3. Vista: resumen de ventas por día ═══

CREATE OR REPLACE VIEW v_ventas_diarias AS
SELECT DATE(fecha) AS dia,
       COUNT(*) AS num_ventas,
       SUM(total) AS monto_total,
       ROUND(AVG(total), 2) AS ticket_promedio,
       MIN(total) AS venta_minima,
       MAX(total) AS venta_maxima
FROM ventas
WHERE estado = 'COMPLETADA'
GROUP BY DATE(fecha)
ORDER BY dia DESC;

-- Uso:
-- SELECT * FROM v_ventas_diarias LIMIT 30;
-- SELECT * FROM v_ventas_diarias WHERE dia >= '2026-07-01';

-- ═══ 4. Vista: dashboard KPIs del mes ═══

CREATE OR REPLACE VIEW v_dashboard_mes AS
SELECT
    COUNT(*) AS ventas_mes,
    SUM(total) AS facturacion,
    ROUND(AVG(total), 2) AS ticket_promedio,
    COUNT(DISTINCT cliente_id) AS clientes_activos,
    (SELECT COUNT(*) FROM inventario WHERE cantidad < cantidad_minima) AS alertas_stock,
    (SELECT COUNT(*) FROM empleados WHERE activo = true) AS empleados_activos
FROM ventas
WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE)
AND estado = 'COMPLETADA';

-- Uso:
-- SELECT * FROM v_dashboard_mes;

-- ═══ 5. Vista: ranking de vendedores ═══

CREATE OR REPLACE VIEW v_ranking_vendedores AS
SELECT
    e.id AS empleado_id,
    e.nombre || ' ' || e.apellido AS vendedor,
    e.puesto,
    s.nombre AS sucursal,
    COUNT(v.id) AS num_ventas,
    SUM(v.total) AS total_vendido,
    ROUND(AVG(v.total), 2) AS ticket_promedio,
    RANK() OVER (ORDER BY SUM(v.total) DESC) AS ranking
FROM empleados e
JOIN ventas v ON v.empleado_id = e.id
JOIN sucursales s ON e.sucursal_id = s.id
WHERE v.estado = 'COMPLETADA'
AND v.fecha >= DATE_TRUNC('month', CURRENT_DATE)
GROUP BY e.id, e.nombre, e.apellido, e.puesto, s.nombre;

-- Uso:
-- SELECT * FROM v_ranking_vendedores WHERE ranking <= 10;

-- ═══ 6. Vista: clientes con métricas ═══

CREATE OR REPLACE VIEW v_clientes_metricas AS
SELECT
    cl.id, cl.nombre, cl.apellido, cl.email, cl.tipo, cl.ciudad,
    COUNT(v.id) AS total_compras,
    COALESCE(SUM(v.total), 0) AS monto_total,
    ROUND(COALESCE(AVG(v.total), 0), 2) AS ticket_promedio,
    MAX(v.fecha) AS ultima_compra,
    CURRENT_DATE - MAX(v.fecha)::DATE AS dias_sin_comprar
FROM clientes cl
LEFT JOIN ventas v ON v.cliente_id = cl.id AND v.estado = 'COMPLETADA'
WHERE cl.activo = true
GROUP BY cl.id, cl.nombre, cl.apellido, cl.email, cl.tipo, cl.ciudad;

-- Uso:
-- SELECT * FROM v_clientes_metricas WHERE dias_sin_comprar > 90;  -- Inactivos
-- SELECT * FROM v_clientes_metricas ORDER BY monto_total DESC LIMIT 10;  -- Mejores

-- ═══ 7. Vista: inventario consolidado ═══

CREATE OR REPLACE VIEW v_inventario_consolidado AS
SELECT
    p.nombre AS producto, p.sku, c.nombre AS categoria,
    SUM(i.cantidad) AS stock_total,
    SUM(i.cantidad_minima) AS minimo_total,
    COUNT(DISTINCT i.sucursal_id) AS en_sucursales,
    CASE
        WHEN SUM(i.cantidad) = 0 THEN 'AGOTADO'
        WHEN SUM(i.cantidad) < SUM(i.cantidad_minima) THEN 'BAJO'
        ELSE 'OK'
    END AS estado_stock
FROM productos p
JOIN inventario i ON i.producto_id = p.id
JOIN categorias c ON p.categoria_id = c.id
WHERE p.activo = true
GROUP BY p.id, p.nombre, p.sku, c.nombre
ORDER BY estado_stock, p.nombre;

-- ═══ 8. MATERIALIZED VIEW: reporte mensual (datos pre-calculados) ═══

CREATE MATERIALIZED VIEW mv_reporte_mensual AS
SELECT
    DATE_TRUNC('month', v.fecha)::DATE AS mes,
    s.nombre AS sucursal,
    COUNT(v.id) AS ventas,
    SUM(v.total) AS facturacion,
    ROUND(AVG(v.total), 2) AS ticket_promedio,
    COUNT(DISTINCT v.cliente_id) AS clientes_unicos
FROM ventas v
JOIN sucursales s ON v.sucursal_id = s.id
WHERE v.estado = 'COMPLETADA'
GROUP BY DATE_TRUNC('month', v.fecha), s.nombre
ORDER BY mes DESC, facturacion DESC;

-- Crear índice en la materialized view (para queries rápidas)
CREATE UNIQUE INDEX idx_mv_reporte_mes_suc ON mv_reporte_mensual(mes, sucursal);

-- Refrescar datos (ejecutar periódicamente, ej: cada hora):
-- REFRESH MATERIALIZED VIEW CONCURRENTLY mv_reporte_mensual;

-- Uso:
-- SELECT * FROM mv_reporte_mensual WHERE mes >= '2026-01-01';
-- Es INSTANTÁNEO porque los datos ya están calculados

-- ═══ 9. Vista: comparación mes actual vs anterior ═══

CREATE OR REPLACE VIEW v_comparacion_mensual AS
WITH mes_actual AS (
    SELECT sucursal_id, SUM(total) AS total
    FROM ventas
    WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE) AND estado = 'COMPLETADA'
    GROUP BY sucursal_id
),
mes_anterior AS (
    SELECT sucursal_id, SUM(total) AS total
    FROM ventas
    WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
      AND fecha < DATE_TRUNC('month', CURRENT_DATE) AND estado = 'COMPLETADA'
    GROUP BY sucursal_id
)
SELECT s.nombre AS sucursal,
       COALESCE(ma.total, 0) AS mes_actual,
       COALESCE(mp.total, 0) AS mes_anterior,
       COALESCE(ma.total, 0) - COALESCE(mp.total, 0) AS diferencia,
       CASE WHEN COALESCE(mp.total, 0) = 0 THEN 0
            ELSE ROUND((COALESCE(ma.total, 0) - mp.total) / mp.total * 100, 1)
       END AS variacion_pct
FROM sucursales s
LEFT JOIN mes_actual ma ON ma.sucursal_id = s.id
LEFT JOIN mes_anterior mp ON mp.sucursal_id = s.id
WHERE s.activa = true
ORDER BY variacion_pct DESC;

-- ═══ 10. Vista updatable: clientes (se puede INSERT/UPDATE) ═══

CREATE OR REPLACE VIEW v_clientes_editable AS
SELECT id, nombre, apellido, email, telefono, ciudad, tipo
FROM clientes
WHERE activo = true;

-- Esto funciona porque es una vista simple (1 tabla, sin GROUP BY):
-- INSERT INTO v_clientes_editable (nombre, apellido, email, tipo) VALUES ('Test', 'Vista', 'test@v.com', 'REGULAR');
-- UPDATE v_clientes_editable SET ciudad = 'CDMX' WHERE id = 1;
