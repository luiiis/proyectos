-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 06: Funciones SQL
-- ════════════════════════════════════════════════════════════════

-- ═══ FUNCIONES DE TEXTO ═══

-- 1. Nombre completo en formato "APELLIDO, Nombre"
SELECT UPPER(apellido) || ', ' || INITCAP(nombre) AS nombre_formal FROM empleados;

-- 2. Email generado automáticamente
SELECT nombre, apellido,
       LOWER(LEFT(nombre, 1) || apellido || '@empresa.com') AS email_generado
FROM empleados;

-- 3. Primeros 20 caracteres de descripción + '...'
SELECT nombre, LEFT(descripcion, 20) || '...' AS resumen FROM productos WHERE descripcion IS NOT NULL;

-- 4. Reemplazar texto
SELECT nombre, REPLACE(nombre, 'HP', 'Hewlett-Packard') AS nombre_completo
FROM productos WHERE nombre LIKE '%HP%';

-- 5. Longitud del nombre + padding
SELECT nombre, LENGTH(nombre) AS largo, LPAD(nombre, 30, '.') AS con_padding FROM productos LIMIT 10;

-- ═══ FUNCIONES NUMÉRICAS ═══

-- 6. Precio con IVA redondeado
SELECT nombre, precio, ROUND(precio * 1.16, 2) AS con_iva,
       CEIL(precio * 1.16) AS redondeado_arriba
FROM productos LIMIT 10;

-- 7. Porcentaje de cada categoría sobre el total
SELECT c.nombre, COUNT(p.id) AS productos,
       ROUND(COUNT(p.id) * 100.0 / (SELECT COUNT(*) FROM productos), 2) AS porcentaje
FROM categorias c
JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre ORDER BY porcentaje DESC;

-- 8. Estadísticas de salario
SELECT puesto,
       COUNT(*) AS empleados,
       ROUND(AVG(salario), 2) AS promedio,
       MIN(salario) AS minimo,
       MAX(salario) AS maximo,
       ROUND(STDDEV(salario), 2) AS desviacion
FROM empleados GROUP BY puesto ORDER BY promedio DESC;

-- ═══ FUNCIONES DE FECHA ═══

-- 9. Antigüedad de empleados
SELECT nombre, apellido, fecha_ingreso,
       AGE(fecha_ingreso) AS antiguedad,
       EXTRACT(YEAR FROM AGE(fecha_ingreso)) AS anios,
       EXTRACT(MONTH FROM AGE(fecha_ingreso)) AS meses
FROM empleados ORDER BY fecha_ingreso;

-- 10. Ventas agrupadas por trimestre
SELECT EXTRACT(YEAR FROM fecha) AS anio,
       EXTRACT(QUARTER FROM fecha) AS trimestre,
       COUNT(*) AS ventas, SUM(total) AS monto
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY anio, trimestre ORDER BY anio, trimestre;

-- 11. Día de la semana con más ventas
SELECT TO_CHAR(fecha, 'Day') AS dia_semana,
       EXTRACT(DOW FROM fecha) AS dia_num,
       COUNT(*) AS ventas
FROM ventas GROUP BY dia_semana, dia_num ORDER BY dia_num;

-- 12. Diferencia de días entre compras de un cliente
SELECT cl.nombre, v.fecha,
       v.fecha - LAG(v.fecha) OVER (PARTITION BY cl.id ORDER BY v.fecha) AS dias_desde_anterior
FROM ventas v JOIN clientes cl ON v.cliente_id = cl.id
WHERE cl.id = 1 ORDER BY v.fecha;

-- ═══ FUNCIONES CONDICIONALES ═══

-- 13. CASE: Clasificar empleados por salario
SELECT nombre, salario,
       CASE
           WHEN salario >= 60000 THEN 'Senior'
           WHEN salario >= 40000 THEN 'Mid'
           WHEN salario >= 25000 THEN 'Junior'
           ELSE 'Trainee'
       END AS nivel
FROM empleados ORDER BY salario DESC;

-- 14. CASE: Clasificar productos por rotación
SELECT p.nombre, COALESCE(SUM(dv.cantidad), 0) AS vendidos,
       CASE
           WHEN SUM(dv.cantidad) IS NULL THEN 'Sin ventas'
           WHEN SUM(dv.cantidad) > 100 THEN 'Alta rotación'
           WHEN SUM(dv.cantidad) > 20 THEN 'Media rotación'
           ELSE 'Baja rotación'
       END AS clasificacion
FROM productos p
LEFT JOIN detalle_venta dv ON dv.producto_id = p.id
GROUP BY p.id, p.nombre
ORDER BY vendidos DESC;

-- 15. COALESCE: Manejar NULLs elegantemente
SELECT nombre,
       COALESCE(telefono, email, 'Sin contacto') AS contacto_principal,
       COALESCE(direccion, ciudad, 'Dirección no registrada') AS ubicacion
FROM clientes;

-- ═══ FUNCIONES DE VENTANA (Window Functions) ═══

-- 16. Ranking de productos por precio dentro de su categoría
SELECT c.nombre AS categoria, p.nombre, p.precio,
       RANK() OVER (PARTITION BY c.id ORDER BY p.precio DESC) AS ranking_en_categoria,
       DENSE_RANK() OVER (ORDER BY p.precio DESC) AS ranking_global
FROM productos p
JOIN categorias c ON p.categoria_id = c.id
ORDER BY c.nombre, ranking_en_categoria;

-- 17. Promedio móvil de ventas (últimos 7 días)
SELECT DATE(fecha) AS dia,
       SUM(total) AS venta_dia,
       ROUND(AVG(SUM(total)) OVER (ORDER BY DATE(fecha) ROWS BETWEEN 6 PRECEDING AND CURRENT ROW), 2) AS promedio_7d
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY DATE(fecha) ORDER BY dia DESC LIMIT 30;

-- 18. Porcentaje del total con window function
SELECT s.nombre, SUM(v.total) AS total_sucursal,
       ROUND(SUM(v.total) * 100.0 / SUM(SUM(v.total)) OVER (), 2) AS pct_del_total
FROM ventas v
JOIN sucursales s ON v.sucursal_id = s.id
WHERE v.estado = 'COMPLETADA'
GROUP BY s.nombre ORDER BY total_sucursal DESC;

-- 19. ROW_NUMBER vs RANK vs DENSE_RANK
SELECT nombre, salario,
       ROW_NUMBER() OVER (ORDER BY salario DESC) AS row_num,   -- siempre consecutivo
       RANK() OVER (ORDER BY salario DESC) AS rank,            -- gaps si hay empates
       DENSE_RANK() OVER (ORDER BY salario DESC) AS dense_rank -- sin gaps
FROM empleados ORDER BY salario DESC LIMIT 15;

-- 20. Primera y última venta de cada cliente
SELECT DISTINCT cl.nombre, cl.apellido,
       FIRST_VALUE(v.fecha) OVER (PARTITION BY cl.id ORDER BY v.fecha) AS primera_compra,
       LAST_VALUE(v.fecha) OVER (PARTITION BY cl.id ORDER BY v.fecha
           ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING) AS ultima_compra,
       COUNT(v.id) OVER (PARTITION BY cl.id) AS total_compras
FROM clientes cl
JOIN ventas v ON v.cliente_id = cl.id
ORDER BY cl.nombre;
