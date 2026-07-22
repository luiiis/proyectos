-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Ejercicios Básicos B051 a B100 (Funciones)
-- ════════════════════════════════════════════════════════════════

-- ═══ FUNCIONES DE TEXTO ═══

-- B051: Nombre completo concatenado
SELECT nombre || ' ' || apellido AS nombre_completo FROM empleados;

-- B052: Email en minúsculas
SELECT nombre, LOWER(email) AS email_normalizado FROM clientes WHERE email IS NOT NULL;

-- B053: Primera letra del nombre + apellido (username)
SELECT LOWER(LEFT(nombre, 1) || apellido) AS username FROM empleados;

-- B054: Longitud del nombre de productos
SELECT nombre, LENGTH(nombre) AS caracteres FROM productos ORDER BY caracteres DESC LIMIT 10;

-- B055: Nombres que tienen más de 10 caracteres
SELECT nombre FROM productos WHERE LENGTH(nombre) > 10;

-- B056: Reemplazar espacios por guiones en SKU
SELECT sku, REPLACE(sku, ' ', '-') AS sku_limpio FROM productos WHERE sku LIKE '% %';

-- B057: Extraer dominio del email
SELECT email, SPLIT_PART(email, '@', 2) AS dominio FROM clientes WHERE email IS NOT NULL;

-- B058: Nombre con padding para reporte
SELECT RPAD(nombre || ' ' || apellido, 30, '.') || LPAD(salario::TEXT, 10, ' ') AS linea
FROM empleados ORDER BY salario DESC;

-- B059: Invertir nombre (REVERSE)
SELECT nombre, REVERSE(nombre) AS invertido FROM empleados LIMIT 5;

-- B060: Quitar espacios extras
SELECT TRIM(BOTH FROM '  Hola Mundo  ') AS sin_espacios;
SELECT nombre, BTRIM(nombre) AS limpio FROM clientes;

-- ═══ FUNCIONES NUMÉRICAS ═══

-- B061: Redondear precios a enteros
SELECT nombre, precio, ROUND(precio) AS redondeado, CEIL(precio) AS techo, FLOOR(precio) AS piso
FROM productos LIMIT 10;

-- B062: Precio con 16% IVA
SELECT nombre, precio, ROUND(precio * 0.16, 2) AS iva, ROUND(precio * 1.16, 2) AS total
FROM productos;

-- B063: Descuento del 15% para VIP
SELECT p.nombre, p.precio, ROUND(p.precio * 0.85, 2) AS precio_vip
FROM productos p WHERE p.precio > 5000;

-- B064: Módulo (residuo) - productos con id par
SELECT * FROM productos WHERE MOD(id, 2) = 0;

-- B065: Valor absoluto de diferencia entre precio y costo
SELECT nombre, ABS(precio - costo) AS margen_absoluto FROM productos WHERE costo > 0;

-- B066: Potencia y raíz cuadrada
SELECT 2^10 AS potencia, SQRT(144) AS raiz, POWER(3, 4) AS tres_cuarta;

-- B067: Random - seleccionar 5 productos al azar
SELECT nombre, precio FROM productos ORDER BY RANDOM() LIMIT 5;

-- B068: Porcentaje de cada producto sobre total de inventario
SELECT nombre, stock, ROUND(stock * 100.0 / (SELECT SUM(stock) FROM productos), 2) AS pct
FROM productos WHERE stock > 0 ORDER BY pct DESC;

-- B069: Rango de precios (max - min) por categoría
SELECT c.nombre, MAX(p.precio) - MIN(p.precio) AS rango_precio
FROM categorias c JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre ORDER BY rango_precio DESC;

-- B070: Truncar decimales (sin redondear)
SELECT TRUNC(3.99) AS truncado, TRUNC(precio, 0) AS precio_truncado FROM productos LIMIT 5;

-- ═══ FUNCIONES DE FECHA ═══

-- B071: Fecha actual en diferentes formatos
SELECT NOW(), CURRENT_DATE, CURRENT_TIME, CURRENT_TIMESTAMP;

