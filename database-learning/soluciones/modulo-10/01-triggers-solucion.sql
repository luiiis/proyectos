-- ════════════════════════════════════════════════════════════════
-- SOLUCIONES - Módulo 10: Triggers
-- Ejecutar en PostgreSQL
-- ════════════════════════════════════════════════════════════════

-- ═══ 1. Trigger de auditoría genérica ═══
-- Registra INSERT, UPDATE, DELETE en cualquier tabla

CREATE OR REPLACE FUNCTION fn_auditar()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_antes, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'DELETE', OLD.id, row_to_json(OLD)::TEXT, current_user, NOW());
        RETURN OLD;
    ELSIF TG_OP = 'UPDATE' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_antes, datos_despues, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'UPDATE', NEW.id, row_to_json(OLD)::TEXT, row_to_json(NEW)::TEXT, current_user, NOW());
        RETURN NEW;
    ELSIF TG_OP = 'INSERT' THEN
        INSERT INTO auditoria (tabla, operacion, registro_id, datos_despues, usuario, fecha)
        VALUES (TG_TABLE_NAME, 'INSERT', NEW.id, row_to_json(NEW)::TEXT, current_user, NOW());
        RETURN NEW;
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Aplicar a tablas sensibles:
CREATE TRIGGER trg_productos_audit AFTER INSERT OR UPDATE OR DELETE ON productos
FOR EACH ROW EXECUTE FUNCTION fn_auditar();

CREATE TRIGGER trg_ventas_audit AFTER INSERT OR UPDATE OR DELETE ON ventas
FOR EACH ROW EXECUTE FUNCTION fn_auditar();

-- ═══ 2. Trigger: descontar stock automáticamente al vender ═══

CREATE OR REPLACE FUNCTION fn_descontar_stock()
RETURNS TRIGGER AS $$
DECLARE
    v_stock_actual INTEGER;
BEGIN
    -- Obtener stock actual
    SELECT cantidad INTO v_stock_actual
    FROM inventario
    WHERE producto_id = NEW.producto_id AND sucursal_id = (
        SELECT sucursal_id FROM ventas WHERE id = NEW.venta_id
    );

    -- Validar stock suficiente
    IF v_stock_actual IS NULL OR v_stock_actual < NEW.cantidad THEN
        RAISE EXCEPTION 'Stock insuficiente para producto %. Disponible: %, Solicitado: %',
            NEW.producto_id, COALESCE(v_stock_actual, 0), NEW.cantidad;
    END IF;

    -- Descontar
    UPDATE inventario
    SET cantidad = cantidad - NEW.cantidad,
        ultima_actualizacion = NOW()
    WHERE producto_id = NEW.producto_id
    AND sucursal_id = (SELECT sucursal_id FROM ventas WHERE id = NEW.venta_id);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_descontar_stock
AFTER INSERT ON detalle_venta
FOR EACH ROW EXECUTE FUNCTION fn_descontar_stock();

-- ═══ 3. Trigger: alerta de stock bajo ═══

CREATE TABLE IF NOT EXISTS alertas_stock (
    id SERIAL PRIMARY KEY,
    producto_id INTEGER REFERENCES productos(id),
    sucursal_id INTEGER REFERENCES sucursales(id),
    stock_actual INTEGER,
    stock_minimo INTEGER,
    fecha TIMESTAMP DEFAULT NOW(),
    atendida BOOLEAN DEFAULT FALSE
);

CREATE OR REPLACE FUNCTION fn_alerta_stock_bajo()
RETURNS TRIGGER AS $$
BEGIN
    -- Si el stock quedó por debajo del mínimo
    IF NEW.cantidad < NEW.cantidad_minima THEN
        INSERT INTO alertas_stock (producto_id, sucursal_id, stock_actual, stock_minimo)
        VALUES (NEW.producto_id, NEW.sucursal_id, NEW.cantidad, NEW.cantidad_minima)
        ON CONFLICT DO NOTHING;  -- No duplicar alertas

        RAISE NOTICE '⚠️ ALERTA: Producto % en sucursal % tiene stock bajo (% < %)',
            NEW.producto_id, NEW.sucursal_id, NEW.cantidad, NEW.cantidad_minima;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_alerta_stock
