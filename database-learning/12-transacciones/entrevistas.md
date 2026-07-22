# Preguntas de Entrevista - Módulo 12: Transacciones

## Nivel Junior

### 1. ¿Qué es una transacción?
**Respuesta:**
Un grupo de operaciones SQL que se ejecutan como UNA unidad. Si falla alguna → se deshacen TODAS (rollback). Si todas son exitosas → se confirman (commit).

```sql
BEGIN;
  UPDATE cuentas SET saldo = saldo - 1000 WHERE id = 1;  -- Restar
  UPDATE cuentas SET saldo = saldo + 1000 WHERE id = 2;  -- Sumar
COMMIT;  -- Ambas se guardan, o ninguna
```

---

### 2. ¿Qué significa ACID?
**Respuesta:**
- **Atomicity:** Todo o nada. No hay "medio commit".
- **Consistency:** La BD siempre está en estado válido (constraints se cumplen).
- **Isolation:** Transacciones concurrentes no se ven entre sí (niveles configurables).
- **Durability:** Una vez commitado → no se pierde, ni con crash del servidor.

---

### 3. ¿Cuál es la diferencia entre COMMIT y ROLLBACK?
**Respuesta:**
- `COMMIT`: confirma todos los cambios de la transacción. Son permanentes.
- `ROLLBACK`: deshace todos los cambios. Como si nunca hubieran pasado.

---

### 4. ¿Qué es un deadlock?
**Respuesta:**
Dos transacciones se bloquean mutuamente esperando la una a la otra:
- Transacción A bloquea tabla 1, espera tabla 2
- Transacción B bloquea tabla 2, espera tabla 1
- Ninguna puede avanzar → deadlock

La BD lo detecta y mata una de las dos (la víctima recibe error). Solución: siempre bloquear tablas en el MISMO ORDEN.

---

### 5. ¿Qué pasa si no usas transacciones explícitas?
**Respuesta:**
En PostgreSQL y Oracle: cada statement individual es una transacción implícita (auto-commit). Es seguro para operaciones individuales.

El problema: si necesitas que 2 operaciones sean atómicas y no usas BEGIN/COMMIT, una puede fallar y la otra ya se guardó → inconsistencia.

---

## Nivel Mid

### 6. Explica los niveles de aislamiento
**Respuesta:**

| Nivel | Dirty Read | Non-Repeatable Read | Phantom Read |
|-------|-----------|--------------------|----|
| READ UNCOMMITTED | Posible | Posible | Posible |
| READ COMMITTED (default PG) | Imposible | Posible | Posible |
| REPEATABLE READ | Imposible | Imposible | Posible |
| SERIALIZABLE | Imposible | Imposible | Imposible |

- **Dirty Read:** leer datos no commitados de otra transacción
- **Non-Repeatable Read:** leer un valor, otra transacción lo cambia, leerlo de nuevo da resultado diferente
- **Phantom Read:** una query devuelve N filas, otra transacción inserta filas, repetir la query da N+M filas

---

### 7. ¿Qué es un SAVEPOINT?
**Respuesta:**
Punto de control dentro de una transacción. Permite rollback parcial:
```sql
BEGIN;
  INSERT INTO ventas (...) VALUES (...);     -- OK
  SAVEPOINT antes_detalle;
  INSERT INTO detalle_venta (...) VALUES (...);  -- Falla
  ROLLBACK TO antes_detalle;  -- Deshace solo el detalle
  -- La venta sigue existiendo
COMMIT;
```

---

### 8. ¿Cómo manejas errores en transacciones con código?
**Respuesta:**
```sql
-- PL/pgSQL:
DO $$
BEGIN
    -- operaciones...
    COMMIT;
EXCEPTION WHEN OTHERS THEN
    ROLLBACK;
    RAISE NOTICE 'Error: %', SQLERRM;
END $$;
```
En aplicación (Java/Spring): `@Transactional` hace rollback automático si lanza RuntimeException.

---

### 9. ¿Qué es el bloqueo optimista vs pesimista?
**Respuesta:**
- **Pesimista:** bloquear el registro al leerlo (`SELECT ... FOR UPDATE`). Nadie más puede modificarlo hasta que termines.
- **Optimista:** no bloquear. Al guardar, verificar que nadie lo cambió (version/timestamp). Si cambió → error, reintentar.

Pesimista: seguro pero lento (muchos bloqueos). Optimista: rápido pero puede fallar (reintentos).

---

### 10. ¿Cómo implementarías una transferencia bancaria segura?
**Respuesta:**
```sql
BEGIN;
  -- Bloquear ambas cuentas (en orden consistente para evitar deadlock)
  SELECT saldo FROM cuentas WHERE id IN (1, 2) ORDER BY id FOR UPDATE;
  
  -- Validar fondos suficientes
  IF (SELECT saldo FROM cuentas WHERE id = 1) < 1000 THEN
    RAISE EXCEPTION 'Fondos insuficientes';
  END IF;
  
  -- Ejecutar transferencia
  UPDATE cuentas SET saldo = saldo - 1000 WHERE id = 1;
  UPDATE cuentas SET saldo = saldo + 1000 WHERE id = 2;
  
  -- Registrar movimiento
  INSERT INTO movimientos (cuenta_origen, cuenta_destino, monto, fecha)
  VALUES (1, 2, 1000, NOW());
  
COMMIT;
```
Claves: FOR UPDATE (bloqueo), validación de saldo, orden consistente, registro de auditoría.
