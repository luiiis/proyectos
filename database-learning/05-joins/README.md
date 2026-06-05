# Módulo 05: JOINs - Unir Tablas

## ¿Qué es un JOIN?
Un JOIN combina filas de dos o más tablas basándose en una columna relacionada (generalmente FK → PK).

**Sin JOINs**: solo puedes consultar UNA tabla a la vez.
**Con JOINs**: puedes obtener datos de MÚLTIPLES tablas en una sola consulta.

---

## 1. INNER JOIN - Solo coincidencias

Devuelve SOLO las filas que tienen coincidencia en AMBAS tablas.

```
Tabla A          Tabla B          INNER JOIN
┌───┐            ┌───┐            ┌───┐
│ 1 │──────────→ │ 1 │            │ 1 │ ✓ (existe en ambas)
│ 2 │──────────→ │ 2 │            │ 2 │ ✓
│ 3 │            │ 4 │            │   │
│ 5 │            │ 5 │──────────→ │ 5 │ ✓
└───┘            └───┘            └───┘
  3 no tiene match en B → se excluye
  4 no tiene match en A → se excluye
```

```sql
-- Empleados CON su sucursal (excluye empleados sin sucursal)
SELECT e.nombre, e.apellido, e.puesto, s.nombre AS sucursal
FROM empleados e
INNER JOIN sucursales s ON e.sucursal_id = s.id;

-- Productos CON su categoría y proveedor
SELECT p.nombre, p.precio, c.nombre AS categoria, pr.nombre AS proveedor
FROM productos p
INNER JOIN categorias c ON p.categoria_id = c.id
INNER JOIN proveedores pr ON p.proveedor_id = pr.id;

-- Ventas con detalle del cliente y empleado
SELECT v.numero_venta, v.fecha, v.total,
       cl.nombre || ' ' || cl.apellido AS cliente,
       e.nombre || ' ' || e.apellido AS vendedor
FROM ventas v
INNER JOIN clientes cl ON v.cliente_id = cl.id
INNER JOIN empleados e ON v.empleado_id = e.id
ORDER BY v.fecha DESC
LIMIT 20;
```

---

## 2. LEFT JOIN - Todo de la izquierda + coincidencias

Devuelve TODAS las filas de la tabla izquierda, y las coincidencias de la derecha (NULL si no hay match).

```
Tabla A          Tabla B          LEFT JOIN
┌───┐            ┌───┐            ┌──────────┐
│ 1 │──────────→ │ 1 │            │ 1 → 1    │ ✓
│ 2 │──────────→ │ 2 │            │ 2 → 2    │ ✓
│ 3 │            │ 4 │            │ 3 → NULL │ ✓ (sin match, pero se incluye)
│ 5 │            │ 5 │──────────→ │ 5 → 5    │ ✓
└───┘            └───┘            └──────────┘
  TODOS de A aparecen, con o sin match en B
```

```sql
-- TODOS los empleados, incluso los que no tienen sucursal asignada
SELECT e.nombre, e.apellido, s.nombre AS sucursal
FROM empleados e
LEFT JOIN sucursales s ON e.sucursal_id = s.id;
-- Empleados sin sucursal tendrán sucursal = NULL

-- Productos que NO se han vendido nunca
SELECT p.nombre, p.precio
FROM productos p
LEFT JOIN detalle_venta dv ON dv.producto_id = p.id
WHERE dv.id IS NULL;  -- Truco: filtrar donde el JOIN no encontró match

-- Clientes con su número de compras (incluyendo los que no han comprado)
SELECT cl.nombre, cl.apellido, COUNT(v.id) AS total_compras
FROM clientes cl
LEFT JOIN ventas v ON v.cliente_id = cl.id
GROUP BY cl.nombre, cl.apellido
ORDER BY total_compras;
-- Clientes sin compras aparecen con total_compras = 0
```

---

## 3. RIGHT JOIN - Todo de la derecha + coincidencias

Igual que LEFT JOIN pero al revés. En la práctica, casi siempre se usa LEFT JOIN reordenando las tablas.

```sql
-- Todas las sucursales con sus empleados (incluso sucursales vacías)
SELECT s.nombre AS sucursal, e.nombre, e.apellido
FROM empleados e
RIGHT JOIN sucursales s ON e.sucursal_id = s.id;

-- Equivalente con LEFT JOIN (más legible):
SELECT s.nombre AS sucursal, e.nombre, e.apellido
FROM sucursales s
LEFT JOIN empleados e ON e.sucursal_id = s.id;
```