AFTER UPDATE ON inventario
FOR EACH ROW
WHEN (NEW.cantidad < NEW.cantidad_minima)
EXECUTE FUNCTION fn_alerta_stock_bajo();

-- ═══ 4. Trigger: calcular total de venta automáticamente ═══

CREATE OR REPLACE FUNCTION fn_actualizar_total_venta()
RETURNS TRIGGER AS $$
DECLARE
    v_subtotal NUMERIC;
BEGIN
    -- Recalcular subtotal de la venta
    SELECT COALESCE(SUM(cantidad * precio_unitario), 0) INTO v_subtotal
    FROM detalle_venta WHERE venta_id = NEW.venta_id;

    -- Actualizar la venta
    UPDATE ventas SET
        subtotal = v_subtotal,
        impuesto = ROUND(v_subtotal * 0.16, 2),
        total = ROUND(v_subtotal * 1.16, 2)
    WHERE id = NEW.venta_id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_calcular_total
AFTER INSERT OR UPDATE OR DELETE ON detalle_venta
FOR EACH ROW EXECUTE FUNCTION fn_actualizar_total_venta();

-- ═══ 5. Trigger BEFORE: validar y normalizar datos ═══

CREATE OR REPLACE FUNCTION fn_normalizar_cliente()
RETURNS TRIGGER AS $$
BEGIN
    -- Normalizar nombre (primera letra mayúscula)
    NEW.nombre := INITCAP(TRIM(NEW.nombre));
    NEW.apellido := INITCAP(TRIM(NEW.apellido));

    -- Email siempre en minúsculas
    NEW.email := LOWER(TRIM(NEW.email));

    -- Validar formato de email básico
    IF NEW.email IS NOT NULL AND NEW.email NOT LIKE '%@%.%' THEN
        RAISE EXCEPTION 'Email inválido: %', NEW.email;
    END IF;

    -- Asignar tipo por defecto
    IF NEW.tipo IS NULL THEN
        NEW.tipo := 'REGULAR';
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_normalizar_cliente
BEFORE INSERT OR UPDATE ON clientes
FOR EACH ROW EXECUTE FUNCTION fn_normalizar_cliente();

-- ═══ 6. Trigger: log de cambios de salario ═══

CREATE TABLE IF NOT EXISTS historial_salarios (
    id SERIAL PRIMARY KEY,
    empleado_id INTEGER REFERENCES empleados(id),
    salario_anterior NUMERIC(10,2),
    salario_nuevo NUMERIC(10,2),
    diferencia NUMERIC(10,2),
    porcentaje_cambio NUMERIC(5,2),
    fecha TIMESTAMP DEFAULT NOW(),
    autorizado_por VARCHAR(50) DEFAULT current_user
);

CREATE OR REPLACE FUNCTION fn_log_cambio_salario()
RETURNS TRIGGER AS $$
BEGIN
    IF OLD.salario != NEW.salario THEN
        INSERT INTO historial_salarios (empleado_id, salario_anterior, salario_nuevo, diferencia, porcentaje_cambio)
        VALUES (NEW.id, OLD.salario, NEW.salario,
                NEW.salario - OLD.salario,
                ROUND((NEW.salario - OLD.salario) / OLD.salario * 100, 2));
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_log_salario
AFTER UPDATE OF salario ON empleados
FOR EACH ROW EXECUTE FUNCTION fn_log_cambio_salario();

-- ═══ PROBAR LOS TRIGGERS ═══

-- Probar auditoría:
-- INSERT INTO productos (nombre, precio, categoria_id) VALUES ('Test Trigger', 100, 1);
-- SELECT * FROM auditoria ORDER BY fecha DESC LIMIT 5;

-- Probar normalización:
-- INSERT INTO clientes (nombre, apellido, email) VALUES ('  juan  ', '  PÉREZ  ', '  JUAN@Email.Com  ');
-- SELECT nombre, apellido, email FROM clientes ORDER BY id DESC LIMIT 1;
-- Resultado esperado: "Juan", "Pérez", "juan@email.com"
