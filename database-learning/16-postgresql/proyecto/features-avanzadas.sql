-- ════════════════════════════════════════════════════════════════
-- MÓDULO 16 - PROYECTO: Features Avanzadas de PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══════ 1. JSONB - NoSQL dentro de SQL ═══════
\echo '── 1. JSONB: Datos flexibles ──'

-- Crear tabla con columna JSONB para configuración flexible
CREATE TABLE IF NOT EXISTS configuracion_usuario (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES empleados(id),
    preferencias JSONB DEFAULT '{}',
    metadata JSONB DEFAULT '{}'
);

-- Insertar datos JSON
INSERT INTO configuracion_usuario (usuario_id, preferencias, metadata) VALUES
(1, '{"tema": "oscuro", "idioma": "es", "dashboard": {"widgets": ["ventas", "stock", "alertas"], "refresh": 30}}', '{"ultimo_login": "2026-05-30", "dispositivo": "desktop"}'),
(2, '{"tema": "claro", "idioma": "es", "dashboard": {"widgets": ["ventas", "clientes"], "refresh": 60}}', '{"ultimo_login": "2026-05-29", "dispositivo": "mobile"}'),
(3, '{"tema": "oscuro", "idioma": "en", "notificaciones": {"email": true, "push": false}}', '{"ultimo_login": "2026-05-28", "dispositivo": "tablet"}');

-- Consultar campos específicos del JSON
SELECT usuario_id,
    preferencias->>'tema' AS tema,
    preferencias->>'idioma' AS idioma,
    preferencias->'dashboard'->>'refresh' AS refresh_segundos,
    preferencias->'dashboard'->'widgets' AS widgets
FROM configuracion_usuario;

-- Filtrar por contenido JSON
SELECT * FROM configuracion_usuario
WHERE preferencias @> '{"tema": "oscuro"}';

-- Buscar en arrays dentro del JSON
SELECT * FROM configuracion_usuario
WHERE preferencias->'dashboard'->'widgets' ? 'alertas';

-- Actualizar un campo dentro del JSON (sin reescribir todo)
UPDATE configuracion_usuario
SET preferencias = jsonb_set(preferencias, '{tema}', '"auto"')
WHERE usuario_id = 1;

-- Agregar un campo nuevo al JSON
UPDATE configuracion_usuario
SET preferencias = preferencias || '{"version": "2.0"}'::jsonb
WHERE usuario_id = 1;

-- Índice GIN para búsquedas rápidas en JSONB
CREATE INDEX IF NOT EXISTS idx_config_prefs ON configuracion_usuario USING gin(preferencias);

\echo '   ✓ JSONB configurado y probado'

-- ═══════ 2. FULL-TEXT SEARCH en español ═══════
\echo ''
\echo '── 2. Full-Text Search ──'

-- Agregar columna de búsqueda a productos
ALTER TABLE productos ADD COLUMN IF NOT EXISTS busqueda_fts tsvector;

-- Poblar con datos de nombre + descripción
UPDATE productos
SET busqueda_fts = to_tsvector('spanish', nombre || ' ' || COALESCE(descripcion, ''));

-- Crear índice GIN para búsqueda rápida
CREATE INDEX IF NOT EXISTS idx_productos_fts ON productos USING gin(busqueda_fts);

-- Buscar productos (con ranking de relevancia)
SELECT nombre, precio,
    ts_rank(busqueda_fts, to_tsquery('spanish', 'laptop & pro')) AS relevancia
FROM productos
WHERE busqueda_fts @@ to_tsquery('spanish', 'laptop & pro')
ORDER BY relevancia DESC
LIMIT 10;

-- Buscar con OR
SELECT nombre FROM productos
WHERE busqueda_fts @@ to_tsquery('spanish', 'monitor | pantalla')
LIMIT 5;

-- Trigger para mantener actualizado automáticamente
CREATE OR REPLACE FUNCTION fn_actualizar_fts()
RETURNS TRIGGER AS $$
BEGIN
    NEW.busqueda_fts := to_tsvector('spanish', NEW.nombre || ' ' || COALESCE(NEW.descripcion, ''));
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

DROP TRIGGER IF EXISTS trg_productos_fts ON productos;
CREATE TRIGGER trg_productos_fts
    BEFORE INSERT OR UPDATE OF nombre, descripcion ON productos
    FOR EACH ROW EXECUTE FUNCTION fn_actualizar_fts();

\echo '   ✓ Full-Text Search configurado'

-- ═══════ 3. BÚSQUEDA FUZZY (tolerante a errores) ═══════
\echo ''
\echo '── 3. Búsqueda Fuzzy con pg_trgm ──'

CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- Índice para búsqueda fuzzy en nombres de clientes
CREATE INDEX IF NOT EXISTS idx_clientes_nombre_trgm
ON clientes USING gin(nombre gin_trgm_ops);

-- Buscar "Carlos" aunque escribas "Carloss" o "Karlos"
SELECT nombre, apellido, similarity(nombre, 'Carloss') AS similitud
FROM clientes
WHERE nombre % 'Carloss'  -- % = similitud > 0.3
ORDER BY similitud DESC
LIMIT 5;

\echo '   ✓ Búsqueda fuzzy configurada'

-- ═══════ 4. CTE RECURSIVA - Organigrama ═══════
\echo ''
\echo '── 4. CTE Recursiva: Organigrama ──'

WITH RECURSIVE organigrama AS (
    -- Caso base: Director (sin jefe)
    SELECT id, nombre, apellido, puesto, jefe_id, 1 AS nivel,
           ARRAY[nombre || ' ' || apellido] AS ruta
    FROM empleados
    WHERE jefe_id IS NULL

    UNION ALL

    -- Caso recursivo: empleados que reportan al nivel anterior
    SELECT e.id, e.nombre, e.apellido, e.puesto, e.jefe_id, o.nivel + 1,
           o.ruta || (e.nombre || ' ' || e.apellido)
    FROM empleados e
    JOIN organigrama o ON e.jefe_id = o.id
)
SELECT
    REPEAT('  ', nivel - 1) || '├── ' || nombre || ' ' || apellido AS organigrama,
    puesto,
    nivel
FROM organigrama
ORDER BY ruta
LIMIT 30;

-- ═══════ 5. GENERATED COLUMNS (columnas calculadas) ═══════
\echo ''
\echo '── 5. Generated Columns ──'

-- Agregar columna calculada que siempre tiene el margen
ALTER TABLE productos ADD COLUMN IF NOT EXISTS
    margen_ganancia NUMERIC(5,2) GENERATED ALWAYS AS (
        CASE WHEN costo > 0 THEN ROUND((precio - costo) / precio * 100, 2) ELSE NULL END
    ) STORED;

SELECT nombre, precio, costo, margen_ganancia
FROM productos
WHERE costo IS NOT NULL
ORDER BY margen_ganancia DESC
LIMIT 10;

\echo ''
\echo '══════════════════════════════════════════════'
\echo '  FEATURES AVANZADAS COMPLETADAS'
\echo '══════════════════════════════════════════════'
