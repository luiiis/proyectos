# Preguntas de Entrevista - Módulo 07: Subconsultas

## Nivel Junior

### 1. ¿Qué es una subconsulta?
**Respuesta:**
Una query dentro de otra query. Se ejecuta primero la interna, su resultado se usa en la externa:
```sql
SELECT * FROM productos WHERE precio > (SELECT AVG(precio) FROM productos);
-- 1. Calcula el promedio (ej: 5000)
-- 2. Filtra productos con precio > 5000
```

### 2. ¿Cuál es la diferencia entre IN y EXISTS?
**Respuesta:**
- `IN`: compara contra una lista de valores. Mejor con subqueries que devuelven POCOS resultados.
- `EXISTS`: verifica si la subquery devuelve AL MENOS una fila. Mejor con subqueries correlacionadas grandes.

```sql
-- IN (lista chica):
SELECT * FROM empleados WHERE sucursal_id IN (SELECT id FROM sucursales WHERE ciudad = 'CDMX');

-- EXISTS (correlacionada):
SELECT * FROM clientes c WHERE EXISTS (SELECT 1 FROM ventas v WHERE v.cliente_id = c.id);
```

### 3. ¿Qué es una subconsulta correlacionada?
**Respuesta:**
Una subconsulta que REFERENCIA la query externa. Se ejecuta UNA VEZ por cada fila de la query externa:
```sql
-- Productos más caros que el promedio de SU categoría:
SELECT * FROM productos p WHERE p.precio > (
    SELECT AVG(precio) FROM productos WHERE categoria_id = p.categoria_id  -- ← referencia a p
);
```

### 4. ¿Cuándo usarías subconsulta en el SELECT?
**Respuesta:**
Para agregar columnas calculadas sin JOIN ni GROUP BY:
```sql
SELECT e.nombre,
    (SELECT COUNT(*) FROM ventas WHERE empleado_id = e.id) AS total_ventas
FROM empleados e;
```
Cuidado: se ejecuta una vez por fila. Con muchos registros, un JOIN + GROUP BY es más eficiente.

### 5. ¿Qué es ANY/ALL con subconsultas?
**Respuesta:**
```sql
-- ANY: al menos uno cumple (como OR)
SELECT * FROM productos WHERE precio > ANY (SELECT precio FROM productos WHERE categoria_id = 1);
-- Productos más caros que AL MENOS UN producto de categoría 1

-- ALL: todos cumplen (como AND)
SELECT * FROM productos WHERE precio > ALL (SELECT precio FROM productos WHERE categoria_id = 1);
-- Productos más caros que TODOS los de categoría 1
```
