# Manual Completo de Java: Desde Basico hasta Java 22

# Indice
1. Fundamentos
2. Variables y Tipos
3. Operadores
4. Control de Flujo
5. Metodos
6. POO
7. Colecciones
8. Excepciones
9. Generics
10. Streams y Lambdas
11. Fechas
12. Archivos
13. Concurrencia
14. Novedades Java 9-22

---

# 1. Fundamentos de Java

## Que es Java
Java es un lenguaje de programacion orientado a objetos, tipado estatico, compilado a bytecode que se ejecuta en la JVM (Java Virtual Machine).

## Como funciona
```
Codigo.java --[javac]--> Codigo.class (bytecode) --[JVM]--> Ejecucion
```

El bytecode es PORTABLE: funciona en cualquier SO que tenga JVM instalada.

## Primer programa
```java
public class HolaMundo {
    public static void main(String[] args) {
        System.out.println("Hola Mundo!");
    }
}
// Compilar: javac HolaMundo.java
// Ejecutar: java HolaMundo
```

## Estructura basica
```java
package com.empresa.modulo;  // Paquete (organizacion)

import java.util.List;       // Importar clases externas

public class MiClase {       // Clase (1 por archivo, mismo nombre que archivo)
    
    // Atributos (estado)
    private String nombre;
    
    // Constructor
    public MiClase(String nombre) {
        this.nombre = nombre;
    }
    
    // Metodos (comportamiento)
    public String getNombre() {
        return nombre;
    }
    
    // Punto de entrada
    public static void main(String[] args) {
        var obj = new MiClase("Java");
        System.out.println(obj.getNombre());
    }
}
```

---

# 2. Variables y Tipos de Datos

## Tipos Primitivos (8 tipos)
```java
// Enteros
byte   b = 127;              // 8 bits  [-128, 127]
short  s = 32000;            // 16 bits [-32768, 32767]
int    i = 2_000_000_000;   // 32 bits (MAS USADO)
long   l = 9_000_000_000L;  // 64 bits (agregar L)

// Decimales
float  f = 3.14f;           // 32 bits (agregar f)
double d = 3.14159265;      // 64 bits (MAS USADO)

// Caracter y booleano
char   c = 'A';             // 16 bits (1 caracter Unicode)
boolean activo = true;      // true o false
```

## Tipos de Referencia (Objetos)
```java
String nombre = "Carlos";           // Cadena de texto
Integer numero = 42;                // Wrapper de int (puede ser null)
int[] arreglo = {1, 2, 3, 4, 5};  // Array
List<String> lista = new ArrayList<>();  // Coleccion
```

## var (Java 10+)
```java
var mensaje = "Hola";       // Java infiere: String
var precio = 99.99;         // Java infiere: double
var lista = new ArrayList<String>();  // Java infiere el tipo

// var solo funciona en variables LOCALES (no en campos de clase)
// El tipo se determina en COMPILACION (sigue siendo tipado estatico)
```

## Constantes
```java
final double IVA = 0.16;                    // No se puede cambiar
static final int MAX_INTENTOS = 3;          // Constante de clase
// Convencion: MAYUSCULAS_CON_GUION_BAJO
```

## Casting (conversion de tipos)
```java
// Implicito (no pierde datos): int -> long -> double
int entero = 42;
double decimal = entero;  // 42.0 (automatico)

// Explicito (puede perder datos): double -> int
double pi = 3.99;
int truncado = (int) pi;  // 3 (trunca, NO redondea)

// String a numero
int n = Integer.parseInt("42");
double d = Double.parseDouble("3.14");

// Numero a String
String s = String.valueOf(42);
String s2 = "" + 42;  // Concatenacion (menos eficiente)
```

---

# 3. Operadores

