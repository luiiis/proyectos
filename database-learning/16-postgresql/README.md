# Módulo 16: PostgreSQL - Características Específicas

## ¿Por qué PostgreSQL?
PostgreSQL es la BD relacional open-source más avanzada. En 2026 es la elección #1 para nuevos proyectos por su cumplimiento SQL, extensibilidad y features únicos.

---

## 1. Tipos de Datos Exclusivos

```sql
-- JSONB: JSON binario indexable (NoSQL dentro de SQL)
CREATE TABLE configuraciones (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES usuarios(id),
    preferencias JSONB DEFAULT '{}'
);

INSERT INTO configuraciones (usuario_id, preferencias) VALUES
(1, '{"tema": "oscuro", "idioma": "es", "notificaciones": {"email": true, "push": false}}');

-- Consultar dentro del JSON
SELECT preferencias->>'tema' AS tema FROM configuraciones WHERE usuario_id = 1;
SELECT preferencias->'notificaciones'->>'email' FROM configuraciones;

-- Filtrar por contenido JSON
SELECT * FROM configuraciones WHERE preferencias @> '{"tema": "oscuro"}';

-- Actualizar campo dentro del JSON
UPDATE configuraciones
SET preferencias = jsonb_set(preferencias, '{idioma}', '"en"')
WHERE usuario_id = 1;

-- ARRAY: listas nativas
CREATE TABLE productos_tags (
    producto_id INTEGER REFERENCES productos(id),
    tags TEXT[] DEFAULT '{}'
);

INSERT INTO productos_tags VALUES (1, ARRAY['gaming', 'premium', 'nuevo']);
SELECT * FROM productos_tags WHERE 'gaming' = ANY(tags);

-- INET/CIDR: direcciones IP nativas
CREATE TABLE accesos (
    id SERIAL PRIMARY KEY,
    ip INET NOT NULL,
    fecha TIMESTAMP DEFAULT NOW()
);
INSERT INTO accesos (ip) VALUES ('192.168.1.100');
SELECT * FROM accesos WHERE ip << '192.168.0.0/16';  -- Subnet match

-- UUID: identificadores universales
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
CREATE TABLE sesiones (
    id UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    usuario_id INTEGER,
    created_at TIMESTAMP DEFAULT NOW()
);

-- TSRANGE/DATERANGE: rangos
CREATE TABLE reservaciones (
    id SERIAL PRIMARY KEY,
    sala VARCHAR(50),
    periodo TSRANGE,
    EXCLUDE USING gist (sala WITH =, periodo WITH &&)  -- No permite solapamiento
);
INSERT INTO reservaciones (sala, periodo) VALUES
('Sala A', '[2026-06-01 09:00, 2026-06-01 11:00)');
```

---

## 2. Full-Text Search (Búsqueda de texto completo)

```sql
-- Crear índice de búsqueda en español
ALTER TABLE productos ADD COLUMN busqueda tsvector;
UPDATE productos SET busqueda = to_tsvector('spanish', nombre || ' ' || COALESCE(descripcion, ''));
CREATE INDEX idx_productos_fts ON productos USING gin(busqueda);

-- Buscar productos
SELECT nombre, precio,
    ts_rank(busqueda, to_tsquery('spanish', 'laptop & pro')) AS relevancia
FROM productos
WHERE busqueda @@ to_tsquery('spanish', 'laptop & pro')
ORDER BY relevancia DESC;

-- Búsqueda con sinónimos y stemming
-- 'laptops' encuentra 'laptop' (stemming automático en español)
SELECT nombre FROM productos
WHERE busqueda @@ to_tsquery('spanish', 'laptops');
```

---

## 3. Extensiones Populares

