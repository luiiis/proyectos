-- Datos de prueba
INSERT INTO categorias (nombre) VALUES ('Electrónica'),('Periféricos'),('Mobiliario'),('Software'),('Redes');
INSERT INTO proveedores (nombre, email) VALUES ('TechDist','ventas@techdist.mx'),('CompuMayoreo','info@compumayoreo.com'),('DigitalSupply','contacto@digital.mx');
INSERT INTO sucursales (nombre, ciudad) VALUES ('Central CDMX','Ciudad de México'),('Monterrey','Monterrey'),('Guadalajara','Guadalajara');
INSERT INTO empleados (nombre, apellido, puesto, salario, sucursal_id) VALUES ('Carlos','García','Gerente',75000,1),('María','López','Vendedor',35000,1),('Juan','Martínez','Vendedor',35000,2),('Ana','Vega','Vendedor',35000,3);
INSERT INTO clientes (nombre, apellido, email, ciudad, tipo) VALUES ('Pedro','Ruiz','pedro@mail.com','CDMX','VIP'),('Laura','Sánchez','laura@mail.com','Monterrey','REGULAR'),('Diego','Torres','diego@mail.com','Guadalajara','REGULAR');
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id) VALUES
('Laptop HP',18999,12000,'LAP-001',1,1),('Monitor Dell',12499,8000,'MON-001',1,1),('Teclado MX',2899,1800,'TEC-001',2,2),
('Mouse MX',1899,1200,'MOU-001',2,2),('Silla Ergo',8999,5500,'SIL-001',3,3),('SSD 1TB',2199,1400,'SSD-001',1,1);
INSERT INTO inventario (producto_id, sucursal_id, cantidad) VALUES (1,1,25),(1,2,15),(2,1,20),(3,1,40),(4,1,50),(5,1,8),(6,1,30);
INSERT INTO ventas (numero, cliente_id, empleado_id, sucursal_id, total) VALUES ('V-001',1,2,1,56999.97),('V-002',2,3,2,2899.00),('V-003',3,4,3,14998.00);
INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal) VALUES (1,1,3,18999.99,56999.97),(2,3,1,2899,2899),(3,4,2,1899,3798),(3,2,1,12499,12499);