```java
// Aritmeticos
10 + 3   // 13
10 - 3   // 7
10 * 3   // 30
10 / 3   // 3 (division ENTERA entre enteros)
10.0 / 3 // 3.333... (division real si al menos uno es double)
10 % 3   // 1 (modulo/residuo)

// Asignacion compuesta
x += 5;  // x = x + 5
x -= 3;  // x = x - 3
x *= 2;  // x = x * 2
x /= 4;  // x = x / 4

// Incremento/Decremento
i++;     // post-incremento (usa valor actual, luego incrementa)
++i;     // pre-incremento (incrementa primero, luego usa)

// Comparacion (devuelven boolean)
==  // igual
!=  // diferente
>   // mayor
>=  // mayor o igual
<   // menor
<=  // menor o igual

// Logicos
&&  // AND (ambos true)
||  // OR (al menos uno true)
!   // NOT (invierte)

// Ternario
String resultado = (edad >= 18) ? "Mayor" : "Menor";

// instanceof (verificar tipo)
if (obj instanceof String s) {  // Java 16+: pattern matching
    System.out.println(s.length());
}
```

---

# 4. Control de Flujo

## if / else
```java
if (nota >= 90) {
    System.out.println("Excelente");
} else if (nota >= 70) {
    System.out.println("Aprobado");
} else {
    System.out.println("Reprobado");
}
```

## switch (clasico vs moderno)
```java
// Clasico (Java 1-13)
switch (dia) {
    case "LUNES":
        System.out.println("Inicio");
        break;  // Sin break: fall-through
    default:
        System.out.println("Otro");
}

// Moderno (Java 14+): sin break, retorna valor
String tipo = switch (dia) {
    case "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" -> "Laboral";
    case "SABADO", "DOMINGO" -> "Fin de semana";
    default -> "Desconocido";
};

// Con bloques (Java 14+)
int resultado = switch (operacion) {
    case "+" -> a + b;
    case "/" -> {
        if (b == 0) throw new ArithmeticException("Division por cero");
        yield a / b;  // yield retorna valor desde un bloque
    }
    default -> 0;
};
```

## Ciclos
```java
// for clasico
for (int i = 0; i < 10; i++) {
    System.out.println(i);
}

// for-each (para colecciones y arrays)
for (String nombre : nombres) {
    System.out.println(nombre);
}

// while
while (condicion) {
    // se ejecuta mientras condicion sea true
}

// do-while (se ejecuta al menos 1 vez)
do {
    // codigo
} while (condicion);

// break: salir del ciclo
// continue: saltar a la siguiente iteracion
```

---

# 5. Metodos (Funciones)

```java
// Metodo con retorno
public double calcularIVA(double precio) {
    return precio * 0.16;
}

// Metodo sin retorno (void)
public void imprimir(String mensaje) {
    System.out.println(mensaje);
}

// Metodo estatico (no necesita instancia)
public static int sumar(int a, int b) {
    return a + b;
}
// Uso: int resultado = MiClase.sumar(3, 4);

// Varargs (numero variable de argumentos)
public int sumarTodos(int... numeros) {
    int total = 0;
    for (int n : numeros) total += n;
    return total;
}
// Uso: sumarTodos(1, 2, 3, 4, 5);

// Sobrecarga (mismo nombre, diferentes parametros)
public double area(double radio) { return Math.PI * radio * radio; }
public double area(double ancho, double alto) { return ancho * alto; }
```

---

# 6. Programacion Orientada a Objetos

## Clase y Objeto
```java
// CLASE = plano/molde
public class Producto {
    private String nombre;
    private double precio;
    
    public Producto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    
    public double getPrecioConIVA() {
        return precio * 1.16;
    }
}

// OBJETO = instancia concreta
Producto laptop = new Producto("Laptop HP", 18999);
System.out.println(laptop.getPrecioConIVA());  // 22038.84
```

## Herencia
```java
public class Empleado {
    protected String nombre;
    protected double salario;
    
    public double calcularPago() { return salario; }
}

public class Vendedor extends Empleado {
    private double comision;
    
    @Override
    public double calcularPago() {
        return salario + comision;  // Sobreescribe el metodo del padre
    }
}
```

## Interfaces
```java
public interface Pagable {
    double calcularMonto();
    void procesarPago();
    
    // Default method (Java 8+)
    default String generarRecibo() {
        return "Recibo #" + System.currentTimeMillis();
    }
}

public class Venta implements Pagable {
    @Override
    public double calcularMonto() { return total * 1.16; }
    
    @Override
    public void procesarPago() { /* logica */ }
}
```

