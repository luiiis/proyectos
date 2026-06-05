-- ════════════════════════════════════════════════════════════════
-- MÓDULO 12: Transacciones - Demo Ejecutable
-- Ejecutar: docker exec -i learn-postgres psql -U postgres -d empresa_db < demo.sql
-- ════════════════════════════════════════════════════════════════

\echo '═══ MÓDULO 12: Transacciones ACID ═══'

-- Crear tabla de cuentas para demo
DROP TABLE IF EXISTS demo_cuentas CASCADE;
CREATE TABLE demo_cuentas (
    id SERIAL PRIMARY KEY,
    titular VARCHAR(50),
    saldo NUMERIC(12,2) CHECK (saldo >= 0)
);
INSERT INTO demo_cuentas (titular, saldo) VALUES
('Carlos', 50000), ('María', 30000), ('Juan', 15000);

\echo '✓ Tabla demo_cuentas creada con 3 cuentas'
SELECT * FROM demo_cuentas;

-- Demo: Transferencia exitosa
\echo ''
\echo '── Transferencia: Carlos → María $10,000 ──'
BEGIN;
    UPDATE demo_cuentas SET saldo = saldo - 10000 WHERE titular = 'Carlos';
    UPDATE demo_cuentas SET saldo = saldo + 10000 WHERE titular = 'María';
COMMIT;
\echo '✓ Transferencia completada'
SELECT * FROM demo_cuentas;

-- Demo: Transferencia fallida (ROLLBACK)
\echo ''
\echo '── Transferencia fallida: Juan intenta enviar $20,000 (solo tiene $15,000) ──'
BEGIN;
    UPDATE demo_cuentas SET saldo = saldo - 20000 WHERE titular = 'Juan';
    -- Esto viola el CHECK (saldo >= 0) → error → rollback
ROLLBACK;
\echo '✓ Rollback ejecutado (saldos sin cambio)'
SELECT * FROM demo_cuentas;

-- Limpiar
DROP TABLE demo_cuentas;
\echo ''
\echo '✓ Demo completada (tabla temporal eliminada)'
