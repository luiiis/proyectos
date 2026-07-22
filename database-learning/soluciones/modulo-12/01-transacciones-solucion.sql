-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 12: Transacciones
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. Transacción básica: registrar venta completa ═══

BEGIN;
    -- Crear venta
    INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id, estado)
    VALUES ('V-DEMO-001', 1, 7, 1, 'COMPLETADA');

    -- Obtener el ID generado (suponiendo que es la última insertada)
    -- En una app real usarías RETURNING id

    -- Insertar detalles
    INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
    VALUES
        (currval('ventas_id_seq'), 1, 2, 18999.00, 37998.00),
        (currval('ventas_id_seq'), 3, 1, 2899.00, 2899.00);

    -- Actualizar totales
    UPDATE ventas SET
        subtotal = 40897.00,
        impuesto = ROUND(40897.00 * 0.16, 2),
        total = ROUND(40897.00 * 1.16, 2)
    WHERE numero_venta = 'V-DEMO-001';

COMMIT;
-- Si algo falla en cualquier paso → todo se deshace automáticamente

-- ═══ 2. Transacción con validación y ROLLBACK explícito ═══

DO $$
DECLARE
    v_stock INTEGER;
    v_venta_id INTEGER;
BEGIN
    -- Verificar stock antes de vender
    SELECT cantidad INTO v_stock FROM inventario
    WHERE producto_id = 1 AND sucursal_id = 1;

    IF v_stock < 5 THEN
        RAISE EXCEPTION 'Stock insuficiente. Disponible: %, Requerido: 5', v_stock;
    END IF;

    -- Crear venta
    INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id, estado)
    VALUES ('V-DEMO-002', 2, 8, 1, 'COMPLETADA')
    RETURNING id INTO v_venta_id;

    -- Insertar detalle
    INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
    VALUES (v_venta_id, 1, 5, 18999.00, 94995.00);

    -- Descontar stock
    UPDATE inventario SET cantidad = cantidad - 5
    WHERE producto_id = 1 AND sucursal_id = 1;

    -- Actualizar total
    UPDATE ventas SET total = ROUND(94995.00 * 1.16, 2)
    WHERE id = v_venta_id;

    RAISE NOTICE '✓ Venta % registrada exitosamente', v_venta_id;

EXCEPTION WHEN OTHERS THEN
    -- ROLLBACK automático en bloques DO
    RAISE NOTICE '✗ Error: %. Transacción cancelada.', SQLERRM;
END $$;

-- ═══ 3. SAVEPOINT: rollback parcial ═══

BEGIN;
    -- Paso 1: Insertar nuevo cliente (siempre debe quedar)
    INSERT INTO clientes (nombre, apellido, email, tipo)
    VALUES ('Nuevo', 'Cliente', 'nuevo@test.com', 'REGULAR');
    RAISE NOTICE '✓ Cliente insertado';

    SAVEPOINT antes_venta;

    -- Paso 2: Intentar registrar venta (puede fallar)
    INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id)
    VALUES ('V-DEMO-003', currval('clientes_id_seq'), 999, 1);
    -- empleado_id = 999 no existe → violación FK

EXCEPTION WHEN foreign_key_violation THEN
    -- Solo deshacer la venta, el cliente se mantiene
    ROLLBACK TO antes_venta;
    RAISE NOTICE '⚠️ Venta falló (FK violation), cliente se mantiene';
END;
COMMIT;

-- ═══ 4. Bloqueo pesimista (FOR UPDATE) ═══

-- Transacción A (en una conexión):
BEGIN;
    -- Bloquear el producto para que nadie más lo modifique
    SELECT * FROM inventario WHERE producto_id = 1 AND sucursal_id = 1 FOR UPDATE;
    -- Ahora solo esta transacción puede modificar esta fila

    -- Simular procesamiento
    -- pg_sleep(5);

    UPDATE inventario SET cantidad = cantidad - 1
    WHERE producto_id = 1 AND sucursal_id = 1;
COMMIT;

-- Si otra transacción intenta FOR UPDATE en la misma fila → espera hasta que termine

-- ═══ 5. Bloqueo optimista (con columna version) ═══

-- Agregar columna version (si no existe):
-- ALTER TABLE productos ADD COLUMN version INTEGER DEFAULT 0;

-- Leer el producto con su versión:
-- SELECT id, nombre, precio, version FROM productos WHERE id = 1;
-- Supongamos: version = 5

-- Actualizar SOLO si la versión no cambió:
UPDATE productos
SET precio = 19999.00, version = version + 1
WHERE id = 1 AND version = 5;
-- Si devuelve "UPDATE 0" → alguien lo modificó antes → reintentar

-- ═══ 6. Niveles de aislamiento ═══

-- READ COMMITTED (default en PostgreSQL):
-- Cada SELECT dentro de la transacción ve los datos más recientes COMMITADOS
BEGIN;
    SET TRANSACTION ISOLATION LEVEL READ COMMITTED;
    SELECT COUNT(*) FROM ventas;  -- Puede dar 100
    -- Otra transacción inserta 5 ventas + COMMIT
    SELECT COUNT(*) FROM ventas;  -- Ahora da 105 (non-repeatable read)
COMMIT;

-- REPEATABLE READ:
-- Todos los SELECT dentro de la transacción ven el MISMO snapshot
BEGIN;
    SET TRANSACTION ISOLATION LEVEL REPEATABLE READ;
    SELECT COUNT(*) FROM ventas;  -- Da 100
    -- Otra transacción inserta 5 ventas + COMMIT
    SELECT COUNT(*) FROM ventas;  -- Sigue dando 100 (snapshot consistente)
COMMIT;

-- SERIALIZABLE:
-- Transacciones se ejecutan como si fueran secuenciales
-- Si detecta conflicto → error "could not serialize access"
BEGIN;
    SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
    -- Cualquier conflicto con otra transacción → una de las dos falla
COMMIT;

-- ═══ 7. Detectar y manejar deadlocks ═══

-- PostgreSQL detecta deadlocks automáticamente (~1 segundo)
-- Una de las transacciones recibe: ERROR: deadlock detected
-- La otra continúa normalmente

-- Para evitar deadlocks: SIEMPRE bloquear recursos en el MISMO ORDEN
-- MAL:  TX1 bloquea tabla A luego B. TX2 bloquea tabla B luego A.
-- BIEN: Ambas bloquean A primero, luego B.

-- Verificar locks activos:
SELECT pid, relation::regclass, mode, granted
FROM pg_locks
WHERE NOT granted;

-- Matar transacción bloqueada:
-- SELECT pg_terminate_backend(pid);
