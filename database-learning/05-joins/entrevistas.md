# Preguntas de Entrevista - Módulo 05: JOINs

## Nivel Junior

### 1. ¿Cuál es la diferencia entre INNER JOIN y LEFT JOIN?
**Respuesta:**
- `INNER JOIN`: solo filas con coincidencia en AMBAS tablas. Si un empleado no tiene sucursal, no aparece.
- `LEFT JOIN`: TODAS las filas de la tabla izquierda + coincidencias de la derecha (NULL si no hay match). El empleado sin sucursal aparece con sucursal = NULL.

---

### 2. ¿Cómo encontrarías registros sin relación? (huérfanos)
**Respuesta:**
```sql
-- Productos que nunca se han vendido:
SELECT p.nombre FROM productos p
LEFT JOIN detalle_venta dv ON dv.producto_id = p.id
WHERE dv.id IS NULL;
```
El truco: LEFT JOIN + WHERE columna_derecha IS NULL → registros de la izquierda SIN match.

---

### 3. ¿Qué es un SELF JOIN?
**Respuesta:**
Join de una tabla consigo misma. Usado para jerarquías:
```sql
-- Empleados con su jefe
SELECT e.nombre AS empleado, j.nombre AS jefe
FROM empleados e
LEFT JOIN empleados j ON e.jefe_id = j.id;
```

---

### 4. ¿Puedes hacer JOIN sin Foreign Key?
**Respuesta:**
Sí. El JOIN es una operación de consulta (SELECT). La FK es una constraint de integridad (DDL). Puedes hacer JOIN con cualquier condición:
```sql
SELECT * FROM tabla_a JOIN tabla_b ON tabla_a.campo = tabla_b.campo;
```
La FK solo GARANTIZA que los datos sean consistentes, pero el JOIN funciona sin ella.

---

### 5. ¿Cuál es la diferencia entre WHERE y ON en un JOIN?
**Respuesta:**
- `ON`: define la CONDICIÓN de unión (cómo se relacionan las tablas)
- `WHERE`: filtra DESPUÉS del join

Con INNER JOIN son equivalentes. Con LEFT JOIN NO:
```sql
-- Esto muestra todos los empleados, sucursal NULL si no coincide con 'Central':
SELECT * FROM empleados e LEFT JOIN sucursales s ON e.sucursal_id = s.id AND s.nombre = 'Central';

-- Esto filtra DESPUÉS: solo muestra empleados cuya sucursal es 'Central':
SELECT * FROM empleados e LEFT JOIN sucursales s ON e.sucursal_id = s.id WHERE s.nombre = 'Central';
```

---

## Nivel Mid

### 6. ¿Cómo harías un JOIN entre 5+ tablas sin perder rendimiento?
**Respuesta:**
1. Asegurar que las columnas de JOIN tienen índices (FK generalmente ya los tienen)
2. Filtrar lo antes posible (WHERE antes de JOINs innecesarios)
3. Usar EXPLAIN para verificar el plan
4. No hacer SELECT * (solo campos necesarios)
5. Considerar si realmente necesitas todas las tablas

---

### 7. ¿Qué es el problema N+1 en JOINs?
**Respuesta:**
En ORMs: cargar 100 empleados → 1 query. Luego acceder a sucursal de cada uno → 100 queries extra (N+1 = 101 queries).

Solución SQL: hacer el JOIN en una sola consulta. En ORM: usar `JOIN FETCH`, `@EntityGraph`, o eager loading selectivo.

---

### 8. ¿Cuándo usarías una subconsulta vs un JOIN?
**Respuesta:**
- **JOIN** cuando: necesitas columnas de ambas tablas en el resultado
- **Subconsulta** cuando: solo necesitas filtrar (IN, EXISTS)
- En rendimiento: JOIN suele ser más rápido (el optimizador los maneja mejor)

```sql
-- Subconsulta (solo filtra):
SELECT * FROM productos WHERE categoria_id IN (SELECT id FROM categorias WHERE activa = true);

-- JOIN (puedes obtener datos de ambas):
SELECT p.*, c.nombre FROM productos p JOIN categorias c ON p.categoria_id = c.id WHERE c.activa = true;
```

---

### 9. ¿Qué es un LATERAL JOIN?
**Respuesta:**
Un JOIN donde la subconsulta de la derecha puede REFERENCIAR la tabla de la izquierda (como un for-each):
```sql
-- Top 3 productos más vendidos POR CADA categoría:
SELECT c.nombre, top_p.*
FROM categorias c
CROSS JOIN LATERAL (
    SELECT p.nombre, SUM(dv.cantidad) AS vendidos
    FROM productos p
    JOIN detalle_venta dv ON dv.producto_id = p.id
    WHERE p.categoria_id = c.id   -- ← referencia a la tabla externa
    GROUP BY p.nombre
    ORDER BY vendidos DESC LIMIT 3
) top_p;
```

---

### 10. ¿Cómo optimizarías una query con múltiples JOINs que tarda 10 segundos?
**Respuesta:**
1. `EXPLAIN ANALYZE` → ver qué JOINs son costosos
2. Verificar índices en columnas de JOIN (FK → PK)
3. ¿Hay Sequential Scans en tablas grandes? → agregar índice
4. Reducir columnas (no SELECT *)
5. Filtrar antes: mover condiciones WHERE a la tabla más restrictiva
6. Considerar materializar con VIEW o tabla temporal
7. ¿El JOIN es realmente necesario? A veces una subconsulta EXISTS es más rápida