## Records (Java 16+)
```java
// ANTES: 30+ lineas para un DTO
public class ClienteDto {
    private final String nombre;
    private final String email;
    // constructor, getters, equals, hashCode, toString...
}

// AHORA: 1 linea
public record ClienteDto(String nombre, String email, int edad) {}

// Uso:
var cliente = new ClienteDto("Carlos", "carlos@mail.com", 30);
cliente.nombre();  // "Carlos" (getter automatico)
```

## Sealed Classes (Java 17+)
```java
// Solo estas clases pueden implementar MetodoPago
public sealed interface MetodoPago permits Efectivo, Tarjeta, Transferencia {}

public record Efectivo(double monto) implements MetodoPago {}
public record Tarjeta(String numero, double monto) implements MetodoPago {}
public record Transferencia(String clabe, double monto) implements MetodoPago {}

// Pattern matching exhaustivo
String procesar(MetodoPago pago) {
    return switch (pago) {
        case Efectivo e -> "Efectivo: $" + e.monto();
        case Tarjeta t -> "Tarjeta: " + t.numero();
        case Transferencia tr -> "CLABE: " + tr.clabe();
        // No necesitas default (el compilador sabe que cubriste todo)
    };
}
```

---

# 7. Colecciones

## Jerarquia
```
Collection
  List (ordenada, permite duplicados)
    ArrayList  → acceso O(1), insercion O(n)
    LinkedList → acceso O(n), insercion O(1)
  Set (sin duplicados)
    HashSet    → sin orden, O(1)
    TreeSet    → ordenado, O(log n)
  Queue (FIFO)
    LinkedList
    PriorityQueue

Map (clave-valor, NO extiende Collection)
  HashMap  → sin orden, O(1)
  TreeMap  → ordenado por clave, O(log n)
  LinkedHashMap → orden de insercion
```

## Ejemplos
```java
// List
List<String> nombres = new ArrayList<>(List.of("Ana", "Luis", "Maria"));
nombres.add("Carlos");
nombres.get(0);  // "Ana"
nombres.remove("Luis");
nombres.contains("Maria");  // true
nombres.size();  // 3

// Map
Map<String, Double> precios = new HashMap<>();
precios.put("Laptop", 18999.99);
precios.put("Mouse", 899.00);
precios.get("Laptop");  // 18999.99
precios.containsKey("Monitor");  // false
precios.getOrDefault("Monitor", 0.0);  // 0.0

// Set
Set<String> emails = new HashSet<>();
emails.add("carlos@mail.com");
emails.add("carlos@mail.com");  // No se agrega (duplicado)
emails.size();  // 1

// Iterar Map
for (var entry : precios.entrySet()) {
    System.out.println(entry.getKey() + ": $" + entry.getValue());
}
```

---

# 8. Excepciones

## Jerarquia
```
Throwable
  Error (NO manejar: OutOfMemoryError, StackOverflowError)
  Exception
    Checked (DEBES manejar: IOException, SQLException)
    RuntimeException / Unchecked (PUEDES manejar: NullPointerException)
```

## Try-Catch-Finally
```java
try {
    int resultado = 10 / 0;
} catch (ArithmeticException e) {
    System.out.println("Error: " + e.getMessage());
} catch (Exception e) {
    System.out.println("Error generico");
} finally {
    System.out.println("Siempre se ejecuta");
}
```

## Try-with-resources (Java 7+)
```java
// Cierra automaticamente recursos que implementan AutoCloseable
try (var reader = new BufferedReader(new FileReader("datos.txt"))) {
    String linea = reader.readLine();
} catch (IOException e) {
    e.printStackTrace();
}
// reader se cierra automaticamente (no hay memory leak)
```

## Custom Exception
```java
public class ProductoNoEncontradoException extends RuntimeException {
    private final Long productoId;
    
    public ProductoNoEncontradoException(Long id) {
        super("Producto no encontrado con id: " + id);
        this.productoId = id;
    }
    
    public Long getProductoId() { return productoId; }
}

// Uso:
throw new ProductoNoEncontradoException(42L);
```

