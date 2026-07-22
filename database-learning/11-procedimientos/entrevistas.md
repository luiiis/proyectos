# Preguntas de Entrevista - Módulo 11: Stored Procedures y Functions

## Nivel Junior

### 1. ¿Cuál es la diferencia entre Function y Procedure?
**Respuesta:**
- **Function:** RETORNA un valor. Se puede usar dentro de un SELECT.
- **Procedure:** NO retorna valor (ejecuta acciones). Se llama con CALL.

```sql
-- Function:
SELECT calcular_iva(18999);  -- Retorna 3039.84

-- Procedure:
CALL registrar_venta(1, ARRAY[1,2,3], ARRAY[2,1,5]);  -- No retorna, ejecuta
```

---

### 2. ¿Cuándo usar un stored procedure vs lógica en la aplicación?
**Respuesta:**
**Procedure cuando:**
- Operaciones atómicas complejas (venta = insertar cabecera + detalle + descontar stock)
- Cálculos masivos sobre millones de filas (más rápido que traer datos a Java)
- Lógica que DEBE ejecutarse sin importar qué aplicación acceda

**Aplicación cuando:**
- Lógica de negocio que cambia frecuentemente
- Necesitas debugging, testing, y herramientas modernas
- Múltiples desarrolladores (PL/pgSQL es más difícil de mantener)

---

### 3. ¿Qué es PL/pgSQL?
**Respuesta:**
Lenguaje procedural de PostgreSQL para escribir funciones y procedures. Combina SQL con estructuras de control (IF, LOOP, variables):
```sql
CREATE FUNCTION fn_descuento(precio NUMERIC, tipo_cliente VARCHAR)
RETURNS NUMERIC AS $$
BEGIN
    IF tipo_cliente = 'VIP' THEN RETURN precio * 0.85;
    ELSIF tipo_cliente = 'MAYORISTA' THEN RETURN precio * 0.80;
    ELSE RETURN precio;
    END IF;
END;
$$ LANGUAGE plpgsql;
```

---

### 4. ¿Qué es un parámetro IN, OUT e INOUT?
**Respuesta:**
- `IN`: parámetro de entrada (default). Solo lectura.
- `OUT`: parámetro de salida. La función lo llena.
- `INOUT`: ambos. Entra con un valor y sale modificado.

```sql
CREATE PROCEDURE sp_calcular(IN precio NUMERIC, OUT con_iva NUMERIC, OUT solo_iva NUMERIC)
LANGUAGE plpgsql AS $$
BEGIN
    solo_iva := precio * 0.16;
    con_iva := precio + solo_iva;
END;
$$;
CALL sp_calcular(100, NULL, NULL);  -- con_iva=116, solo_iva=16
```

---

### 5. ¿Cómo manejas errores en PL/pgSQL?
**Respuesta:**
```sql
CREATE FUNCTION fn_dividir(a NUMERIC, b NUMERIC) RETURNS NUMERIC AS $$
BEGIN
    IF b = 0 THEN
        RAISE EXCEPTION 'División por cero no permitida';
    END IF;
    RETURN a / b;
EXCEPTION
    WHEN division_by_zero THEN
        RAISE NOTICE 'Error: %', SQLERRM;
        RETURN NULL;
    WHEN OTHERS THEN
        RAISE NOTICE 'Error inesperado: %', SQLERRM;
        RETURN NULL;
END;
$$ LANGUAGE plpgsql;
```

---

## Nivel Mid

### 6. ¿Cómo implementarías un procedure para registrar una venta completa?
**Respuesta:**
```sql
CREATE OR REPLACE PROCEDURE sp_registrar_venta(
    p_cliente_id INT, p_empleado_id INT, p_items JSONB
) LANGUAGE plpgsql AS $$
DECLARE
    v_venta_id INT;
    v_total NUMERIC := 0;
    item JSONB;
BEGIN
    -- Crear cabecera
    INSERT INTO ventas (cliente_id, empleado_id, fecha)
    VALUES (p_cliente_id, p_empleado_id, NOW()) RETURNING id INTO v_venta_id;
    
    -- Procesar items
    FOR item IN SELECT * FROM jsonb_array_elements(p_items) LOOP
        -- Validar stock
        IF (SELECT stock FROM productos WHERE id = (item->>'producto_id')::INT) < (item->>'cantidad')::INT THEN
            RAISE EXCEPTION 'Stock insuficiente para producto %', item->>'producto_id';
        END IF;
        
        -- Insertar detalle + descontar stock
        INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario)
        SELECT v_venta_id, (item->>'producto_id')::INT, (item->>'cantidad')::INT, precio
        FROM productos WHERE id = (item->>'producto_id')::INT;
        
        UPDATE productos SET stock = stock - (item->>'cantidad')::INT
        WHERE id = (item->>'producto_id')::INT;
    END LOOP;
    
    -- Actualizar total
    UPDATE ventas SET total = (SELECT SUM(cantidad * precio_unitario) FROM detalle_venta WHERE venta_id = v_venta_id)
    WHERE id = v_venta_id;
END;
$$;
```

---

### 7. ¿Qué es una función SET-RETURNING (retorna tabla)?
**Respuesta:**
```sql
CREATE FUNCTION fn_productos_con_alerta(minimo INT DEFAULT 5)
RETURNS TABLE(producto_nombre VARCHAR, stock_actual INT, diferencia INT)
LANGUAGE plpgsql AS $$
BEGIN
    RETURN QUERY
    SELECT p.nombre, p.stock, minimo - p.stock
    FROM productos p
    WHERE p.stock < minimo AND p.activo = true
    ORDER BY p.stock;
END;
$$;

-- Usar como una tabla:
SELECT * FROM fn_productos_con_alerta(10);
```

---

### 8. ¿Functions en PostgreSQL vs MySQL vs Oracle?
**Respuesta:**
| Aspecto | PostgreSQL | MySQL | Oracle |
|---------|-----------|-------|--------|
| Lenguaje | PL/pgSQL, PL/Python, PL/Perl | SQL/Procedural | PL/SQL |
| RETURNS TABLE | ✓ | ✗ | ✓ (PIPELINED) |
| Procedures (CALL) | ✓ (desde PG 11) | ✓ | ✓ |
| Overloading | ✓ (mismo nombre, diferentes params) | ✗ | ✓ |
| Exception handling | ✓ | ✓ (HANDLER) | ✓ |
| Debugging | Limited (RAISE NOTICE) | Limited | SQL Developer debugger |
