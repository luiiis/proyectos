# Módulo 10: Triggers

## ¿Qué es un Trigger?
Un trigger es código que se ejecuta AUTOMÁTICAMENTE cuando ocurre un evento (INSERT, UPDATE, DELETE) en una tabla.

**Analogía**: Una alarma que se activa cuando alguien abre la puerta. No la activas tú, se activa sola.

---

## 1. Tipos de Triggers

| Tipo | Cuándo se ejecuta | Uso típico |
|------|-------------------|-----------|
| BEFORE INSERT | Antes de insertar | Validar datos, auto-calcular campos |
| AFTER INSERT | Después de insertar | Auditoría, notificaciones |
| BEFORE UPDATE | Antes de actualizar | Validar cambios, guardar historial |
| AFTER UPDATE | Después de actualizar | Auditoría, sincronizar datos |
| BEFORE DELETE | Antes de eliminar | Prevenir borrado, soft delete |
| AFTER DELETE | Después de eliminar | Auditoría, limpiar dependencias |

---

## 2. Trigger de Auditoría Completa (PostgreSQL)

```sql
-- Función que registra CUALQUIER cambio en la tabla auditoria
CREATE OR REPLACE FUNCTION fn_auditoria()
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

-- Aplicar a tablas que quieres auditar
CREATE TRIGGER trg_auditoria_productos
    AFTER INSERT OR UPDATE OR DELETE ON productos
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

CREATE TRIGGER trg_auditoria_ventas
    AFTER INSERT OR UPDATE OR DELETE ON ventas
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();

CREATE TRIGGER trg_auditoria_empleados
    AFTER INSERT OR UPDATE OR DELETE ON empleados
    FOR EACH ROW EXECUTE FUNCTION fn_auditoria();
```

## 3. Trigger para Actualizar Inventario

```sql
-- Cuando se inserta un detalle de venta, descontar del inventario
CREATE OR REPLACE FUNCTION fn_descontar_inventario()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE inventario
    SET cantidad = cantidad - NEW.cantidad,
        ultima_actualizacion = NOW()
    WHERE producto_id = NEW.producto_id
    AND sucursal_id = (SELECT sucursal_id FROM ventas WHERE id = NEW.venta_id);
    
    -- Verificar que no quede negativo
    IF (SELECT cantidad FROM inventario 
        WHERE producto_id = NEW.producto_id 
        AND sucursal_id = (SELECT sucursal_id FROM ventas WHERE id = NEW.venta_id)) < 0 THEN
        RAISE EXCEPTION 'Stock insuficiente para producto %', NEW.producto_id;
    END IF;
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_descontar_inventario
    AFTER INSERT ON detalle_venta
    FOR EACH ROW EXECUTE FUNCTION fn_descontar_inventario();
```

## 4. Trigger para Calcular Totales

```sql
-- Auto-calcular subtotal en detalle_venta
CREATE OR REPLACE FUNCTION fn_calcular_subtotal()
RETURNS TRIGGER AS $$
BEGIN
    NEW.subtotal := NEW.cantidad * NEW.precio_unitario - COALESCE(NEW.descuento, 0);
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_calcular_subtotal
    BEFORE INSERT OR UPDATE ON detalle_venta
    FOR EACH ROW EXECUTE FUNCTION fn_calcular_subtotal();

-- Auto-actualizar total de la venta cuando cambia un detalle
CREATE OR REPLACE FUNCTION fn_actualizar_total_venta()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE ventas
    SET subtotal = (SELECT COALESCE(SUM(subtotal), 0) FROM detalle_venta WHERE venta_id = NEW.venta_id),
        impuesto = (SELECT COALESCE(SUM(subtotal), 0) * 0.16 FROM detalle_venta WHERE venta_id = NEW.venta_id),
        total = (SELECT COALESCE(SUM(subtotal), 0) * 1.16 FROM detalle_venta WHERE venta_id = NEW.venta_id)
    WHERE id = NEW.venta_id;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_actualizar_total
    AFTER INSERT OR UPDATE OR DELETE ON detalle_venta
    FOR EACH ROW EXECUTE FUNCTION fn_actualizar_total_venta();
```

## 5. Ejercicios

1. Crea un trigger que impida eliminar empleados con ventas asociadas
2. Crea un trigger que envíe una alerta (INSERT en tabla alertas) cuando el stock baja del mínimo
3. Crea un trigger que actualice `updated_at` automáticamente en cada UPDATE
4. Crea un trigger que convierta emails a minúsculas antes de insertar
5. ¿Cuáles son los riesgos de tener demasiados triggers?

---

## Siguiente Módulo
→ [11-Procedimientos](../11-procedimientos/README.md)
