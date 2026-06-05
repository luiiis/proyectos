-- ============================================================
-- SQL Server - Datos iniciales (Seed)
-- Logs de auditoría y sistema de ejemplo
-- ============================================================

USE logs_db;
GO

-- ==================== AUDIT LOGS ====================
INSERT INTO audit_logs (action, entity, entity_id, username, details, ip_address) VALUES
('REGISTER', 'User', 1, 'admin', 'Usuario administrador creado durante inicialización del sistema', '127.0.0.1'),
('REGISTER', 'User', 2, 'manager1', 'Usuario gerente registrado', '192.168.1.100'),
('REGISTER', 'User', 3, 'user1', 'Usuario estándar registrado', '192.168.1.101'),
('REGISTER', 'User', 4, 'user2', 'Usuario estándar registrado', '192.168.1.102'),
('REGISTER', 'User', 5, 'user3', 'Usuario estándar registrado', '192.168.1.103'),
('LOGIN', 'User', 1, 'admin', 'Inicio de sesión exitoso', '192.168.1.1'),
('LOGIN', 'User', 2, 'manager1', 'Inicio de sesión exitoso', '192.168.1.100'),
('LOGIN', 'User', 3, 'user1', 'Inicio de sesión exitoso', '192.168.1.101'),
('ASSIGN_ROLE', 'User', 2, 'admin', 'Rol MANAGER asignado a manager1', '192.168.1.1'),
('CREATE', 'Product', 1, 'admin', 'Producto creado: Laptop HP ProBook 450 G9', '192.168.1.1'),
('CREATE', 'Product', 2, 'admin', 'Producto creado: Monitor Dell 27" 4K', '192.168.1.1'),
('CREATE', 'Product', 3, 'admin', 'Producto creado: Teclado Mecánico Logitech MX', '192.168.1.1'),
('STOCK_ENTRY', 'Product', 1, 'admin', 'Entrada de stock: 25 unidades - Stock inicial', '192.168.1.1'),
('STOCK_ENTRY', 'Product', 2, 'admin', 'Entrada de stock: 15 unidades - Stock inicial', '192.168.1.1'),
('STOCK_EXIT', 'Product', 1, 'manager1', 'Salida de stock: 3 unidades - Venta V-2024-001', '192.168.1.100'),
('STOCK_EXIT', 'Product', 3, 'manager1', 'Salida de stock: 5 unidades - Venta V-2024-002', '192.168.1.100'),
('CREATE', 'Sale', 1, 'manager1', 'Venta V-2024-001 creada - Total: $56,999.97', '192.168.1.100'),
('CREATE', 'Sale', 2, 'manager1', 'Venta V-2024-002 creada - Total: $14,495.00', '192.168.1.100'),
('CREATE', 'Sale', 3, 'manager1', 'Venta V-2024-003 creada - Total: $13,998.00', '192.168.1.100'),
('UPDATE', 'User', 3, 'user1', 'Usuario actualizó su perfil (teléfono)', '192.168.1.101');
GO

