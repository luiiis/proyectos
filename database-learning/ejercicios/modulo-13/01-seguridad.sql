-- ════════════════════════════════════════════════════════════════
-- MÓDULO 13 - EJERCICIO 1: Seguridad
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Implementa un sistema de seguridad completo con usuarios, roles y permisos.
-- Ejecuta como superusuario (postgres).
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 13.1: Crear roles de base de datos
-- ═══════════════════════════════════════════════════════════════
-- Crea estos roles (grupos de permisos):
-- - rol_admin: acceso total a todo
-- - rol_gerente: SELECT, INSERT, UPDATE en todas las tablas (no DELETE)
-- - rol_vendedor: SELECT en productos/clientes, INSERT en ventas/detalle_venta
-- - rol_auditor: solo SELECT en todas las tablas (lectura)
-- - rol_reportes: solo SELECT en vistas (no tablas directas)

-- TU CÓDIGO:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 13.2: Crear usuarios y asignar roles
-- ═══════════════════════════════════════════════════════════════
-- Crea estos usuarios con passwords seguros:
-- - usr_admin (rol_admin)
-- - usr_gerente_mty (rol_gerente)
-- - usr_vendedor1 (rol_vendedor)
-- - usr_auditor (rol_auditor)
-- - usr_reportes (rol_reportes)

-- TU CÓDIGO:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 13.3: Otorgar permisos granulares
-- ═══════════════════════════════════════════════════════════════
-- Configura permisos específicos:
-- a) rol_vendedor puede ver productos pero NO el campo "costo" (margen es secreto)
-- b) rol_gerente puede UPDATE salarios pero NO puede ver la tabla auditoria
-- c) rol_reportes solo puede acceder a vistas que empiecen con "v_" o "mv_"

-- TU CÓDIGO:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 13.4: Row Level Security (RLS)
-- ═══════════════════════════════════════════════════════════════
-- Implementa RLS para que cada vendedor solo vea SUS ventas:
-- 1. Habilita RLS en la tabla ventas
-- 2. Crea una política que filtre por empleado_id del usuario actual
-- 3. Configura una variable de sesión para identificar al empleado
-- PISTA: 
--   ALTER TABLE ventas ENABLE ROW LEVEL SECURITY;
--   CREATE POLICY ... USING (empleado_id = current_setting('app.empleado_id')::int);

-- TU CÓDIGO:


-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 13.5: Probar la seguridad
-- ═══════════════════════════════════════════════════════════════
-- Conéctate como cada usuario y verifica:
-- a) usr_vendedor1 NO puede hacer DELETE
-- b) usr_vendedor1 NO puede ver el campo "costo" de productos
-- c) usr_auditor puede SELECT pero NO INSERT/UPDATE/DELETE
-- d) usr_reportes solo puede consultar vistas

-- COMANDOS DE PRUEBA:
-- SET ROLE usr_vendedor1;
-- DELETE FROM productos WHERE id = 1;  -- Debe dar ERROR
-- SELECT costo FROM productos;  -- Debe dar ERROR
-- RESET ROLE;  -- Volver a postgres

-- Documenta los resultados:

