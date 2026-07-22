-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 11: Stored Procedures y Functions
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. Function: calcular precio con descuento e IVA ═══

CREATE OR REPLACE FUNCTION fn_precio_final(
    p_precio NUMERIC,
    p_tipo_cliente VARCHAR DEFAULT 'REGULAR'
) RETURNS NUMERIC AS $$
DECLARE
    v_descuento NUMERIC := 0;
    v_iva NUMERIC := 0.16;
BEGIN
    -- Determinar descuento según tipo
    v_descuento := CASE p_tipo_cliente
        WHEN 'VIP' THEN 0.10
        WHEN 'MAYORISTA' THEN 0.20
        ELSE 0
    END;

    RETURN ROUND(p_precio * (1 - v_descuento) * (1 + v_iva), 2);
END;
$$ LANGUAGE plpgsql IMMUTABLE;

-- Uso:
-- SELECT fn_precio_final(1000, 'VIP');       → 1044.00 (10% desc + 16% IVA)
-- SELECT fn_precio_final(1000, 'REGULAR');   → 1160.00 (sin desc + 16% IVA)
-- SELECT fn_precio_final(1000, 'MAYORISTA'); → 928.00  (20% desc + 16% IVA)

-- ═══ 2. Function que retorna tabla: productos con alerta ═══

CREATE OR REPLACE FUNCTION fn_productos_stock_bajo(p_minimo INTEGER DEFAULT 10)
RETURNS TABLE (
    producto VARCHAR,
    sucursal VARCHAR,
    stock_actual INTEGER,
    stock_minimo INTEGER,
    diferencia INTEGER
) AS $$
BEGIN
    RETURN QUERY
    SELECT p.nombre::VARCHAR, s.nombre::VARCHAR,
           i.cantidad, i.cantidad_minima,
           i.cantidad_minima - i.cantidad
    FROM inventario i
    JOIN productos p ON i.producto_id = p.id
    JOIN sucursales s ON i.sucursal_id = s.id
    WHERE i.cantidad < COALESCE(i.cantidad_minima, p_minimo)
    ORDER BY (i.cantidad_minima - i.cantidad) DESC;
END;
$$ LANGUAGE plpgsql;

-- Uso:
-- SELECT * FROM fn_productos_stock_bajo(5);
-- SELECT * FROM fn_productos_stock_bajo();  -- usa default 10

-- ═══ 3. Procedure: registrar venta completa ═══

CREATE OR REPLACE PROCEDURE sp_registrar_venta(
    p_cliente_id INTEGER,
    p_empleado_id INTEGER,
    p_sucursal_id INTEGER,
    p_items JSONB  -- [{"producto_id": 1, "cantidad": 2}, ...]
) LANGUAGE plpgsql AS $$
DECLARE
    v_venta_id INTEGER;
    v_subtotal NUMERIC := 0;
    v_item JSONB;
    v_precio NUMERIC;
    v_stock INTEGER;
    v_numero VARCHAR;
BEGIN
    -- Generar número de venta
    v_numero := 'V-' || TO_CHAR(NOW(), 'YYYYMMDD') || '-' || LPAD(nextval('ventas_id_seq')::TEXT, 5, '0');

    -- Crear cabecera de venta
    INSERT INTO ventas (numero_venta, cliente_id, empleado_id, sucursal_id, estado)
    VALUES (v_numero, p_cliente_id, p_empleado_id, p_sucursal_id, 'COMPLETADA')
    RETURNING id INTO v_venta_id;

    -- Procesar cada item
    FOR v_item IN SELECT * FROM jsonb_array_elements(p_items) LOOP
        -- Obtener precio del producto
        SELECT precio INTO v_precio FROM productos WHERE id = (v_item->>'producto_id')::INT;

        IF v_precio IS NULL THEN
            RAISE EXCEPTION 'Producto % no encontrado', v_item->>'producto_id';
        END IF;

        -- Verificar stock
        SELECT cantidad INTO v_stock FROM inventario
        WHERE producto_id = (v_item->>'producto_id')::INT AND sucursal_id = p_sucursal_id;

        IF COALESCE(v_stock, 0) < (v_item->>'cantidad')::INT THEN
            RAISE EXCEPTION 'Stock insuficiente para producto %. Disponible: %, Solicitado: %',
                v_item->>'producto_id', COALESCE(v_stock, 0), v_item->>'cantidad';
        END IF;

        -- Insertar detalle
        INSERT INTO detalle_venta (venta_id, producto_id, cantidad, precio_unitario, subtotal)
        VALUES (v_venta_id, (v_item->>'producto_id')::INT, (v_item->>'cantidad')::INT,
                v_precio, v_precio * (v_item->>'cantidad')::INT);

        -- Descontar stock
        UPDATE inventario SET cantidad = cantidad - (v_item->>'cantidad')::INT
        WHERE producto_id = (v_item->>'producto_id')::INT AND sucursal_id = p_sucursal_id;

        v_subtotal := v_subtotal + (v_precio * (v_item->>'cantidad')::INT);
    END LOOP;

    -- Actualizar totales de la venta
    UPDATE ventas SET
        subtotal = v_subtotal,
        impuesto = ROUND(v_subtotal * 0.16, 2),
        total = ROUND(v_subtotal * 1.16, 2)
    WHERE id = v_venta_id;

    RAISE NOTICE '✓ Venta % registrada. Total: $%', v_numero, ROUND(v_subtotal * 1.16, 2);
