-- ════════════════════════════════════════════════════════════════
-- MÓDULO 18 - PROYECTO: Features de Oracle
-- ════════════════════════════════════════════════════════════════
-- Ejecutar como empresa_user en Oracle XE
-- ════════════════════════════════════════════════════════════════

-- ═══════ 1. PACKAGE PL/SQL (agrupar lógica relacionada) ═══════
-- Un Package es como una "clase" en Java: agrupa funciones y procedures relacionados

CREATE OR REPLACE PACKAGE pkg_reportes AS
    -- Declaración pública (lo que otros pueden llamar)
    FUNCTION fn_ventas_mes(p_año NUMBER, p_mes NUMBER) RETURN NUMBER;
    FUNCTION fn_top_producto(p_categoria NUMBER) RETURN VARCHAR2;
    PROCEDURE sp_resumen_sucursal(p_sucursal_id NUMBER);
END pkg_reportes;
/

CREATE OR REPLACE PACKAGE BODY pkg_reportes AS

    FUNCTION fn_ventas_mes(p_año NUMBER, p_mes NUMBER) RETURN NUMBER IS
        v_total NUMBER;
    BEGIN
        SELECT NVL(SUM(total), 0) INTO v_total
        FROM ventas
        WHERE EXTRACT(YEAR FROM fecha) = p_año
        AND EXTRACT(MONTH FROM fecha) = p_mes
        AND estado = 'COMPLETADA';
        RETURN v_total;
    END;

    FUNCTION fn_top_producto(p_categoria NUMBER) RETURN VARCHAR2 IS
        v_nombre VARCHAR2(150);
    BEGIN
        SELECT nombre INTO v_nombre
        FROM (
            SELECT p.nombre, SUM(dv.cantidad) AS total_vendido
            FROM productos p
            JOIN detalle_venta dv ON dv.producto_id = p.id
            WHERE p.categoria_id = p_categoria
            GROUP BY p.nombre
            ORDER BY total_vendido DESC
        ) WHERE ROWNUM = 1;
        RETURN v_nombre;
    EXCEPTION
        WHEN NO_DATA_FOUND THEN RETURN 'Sin datos';
    END;

    PROCEDURE sp_resumen_sucursal(p_sucursal_id NUMBER) IS
        v_ventas NUMBER;
        v_empleados NUMBER;
    BEGIN
        SELECT COUNT(*), NVL(SUM(total), 0) INTO v_empleados, v_ventas
        FROM ventas WHERE sucursal_id = p_sucursal_id AND estado = 'COMPLETADA';

        DBMS_OUTPUT.PUT_LINE('Sucursal: ' || p_sucursal_id);
        DBMS_OUTPUT.PUT_LINE('Ventas totales: $' || TO_CHAR(v_ventas, '999,999,999.99'));
        DBMS_OUTPUT.PUT_LINE('Cantidad: ' || v_empleados);
    END;

END pkg_reportes;
/

-- Uso del package:
-- SELECT pkg_reportes.fn_ventas_mes(2024, 6) FROM dual;
-- EXEC pkg_reportes.sp_resumen_sucursal(1);

-- ═══════ 2. ANALYTIC FUNCTIONS (Oracle fue pionero) ═══════

-- LISTAGG: concatenar valores de múltiples filas en una sola
SELECT categoria_id,
    LISTAGG(nombre, ', ') WITHIN GROUP (ORDER BY precio DESC) AS productos
FROM productos
WHERE ROWNUM <= 50
GROUP BY categoria_id;

-- RATIO_TO_REPORT: porcentaje del total
SELECT nombre, salario,
    ROUND(RATIO_TO_REPORT(salario) OVER () * 100, 2) AS pct_nomina_total
FROM empleados
WHERE ROWNUM <= 10
ORDER BY salario DESC;

-- LAG/LEAD con múltiples offsets
SELECT fecha, total,
    LAG(total, 1) OVER (ORDER BY fecha) AS venta_anterior,
    LEAD(total, 1) OVER (ORDER BY fecha) AS venta_siguiente
FROM ventas
WHERE ROWNUM <= 20
ORDER BY fecha;

-- ═══════ 3. PAGINACIÓN en Oracle ═══════

-- Oracle 12c+: FETCH FIRST (estándar SQL)
SELECT nombre, precio FROM productos
ORDER BY precio DESC
FETCH FIRST 10 ROWS ONLY;

-- Con OFFSET (página 2)
SELECT nombre, precio FROM productos
ORDER BY precio DESC
OFFSET 10 ROWS FETCH NEXT 10 ROWS ONLY;

-- Oracle clásico: ROWNUM (legacy pero aún se ve en código viejo)
SELECT * FROM (
    SELECT nombre, precio, ROWNUM AS rn
    FROM (SELECT nombre, precio FROM productos ORDER BY precio DESC)
) WHERE rn BETWEEN 11 AND 20;

-- ═══════ 4. MERGE (UPSERT en Oracle) ═══════
MERGE INTO productos dest
USING (SELECT 'Producto Oracle Test' AS nombre, 999.99 AS precio, 'ORA-TEST-001' AS sku FROM dual) src
ON (dest.sku = src.sku)
WHEN MATCHED THEN
    UPDATE SET dest.precio = src.precio, dest.nombre = src.nombre
WHEN NOT MATCHED THEN
    INSERT (id, nombre, precio, sku, activo)
    VALUES (seq_productos.NEXTVAL, src.nombre, src.precio, src.sku, 1);

-- ═══════ 5. FLASHBACK QUERY (ver datos del pasado) ═══════
-- Oracle puede consultar cómo estaban los datos hace X minutos
-- SELECT * FROM productos AS OF TIMESTAMP (SYSTIMESTAMP - INTERVAL '30' MINUTE);
-- Útil para: "¿cuál era el precio hace 1 hora?"
