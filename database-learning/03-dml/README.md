# Módulo 03: DML - Data Manipulation Language

## ¿Qué es DML?
DML son los comandos para MANIPULAR los datos dentro de las tablas: insertar, actualizar y eliminar registros.

```
DDL = Crea/modifica la ESTRUCTURA (tablas, columnas)
DML = Crea/modifica los DATOS (registros, filas)
```

---

## 1. INSERT - Insertar Datos

### Sintaxis básica:
```sql
-- Insertar especificando columnas (RECOMENDADO)
INSERT INTO clientes (nombre, apellido, email, ciudad)
VALUES ('Roberto', 'Méndez', 'roberto@email.com', 'Monterrey');

-- Insertar sin especificar columnas (debe coincidir con TODAS las columnas en orden)
-- NO RECOMENDADO: si agregas una columna a la tabla, este INSERT se rompe
INSERT INTO categorias VALUES (DEFAULT, 'Gaming', 'Productos para gamers', TRUE);
```

### Insertar múltiples registros:
```sql
INSERT INTO categorias (nombre, descripcion) VALUES
('Servidores', 'Equipos de servidor'),
('Seguridad', 'Cámaras y alarmas'),
('Telefonía', 'Teléfonos y accesorios');
```

### INSERT con SELECT (copiar datos de otra tabla):
```sql
-- Crear tabla de respaldo con datos existentes
INSERT INTO clientes_respaldo (nombre, apellido, email)
SELECT nombre, apellido, email
FROM clientes
WHERE ciudad = 'Ciudad de México';
```

### INSERT con RETURNING (PostgreSQL - obtener el ID generado):
```sql
-- PostgreSQL: devuelve el registro insertado
INSERT INTO productos (nombre, precio, categoria_id)
VALUES ('Nuevo Producto', 999.99, 1)
RETURNING id, nombre, precio;
-- Resultado: id=501, nombre='Nuevo Producto', precio=999.99

-- MySQL: usar LAST_INSERT_ID()
INSERT INTO productos (nombre, precio, categoria_id) VALUES ('Nuevo', 999.99, 1);
SELECT LAST_INSERT_ID();

-- Oracle: usar RETURNING INTO (en PL/SQL)
```

---

## 2. UPDATE - Actualizar Datos

### Sintaxis básica:
```sql
-- Actualizar UN campo de UN registro
UPDATE empleados
SET salario = 50000
WHERE id = 7;

-- Actualizar MÚLTIPLES campos
UPDATE empleados
SET salario = 50000,
    puesto = 'Vendedor Senior',
    sucursal_id = 2
WHERE id = 7;

-- ⚠️ SIN WHERE actualiza TODOS los registros (PELIGROSO)
UPDATE productos SET precio = 0;  -- ¡Todos los productos a precio 0!
```

### UPDATE con cálculos:
```sql
-- Aumentar 10% el salario de todos los vendedores
UPDATE empleados
SET salario = salario * 1.10
WHERE puesto = 'Vendedor';

-- Aplicar descuento del 15% a productos de una categoría
UPDATE productos
SET precio = precio * 0.85
WHERE categoria_id = 3;
```

### UPDATE con JOIN (actualizar basado en otra tabla):
```sql
-- PostgreSQL: UPDATE con FROM
UPDATE inventario
SET cantidad = cantidad + 50
FROM productos p
WHERE inventario.producto_id = p.id
AND p.categoria_id = 1;

-- MySQL: UPDATE con JOIN
UPDATE inventario i
JOIN productos p ON i.producto_id = p.id
SET i.cantidad = i.cantidad + 50
WHERE p.categoria_id = 1;
```

### UPDATE con subconsulta:
```sql
-- Asignar el jefe de la sucursal 1 a empleados sin jefe
UPDATE empleados
SET jefe_id = (SELECT id FROM empleados WHERE puesto = 'Gerente Ventas' AND sucursal_id = 1)
WHERE jefe_id IS NULL AND sucursal_id = 1;
```

---

## 3. DELETE - Eliminar Datos

### Sintaxis básica:
```sql
-- Eliminar UN registro
DELETE FROM clientes WHERE id = 500;

-- Eliminar con condición
DELETE FROM ventas WHERE estado = 'CANCELADA' AND fecha < '2023-01-01';

-- ⚠️ SIN WHERE elimina TODOS los registros
DELETE FROM auditoria;  -- ¡Borra toda la auditoría!
```

### DELETE con subconsulta:
```sql
-- Eliminar productos que no tienen inventario en ninguna sucursal
DELETE FROM productos
WHERE id NOT IN (SELECT DISTINCT producto_id FROM inventario);
```

