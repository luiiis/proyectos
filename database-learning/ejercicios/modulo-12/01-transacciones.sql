-- ════════════════════════════════════════════════════════════════
-- MÓDULO 12 - EJERCICIO 1: Transacciones
-- ════════════════════════════════════════════════════════════════
-- INSTRUCCIONES:
-- Implementa operaciones transaccionales para garantizar consistencia.
-- ════════════════════════════════════════════════════════════════

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 12.1: Crear tabla de cuentas bancarias para practicar
-- ═══════════════════════════════════════════════════════════════
CREATE TABLE IF NOT EXISTS cuentas_bancarias (
    id SERIAL PRIMARY KEY,
    titular VARCHAR(100) NOT NULL,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    saldo NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (saldo >= 0),
    tipo VARCHAR(20) DEFAULT 'AHORRO' CHECK (tipo IN ('AHORRO', 'CORRIENTE', 'NOMINA')),
    activa BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT NOW()
);

INSERT INTO cuentas_bancarias (titular, numero_cuenta, saldo, tipo) VALUES
('Carlos García', 'CTA-001', 50000.00, 'NOMINA'),
('María López', 'CTA-002', 30000.00, 'AHORRO'),
('Juan Martínez', 'CTA-003', 15000.00, 'CORRIENTE'),
('Ana Vega', 'CTA-004', 80000.00, 'AHORRO'),
('Pedro Sánchez', 'CTA-005', 5000.00, 'NOMINA');

-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 12.2: Transferencia bancaria con transacción
-- ═══════════════════════════════════════════════════════════════
-- Implementa una transferencia de $10,000 de CTA-001 a CTA-002
-- DEBE ser atómica: si falla el depósito, el retiro se deshace
-- Pasos:
-- 1. BEGIN
-- 2. Verificar saldo suficiente en origen
-- 3. Restar del origen
-- 4. Sumar al destino
-- 5. COMMIT (o ROLLBACK si algo falla)

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 12.3: Transacción con SAVEPOINT
-- ═══════════════════════════════════════════════════════════════
-- Escenario: Registrar una venta con 3 productos.
-- Si el tercer producto no tiene stock, deshacer SOLO ese producto
-- pero mantener los otros 2.
-- Usa SAVEPOINT para puntos de guardado intermedios.

-- TU CÓDIGO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 12.4: Procedimiento de transferencia segura
-- ═══════════════════════════════════════════════════════════════
-- Crea un procedimiento sp_transferencia(origen, destino, monto) que:
-- 1. Valide que ambas cuentas existen y están activas
-- 2. Valide saldo suficiente
-- 3. Ejecute la transferencia en una transacción
-- 4. Registre la operación en una tabla "movimientos_bancarios"
-- 5. Si algo falla, haga ROLLBACK y lance excepción con mensaje claro

-- Primero crea la tabla de movimientos:
CREATE TABLE IF NOT EXISTS movimientos_bancarios (
    id SERIAL PRIMARY KEY,
    cuenta_origen VARCHAR(20) REFERENCES cuentas_bancarias(numero_cuenta),
    cuenta_destino VARCHAR(20) REFERENCES cuentas_bancarias(numero_cuenta),
    monto NUMERIC(12,2) NOT NULL CHECK (monto > 0),
    tipo VARCHAR(20) CHECK (tipo IN ('TRANSFERENCIA', 'DEPOSITO', 'RETIRO')),
    estado VARCHAR(20) DEFAULT 'COMPLETADO',
    fecha TIMESTAMP DEFAULT NOW()
);

-- TU PROCEDIMIENTO AQUÍ:




-- ═══════════════════════════════════════════════════════════════
-- EJERCICIO 12.5: Simular concurrencia (deadlock)
-- ═══════════════════════════════════════════════════════════════
-- Abre DOS terminales conectadas a PostgreSQL.
-- Terminal 1: Transfiere de CTA-001 a CTA-002
-- Terminal 2: Transfiere de CTA-002 a CTA-001 (al mismo tiempo)
-- Observa qué pasa. ¿Cómo lo resolverías?
--
-- Terminal 1:
-- BEGIN;
-- UPDATE cuentas_bancarias SET saldo = saldo - 1000 WHERE numero_cuenta = 'CTA-001';
-- (espera 5 segundos)
-- UPDATE cuentas_bancarias SET saldo = saldo + 1000 WHERE numero_cuenta = 'CTA-002';
-- COMMIT;
--
-- Terminal 2 (ejecutar mientras Terminal 1 espera):
-- BEGIN;
-- UPDATE cuentas_bancarias SET saldo = saldo - 500 WHERE numero_cuenta = 'CTA-002';
-- UPDATE cuentas_bancarias SET saldo = saldo + 500 WHERE numero_cuenta = 'CTA-001';
-- COMMIT;
--
-- PREGUNTA: ¿Qué error ves? ¿Cómo prevenirlo?
-- PISTA: Ordenar los locks siempre en el mismo orden (por ID menor primero)
