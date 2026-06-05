-- Datos iniciales que JPA ejecuta al arrancar (solo si las tablas están vacías)
-- Esto asegura que siempre existan los roles básicos

INSERT IGNORE INTO roles (name, description) VALUES ('ADMIN', 'Administrador del sistema');
INSERT IGNORE INTO roles (name, description) VALUES ('MANAGER', 'Gerente/Supervisor');
INSERT IGNORE INTO roles (name, description) VALUES ('USER', 'Usuario estándar');
