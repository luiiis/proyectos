-- ════════════════════════════════════════════════════════════════
-- MÓDULO 09 - EJERCICIO 1: Índices y Performance
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Para cada ejercicio, ejecuta EXPLAIN ANALYZE antes y después de crear el índice.
-- Compara los tiempos y el tipo de scan (Seq Scan vs Index Scan).
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.1: Analizar query sin índice
-- ═══════════════════════════════════════════════════════════════
-- Ejecuta esta query y observa el plan de ejecución:
EXPLAIN ANALYZE
SELECT * FROM ventas
WHERE fecha BETWEEN '2024-06-01' AND '2024-06-30'
AND sucursal_id = 2
AND estado = 'COMPLETADA';

-- PREGUNTAS:
-- a) ¿Qué tipo de scan usa? (Seq Scan o Index Scan)
-- b) ¿Cuántas filas examina vs cuántas devuelve?
-- c) ¿Cuánto tiempo tarda?
-- RESPUESTAS:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.2: Crear índice y comparar
-- ═══════════════════════════════════════════════════════════════
-- Crea un índice compuesto que optimice la query anterior.
-- ¿En qué ORDEN pones las columnas? ¿Por qué?
-- PISTA: Pon primero la columna con mayor selectividad (más valores únicos)

-- TU ÍNDICE:


-- Ahora ejecuta la misma query con EXPLAIN ANALYZE:
-- ¿Cambió el plan? ¿Cuánto más rápido es?


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.3: Partial Index (índice condicional)
-- ═══════════════════════════════════════════════════════════════
-- El 90% de las queries filtran por estado='COMPLETADA'.
-- Crea un partial index que SOLO indexe ventas completadas.
-- Ventaja: índice más pequeño = más rápido + menos espacio en disco.

-- TU ÍNDICE:


-- Compara tamaño:
-- SELECT pg_size_pretty(pg_relation_size('tu_indice_normal'));
-- SELECT pg_size_pretty(pg_relation_size('tu_indice_parcial'));


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.4: Índice para búsqueda de texto
-- ═══════════════════════════════════════════════════════════════
-- Esta query es lenta porque usa LIKE '%texto%' (no puede usar B-Tree):
EXPLAIN ANALYZE
SELECT nombre, precio FROM productos WHERE nombre ILIKE '%laptop%';

-- Crea un índice GIN con pg_trgm para búsqueda fuzzy:
-- PISTA: CREATE EXTENSION IF NOT EXISTS pg_trgm;
--        CREATE INDEX ... USING gin(nombre gin_trgm_ops);

-- TU CÓDIGO:


-- Ejecuta de nuevo y compara el plan.


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.5: Identificar índices innecesarios
-- ═══════════════════════════════════════════════════════════════
-- Consulta qué índices existen y cuáles NO se están usando:

SELECT
    schemaname, tablename, indexname,
    idx_scan AS veces_usado,
    pg_size_pretty(pg_relation_size(indexrelid)) AS tamaño
FROM pg_stat_user_indexes
ORDER BY idx_scan ASC;

-- PREGUNTAS:
-- a) ¿Hay índices con idx_scan = 0? (nunca usados)
-- b) ¿Cuánto espacio ocupan esos índices inútiles?
-- c) ¿Los eliminarías? ¿Qué riesgo hay?


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.6: Optimizar una query compleja
-- ═══════════════════════════════════════════════════════════════
-- Esta query es el reporte más usado pero tarda 5+ segundos:
EXPLAIN ANALYZE
SELECT
    c.nombre || ' ' || c.apellido AS cliente,
    c.ciudad,
    COUNT(v.id) AS compras,
    SUM(v.total) AS total_gastado
FROM clientes c
JOIN ventas v ON v.cliente_id = c.id
WHERE v.fecha >= '2024-01-01'
AND v.estado = 'COMPLETADA'
AND c.ciudad = 'Ciudad de México'
GROUP BY c.nombre, c.apellido, c.ciudad
HAVING SUM(v.total) > 10000
ORDER BY total_gastado DESC;

-- TAREA:
-- 1. Identifica los cuellos de botella en el EXPLAIN
-- 2. Crea los índices necesarios (pueden ser 2-3)
-- 3. Ejecuta de nuevo y documenta la mejora

-- TUS ÍNDICES:


-- RESULTADO ANTES: ___ ms
-- RESULTADO DESPUÉS: ___ ms
-- MEJORA: ___x más rápido


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 9.7: Covering Index (Index-Only Scan)
-- ═══════════════════════════════════════════════════════════════
-- Un "covering index" incluye TODAS las columnas que la query necesita.
-- Así PostgreSQL no necesita ir a la tabla (Index-Only Scan = más rápido).

-- Query frecuente: solo necesita nombre y precio de productos activos
EXPLAIN ANALYZE
SELECT nombre, precio FROM productos WHERE activo = TRUE AND categoria_id = 1;

-- Crea un índice que INCLUYA nombre y precio:
-- PISTA: CREATE INDEX ... ON productos(categoria_id) INCLUDE (nombre, precio) WHERE activo = TRUE;

-- TU ÍNDICE:


-- Verifica que ahora dice "Index Only Scan" en el EXPLAIN.