-- ==================== SYSTEM LOGS ====================
INSERT INTO system_logs (level, service, message, request_url, request_method, response_status, duration_ms, username, ip_address) VALUES
('INFO', 'backend-auth', 'Aplicación iniciada correctamente en puerto 8080', NULL, NULL, NULL, NULL, NULL, '127.0.0.1'),
('INFO', 'backend-mail', 'Aplicación iniciada correctamente en puerto 8081', NULL, NULL, NULL, NULL, NULL, '127.0.0.1'),
('INFO', 'backend-auth', 'Conexión a MySQL establecida', NULL, NULL, NULL, 245, NULL, '127.0.0.1'),
('INFO', 'backend-auth', 'Conexión a Oracle establecida', NULL, NULL, NULL, 1230, NULL, '127.0.0.1'),
('INFO', 'backend-auth', 'Conexión a SQL Server establecida', NULL, NULL, NULL, 890, NULL, '127.0.0.1'),
('INFO', 'backend-auth', 'Login exitoso', '/api/auth/login', 'POST', 200, 156, 'admin', '192.168.1.1'),
('INFO', 'backend-auth', 'Producto creado exitosamente', '/api/products', 'POST', 200, 89, 'admin', '192.168.1.1'),
('WARN', 'backend-auth', 'Stock bajo detectado: Silla Ergonómica (8 unidades)', NULL, NULL, NULL, NULL, 'system', '127.0.0.1'),
('WARN', 'backend-auth', 'Stock bajo detectado: Impresora HP LaserJet (7 unidades)', NULL, NULL, NULL, NULL, 'system', '127.0.0.1'),
('ERROR', 'backend-mail', 'Error al enviar correo: Connection timeout', '/api/mail/send', 'POST', 500, 5000, 'admin', '192.168.1.1'),
('INFO', 'backend-mail', 'Correo de recuperación enviado exitosamente', '/api/mail/password-reset/request', 'POST', 200, 2340, NULL, '192.168.1.103'),
('INFO', 'backend-auth', 'Consulta de productos', '/api/products', 'GET', 200, 45, 'user1', '192.168.1.101'),
('INFO', 'backend-auth', 'Venta registrada: V-2024-001', '/api/sales', 'POST', 200, 234, 'manager1', '192.168.1.100');
GO

-- ==================== LOGIN ATTEMPTS ====================
INSERT INTO login_attempts (username, ip_address, success, failure_reason, user_agent) VALUES
('admin', '192.168.1.1', 1, NULL, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0'),
('manager1', '192.168.1.100', 1, NULL, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0'),
('user1', '192.168.1.101', 1, NULL, 'Mozilla/5.0 (Macintosh; Intel Mac OS X) Safari/17.0'),
('user2', '192.168.1.102', 1, NULL, 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) Firefox/121.0'),
('hacker', '45.33.32.156', 0, 'Usuario no encontrado', 'curl/7.88.1'),
('admin', '203.0.113.50', 0, 'Contraseña incorrecta', 'Python-urllib/3.11'),
('admin', '203.0.113.50', 0, 'Contraseña incorrecta', 'Python-urllib/3.11'),
('user1', '192.168.1.101', 0, 'Contraseña incorrecta', 'Mozilla/5.0 (Macintosh; Intel Mac OS X) Safari/17.0'),
('user1', '192.168.1.101', 1, NULL, 'Mozilla/5.0 (Macintosh; Intel Mac OS X) Safari/17.0'),
('user3', '192.168.1.103', 1, NULL, 'Mozilla/5.0 (Linux; Android 14) Chrome/120.0');
GO

-- ==================== NOTIFICATIONS ====================
INSERT INTO notifications (user_id, title, message, type, is_read) VALUES
(1, 'Sistema inicializado', 'El sistema se ha configurado correctamente con todas las bases de datos.', 'SUCCESS', 1),
(1, 'Stock bajo detectado', 'Los siguientes productos tienen stock bajo: Silla Ergonómica (8), Impresora HP (7)', 'WARNING', 0),
(2, 'Venta completada', 'La venta V-2024-001 se ha registrado exitosamente por $56,999.97', 'SUCCESS', 1),
(2, 'Venta completada', 'La venta V-2024-002 se ha registrado exitosamente por $14,495.00', 'SUCCESS', 1),
(2, 'Venta completada', 'La venta V-2024-003 se ha registrado exitosamente por $13,998.00', 'SUCCESS', 0),
(3, 'Bienvenido al sistema', 'Tu cuenta ha sido creada exitosamente. Ya puedes explorar el catálogo de productos.', 'INFO', 1),
(4, 'Bienvenido al sistema', 'Tu cuenta ha sido creada exitosamente. Ya puedes explorar el catálogo de productos.', 'INFO', 0),
(5, 'Bienvenido al sistema', 'Tu cuenta ha sido creada exitosamente. Ya puedes explorar el catálogo de productos.', 'INFO', 0),
(3, 'Contraseña actualizada', 'Tu contraseña ha sido cambiada exitosamente.', 'INFO', 1),
(1, 'Intento de acceso sospechoso', 'Se detectaron 2 intentos fallidos de login desde IP 203.0.113.50', 'ERROR', 0);
GO
