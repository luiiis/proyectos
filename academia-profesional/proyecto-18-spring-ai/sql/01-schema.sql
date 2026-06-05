-- Habilitar extension pgvector (almacenar embeddings)
CREATE EXTENSION IF NOT EXISTS vector;

-- Tabla de productos con columna de embedding
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio NUMERIC(10,2) NOT NULL,
    categoria VARCHAR(50),
    caracteristicas TEXT,  -- "16GB RAM, 512GB SSD, Intel i7"
    embedding vector(1536),  -- Vector de 1536 dimensiones (OpenAI ada-002)
    activo BOOLEAN DEFAULT TRUE
);

-- Indice para busqueda por similitud (cosine distance)
CREATE INDEX idx_productos_embedding ON productos USING ivfflat (embedding vector_cosine_ops);

-- Tabla para historial de conversaciones
CREATE TABLE conversaciones (
    id SERIAL PRIMARY KEY,
    session_id VARCHAR(50) NOT NULL,
    rol VARCHAR(10) NOT NULL,  -- 'user' o 'assistant'
    contenido TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Datos de prueba
INSERT INTO productos (nombre, descripcion, precio, categoria, caracteristicas) VALUES
('Laptop HP ProBook 450', 'Laptop empresarial para trabajo pesado', 18999, 'Laptops', '32GB RAM, 1TB SSD, Intel Core i7, 15.6 pulgadas, Windows 11 Pro'),
('MacBook Air M3', 'Laptop ultraligera de Apple', 24999, 'Laptops', '16GB RAM, 512GB SSD, Chip M3, 13.6 pulgadas, macOS'),
('Dell XPS 15', 'Laptop premium con pantalla OLED', 35999, 'Laptops', '32GB RAM, 1TB SSD, Intel Core Ultra 9, 15.6 OLED, Windows 11'),
('Monitor LG 27 4K', 'Monitor profesional para diseno', 12499, 'Monitores', '27 pulgadas, 4K UHD, USB-C, HDR400, IPS'),
('Teclado Logitech MX Keys', 'Teclado inalambrico premium', 2899, 'Perifericos', 'Bluetooth, retroiluminado, multi-dispositivo, recargable'),
('Silla Herman Miller Aeron', 'Silla ergonomica premium', 32500, 'Mobiliario', 'Soporte lumbar, ajustable, malla transpirable, garantia 12 anos'),
('Webcam Elgato Facecam Pro', 'Camara 4K para videollamadas', 5999, 'Perifericos', '4K 60fps, sensor Sony, sin compresion, USB-C'),
('Audifonos Sony WH-1000XM5', 'Audifonos con cancelacion de ruido', 7499, 'Audio', 'ANC, 30hrs bateria, Bluetooth 5.3, LDAC, plegables');