### Soft Delete vs Hard Delete:
```sql
-- HARD DELETE: elimina físicamente (irreversible)
DELETE FROM clientes WHERE id = 100;

-- SOFT DELETE: marca como inactivo (RECOMENDADO en producción)
UPDATE clientes SET activo = FALSE WHERE id = 100;

-- Después, las consultas filtran:
SELECT * FROM clientes WHERE activo = TRUE;
```

**¿Por qué Soft Delete?**
- Los datos históricos se preservan (ventas pasadas siguen referenciando al cliente)
- Se puede "restaurar" un registro
- Cumplimiento legal (algunas leyes exigen retener datos X años)
- Auditoría (saber qué existió)

---

## 4. MERGE / UPSERT - Insertar o Actualizar

### PostgreSQL (ON CONFLICT):
```sql
-- Si el SKU ya existe, actualizar precio. Si no, insertar.
INSERT INTO productos (nombre, precio, sku, categoria_id)
VALUES ('Laptop Pro', 25000, 'SKU-00001', 2)
ON CONFLICT (sku) DO UPDATE
SET precio = EXCLUDED.precio,
    nombre = EXCLUDED.nombre;
-- EXCLUDED se refiere a los valores que intentaste insertar
```

### MySQL (ON DUPLICATE KEY):
```sql
INSERT INTO productos (nombre, precio, sku, categoria_id)
VALUES ('Laptop Pro', 25000, 'SKU-00001', 2)
ON DUPLICATE KEY UPDATE
    precio = VALUES(precio),
    nombre = VALUES(nombre);
```

### Oracle (MERGE):
```sql
MERGE INTO productos dest
USING (SELECT 'Laptop Pro' AS nombre, 25000 AS precio, 'SKU-00001' AS sku FROM dual) src
ON (dest.sku = src.sku)
WHEN MATCHED THEN
    UPDATE SET dest.precio = src.precio, dest.nombre = src.nombre
WHEN NOT MATCHED THEN
    INSERT (nombre, precio, sku) VALUES (src.nombre, src.precio, src.sku);
```

---

## 5. Ejercicios

### Ejercicio 3.1 - INSERT
1. Inserta 3 nuevos clientes VIP de la ciudad de Querétaro
2. Inserta un producto con categoría 'Electrónica' y proveedor 'TechDistributor MX'
3. Inserta una venta con 2 productos en su detalle

### Ejercicio 3.2 - UPDATE
1. Aumenta 15% el salario de empleados con más de 3 años de antigüedad
2. Cambia el estado de todas las ventas 'PENDIENTE' con más de 30 días a 'CANCELADA'
3. Actualiza el stock mínimo a 20 para todos los productos de la categoría 'Electrónica'

### Ejercicio 3.3 - DELETE
1. Elimina (soft delete) los clientes que no han comprado en los últimos 2 años
2. Elimina los registros de auditoría con más de 1 año de antigüedad
3. ¿Por qué NO deberías hacer `DELETE FROM productos WHERE stock = 0`?

### Ejercicio 3.4 - UPSERT
1. Escribe un UPSERT que actualice el precio de un producto si el SKU existe, o lo cree si no
2. ¿En qué escenario real usarías MERGE? Da 3 ejemplos

---

## 6. Errores Comunes

| Error | Causa | Solución |
|-------|-------|----------|
| UPDATE sin WHERE | Actualiza TODOS los registros | SIEMPRE verificar con SELECT primero |
| DELETE sin WHERE | Borra TODOS los registros | Usar transacciones (BEGIN/ROLLBACK) |
| FK violation en DELETE | Otros registros dependen de este | Eliminar dependientes primero o usar CASCADE |
| Unique violation en INSERT | El valor ya existe | Usar ON CONFLICT / ON DUPLICATE KEY |
| NOT NULL violation | Intentas insertar NULL en campo obligatorio | Proporcionar valor o usar DEFAULT |

### Buena práctica: Verificar antes de modificar
```sql
-- PASO 1: Ver qué vas a afectar
SELECT * FROM empleados WHERE puesto = 'Vendedor' AND salario < 30000;
-- "OK, son 15 registros, es lo que espero"

-- PASO 2: Ejecutar el UPDATE
UPDATE empleados SET salario = salario * 1.10
WHERE puesto = 'Vendedor' AND salario < 30000;

-- PASO 3: Verificar resultado
SELECT * FROM empleados WHERE puesto = 'Vendedor' AND salario < 33000;
```

---

## Siguiente Módulo
→ [04-Consultas SQL](../04-consultas/README.md)