```sql
-- PostGIS: datos geoespaciales
CREATE EXTENSION postgis;
ALTER TABLE sucursales ADD COLUMN ubicacion GEOMETRY(Point, 4326);
UPDATE sucursales SET ubicacion = ST_SetSRID(ST_MakePoint(-99.1332, 19.4326), 4326) WHERE id = 1;
-- Sucursales a menos de 10km de un punto
SELECT nombre, ST_Distance(ubicacion::geography, ST_MakePoint(-99.15, 19.43)::geography) / 1000 AS km
FROM sucursales ORDER BY ubicacion <-> ST_MakePoint(-99.15, 19.43)::geometry LIMIT 5;

-- pg_trgm: búsqueda fuzzy (tolerante a errores de escritura)
CREATE EXTENSION pg_trgm;
CREATE INDEX idx_clientes_nombre_trgm ON clientes USING gin(nombre gin_trgm_ops);
SELECT nombre, similarity(nombre, 'Carloss') AS sim
FROM clientes WHERE nombre % 'Carloss' ORDER BY sim DESC LIMIT 5;
-- Encuentra "Carlos" aunque escribas "Carloss"

-- pgcrypto: encriptación
CREATE EXTENSION pgcrypto;
-- Hash de password
SELECT crypt('mi_password', gen_salt('bf', 12));
-- Verificar password
SELECT (crypt('mi_password', password_hash) = password_hash) AS valido FROM usuarios WHERE id = 1;
```

---

## 4. Particionamiento Nativo

```sql
-- Particionar ventas por rango de fecha
CREATE TABLE ventas_particionada (
    id BIGSERIAL,
    numero_venta VARCHAR(20) NOT NULL,
    fecha TIMESTAMP NOT NULL,
    total NUMERIC(12,2),
    PRIMARY KEY (id, fecha)
) PARTITION BY RANGE (fecha);

-- Crear particiones por año
CREATE TABLE ventas_2024 PARTITION OF ventas_particionada
    FOR VALUES FROM ('2024-01-01') TO ('2025-01-01');
CREATE TABLE ventas_2025 PARTITION OF ventas_particionada
    FOR VALUES FROM ('2025-01-01') TO ('2026-01-01');
CREATE TABLE ventas_2026 PARTITION OF ventas_particionada
    FOR VALUES FROM ('2026-01-01') TO ('2027-01-01');

-- Las queries automáticamente solo escanean la partición relevante
EXPLAIN SELECT * FROM ventas_particionada WHERE fecha >= '2025-06-01';
-- Solo toca ventas_2025 y ventas_2026 (partition pruning)
```

---

## 5. CTEs Recursivas (Jerarquías)

```sql
-- Árbol organizacional completo (empleado → jefe → director)
WITH RECURSIVE jerarquia AS (
    -- Caso base: el director (sin jefe)
    SELECT id, nombre, apellido, puesto, jefe_id, 1 AS nivel,
           nombre || ' ' || apellido AS ruta
    FROM empleados WHERE jefe_id IS NULL

    UNION ALL

    -- Caso recursivo: empleados que reportan al nivel anterior
    SELECT e.id, e.nombre, e.apellido, e.puesto, e.jefe_id, j.nivel + 1,
           j.ruta || ' → ' || e.nombre || ' ' || e.apellido
    FROM empleados e
    JOIN jerarquia j ON e.jefe_id = j.id
)
SELECT nivel, REPEAT('  ', nivel-1) || nombre || ' ' || apellido AS organigrama, puesto
FROM jerarquia ORDER BY ruta;
```

---

## 6. LISTEN/NOTIFY (Pub/Sub nativo)

```sql
-- Notificaciones en tiempo real sin polling
-- Terminal 1 (escucha):
LISTEN nuevo_pedido;

-- Terminal 2 (notifica):
NOTIFY nuevo_pedido, '{"venta_id": 5001, "total": 15000}';

-- En un trigger:
CREATE OR REPLACE FUNCTION fn_notificar_venta()
RETURNS TRIGGER AS $$
BEGIN
    PERFORM pg_notify('nuevo_pedido', json_build_object('id', NEW.id, 'total', NEW.total)::text);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;
```

---

## 7. Ejercicios

1. Almacena las preferencias de usuario en JSONB y consulta por tema oscuro
2. Implementa búsqueda full-text en productos con ranking de relevancia
3. Crea una tabla particionada de logs por mes
4. Genera el organigrama completo con CTE recursiva
5. Implementa búsqueda fuzzy que tolere errores de escritura
6. Usa LISTEN/NOTIFY para alertar cuando el stock baja del mínimo
7. Compara rendimiento: JSONB vs tabla normalizada para datos flexibles

---

## Siguiente Módulo
→ [17-MySQL](../17-mysql/README.md)
