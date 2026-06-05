-- Proyecto 07: Schema para API Productos
-- Se crea automáticamente con JPA (ddl-auto:update) pero aquí está el SQL explícito
-- Para crear manualmente: docker exec -i productos-postgres psql -U postgres -d productos_db < sql/schema.sql

CREATE TABLE IF NOT EXISTS productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL CHECK (precio > 0),
    costo NUMERIC(10,2),
    stock INTEGER NOT NULL DEFAULT 0 CHECK (stock >= 0),
    sku VARCHAR(50) UNIQUE,
    categoria VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Datos de prueba
INSERT INTO productos (nombre, precio, costo, stock, sku, categoria) VALUES
('Laptop HP ProBook', 18999.99, 12000, 25, 'LAP-001', 'Electrónica'),
('Monitor Dell 27"', 12499.00, 8000, 15, 'MON-001', 'Electrónica'),
('Teclado Logitech MX', 2899.00, 1800, 40, 'TEC-001', 'Periféricos'),
('Mouse MX Master 3S', 1899.00, 1200, 50, 'MOU-001', 'Periféricos'),
('Silla Ergonómica', 8999.00, 5500, 8, 'SIL-001', 'Mobiliario'),
('SSD Samsung 1TB', 2199.00, 1400, 30, 'SSD-001', 'Almacenamiento'),
('Webcam Elgato 4K', 5999.00, 3800, 20, 'WEB-001', 'Periféricos'),
('Audífonos Sony XM5', 7499.00, 4500, 18, 'AUD-001', 'Audio'),
('Hub USB-C 7en1', 1299.00, 800, 45, 'HUB-001', 'Accesorios'),
('UPS CyberPower 1500VA', 4599.00, 2800, 12, 'UPS-001', 'Energía')
ON CONFLICT (sku) DO NOTHING;