---

# 9. Generics

```java
// Clase generica
public class Resultado<T> {
    private final boolean exito;
    private final T datos;
    private final String error;
    
    public static <T> Resultado<T> ok(T datos) {
        return new Resultado<>(true, datos, null);
    }
    
    public static <T> Resultado<T> error(String msg) {
        return new Resultado<>(false, null, msg);
    }
}

// Uso:
Resultado<Producto> r1 = Resultado.ok(new Producto("Laptop", 999));
Resultado<List<String>> r2 = Resultado.ok(List.of("a", "b"));
Resultado<Void> r3 = Resultado.error("No encontrado");

// Metodo generico
public <T extends Comparable<T>> T maximo(T a, T b) {
    return a.compareTo(b) > 0 ? a : b;
}

// Wildcards
List<?>              // cualquier tipo (solo lectura)
List<? extends Number>  // Number o subclases (lectura)
List<? super Integer>   // Integer o superclases (escritura)
```

---

# 10. Streams y Lambdas (Java 8+)

## Lambdas
```java
// Sin lambda (clase anonima)
Comparator<String> comp = new Comparator<String>() {
    public int compare(String a, String b) { return a.compareTo(b); }
};

// Con lambda
Comparator<String> comp = (a, b) -> a.compareTo(b);

// Method reference
Comparator<String> comp = String::compareTo;
```

## Functional Interfaces
```java
Predicate<T>   // T -> boolean    (filtrar)
Function<T,R>  // T -> R          (transformar)
Consumer<T>    // T -> void       (ejecutar)
Supplier<T>    // () -> T         (crear)
```

## Stream API
```java
List<Producto> productos = obtenerProductos();

// Filtrar + transformar + ordenar + limitar
List<String> resultado = productos.stream()
    .filter(p -> p.getPrecio() > 1000)       // Solo caros
    .filter(Producto::isActivo)               // Solo activos
    .map(Producto::getNombre)                 // Extraer nombre
    .sorted()                                 // Ordenar A-Z
    .limit(10)                                // Maximo 10
    .toList();                                // Recolectar

// Reducir a un valor
double total = productos.stream()
    .mapToDouble(Producto::getPrecio)
    .sum();

// Agrupar
Map<String, List<Producto>> porCategoria = productos.stream()
    .collect(Collectors.groupingBy(Producto::getCategoria));

// Estadisticas
DoubleSummaryStatistics stats = productos.stream()
    .mapToDouble(Producto::getPrecio)
    .summaryStatistics();
// stats.getAverage(), stats.getMax(), stats.getMin(), stats.getCount()
```

---

# 11. Fechas (java.time - Java 8+)

```java
// Fecha sin hora
LocalDate hoy = LocalDate.now();
LocalDate navidad = LocalDate.of(2026, 12, 25);

// Fecha con hora
LocalDateTime ahora = LocalDateTime.now();
ZonedDateTime conZona = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));

// Operaciones
LocalDate manana = hoy.plusDays(1);
long diasEntre = ChronoUnit.DAYS.between(hoy, navidad);

// Antiguedad
Period antiguedad = Period.between(fechaIngreso, hoy);
System.out.println(antiguedad.getYears() + " anios");

// Formatear
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
String formateado = ahora.format(fmt);  // "30/05/2026 14:30"

// Parsear
LocalDate parsed = LocalDate.parse("2026-12-25");
```

---

# 12. Archivos (NIO - Java 7+)

```java
// Leer archivo completo
String contenido = Files.readString(Path.of("datos.txt"));
List<String> lineas = Files.readAllLines(Path.of("datos.csv"));

// Escribir
Files.writeString(Path.of("salida.txt"), "Hola mundo");
Files.write(Path.of("lista.txt"), lineas);

// Leer linea por linea (eficiente para archivos grandes)
try (var reader = Files.newBufferedReader(Path.of("grande.csv"))) {
    reader.lines()
        .skip(1)  // Saltar header
        .map(line -> line.split(","))
        .forEach(cols -> System.out.println(cols[0]));
}

// Crear directorios
Files.createDirectories(Path.of("reportes/2026/mayo"));

// Listar archivos
Files.list(Path.of("."))
    .filter(Files::isRegularFile)
    .forEach(System.out::println);
```

