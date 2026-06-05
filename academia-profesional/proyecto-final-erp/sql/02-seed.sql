-- ERP - Datos iniciales
INSERT INTO roles (nombre, descripcion) VALUES ('ADMIN','Administrador'),('GERENTE','Gerente'),('VENDEDOR','Vendedor');
INSERT INTO usuarios (username, password_hash, email, nombre, apellido, rol_id) VALUES
('admin','$2a$12$hash_admin','admin@erp.com','Admin','Sistema',1),
('gerente','$2a$12$hash_gerente','gerente@erp.com','Carlos','García',2),
('vendedor','$2a$12$hash_vendedor','vendedor@erp.com','María','López',3);
INSERT INTO sucursales (nombre, ciudad) VALUES ('Central CDMX','Ciudad de México'),('Monterrey','Monterrey'),('Guadalajara','Guadalajara');
INSERT INTO categorias (nombre) VALUES ('Electrónica'),('Periféricos'),('Mobiliario'),('Software'),('Redes');
INSERT INTO proveedores (nombre, contacto, email) VALUES ('TechDist','Roberto','ventas@techdist.mx'),('CompuMayoreo','Laura','laura@compumayoreo.com');
INSERT INTO clientes (nombre, apellido, email, ciudad, tipo) VALUES ('Juan','Pérez','juan@mail.com','CDMX','REGULAR'),('Ana','Vega','ana@mail.com','Monterrey','VIP'),('Pedro','Ruiz','pedro@mail.com','Guadalajara','REGULAR');
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id) VALUES
('Laptop HP ProBook',18999,12000,'HP-PB-001',1,1),('Monitor Dell 27"',12499,8000,'DELL-27-001',1,1),
('Teclado Logitech MX',2899,1800,'LOG-MX-001',2,2),('Mouse MX Master',1899,1200,'LOG-MM-001',2,2),
('Silla Ergonómica',8999,5500,'SILLA-ERG-001',3,2);
INSERT INTO inventario (producto_id, sucursal_id, cantidad, minimo) VALUES (1,1,25,5),(1,2,15,5),(2,1,20,5),(3,1,40,10),(4,1,35,10),(5,1,8,3);
