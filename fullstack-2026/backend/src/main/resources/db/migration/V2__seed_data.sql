-- ════════════════════════════════════════════════════════════════
-- V2: Datos iniciales (Seed)
-- ════════════════════════════════════════════════════════════════

-- Roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Administrador - acceso total'),
('MANAGER', 'Gerente - gestión de productos e inventario'),
('USER', 'Usuario estándar - solo lectura');

-- Usuarios (passwords manejados por Keycloak, aquí solo datos de perfil)
INSERT INTO users (username, email, keycloak_id, first_name, last_name, phone) VALUES
('admin', 'admin@sistema.com', 'kc-admin-001', 'Admin', 'Sistema', '+52 555 0001000'),
('manager1', 'manager@sistema.com', 'kc-manager-001', 'Carlos', 'García', '+52 555 0002000'),
('user1', 'usuario@sistema.com', 'kc-user-001', 'María', 'Hernández', '+52 555 0003000');

-- Asignar roles
INSERT INTO user_roles (user_id, role_id) VALUES
(1, 1), (1, 3),  -- admin: ADMIN + USER
(2, 2), (2, 3),  -- manager1: MANAGER + USER
(3, 3);           -- user1: USER

-- Productos
INSERT INTO products (name, description, price, stock, category, sku) VALUES
('MacBook Pro 16" M4', 'Laptop Apple con chip M4 Pro, 36GB RAM, 1TB SSD', 54999.00, 15, 'Laptops', 'APPLE-MBP16-M4'),
('Dell XPS 15', 'Laptop Dell 15.6" OLED, Intel Core Ultra 9, 32GB RAM', 38999.00, 20, 'Laptops', 'DELL-XPS15-2025'),
('Monitor LG 27" 4K', 'Monitor UltraFine 27UN850, USB-C, HDR400', 12499.00, 30, 'Monitores', 'LG-27UN850'),
('Teclado Keychron Q1 Pro', 'Teclado mecánico 75%, hot-swap, Bluetooth', 3899.00, 45, 'Periféricos', 'KEY-Q1PRO'),
('Mouse Logitech MX Master 3S', 'Mouse ergonómico, 8000 DPI, multi-dispositivo', 1899.00, 50, 'Periféricos', 'LOG-MXM3S'),
('Silla Herman Miller Aeron', 'Silla ergonómica, soporte lumbar PostureFit', 32500.00, 8, 'Mobiliario', 'HM-AERON-C'),
('Webcam Elgato Facecam Pro', 'Cámara 4K60, sensor Sony, sin compresión', 5999.00, 25, 'Periféricos', 'ELG-FCPRO'),
('Audífonos Sony WH-1000XM5', 'Over-ear, ANC, 30hrs batería, LDAC', 7499.00, 35, 'Audio', 'SONY-XM5'),
('Hub Thunderbolt CalDigit TS4', 'Dock 18 puertos, 98W carga, 2.5GbE', 8999.00, 12, 'Accesorios', 'CAL-TS4'),
('SSD Samsung 990 Pro 2TB', 'NVMe M.2, 7450MB/s lectura, con heatsink', 4299.00, 40, 'Almacenamiento', 'SAM-990PRO-2T'),
('iPad Pro 13" M4', 'Tablet Apple, pantalla Tandem OLED, 256GB', 27999.00, 18, 'Tablets', 'APPLE-IPADPRO13'),
('Raspberry Pi 5 8GB', 'Microcomputadora ARM, 8GB RAM, WiFi 6', 1899.00, 60, 'Componentes', 'RPI5-8GB'),
('UPS CyberPower 1500VA', 'No-break 1500VA/900W, 12 contactos, USB', 4599.00, 15, 'Energía', 'CP-1500VA'),
('Cable USB-C Thunderbolt 4', 'Cable 0.8m, 40Gbps, 100W PD, certificado', 899.00, 100, 'Accesorios', 'TB4-CABLE-08'),
('Impresora Brother HL-L2460DW', 'Láser mono, WiFi, dúplex, 36ppm', 4999.00, 10, 'Impresión', 'BRO-L2460DW');

-- Movimientos de inventario iniciales
INSERT INTO inventory_movements (product_id, type, quantity, reason, created_by) VALUES
(1, 'ENTRY', 15, 'Stock inicial - Compra proveedor Apple', 'admin'),
(2, 'ENTRY', 20, 'Stock inicial - Compra proveedor Dell', 'admin'),
(3, 'ENTRY', 30, 'Stock inicial - Compra proveedor LG', 'admin'),
(4, 'ENTRY', 45, 'Stock inicial - Compra proveedor Keychron', 'admin'),
(5, 'ENTRY', 50, 'Stock inicial - Compra proveedor Logitech', 'admin'),
(1, 'EXIT', 2, 'Venta #V-2026-001 - TechCorp SA', 'manager1'),
(5, 'EXIT', 5, 'Venta #V-2026-002 - Oficina Digital MX', 'manager1'),
(8, 'EXIT', 3, 'Venta #V-2026-003 - Equipo remoto', 'manager1');

-- Audit logs de ejemplo (usando JSONB)
INSERT INTO audit_logs (action, entity_type, entity_id, username, details, ip_address) VALUES
('CREATE', 'Product', 1, 'admin', '{"name": "MacBook Pro 16\" M4", "price": 54999.00}'::jsonb, '192.168.1.1'),
('CREATE', 'Product', 2, 'admin', '{"name": "Dell XPS 15", "price": 38999.00}'::jsonb, '192.168.1.1'),
('STOCK_EXIT', 'Product', 1, 'manager1', '{"quantity": 2, "reason": "Venta #V-2026-001"}'::jsonb, '192.168.1.100'),
('LOGIN', 'User', 1, 'admin', '{"method": "keycloak", "success": true}'::jsonb, '192.168.1.1'),
('LOGIN', 'User', 2, 'manager1', '{"method": "keycloak", "success": true}'::jsonb, '192.168.1.100');
