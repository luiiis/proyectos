-- ════════════════════════════════════════════════════════════════
-- MÓDULO 10: Triggers - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 10: Triggers de Auditoría ═══'

-- Crear función de auditoría
CREATE OR REPLACE FUNCTION fn_auditoria_demo()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_despues, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'INSERT', NEW.id, to_jsonb(NEW), current_user, NOW());
        RETURN NEW;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_antes, datos_despues, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'UPDATE', NEW.id, to_jsonb(OLD), to_jsonb(NEW), current_user, NOW());
        RETURN NEW;
    ELSIF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_antes, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'DELETE', OLD.id, to_jsonb(OLD), current_user, NOW());
        RETURN OLD;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Aplicar trigger a productos
DROP TRIGGER IF EXISTS trg_demo_audit ON productos;
CREATE TRIGGER trg_demo_audit
    AFTER INSERT OR UPDATE OR DELETE ON productos
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria_demo();

\echo '✓ Trigger de auditoría creado en productos'

-- Probar: insertar producto
\echo ''
\echo '── Insertando producto de prueba ──'
INSERT INTO productos (nombre, precio, costo, sku, categoria_id, proveedor_id)
VALUES ('TRIGGER TEST', 555.55, 300, 'TRG-TEST-001', 1, 1)
RETURNING id, nombre;

-- Probar: actualizar
\echo ''
\echo '── Actualizando precio ──'
UPDATE productos SET precio = 666.66 WHERE sku = 'TRG-TEST-001';

-- Ver auditoría generada
\echo ''
\echo '── Registros de auditoría generados ──'
SELECT id, tabla, operacion, registro_id,
    datos_despues->>'nombre' AS producto,
    usuario, fecha::timestamp(0)
FROM auditoria
ORDER BY fecha DESC LIMIT 5;

-- Limpiar
DELETE FROM productos WHERE sku = 'TRG-TEST-001';
\echo ''
\echo '✓ Demo completada (producto de prueba eliminado, auditoría preservada)'
