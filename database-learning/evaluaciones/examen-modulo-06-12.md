# Evaluación - Módulos 06 a 12: Funciones hasta Transacciones

## Instrucciones
- Tiempo: 90 minutos
- Sin documentación
- Base de datos: `empresa_db`

---

## Sección A: Teoría (20 puntos)

1. ¿Cuál es la diferencia entre una Function y un Stored Procedure?
2. ¿Qué es un trigger BEFORE vs AFTER? ¿Cuándo usarías cada uno?
3. Explica qué es ACID en transacciones con un ejemplo real.
4. ¿Qué es un deadlock y cómo lo evitas?
5. ¿Cuándo usarías una VIEW vs una consulta directa?
6. ¿Qué es un índice y por qué NO poner índice en todo?
7. ¿Cuál es la diferencia entre ROLLBACK y SAVEPOINT?
8. ¿Qué hace COALESCE? ¿Y NULLIF?
9. ¿Qué diferencia hay entre ROW_NUMBER, RANK y DENSE_RANK?
10. ¿Cuándo usarías un CTE recursivo?

---

## Sección B: Práctica (80 puntos)

### B1. Window Functions (10 pts)
Muestra para cada empleado: nombre, salario, salario promedio de su sucursal, y si gana más o menos que el promedio (columna "vs_promedio": 'ARRIBA' o 'ABAJO').

### B2. Crear una Function (10 pts)
Crea una función `fn_calcular_precio_final(precio NUMERIC, tipo_cliente VARCHAR)` que:
- Cliente REGULAR: sin descuento + 16% IVA
- Cliente VIP: 10% descuento + 16% IVA
- Cliente MAYORISTA: 20% descuento + 16% IVA
- Retorne el precio final

### B3. Crear un Trigger (15 pts)
Crea un trigger que, al INSERT en `detalle_venta`:
1. Descuente el stock del producto
2. Si el stock queda < 5, inserte una alerta en tabla `alertas_stock`
3. Si no hay stock suficiente, cancele la operación con un error

### B4. Transacción (10 pts)
Escribe una transacción que:
1. Cree una venta
2. Inserte 3 items en detalle_venta
3. Actualice el total de la venta
4. Si algún producto no tiene stock → ROLLBACK todo
5. Si todo OK → COMMIT

### B5. Subconsulta correlacionada (10 pts)
Muestra productos cuyo precio es mayor que el precio promedio de su MISMA categoría.

### B6. Crear una View (10 pts)
Crea una vista `v_reporte_mensual` que muestre por mes:
- Año y mes
- Cantidad de ventas
- Monto total
- Ticket promedio
- Comparación % vs mes anterior

### B7. Query avanzado con CTE (15 pts)
Usando CTE, crea un reporte que muestre:
- Los 3 mejores vendedores del mes
- Con: nombre, sucursal, total vendido, número de ventas
- Y un ranking (1°, 2°, 3°)
- Solo ventas con estado 'COMPLETADA'

---

## Rúbrica
- 60+ = Aprobado (nivel junior)
- 80+ = Nivel mid (puedes entrar a entrevista)
- 90+ = Nivel senior junior (dominas SQL avanzado)
