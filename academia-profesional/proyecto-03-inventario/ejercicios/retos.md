# Ejercicios y Retos - Proyecto 03: Sistema Inventario

## Reto 1: Búsqueda por nombre
Agrega un método `buscarPorNombre(String termino)` que encuentre productos cuyo nombre contenga el término (case-insensitive).

**Pista:**
```java
productos.values().stream()
    .filter(p -> p.nombre().toLowerCase().contains(termino.toLowerCase()))
    .toList();
```

**Lo que practicas:** Stream API, filter, String manipulation

---

## Reto 2: Top 3 productos más caros
Agrega un método `topProductosPrecio(int cantidad)` que muestre los N productos más caros.

**Pista:**
```java
.sorted(Comparator.comparingDouble(Producto::precio).reversed())
.limit(cantidad)
```

**Lo que practicas:** Comparator, sorted, limit

---

## Reto 3: Reporte por rango de fechas
Agrega un método `movimientosEntre(LocalDateTime desde, LocalDateTime hasta)` que filtre movimientos por rango de fechas.

**Lo que practicas:** java.time, comparación de fechas con isAfter/isBefore

---

## Reto 4: Producto más movido
Crea un método que identifique el producto con más movimientos (entradas + salidas combinadas).

**Pista:**
```java
movimientos.stream()
    .collect(Collectors.groupingBy(Movimiento::sku, Collectors.counting()))
    // luego encontrar el máximo
```

**Lo que practicas:** groupingBy, counting, max con Comparator

---

## Reto 5: Deshacer última salida
Implementa un método `deshacerUltimaSalida()` que:
1. Encuentre el último movimiento de tipo SALIDA
2. Devuelva las unidades al stock
3. Registre un nuevo movimiento de tipo "CORRECCION"

**Pista:** Puedes acceder al último elemento de un ArrayList con `list.get(list.size() - 1)` o usar `ListIterator` en reversa.

**Lo que practicas:** Manipulación de listas, lógica de negocio

---

## Reto 6: Alertas configurables
Crea un `Map<String, Integer> stockMinimo` donde cada producto tenga su propio umbral de alerta. Modifica `reporteStockBajo()` para usar este mapa personalizado en vez de un mínimo global.

**Lo que practicas:** HashMap adicional, comparaciones cruzadas entre mapas

---

## Reto 7: LinkedHashMap para orden de inserción
Cambia `productos` de `HashMap` a `LinkedHashMap`. ¿Qué diferencia notas en el reporte? ¿Cuándo preferirías uno sobre otro?

**Lo que practicas:** Diferencias prácticas entre implementaciones de Map

---

## Reto 8: Exportar movimientos a formato tabla
Crea un método `exportarMovimientos()` que imprima todos los movimientos en formato tabla con columnas alineadas:
```
SKU         TIPO      CANTIDAD  FECHA                MOTIVO
LAP-001     ENTRADA   +25       2026-07-21 10:30:00  Compra proveedor HP
LAP-001     SALIDA    -3        2026-07-21 10:31:00  Venta #V-001
```

**Pista:** Usa `String.format("%-12s %-9s %+4d  %-20s %s", ...)`

**Lo que practicas:** Formateo de strings, iteración

---

## Reto 9 (Avanzado): PriorityQueue para pedidos pendientes
Cuando un producto tiene stock 0 y alguien intenta hacer una salida:
1. En vez de rechazar, agregar a una `PriorityQueue<Pedido>` (ordenada por prioridad/fecha)
2. Cuando llega una entrada, atender automáticamente los pedidos pendientes en orden

**Lo que practicas:** PriorityQueue, Comparable, lógica compleja

---

## Reto 10 (Avanzado): Inmutabilidad con Collections.unmodifiableList
Modifica los métodos de reporte para que devuelvan listas inmutables:
```java
public List<Producto> productosStockBajo() {
    return Collections.unmodifiableList(
        stock.entrySet().stream()
            .filter(e -> e.getValue() < minimo)
            .map(e -> productos.get(e.getKey()))
            .toList()
    );
}
```
Intenta modificar la lista devuelta y observa qué excepción obtienes.

**Lo que practicas:** Inmutabilidad, defensive copies, seguridad en APIs
