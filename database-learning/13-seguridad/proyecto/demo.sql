-- ════════════════════════════════════════════════════════════════
-- MÓDULO 13: Seguridad - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 13: Seguridad - Usuarios y Roles ═══'

-- Crear roles
DROP ROLE IF EXISTS demo_vendedor;
DROP ROLE IF EXISTS demo_auditor;
CREATE ROLE demo_vendedor;
CREATE ROLE demo_auditor;

\echo '✓ Roles creados: demo_vendedor, demo_auditor'

-- Asignar permisos
GRANT SELECT ON productos, clientes TO demo_vendedor;
GRANT INSERT ON ventas, detalle_venta TO demo_vendedor;
GRANT SELECT ON ALL TABLES IN SCHEMA public TO demo_auditor;

\echo '✓ Permisos asignados'

-- Ver permisos
\echo ''
\echo '── Permisos del rol demo_vendedor ──'
SELECT grantee, table_name, privilege_type
FROM information_schema.table_privileges
WHERE grantee = 'demo_vendedor'
ORDER BY table_name;

\echo ''
\echo '── Permisos del rol demo_auditor ──'
SELECT grantee, table_name, privilege_type
FROM information_schema.table_privileges
WHERE grantee = 'demo_auditor'
ORDER BY table_name
LIMIT 10;

-- Limpiar
REVOKE ALL ON ALL TABLES IN SCHEMA public FROM demo_vendedor, demo_auditor;
DROP ROLE demo_vendedor;
DROP ROLE demo_auditor;
\echo ''
\echo '✓ Demo completada (roles eliminados)'
