-- Proyecto 11: Schema Full Stack Ventas
CREATE TABLE roles (id SERIAL PRIMARY KEY, nombre VARCHAR(30) UNIQUE NOT NULL);
CREATE TABLE usuarios (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE NOT NULL, password_hash VARCHAR(255) NOT NULL, email VARCHAR(100), nombre VARCHAR(50), rol_id INT REFERENCES roles(id), activo BOOLEAN DEFAULT TRUE, created_at TIMESTAMP DEFAULT NOW());
CREATE TABLE categorias (id SERIAL PRIMARY KEY, nombre VARCHAR(50) UNIQUE NOT NULL);
CREATE TABLE clientes (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL, apellido VARCHAR(50), email VARCHAR(100) UNIQUE, telefono VARCHAR(20), ciudad VARCHAR(50), tipo VARCHAR(20) DEFAULT 'REGULAR', activo BOOLEAN DEFAULT TRUE);
CREATE TABLE productos (id SERIAL PRIMARY KEY, nombre VARCHAR(150) NOT NULL, precio NUMERIC(10,2) NOT NULL, stock INT DEFAULT 0, sku VARCHAR(50) UNIQUE, categoria_id INT REFERENCES categorias(id), activo BOOLEAN DEFAULT TRUE, created_at TIMESTAMP DEFAULT NOW());
CREATE TABLE ventas (id SERIAL PRIMARY KEY, numero VARCHAR(20) UNIQUE NOT NULL, fecha TIMESTAMP DEFAULT NOW(), cliente_id INT REFERENCES clientes(id), usuario_id INT REFERENCES usuarios(id), subtotal NUMERIC(12,2) DEFAULT 0, impuesto NUMERIC(12,2) DEFAULT 0, total NUMERIC(12,2) DEFAULT 0, estado VARCHAR(20) DEFAULT 'COMPLETADA');
CREATE TABLE detalle_venta (id SERIAL PRIMARY KEY, venta_id INT REFERENCES ventas(id) ON DELETE CASCADE, producto_id INT REFERENCES productos(id), cantidad INT CHECK(cantidad>0), precio_unitario NUMERIC(10,2), subtotal NUMERIC(12,2));

-- Datos iniciales
INSERT INTO roles (nombre) VALUES ('ADMIN'),('VENDEDOR');
-- Password: admin123 (BCrypt hash)
INSERT INTO usuarios (username, password_hash, nombre, rol_id) VALUES ('admin','$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy','Administrador',1);
INSERT INTO usuarios (username, password_hash, nombre, rol_id) VALUES ('vendedor','$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi','Vendedor',2);
INSERT INTO categorias (nombre) VALUES ('Electrónica'),('Periféricos'),('Mobiliario');
INSERT INTO clientes (nombre, apellido, email, ciudad, tipo) VALUES ('Pedro','Ruiz','pedro@mail.com','CDMX','VIP'),('Laura','Sánchez','laura@mail.com','Monterrey','REGULAR');
INSERT INTO productos (nombre, precio, stock, sku, categoria_id) VALUES ('Laptop HP',18999,25,'LAP-001',1),('Monitor Dell',12499,15,'MON-001',1),('Teclado MX',2899,40,'TEC-001',2),('Mouse MX',1899,50,'MOU-001',2),('Silla Ergo',8999,8,'SIL-001',3);
