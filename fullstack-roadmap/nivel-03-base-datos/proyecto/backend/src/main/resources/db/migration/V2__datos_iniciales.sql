-- ════════════════════════════════════════════════════════════════
-- V2: Datos iniciales (seed data para desarrollo)
-- ════════════════════════════════════════════════════════════════

-- Categorías
INSERT INTO categorias (nombre, descripcion) VALUES
('Electrónica', 'Dispositivos electrónicos y gadgets'),
('Periféricos', 'Teclados, mouse, webcams, audífonos'),
('Almacenamiento', 'Discos duros, SSDs, memorias USB'),
('Mobiliario', 'Escritorios, sillas, estantes'),
('Software', 'Licencias y suscripciones de software');

-- Productos
INSERT INTO productos (nombre, descripcion, precio, existencia, categoria_id, sku) VALUES
('Laptop HP ProBook', 'Laptop empresarial 16GB RAM, 512GB SSD', 18999.00, 25, 1, 'LAP-HP-001'),
('Monitor Dell 27"', 'Monitor 4K IPS USB-C', 12499.00, 15, 1, 'MON-DELL-001'),
('MacBook Air M3', 'Apple Silicon, 8GB RAM, 256GB', 24999.00, 10, 1, 'LAP-MAC-001'),
('Teclado MX Keys', 'Teclado inalámbrico Logitech', 2899.00, 40, 2, 'TEC-LOG-001'),
('Mouse MX Master 3S', 'Mouse ergonómico inalámbrico', 1899.00, 50, 2, 'MOU-LOG-001'),
('Webcam Elgato Facecam', 'Cámara 1080p60 para streaming', 5999.00, 20, 2, 'CAM-ELG-001'),
('SSD Samsung 1TB', 'NVMe M.2 PCIe 4.0', 2199.00, 30, 3, 'SSD-SAM-001'),
('SSD Kingston 500GB', 'SATA III 2.5 pulgadas', 899.00, 45, 3, 'SSD-KNG-001'),
('Silla Ergonómica Pro', 'Silla con soporte lumbar y cabeza', 8999.00, 8, 4, 'SIL-ERG-001'),
('Escritorio Elevable', 'Standing desk eléctrico 120x60', 12500.00, 5, 4, 'ESC-ELE-001'),
('Audífonos Sony WH-1000XM5', 'Noise cancelling premium', 7999.00, 18, 2, 'AUD-SON-001'),
('Hub USB-C 7 en 1', 'HDMI + USB + SD + Ethernet', 1299.00, 35, 2, 'HUB-USB-001'),
('iPad Pro 11"', 'Apple M2, 128GB WiFi', 19999.00, 7, 1, 'TAB-APP-001'),
('Memoria USB 128GB', 'USB 3.2 alta velocidad', 399.00, 60, 3, 'USB-KNG-001'),
('Licencia Office 365', 'Microsoft 365 Business anual', 2499.00, 100, 5, 'LIC-OFF-001');
