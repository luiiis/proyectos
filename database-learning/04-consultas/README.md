# Módulo 04: Consultas SQL (SELECT)

## El comando más importante de SQL
SELECT es el 80% de lo que harás como desarrollador. Dominar SELECT = dominar SQL.

---

## 1. SELECT Básico

```sql
-- Todas las columnas
SELECT * FROM productos;

-- Columnas específicas (SIEMPRE preferir esto sobre *)
SELECT nombre, precio, stock FROM productos;

-- Alias (renombrar columnas en el resultado)
SELECT 
    nombre AS producto,
    precio AS precio_unitario,
    precio * 1.16 AS precio_con_iva
FROM productos;
```

---

## 2. WHERE - Filtrar Resultados

### Operadores de comparación:
```sql
-- Igual
SELECT * FROM empleados WHERE puesto = 'Vendedor';

-- Diferente
SELECT * FROM productos WHERE categoria_id != 3;
-- También: <> (estándar SQL)

-- Mayor, menor
SELECT * FROM productos WHERE precio > 10000;
SELECT * FROM empleados WHERE salario <= 40000;

-- BETWEEN (rango inclusivo)
SELECT * FROM productos WHERE precio BETWEEN 1000 AND 5000;
-- Equivale a: precio >= 1000 AND precio <= 5000

-- IN (lista de valores)
SELECT * FROM clientes WHERE ciudad IN ('Monterrey', 'Guadalajara', 'Puebla');

-- LIKE (patrones de texto)
SELECT * FROM productos WHERE nombre LIKE 'Laptop%';     -- Empieza con "Laptop"
SELECT * FROM productos WHERE nombre LIKE '%Pro%';       -- Contiene "Pro"
SELECT * FROM productos WHERE nombre LIKE '___-%';       -- 3 chars + guión + algo
-- % = cualquier cantidad de caracteres
-- _ = exactamente 1 carácter

-- ILIKE (PostgreSQL: LIKE sin importar mayúsculas/minúsculas)
SELECT * FROM productos WHERE nombre ILIKE '%laptop%';

-- IS NULL / IS NOT NULL
SELECT * FROM empleados WHERE jefe_id IS NULL;  -- Empleados sin jefe
SELECT * FROM clientes WHERE email IS NOT NULL; -- Clientes con email
```

### Operadores lógicos:
```sql
-- AND (ambas condiciones deben cumplirse)
SELECT * FROM productos WHERE precio > 5000 AND categoria_id = 1;

-- OR (al menos una condición)
SELECT * FROM empleados WHERE puesto = 'Gerente Sucursal' OR puesto = 'Director General';

-- NOT (negar condición)
SELECT * FROM clientes WHERE NOT ciudad = 'Ciudad de México';
SELECT * FROM productos WHERE categoria_id NOT IN (1, 2, 3);

-- Combinación (usar paréntesis para claridad)
SELECT * FROM productos
WHERE (categoria_id = 1 OR categoria_id = 2)
AND precio > 5000
AND activo = TRUE;
```

---

## 3. ORDER BY - Ordenar Resultados

```sql
-- Ascendente (default)
SELECT nombre, precio FROM productos ORDER BY precio;
SELECT nombre, precio FROM productos ORDER BY precio ASC;

-- Descendente
SELECT nombre, salario FROM empleados ORDER BY salario DESC;

-- Múltiples columnas (primero por ciudad, luego por nombre)
SELECT nombre, apellido, ciudad FROM clientes
ORDER BY ciudad ASC, apellido ASC;

-- Ordenar por posición de columna (no recomendado pero funciona)
SELECT nombre, precio, stock FROM productos ORDER BY 2 DESC;  -- Ordena por precio

-- Ordenar por expresión
SELECT nombre, precio, precio * 1.16 AS con_iva FROM productos
ORDER BY con_iva DESC;

-- NULLS FIRST / NULLS LAST (PostgreSQL)
SELECT nombre, jefe_id FROM empleados ORDER BY jefe_id NULLS FIRST;
```

---

## 4. LIMIT y OFFSET - Paginación

```sql
-- Primeros 10 resultados
SELECT * FROM productos ORDER BY precio DESC LIMIT 10;

-- Paginación: página 2 (registros 11-20)
SELECT * FROM productos ORDER BY id LIMIT 10 OFFSET 10;

-- Top 5 empleados mejor pagados
SELECT nombre, apellido, salario FROM empleados
ORDER BY salario DESC LIMIT 5;

-- Oracle usa FETCH FIRST:
SELECT nombre, salario FROM empleados
ORDER BY salario DESC
FETCH FIRST 5 ROWS ONLY;
```

---

## 5. DISTINCT - Valores Únicos

