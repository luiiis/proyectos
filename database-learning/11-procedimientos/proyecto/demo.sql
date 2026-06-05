-- ════════════════════════════════════════════════════════════════
-- MÓDULO 11: Procedimientos - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 11: Stored Procedures y Functions ═══'

-- Función: total de compras de un cliente
CREATE OR REPLACE FUNCTION fn_total_compras(p_cliente_id INTEGER)
RETURNS NUMERIC AS $$
    SELECT COALESCE(SUM(total), 0) FROM ventas
    WHERE cliente_id = p_cliente_id AND estado = 'COMPLETADA';
$$ LANGUAGE sql;

\echo '✓ Función fn_total_compras creada'

-- Función: clasificar cliente
CREATE OR REPLACE FUNCTION fn_nivel_cliente(p_cliente_id INTEGER)
RETURNS TEXT AS $$
DECLARE v_total NUMERIC;
BEGIN
    v_total := fn_total_compras(p_cliente_id);
    RETURN CASE
        WHEN v_total > 500000 THEN 'DIAMANTE'
        WHEN v_total > 200000 THEN 'ORO'
        WHEN v_total > 50000 THEN 'PLATA'
        WHEN v_total > 0 THEN 'BRONCE'
        ELSE 'SIN COMPRAS'
    END;
END;
$$ LANGUAGE plpgsql;

\echo '✓ Función fn_nivel_cliente creada'

-- Probar funciones
\echo ''
\echo '── Top 10 clientes con nivel ──'
SELECT c.nombre, c.apellido,
    fn_total_compras(c.id) AS total_compras,
    fn_nivel_cliente(c.id) AS nivel
FROM clientes c
ORDER BY fn_total_compras(c.id) DESC
LIMIT 10;

-- Procedimiento: resumen de sucursal
CREATE OR REPLACE PROCEDURE sp_resumen_sucursal(p_sucursal_id INTEGER)
LANGUAGE plpgsql AS $$
DECLARE
    v_nombre TEXT; v_empleados INT; v_ventas INT; v_monto NUMERIC;
BEGIN
    SELECT nombre INTO v_nombre FROM sucursales WHERE id = p_sucursal_id;
    SELECT COUNT(*) INTO v_empleados FROM empleados WHERE sucursal_id = p_sucursal_id;
    SELECT COUNT(*), COALESCE(SUM(total), 0) INTO v_ventas, v_monto
    FROM ventas WHERE sucursal_id = p_sucursal_id AND estado = 'COMPLETADA';

    RAISE NOTICE '═══ Sucursal: % ═══', v_nombre;
    RAISE NOTICE 'Empleados: %', v_empleados;
    RAISE NOTICE 'Ventas: %', v_ventas;
    RAISE NOTICE 'Monto total: $%', ROUND(v_monto, 2);
END;
$$;

\echo ''
\echo '── Resumen sucursal 1 (ver mensajes NOTICE) ──'
CALL sp_resumen_sucursal(1);
