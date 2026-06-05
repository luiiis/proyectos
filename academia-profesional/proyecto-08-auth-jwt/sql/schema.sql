-- Proyecto 08: Schema para Auth JWT
-- JPA crea la tabla automáticamente, pero aquí está el SQL explícito

CREATE TABLE IF NOT EXISTS usuarios (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,  -- BCrypt hash
    email VARCHAR(100) UNIQUE,
    nombre VARCHAR(50),
    apellido VARCHAR(50),
    rol VARCHAR(20) DEFAULT 'USER',  -- ADMIN, GERENTE, VENDEDOR, USER
    activo BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

-- Usuario admin de prueba (password: admin123)
-- Hash generado con BCrypt strength 12
INSERT INTO usuarios (username, password, email, nombre, rol) VALUES
('admin', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy', 'admin@erp.com', 'Administrador', 'ADMIN')
ON CONFLICT (username) DO NOTHING;
