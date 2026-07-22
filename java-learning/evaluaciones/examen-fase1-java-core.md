# Examen Fase 1: Java Core (Módulos 1-13)

## Instrucciones
- Tiempo: 90 minutos
- Sin IDE (solo papel/pizarra o editor básico sin autocompletado)
- Puedes compilar y ejecutar para verificar

---

## Sección A: Teoría (30 puntos, 3 pts cada una)

1. ¿Cuál es la diferencia entre Stack y Heap en Java? ¿Dónde viven los primitivos vs los objetos?
2. ¿Por qué String es inmutable? Nombra 3 razones.
3. ¿Cuál es la diferencia entre `HashMap` y `ConcurrentHashMap`?
4. Explica el ciclo de vida de un Thread en Java (estados).
5. ¿Qué es el problema del "diamond" en herencia múltiple y cómo Java lo resuelve?
6. ¿Cuándo usarías `Comparable` vs `Comparator`?
7. ¿Qué es `try-with-resources` y por qué se inventó?
8. ¿Cuál es la diferencia entre `Collections.unmodifiableList()` y `List.of()`?
9. ¿Qué hace `volatile` en una variable?
10. ¿Qué es un `sealed class` (Java 17) y para qué sirve?

---

## Sección B: Código (70 puntos)

### B1. Streams (10 pts)
Dada una `List<Empleado>` donde Empleado tiene: nombre, departamento, salario.

Escribe UN pipeline de Stream que:
1. Filtre solo empleados con salario > 30,000
2. Agrupe por departamento
3. Para cada grupo, calcule el salario promedio
4. Devuelva un `Map<String, Double>` ordenado por promedio descendente

### B2. Generics (10 pts)
Implementa una clase genérica `Resultado<T>` que represente éxito o error:
```java
// Uso esperado:
Resultado<Producto> ok = Resultado.exito(producto);
Resultado<Producto> err = Resultado.error("Producto no encontrado");

ok.isExito();        // true
ok.getValor();       // producto
err.getError();      // "Producto no encontrado"
err.getValor();      // lanza NoSuchElementException
```

### B3. Colecciones + POO (15 pts)
Diseña e implementa un sistema de CACHÉ simple (`MiCache<K, V>`) con:
- `put(K key, V value)` → agregar al caché
- `get(K key)` → obtener (o null si no existe)
- `containsKey(K key)` → verificar existencia
- `size()` → cantidad de elementos
- `clear()` → vaciar
- Capacidad máxima: si se excede, eliminar el PRIMER elemento insertado (FIFO)

### B4. Excepciones (10 pts)
Crea una jerarquía de excepciones para un sistema de pagos:
- `PagoException` (base)
  - `FondosInsuficientesException` (con monto disponible y monto requerido)
  - `TarjetaExpiradaException` (con fecha de expiración)
  - `LimiteExcedidoException` (con límite y monto intentado)

Incluye constructor, campos, getters, y un método `getMessage()` descriptivo.

### B5. Concurrencia (15 pts)
Implementa un `ContadorThreadSafe` que:
- Múltiples hilos puedan incrementar simultáneamente
- Tenga métodos: `incrementar()`, `decrementar()`, `getValor()`
- Sea thread-safe (sin race conditions)
- Demuéstralo creando 10 threads que incrementen 1000 veces cada uno → resultado debe ser 10,000

### B6. Archivos + Streams (10 pts)
Escribe un método que lea un archivo CSV de productos (nombre,precio,stock) y devuelva:
- `List<Producto>` con todos los productos
- Ignorar la primera línea (header)
- Si una línea tiene formato inválido, loguear warning y continuar
- Usar try-with-resources y NIO (Files, Path)

---

## Rúbrica
| Criterio | Peso |
|----------|------|
| Compila sin errores | 25% |
| Lógica correcta | 35% |
| Buenas prácticas (naming, inmutabilidad, null-safety) | 20% |
| Manejo de edge cases | 10% |
| Código limpio y legible | 10% |

## Aprobación
- 50+ = Aprobado (necesitas reforzar)
- 65+ = Competente (puedes avanzar a Spring)
- 80+ = Sólido (listo para entrevista junior Java)
- 90+ = Excelente (nivel mid en Java core)
