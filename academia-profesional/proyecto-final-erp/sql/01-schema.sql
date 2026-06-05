-- ERP Empresarial - Schema completo
CREATE TABLE roles (id SERIAL PRIMARY KEY, nombre VARCHAR(30) UNIQUE NOT NULL, descripcion VARCHAR(100));
CREATE TABLE usuarios (id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE NOT NULL, password_hash VARCHAR(255) NOT NULL, email VARCHAR(100) UNIQUE, nombre VARCHAR(50), apellido VARCHAR(50), rol_id INT REFERENCES roles(id), activo BOOLEAN DEFAULT TRUE, created_at TIMESTAMP DEFAULT NOW());
CREATE TABLE sucursales (id SERIAL PRIMARY KEY, nombre VARCHAR(100) NOT NULL, ciudad VARCHAR(50), activa BOOLEAN DEFAULT TRUE);
CREATE TABLE categorias (id SERIAL PRIMARY KEY, nombre VARCHAR(50) UNIQUE NOT NULL);
CREATE TABLE proveedores (id SERIAL PRIMARY KEY, nombre VARCHAR(100) NOT NULL, contacto VARCHAR(100), telefono VARCHAR(20), email VARCHAR(100));
CREATE TABLE clientes (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL, apellido VARCHAR(50), email VARCHAR(100) UNIQUE, telefono VARCHAR(20), ciudad VARCHAR(50), tipo VARCHAR(20) DEFAULT 'REGULAR', activo BOOLEAN DEFAULT TRUE, created_at TIMESTAMP DEFAULT NOW());
CREATE TABLE productos (id SERIAL PRIMARY KEY, nombre VARCHAR(150) NOT NULL, precio NUMERIC(10,2) NOT NULL CHECK(precio>0), costo NUMERIC(10,2), sku VARCHAR(50) UNIQUE, categoria_id INT REFERENCES categorias(id), proveedor_id INT REFERENCES proveedores(id), activo BOOLEAN DEFAULT TRUE, created_at TIMESTAMP DEFAULT NOW());
CREATE TABLE inventario (id SERIAL PRIMARY KEY, producto_id INT NOT NULL REFERENCES productos(id), sucursal_id INT NOT NULL REFERENCES sucursales(id), cantidad INT DEFAULT 0 CHECK(cantidad>=0), minimo INT DEFAULT 10, UNIQUE(producto_id, sucursal_id));
CREATE TABLE ventas (id SERIAL PRIMARY KEY, numero VARCHAR(20) UNIQUE NOT NULL, fecha TIMESTAMP DEFAULT NOW(), cliente_id INT REFERENCES clientes(id), usuario_id INT NOT NULL REFERENCES usuarios(id), sucursal_id INT NOT NULL REFERENCES sucursales(id), subtotal NUMERIC(12,2) DEFAULT 0, impuesto NUMERIC(12,2) DEFAULT 0, total NUMERIC(12,2) DEFAULT 0, estado VARCHAR(20) DEFAULT 'COMPLETADA', metodo_pago VARCHAR(20) DEFAULT 'EFECTIVO');
CREATE TABLE detalle_venta (id SERIAL PRIMARY KEY, venta_id INT NOT NULL REFERENCES ventas(id) ON DELETE CASCADE, producto_id INT NOT NULL REFERENCES productos(id), cantidad INT NOT NULL CHECK(cantidad>0), precio_unitario NUMERIC(10,2) NOT NULL, subtotal NUMERIC(12,2) NOT NULL);
CREATE TABLE auditoria (id SERIAL PRIMARY KEY, tabla VARCHAR(50) NOT NULL, operacion VARCHAR(10) NOT NULL, registro_id INT, datos JSONB, usuario VARCHAR(50), fecha TIMESTAMP DEFAULT NOW());

-- Índices
CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_detalle_producto ON detalle_venta(producto_id);
CREATE INDEX idx_auditoria_fecha ON auditoria(fecha);
