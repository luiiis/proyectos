-- ════════════════════════════════════════════════════════════════
-- MÓDULO 06 - EJERCICIO 1: Funciones SQL
-- ════════════════════════════════════════════════════════════════

-- ═══════ FUNCIONES DE TEXTO ═══════

-- 6.1: Muestra nombre completo de empleados en MAYÚSCULAS
--      Formato: "GARCÍA LÓPEZ, CARLOS"
-- TU QUERY:


-- 6.2: Extrae el DOMINIO del email de cada empleado (lo que está después de @)
--      Ejemplo: "carlos@empresa.com" → "empresa.com"
--      PISTA: SUBSTRING(email FROM POSITION('@' IN email) + 1)
-- TU QUERY:


-- 6.3: Genera un código de cliente: primeras 3 letras del apellido (mayúsculas) + '-' + id con 4 dígitos
--      Ejemplo: cliente id=7, apellido="García" → "GAR-0007"
--      PISTA: UPPER(LEFT(apellido, 3)) || '-' || LPAD(id::text, 4, '0')
-- TU QUERY:


-- 6.4: Limpia los teléfonos: quita guiones y espacios, deja solo números
--      "55-1234-5678" → "5512345678"
--      PISTA: REPLACE(REPLACE(telefono, '-', ''), ' ', '')
-- TU QUERY:


-- 6.5: Muestra productos cuyo nombre tiene más de 20 caracteres, 
--      truncado a 20 chars + "..."
--      Ejemplo: "Monitor Ultrawide 34 pulgadas" → "Monitor Ultrawide 34..."
-- TU QUERY:


-- ═══════ FUNCIONES NUMÉRICAS ═══════

-- 6.6: Calcula el margen de ganancia (%) de cada producto: ((precio - costo) / precio) * 100
--      Redondea a 1 decimal. Solo productos que tienen costo registrado.
--      Ordena por margen DESC
-- TU QUERY:


-- 6.7: Redondea todos los precios al múltiplo de 100 más cercano
--      Ejemplo: 18999.99 → 19000, 1234.56 → 1200
--      PISTA: ROUND(precio / 100) * 100
-- TU QUERY:


-- 6.8: Calcula para cada producto: precio sin IVA, IVA (16%), precio con IVA
--      Columnas: nombre, precio_base, iva, precio_final
-- TU QUERY:


-- ═══════ FUNCIONES DE FECHA ═══════

-- 6.9: ¿Cuántos DÍAS lleva cada empleado trabajando? Ordena por antigüedad DESC
--      Columnas: nombre, fecha_ingreso, dias_trabajando, años_aproximados
-- TU QUERY:


-- 6.10: Muestra ventas agrupadas por TRIMESTRE del año 2024
--       Columnas: trimestre (Q1, Q2, Q3, Q4), total_ventas, monto_total
--       PISTA: EXTRACT(QUARTER FROM fecha)
-- TU QUERY:


-- 6.11: ¿Qué DÍA DE LA SEMANA se vende más?
--       Columnas: dia_semana (Lunes, Martes...), total_ventas, monto_promedio
--       PISTA: TO_CHAR(fecha, 'Day') o EXTRACT(DOW FROM fecha)
-- TU QUERY:


-- 6.12: Muestra empleados que cumplen años este mes
--       PISTA: EXTRACT(MONTH FROM fecha_nacimiento) = EXTRACT(MONTH FROM CURRENT_DATE)
--       Nota: usa la tabla clientes que sí tiene fecha_nacimiento
-- TU QUERY:


-- ═══════ FUNCIONES CONDICIONALES ═══════

-- 6.13: Clasifica productos por rango de precio usando CASE:
--       'Económico' (< 1000), 'Medio' (1000-5000), 'Premium' (5000-20000), 'Lujo' (> 20000)
--       Muestra cuántos productos hay en cada rango
-- TU QUERY:


-- 6.14: Muestra ventas con el nombre del estado en español y un emoji:
--       COMPLETADA → '✅ Completada', PENDIENTE → '⏳ Pendiente', 
--       CANCELADA → '❌ Cancelada', DEVUELTA → '↩️ Devuelta'
-- TU QUERY:


-- ═══════ WINDOW FUNCTIONS ═══════

-- 6.15: Ranking de empleados por salario DENTRO de cada sucursal
--       Columnas: sucursal, nombre, salario, ranking_en_sucursal
--       PISTA: ROW_NUMBER() OVER (PARTITION BY sucursal_id ORDER BY salario DESC)
-- TU QUERY:


-- 6.16: Para cada venta, muestra el total acumulado del mes (running total)
--       Columnas: fecha, numero_venta, total, acumulado_mes
--       Solo ventas de enero 2024
--       PISTA: SUM(total) OVER (ORDER BY fecha)
-- TU QUERY:


-- 6.17: Muestra ventas mensuales con la diferencia vs mes anterior
--       Columnas: mes, total_mes, mes_anterior, diferencia, crecimiento_pct
--       PISTA: LAG(SUM(total)) OVER (ORDER BY mes)
-- TU QUERY:


-- 6.18: Divide empleados en 4 cuartiles por salario y muestra el rango de cada cuartil
--       Columnas: nombre, salario, cuartil
--       PISTA: NTILE(4) OVER (ORDER BY salario)
-- TU QUERY:


-- 6.19: Para cada producto, muestra su precio y el precio promedio de su categoría
--       Columnas: nombre, precio, precio_promedio_categoria, diferencia_vs_promedio
--       PISTA: AVG(precio) OVER (PARTITION BY categoria_id)
-- TU QUERY:


-- 6.20: Top 3 productos más vendidos por categoría (sin subconsultas)
--       PISTA: ROW_NUMBER() OVER (PARTITION BY categoria_id ORDER BY total_vendido DESC)
--       Luego filtrar WHERE ranking <= 3
-- TU QUERY:

