# Entrevista Técnica Java - Nivel Junior (Java Core)

## 1. ¿Cuál es la diferencia entre JDK, JRE y JVM?
**Respuesta:**
- **JVM** (Java Virtual Machine): ejecuta bytecode. Es lo que hace Java "portable".
- **JRE** (Java Runtime Environment): JVM + librerías estándar. Para EJECUTAR apps.
- **JDK** (Java Development Kit): JRE + compilador + herramientas. Para DESARROLLAR.

```
JDK = JRE + javac + herramientas dev
JRE = JVM + librerías (java.util, java.io, etc.)
JVM = máquina virtual que ejecuta .class
```

---

## 2. ¿Cuál es la diferencia entre `==` y `.equals()`?
**Respuesta:**
- `==` compara **referencias** (¿son el mismo objeto en memoria?)
- `.equals()` compara **contenido** (¿tienen el mismo valor?)

```java
String a = new String("hola");
String b = new String("hola");
a == b;       // false (son objetos distintos en heap)
a.equals(b);  // true (mismo contenido)

// Con String pool:
String c = "hola";
String d = "hola";
c == d;       // true (String pool reutiliza la misma referencia)
```

---

## 3. ¿Qué es el String Pool?
**Respuesta:**
Área especial de memoria (heap) donde Java almacena literals de String. Si dos variables tienen el mismo texto literal, apuntan al MISMO objeto (ahorra memoria).

```java
String a = "Java";  // Va al pool
String b = "Java";  // Reutiliza del pool
String c = new String("Java");  // NUEVO objeto (no usa pool)

a == b;  // true (mismo objeto del pool)
a == c;  // false (c es un objeto nuevo)
c.intern();  // Fuerza a entrar al pool
```

---

## 4. ¿Qué son los tipos primitivos vs wrapper classes?
**Respuesta:**

| Primitivo | Wrapper | Default | Puede ser null |
|-----------|---------|---------|----------------|
| int | Integer | 0 | ❌ primitivo / ✅ wrapper |
| double | Double | 0.0 | ❌ / ✅ |
| boolean | Boolean | false | ❌ / ✅ |
| char | Character | '\u0000' | ❌ / ✅ |

Usar **wrapper** cuando: colecciones genéricas (`List<Integer>`), necesitas null, APIs.
Usar **primitivo** cuando: rendimiento importa, no necesitas null.

Autoboxing: `Integer x = 5;` (Java convierte automáticamente).

---

## 5. ¿Qué es inmutabilidad? ¿Por qué String es inmutable?
**Respuesta:**
Inmutable = una vez creado, NO se puede modificar. Cada operación crea un NUEVO objeto.

```java
String s = "Hola";
s.concat(" Mundo");  // Crea nuevo String, s sigue siendo "Hola"
s = s.concat(" Mundo");  // Ahora s apunta al nuevo "Hola Mundo"
```

String es inmutable por: seguridad (no se puede modificar un hash/password ya creado), String Pool funciona, thread-safe sin sincronización.

Para strings mutables: `StringBuilder` (no thread-safe, rápido) o `StringBuffer` (thread-safe, lento).

---

## 6. ¿Cuáles son los 4 pilares de la POO?
**Respuesta:**
1. **Abstracción**: representar solo lo esencial. `class Vehiculo { void acelerar(); }` sin saber si es auto o moto.
2. **Encapsulamiento**: ocultar datos internos. Campos `private` + getters/setters públicos.
3. **Herencia**: clase hijo hereda de padre. `class Perro extends Animal`.
4. **Polimorfismo**: tratar objetos diferentes de forma genérica. `Animal a = new Perro(); a.hacerSonido();`

---

## 7. ¿Cuál es la diferencia entre abstract class e interface?
**Respuesta:**

| Característica | Abstract Class | Interface |
|---------------|---------------|-----------|
| Herencia | Solo 1 (extends) | Múltiples (implements) |
| Campos | Sí (cualquier tipo) | Solo constantes (static final) |
| Constructor | Sí | No |
| Métodos | Abstractos + concretos | Abstractos + default (Java 8+) |
| Cuándo | "Es un tipo de" | "Puede hacer" |

```java
abstract class Animal { }      // "Perro ES UN Animal"
interface Nadador { void nadar(); }  // "Perro PUEDE nadar"
```

---

## 8. ¿Qué es el Garbage Collector?
**Respuesta:**
Proceso automático de la JVM que libera memoria de objetos que ya nadie referencia.