---

## 4. FULL OUTER JOIN - Todo de ambas tablas

Devuelve TODAS las filas de AMBAS tablas, con NULL donde no hay match.

```
Tabla A          Tabla B          FULL JOIN
┌───┐            ┌───┐            ┌──────────────┐
│ 1 │──────────→ │ 1 │            │ 1 → 1        │
│ 2 │──────────→ │ 2 │            │ 2 → 2        │
│ 3 │            │ 4 │            │ 3 → NULL     │ (solo en A)
│ 5 │            │ 5 │──────────→ │ 5 → 5        │
└───┘            └───┘            │ NULL → 4     │ (solo en B)
                                  └──────────────┘
```

```sql
-- PostgreSQL soporta FULL OUTER JOIN
SELECT p.nombre AS producto, i.cantidad AS stock, s.nombre AS sucursal
FROM productos p
FULL OUTER JOIN inventario i ON i.producto_id = p.id
FULL OUTER JOIN sucursales s ON i.sucursal_id = s.id;

-- MySQL NO soporta FULL OUTER JOIN directamente
-- Se simula con UNION de LEFT + RIGHT:
SELECT p.nombre, i.cantidad FROM productos p LEFT JOIN inventario i ON i.producto_id = p.id
UNION
SELECT p.nombre, i.cantidad FROM productos p RIGHT JOIN inventario i ON i.producto_id = p.id;
```

---

## 5. SELF JOIN - Tabla consigo misma

Útil para jerarquías (empleado → jefe) o comparaciones dentro de la misma tabla.

```sql
-- Empleados con el nombre de su jefe
SELECT 
    e.nombre || ' ' || e.apellido AS empleado,
    e.puesto,
    j.nombre || ' ' || j.apellido AS jefe,
    j.puesto AS puesto_jefe
FROM empleados e
LEFT JOIN empleados j ON e.jefe_id = j.id
ORDER BY j.nombre NULLS FIRST;

-- Productos más caros que el promedio de su categoría
SELECT p1.nombre, p1.precio, p1.categoria_id
FROM productos p1
JOIN (
    SELECT categoria_id, AVG(precio) AS precio_promedio
    FROM productos
    GROUP BY categoria_id
) p2 ON p1.categoria_id = p2.categoria_id
WHERE p1.precio > p2.precio_promedio;
```

---

## 6. CROSS JOIN - Producto cartesiano

Combina CADA fila de A con CADA fila de B. Si A tiene 5 filas y B tiene 3, el resultado tiene 15 filas.

```sql
-- Generar todas las combinaciones producto-sucursal (para inventario inicial)
SELECT p.id AS producto_id, s.id AS sucursal_id, 0 AS cantidad
FROM productos p
CROSS JOIN sucursales s;
-- 500 productos × 5 sucursales = 2500 filas
```

---

## 7. JOINs Múltiples (3+ tablas)

```sql
-- Reporte completo de ventas
SELECT 
    v.numero_venta,
    v.fecha,
    cl.nombre || ' ' || cl.apellido AS cliente,
    cl.tipo AS tipo_cliente,
    e.nombre || ' ' || e.apellido AS vendedor,
    s.nombre AS sucursal,
    s.ciudad,
    p.nombre AS producto,
    cat.nombre AS categoria,
    dv.cantidad,
    dv.precio_unitario,
    dv.subtotal,
    v.total AS total_venta
FROM ventas v
JOIN clientes cl ON v.cliente_id = cl.id
JOIN empleados e ON v.empleado_id = e.id
JOIN sucursales s ON v.sucursal_id = s.id
JOIN detalle_venta dv ON dv.venta_id = v.id
JOIN productos p ON dv.producto_id = p.id
JOIN categorias cat ON p.categoria_id = cat.id
WHERE v.estado = 'COMPLETADA'
ORDER BY v.fecha DESC
LIMIT 100;
```

---

## 8. Diagrama Visual de JOINs

