# Módulo 18: Oracle - Características Específicas

## ¿Cuándo se usa Oracle?
Oracle domina en banca, gobierno, telecomunicaciones y grandes corporaciones. Es la BD más robusta para cargas críticas con alta concurrencia y volúmenes masivos.

---

## 1. Arquitectura Oracle

```
┌─────────────────────────────────────────┐
│              INSTANCIA                   │
│  ┌─────────────────────────────────┐    │
│  │         SGA (Memoria)           │    │
│  │  ┌──────┐ ┌──────┐ ┌────────┐  │    │
│  │  │Buffer│ │Shared│ │  Redo  │  │    │
│  │  │Cache │ │ Pool │ │Log Buf │  │    │
│  │  └──────┘ └──────┘ └────────┘  │    │
│  └─────────────────────────────────┘    │
│  ┌─────────────────────────────────┐    │
│  │     Background Processes         │    │
│  │  DBWR  LGWR  CKPT  SMON  PMON  │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
                    │
┌─────────────────────────────────────────┐
│              BASE DE DATOS               │
│  ┌──────────┐ ┌──────────┐ ┌────────┐  │
│  │Datafiles │ │Redo Logs │ │Control │  │
│  │(.dbf)    │ │(.log)    │ │Files   │  │
│  └──────────┘ └──────────┘ └────────┘  │
└─────────────────────────────────────────┘
```

## 2. Diferencias Clave con PostgreSQL/MySQL

```sql
-- Oracle NO tiene AUTO_INCREMENT. Usa SEQUENCES:
CREATE SEQUENCE seq_clientes START WITH 1 INCREMENT BY 1;

-- Insertar con sequence:
INSERT INTO clientes (id, nombre) VALUES (seq_clientes.NEXTVAL, 'Carlos');

-- Oracle 12c+: IDENTITY columns (similar a AUTO_INCREMENT)
CREATE TABLE clientes (
    id NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre VARCHAR2(100)
);

-- DUAL: tabla dummy para SELECT sin FROM
SELECT SYSDATE FROM DUAL;
SELECT 2 + 2 FROM DUAL;

-- NVL en lugar de COALESCE (aunque COALESCE también funciona)
SELECT NVL(telefono, 'Sin teléfono') FROM clientes;

-- ROWNUM en lugar de LIMIT (Oracle < 12c)
SELECT * FROM (SELECT * FROM productos ORDER BY precio DESC) WHERE ROWNUM <= 10;

-- Oracle 12c+: FETCH FIRST
SELECT * FROM productos ORDER BY precio DESC FETCH FIRST 10 ROWS ONLY;

-- DECODE: versión Oracle de CASE simple
SELECT nombre, DECODE(tipo, 'VIP', 'Premium', 'REGULAR', 'Estándar', 'Otro') FROM clientes;

-- String concatenation: || (igual que PostgreSQL)
SELECT nombre || ' ' || apellido AS nombre_completo FROM clientes;

-- Fecha actual
SELECT SYSDATE FROM DUAL;        -- Solo fecha
SELECT SYSTIMESTAMP FROM DUAL;   -- Con timestamp
```

## 3. PL/SQL (Procedural Language)

```sql
-- Bloque anónimo PL/SQL
DECLARE
    v_total NUMBER;
    v_nombre VARCHAR2(100);
BEGIN
    SELECT COUNT(*), MAX(nombre) INTO v_total, v_nombre FROM productos;
    DBMS_OUTPUT.PUT_LINE('Total productos: ' || v_total);
    DBMS_OUTPUT.PUT_LINE('Último: ' || v_nombre);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        DBMS_OUTPUT.PUT_LINE('No hay datos');
    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE('Error: ' || SQLERRM);
END;
/

-- Package (agrupa procedures y functions relacionados)
CREATE OR REPLACE PACKAGE pkg_ventas AS
    FUNCTION fn_total_cliente(p_cliente_id NUMBER) RETURN NUMBER;
    PROCEDURE sp_registrar_venta(p_cliente_id NUMBER, p_empleado_id NUMBER, p_total NUMBER);
END pkg_ventas;
/

CREATE OR REPLACE PACKAGE BODY pkg_ventas AS
    FUNCTION fn_total_cliente(p_cliente_id NUMBER) RETURN NUMBER IS
        v_total NUMBER;
    BEGIN
        SELECT NVL(SUM(total), 0) INTO v_total FROM ventas WHERE cliente_id = p_cliente_id;
        RETURN v_total;
    END;

    PROCEDURE sp_registrar_venta(p_cliente_id NUMBER, p_empleado_id NUMBER, p_total NUMBER) IS
    BEGIN
        INSERT INTO ventas (id, numero_venta, cliente_id, empleado_id, total, fecha)
        VALUES (seq_ventas.NEXTVAL, 'V-' || TO_CHAR(SYSDATE, 'YYYYMMDD') || '-' || seq_ventas.CURRVAL,
                p_cliente_id, p_empleado_id, p_total, SYSDATE);
        COMMIT;
    END;
END pkg_ventas;
/
```

## 4. Tablespaces y Almacenamiento

```sql
-- Crear tablespace (espacio de almacenamiento)
CREATE TABLESPACE ts_ventas
    DATAFILE '/opt/oracle/oradata/ventas01.dbf' SIZE 500M
    AUTOEXTEND ON NEXT 100M MAXSIZE 5G;

-- Asignar tablespace a una tabla
CREATE TABLE ventas_historico (
    id NUMBER PRIMARY KEY,
    fecha DATE,
    total NUMBER(12,2)
) TABLESPACE ts_ventas;

-- Ver espacio usado
SELECT tablespace_name, 
    ROUND(SUM(bytes)/1024/1024) AS mb_total,
    ROUND(SUM(bytes - NVL(free_space,0))/1024/1024) AS mb_usado
FROM dba_data_files
GROUP BY tablespace_name;
```

## 5. Analytic Functions (Oracle fue pionero)

```sql
-- Oracle tiene las window functions más completas
SELECT nombre, salario, sucursal_id,
    RANK() OVER (PARTITION BY sucursal_id ORDER BY salario DESC) AS rank_sucursal,
    RATIO_TO_REPORT(salario) OVER (PARTITION BY sucursal_id) AS porcentaje_nomina,
    LISTAGG(nombre, ', ') WITHIN GROUP (ORDER BY nombre) OVER (PARTITION BY sucursal_id) AS colegas
FROM empleados;

-- MODEL clause (spreadsheet-like calculations)
SELECT mes, ventas, ventas_acumulado
FROM ventas_mensuales
MODEL
    DIMENSION BY (mes)
    MEASURES (total AS ventas, 0 AS ventas_acumulado)
    RULES (
        ventas_acumulado[ANY] = SUM(ventas)[mes <= CV(mes)]
    );
```

## 6. Ejercicios

1. Crea un package PL/SQL con funciones de reportes de ventas
2. Implementa un trigger de auditoría en Oracle (sintaxis diferente a PostgreSQL)
3. Crea un tablespace dedicado para datos históricos
4. Usa LISTAGG para mostrar todos los productos de cada categoría en una fila
5. Implementa paginación con FETCH FIRST y compara con ROWNUM
6. ¿Cuándo justifica el costo de Oracle vs PostgreSQL gratuito?

---

## Siguiente Módulo
→ [19-Docker](../19-docker/README.md)
