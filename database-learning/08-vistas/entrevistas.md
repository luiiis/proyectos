# Preguntas de Entrevista - Módulo 08: Vistas

## Nivel Junior

### 1. ¿Qué es una Vista (VIEW)?
**Respuesta:**
Una query guardada con nombre que se usa como si fuera una tabla:
```sql
CREATE VIEW v_productos_activos AS
SELECT nombre, precio, stock FROM productos WHERE activo = true;

-- Usar:
SELECT * FROM v_productos_activos WHERE precio > 5000;
```
No almacena datos, se ejecuta cada vez que la consultas.

### 2. ¿Cuál es la diferencia entre VIEW y Materialized VIEW?
**Respuesta:**
- **VIEW:** query que se ejecuta en cada consulta. Siempre datos frescos. Puede ser lenta si la query es compleja.
- **Materialized VIEW:** resultado pre-calculado almacenado en disco. Muy rápida. Pero datos pueden estar desactualizados hasta que hagas `REFRESH`.

```sql
CREATE MATERIALIZED VIEW mv_ventas_resumen AS SELECT ... ;
REFRESH MATERIALIZED VIEW mv_ventas_resumen;  -- Actualizar manualmente
```

### 3. ¿Se puede hacer INSERT/UPDATE en una VIEW?
**Respuesta:**
En views SIMPLES (una tabla, sin GROUP BY, sin funciones): SÍ.
En views complejas (JOINs, agregaciones): NO directamente. Se puede con `INSTEAD OF` trigger.

### 4. ¿Para qué sirven las vistas en seguridad?
**Respuesta:**
Para limitar qué datos ve un usuario sin darle acceso directo a la tabla:
```sql
-- Vista que oculta salario:
CREATE VIEW v_empleados_publico AS
SELECT nombre, apellido, puesto, sucursal_id FROM empleados;

-- Dar acceso solo a la vista:
GRANT SELECT ON v_empleados_publico TO usuario_consulta;
-- El usuario NO puede ver salarios, emails, etc.
```

### 5. ¿Cuándo usar Materialized View?
**Respuesta:**
- Reportes pesados que se ejecutan frecuentemente
- Datos que no cambian cada segundo (refrescar cada hora/día)
- Dashboards con KPIs agregados
- Resultados de JOINs complejos entre muchas tablas
- NO usar para datos que necesitan ser en tiempo real
