# Ejercicios y Retos - Proyecto 05: Base de Datos Ventas

## Reto 1: Queries básicos
Escribe las siguientes consultas SQL:

1. Listar todos los productos con precio mayor a $5,000
2. Contar cuántos clientes hay por ciudad
3. Mostrar las 5 ventas más recientes con nombre del cliente
4. Calcular el total de ventas del mes actual

**Lo que practicas:** SELECT, WHERE, JOIN, GROUP BY, ORDER BY, funciones de fecha

---

## Reto 2: Subconsultas
1. Productos que NUNCA se han vendido (no aparecen en detalle_venta)
2. Clientes que han comprado más que el promedio general
3. La categoría con más productos registrados

**Pista:**
```sql
-- Productos nunca vendidos
SELECT * FROM productos 
WHERE id NOT IN (SELECT DISTINCT producto_id FROM detalle_venta);
```

**Lo que practicas:** Subconsultas, NOT IN, subquery correlacionada

---

## Reto 3: Crear un VIEW
Crea una vista `v_resumen_ventas_diario` que muestre por día:
- Fecha
- Cantidad de ventas
- Monto total
- Ticket promedio

```sql
CREATE VIEW v_resumen_ventas_diario AS
SELECT 
    DATE(fecha) AS dia,
    COUNT(*) AS num_ventas,
    SUM(total) AS monto_total,
    AVG(total) AS ticket_promedio
FROM ventas
GROUP BY DATE(fecha)
ORDER BY dia DESC;
```

**Lo que practicas:** CREATE VIEW, funciones de agregación

---

## Reto 4: Crear un TRIGGER
Crea un trigger que registre en la tabla `auditoria` cada vez que se elimine un producto:
```sql
CREATE OR REPLACE FUNCTION fn_auditar_eliminacion()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO auditoria(tabla, operacion, datos_anteriores, fecha)
    VALUES ('productos', 'DELETE', row_to_json(OLD)::text, NOW());
    RETURN OLD;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_productos_delete
BEFORE DELETE ON productos
FOR EACH ROW EXECUTE FUNCTION fn_auditar_eliminacion();
```

**Lo que practicas:** Triggers, PL/pgSQL, funciones, auditoría

---

## Reto 5: Stored Procedure para registrar venta
Crea un procedure que:
1. Reciba: cliente_id, empleado_id, lista de (producto_id, cantidad)
2. Valide stock suficiente para cada producto
3. Cree la venta + detalles
4. Descuente stock
5. Todo en una transacción (si falla un producto, se deshace todo)

**Lo que practicas:** Procedures, transacciones, validaciones, cursores

---

## Reto 6: Índices y performance
1. Ejecuta `EXPLAIN ANALYZE` en una query lenta
2. Crea un índice que mejore su performance
3. Compara tiempos antes/después

```sql
-- Sin índice (full table scan):
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha BETWEEN '2026-01-01' AND '2026-01-31';

-- Crear índice:
CREATE INDEX idx_ventas_fecha ON ventas(fecha);

-- Con índice (index scan, mucho más rápido):
EXPLAIN ANALYZE SELECT * FROM ventas WHERE fecha BETWEEN '2026-01-01' AND '2026-01-31';
```

**Lo que practicas:** EXPLAIN ANALYZE, índices B-Tree, optimización

---

## Reto 7: Normalización
La siguiente tabla NO está normalizada. Identifica los problemas y normalízala a 3FN:

```sql
-- TABLA MAL DISEÑADA:
CREATE TABLE pedidos_mal (
    id SERIAL,
    cliente_nombre VARCHAR(100),
    cliente_email VARCHAR(100),
    cliente_ciudad VARCHAR(50),
    producto_nombre VARCHAR(100),
    producto_precio DECIMAL,
    cantidad INT,
    vendedor_nombre VARCHAR(100),
    vendedor_sucursal VARCHAR(50)
);
```

**Problemas:** Redundancia, anomalías de actualización, anomalías de eliminación.

**Lo que practicas:** Normalización 1FN, 2FN, 3FN

---

## Reto 8 (Avanzado): Consulta recursiva (CTE)
Crea una tabla `categorias` jerárquica (categoria padre → hija) y escribe una query recursiva que muestre el árbol completo:

```sql
WITH RECURSIVE arbol AS (
    SELECT id, nombre, padre_id, 0 AS nivel
    FROM categorias WHERE padre_id IS NULL
    
    UNION ALL
    
    SELECT c.id, c.nombre, c.padre_id, a.nivel + 1
    FROM categorias c JOIN arbol a ON c.padre_id = a.id
)
SELECT REPEAT('  ', nivel) || nombre AS categoria FROM arbol ORDER BY nivel;
```

**Lo que practicas:** CTEs recursivas, datos jerárquicos
