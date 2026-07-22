# Evaluación Final - Módulos 13 a 21: Seguridad, Admin, Optimización, DBA

## Instrucciones
- Tiempo: 120 minutos
- Sin documentación
- Base de datos: `empresa_db`
- Este es el examen FINAL. Aprobarlo significa nivel DBA Junior / Backend Mid.

---

## Sección A: Teoría (30 puntos, 3 pts cada una)

1. ¿Qué es Row Level Security y cuándo lo usarías?
2. Explica la diferencia entre VACUUM y VACUUM FULL.
3. ¿Qué es un deadlock? ¿Cómo lo evitas y cómo lo detecta PostgreSQL?
4. Nombra 3 tipos de índices en PostgreSQL y cuándo usar cada uno.
5. ¿Qué es una Materialized View? ¿Ventajas y desventajas vs View normal?
6. Explica qué muestra EXPLAIN ANALYZE y qué buscarías en el output.
7. ¿Cuál es la diferencia entre replicación síncrona y asíncrona?
8. ¿Qué es connection pooling y por qué es necesario?
9. ¿Cómo implementarías auditoría completa en una tabla?
10. ¿Qué es particionamiento y cuándo es necesario?

---

## Sección B: Práctica SQL (70 puntos)

### B1. Seguridad (10 pts)
Crea un sistema de permisos para un equipo de 3 personas:
- `analista_datos`: solo SELECT en todas las tablas
- `vendedor`: SELECT en todo + INSERT en ventas y detalle_venta
- `gerente`: todo excepto DROP y TRUNCATE

Incluye: CREATE ROLE, GRANT, REVOKE.

### B2. Trigger de auditoría (15 pts)
Crea un trigger que registre en `historial_precios`:
- Cada vez que se modifica el precio de un producto
- Guardar: producto_id, precio_anterior, precio_nuevo, porcentaje_cambio, usuario, fecha
- Si el aumento es mayor al 50%, lanzar WARNING

### B3. Stored Procedure (15 pts)
Crea un procedure `sp_cierre_mensual(p_mes DATE)` que:
1. Calcule el total de ventas del mes
2. Calcule comisiones (5% para cada vendedor sobre sus ventas)
3. Inserte los resultados en una tabla `comisiones_mensuales`
4. Si ya existe un cierre para ese mes, lanzar error
5. Todo en una transacción

### B4. Optimización (15 pts)
Dada esta query lenta:
```sql
SELECT c.nombre, c.apellido,
       (SELECT COUNT(*) FROM ventas WHERE cliente_id = c.id) AS compras,
       (SELECT SUM(total) FROM ventas WHERE cliente_id = c.id) AS total
FROM clientes c
WHERE (SELECT MAX(fecha) FROM ventas WHERE cliente_id = c.id) > '2026-01-01'
ORDER BY total DESC;
```

1. Explica por qué es lenta (identifica los problemas)
2. Reescríbela optimizada (JOIN o CTE)
3. ¿Qué índices crearías para acelerar la query optimizada?

### B5. Administración (15 pts)
Escribe queries para un "health check" completo:
1. Tamaño de las 5 tablas más grandes
2. Índices que no se han usado nunca
3. Tablas con más de 10% de filas muertas (necesitan VACUUM)
4. Queries activas que llevan más de 30 segundos
5. Cache hit ratio (debe ser >99%)

---

## Criterios de Evaluación

| Criterio | Peso |
|----------|------|
| Sintaxis correcta (ejecuta sin error) | 30% |
| Resultado correcto y completo | 30% |
| Seguridad (no vulnerabilidades) | 15% |
| Performance (no ineficiencias obvias) | 15% |
| Buenas prácticas (nombres, comentarios) | 10% |

## Aprobación
- 50+ = Aprobado (necesitas práctica)
- 65+ = Nivel DBA Junior (puedes administrar BD en desarrollo)
- 80+ = Nivel Backend Mid (puedes diseñar esquemas y optimizar)
- 90+ = Nivel Senior (puedes ser responsable de BD en producción)
