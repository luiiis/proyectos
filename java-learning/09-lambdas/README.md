# Módulo 09: Lambdas y Functional Interfaces

## Expresiones Lambda
Funciones anónimas que se pasan como parámetro.

```java
// Sin lambda
Comparator<Producto> comp = new Comparator<Producto>() {
    @Override
    public int compare(Producto a, Producto b) {
        return Double.compare(a.getPrecio(), b.getPrecio());
    }
};

// Con lambda (1 línea)
Comparator<Producto> comp = (a, b) -> Double.compare(a.getPrecio(), b.getPrecio());

// Method reference (aún más corto)
Comparator<Producto> comp = Comparator.comparingDouble(Producto::getPrecio);
```

## Functional Interfaces Principales
```java
Predicate<T>    → boolean test(T t)        // Filtrar: p -> p.getPrecio() > 1000
Function<T,R>   → R apply(T t)            // Transformar: p -> p.getNombre()
Consumer<T>     → void accept(T t)        // Ejecutar: p -> System.out.println(p)
Supplier<T>     → T get()                 // Crear: () -> new Producto()
BiFunction<T,U,R> → R apply(T t, U u)    // Dos params: (a, b) -> a + b
UnaryOperator<T> → T apply(T t)          // Mismo tipo: s -> s.toUpperCase()
```

## Ejercicios
1. Ordena una lista de empleados por: nombre, salario, fecha de ingreso (3 Comparators)
2. Crea un validador genérico usando Predicate<T>
3. Implementa un pipeline de transformaciones con Function.andThen()
4. Crea un builder de queries usando lambdas
