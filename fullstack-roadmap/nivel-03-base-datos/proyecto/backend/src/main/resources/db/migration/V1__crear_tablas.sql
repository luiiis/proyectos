-- ════════════════════════════════════════════════════════════════
-- V1: Crear tablas iniciales - Categorías y Productos
-- Flyway ejecuta esto UNA VEZ y registra que ya se aplicó.
-- NUNCA modificar este archivo después de ejecutarlo.
-- Para cambios: crear V2__nombre.sql
-- ════════════════════════════════════════════════════════════════

-- Tabla de categorías
CREATE TABLE categorias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200),
    activa      BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla de productos
CREATE TABLE productos (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(150) NOT NULL,
    descripcion   VARCHAR(500),
    precio        DECIMAL(10,2) NOT NULL,
    existencia    INT NOT NULL DEFAULT 0,
    categoria_id  BIGINT,
    sku           VARCHAR(50) UNIQUE,
    activo        BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    -- Foreign Key: cada producto pertenece a una categoría
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id) REFERENCES categorias(id),

    -- Validaciones a nivel de BD
    CONSTRAINT chk_precio_positivo CHECK (precio > 0),
    CONSTRAINT chk_existencia_no_negativa CHECK (existencia >= 0)
);

-- Índices para búsquedas frecuentes
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_nombre ON productos(nombre);
CREATE INDEX idx_productos_activo ON productos(activo);
