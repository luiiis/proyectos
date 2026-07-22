# Preguntas de Entrevista - Módulo 10: Triggers

## Nivel Junior

### 1. ¿Qué es un Trigger?
**Respuesta:**
Código que se ejecuta AUTOMÁTICAMENTE cuando ocurre un evento en una tabla (INSERT, UPDATE, DELETE). No lo llamas manualmente, la BD lo ejecuta sola.

Ejemplo: cada vez que se inserta una venta → el trigger registra en la tabla de auditoría quién hizo qué y cuándo.

---

### 2. ¿Cuál es la diferencia entre BEFORE y AFTER trigger?
**Respuesta:**
- `BEFORE`: se ejecuta ANTES de la operación. Puede MODIFICAR o CANCELAR la operación.
- `AFTER`: se ejecuta DESPUÉS. La operación ya se realizó. Útil para auditoría y notificaciones.

```sql
BEFORE INSERT → validar datos, ajustar valores antes de guardar
AFTER INSERT  → registrar en auditoría, disparar notificación
```

---

### 3. ¿Qué son OLD y NEW en un trigger?
**Respuesta:**
- `OLD`: el registro ANTES del cambio (disponible en UPDATE y DELETE)
- `NEW`: el registro DESPUÉS del cambio (disponible en INSERT y UPDATE)

```sql
-- En un UPDATE: OLD.precio = 100, NEW.precio = 150
-- En un INSERT: solo NEW está disponible
-- En un DELETE: solo OLD está disponible
```

---

### 4. ¿Cuándo usar un trigger vs lógica en la aplicación?
**Respuesta:**
**Trigger cuando:**
- Auditoría (SIEMPRE se debe registrar, sin importar quién modifica)
- Campos calculados automáticos (total = subtotal + impuesto)
- Integridad que no se puede expresar con constraints

**Aplicación cuando:**
- Lógica de negocio compleja (difícil de debuguear en PL/pgSQL)
- Necesitas enviar emails, llamar APIs externas
- La lógica puede cambiar frecuentemente

---

### 5. ¿Puede un trigger causar problemas de rendimiento?
**Respuesta:**
Sí. Se ejecuta en CADA fila afectada. Si haces UPDATE de 100,000 filas y tienes un trigger → se ejecuta 100,000 veces. Puede hacer el batch UPDATE 10x más lento.

Mitigaciones: `FOR EACH STATEMENT` (una vez por operación, no por fila), o desactivar triggers temporalmente para operaciones masivas: `ALTER TABLE tabla DISABLE TRIGGER ALL;`

---

## Nivel Mid

### 6. ¿Cómo implementarías un sistema de auditoría completo con triggers?
**Respuesta:**
```sql
CREATE OR REPLACE FUNCTION fn_auditar()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria(tabla, operacion, registro_id, datos_antes, datos_despues, usuario, fecha)
    VALUES (TG_TABLE_NAME, TG_OP,
        COALESCE(NEW.id, OLD.id),
        CASE WHEN TG_OP != 'INSERT' THEN row_to_json(OLD) END,
        CASE WHEN TG_OP != 'DELETE' THEN row_to_json(NEW) END,
        current_user, NOW());
    RETURN COALESCE(NEW, OLD);
END;
$$ LANGUAGE plpgsql;

-- Aplicar a cada tabla:
CREATE TRIGGER trg_productos_audit
AFTER INSERT OR UPDATE OR DELETE ON productos
FOR EACH ROW EXECUTE FUNCTION fn_auditar();
```

---

### 7. ¿Qué es un trigger recursivo y cómo evitarlo?
**Respuesta:**
Cuando un trigger modifica la misma tabla → dispara el trigger de nuevo → loop infinito.

Solución en PostgreSQL: usar variable de sesión como "flag":
```sql
IF current_setting('app.in_trigger', true) = 'true' THEN RETURN NEW; END IF;
PERFORM set_config('app.in_trigger', 'true', true);
-- ... lógica ...
PERFORM set_config('app.in_trigger', 'false', true);
```

---

### 8. ¿Cuál es la diferencia entre FOR EACH ROW y FOR EACH STATEMENT?
**Respuesta:**
- `FOR EACH ROW`: se ejecuta una vez POR CADA fila afectada. Tiene acceso a OLD/NEW.
- `FOR EACH STATEMENT`: se ejecuta UNA sola vez por operación completa. NO tiene OLD/NEW.

`UPDATE productos SET precio = precio * 1.1` con 1000 filas:
- FOR EACH ROW → trigger se ejecuta 1000 veces
- FOR EACH STATEMENT → trigger se ejecuta 1 vez

---

### 9. ¿Cómo harías que un trigger actualice el stock al registrar una venta?
**Respuesta:**
```sql
CREATE FUNCTION fn_descontar_stock()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE productos SET stock = stock - NEW.cantidad
    WHERE id = NEW.producto_id;
    
    -- Validar que no quede negativo
    IF (SELECT stock FROM productos WHERE id = NEW.producto_id) < 0 THEN
        RAISE EXCEPTION 'Stock insuficiente para producto %', NEW.producto_id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_descontar_stock
AFTER INSERT ON detalle_venta
FOR EACH ROW EXECUTE FUNCTION fn_descontar_stock();
```

---

### 10. ¿Triggers en MySQL vs PostgreSQL vs Oracle?
**Respuesta:**
| Aspecto | PostgreSQL | MySQL | Oracle |
|---------|-----------|-------|--------|
| Lenguaje | PL/pgSQL | SQL/procedural | PL/SQL |
| FOR EACH STATEMENT | ✓ | ✗ (solo ROW) | ✓ |
| Múltiples triggers mismo evento | ✓ | ✓ (desde 8.0) | ✓ |
| INSTEAD OF trigger | ✓ (en views) | ✗ | ✓ |
| Transaccional | ✓ | ✓ | ✓ |
| RAISE EXCEPTION | ✓ | SIGNAL | RAISE_APPLICATION_ERROR |
