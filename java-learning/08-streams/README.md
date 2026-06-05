# Módulo 08: Stream API

## ¿Qué es un Stream?
Pipeline de operaciones sobre colecciones. Declarativo (QUÉ quieres) vs imperativo (CÓMO hacerlo).

```java
List<Producto> productos = obtenerProductos();

// IMPERATIVO (cómo): 10 líneas
List<String> resultado = new ArrayList<>();
for (Producto p : productos) {
    if (p.getPrecio() > 1000 && p.isActivo()) {
        resultado.add(p.getNombre().toUpperCase());
    }
}
Collections.sort(resultado);

// DECLARATIVO (qué): 5 líneas, más legible
List<String> resultado = productos.stream()
    .filter(p -> p.getPrecio() > 1000)
    .filter(Producto::isActivo)
    .map(p -> p.getNombre().toUpperCase())
    .sorted()
    .toList();
```

## Operaciones Principales

```java
// filter: filtrar elementos
productos.stream().filter(p -> p.getStock() > 0)

// map: transformar cada elemento
productos.stream().map(Producto::getNombre)  // Stream<String>
productos.stream().map(p -> p.getPrecio() * 1.16)  // Stream<Double>

// flatMap: aplanar listas anidadas
pedidos.stream().flatMap(p -> p.getDetalles().stream())

// reduce: combinar todos en uno
double total = productos.stream()
    .mapToDouble(Producto::getPrecio)
    .reduce(0.0, Double::sum);
// O más simple:
double total = productos.stream().mapToDouble(Producto::getPrecio).sum();

// collect: recolectar en una estructura
Map<String, List<Producto>> porCategoria = productos.stream()
    .collect(Collectors.groupingBy(Producto::getCategoria));

Map<String, Double> promediosPorCategoria = productos.stream()
    .collect(Collectors.groupingBy(
        Producto::getCategoria,
        Collectors.averagingDouble(Producto::getPrecio)
    ));

// Otros: distinct, sorted, limit, skip, peek, count, anyMatch, allMatch, findFirst
```

## Ejercicios
1. Filtra productos con precio > 5000 y stock > 0, ordena por precio DESC
2. Agrupa ventas por mes y calcula el total de cada mes
3. Encuentra el producto más caro de cada categoría
4. Calcula el promedio de salarios por departamento
5. Genera un reporte: top 5 clientes por monto total de compras