---

# 13. Concurrencia

## Virtual Threads (Java 21+)
```java
// ANTES: Thread pool limitado (200 threads)
ExecutorService executor = Executors.newFixedThreadPool(200);

// AHORA: Virtual Threads (1,000,000+ tareas)
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    IntStream.range(0, 100_000).forEach(i ->
        executor.submit(() -> {
            Thread.sleep(Duration.ofMillis(100));
            return procesarPedido(i);
        })
    );
}
```

## CompletableFuture
```java
CompletableFuture<String> futuro = CompletableFuture
    .supplyAsync(() -> buscarDatos())        // Async
    .thenApply(datos -> transformar(datos))   // Encadenar
    .thenApply(datos -> formatear(datos))
    .exceptionally(ex -> "Error: " + ex.getMessage());

String resultado = futuro.get();  // Esperar resultado

// Paralelo
CompletableFuture.allOf(futuro1, futuro2, futuro3).join();
```

---

# 14. Novedades por Version (Java 9-22)

## Java 9 (2017)
```java
// Modulos (JPMS)
module com.empresa.app {
    requires java.sql;
    exports com.empresa.api;
}

// Metodos de fabrica para colecciones inmutables
List<String> lista = List.of("a", "b", "c");
Map<String, Integer> mapa = Map.of("uno", 1, "dos", 2);
Set<String> set = Set.of("x", "y", "z");

// Stream: takeWhile, dropWhile
Stream.of(1,2,3,4,5).takeWhile(n -> n < 4);  // [1,2,3]
```

## Java 10 (2018)
```java
// var (inferencia de tipo local)
var lista = new ArrayList<String>();
var mapa = Map.of("key", "value");
```

## Java 11 (2018 - LTS)
```java
// String nuevos metodos
"  hola  ".strip();       // "hola" (mejor que trim, soporta Unicode)
"  ".isBlank();           // true
"hola\nmundo".lines();    // Stream<String>
"ja".repeat(3);           // "jajaja"

// Files.readString / writeString
String contenido = Files.readString(Path.of("archivo.txt"));

// HTTP Client (reemplaza HttpURLConnection)
HttpClient client = HttpClient.newHttpClient();
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.ejemplo.com/datos"))
    .build();
HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
```

## Java 14 (2020)
```java
// Switch expressions (ya no preview)
String tipo = switch (dia) {
    case "LUNES" -> "Laboral";
    case "SABADO" -> "Descanso";
    default -> "Otro";
};

// Records (preview)
record Punto(int x, int y) {}

// Helpful NullPointerExceptions
// Antes: "NullPointerException" (sin detalle)
// Ahora: "Cannot invoke String.length() because the return value of getEmail() is null"
```

## Java 16 (2021)
```java
// Records (estable)
public record ProductoDto(Long id, String nombre, double precio) {}

// Pattern matching para instanceof
if (obj instanceof String s) {
    System.out.println(s.length());  // s ya esta casteado
}

// Stream.toList() (inmutable)
List<String> lista = stream.toList();  // vs .collect(Collectors.toList())
```

## Java 17 (2021 - LTS)
```java
// Sealed classes
public sealed interface Forma permits Circulo, Rectangulo, Triangulo {}
public record Circulo(double radio) implements Forma {}
public record Rectangulo(double ancho, double alto) implements Forma {}

// Pattern matching en switch (preview)
String desc = switch (forma) {
    case Circulo c -> "Circulo de radio " + c.radio();
    case Rectangulo r -> "Rectangulo " + r.ancho() + "x" + r.alto();
    case Triangulo t -> "Triangulo";
};

// Text blocks (estable desde Java 15)
String json = """
        {
            "nombre": "Carlos",
            "edad": 30
        }
        """;
```

