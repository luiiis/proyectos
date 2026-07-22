# Preguntas de Entrevista - Módulo 03: DML

## Nivel Junior

### 1. ¿Cuál es la diferencia entre INSERT, UPDATE y DELETE?
**Respuesta:**
- `INSERT`: agrega filas nuevas a una tabla
- `UPDATE`: modifica filas existentes
- `DELETE`: elimina filas existentes

Los tres son DML (Data Manipulation Language): manipulan DATOS, no estructura.

---

### 2. ¿Qué pasa si haces UPDATE sin WHERE?
**Respuesta:**
Actualiza TODAS las filas de la tabla. Es el error más peligroso en SQL:
```sql
UPDATE productos SET precio = 0;  -- ¡TODOS los productos a precio 0!
```
Regla: SIEMPRE verificar el WHERE antes de ejecutar un UPDATE o DELETE. Hacer SELECT primero con el mismo WHERE.

---

### 3. ¿Qué es INSERT ... ON CONFLICT (UPSERT)?
**Respuesta:**
Insertar si no existe, actualizar si ya existe (por clave única):
```sql
INSERT INTO productos (sku, nombre, precio)
VALUES ('LAP-001', 'Laptop HP', 18999)
ON CONFLICT (sku) DO UPDATE SET precio = EXCLUDED.precio;
-- Si sku='LAP-001' ya existe → actualiza el precio
-- Si no existe → inserta
```

---

### 4. ¿Cómo harías un INSERT masivo eficiente?
**Respuesta:**
```sql
-- Múltiples VALUES en 1 statement (más rápido que N inserts individuales):
INSERT INTO productos (nombre, precio) VALUES
('Producto 1', 100),
('Producto 2', 200),
('Producto 3', 300);

-- INSERT ... SELECT (copiar de otra tabla):
INSERT INTO productos_backup SELECT * FROM productos WHERE activo = true;

-- COPY (el más rápido, desde archivo CSV):
COPY productos FROM '/tmp/productos.csv' CSV HEADER;
```

---

### 5. ¿Cuál es la diferencia entre DELETE y TRUNCATE?
**Respuesta:**
- `DELETE`: fila por fila, genera logs, se puede hacer ROLLBACK, dispara triggers
- `TRUNCATE`: elimina todo de golpe, no genera logs individuales, más rápido, NO dispara triggers row-level, resetea secuencias

---

## Nivel Mid

### 6. ¿Qué es un RETURNING clause?
**Respuesta:**
PostgreSQL permite ver qué se insertó/actualizó/eliminó:
```sql
INSERT INTO productos (nombre, precio) VALUES ('Nuevo', 999)
RETURNING id, nombre, created_at;
-- Devuelve: id=501, nombre='Nuevo', created_at='2026-07-21 10:30:00'
-- Útil para obtener el ID generado sin hacer otro SELECT
```

---

### 7. ¿Cómo harías un UPDATE con datos de otra tabla?
**Respuesta:**
```sql
-- Actualizar precios basándose en una tabla de descuentos:
UPDATE productos p
SET precio = p.precio * (1 - d.porcentaje/100)
FROM descuentos d
WHERE d.categoria_id = p.categoria_id AND d.activo = true;
```

---

### 8. ¿Qué es un CTE (WITH) y cómo se usa con DML?
**Respuesta:**
```sql
-- Eliminar clientes inactivos y registrar cuáles se eliminaron:
WITH eliminados AS (
    DELETE FROM clientes WHERE activo = false AND last_login < '2025-01-01'
    RETURNING id, nombre, email
)
INSERT INTO clientes_eliminados (cliente_id, nombre, email, fecha_eliminacion)
SELECT id, nombre, email, NOW() FROM eliminados;
```

---

### 9. ¿Cómo previenes inyección SQL en DML?
**Respuesta:**
NUNCA concatenar valores directamente en SQL:
```java
// MAL (vulnerable):
"UPDATE productos SET precio = " + input + " WHERE id = " + id
// Si input = "0; DROP TABLE productos; --" → desastre

// BIEN (PreparedStatement):
PreparedStatement ps = conn.prepareStatement("UPDATE productos SET precio = ? WHERE id = ?");
ps.setDouble(1, precio);
ps.setLong(2, id);
```

---

### 10. ¿Cómo manejas updates concurrentes en la misma fila?
**Respuesta:**
- **Bloqueo pesimista:** `SELECT ... FOR UPDATE` antes del UPDATE
- **Bloqueo optimista:** columna `version` o `updated_at`, verificar que no cambió:
```sql
UPDATE productos SET precio = 999, version = version + 1
WHERE id = 1 AND version = 5;  -- Solo actualiza si nadie más lo cambió
-- Si rows affected = 0 → alguien lo cambió antes → reintentar
```
