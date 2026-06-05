# Módulo 06: Funciones SQL

## Tipos de Funciones

Las funciones SQL transforman datos. Se dividen en:
- **Texto**: manipular strings
- **Numéricas**: cálculos matemáticos
- **Fecha/Hora**: manipular fechas
- **Agregación**: operar sobre grupos (COUNT, SUM, AVG)
- **Window Functions**: cálculos sobre ventanas de datos

---

## 1. Funciones de Texto

```sql
-- UPPER / LOWER: mayúsculas / minúsculas
SELECT UPPER(nombre) FROM clientes;          -- 'CARLOS'
SELECT LOWER(email) FROM empleados;          -- 'carlos@empresa.com'

-- TRIM: eliminar espacios
SELECT TRIM('  hola  ');                     -- 'hola'
SELECT LTRIM('  hola');                      -- 'hola'
SELECT RTRIM('hola  ');                      -- 'hola'

-- SUBSTRING: extraer parte del texto
SELECT SUBSTRING(nombre FROM 1 FOR 3) FROM clientes;  -- PostgreSQL
SELECT SUBSTRING(nombre, 1, 3) FROM clientes;         -- MySQL
SELECT SUBSTR(nombre, 1, 3) FROM clientes;            -- Oracle

-- LENGTH / CHAR_LENGTH: longitud del texto
SELECT nombre, LENGTH(nombre) AS caracteres FROM productos;

-- CONCAT: unir textos
SELECT CONCAT(nombre, ' ', apellido) AS nombre_completo FROM clientes;
-- PostgreSQL también: nombre || ' ' || apellido

-- REPLACE: reemplazar texto
SELECT REPLACE(telefono, '-', '') FROM clientes;  -- Quitar guiones

-- POSITION / STRPOS: encontrar posición
SELECT POSITION('@' IN email) FROM clientes;  -- Posición de @

-- LEFT / RIGHT: primeros/últimos N caracteres
SELECT LEFT(sku, 3) AS prefijo FROM productos;   -- 'SKU'
SELECT RIGHT(sku, 5) AS sufijo FROM productos;   -- '00001'

-- INITCAP (PostgreSQL): primera letra mayúscula
SELECT INITCAP('hola mundo');  -- 'Hola Mundo'

-- LPAD / RPAD: rellenar con caracteres
SELECT LPAD(id::text, 5, '0') FROM productos;  -- '00001', '00002'
```

---

## 2. Funciones Numéricas

```sql
-- ROUND: redondear
SELECT ROUND(3.14159, 2);     -- 3.14
SELECT ROUND(precio, 0) FROM productos;  -- Sin decimales

-- CEIL / CEILING: redondear hacia arriba
SELECT CEIL(3.1);             -- 4
SELECT CEIL(3.9);             -- 4

-- FLOOR: redondear hacia abajo
SELECT FLOOR(3.9);            -- 3
SELECT FLOOR(3.1);            -- 3

-- ABS: valor absoluto
SELECT ABS(-42);              -- 42

-- MOD: módulo (residuo de división)
SELECT MOD(10, 3);            -- 1 (10 / 3 = 3 residuo 1)

-- POWER: potencia
SELECT POWER(2, 10);          -- 1024

-- SQRT: raíz cuadrada
SELECT SQRT(144);             -- 12

-- RANDOM (PostgreSQL) / RAND() (MySQL): número aleatorio
SELECT RANDOM();              -- 0.7234... (entre 0 y 1)
SELECT FLOOR(RANDOM() * 100) + 1;  -- Entero aleatorio 1-100

-- Cálculos de negocio
SELECT 
    nombre,
    precio,
    costo,
    ROUND(precio - costo, 2) AS ganancia,
    ROUND((precio - costo) / precio * 100, 1) AS margen_porcentaje
FROM productos
WHERE costo IS NOT NULL;
```

---

## 3. Funciones de Fecha y Hora