END;
$$;

-- Uso:
-- CALL sp_registrar_venta(1, 7, 1, '[{"producto_id": 1, "cantidad": 2}, {"producto_id": 3, "cantidad": 1}]'::JSONB);

-- ═══ 4. Function: reporte de ventas por período ═══

CREATE OR REPLACE FUNCTION fn_reporte_ventas(
    p_fecha_inicio DATE DEFAULT DATE_TRUNC('month', CURRENT_DATE)::DATE,
    p_fecha_fin DATE DEFAULT CURRENT_DATE
) RETURNS TABLE (
    fecha DATE,
    num_ventas BIGINT,
    monto_total NUMERIC,
    ticket_promedio NUMERIC,
    mejor_vendedor TEXT
) AS $$
BEGIN
    RETURN QUERY
    SELECT v.fecha::DATE,
           COUNT(*)::BIGINT,
           SUM(v.total),
           ROUND(AVG(v.total), 2),
           (SELECT e.nombre || ' ' || e.apellido
            FROM ventas v2 JOIN empleados e ON v2.empleado_id = e.id
            WHERE v2.fecha::DATE = v.fecha::DATE
            GROUP BY e.id, e.nombre, e.apellido
            ORDER BY SUM(v2.total) DESC LIMIT 1)
    FROM ventas v
    WHERE v.fecha::DATE BETWEEN p_fecha_inicio AND p_fecha_fin
    AND v.estado = 'COMPLETADA'
    GROUP BY v.fecha::DATE
    ORDER BY v.fecha::DATE DESC;
END;
$$ LANGUAGE plpgsql;

-- Uso:
-- SELECT * FROM fn_reporte_ventas('2026-07-01', '2026-07-31');
-- SELECT * FROM fn_reporte_ventas();  -- mes actual

-- ═══ 5. Function: análisis de cliente ═══

CREATE OR REPLACE FUNCTION fn_perfil_cliente(p_cliente_id INTEGER)
RETURNS JSONB AS $$
DECLARE
    v_resultado JSONB;
BEGIN
    SELECT jsonb_build_object(
        'cliente', cl.nombre || ' ' || cl.apellido,
        'tipo', cl.tipo,
        'email', cl.email,
        'total_compras', COUNT(v.id),
        'monto_total', COALESCE(SUM(v.total), 0),
        'ticket_promedio', ROUND(COALESCE(AVG(v.total), 0), 2),
        'primera_compra', MIN(v.fecha),
        'ultima_compra', MAX(v.fecha),
        'dias_sin_comprar', CURRENT_DATE - MAX(v.fecha)::DATE,
        'categoria_favorita', (
            SELECT c.nombre FROM detalle_venta dv
            JOIN ventas v2 ON dv.venta_id = v2.id
            JOIN productos p ON dv.producto_id = p.id
            JOIN categorias c ON p.categoria_id = c.id
            WHERE v2.cliente_id = p_cliente_id
            GROUP BY c.nombre ORDER BY SUM(dv.cantidad) DESC LIMIT 1
        )
    ) INTO v_resultado
    FROM clientes cl
    LEFT JOIN ventas v ON v.cliente_id = cl.id AND v.estado = 'COMPLETADA'
    WHERE cl.id = p_cliente_id
    GROUP BY cl.id, cl.nombre, cl.apellido, cl.tipo, cl.email;

    RETURN v_resultado;
END;
$$ LANGUAGE plpgsql;

-- Uso:
-- SELECT fn_perfil_cliente(1);
-- Retorna JSON con toda la info del cliente
