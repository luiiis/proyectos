-- ============================================================
-- MySQL - Datos iniciales (Seed)
-- ============================================================

USE auth_db;

-- ==================== ROLES ====================
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Administrador del sistema - acceso total'),
('MANAGER', 'Gerente/Supervisor - gestión de productos e inventario'),
('USER', 'Usuario estándar - solo lectura y perfil propio')
ON DUPLICATE KEY UPDATE description = VALUES(description);

-- ==================== USUARIOS ====================
-- Contraseñas encriptadas con BCrypt (strength 12)
-- admin123 -> $2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy
-- manager123 -> $2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi
-- user123 -> $2a$12$WApznUPhDubN0oevaSFr9OiaGSmVYIXAiV1eOATG/MBh0EotRFEyS

INSERT INTO users (username, email, password, first_name, last_name, phone, is_active) VALUES
('admin', 'admin@sistema.com', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy', 'Administrador', 'Sistema', '+52 555 0001000', TRUE),
('manager1', 'manager@sistema.com', '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Carlos', 'García López', '+52 555 0002000', TRUE),
('user1', 'usuario@sistema.com', '$2a$12$WApznUPhDubN0oevaSFr9OiaGSmVYIXAiV1eOATG/MBh0EotRFEyS', 'María', 'Hernández Ruiz', '+52 555 0003000', TRUE),
('user2', 'juan@sistema.com', '$2a$12$WApznUPhDubN0oevaSFr9OiaGSmVYIXAiV1eOATG/MBh0EotRFEyS', 'Juan', 'Martínez Soto', '+52 555 0004000', TRUE),
('user3', 'ana@sistema.com', '$2a$12$WApznUPhDubN0oevaSFr9OiaGSmVYIXAiV1eOATG/MBh0EotRFEyS', 'Ana', 'López Vega', '+52 555 0005000', TRUE)
ON DUPLICATE KEY UPDATE email = VALUES(email);

-- ==================== ASIGNACIÓN DE ROLES ====================
-- admin -> ADMIN + USER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'admin' AND r.name = 'ADMIN'
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'admin' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = user_id;

-- manager1 -> MANAGER + USER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'manager1' AND r.name = 'MANAGER'
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'manager1' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = user_id;

-- user1, user2, user3 -> USER
INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'user1' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'user2' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = user_id;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id FROM users u, roles r WHERE u.username = 'user3' AND r.name = 'USER'
ON DUPLICATE KEY UPDATE user_id = user_id;
