# Preguntas de Entrevista - Módulo 04: Consultas SELECT

## Nivel Junior

### 1. ¿Cuál es el orden de ejecución de una query SQL?
**Respuesta:**
NO se ejecuta en el orden que escribes. El orden REAL es:
```
1. FROM / JOIN     → ¿De qué tablas?
2. WHERE           → ¿Qué filas filtrar?
3. GROUP BY        → ¿Cómo agrupar?
4. HAVING          → ¿Qué grupos filtrar?
5. SELECT          → ¿Qué columnas mostrar?
6. DISTINCT        → ¿Eliminar duplicados?
7. ORDER BY        → ¿En qué orden?
8. LIMIT / OFFSET  → ¿Cuántos resultados?
```
Por eso no puedes usar alias de SELECT en WHERE (WHERE se ejecuta antes).

---

### 2. ¿Cuál es la diferencia entre WHERE y HAVING?
**Respuesta:**
- `WHERE`: filtra FILAS antes del GROUP BY
- `HAVING`: filtra GRUPOS después del GROUP BY

```sql
-- WHERE: solo ventas > 1000
SELECT * FROM ventas WHERE total > 1000;

-- HAVING: categorías que tienen más de 5 productos
SELECT categoria_id, COUNT(*) FROM productos GROUP BY categoria_id HAVING COUNT(*) > 5;
```

---

### 3. ¿Qué hace DISTINCT?
**Respuesta:**
Elimina filas duplicadas del resultado:
```sql
SELECT DISTINCT ciudad FROM clientes;  -- Ciudades únicas
SELECT COUNT(DISTINCT ciudad) FROM clientes;  -- Cuántas ciudades hay
```

---

### 4. ¿Cuál es la diferencia entre COUNT(*), COUNT(columna) y COUNT(DISTINCT columna)?
**Respuesta:**
- `COUNT(*)`: cuenta TODAS las filas (incluyendo NULLs)
- `COUNT(columna)`: cuenta filas donde esa columna NO es NULL
- `COUNT(DISTINCT columna)`: cuenta valores ÚNICOS no NULL

```sql
-- 100 clientes, 5 sin email:
SELECT COUNT(*) FROM clientes;        -- 100
SELECT COUNT(email) FROM clientes;    -- 95 (sin NULLs)
SELECT COUNT(DISTINCT ciudad) FROM clientes;  -- 12 (ciudades únicas)
```

---

### 5. ¿Cómo funciona LIKE vs ILIKE?
**Respuesta:**
- `LIKE`: búsqueda por patrón (case-sensitive)
- `ILIKE`: búsqueda por patrón (case-insensitive, PostgreSQL)
- `%`: cualquier cantidad de caracteres
- `_`: exactamente 1 carácter

```sql
SELECT * FROM productos WHERE nombre LIKE 'Laptop%';    -- Empieza con "Laptop"
SELECT * FROM productos WHERE nombre ILIKE '%laptop%';  -- Contiene "laptop" (sin importar mayúsculas)
SELECT * FROM productos WHERE sku LIKE 'LAP-___';       -- "LAP-" + exactamente 3 caracteres
```

---

## Nivel Mid

### 6. ¿Qué son las Window Functions?
**Respuesta:**
Funciones que calculan sobre un conjunto de filas RELACIONADAS con la fila actual, sin colapsar filas (a diferencia de GROUP BY):
```sql
SELECT nombre, salario, puesto,
    AVG(salario) OVER (PARTITION BY puesto) AS promedio_puesto,
    RANK() OVER (ORDER BY salario DESC) AS ranking
FROM empleados;
-- Cada fila mantiene su identidad + tiene el cálculo de la "ventana"
```

---

### 7. ¿Cuál es la diferencia entre UNION y UNION ALL?
**Respuesta:**
- `UNION`: combina resultados ELIMINANDO duplicados (más lento, hace DISTINCT)
- `UNION ALL`: combina resultados CON duplicados (más rápido)

Regla: usa `UNION ALL` si sabes que no hay duplicados o no te importan.

---

### 8. ¿Qué es un CTE (Common Table Expression)?
**Respuesta:**
Una "tabla temporal nombrada" dentro de una query. Mejora legibilidad:
```sql
WITH ventas_mes AS (
    SELECT empleado_id, SUM(total) AS total_vendido
    FROM ventas WHERE fecha >= DATE_TRUNC('month', CURRENT_DATE)
    GROUP BY empleado_id
)
SELECT e.nombre, vm.total_vendido
FROM empleados e
JOIN ventas_mes vm ON vm.empleado_id = e.id
WHERE vm.total_vendido > 100000;
```

---

### 9. ¿Cómo paginias eficientemente con millones de registros?
**Respuesta:**
`OFFSET` es lento con valores altos (OFFSET 1000000 = leer y descartar 1M filas).

Solución: **keyset pagination** (cursor-based):
```sql
-- En vez de: OFFSET 1000000 LIMIT 20
-- Usar: WHERE id > ultimo_id_de_pagina_anterior
SELECT * FROM productos WHERE id > 50000 ORDER BY id LIMIT 20;
-- Instantáneo sin importar cuántos registros haya antes
```

---

### 10. ¿Qué es COALESCE y cuándo usarlo?
**Respuesta:**
Devuelve el primer valor no-NULL de una lista:
```sql
SELECT COALESCE(telefono, email, 'Sin contacto') AS contacto FROM clientes;
-- Si telefono es NULL → usa email. Si email también es NULL → usa 'Sin contacto'

-- Útil para evitar NULLs en cálculos:
SELECT nombre, COALESCE(descuento, 0) AS descuento FROM productos;
```
