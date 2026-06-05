-- ════════════════════════════════════════════════════════════════
-- V1: Schema inicial - PostgreSQL 16
-- ════════════════════════════════════════════════════════════════
-- Flyway ejecuta este archivo UNA sola vez y registra que ya se aplicó.
-- Si necesitas cambios, creas V2__nombre.sql (NUNCA modificas V1).
--
-- ¿Por qué Flyway en lugar de hibernate.ddl-auto=update?
-- 1. CONTROL: sabes exactamente qué SQL se ejecuta
-- 2. VERSIONADO: cada cambio tiene su archivo (auditable en Git)
-- 3. ROLLBACK: puedes revertir migraciones
-- 4. PRODUCCIÓN: ddl-auto=update puede borrar columnas o datos
-- 5. EQUIPO: todos tienen la misma BD (no "en mi máquina funciona")

-- ═══════ ROLES ═══════
CREATE TABLE roles (
    id          BIGSERIAL PRIMARY KEY,  -- BIGSERIAL = auto-increment en PostgreSQL
    name        VARCHAR(30) NOT NULL UNIQUE,
    description VARCHAR(100)
);

-- ═══════ USUARIOS ═══════
CREATE TABLE users (
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(50) NOT NULL UNIQUE,
    email        VARCHAR(100) NOT NULL UNIQUE,
    keycloak_id  VARCHAR(255) UNIQUE,  -- ID del usuario en Keycloak
    first_name   VARCHAR(50),
    last_name    VARCHAR(50),
    phone        VARCHAR(20),
    is_active    BOOLEAN DEFAULT TRUE,
    created_at   TIMESTAMPTZ DEFAULT NOW(),  -- TIMESTAMPTZ = con timezone (buena práctica)
    updated_at   TIMESTAMPTZ DEFAULT NOW()
);

-- Índices para búsquedas frecuentes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_keycloak ON users(keycloak_id);

-- ═══════ USER_ROLES (M:N) ═══════
CREATE TABLE user_roles (
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    role_id BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, role_id)
);

-- ═══════ PRODUCTOS ═══════
CREATE TABLE products (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(150) NOT NULL,
    description VARCHAR(1000),
    price       NUMERIC(12, 2) NOT NULL CHECK (price > 0),  -- CHECK constraint en BD
    stock       INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    category    VARCHAR(50),
    sku         VARCHAR(50) UNIQUE,
    image_url   VARCHAR(500),
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    updated_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_products_category ON products(category);
CREATE INDEX idx_products_active ON products(is_active) WHERE is_active = TRUE;  -- Partial index
CREATE INDEX idx_products_name_search ON products USING gin(to_tsvector('spanish', name));  -- Full-text search

-- ═══════ MOVIMIENTOS DE INVENTARIO ═══════
CREATE TABLE inventory_movements (
    id          BIGSERIAL PRIMARY KEY,
    product_id  BIGINT NOT NULL REFERENCES products(id),
    type        VARCHAR(10) NOT NULL CHECK (type IN ('ENTRY', 'EXIT')),
    quantity    INTEGER NOT NULL CHECK (quantity > 0),
    reason      VARCHAR(200),
    created_by  VARCHAR(50),
    created_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_inventory_product ON inventory_movements(product_id);

-- ═══════ AUDIT LOG (usando JSONB de PostgreSQL) ═══════
-- PostgreSQL tiene JSONB nativo: almacena JSON indexable y queryable
-- No necesitas SQL Server separado para esto
CREATE TABLE audit_logs (
    id          BIGSERIAL PRIMARY KEY,
    action      VARCHAR(50) NOT NULL,
    entity_type VARCHAR(50) NOT NULL,
    entity_id   BIGINT,
    username    VARCHAR(50),
    details     JSONB,  -- Datos flexibles en formato JSON (indexable)
    ip_address  INET,   -- Tipo nativo de PostgreSQL para IPs
    created_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_audit_username ON audit_logs(username);
CREATE INDEX idx_audit_action ON audit_logs(action);
CREATE INDEX idx_audit_created ON audit_logs(created_at);
CREATE INDEX idx_audit_details ON audit_logs USING gin(details);  -- Índice en JSONB