```sql
-- Fecha/hora actual
SELECT CURRENT_DATE;           -- 2026-05-30
SELECT CURRENT_TIMESTAMP;      -- 2026-05-30 14:30:00
SELECT NOW();                  -- PostgreSQL: igual que CURRENT_TIMESTAMP

-- EXTRACT: extraer parte de una fecha
SELECT EXTRACT(YEAR FROM fecha) FROM ventas;    -- 2024
SELECT EXTRACT(MONTH FROM fecha) FROM ventas;   -- 6
SELECT EXTRACT(DOW FROM fecha) FROM ventas;     -- 0=domingo, 6=sábado

-- DATE_PART (PostgreSQL)
SELECT DATE_PART('year', fecha) FROM ventas;
SELECT DATE_PART('quarter', fecha) FROM ventas;  -- Trimestre (1-4)

-- Aritmética de fechas
SELECT CURRENT_DATE + INTERVAL '30 days';       -- 30 días después
SELECT CURRENT_DATE - INTERVAL '1 year';        -- Hace 1 año
SELECT fecha + INTERVAL '2 hours' FROM ventas;  -- Sumar horas

-- Diferencia entre fechas
SELECT CURRENT_DATE - fecha_ingreso AS dias_trabajando FROM empleados;
-- PostgreSQL: devuelve intervalo
SELECT AGE(CURRENT_DATE, fecha_ingreso) FROM empleados;  -- '3 years 2 months'

-- DATE_TRUNC: truncar a una unidad (PostgreSQL)
SELECT DATE_TRUNC('month', fecha) AS mes, COUNT(*) AS ventas
FROM ventas
GROUP BY DATE_TRUNC('month', fecha)
ORDER BY mes;

-- TO_CHAR: formatear fecha como texto (PostgreSQL/Oracle)
SELECT TO_CHAR(fecha, 'DD/MM/YYYY HH24:MI') FROM ventas;
SELECT TO_CHAR(fecha, 'Month YYYY') FROM ventas;  -- 'January  2024'

-- MySQL equivalentes:
SELECT DATE_FORMAT(fecha, '%d/%m/%Y %H:%i') FROM ventas;
SELECT YEAR(fecha), MONTH(fecha), DAY(fecha) FROM ventas;
```

---

## 4. Funciones Condicionales

```sql
-- CASE WHEN: if/else en SQL
SELECT nombre, salario,
    CASE
        WHEN salario >= 70000 THEN 'Alto'
        WHEN salario >= 40000 THEN 'Medio'
        ELSE 'Bajo'
    END AS nivel_salarial
FROM empleados;

-- COALESCE: primer valor no-null
SELECT nombre, COALESCE(telefono, email, 'Sin contacto') AS contacto
FROM clientes;

-- NULLIF: devuelve NULL si ambos valores son iguales
SELECT NULLIF(descuento, 0) FROM ventas;  -- NULL si descuento es 0

-- GREATEST / LEAST: mayor/menor de una lista
SELECT GREATEST(precio, costo * 2) AS precio_minimo_sugerido FROM productos;
```

---

## 5. Window Functions (Funciones de Ventana)

Las window functions calculan sobre un "grupo" de filas SIN colapsar el resultado (a diferencia de GROUP BY).

```sql
-- ROW_NUMBER: número de fila
SELECT nombre, salario,
    ROW_NUMBER() OVER (ORDER BY salario DESC) AS ranking
FROM empleados;

-- RANK: ranking con empates (salta posiciones)
-- DENSE_RANK: ranking con empates (no salta)
SELECT nombre, salario,
    RANK() OVER (ORDER BY salario DESC) AS rank,
    DENSE_RANK() OVER (ORDER BY salario DESC) AS dense_rank
FROM empleados;

-- PARTITION BY: ranking DENTRO de cada grupo
SELECT nombre, sucursal_id, salario,
    ROW_NUMBER() OVER (PARTITION BY sucursal_id ORDER BY salario DESC) AS rank_en_sucursal
FROM empleados;

-- LAG / LEAD: valor de fila anterior/siguiente
SELECT 
    DATE_TRUNC('month', fecha) AS mes,
    SUM(total) AS ventas_mes,
    LAG(SUM(total)) OVER (ORDER BY DATE_TRUNC('month', fecha)) AS mes_anterior,
    SUM(total) - LAG(SUM(total)) OVER (ORDER BY DATE_TRUNC('month', fecha)) AS diferencia
FROM ventas
GROUP BY DATE_TRUNC('month', fecha)
ORDER BY mes;

-- SUM() OVER: acumulado
SELECT fecha, total,
    SUM(total) OVER (ORDER BY fecha) AS acumulado
FROM ventas
WHERE EXTRACT(YEAR FROM fecha) = 2024
ORDER BY fecha;

-- NTILE: dividir en N grupos iguales
SELECT nombre, salario,
    NTILE(4) OVER (ORDER BY salario) AS cuartil
FROM empleados;
-- cuartil 1 = 25% peor pagado, cuartil 4 = 25% mejor pagado
```

---

## 6. Ejercicios

### Texto
1. Muestra nombre completo de clientes en mayúsculas
2. Extrae el dominio del email de cada empleado (lo que está después de @)
3. Genera un código de cliente: primeras 3 letras del apellido + id con 4 dígitos (ej: GAR-0001)

### Números
4. Calcula el margen de ganancia (%) de cada producto
5. Redondea todos los precios al múltiplo de 100 más cercano
6. Calcula el IVA (16%) y precio final de cada producto

### Fechas
7. ¿Cuántos días lleva cada empleado trabajando?
8. Muestra ventas agrupadas por trimestre
9. ¿Qué día de la semana se vende más?

### Window Functions
10. Ranking de los 3 productos más vendidos por categoría
11. Ventas acumuladas por mes (running total)
12. Diferencia porcentual de ventas mes a mes
13. Percentil de salario de cada empleado dentro de su sucursal
14. Promedio móvil de ventas de los últimos 3 meses

---

## Siguiente Módulo
→ [07-Subconsultas](../07-subconsultas/README.md)
