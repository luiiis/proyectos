# Preguntas de Entrevista - Tema: Colecciones Java

## Nivel Junior

### 1. ¿Cuál es la diferencia entre ArrayList y LinkedList?
**Respuesta:**

| Operación | ArrayList | LinkedList |
|-----------|-----------|------------|
| Acceso por índice `get(i)` | O(1) rápido | O(n) lento |
| Agregar al final `add()` | O(1) amortizado | O(1) |
| Insertar en medio | O(n) (mueve elementos) | O(1) (cambia punteros) |
| Memoria | Menos (array contiguo) | Más (nodo + punteros) |

**Regla práctica:** Usa ArrayList el 95% del tiempo. LinkedList solo si insertas/eliminas mucho en medio.

---

### 2. ¿Cuál es la diferencia entre HashMap y TreeMap?
**Respuesta:**
- **HashMap**: sin orden, O(1) para get/put. Usa cuando solo necesitas buscar por clave.
- **TreeMap**: ordenado por clave (natural o Comparator), O(log n). Usa cuando necesitas recorrer en orden.

```java
HashMap: {B=2, A=1, C=3}  // orden impredecible
TreeMap: {A=1, B=2, C=3}  // siempre ordenado por clave
```

---

### 3. ¿Qué pasa si metes una clave duplicada en un HashMap?
**Respuesta:**
Sobreescribe el valor anterior. HashMap no permite claves duplicadas (pero sí valores duplicados):
```java
map.put("clave", "valor1");
map.put("clave", "valor2");  // "valor1" se pierde
map.get("clave");  // → "valor2"
```

---

### 4. ¿Qué es `merge()` y para qué sirve?
**Respuesta:**
`merge(clave, valor, funcionSiExiste)`:
- Si la clave NO existe → inserta el valor
- Si la clave SÍ existe → aplica la función con el valor existente y el nuevo

```java
stock.merge("LAP-001", 10, Integer::sum);
// Si no existe: inserta 10
// Si existe con 25: 25 + 10 = 35
```
Es más conciso que `containsKey` + `put`.

---

### 5. ¿Qué es Stream API? ¿Reemplaza a los for loops?
**Respuesta:**
Stream es una forma declarativa de procesar colecciones: dices QUÉ quieres, no CÓMO iterarlo.

```java
// Imperativo (cómo)
List<String> caros = new ArrayList<>();
for (Producto p : productos) {
    if (p.precio() > 10000) {
        caros.add(p.nombre());
    }
}

// Declarativo (qué)
List<String> caros = productos.stream()
    .filter(p -> p.precio() > 10000)
    .map(Producto::nombre)
    .toList();
```

No reemplaza for loops siempre: para operaciones simples o con efectos secundarios, un for puede ser más claro.

---

## Nivel Mid

### 6. ¿Cómo funciona HashMap internamente?
**Respuesta:**
1. Calcula `hashCode()` de la clave
2. El hash determina en qué "bucket" (posición del array interno) se guarda
3. Si dos claves caen en el mismo bucket → colisión → se guardan en lista/árbol
4. Al buscar: calcula hash → va al bucket → busca con `equals()`

Por eso **debes** implementar `hashCode()` y `equals()` correctamente si usas objetos custom como claves.

---

### 7. ¿Cuál es la diferencia entre `map()`, `flatMap()` y `reduce()`?
**Respuesta:**
- `map(T → R)`: transforma cada elemento 1:1
- `flatMap(T → Stream<R>)`: transforma y "aplana" streams anidados
- `reduce(identity, accumulator)`: combina todos los elementos en uno

```java
// map: [1,2,3] → ["1","2","3"]
stream.map(String::valueOf)

// flatMap: [[1,2],[3,4]] → [1,2,3,4]
stream.flatMap(Collection::stream)

// reduce: [1,2,3,4,5] → 15
stream.reduce(0, Integer::sum)
```

---

### 8. ¿Qué es un ConcurrentHashMap y cuándo usarlo?
**Respuesta:**
HashMap NO es thread-safe. Si múltiples hilos lo modifican simultáneamente → datos corruptos.

`ConcurrentHashMap`:
- Thread-safe sin sincronizar todo el mapa (usa segmentos)
- Mejor performance que `Collections.synchronizedMap()`
- Permite lecturas concurrentes sin bloqueo

Usarlo cuando: múltiples hilos acceden al mismo mapa (servidores web, procesamiento paralelo).

---

### 9. ¿Qué diferencia hay entre `Collectors.toList()` y `.toList()`?
**Respuesta:**
- `.toList()` (Java 16+): devuelve lista **inmutable** (no puedes agregar/remover)
- `Collectors.toList()`: devuelve lista **mutable** (ArrayList modificable)
- `Collectors.toUnmodifiableList()` (Java 10+): inmutable explícito

```java
var inmutable = stream.toList();           // UnsupportedOperationException si modificas
var mutable = stream.collect(Collectors.toList());  // puedes .add() después
```

---

### 10. Explica este código: ¿qué resultado da y por qué?
```java
Map<String, Long> resultado = productos.stream()
    .collect(Collectors.groupingBy(
        Producto::categoria,
        Collectors.counting()
    ));
```
**Respuesta:**
Agrupa productos por categoría y cuenta cuántos hay en cada grupo:
```
{"Electrónica": 2, "Periféricos": 2, "Mobiliario": 1, "Almacenamiento": 1}
```
- `groupingBy(classifier)`: crea un Map donde la clave es el resultado del classifier
- `Collectors.counting()`: el "downstream collector" que define qué hacer con cada grupo (en este caso, contar)

---

## Nivel Senior

### 11. ¿Cómo implementarías un caché LRU (Least Recently Used) con colecciones de Java?
**Respuesta:**
Usando `LinkedHashMap` con acceso-ordenado:
```java
Map<String, Object> cache = new LinkedHashMap<>(16, 0.75f, true) {
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > MAX_SIZE;  // eliminar el menos usado cuando se llena
    }
};
```
`accessOrder=true` hace que cada `get()` mueva el elemento al final → el primero siempre es el menos usado.

---

### 12. ¿Cuándo usarías `parallelStream()` y cuándo NO?
**Respuesta:**
**Usar cuando:**
- Colecciones GRANDES (>10,000 elementos)
- Operaciones CPU-intensivas por elemento
- Sin efectos secundarios (no modificar estado compartido)

**NO usar cuando:**
- Colecciones pequeñas (overhead de threading > beneficio)
- Operaciones con I/O (bloquean hilos del ForkJoinPool)
- Orden importa y es costoso mantenerlo
- Hay estado compartido mutable (race conditions)
