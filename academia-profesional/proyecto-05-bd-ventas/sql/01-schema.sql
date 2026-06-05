-- Proyecto 05: Base de Datos de Ventas completa
-- 10 tablas con relaciones, constraints, índices

CREATE TABLE categorias (id SERIAL PRIMARY KEY, nombre VARCHAR(50) UNIQUE NOT NULL);
CREATE TABLE proveedores (id SERIAL PRIMARY KEY, nombre VARCHAR(100) NOT NULL, telefono VARCHAR(20), email VARCHAR(100));
CREATE TABLE sucursales (id SERIAL PRIMARY KEY, nombre VARCHAR(100) NOT NULL, ciudad VARCHAR(50));
CREATE TABLE empleados (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL, apellido VARCHAR(50), puesto VARCHAR(50), salario NUMERIC(10,2) CHECK(salario>0), sucursal_id INT REFERENCES sucursales(id), fecha_ingreso DATE DEFAULT CURRENT_DATE);
CREATE TABLE clientes (id SERIAL PRIMARY KEY, nombre VARCHAR(50) NOT NULL, apellido VARCHAR(50), email VARCHAR(100) UNIQUE, ciudad VARCHAR(50), tipo VARCHAR(20) DEFAULT 'REGULAR');
CREATE TABLE productos (id SERIAL PRIMARY KEY, nombre VARCHAR(150) NOT NULL, precio NUMERIC(10,2) NOT NULL CHECK(precio>0), costo NUMERIC(10,2), sku VARCHAR(50) UNIQUE, categoria_id INT REFERENCES categorias(id), proveedor_id INT REFERENCES proveedores(id), activo BOOLEAN DEFAULT TRUE);
CREATE TABLE inventario (id SERIAL PRIMARY KEY, producto_id INT REFERENCES productos(id), sucursal_id INT REFERENCES sucursales(id), cantidad INT DEFAULT 0 CHECK(cantidad>=0), UNIQUE(producto_id, sucursal_id));
CREATE TABLE ventas (id SERIAL PRIMARY KEY, numero VARCHAR(20) UNIQUE NOT NULL, fecha TIMESTAMP DEFAULT NOW(), cliente_id INT REFERENCES clientes(id), empleado_id INT REFERENCES empleados(id), sucursal_id INT REFERENCES sucursales(id), total NUMERIC(12,2) DEFAULT 0, estado VARCHAR(20) DEFAULT 'COMPLETADA');
CREATE TABLE detalle_venta (id SERIAL PRIMARY KEY, venta_id INT REFERENCES ventas(id) ON DELETE CASCADE, producto_id INT REFERENCES productos(id), cantidad INT CHECK(cantidad>0), precio_unitario NUMERIC(10,2), subtotal NUMERIC(12,2));
CREATE TABLE auditoria (id SERIAL PRIMARY KEY, tabla VARCHAR(50), operacion VARCHAR(10), registro_id INT, datos JSONB, usuario VARCHAR(50), fecha TIMESTAMP DEFAULT NOW());

CREATE INDEX idx_ventas_fecha ON ventas(fecha);
CREATE INDEX idx_ventas_cliente ON ventas(cliente_id);
CREATE INDEX idx_detalle_venta ON detalle_venta(venta_id);
CREATE INDEX idx_productos_cat ON productos(categoria_id);