-- B072: Extraer partes de una fecha
SELECT fecha, EXTRACT(YEAR FROM fecha) AS anio,
       EXTRACT(MONTH FROM fecha) AS mes, EXTRACT(DAY FROM fecha) AS dia,
       EXTRACT(DOW FROM fecha) AS dia_semana  -- 0=domingo
FROM ventas LIMIT 5;

-- B073: Formatear fecha en español
SELECT fecha, TO_CHAR(fecha, 'DD "de" Month "de" YYYY') AS fecha_espanol
FROM ventas LIMIT 5;

-- B074: Antigüedad en años y meses
SELECT nombre, fecha_ingreso, AGE(fecha_ingreso) AS antiguedad,
       EXTRACT(YEAR FROM AGE(fecha_ingreso))::INT AS anios,
       EXTRACT(MONTH FROM AGE(fecha_ingreso))::INT AS meses
FROM empleados ORDER BY fecha_ingreso;

-- B075: Ventas de los últimos 7 días
SELECT * FROM ventas WHERE fecha >= CURRENT_DATE - INTERVAL '7 days';

-- B076: Primer y último día del mes actual
SELECT DATE_TRUNC('month', CURRENT_DATE) AS primer_dia,
       (DATE_TRUNC('month', CURRENT_DATE) + INTERVAL '1 month - 1 day')::DATE AS ultimo_dia;

-- B077: Diferencia en días entre dos fechas
SELECT numero_venta, fecha,
       CURRENT_DATE - fecha::DATE AS dias_transcurridos
FROM ventas ORDER BY fecha DESC LIMIT 10;

-- B078: Empleados que cumplen años este mes
SELECT nombre, apellido, fecha_nacimiento
FROM clientes
WHERE EXTRACT(MONTH FROM fecha_nacimiento) = EXTRACT(MONTH FROM CURRENT_DATE);

-- B079: Sumar 30 días a la fecha de venta (fecha de vencimiento)
SELECT numero_venta, fecha, fecha + INTERVAL '30 days' AS vencimiento FROM ventas LIMIT 10;

-- B080: Truncar a inicio de semana/mes/año
SELECT fecha,
       DATE_TRUNC('week', fecha)::DATE AS inicio_semana,
       DATE_TRUNC('month', fecha)::DATE AS inicio_mes,
       DATE_TRUNC('year', fecha)::DATE AS inicio_anio
FROM ventas LIMIT 5;

-- ═══ FUNCIONES DE AGREGACIÓN AVANZADAS ═══

-- B081: Contar, sumar, promediar ventas por mes
SELECT TO_CHAR(fecha, 'YYYY-MM') AS mes, COUNT(*) AS ventas,
       SUM(total) AS total, ROUND(AVG(total), 2) AS promedio
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY TO_CHAR(fecha, 'YYYY-MM') ORDER BY mes DESC;

-- B082: String aggregation (listar productos por categoría)
SELECT c.nombre, STRING_AGG(p.nombre, ', ' ORDER BY p.nombre) AS productos
FROM categorias c JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre;

-- B083: BOOL_AND / BOOL_OR
SELECT sucursal_id, BOOL_AND(activo) AS todos_activos, BOOL_OR(NOT activo) AS alguno_inactivo
FROM empleados GROUP BY sucursal_id;

-- B084: Array aggregation
SELECT c.nombre, ARRAY_AGG(DISTINCT p.nombre ORDER BY p.nombre) AS productos_array
FROM categorias c JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre;

-- B085: Estadísticas completas
SELECT c.nombre AS categoria,
       COUNT(*) AS productos,
       ROUND(AVG(p.precio), 2) AS promedio,
       ROUND(STDDEV(p.precio), 2) AS desviacion,
       PERCENTILE_CONT(0.5) WITHIN GROUP (ORDER BY p.precio) AS mediana
FROM categorias c JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre;

-- ═══ CASE, COALESCE, NULLIF ═══

-- B086: Clasificar ventas por monto
SELECT numero_venta, total,
       CASE
           WHEN total >= 50000 THEN 'GRANDE'
           WHEN total >= 10000 THEN 'MEDIANA'
           ELSE 'PEQUEÑA'
       END AS clasificacion
FROM ventas ORDER BY total DESC;

