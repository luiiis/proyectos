# Evaluación - Módulos 01 a 05: Fundamentos hasta JOINs

## Instrucciones
- Tiempo: 60 minutos
- No ver soluciones ni documentación
- Usar la base de datos `empresa_db` (Docker)
- Ejecutar cada query y verificar que devuelve resultados

---

## Sección A: Teoría (20 puntos, 2 pts cada una)

1. ¿Qué tipo de relación hay entre `ventas` y `detalle_venta`? ¿Por qué se separan?
2. ¿Cuál es la diferencia entre PRIMARY KEY y UNIQUE?
3. ¿Qué pasa si intentas insertar un registro con un FK que no existe en la tabla padre?
4. ¿Cuándo usarías NUMERIC(10,2) vs INTEGER?
5. Nombra 3 funciones de agregación y explica qué hace cada una.
6. ¿Cuál es la diferencia entre INNER JOIN y LEFT JOIN?
7. ¿Qué es un SELF JOIN? Da un ejemplo de cuándo lo usarías.
8. ¿Por qué DELETE sin WHERE es peligroso?
9. ¿Cuál es el orden REAL de ejecución de SELECT...FROM...WHERE...GROUP BY...ORDER BY?
10. ¿Qué devuelve COUNT(*) vs COUNT(email) si hay registros con email NULL?

---

## Sección B: Práctica SQL (80 puntos)

### Queries básicos (5 pts cada uno)

**B1.** Muestra los 10 productos más caros con su categoría.

**B2.** ¿Cuántos clientes hay por tipo (REGULAR, VIP, MAYORISTA)?

**B3.** Muestra empleados con salario mayor a $40,000 ordenados de mayor a menor.

**B4.** ¿Cuál es el precio promedio de productos por categoría?

**B5.** Muestra las 5 ventas más recientes con nombre del cliente y total.

### Queries intermedios (10 pts cada uno)

**B6.** Muestra el total vendido por cada empleado este año, ordenado de mayor a menor. Incluye nombre completo y sucursal.

**B7.** Lista los productos que NUNCA se han vendido (pista: LEFT JOIN + IS NULL).

**B8.** Para cada sucursal, muestra: nombre, cantidad de empleados, y suma de salarios.

**B9.** Muestra los clientes que han gastado más de $50,000 en total, con la cantidad de compras y el monto total.

**B10.** Muestra los 5 productos más vendidos (por cantidad) con nombre de categoría y proveedor.

### Query avanzado (15 pts)

**B11.** Reporte completo: Para cada venta del último mes, muestra:
- Número de venta
- Fecha
- Nombre completo del cliente + tipo
- Nombre del vendedor + sucursal
- Cantidad de productos diferentes
- Total de la venta

Ordenado por total de mayor a menor.

---

## Rúbrica

| Criterio | Puntos |
|----------|--------|
| Query ejecuta sin errores | 40% |
| Resultado correcto | 40% |
| Uso correcto de JOINs (no subconsultas innecesarias) | 10% |
| Legibilidad (alias, indentación) | 10% |

## Aprobación
- 60+ puntos = Aprobado
- 80+ puntos = Avanzar al siguiente bloque
- 90+ puntos = Nivel para entrevista junior
