-- ════════════════════════════════════════════════════════════════
-- MÓDULO 02: DDL - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 02: DDL - Crear y Modificar Tablas ═══'

-- Crear tabla de ejemplo
DROP TABLE IF EXISTS demo_departamentos CASCADE;
CREATE TABLE demo_departamentos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    presupuesto NUMERIC(12,2) CHECK (presupuesto > 0),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

\echo '✓ Tabla demo_departamentos creada'

-- Insertar datos de prueba
INSERT INTO demo_departamentos (nombre, presupuesto) VALUES
('Tecnología', 500000),
('Ventas', 300000),
('Marketing', 200000);

-- Modificar tabla
ALTER TABLE demo_departamentos ADD COLUMN gerente VARCHAR(100);
ALTER TABLE demo_departamentos ADD COLUMN ubicacion VARCHAR(50) DEFAULT 'CDMX';

\echo '✓ Columnas agregadas'

-- Ver estructura final
\d demo_departamentos

-- Ver datos
SELECT * FROM demo_departamentos;

-- Limpiar
DROP TABLE demo_departamentos;
\echo '✓ Tabla eliminada (limpieza)'