-- B087: Contar ventas por clasificación
SELECT
    COUNT(CASE WHEN total >= 50000 THEN 1 END) AS grandes,
    COUNT(CASE WHEN total BETWEEN 10000 AND 49999 THEN 1 END) AS medianas,
    COUNT(CASE WHEN total < 10000 THEN 1 END) AS pequenas
FROM ventas WHERE estado = 'COMPLETADA';

-- B088: COALESCE para manejar NULLs
SELECT nombre, COALESCE(telefono, 'Sin teléfono') AS telefono,
       COALESCE(email, 'Sin email') AS email
FROM clientes;

-- B089: NULLIF para evitar división por cero
SELECT nombre, total, descuento,
       ROUND(descuento / NULLIF(total, 0) * 100, 2) AS pct_descuento
FROM ventas WHERE descuento > 0;

-- B090: Pivot con CASE (ventas por mes en columnas)
SELECT
    EXTRACT(YEAR FROM fecha) AS anio,
    SUM(CASE WHEN EXTRACT(MONTH FROM fecha) = 1 THEN total ELSE 0 END) AS enero,
    SUM(CASE WHEN EXTRACT(MONTH FROM fecha) = 2 THEN total ELSE 0 END) AS febrero,
    SUM(CASE WHEN EXTRACT(MONTH FROM fecha) = 3 THEN total ELSE 0 END) AS marzo,
    SUM(CASE WHEN EXTRACT(MONTH FROM fecha) = 4 THEN total ELSE 0 END) AS abril,
    SUM(CASE WHEN EXTRACT(MONTH FROM fecha) = 5 THEN total ELSE 0 END) AS mayo,
    SUM(CASE WHEN EXTRACT(MONTH FROM fecha) = 6 THEN total ELSE 0 END) AS junio
FROM ventas WHERE estado = 'COMPLETADA'
GROUP BY EXTRACT(YEAR FROM fecha);

-- ═══ CONVERSIONES Y CAST ═══

-- B091: Número a texto formateado
SELECT nombre, TO_CHAR(precio, 'FM$999,999.00') AS precio_formato FROM productos LIMIT 10;

-- B092: Texto a número
SELECT '42'::INTEGER + 8 AS resultado;
SELECT CAST('3.14' AS NUMERIC) * 2 AS doble_pi;

-- B093: Fecha a texto en formato personalizado
SELECT TO_CHAR(NOW(), 'Day DD "de" Month YYYY, HH24:MI:SS') AS ahora_formateado;

-- B094: Texto a fecha
SELECT TO_DATE('21/07/2026', 'DD/MM/YYYY') AS fecha_parseada;

-- B095: Generar JSON desde columnas
SELECT jsonb_build_object(
    'id', id, 'nombre', nombre, 'precio', precio, 'stock', stock
) AS producto_json
FROM productos LIMIT 5;

-- ═══ MISCELÁNEOS ═══

-- B096: GENERATE_SERIES (generar datos)
SELECT generate_series(1, 12) AS mes;  -- Meses del 1 al 12
SELECT generate_series('2026-01-01'::DATE, '2026-12-31'::DATE, '1 month') AS meses_2026;

-- B097: EXISTS como boolean
SELECT nombre, EXISTS(SELECT 1 FROM ventas WHERE cliente_id = c.id) AS ha_comprado
FROM clientes c LIMIT 10;

-- B098: GREATEST / LEAST
SELECT nombre, precio, costo, GREATEST(precio, costo) AS mayor, LEAST(precio, costo) AS menor
FROM productos WHERE costo > 0 LIMIT 10;

-- B099: Concatenar con separador NULL-safe
SELECT CONCAT_WS(' | ', nombre, apellido, ciudad, tipo) AS info_completa FROM clientes LIMIT 10;

-- B100: Resumen completo de la BD
SELECT
    (SELECT COUNT(*) FROM empleados WHERE activo) AS empleados,
    (SELECT COUNT(*) FROM clientes WHERE activo) AS clientes,
    (SELECT COUNT(*) FROM productos WHERE activo) AS productos,
    (SELECT COUNT(*) FROM ventas) AS ventas_total,
    (SELECT SUM(total) FROM ventas WHERE estado = 'COMPLETADA') AS facturacion_total;
