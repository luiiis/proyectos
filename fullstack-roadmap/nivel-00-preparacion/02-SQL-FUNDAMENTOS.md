# SQL Fundamentos — Lo que necesitas ANTES del Nivel 3

## ¿Qué es SQL?
SQL (Structured Query Language) es el lenguaje para hablar con bases de datos. Toda app profesional guarda datos en una BD. Si no sabes SQL, no puedes trabajar como backend.

---

## 1. Conceptos Básicos

### Base de datos → Tablas → Filas → Columnas

```
Base de datos: tienda_db
├── Tabla: categorias
│   ├── Fila: {id: 1, nombre: "Electrónica"}
│   └── Fila: {id: 2, nombre: "Periféricos"}
└── Tabla: productos
    ├── Fila: {id: 1, nombre: "Laptop HP", precio: 18999, categoria_id: 1}
    ├── Fila: {id: 2, nombre: "Mouse", precio: 299, categoria_id: 2}
    └── Fila: {id: 3, nombre: "Monitor", precio: 5999, categoria_id: 1}
```

### Analogía
| SQL | Excel |
|-----|-------|
| Base de datos | Archivo .xlsx |
| Tabla | Hoja (pestaña) |
| Columna | Encabezado |
| Fila | Una fila de datos |
| Query | Fórmula o filtro |

---

## 2. Crear Base de Datos y Tablas (DDL)

```sql
-- Crear base de datos
CREATE DATABASE tienda_db;

-- Seleccionar BD
USE tienda_db;

-- Crear tabla de categorías
CREATE TABLE categorias (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,  -- ID único, se genera solo
    nombre      VARCHAR(100) NOT NULL UNIQUE,       -- Texto, obligatorio, no repetir
    descripcion VARCHAR(255),                       -- Texto, opcional
    activa      BOOLEAN DEFAULT TRUE,              -- Valor por defecto: true
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Fecha automática
);

-- Crear tabla de productos
CREATE TABLE productos (
    id            BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre        VARCHAR(150) NOT NULL,
    precio        DECIMAL(10,2) NOT NULL,           -- 10 dígitos, 2 decimales
    existencia    INT NOT NULL DEFAULT 0,
    categoria_id  BIGINT,                           -- FK: referencia a categorias
    activo        BOOLEAN DEFAULT TRUE,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Relación: cada producto PERTENECE a una categoría
    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
);
```

### Tipos de datos comunes
| Tipo SQL | Para qué | Ejemplo |
|----------|----------|---------|
| `BIGINT` | IDs, números grandes | id, usuario_id |
| `INT` | Enteros normales | existencia, cantidad |
| `VARCHAR(n)` | Texto de hasta n caracteres | nombre, email |
| `TEXT` | Texto muy largo | descripción larga |
| `DECIMAL(p,s)` | Dinero (p total, s decimales) | precio DECIMAL(10,2) |
| `BOOLEAN` | true/false | activo, completado |
| `TIMESTAMP` | Fecha + hora | created_at |
| `DATE` | Solo fecha | fecha_nacimiento |

### Restricciones
| Restricción | Qué hace | Ejemplo |
|-------------|----------|---------|
| `PRIMARY KEY` | Identificador único de la fila | `id BIGINT PRIMARY KEY` |
| `NOT NULL` | No permite valor vacío | `nombre VARCHAR(100) NOT NULL` |
| `UNIQUE` | No permite valores repetidos | `email VARCHAR(100) UNIQUE` |
| `DEFAULT` | Valor si no se proporciona | `activo BOOLEAN DEFAULT TRUE` |
| `FOREIGN KEY` | Relación con otra tabla | `REFERENCES categorias(id)` |
| `CHECK` | Validación personalizada | `CHECK (precio > 0)` |

---

## 3. Insertar Datos (INSERT)

```sql
-- Insertar una categoría
INSERT INTO categorias (nombre, descripcion) VALUES ('Electrónica', 'Dispositivos electrónicos');

-- Insertar varias a la vez
INSERT INTO categorias (nombre, descripcion) VALUES
('Periféricos', 'Teclados, mouse, webcams'),
('Almacenamiento', 'Discos duros, SSDs'),
('Mobiliario', 'Escritorios y sillas');

-- Insertar productos
INSERT INTO productos (nombre, precio, existencia, categoria_id) VALUES
('Laptop HP', 18999.00, 25, 1),
('Mouse Logitech', 899.00, 50, 2),
('Monitor Dell 27"', 12499.00, 15, 1),
('SSD Samsung 1TB', 2199.00, 30, 3),
('Silla Ergonómica', 8999.00, 8, 4);
```