## Java 21 (2023 - LTS)
```java
// Virtual Threads (estable)
Thread.startVirtualThread(() -> {
    System.out.println("Hola desde virtual thread");
});

// En Spring Boot:
// spring.threads.virtual.enabled=true

// Sequenced Collections
SequencedCollection<String> lista = new ArrayList<>();
lista.addFirst("primero");
lista.addLast("ultimo");
lista.getFirst();
lista.getLast();
lista.reversed();  // Vista invertida

// Pattern matching switch (estable)
String resultado = switch (obj) {
    case Integer i -> "Entero: " + i;
    case String s when s.length() > 5 -> "String largo";
    case String s -> "String: " + s;
    case null -> "null";
    default -> "Otro";
};

// Record patterns (deconstruccion)
record Punto(int x, int y) {}
if (obj instanceof Punto(int x, int y)) {
    System.out.println("x=" + x + ", y=" + y);
}
```

## Java 22 (2024)
```java
// Statements before super() (preview)
public class Hijo extends Padre {
    public Hijo(String valor) {
        // Ahora puedes ejecutar codigo ANTES de super()
        if (valor == null) throw new IllegalArgumentException("null!");
        super(valor.toUpperCase());
    }
}

// Unnamed variables (estable)
try {
    // ...
} catch (Exception _) {  // No necesitas el nombre si no lo usas
    System.out.println("Error ignorado");
}

for (var _ : lista) {  // Solo necesitas contar iteraciones
    count++;
}

// Stream Gatherers (preview)
// Permite crear operaciones intermedias custom en streams
stream.gather(windowFixed(3));  // Agrupar de 3 en 3
```

---

# 15. Ejercicios por Nivel

## Basico (1-20)
1. Calculadora con las 4 operaciones
2. Conversor de temperatura (C, F, K)
3. Determinar si un numero es primo
4. FizzBuzz (1-100)
5. Invertir un string sin reverse()
6. Contar vocales en una cadena
7. Factorial recursivo e iterativo
8. Serie Fibonacci
9. Ordenar array con bubble sort
10. Busqueda binaria en array ordenado

## Intermedio (21-40)
11. Sistema de inventario con HashMap
12. Cola de atencion al cliente (Queue)
13. Validador de email con regex
14. Leer CSV y cargar en List de objetos
15. Implementar Stack con array
16. Patron Strategy para descuentos
17. Streams: top 5 productos mas caros por categoria
18. CompletableFuture: descargar 5 URLs en paralelo
19. Custom exception hierarchy para sistema bancario
20. Generics: clase Cache<K,V> con TTL

## Avanzado (41-50)
21. Implementar LRU Cache con O(1)
22. Producer/Consumer con BlockingQueue
23. API REST completa con Spring Boot
24. JWT authentication desde cero
25. Conexion a PostgreSQL con JDBC puro
26. Implementar patron Observer para eventos
27. Virtual Threads: procesar 100K tareas
28. Sealed classes + pattern matching para maquina de estados
29. Records + Streams para ETL de datos
30. Refactorizar codigo legacy a Java 21

---

# 16. Buenas Practicas

```java
// 1. Nombres claros
// MAL:  int d; String s; void proc();
// BIEN: int diasRestantes; String nombreCliente; void calcularDescuento();

// 2. Metodos cortos (max 20 lineas)
// Si un metodo hace muchas cosas -> dividir en metodos mas pequenos

// 3. Inmutabilidad cuando sea posible
// Usar records, final, List.of(), Collections.unmodifiableList()

// 4. Evitar null
// Usar Optional<T> en lugar de retornar null
public Optional<Producto> buscar(Long id) {
    return repository.findById(id);
}

// 5. Cerrar recursos (try-with-resources)
// NUNCA dejar conexiones, streams o archivos abiertos

// 6. Validar entrada
// NUNCA confiar en datos del usuario
if (precio <= 0) throw new IllegalArgumentException("Precio debe ser positivo");

// 7. Logging en lugar de System.out.println
private static final Logger log = LoggerFactory.getLogger(MiClase.class);
log.info("Producto creado: {}", producto.getId());
log.error("Error procesando venta", exception);
```
