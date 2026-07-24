-- ════════════════════════════════════════════════════════════════
-- V2: Datos iniciales de roles, permisos y usuarios de prueba
-- Passwords hasheados con BCrypt (generados por Spring Security)
-- ════════════════════════════════════════════════════════════════

-- Roles
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador con acceso total'),
('SUPERVISOR', 'Supervisa operaciones y genera reportes'),
('CAJERO', 'Realiza ventas y consultas'),
('ALMACENISTA', 'Gestiona inventario y productos');

-- Permisos
INSERT INTO permisos (nombre, descripcion, modulo) VALUES
('PRODUCTO_CREAR', 'Crear nuevos productos', 'PRODUCTOS'),
('PRODUCTO_EDITAR', 'Editar productos existentes', 'PRODUCTOS'),
('PRODUCTO_ELIMINAR', 'Eliminar productos', 'PRODUCTOS'),
('PRODUCTO_CONSULTAR', 'Consultar productos', 'PRODUCTOS'),
('CATEGORIA_GESTIONAR', 'Crear/editar/eliminar categorías', 'CATEGORIAS'),
('VENTA_CREAR', 'Registrar ventas', 'VENTAS'),
('VENTA_CONSULTAR', 'Consultar ventas', 'VENTAS'),
('VENTA_CANCELAR', 'Cancelar ventas', 'VENTAS'),
('REPORTE_CONSULTAR', 'Ver reportes', 'REPORTES'),
('USUARIO_GESTIONAR', 'Gestionar usuarios', 'USUARIOS');

-- Roles ↔ Permisos
-- ADMIN: todos los permisos
INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9), (1, 10);

-- SUPERVISOR: consultar, reportes, gestionar ventas
INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
(2, 4), (2, 7), (2, 8), (2, 9);

-- CAJERO: consultar productos, crear/consultar ventas
INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
(3, 4), (3, 6), (3, 7);

-- ALMACENISTA: gestionar productos y categorías
INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
(4, 1), (4, 2), (4, 4), (4, 5);

-- Usuarios de prueba
-- Password "Admin123!" -> BCrypt hash
INSERT INTO usuarios (username, email, password_hash, nombre) VALUES
('admin', 'admin@softwarelee.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Administrador'),
('supervisor', 'supervisor@softwarelee.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Supervisor General'),
('cajero', 'cajero@softwarelee.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Cajero Principal');

-- Usuarios ↔ Roles
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
(1, 1),  -- admin → ADMIN
(2, 2),  -- supervisor → SUPERVISOR
(3, 3);  -- cajero → CAJERO
