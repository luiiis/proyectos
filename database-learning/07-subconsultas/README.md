# Módulo 07: Subconsultas

## ¿Qué es una Subconsulta?
Una query DENTRO de otra query. La query interna se ejecuta primero y su resultado alimenta a la externa.

---

## 1. Subconsultas Escalares (devuelven UN valor)

```sql
-- Empleados que ganan más que el promedio
SELECT nombre, salario
FROM empleados
WHERE salario > (SELECT AVG(salario) FROM empleados);

-- Producto más caro
SELECT * FROM productos
WHERE precio = (SELECT MAX(precio) FROM productos);

-- Última venta registrada
SELECT * FROM ventas
WHERE fecha = (SELECT MAX(fecha) FROM ventas);
```

## 2. Subconsultas con IN / NOT IN

```sql
-- Clientes que han comprado (su ID aparece en ventas)
SELECT nombre, apellido FROM clientes
WHERE id IN (SELECT DISTINCT cliente_id FROM ventas);

-- Productos que NUNCA se han vendido
SELECT nombre, precio FROM productos
WHERE id NOT IN (SELECT DISTINCT producto_id FROM detalle_venta);

-- Empleados que trabajan en sucursales de CDMX
SELECT nombre, apellido FROM empleados
WHERE sucursal_id IN (SELECT id FROM sucursales WHERE ciudad = 'Ciudad de México');
```

## 3. Subconsultas Correlacionadas

La subconsulta se ejecuta UNA VEZ POR CADA FILA de la query externa.

```sql
-- Empleados que ganan más que el promedio de SU sucursal
SELECT e.nombre, e.salario, e.sucursal_id
FROM empleados e
WHERE e.salario > (
    SELECT AVG(e2.salario) 
    FROM empleados e2 
    WHERE e2.sucursal_id = e.sucursal_id  -- Correlación: usa e.sucursal_id
);

-- Último pedido de cada cliente
SELECT v.*
FROM ventas v
WHERE v.fecha = (
    SELECT MAX(v2.fecha) FROM ventas v2 WHERE v2.cliente_id = v.cliente_id
);
```

## 4. EXISTS / NOT EXISTS

Más eficiente que IN para tablas grandes. Devuelve TRUE/FALSE.

```sql
-- Clientes que SÍ han comprado (EXISTS es más rápido que IN para tablas grandes)
SELECT cl.nombre, cl.apellido
FROM clientes cl
WHERE EXISTS (
    SELECT 1 FROM ventas v WHERE v.cliente_id = cl.id
);

-- Productos sin inventario en NINGUNA sucursal
SELECT p.nombre
FROM productos p
WHERE NOT EXISTS (
    SELECT 1 FROM inventario i WHERE i.producto_id = p.id AND i.cantidad > 0
);

-- Categorías que tienen al menos un producto con precio > 10000
SELECT c.nombre
FROM categorias c
WHERE EXISTS (
    SELECT 1 FROM productos p WHERE p.categoria_id = c.id AND p.precio > 10000
);
```

## 5. Subconsultas en FROM (Derived Tables)

```sql
-- Ventas por mes con ranking
SELECT * FROM (
    SELECT 
        DATE_TRUNC('month', fecha) AS mes,
        SUM(total) AS total_mes,
        RANK() OVER (ORDER BY SUM(total) DESC) AS ranking
    FROM ventas
    WHERE estado = 'COMPLETADA'
    GROUP BY DATE_TRUNC('month', fecha)
) AS ventas_mensuales
WHERE ranking <= 5;
```

## 6. CTEs (Common Table Expressions) - Alternativa moderna

```sql
-- CTE: más legible que subconsultas anidadas
WITH ventas_por_empleado AS (
    SELECT empleado_id, SUM(total) AS total_vendido, COUNT(*) AS num_ventas
    FROM ventas
    WHERE estado = 'COMPLETADA'
    GROUP BY empleado_id
),
promedio_general AS (
    SELECT AVG(total_vendido) AS promedio FROM ventas_por_empleado
)
SELECT e.nombre, e.apellido, vpe.total_vendido, vpe.num_ventas
FROM ventas_por_empleado vpe
JOIN empleados e ON e.id = vpe.empleado_id
CROSS JOIN promedio_general pg
WHERE vpe.total_vendido > pg.promedio
ORDER BY vpe.total_vendido DESC;
```

## 7. Ejercicios

1. Productos más caros que el promedio de su categoría
2. Clientes que han gastado más que el promedio general
3. Empleados que no han vendido nada este mes
4. Sucursal con mayor facturación (sin usar ORDER BY + LIMIT)
5. Productos que se venden en todas las sucursales (división relacional)
6. Segundo producto más caro de cada categoría
7. Clientes cuya primera compra fue hace más de 1 año
8. Empleados cuyo salario es el máximo de su puesto

---

## Siguiente Módulo
→ [08-Vistas](../08-vistas/README.md)
