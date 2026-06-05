# Módulo 15: Optimización y Tuning

## 1. EXPLAIN ANALYZE en Profundidad

```sql
-- Query sin optimizar
EXPLAIN (ANALYZE, BUFFERS, FORMAT TEXT)
SELECT c.nombre, c.apellido, SUM(v.total) AS total_compras
FROM clientes c
JOIN ventas v ON v.cliente_id = c.id
WHERE v.fecha >= '2024-01-01'
GROUP BY c.nombre, c.apellido
HAVING SUM(v.total) > 10000
ORDER BY total_compras DESC;

-- Interpretar el resultado:
-- "Seq Scan" → necesita índice
-- "Sort" con alto costo → necesita índice en ORDER BY
-- "Hash Join" → normal para tablas grandes
-- "Rows Removed by Filter: 9000" → el filtro descarta mucho → índice ayudaría
```

## 2. Estrategias de Optimización

### Regla 1: Índices correctos
```sql
-- ANTES (Seq Scan en ventas: 10000 filas)
SELECT * FROM ventas WHERE fecha >= '2024-01-01' AND sucursal_id = 1;

-- DESPUÉS (Index Scan: directo a los datos)
CREATE INDEX idx_ventas_fecha_sucursal ON ventas(fecha, sucursal_id);
```

### Regla 2: Evitar SELECT *
```sql
-- MAL: trae TODAS las columnas (incluyendo TEXT/BLOB pesados)
SELECT * FROM productos;

-- BIEN: solo lo que necesitas
SELECT id, nombre, precio, stock FROM productos;
```

### Regla 3: Paginación eficiente
```sql
-- MAL: OFFSET alto es lento (debe contar N filas para saltarlas)
SELECT * FROM ventas ORDER BY id LIMIT 20 OFFSET 100000;

-- BIEN: Keyset pagination (usa el último ID visto)
SELECT * FROM ventas WHERE id > 100000 ORDER BY id LIMIT 20;
```

### Regla 4: Evitar N+1 queries
```sql
-- MAL (N+1): 1 query para ventas + 1 query POR CADA venta para el cliente
-- Esto genera 1001 queries para 1000 ventas

-- BIEN: 1 sola query con JOIN
SELECT v.*, c.nombre AS cliente
FROM ventas v JOIN clientes c ON v.cliente_id = c.id;
```

### Regla 5: Particionar tablas grandes
```sql
-- Particionar ventas por año (cada año en su propia "sub-tabla")
CREATE TABLE ventas (
    id SERIAL,
    fecha TIMESTAMP NOT NULL,
    total NUMERIC(12,2)
) PARTITION BY RANGE (fecha);

CREATE TABLE ventas_2023 PARTITION OF ventas FOR VALUES FROM ('2023-01-01') TO ('2024-01-01');
CREATE TABLE ventas_2024 PARTITION OF ventas FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
CREATE TABLE ventas_2025 PARTITION OF ventas FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
-- Queries que filtran por fecha solo escanean la partición relevante
```

## 3. Comparación de Planes de Ejecución

| Operación | Costo relativo | Cuándo ocurre |
|-----------|---------------|---------------|
| Index Only Scan | ⭐ Más rápido | Todos los datos están en el índice |
| Index Scan | ⭐⭐ Rápido | Usa índice + lee tabla |
| Bitmap Index Scan | ⭐⭐⭐ Bueno | Combina múltiples índices |
| Seq Scan | ❌ Lento | Sin índice útil |
| Sort (en disco) | ❌❌ Muy lento | work_mem insuficiente |
| Nested Loop | Depende | Bueno si tabla interna es pequeña |
| Hash Join | Bueno | Tablas grandes, igualdad |
| Merge Join | Bueno | Datos ya ordenados |

## 4. Ejercicios de Tuning

1. Encuentra la query más lenta del sistema y optimízala
2. Compara el plan de ejecución de una query con y sin índice
3. Implementa particionamiento en la tabla de ventas
4. Optimiza: `SELECT * FROM ventas WHERE EXTRACT(YEAR FROM fecha) = 2024` (¿por qué es lento?)
5. Diseña la estrategia de índices para una tabla con 10 millones de registros

---

## Módulos Siguientes
→ [16-PostgreSQL](../16-postgresql/README.md) | [17-MySQL](../17-mysql/README.md) | [18-Oracle](../18-oracle/README.md)