```sql
-- Ciudades únicas
SELECT DISTINCT ciudad FROM clientes;

-- Combinaciones únicas
SELECT DISTINCT ciudad, tipo FROM clientes;

-- Contar valores únicos
SELECT COUNT(DISTINCT ciudad) AS ciudades_diferentes FROM clientes;
```

---

## 6. GROUP BY - Agrupar Datos

```sql
-- Contar clientes por ciudad
SELECT ciudad, COUNT(*) AS total
FROM clientes
GROUP BY ciudad
ORDER BY total DESC;

-- Ventas totales por empleado
SELECT e.nombre, e.apellido, COUNT(v.id) AS num_ventas, SUM(v.total) AS total_vendido
FROM empleados e
JOIN ventas v ON v.empleado_id = e.id
GROUP BY e.nombre, e.apellido
ORDER BY total_vendido DESC;

-- Promedio de precio por categoría
SELECT c.nombre AS categoria, 
       COUNT(p.id) AS productos,
       ROUND(AVG(p.precio), 2) AS precio_promedio,
       MIN(p.precio) AS precio_min,
       MAX(p.precio) AS precio_max
FROM categorias c
JOIN productos p ON p.categoria_id = c.id
GROUP BY c.nombre
ORDER BY precio_promedio DESC;
```

### Funciones de agregación:
| Función | Qué hace | Ejemplo |
|---------|----------|---------|
| COUNT(*) | Cuenta filas | `COUNT(*)` → 1000 |
| COUNT(col) | Cuenta no-nulls | `COUNT(email)` → 950 |
| SUM(col) | Suma valores | `SUM(total)` → 5000000 |
| AVG(col) | Promedio | `AVG(salario)` → 42000 |
| MIN(col) | Valor mínimo | `MIN(precio)` → 99.99 |
| MAX(col) | Valor máximo | `MAX(salario)` → 120000 |

---

## 7. HAVING - Filtrar Grupos

```sql
-- HAVING es el WHERE de los grupos
-- WHERE filtra FILAS (antes de agrupar)
-- HAVING filtra GRUPOS (después de agrupar)

-- Ciudades con más de 50 clientes
SELECT ciudad, COUNT(*) AS total
FROM clientes
GROUP BY ciudad
HAVING COUNT(*) > 50;

-- Empleados que han vendido más de $100,000
SELECT e.nombre, e.apellido, SUM(v.total) AS total_vendido
FROM empleados e
JOIN ventas v ON v.empleado_id = e.id
GROUP BY e.nombre, e.apellido
HAVING SUM(v.total) > 100000
ORDER BY total_vendido DESC;

-- Productos con stock promedio menor a 20 en todas las sucursales
SELECT p.nombre, ROUND(AVG(i.cantidad), 1) AS stock_promedio
FROM productos p
JOIN inventario i ON i.producto_id = p.id
GROUP BY p.nombre
HAVING AVG(i.cantidad) < 20
ORDER BY stock_promedio;
```

---

## 8. Orden de Ejecución de SQL

```
Orden en que ESCRIBES:          Orden en que se EJECUTA:
1. SELECT                       1. FROM / JOIN
2. FROM                         2. WHERE
3. WHERE                        3. GROUP BY
4. GROUP BY                     4. HAVING
5. HAVING                       5. SELECT
6. ORDER BY                     6. DISTINCT
7. LIMIT                        7. ORDER BY
                                8. LIMIT / OFFSET
```

**¿Por qué importa?**
- No puedes usar un alias de SELECT en WHERE (aún no existe)
- Puedes usar un alias de SELECT en ORDER BY (ya se calculó)
- HAVING puede usar funciones de agregación (GROUP BY ya se ejecutó)

---

## 9. Ejercicios Progresivos

### Nivel 1 - Básico
1. Muestra los 10 productos más caros
2. Muestra empleados contratados en 2023
3. Muestra clientes VIP de Monterrey o Guadalajara
4. ¿Cuántos productos hay por categoría?
5. ¿Cuál es el salario promedio por puesto?

### Nivel 2 - Intermedio
6. Top 5 clientes con más compras (cantidad de ventas)
7. Ventas totales por mes del año 2024
8. Productos que nunca se han vendido
9. Empleados cuyo salario está por encima del promedio de su sucursal
10. Sucursal con mayor facturación total

### Nivel 3 - Avanzado
11. Porcentaje de ventas por método de pago
12. Crecimiento mensual de ventas (mes actual vs mes anterior)
13. Ranking de vendedores por sucursal
14. Productos con stock por debajo del mínimo en alguna sucursal
15. Clientes que compraron en todas las sucursales

---

## Siguiente Módulo
→ [05-JOINs](../05-joins/README.md)
