# Módulo 07: Generics

## ¿Qué son?
Permiten escribir código que funciona con CUALQUIER tipo, manteniendo type-safety en compilación.

```java
// Sin generics: casting manual, errores en runtime
List lista = new ArrayList();
lista.add("Hola");
lista.add(42);  // Compila, pero es un error lógico
String s = (String) lista.get(1);  // ClassCastException en RUNTIME

// Con generics: errores detectados en COMPILACIÓN
List<String> lista = new ArrayList<>();
lista.add("Hola");
lista.add(42);  // ERROR DE COMPILACIÓN ← mucho mejor
```

## Clases Genéricas
```java
public class Resultado<T> {
    private final boolean exito;
    private final T datos;
    private final String error;

    public static <T> Resultado<T> ok(T datos) {
        return new Resultado<>(true, datos, null);
    }
    public static <T> Resultado<T> error(String mensaje) {
        return new Resultado<>(false, null, mensaje);
    }
}

// Uso:
Resultado<Producto> r1 = Resultado.ok(new Producto("Laptop", 999));
Resultado<List<Cliente>> r2 = Resultado.ok(listaClientes);
Resultado<Void> r3 = Resultado.error("No encontrado");
```

## Bounded Types y Wildcards
```java
// T debe ser Comparable
public <T extends Comparable<T>> T maximo(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Wildcards
List<?> cualquierLista;                    // Solo lectura
List<? extends Number> numeros;            // Number o subclases (lectura)
List<? super Integer> destino;             // Integer o superclases (escritura)
```

## Ejercicios
1. Crea una clase genérica `Par<A, B>` que almacene dos valores de tipos diferentes
2. Crea un método genérico que encuentre el máximo de una lista de Comparable
3. Implementa una clase `Cache<K, V>` genérica con get, put, remove
