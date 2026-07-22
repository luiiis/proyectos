# Preguntas de Entrevista - Módulo 01: Fundamentos de BD

## Nivel Junior

### 1. ¿Qué es una base de datos relacional?
**Respuesta:**
Es un sistema que almacena datos en TABLAS (filas y columnas) relacionadas entre sí mediante llaves (PK y FK). "Relacional" viene de la teoría de conjuntos y relaciones matemáticas, no de que las tablas "se relacionan" (aunque también lo hacen).

---

### 2. ¿Cuál es la diferencia entre Primary Key y Foreign Key?
**Respuesta:**
- **PK (Primary Key):** Identificador único de cada fila. No puede repetirse ni ser NULL. Ejemplo: `empleados.id = 1` solo existe una vez.
- **FK (Foreign Key):** Referencia a la PK de OTRA tabla. Crea la relación entre tablas. Ejemplo: `empleados.sucursal_id = 3` apunta a `sucursales.id = 3`.

---

### 3. ¿Cuáles son los tipos de relaciones entre tablas?
**Respuesta:**
- **1:N (uno a muchos):** Un departamento tiene muchos empleados. La más común (90%).
- **M:N (muchos a muchos):** Un estudiante toma muchos cursos, un curso tiene muchos estudiantes. Se implementa con tabla intermedia.
- **1:1 (uno a uno):** Un empleado tiene un expediente. Poco común.

---

### 4. ¿Qué es normalización y para qué sirve?
**Respuesta:**
Organizar datos eliminando redundancia:
- Sin normalizar: nombre del cliente repetido en cada venta (100 ventas = 100 veces "Carlos García")
- Normalizado: cliente en su tabla, venta solo guarda `cliente_id = 1`

Beneficios: menos espacio, sin inconsistencias (cambias el nombre en 1 lugar, no en 100).

---

### 5. ¿Cuál es la diferencia entre SQL y NoSQL?
**Respuesta:**

| SQL (Relacional) | NoSQL (No relacional) |
|------------------|----------------------|
| Tablas con esquema fijo | Documentos flexibles (JSON) |
| ACID (transacciones seguras) | Eventually consistent |
| JOINs entre tablas | Datos desnormalizados |
| PostgreSQL, MySQL, Oracle | MongoDB, Redis, Cassandra |
| Datos estructurados | Datos semi-estructurados |

Regla: usa SQL para transacciones y datos estructurados. NoSQL para flexibilidad y escala horizontal.

---

## Nivel Mid

### 6. ¿Qué es ACID en bases de datos?
**Respuesta:**
Propiedades de las transacciones:
- **A**tomicity: todo o nada (si falla una parte, se deshace todo)
- **C**onsistency: la BD pasa de un estado válido a otro válido
- **I**solation: transacciones concurrentes no se interfieren
- **D**urability: datos commitados no se pierden (ni con crash)

Ejemplo: transferencia bancaria. Restar $1000 de cuenta A + sumar $1000 a cuenta B. Si falla la suma → revertir la resta.

---

### 7. ¿Cuándo usarías PostgreSQL vs MySQL vs Oracle?
**Respuesta:**
- **PostgreSQL:** Aplicaciones complejas, startup moderno, datos geoespaciales, JSON, extensiones. Gratis. Más cumplimiento SQL estándar.
- **MySQL:** Aplicaciones web simples, WordPress, alta velocidad en lecturas simples. Gratis. Comunidad más grande.
- **Oracle:** Banca, gobierno, telecomunicaciones. Extremadamente robusto. Caro. Requiere DBA dedicado.

---

### 8. ¿Qué es un esquema (schema) de base de datos?
**Respuesta:**
Es un namespace lógico dentro de una BD que agrupa tablas, vistas, funciones. Como "carpetas" dentro de la BD:
```sql
public.empleados      -- schema por defecto
ventas.facturas       -- schema específico
auditoria.logs        -- schema de auditoría
```
Se usa para organizar y controlar permisos (un rol puede acceder a un schema pero no a otro).

---

### 9. ¿Qué es un ORM y cómo se relaciona con SQL?
**Respuesta:**
ORM (Object-Relational Mapping) mapea clases de tu lenguaje a tablas SQL:
- Clase `Producto` → tabla `productos`
- Atributo `nombre` → columna `nombre`
- `productRepo.save(p)` → genera `INSERT INTO productos (...) VALUES (...)`

ORMs populares: Hibernate (Java), Sequelize (Node), SQLAlchemy (Python), EF Core (.NET).

Ventaja: escribes menos SQL. Desventaja: puede generar queries ineficientes.

---

### 10. ¿Qué es la integridad referencial?
**Respuesta:**
Garantía de que las FK siempre apuntan a registros que EXISTEN:
- No puedes insertar `venta.cliente_id = 999` si no existe `clientes.id = 999`
- No puedes borrar `clientes.id = 1` si hay ventas que lo referencian

Se implementa con `FOREIGN KEY ... REFERENCES ...` y opciones ON DELETE:
- `CASCADE`: borrar cliente → borrar sus ventas
- `SET NULL`: borrar cliente → ventas.cliente_id = NULL
- `RESTRICT` (default): no permite borrar si hay referencias
