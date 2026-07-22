-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 13: Seguridad en BD
-- Ejecutar en PostgreSQL como superusuario (postgres)
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. Crear roles (grupos de permisos) ═══

-- Rol para consultas (solo lectura)
CREATE ROLE rol_consulta;
GRANT CONNECT ON DATABASE empresa_db TO rol_consulta;
GRANT USAGE ON SCHEMA public TO rol_consulta;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO rol_consulta;
-- Que aplique a tablas futuras también:
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT SELECT ON TABLES TO rol_consulta;

-- Rol para vendedores (lectura + insertar ventas)
CREATE ROLE rol_vendedor;
GRANT CONNECT ON DATABASE empresa_db TO rol_vendedor;
GRANT USAGE ON SCHEMA public TO rol_vendedor;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO rol_vendedor;
GRANT INSERT, UPDATE ON ventas, detalle_venta TO rol_vendedor;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA public TO rol_vendedor;

-- Rol para administradores (todo)
CREATE ROLE rol_admin;
GRANT ALL PRIVILEGES ON DATABASE empresa_db TO rol_admin;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO rol_admin;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO rol_admin;

-- ═══ 2. Crear usuarios y asignar roles ═══

-- Usuario de consulta
CREATE USER usuario_reportes WITH PASSWORD 'reportes2026!';
GRANT rol_consulta TO usuario_reportes;

-- Usuario vendedor
CREATE USER carlos_vendedor WITH PASSWORD 'carlos2026!';
GRANT rol_vendedor TO carlos_vendedor;

-- Usuario admin
CREATE USER dba_admin WITH PASSWORD 'admin_seguro_2026!';
GRANT rol_admin TO dba_admin;

-- ═══ 3. Revocar permisos específicos ═══

-- El vendedor NO puede ver salarios
REVOKE SELECT ON empleados FROM rol_vendedor;
-- Pero sí puede ver la vista pública:
GRANT SELECT ON v_empleados_publico TO rol_vendedor;

-- Nadie puede DROP tablas excepto admin:
REVOKE DROP ON SCHEMA public FROM PUBLIC;

-- ═══ 4. Row Level Security (RLS) ═══

-- Cada vendedor solo ve SUS ventas
ALTER TABLE ventas ENABLE ROW LEVEL SECURITY;

-- Política: vendedores solo ven sus propias ventas
CREATE POLICY pol_vendedor_ventas ON ventas
    FOR SELECT
    USING (
        empleado_id = (
            SELECT id FROM empleados WHERE email = current_user || '@empresa.com'
        )
        OR current_user IN (SELECT rolname FROM pg_roles WHERE rolname = 'rol_admin')
    );

-- Administradores ven todo
CREATE POLICY pol_admin_ventas ON ventas
    FOR ALL
    USING (pg_has_role(current_user, 'rol_admin', 'MEMBER'));

-- ═══ 5. Auditar accesos ═══

-- Tabla de log de accesos
CREATE TABLE IF NOT EXISTS log_accesos (
    id BIGSERIAL PRIMARY KEY,
    usuario VARCHAR(50) DEFAULT current_user,
    ip VARCHAR(45) DEFAULT inet_client_addr()::TEXT,
    accion VARCHAR(100),
    tabla VARCHAR(50),
    detalle TEXT,
    fecha TIMESTAMP DEFAULT NOW()
);

-- Function para registrar acceso
CREATE OR REPLACE FUNCTION fn_log_acceso(p_accion VARCHAR, p_tabla VARCHAR, p_detalle TEXT DEFAULT NULL)
RETURNS VOID AS $$
BEGIN
    INSERT INTO log_accesos (accion, tabla, detalle)
    VALUES (p_accion, p_tabla, p_detalle);
END;
$$ LANGUAGE plpgsql;

-- ═══ 6. Encriptar datos sensibles ═══

-- Extensión para encriptación
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Encriptar password
-- INSERT INTO usuarios (username, password_hash)
-- VALUES ('admin', crypt('mi_password', gen_salt('bf')));

-- Verificar password
-- SELECT * FROM usuarios
-- WHERE username = 'admin' AND password_hash = crypt('mi_password', password_hash);

-- Encriptar datos sensibles (columna completa)
-- UPDATE clientes SET tarjeta_encriptada = pgp_sym_encrypt(tarjeta, 'clave_secreta');
-- SELECT pgp_sym_decrypt(tarjeta_encriptada::bytea, 'clave_secreta') FROM clientes;

-- ═══ 7. Prevenir SQL Injection desde la BD ═══

-- Stored procedure seguro (parámetros tipados, no concatenación):
CREATE OR REPLACE FUNCTION fn_buscar_cliente_seguro(p_nombre VARCHAR)
RETURNS TABLE (id INT, nombre VARCHAR, apellido VARCHAR) AS $$
BEGIN
    -- CORRECTO: usa parámetro (no concatenación)
    RETURN QUERY SELECT cl.id, cl.nombre, cl.apellido
    FROM clientes cl WHERE cl.nombre ILIKE '%' || p_nombre || '%';
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;
-- SECURITY DEFINER = se ejecuta con permisos del CREADOR (no del usuario que llama)

-- ═══ 8. Verificar permisos actuales ═══

-- Ver roles de un usuario
SELECT r.rolname, m.member::regrole
FROM pg_auth_members m
JOIN pg_roles r ON r.oid = m.roleid
WHERE m.member = (SELECT oid FROM pg_roles WHERE rolname = 'carlos_vendedor');

-- Ver permisos en una tabla
SELECT grantee, privilege_type
FROM information_schema.table_privileges
WHERE table_name = 'ventas';

-- Ver políticas RLS
SELECT * FROM pg_policies WHERE tablename = 'ventas';

-- ═══ 9. Revocar acceso de emergencia ═══

-- Bloquear usuario inmediatamente:
ALTER USER carlos_vendedor NOLOGIN;  -- No puede conectarse

-- Terminar sesiones activas:
SELECT pg_terminate_backend(pid) FROM pg_stat_activity WHERE usename = 'carlos_vendedor';

-- Restaurar acceso:
ALTER USER carlos_vendedor LOGIN;

-- ═══ 10. Limitar conexiones y recursos ═══

-- Máximo 5 conexiones simultáneas para este usuario:
ALTER USER usuario_reportes CONNECTION LIMIT 5;

-- Timeout de queries (máximo 30 segundos):
ALTER USER usuario_reportes SET statement_timeout = '30s';

-- Solo puede conectar desde la red interna:
-- En pg_hba.conf:
-- host empresa_db usuario_reportes 10.0.0.0/8 scram-sha-256
-- host empresa_db usuario_reportes 0.0.0.0/0 reject
