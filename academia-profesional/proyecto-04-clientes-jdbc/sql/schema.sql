CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE,
    telefono VARCHAR(20),
    ciudad VARCHAR(50),
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO clientes (nombre, apellido, email, telefono, ciudad) VALUES
('Carlos', 'García', 'carlos@mail.com', '55-1234-5678', 'CDMX'),
('María', 'López', 'maria@mail.com', '81-2345-6789', 'Monterrey'),
('Juan', 'Martínez', 'juan@mail.com', '33-3456-7890', 'Guadalajara'),
('Ana', 'Vega', 'ana@mail.com', '22-4567-8901', 'Puebla'),
('Pedro', 'Sánchez', 'pedro@mail.com', '99-5678-9012', 'Cancún');
