# Módulo 12: Transacciones y ACID

## ¿Qué es una Transacción?
Un grupo de operaciones SQL que se ejecutan como UNA SOLA UNIDAD. O se ejecutan TODAS, o no se ejecuta NINGUNA.

**Ejemplo real**: Transferencia bancaria.
- Paso 1: Restar $1000 de cuenta A
- Paso 2: Sumar $1000 a cuenta B
- Si el paso 2 falla, el paso 1 se DESHACE (rollback)

---

## 1. Propiedades ACID

| Propiedad | Significado | Ejemplo |
|-----------|-------------|---------|
| **A**tomicity | Todo o nada | Si falla un paso, se deshace todo |
| **C**onsistency | BD siempre en estado válido | No puede haber saldo negativo |
| **I**solation | Transacciones no se interfieren | Dos ventas simultáneas no se pisan |
| **D**urability | Una vez confirmado, persiste | Si se cae el servidor, los datos siguen |

---

## 2. COMMIT y ROLLBACK

```sql
-- Iniciar transacción explícita
BEGIN;  -- o START TRANSACTION;

-- Operaciones
UPDATE cuentas SET saldo = saldo - 1000 WHERE id = 1;
UPDATE cuentas SET saldo = saldo + 1000 WHERE id = 2;

-- Si todo está bien:
COMMIT;  -- Confirmar cambios (permanentes)

-- Si algo salió mal:
ROLLBACK;  -- Deshacer TODO desde el BEGIN
```

## 3. SAVEPOINT (puntos de guardado)

```sql
BEGIN;

INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id, total)
VALUES ('V-TEST-001', 1, 7, 1, 5000);

SAVEPOINT sp_detalle;  -- Punto de guardado

INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
VALUES (currval('ventas_id_seq'), 999, 1, 5000, 5000);  -- producto 999 no existe → ERROR

-- Solo deshacer hasta el savepoint (la venta se mantiene)
ROLLBACK TO sp_detalle;

-- Insertar detalle correcto
INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
VALUES (currval('ventas_id_seq'), 1, 1, 5000, 5000);

COMMIT;  -- Confirmar todo
```

## 4. Simulador de Transferencia Bancaria

```sql
-- Crear tabla de cuentas para el ejercicio
CREATE TABLE cuentas (
    id SERIAL PRIMARY KEY,
    titular VARCHAR(100) NOT NULL,
    saldo NUMERIC(12,2) NOT NULL CHECK (saldo >= 0),
    tipo VARCHAR(20) DEFAULT 'AHORRO'
);

INSERT INTO cuentas (titular, saldo) VALUES
('Carlos García', 50000.00),
('María López', 30000.00),
('Juan Martínez', 15000.00);

-- Procedimiento de transferencia con transacción
CREATE OR REPLACE PROCEDURE sp_transferencia(
    p_origen INTEGER,
    p_destino INTEGER,
    p_monto NUMERIC
)
LANGUAGE plpgsql AS $$
DECLARE
    v_saldo_origen NUMERIC;
BEGIN
    -- Verificar saldo
    SELECT saldo INTO v_saldo_origen FROM cuentas WHERE id = p_origen FOR UPDATE;
    -- FOR UPDATE: bloquea la fila para evitar condiciones de carrera
    
    IF v_saldo_origen IS NULL THEN
        RAISE EXCEPTION 'Cuenta origen % no existe', p_origen;
    END IF;
    
    IF v_saldo_origen < p_monto THEN
        RAISE EXCEPTION 'Saldo insuficiente. Disponible: %, Solicitado: %', v_saldo_origen, p_monto;
    END IF;
    
    -- Ejecutar transferencia (atómica)
    UPDATE cuentas SET saldo = saldo - p_monto WHERE id = p_origen;
    UPDATE cuentas SET saldo = saldo + p_monto WHERE id = p_destino;
    
    -- Si llegamos aquí, todo OK → COMMIT implícito al salir del procedimiento
    RAISE NOTICE 'Transferencia exitosa: $% de cuenta % a cuenta %', p_monto, p_origen, p_destino;
END;
$$;

-- Probar:
CALL sp_transferencia(1, 2, 10000);  -- Carlos → María: $10,000
SELECT * FROM cuentas;
-- Carlos: 40000, María: 40000

CALL sp_transferencia(3, 1, 20000);  -- Juan → Carlos: $20,000 (FALLA: saldo insuficiente)
```

## 5. Niveles de Aislamiento

| Nivel | Dirty Read | Non-Repeatable Read | Phantom Read | Performance |
|-------|-----------|--------------------|--------------|----|
| READ UNCOMMITTED | ✓ posible | ✓ posible | ✓ posible | Más rápido |
| READ COMMITTED (default PG) | ✗ | ✓ posible | ✓ posible | Bueno |
| REPEATABLE READ | ✗ | ✗ | ✓ posible | Medio |
| SERIALIZABLE | ✗ | ✗ | ✗ | Más lento |

```sql
-- Cambiar nivel de aislamiento
SET TRANSACTION ISOLATION LEVEL SERIALIZABLE;
BEGIN;
-- ... operaciones ...
COMMIT;
```

## 6. Ejercicios

1. Implementa una transacción que registre una compra (encabezado + detalles + actualizar inventario)
2. ¿Qué pasa si dos usuarios intentan comprar el último producto al mismo tiempo?
3. Implementa un "cierre de caja" que sume todas las ventas del día y las registre
4. Simula un deadlock y explica cómo resolverlo
5. ¿Cuándo usarías SERIALIZABLE vs READ COMMITTED?

---

## Siguiente Módulo
→ [13-Seguridad](../13-seguridad/README.md)
