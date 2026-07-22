-- Proyecto 12: Schema para demostrar Docker
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    precio NUMERIC(10,2) NOT NULL,
    stock INT DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO productos (nombre, precio, stock) VALUES
('Laptop HP ProBook', 18999.00, 25),
('Monitor Dell 27"', 12499.00, 15),
('Teclado MX Keys', 2899.00, 40),
('Mouse MX Master 3', 1899.00, 50),
('Silla Ergonómica', 8999.00, 8),
('SSD Samsung 1TB', 2199.00, 30);