---

## 4. Consultar Datos (SELECT)

```sql
-- Todos los productos
SELECT * FROM productos;

-- Solo nombre y precio
SELECT nombre, precio FROM productos;

-- Con condición
SELECT * FROM productos WHERE precio > 5000;

-- Múltiples condiciones
SELECT * FROM productos WHERE precio > 1000 AND existencia > 10;
SELECT * FROM productos WHERE categoria_id = 1 OR categoria_id = 2;

-- Buscar texto parcial (LIKE)
SELECT * FROM productos WHERE nombre LIKE '%laptop%';    -- contiene "laptop"
SELECT * FROM productos WHERE nombre LIKE 'M%';         -- empieza con "M"

-- Ordenar resultados
SELECT * FROM productos ORDER BY precio ASC;             -- más barato primero
SELECT * FROM productos ORDER BY precio DESC;            -- más caro primero
SELECT * FROM productos ORDER BY nombre;                 -- alfabético

-- Limitar resultados (paginación)
SELECT * FROM productos LIMIT 10;                       -- primeros 10
SELECT * FROM productos LIMIT 10 OFFSET 20;             -- página 3 (saltar 20)

-- Contar
SELECT COUNT(*) FROM productos;                          -- total de filas
SELECT COUNT(*) FROM productos WHERE activo = TRUE;      -- solo activos

-- Valores únicos
SELECT DISTINCT categoria_id FROM productos;

-- Alias (renombrar columnas en el resultado)
SELECT nombre AS producto, precio AS costo FROM productos;
```

---

## 5. Funciones Agregadas

```sql
-- Contar cuántos productos hay
SELECT COUNT(*) AS total FROM productos;

-- Precio más alto y más bajo
SELECT MAX(precio) AS mas_caro, MIN(precio) AS mas_barato FROM productos;

-- Promedio de precios
SELECT AVG(precio) AS precio_promedio FROM productos;

-- Suma total del inventario (precio × existencia)
SELECT SUM(precio * existencia) AS valor_inventario FROM productos;

-- Agrupar por categoría
SELECT categoria_id, COUNT(*) AS cantidad, AVG(precio) AS precio_promedio
FROM productos
GROUP BY categoria_id;

-- Agrupar y filtrar grupos (HAVING)
SELECT categoria_id, COUNT(*) AS cantidad
FROM productos
GROUP BY categoria_id
HAVING COUNT(*) > 2;   -- Solo categorías con más de 2 productos
```

### Diferencia WHERE vs HAVING
- `WHERE` filtra FILAS (antes de agrupar)
- `HAVING` filtra GRUPOS (después de agrupar)

---

## 6. Actualizar Datos (UPDATE)

```sql
-- Actualizar un producto
UPDATE productos SET precio = 19999.00 WHERE id = 1;

-- Actualizar múltiples campos
UPDATE productos
SET nombre = 'Laptop HP ProBook v2', precio = 19999.00, existencia = 20
WHERE id = 1;

-- Actualizar varios registros
UPDATE productos SET activo = FALSE WHERE existencia = 0;

-- ⚠️ CUIDADO: sin WHERE actualiza TODOS
UPDATE productos SET precio = 0;  -- ❌ PELIGRO: todos los precios se van a 0
```

**Regla de oro**: SIEMPRE incluir WHERE en un UPDATE (o DELETE).

---

## 7. Eliminar Datos (DELETE)

```sql
-- Eliminar un producto
DELETE FROM productos WHERE id = 5;

-- Eliminar varios
DELETE FROM productos WHERE activo = FALSE;

-- ⚠️ NUNCA hacer esto sin WHERE:
DELETE FROM productos;  -- ❌ BORRA TODO

-- En la práctica usamos SOFT DELETE (no borrar, solo desactivar):
UPDATE productos SET activo = FALSE WHERE id = 5;
-- Y siempre filtramos: WHERE activo = TRUE
```

---

## 8. JOIN — Combinar Tablas

```sql
-- INNER JOIN: solo productos que TIENEN categoría
SELECT p.nombre, p.precio, c.nombre AS categoria
FROM productos p
INNER JOIN categorias c ON p.categoria_id = c.id;

-- LEFT JOIN: TODOS los productos (aunque no tengan categoría)
SELECT p.nombre, p.precio, c.nombre AS categoria
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id;

-- Resultado del LEFT JOIN:
-- Laptop HP     | 18999 | Electrónica
-- Mouse         | 899   | Periféricos
-- Producto X    | 500   | NULL          ← no tiene categoría, pero aparece
```