```
         INNER JOIN              LEFT JOIN               RIGHT JOIN
      ┌─────┬─────┐          ┌─────┬─────┐          ┌─────┬─────┐
     ╱│     │█████│╲        ╱│█████│█████│╲        ╱│█████│█████│╲
    │ │     │█████│ │      │ │█████│█████│ │      │ │█████│█████│ │
    │ │  A  │█B██ │ │      │ │██A██│█B███│ │      │ │  A  │██B██│ │
    │ │     │█████│ │      │ │█████│█████│ │      │ │█████│█████│ │
     ╲│     │█████│╱        ╲│█████│█████│╱        ╲│█████│█████│╱
      └─────┴─────┘          └─────┴─────┘          └─────┴─────┘
    Solo intersección        Todo A + intersección   Intersección + todo B


         FULL JOIN              LEFT EXCLUSIVE          CROSS JOIN
      ┌─────┬─────┐          ┌─────┬─────┐
     ╱│█████│█████│╲        ╱│█████│     │╲         A × B = todas las
    │ │█████│█████│ │      │ │█████│     │ │        combinaciones posibles
    │ │██A██│██B██│ │      │ │██A██│  B  │ │
    │ │█████│█████│ │      │ │█████│     │ │
     ╲│█████│█████│╱        ╲│█████│     │╱
      └─────┴─────┘          └─────┴─────┘
    Todo A + todo B          Solo A sin match en B
```

---

## 9. Ejercicios (50 ejercicios)

### Nivel Básico (1-15)
1. Muestra empleados con el nombre de su sucursal
2. Muestra productos con el nombre de su categoría
3. Muestra ventas con nombre del cliente
4. Muestra productos con nombre del proveedor y categoría
5. Muestra el detalle de la venta #V-20230101-00001 con nombres de productos
6. ¿Cuántos empleados tiene cada sucursal?
7. ¿Cuántos productos tiene cada categoría?
8. Muestra clientes que han comprado (al menos 1 venta)
9. Muestra empleados con su jefe directo
10. Total vendido por cada empleado
11. Productos vendidos en la sucursal 'Central CDMX'
12. Clientes que compraron productos de la categoría 'Electrónica'
13. Proveedores cuyos productos se han vendido
14. Ventas del mes actual con detalle completo
15. Empleados que no han realizado ninguna venta

### Nivel Intermedio (16-35)
16. Top 10 productos más vendidos (por cantidad)
17. Top 10 clientes por monto total de compras
18. Ventas por categoría de producto
19. Empleados que venden más que el promedio
20. Productos que están en inventario en TODAS las sucursales
21. Sucursales donde NO hay stock de un producto específico
22. Clientes que compraron en más de una sucursal
23. Productos que se venden por encima de su costo (margen positivo)
24. Empleados con su cadena jerárquica completa (empleado → jefe → director)
25. Ventas donde el descuento fue mayor al 10%
26. Proveedores que no han suministrado productos vendidos este año
27. Comparar ventas por sucursal: este mes vs mes anterior
28. Productos con stock menor al mínimo en alguna sucursal
29. Clientes VIP que no han comprado en los últimos 3 meses
30. Ranking de vendedores por sucursal (con posición)
31. Productos más vendidos por cada categoría (top 3 por categoría)
32. Meses con mayor y menor facturación
33. Clientes que compraron el mismo producto más de una vez
34. Empleados que trabajan en la misma sucursal que su jefe
35. Ventas con más de 5 productos diferentes

### Nivel Avanzado (36-50)
36. Análisis de cohortes: retención de clientes por mes de primera compra
37. Productos con tendencia de ventas creciente (últimos 3 meses)
38. Empleados cuyo salario está por encima del percentil 75 de su sucursal
39. Clientes que compraron TODOS los productos de una categoría
40. Simulación de comisiones: 5% del total vendido por cada empleado
41. Análisis ABC de productos (80/20 - Pareto)
42. Detección de anomalías: ventas con monto inusualmente alto
43. Cross-selling: clientes que compraron A también compraron B
44. Forecast simple: promedio móvil de ventas de los últimos 3 meses
45. Rotación de inventario por producto (ventas / stock promedio)
46. Empleados que nunca han vendido a clientes VIP
47. Productos que se venden mejor en una sucursal que en otras
48. Análisis de canasta: productos que se compran juntos frecuentemente
49. Cálculo de lifetime value por cliente
50. Dashboard query: KPIs principales del negocio en una sola consulta

---

## Siguiente Módulo
→ [06-Funciones SQL](../06-funciones/README.md)
