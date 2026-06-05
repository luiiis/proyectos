-- ════════════════════════════════════════════════════════════════
-- MÓDULO 17 - PROYECTO: Features de MySQL
-- ════════════════════════════════════════════════════════════════
-- Ejecutar: docker exec -i learn-mysql mysql -uroot -pmysql123 empresa_db < mysql-especifico.sql
-- ════════════════════════════════════════════════════════════════

USE empresa_db;

-- ═══════ 1. FULLTEXT SEARCH en MySQL ═══════
-- MySQL usa índices FULLTEXT (diferente a tsvector de PostgreSQL)

ALTER TABLE productos ADD FULLTEXT INDEX idx_ft_productos (nombre, descripcion);

-- Búsqueda en lenguaje natural
SELECT nombre, precio,
    MATCH(nombre, descripcion) AGAINST('laptop profesional') AS relevancia
FROM productos
WHERE MATCH(nombre, descripcion) AGAINST('laptop profesional')
ORDER BY relevancia DESC
LIMIT 10;

-- Búsqueda booleana (operadores + - *)
SELECT nombre, precio
FROM productos
WHERE MATCH(nombre, descripcion) AGAINST('+laptop -gaming' IN BOOLEAN MODE)
LIMIT 10;

-- ═══════ 2. JSON en MySQL ═══════
CREATE TABLE IF NOT EXISTS config_app (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_id INT,
    config JSON NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO config_app (usuario_id, config) VALUES
(1, '{"tema": "oscuro", "idioma": "es", "modulos": ["ventas", "inventario"]}'),
(2, '{"tema": "claro", "idioma": "en", "modulos": ["reportes"]}');

-- Extraer campos JSON
SELECT
    usuario_id,
    JSON_UNQUOTE(JSON_EXTRACT(config, '$.tema')) AS tema,
    JSON_UNQUOTE(JSON_EXTRACT(config, '$.idioma')) AS idioma,
    config->>'$.tema' AS tema_corto  -- Sintaxis corta (MySQL 8+)
FROM config_app;

-- Filtrar por contenido JSON
SELECT * FROM config_app
WHERE JSON_CONTAINS(config->'$.modulos', '"ventas"');

-- Modificar JSON
UPDATE config_app
SET config = JSON_SET(config, '$.tema', 'auto')
WHERE usuario_id = 1;

-- ═══════ 3. WINDOW FUNCTIONS (MySQL 8+) ═══════
SELECT
    nombre,
    precio,
    categoria_id,
    ROW_NUMBER() OVER (PARTITION BY categoria_id ORDER BY precio DESC) AS ranking,
    AVG(precio) OVER (PARTITION BY categoria_id) AS promedio_categoria
FROM productos
LIMIT 20;

-- ═══════ 4. CTE RECURSIVA (MySQL 8+) ═══════
WITH RECURSIVE jerarquia AS (
    SELECT id, nombre, apellido, puesto, jefe_id, 1 AS nivel
    FROM empleados WHERE jefe_id IS NULL
    UNION ALL
    SELECT e.id, e.nombre, e.apellido, e.puesto, e.jefe_id, j.nivel + 1
    FROM empleados e
    JOIN jerarquia j ON e.jefe_id = j.id
)
SELECT CONCAT(REPEAT('  ', nivel-1), '├── ', nombre, ' ', apellido) AS organigrama, puesto, nivel
FROM jerarquia
ORDER BY nivel, nombre
LIMIT 20;

-- ═══════ 5. VARIABLES DE SISTEMA ═══════
SHOW VARIABLES LIKE 'innodb_buffer_pool_size';
SHOW VARIABLES LIKE 'max_connections';
SHOW VARIABLES LIKE 'query_cache%';
SHOW STATUS LIKE 'Threads_connected';
SHOW STATUS LIKE 'Slow_queries';

-- ═══════ 6. EXPLAIN en MySQL ═══════
EXPLAIN SELECT v.*, c.nombre AS cliente
FROM ventas v
JOIN clientes c ON v.cliente_id = c.id
WHERE v.fecha >= '2024-01-01'
ORDER BY v.total DESC
LIMIT 10;
