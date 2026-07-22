# Preguntas de Entrevista - Módulo 02: DDL

## Nivel Junior

### 1. ¿Cuál es la diferencia entre DROP, TRUNCATE y DELETE?
**Respuesta:**
- `DROP TABLE`: elimina la tabla completa (estructura + datos). Irreversible.
- `TRUNCATE TABLE`: elimina TODOS los datos, mantiene estructura. Irreversible. Rápido.
- `DELETE FROM`: elimina filas selectivamente (con WHERE). Reversible (ROLLBACK). Lento.

---

### 2. ¿Qué tipos de constraints existen?
**Respuesta:**
- `PRIMARY KEY`: identificador único, no null
- `FOREIGN KEY`: referencia a PK de otra tabla
- `NOT NULL`: campo obligatorio
- `UNIQUE`: no permite duplicados (pero permite NULL)
- `CHECK`: condición que debe cumplirse (`CHECK (precio > 0)`)
- `DEFAULT`: valor si no se especifica

---

### 3. ¿Qué diferencia hay entre VARCHAR(100) y TEXT?
**Respuesta:**
- `VARCHAR(100)`: límite máximo de 100 caracteres. El DB valida el largo.
- `TEXT`: sin límite de tamaño. No valida largo.

En PostgreSQL: rendimiento es el mismo. En MySQL: TEXT no puede ser parte de un índice directamente.
Usar VARCHAR cuando conoces el máximo razonable (email: 100, nombre: 50). TEXT para contenido libre (descripciones, comentarios).

---

### 4. ¿Qué es SERIAL en PostgreSQL?
**Respuesta:**
Es un atajo para crear una secuencia auto-incremental:
```sql
id SERIAL PRIMARY KEY
-- Equivale a:
id INTEGER NOT NULL DEFAULT nextval('tabla_id_seq')
```
En MySQL es `AUTO_INCREMENT`. En Oracle es `SEQUENCE` + trigger o `GENERATED ALWAYS AS IDENTITY`.

---

### 5. ¿Cuándo usarías ALTER TABLE vs recrear la tabla?
**Respuesta:**
`ALTER TABLE` cuando:
- Agregar/eliminar columnas
- Cambiar tipos de datos
- Agregar constraints
- La tabla tiene datos que no quieres perder

Recrear cuando:
- Cambios drásticos de estructura
- Tabla vacía o de desarrollo
- Migración de esquema completa

---

## Nivel Mid

### 6. ¿Qué es una migración de base de datos?
**Respuesta:**
Scripts SQL versionados que modifican el esquema de forma controlada:
```
V1__crear_tabla_productos.sql
V2__agregar_campo_imagen.sql  
V3__crear_indice_precio.sql
```
Herramientas: Flyway (Java), Liquibase, Alembic (Python), Prisma Migrate.
Beneficios: versionado en Git, reproducible, rollback, equipo sincronizado.

---

### 7. ¿Cómo agregarías una columna NOT NULL a una tabla con datos?
**Respuesta:**
No puedes directamente (los registros existentes tendrían NULL). Pasos:
```sql
-- 1. Agregar con DEFAULT
ALTER TABLE productos ADD COLUMN imagen VARCHAR(200) DEFAULT 'sin-imagen.jpg';
-- 2. O agregar sin NOT NULL, llenar datos, luego agregar constraint
ALTER TABLE productos ADD COLUMN imagen VARCHAR(200);
UPDATE productos SET imagen = 'default.jpg' WHERE imagen IS NULL;
ALTER TABLE productos ALTER COLUMN imagen SET NOT NULL;
```

---

### 8. ¿Qué es ON DELETE CASCADE y cuándo usarlo?
**Respuesta:**
Cuando borras un registro padre, automáticamente borra los hijos:
```sql
FOREIGN KEY (venta_id) REFERENCES ventas(id) ON DELETE CASCADE
-- Borrar venta → borra automáticamente sus detalle_venta
```
Usarlo en: relaciones padre-hijo fuertes (venta → detalle). NO usarlo en: datos que deben preservarse (cliente → ventas: no borrar historial).

---

### 9. ¿Cuál es la diferencia entre NUMERIC(10,2) y FLOAT?
**Respuesta:**
- `NUMERIC(10,2)` / `DECIMAL`: precisión EXACTA. 10 dígitos total, 2 decimales. Para dinero.
- `FLOAT` / `DOUBLE`: precisión aproximada. Errores de redondeo. Para cálculos científicos.

```sql
-- NUNCA uses FLOAT para dinero:
SELECT 0.1 + 0.2;  -- FLOAT puede dar 0.30000000000000004
-- SIEMPRE usa NUMERIC/DECIMAL para dinero:
SELECT 0.1::NUMERIC + 0.2::NUMERIC;  -- Siempre da 0.3
```

---

### 10. ¿Cómo diseñarías una tabla de auditoría?
**Respuesta:**
```sql
CREATE TABLE auditoria (
    id BIGSERIAL PRIMARY KEY,
    tabla VARCHAR(50) NOT NULL,
    operacion VARCHAR(10) NOT NULL, -- INSERT, UPDATE, DELETE
    registro_id BIGINT,
    datos_antes JSONB,    -- Estado antes del cambio
    datos_despues JSONB,  -- Estado después
    usuario VARCHAR(50),
    ip VARCHAR(45),
    fecha TIMESTAMP DEFAULT NOW()
);
-- Se llena con triggers en cada tabla monitoreada
-- JSONB permite almacenar cualquier estructura sin schema fijo
```