```java
Producto p = new Producto("Laptop");  // Objeto en heap
p = null;  // Ya nadie referencia al objeto → elegible para GC
// El GC lo eliminará cuando necesite memoria
```

No puedes forzar el GC (`System.gc()` es solo una sugerencia). Algoritmos: G1GC (default Java 11+), ZGC (baja latencia), Shenandoah.

---

## 9. ¿Qué es final, finally y finalize?
**Respuesta:**
- `final`: constante/no modificable. Variable, método o clase.
  - `final int X = 5;` → no se puede reasignar
  - `final void metodo()` → no se puede override
  - `final class Util` → no se puede heredar
- `finally`: bloque que SIEMPRE se ejecuta después de try/catch (para cerrar recursos).
- `finalize()`: método (DEPRECATED) que el GC llama antes de eliminar un objeto.

---

## 10. ¿Cuándo usarías ArrayList vs LinkedList vs HashMap?
**Respuesta:**
- **ArrayList**: lista donde accedes por índice frecuentemente. O(1) acceso, O(n) inserción al inicio.
- **LinkedList**: lista donde insertas/eliminas mucho al inicio/medio. O(1) inserción, O(n) acceso.
- **HashMap**: cuando necesitas buscar por clave. O(1) get/put.

Regla práctica: ArrayList el 95% del tiempo. HashMap cuando necesitas clave→valor.

---

## 11. ¿Qué es un Record en Java?
**Respuesta:**
Clase inmutable que autogenera: constructor, getters, equals, hashCode, toString. Desde Java 16.

```java
public record Producto(String nombre, double precio) {}
// Equivale a ~30 líneas de clase tradicional
// Uso: var p = new Producto("Laptop", 18999); p.nombre(); p.precio();
```

Usar para: DTOs, value objects, datos inmutables.

---

## 12. ¿Qué es Optional y por qué usarlo?
**Respuesta:**
Contenedor que puede tener un valor o estar vacío. Evita `NullPointerException`.

```java
// Sin Optional (peligroso):
Producto p = buscarPorId(1);  // puede ser null
p.getNombre();  // NullPointerException si es null

// Con Optional (seguro):
Optional<Producto> op = buscarPorId(1);
String nombre = op.map(Producto::getNombre).orElse("No encontrado");
op.ifPresent(prod -> System.out.println(prod));
op.orElseThrow(() -> new NotFoundException("No existe"));
```

---

## 13. Explica Stream API con un ejemplo real
**Respuesta:**
```java
List<Producto> productos = obtenerProductos();

// Productos de electrónica con precio > 5000, top 5 más caros
List<String> resultado = productos.stream()
    .filter(p -> p.getCategoria().equals("Electrónica"))
    .filter(p -> p.getPrecio() > 5000)
    .sorted(Comparator.comparingDouble(Producto::getPrecio).reversed())
    .limit(5)
    .map(Producto::getNombre)
    .toList();

// Total vendido por categoría
Map<String, Double> ventasPorCategoria = productos.stream()
    .collect(Collectors.groupingBy(
        Producto::getCategoria,
        Collectors.summingDouble(Producto::getPrecio)
    ));
```

---

## 14. ¿Qué son las excepciones checked vs unchecked?
**Respuesta:**
- **Checked** (extends Exception): el compilador OBLIGA a manejarlas. `IOException`, `SQLException`.
- **Unchecked** (extends RuntimeException): no obliga. `NullPointerException`, `IllegalArgumentException`.

```java
// Checked: DEBES hacer try/catch o throws
public void leerArchivo() throws IOException { ... }

// Unchecked: puedes manejar o no
public void dividir(int a, int b) {
    if (b == 0) throw new IllegalArgumentException("Divisor no puede ser 0");
    return a / b;
}
```

Regla: usar unchecked para errores de programación (bugs). Checked para errores recuperables.

---

## 15. ¿Qué son Virtual Threads (Java 21)?
**Respuesta:**
Threads ultra-ligeros que permiten crear MILLONES sin saturar recursos:

```java
// Antes (Platform Threads): máximo ~2000 antes de quedarse sin memoria
// Ahora (Virtual Threads): millones sin problema

try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            // Cada tarea corre en su propio virtual thread
            Thread.sleep(1000);  // No bloquea un OS thread
        });
    }
}
```

Beneficio: código síncrono simple pero con la escala de código asíncrono.
