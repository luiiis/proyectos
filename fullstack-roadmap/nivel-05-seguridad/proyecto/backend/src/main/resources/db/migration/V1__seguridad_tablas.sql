-- ════════════════════════════════════════════════════════════════
-- V1: Tablas de seguridad (usuarios, roles, permisos)
-- ════════════════════════════════════════════════════════════════

CREATE TABLE roles (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(30) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
);

CREATE TABLE permisos (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(100),
    modulo      VARCHAR(30)
);

CREATE TABLE usuarios (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    username      VARCHAR(50) NOT NULL UNIQUE,
    email         VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre        VARCHAR(50),
    apellido      VARCHAR(50),
    activo        BOOLEAN DEFAULT TRUE,
    bloqueado     BOOLEAN DEFAULT FALSE,
    intentos_fallidos INT DEFAULT 0,
    ultimo_login  TIMESTAMP NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE usuarios_roles (
    usuario_id BIGINT NOT NULL,
    rol_id     BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE,
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE
);

CREATE TABLE roles_permisos (
    rol_id     BIGINT NOT NULL,
    permiso_id BIGINT NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    FOREIGN KEY (rol_id) REFERENCES roles(id) ON DELETE CASCADE,
    FOREIGN KEY (permiso_id) REFERENCES permisos(id) ON DELETE CASCADE
);

-- ═══ DATOS INICIALES ═══

-- Roles
INSERT INTO roles (nombre, descripcion) VALUES
('ADMIN', 'Administrador del sistema'),
('SUPERVISOR', 'Supervisor de operaciones'),
('CAJERO', 'Operador de caja'),
('ALMACENISTA', 'Gestor de inventario');

-- Permisos
INSERT INTO permisos (nombre, descripcion, modulo) VALUES
('PRODUCTO_CREAR', 'Crear productos', 'PRODUCTOS'),
('PRODUCTO_EDITAR', 'Editar productos', 'PRODUCTOS'),
('PRODUCTO_ELIMINAR', 'Eliminar productos', 'PRODUCTOS'),
('PRODUCTO_VER', 'Ver productos', 'PRODUCTOS'),
('VENTA_CREAR', 'Registrar ventas', 'VENTAS'),
('VENTA_VER', 'Ver ventas', 'VENTAS'),
('REPORTE_CONSULTAR', 'Consultar reportes', 'REPORTES'),
('USUARIO_GESTIONAR', 'Gestionar usuarios', 'USUARIOS');

-- Asignar permisos a roles
INSERT INTO roles_permisos (rol_id, permiso_id) VALUES
-- ADMIN tiene todos
(1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),
-- SUPERVISOR
(2,1),(2,2),(2,4),(2,5),(2,6),(2,7),
-- CAJERO
(3,4),(3,5),(3,6),
-- ALMACENISTA
(4,1),(4,2),(4,4);

-- Usuarios de prueba (passwords hasheados con BCrypt)
-- admin / Admin123!
INSERT INTO usuarios (username, email, password_hash, nombre, apellido) VALUES
('admin', 'admin@sistema.com', '$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy', 'Administrador', 'Sistema');
-- supervisor / Super123!
INSERT INTO usuarios (username, email, password_hash, nombre, apellido) VALUES
('supervisor', 'supervisor@sistema.com', '$2a$12$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Supervisor', 'Uno');
-- cajero / Cajero123!
INSERT INTO usuarios (username, email, password_hash, nombre, apellido) VALUES
('cajero', 'cajero@sistema.com', '$2a$12$WApznUPhDubKGhcjEh1KKeG.0RB6bvLBb6mKp5HwcRJAKcMWHMCmy', 'Cajero', 'Principal');

-- Asignar roles
INSERT INTO usuarios_roles (usuario_id, rol_id) VALUES
(1, 1), -- admin → ADMIN
(2, 2), -- supervisor → SUPERVISOR
(3, 3); -- cajero → CAJERO
