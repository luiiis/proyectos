# Preguntas de Entrevista - Tema: SQL y Bases de Datos

## Nivel Junior

### 1. ¿Cuál es la diferencia entre WHERE y HAVING?
**Respuesta:**
- `WHERE`: filtra FILAS antes del GROUP BY
- `HAVING`: filtra GRUPOS después del GROUP BY

```sql
-- WHERE: solo ventas > $1000
SELECT * FROM ventas WHERE total > 1000;

-- HAVING: solo categorías con más de 5 productos
SELECT categoria, COUNT(*) FROM productos 
GROUP BY categoria 
HAVING COUNT(*) > 5;
```

---

### 2. ¿Qué tipos de JOIN existen?
**Respuesta:**
- `INNER JOIN`: solo filas que coinciden en ambas tablas
- `LEFT JOIN`: todas las filas de la izquierda + coincidencias de la derecha (NULL si no hay)
- `RIGHT JOIN`: todas las de la derecha + coincidencias de la izquierda
- `FULL JOIN`: todas las filas de ambas tablas (NULL donde no hay match)
- `CROSS JOIN`: producto cartesiano (cada fila con cada fila)

El más usado: LEFT JOIN (para no perder datos del lado principal).

---

### 3. ¿Qué es una Primary Key y una Foreign Key?
**Respuesta:**
- **Primary Key (PK)**: identificador único de cada fila. No puede ser NULL ni repetirse.
- **Foreign Key (FK)**: referencia a la PK de otra tabla. Garantiza integridad referencial.

```sql
ventas.cliente_id → clientes.id  (FK apunta a PK)
-- No puedes insertar una venta con cliente_id=999 si no existe cliente con id=999
```

---

### 4. ¿Qué es normalización? ¿Para qué sirve?
**Respuesta:**
Organizar datos para eliminar redundancia:
- **1FN**: cada campo es atómico (no listas ni campos repetidos)
- **2FN**: cada campo depende de TODA la PK (no de parte)
- **3FN**: ningún campo depende de otro campo no-clave (dependencia transitiva)

Beneficio: menos espacio, menos inconsistencias, más fácil de modificar.

---

### 5. ¿Cuál es la diferencia entre DELETE, TRUNCATE y DROP?
**Respuesta:**
- `DELETE FROM tabla WHERE...`: borra filas específicas. Se puede revertir (transacción). Dispara triggers.
- `TRUNCATE TABLE tabla`: borra TODAS las filas. Más rápido. No dispara triggers. Resetea secuencias.
- `DROP TABLE tabla`: elimina la tabla completa (estructura + datos). Irreversible.

---

## Nivel Mid

### 6. ¿Qué es un índice y cuándo usarlo?
**Respuesta:**
Un índice es una estructura (B-Tree generalmente) que acelera búsquedas:
- Sin índice: recorre TODA la tabla (sequential scan) → O(n)
- Con índice: va directo a la fila → O(log n)

**Crear índice cuando:**
- Columnas usadas frecuentemente en WHERE, JOIN, ORDER BY
- Tablas grandes (>10,000 filas)

**NO crear cuando:**
- Tablas pequeñas (overhead > beneficio)
- Columnas con poca cardinalidad (ej: boolean con solo true/false)
- Tablas con muchos INSERT/UPDATE (el índice se actualiza en cada cambio)

---

### 7. ¿Qué es una transacción y qué significa ACID?
**Respuesta:**
Transacción = grupo de operaciones que se ejecutan como una unidad.
- **A**tomicity: todas se ejecutan o ninguna
- **C**onsistency: la BD queda en estado válido
- **I**solation: transacciones concurrentes no se interfieren
- **D**urability: datos commitados se persisten (incluso si hay crash)

Ejemplo: transferencia bancaria = restar de cuenta A + sumar a cuenta B. Si falla la suma, se deshace la resta.

---

### 8. ¿Qué es un Trigger? ¿Cuándo usarlo y cuándo evitarlo?
**Respuesta:**
Código que se ejecuta automáticamente ante un evento (INSERT, UPDATE, DELETE).

**Usarlo para:** auditoría, actualizar campos calculados, validaciones complejas.
**Evitarlo cuando:** lógica de negocio compleja (difícil de debuguear), performance crítica (se ejecuta en CADA operación), lógica que debería estar en la aplicación.

---

### 9. ¿Cuál es la diferencia entre una View y una Materialized View?
**Respuesta:**
- **View**: query guardada que se ejecuta en cada consulta (siempre datos frescos, puede ser lenta)
- **Materialized View**: resultado pre-calculado almacenado (muy rápida, pero datos pueden estar desactualizados)

```sql
-- Materialized View: se ejecuta una vez y se almacena
CREATE MATERIALIZED VIEW mv_resumen AS SELECT ... ;
REFRESH MATERIALIZED VIEW mv_resumen;  -- Actualizar manualmente
```

Usar Materialized View para reportes pesados que no necesitan datos al segundo.

---

### 10. Explica los niveles de aislamiento de transacciones
**Respuesta:**
De menos a más estricto:
1. **READ UNCOMMITTED**: puede leer datos no commitados (dirty read)
2. **READ COMMITTED** (default en PostgreSQL): solo lee datos commitados
3. **REPEATABLE READ**: las lecturas dentro de una transacción son consistentes
4. **SERIALIZABLE**: transacciones se ejecutan como si fueran secuenciales

Mayor aislamiento = más seguro pero más lento (más bloqueos).

---

## Nivel Senior

### 11. ¿Cómo optimizarías una query que tarda 30 segundos?
**Respuesta (paso a paso):**
1. `EXPLAIN ANALYZE` → ver el plan de ejecución (qué escanea, cuánto tarda cada paso)
2. Buscar Sequential Scans en tablas grandes → crear índices
3. Verificar JOINs: ¿están usando índices en las FK?
4. Reducir datos: ¿puedes filtrar antes del JOIN?
5. Considerar: paginación, materialized views, desnormalización selectiva
6. Verificar estadísticas: `ANALYZE tabla;` (actualiza estadísticas del planner)

---

### 12. ¿Cuándo desnormalizarías una tabla?
**Respuesta:**
Cuando la normalización causa JOINs excesivos en queries frecuentes:
- Reportes que unen 8+ tablas y se ejecutan miles de veces/día
- Datos que casi nunca cambian (ej: guardar nombre_cliente en la venta además del FK)
- Tablas de analítica/data warehouse (star schema, snowflake)

La desnormalización sacrifica espacio y consistencia por velocidad de lectura.
