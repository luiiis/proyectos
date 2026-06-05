# Evaluaciones y Exámenes

## Examen Módulos 1-5 (Fundamentos + SQL Básico)

### Pregunta 1 (Teoría)
¿Cuál es la diferencia entre PRIMARY KEY y UNIQUE? ¿Puede una tabla tener múltiples de cada uno?

### Pregunta 2 (DDL)
Escribe el CREATE TABLE para un sistema de reservaciones de hotel con:
- Habitaciones (número, tipo, precio por noche, piso, estado)
- Huéspedes (nombre, documento, teléfono, email)
- Reservaciones (huésped, habitación, fecha entrada, fecha salida, total)
Incluye PK, FK, CHECK constraints apropiados.

### Pregunta 3 (DML)
Dado que tienes la tabla `productos` con 500 registros:
1. Inserta un producto nuevo con categoría 'Electrónica'
2. Aumenta 20% el precio de todos los productos con stock < 5
3. Elimina (soft delete) productos sin ventas en los últimos 6 meses

### Pregunta 4 (SELECT)
Escribe queries para:
1. Los 5 productos más caros de cada categoría
2. Clientes que han comprado más de 3 veces este mes
3. Promedio de venta por día de la semana

### Pregunta 5 (JOINs)
Escribe UNA query que muestre:
- Nombre del producto, categoría, proveedor, stock total (todas las sucursales), y cantidad vendida total

---

## Examen Módulos 6-10 (Intermedio)

### Pregunta 1 (Funciones)
Usando Window Functions, escribe una query que muestre:
- Cada venta con su total
- El total acumulado del mes
- La diferencia con la venta anterior
- El porcentaje que representa del total mensual

### Pregunta 2 (Subconsultas)
Sin usar JOINs, encuentra:
1. Productos cuyo precio es mayor al promedio de su categoría
2. Clientes que han comprado en TODAS las sucursales
3. El segundo empleado mejor pagado de cada sucursal

### Pregunta 3 (Vistas)
Diseña un sistema de vistas para un dashboard gerencial que muestre:
- Ventas del día/semana/mes
- Top 10 productos
- Alertas de inventario
- Performance de vendedores

### Pregunta 4 (Índices)
Dada esta query lenta:
```sql
SELECT c.nombre, SUM(v.total) FROM clientes c
JOIN ventas v ON v.cliente_id = c.id
WHERE v.fecha BETWEEN '2024-01-01' AND '2024-12-31'
AND c.ciudad = 'Monterrey'
GROUP BY c.nombre ORDER BY SUM(v.total) DESC;
```
1. ¿Qué índices crearías?
2. Muestra el EXPLAIN antes y después
3. ¿Qué tipo de JOIN esperas ver?

### Pregunta 5 (Triggers)
Implementa un sistema de auditoría completo que:
1. Registre INSERT/UPDATE/DELETE en tabla productos
2. Guarde datos antes y después del cambio
3. Registre el usuario y timestamp
4. Impida eliminar productos con ventas asociadas

---

## Examen Módulos 11-15 (Avanzado)

### Pregunta 1 (Procedimientos)
Crea un stored procedure `sp_cierre_mensual` que:
1. Calcule ventas totales del mes por sucursal
2. Calcule comisiones (5% para vendedores, 3% para gerentes)
3. Genere un registro en tabla `cierres_mensuales`
4. Maneje errores y haga rollback si algo falla

### Pregunta 2 (Transacciones)
Explica qué problema ocurre en este escenario y cómo resolverlo:
- Usuario A lee stock = 5
- Usuario B lee stock = 5
- Usuario A vende 3 (stock = 2)
- Usuario B vende 4 (stock = 1... ¡pero debería fallar!)

### Pregunta 3 (Seguridad)
Diseña un esquema de seguridad para:
- 5 roles con permisos diferentes
- Row Level Security (cada sucursal solo ve sus datos)
- Auditoría de accesos
- Política de passwords

### Pregunta 4 (Optimización)
Tienes una tabla con 50 millones de registros de logs. Las queries tardan 30+ segundos.
1. ¿Qué estrategia de particionamiento usarías?
2. ¿Qué índices?
3. ¿Archivarías datos antiguos? ¿Cómo?
4. ¿Usarías materialized views? ¿Para qué?

### Pregunta 5 (Administración)
Diseña un plan de:
1. Backup (frecuencia, tipo, retención)
2. Monitoreo (qué métricas, qué alertas)
3. Mantenimiento (VACUUM, REINDEX, estadísticas)
4. Disaster Recovery (RTO, RPO)

---

## Retos Tipo Entrevista Técnica

### Reto 1: Diseño de Schema
"Diseña la base de datos para un sistema de delivery (tipo Uber Eats). Incluye: restaurantes, menús, pedidos, repartidores, tracking en tiempo real, calificaciones."

### Reto 2: Query Compleja
"Escribe una query que identifique clientes en riesgo de churn (no han comprado en 60+ días pero antes compraban al menos 1 vez al mes)."

### Reto 3: Optimización
"Esta query tarda 45 segundos en producción. Optimízala sin cambiar la lógica de negocio."
```sql
SELECT * FROM ventas v
WHERE EXTRACT(YEAR FROM v.fecha) = 2024
AND v.empleado_id IN (SELECT id FROM empleados WHERE sucursal_id = 1)
AND v.total > (SELECT AVG(total) FROM ventas)
ORDER BY v.fecha DESC;
```

### Reto 4: Concurrencia
"Dos cajeros intentan vender el último producto al mismo tiempo. ¿Cómo garantizas que solo uno lo logre sin degradar performance?"

### Reto 5: Migración
"Necesitas migrar 100GB de datos de Oracle a PostgreSQL con zero downtime. ¿Cuál es tu estrategia?"
