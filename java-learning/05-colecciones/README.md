# Módulo 05: Colecciones (Collections Framework)

## Jerarquía de Colecciones

```
                    Iterable
                       │
                  Collection
                 ╱     │     ╲
              List    Set    Queue
             ╱   ╲     │      │
      ArrayList  LinkedList  HashSet  PriorityQueue
                         TreeSet

                    Map (separado)
                   ╱         ╲
              HashMap      TreeMap
```

## Comparación de Complejidad (Big O)

| Operación | ArrayList | LinkedList | HashMap | TreeMap | HashSet |
|-----------|-----------|------------|---------|---------|---------|
| Acceso por índice | O(1) | O(n) | - | - | - |
| Búsqueda | O(n) | O(n) | O(1) | O(log n) | O(1) |
| Inserción al final | O(1)* | O(1) | O(1) | O(log n) | O(1) |
| Inserción al inicio | O(n) | O(1) | - | - | - |
| Eliminación | O(n) | O(1)** | O(1) | O(log n) | O(1) |

## Cuándo usar cada una

| Necesitas... | Usa |
|-------------|-----|
| Lista ordenada con acceso rápido por posición | ArrayList |
| Insertar/eliminar frecuente al inicio/medio | LinkedList |
| Buscar por clave rápidamente | HashMap |
| Claves ordenadas | TreeMap |
| Elementos únicos sin orden | HashSet |
| Elementos únicos ordenados | TreeSet |
| Cola FIFO | Queue / LinkedList |
| Pila LIFO | Deque / ArrayDeque |

## Ejemplos Prácticos

```java
// ArrayList: lista de productos
List<String> productos = new ArrayList<>();
productos.add("Laptop");
productos.add("Mouse");
productos.get(0);  // "Laptop" - O(1)
productos.remove("Mouse");
productos.contains("Laptop");  // true

// HashMap: catálogo con precio
Map<String, Double> catalogo = new HashMap<>();
catalogo.put("SKU-001", 18999.99);
catalogo.put("SKU-002", 1899.00);
catalogo.get("SKU-001");  // 18999.99 - O(1)
catalogo.containsKey("SKU-003");  // false

// HashSet: emails únicos
Set<String> emails = new HashSet<>();
emails.add("carlos@mail.com");
emails.add("carlos@mail.com");  // No se agrega (duplicado)
emails.size();  // 1

// Iterar con for-each
for (Map.Entry<String, Double> entry : catalogo.entrySet()) {
    System.out.println(entry.getKey() + ": $" + entry.getValue());
}
```

## Ejercicios
1. Implementa un sistema de inventario con HashMap<String, Integer> (sku → stock)
2. Elimina duplicados de una lista usando Set
3. Implementa una cola de atención al cliente (FIFO)
4. Ordena una lista de empleados por salario usando Comparator
5. Implementa un caché LRU con LinkedHashMap
