-- ════════════════════════════════════════════════════════════════
-- MÓDULO 01: Fundamentos - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 01: Explorando la Base de Datos ═══'
\echo ''

-- Ver todas las tablas disponibles
\echo '── Tablas en la base de datos ──'
SELECT tablename FROM pg_tables WHERE schemaname = 'public' ORDER BY tablename;

-- Ver estructura de una tabla
\echo ''
\echo '── Estructura de la tabla "productos" ──'
\d productos

-- Ver relaciones (Foreign Keys)
\echo ''
\echo '── Relaciones entre tablas ──'
SELECT
    tc.table_name AS tabla,
    kcu.column_name AS columna_fk,
    ccu.table_name AS tabla_referenciada,
    ccu.column_name AS columna_referenciada
FROM information_schema.table_constraints tc
JOIN information_schema.key_column_usage kcu ON tc.constraint_name = kcu.constraint_name
JOIN information_schema.constraint_column_usage ccu ON ccu.constraint_name = tc.constraint_name
WHERE tc.constraint_type = 'FOREIGN KEY'
ORDER BY tc.table_name;

-- Contar registros por tabla
\echo ''
\echo '── Registros por tabla ──'
SELECT 'sucursales' AS tabla, COUNT(*) AS registros FROM sucursales
UNION ALL SELECT 'categorias', COUNT(*) FROM categorias
UNION ALL SELECT 'proveedores', COUNT(*) FROM proveedores
UNION ALL SELECT 'empleados', COUNT(*) FROM empleados
UNION ALL SELECT 'clientes', COUNT(*) FROM clientes
UNION ALL SELECT 'productos', COUNT(*) FROM productos
UNION ALL SELECT 'inventario', COUNT(*) FROM inventario
UNION ALL SELECT 'ventas', COUNT(*) FROM ventas
UNION ALL SELECT 'detalle_venta', COUNT(*) FROM detalle_venta
ORDER BY registros DESC;
