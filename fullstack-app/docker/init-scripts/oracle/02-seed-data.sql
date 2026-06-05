-- ============================================================
-- Oracle XE - Datos iniciales (Seed)
-- Productos de ejemplo para tienda pequeña
-- ============================================================

-- ==================== PRODUCTOS ====================
INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Laptop HP ProBook 450 G9', 'Laptop empresarial 15.6" Intel Core i5, 16GB RAM, 512GB SSD', 18999.99, 25, 'Electrónica', 'HP-PB450-G9', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Monitor Dell 27" 4K', 'Monitor UltraSharp U2723QE, USB-C, 4K UHD', 12499.00, 15, 'Electrónica', 'DELL-U2723QE', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Teclado Mecánico Logitech MX', 'Teclado inalámbrico mecánico, retroiluminado, multi-dispositivo', 2899.00, 40, 'Periféricos', 'LOG-MX-MECH', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Mouse Logitech MX Master 3S', 'Mouse ergonómico inalámbrico, sensor 8000 DPI', 1899.00, 35, 'Periféricos', 'LOG-MXM3S', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Silla Ergonómica Herman Miller', 'Silla de oficina Aeron, soporte lumbar ajustable', 28500.00, 8, 'Mobiliario', 'HM-AERON-B', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Escritorio Eléctrico Standing', 'Escritorio ajustable en altura 120x60cm, motor dual', 8999.00, 12, 'Mobiliario', 'DESK-ELEC-120', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Webcam Logitech Brio 4K', 'Cámara web 4K HDR, enfoque automático, micrófono dual', 3499.00, 20, 'Periféricos', 'LOG-BRIO4K', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Audífonos Sony WH-1000XM5', 'Audífonos over-ear, cancelación de ruido, 30hrs batería', 6999.00, 18, 'Audio', 'SONY-WH1000XM5', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Hub USB-C Anker 7 en 1', 'Hub multipuerto: HDMI 4K, USB 3.0, SD, Ethernet', 1299.00, 50, 'Accesorios', 'ANK-HUB7IN1', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Disco SSD Samsung 1TB', 'SSD NVMe M.2 970 EVO Plus, lectura 3500MB/s', 2199.00, 30, 'Almacenamiento', 'SAM-970EVO-1TB', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Cable HDMI 2.1 3m', 'Cable HDMI 8K 60Hz, 4K 120Hz, eARC', 499.00, 100, 'Accesorios', 'HDMI21-3M', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Memoria RAM DDR5 32GB', 'Kit 2x16GB DDR5 5600MHz, Kingston Fury Beast', 3299.00, 22, 'Componentes', 'KNG-DDR5-32', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'UPS APC 1500VA', 'No-break 1500VA/900W, 8 contactos, regulador', 4599.00, 10, 'Energía', 'APC-BX1500', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Impresora HP LaserJet Pro', 'Impresora láser monocromática, WiFi, dúplex automático', 5999.00, 7, 'Impresión', 'HP-LJ-PRO-M404', 1);

INSERT INTO products (id, name, description, price, stock, category, sku, is_active) VALUES
(PRODUCT_SEQ.NEXTVAL, 'Tablet Samsung Galaxy Tab S9', 'Tablet 11" AMOLED, 128GB, S-Pen incluido', 13999.00, 14, 'Electrónica', 'SAM-TABS9-128', 1);

-- ==================== MOVIMIENTOS DE INVENTARIO ====================
-- Entradas iniciales de stock
INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 1, 'ENTRY', 25, 'Stock inicial - Compra proveedor HP México', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 2, 'ENTRY', 15, 'Stock inicial - Compra proveedor Dell', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 3, 'ENTRY', 40, 'Stock inicial - Compra proveedor Logitech', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 4, 'ENTRY', 35, 'Stock inicial - Compra proveedor Logitech', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 5, 'ENTRY', 8, 'Stock inicial - Compra proveedor Herman Miller', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 6, 'ENTRY', 12, 'Stock inicial - Compra proveedor mobiliario', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 7, 'ENTRY', 20, 'Stock inicial - Compra proveedor Logitech', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 8, 'ENTRY', 18, 'Stock inicial - Compra proveedor Sony', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 9, 'ENTRY', 50, 'Stock inicial - Compra proveedor Anker', 'admin');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 10, 'ENTRY', 30, 'Stock inicial - Compra proveedor Samsung', 'admin');

-- Algunas salidas de ejemplo
INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 1, 'EXIT', 3, 'Venta - Orden #V001', 'manager1');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 3, 'EXIT', 5, 'Venta - Orden #V002', 'manager1');

INSERT INTO inventory_movements (id, product_id, type, quantity, reason, created_by) VALUES
(INVENTORY_SEQ.NEXTVAL, 8, 'EXIT', 2, 'Venta - Orden #V003', 'manager1');

-- ==================== VENTAS DE EJEMPLO ====================
INSERT INTO sales (id, sale_number, customer_name, customer_email, total_amount, status, notes, created_by) VALUES
(SALE_SEQ.NEXTVAL, 'V-2024-001', 'Empresa TechCorp SA', 'compras@techcorp.com', 56999.97, 'COMPLETED', 'Compra de 3 laptops para nuevo personal', 'manager1');

INSERT INTO sales (id, sale_number, customer_name, customer_email, total_amount, status, notes, created_by) VALUES
(SALE_SEQ.NEXTVAL, 'V-2024-002', 'Roberto Sánchez', 'roberto.sanchez@email.com', 14495.00, 'COMPLETED', 'Teclados y accesorios para oficina', 'manager1');

INSERT INTO sales (id, sale_number, customer_name, customer_email, total_amount, status, notes, created_by) VALUES
(SALE_SEQ.NEXTVAL, 'V-2024-003', 'Consultora Digital MX', 'admin@digitalconsulting.mx', 13998.00, 'COMPLETED', 'Audífonos para equipo remoto', 'manager1');

-- ==================== DETALLE DE VENTAS ====================
-- Venta V-2024-001: 3 Laptops HP
INSERT INTO sale_details (id, sale_id, product_id, quantity, unit_price, subtotal) VALUES
(SALE_DETAIL_SEQ.NEXTVAL, 1, 1, 3, 18999.99, 56999.97);

-- Venta V-2024-002: 5 Teclados
INSERT INTO sale_details (id, sale_id, product_id, quantity, unit_price, subtotal) VALUES
(SALE_DETAIL_SEQ.NEXTVAL, 2, 3, 5, 2899.00, 14495.00);

-- Venta V-2024-003: 2 Audífonos Sony
INSERT INTO sale_details (id, sale_id, product_id, quantity, unit_price, subtotal) VALUES
(SALE_DETAIL_SEQ.NEXTVAL, 3, 8, 2, 6999.00, 13998.00);

COMMIT;
