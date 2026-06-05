# Módulo 11: Stored Procedures y Functions

## ¿Qué son?
Bloques de código SQL almacenados en la BD que se ejecutan bajo demanda. Encapsulan lógica de negocio compleja.

---

## 1. Functions (devuelven un valor)

```sql
-- PostgreSQL: Función que calcula el total de compras de un cliente
CREATE OR REPLACE FUNCTION fn_total_compras_cliente(p_cliente_id INTEGER)
RETURNS NUMERIC AS $$
DECLARE
    v_total NUMERIC;
BEGIN
    SELECT COALESCE(SUM(total), 0) INTO v_total
    FROM ventas
    WHERE cliente_id = p_cliente_id AND estado = 'COMPLETADA';
    
    RETURN v_total;
END;
$$ LANGUAGE plpgsql;

-- Uso:
SELECT nombre, apellido, fn_total_compras_cliente(id) AS total_gastado
FROM clientes
ORDER BY fn_total_compras_cliente(id) DESC
LIMIT 10;
```

## 2. Stored Procedures (ejecutan acciones)

```sql
-- Procedimiento: Registrar una venta completa
CREATE OR REPLACE PROCEDURE sp_registrar_venta(
    p_cliente_id INTEGER,
    p_empleado_id INTEGER,
    p_sucursal_id INTEGER,
    p_productos JSONB  -- [{producto_id, cantidad, precio}]
)
LANGUAGE plpgsql AS $$
DECLARE
    v_venta_id INTEGER;
    v_numero VARCHAR(20);
    v_item JSONB;
    v_subtotal NUMERIC := 0;
BEGIN
    -- Generar número de venta
    v_numero := 'V-' || TO_CHAR(NOW(), 'YYYYMMDD') || '-' || LPAD(nextval('seq_venta_num')::text, 5, '0');
    
    -- Crear encabezado de venta
    INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id)
    VALUES (v_numero, p_cliente_id, p_empleado_id, p_sucursal_id)
    RETURNING id INTO v_venta_id;
    
    -- Insertar detalles
    FOR v_item IN SELECT * FROM jsonb_array_elements(p_productos)
    LOOP
        INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
        VALUES (
            v_venta_id,
            (v_item->>'producto_id')::integer,
            (v_item->>'cantidad')::integer,
            (v_item->>'precio')::numeric,
            (v_item->>'cantidad')::integer * (v_item->>'precio')::numeric
        );
        v_subtotal := v_subtotal + (v_item->>'cantidad')::integer * (v_item->>'precio')::numeric;
    END LOOP;
    
    -- Actualizar totales
    UPDATE ventas
    SET subtotal = v_subtotal, impuesto = v_subtotal * 0.16, total = v_subtotal * 1.16
    WHERE id = v_venta_id;
    
    RAISE NOTICE 'Venta % creada exitosamente. Total: %', v_numero, v_subtotal * 1.16;
END;
$$;

-- Uso:
CALL sp_registrar_venta(
    1,  -- cliente_id
    7,  -- empleado_id
    1,  -- sucursal_id
    '[{"producto_id": 1, "cantidad": 2, "precio": 18999.99}, {"producto_id": 5, "cantidad": 1, "precio": 1899.00}]'::jsonb
);
```

## 3. Cursores (iterar fila por fila)

```sql
-- Procedimiento con cursor: aplicar descuento a clientes VIP
CREATE OR REPLACE PROCEDURE sp_aplicar_descuento_vip()
LANGUAGE plpgsql AS $$
DECLARE
    cur_vip CURSOR FOR SELECT id, nombre FROM clientes WHERE tipo = 'VIP';
    v_cliente RECORD;
    v_descuento NUMERIC;
BEGIN
    OPEN cur_vip;
    LOOP
        FETCH cur_vip INTO v_cliente;
        EXIT WHEN NOT FOUND;
        
        -- Calcular descuento basado en historial
        SELECT CASE
            WHEN SUM(total) > 100000 THEN 0.15
            WHEN SUM(total) > 50000 THEN 0.10
            ELSE 0.05
        END INTO v_descuento
        FROM ventas WHERE cliente_id = v_cliente.id;
        
        RAISE NOTICE 'Cliente %: descuento %', v_cliente.nombre, v_descuento * 100 || '%';
    END LOOP;
    CLOSE cur_vip;
END;
$$;
```

## 4. Manejo de Errores

```sql
CREATE OR REPLACE PROCEDURE sp_transferir_stock(
    p_producto_id INTEGER,
    p_origen INTEGER,
    p_destino INTEGER,
    p_cantidad INTEGER
)
LANGUAGE plpgsql AS $$
DECLARE
    v_stock_actual INTEGER;
BEGIN
    -- Verificar stock disponible
    SELECT cantidad INTO v_stock_actual
    FROM inventario
    WHERE producto_id = p_producto_id AND sucursal_id = p_origen;
    
    IF v_stock_actual IS NULL THEN
        RAISE EXCEPTION 'Producto % no existe en sucursal %', p_producto_id, p_origen;
    END IF;
    
    IF v_stock_actual < p_cantidad THEN
        RAISE EXCEPTION 'Stock insuficiente. Disponible: %, Solicitado: %', v_stock_actual, p_cantidad;
    END IF;
    
    -- Ejecutar transferencia
    UPDATE inventario SET cantidad = cantidad - p_cantidad WHERE producto_id = p_producto_id AND sucursal_id = p_origen;
    
    INSERT INTO inventario (producto_id, sucursal_id, cantidad)
    VALUES (p_producto_id, p_destino, p_cantidad)
    ON CONFLICT (producto_id, sucursal_id) DO UPDATE SET cantidad = inventario.cantidad + p_cantidad;
    
    RAISE NOTICE 'Transferencia exitosa: % unidades de sucursal % a %', p_cantidad, p_origen, p_destino;

EXCEPTION
    WHEN OTHERS THEN
        RAISE EXCEPTION 'Error en transferencia: %', SQLERRM;
END;
$$;
```

## 5. Ejercicios

1. Crea una función que devuelva el margen de ganancia de un producto
2. Crea un procedimiento que cierre ventas pendientes de más de 30 días
3. Crea un procedimiento de "cierre de mes" que genere un resumen
4. Crea una función que devuelva el ranking de un empleado en su sucursal
5. Implementa un procedimiento de "devolución de venta" que restaure inventario

---

## Siguiente Módulo
→ [12-Transacciones](../12-transacciones/README.md)