### ¿Cuándo usar cada JOIN?
| JOIN | Qué devuelve | Uso |
|------|-------------|-----|
| `INNER JOIN` | Solo filas que coinciden en AMBAS tablas | Cuando la relación es obligatoria |
| `LEFT JOIN` | Todas las de la tabla izquierda + coincidencias | Cuando la relación es opcional |
| `RIGHT JOIN` | Todas las de la tabla derecha + coincidencias | Poco usado |

### Ejemplo práctico (el que usamos en el Nivel 3)
```sql
-- Listar productos con nombre de su categoría
SELECT p.*,
       c.nombre AS categoria_nombre
FROM productos p
LEFT JOIN categorias c ON p.categoria_id = c.id
WHERE p.activo = TRUE
ORDER BY p.nombre;
```

---

## 9. Subconsultas

```sql
-- Productos más caros que el promedio
SELECT * FROM productos
WHERE precio > (SELECT AVG(precio) FROM productos);

-- Categorías que tienen al menos un producto
SELECT * FROM categorias
WHERE id IN (SELECT DISTINCT categoria_id FROM productos);

-- Producto más caro de cada categoría
SELECT * FROM productos p
WHERE precio = (
    SELECT MAX(precio) FROM productos WHERE categoria_id = p.categoria_id
);
```

---

## 10. Claves Primarias y Foráneas

```sql
-- Clave Primaria (PK): identifica UNICAMENTE cada fila
-- Cada tabla debe tener UNA
id BIGINT AUTO_INCREMENT PRIMARY KEY

-- Clave Foránea (FK): conecta con otra tabla
-- "Este producto PERTENECE a esta categoría"
categoria_id BIGINT,
FOREIGN KEY (categoria_id) REFERENCES categorias(id)
```

### Relaciones
```
categorias (1) ←────→ (N) productos
"Una categoría tiene MUCHOS productos"
"Un producto pertenece a UNA categoría"

usuarios (N) ←────→ (N) roles    (tabla intermedia: usuarios_roles)
"Un usuario puede tener MUCHOS roles"
"Un rol puede pertenecer a MUCHOS usuarios"
```

---

## 11. Índices (Performance)

```sql
-- Los índices hacen las búsquedas RÁPIDAS
-- Ponlos en columnas que usas mucho en WHERE o JOIN

CREATE INDEX idx_productos_nombre ON productos(nombre);
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_productos_activo ON productos(activo);

-- ¿Cuándo crear un índice?
-- ✅ Columnas usadas en WHERE frecuentemente
-- ✅ Columnas usadas en JOIN (foreign keys)
-- ✅ Columnas usadas en ORDER BY
-- ❌ Columnas que se actualizan TODO el tiempo
-- ❌ Tablas pequeñas (menos de 1000 filas)
```

---

## 12. Transacciones

```sql
-- Una transacción agrupa operaciones: o se ejecutan TODAS o NINGUNA

START TRANSACTION;

-- Registrar una venta (2 operaciones que DEBEN ir juntas)
INSERT INTO ventas (cliente_id, total) VALUES (1, 5000);
UPDATE productos SET existencia = existencia - 1 WHERE id = 3;

-- Si todo salió bien:
COMMIT;

-- Si algo falló:
ROLLBACK;  -- Deshace TODO lo anterior
```

### ¿Por qué importa?
Imagina que insertas la venta pero falla al actualizar el stock. Sin transacción: la venta se registra pero el stock no baja. Con transacción: se deshace todo y queda consistente.

---

## Ejercicios para practicar ANTES del Nivel 3

### Ejercicio 1: Crear tablas
Crea una BD `biblioteca_db` con tablas: `autores` (id, nombre, pais) y `libros` (id, titulo, autor_id, año, disponible).

### Ejercicio 2: Insertar datos
Agrega 3 autores y 5 libros.

### Ejercicio 3: Consultas
- Todos los libros de un autor específico (JOIN)
- Libros publicados después del año 2000
- Cantidad de libros por autor (GROUP BY)
- El autor con más libros

### Ejercicio 4: Actualizar y eliminar
- Marcar un libro como no disponible
- Cambiar el nombre de un autor
- Eliminar libros no disponibles

---

## Siguiente paso
Cuando domines SELECT, INSERT, UPDATE, DELETE, JOIN y GROUP BY → ve al **Nivel 3: Productos con BD** con confianza.
