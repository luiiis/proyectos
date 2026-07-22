# Manual Completo: Java 8 a Java 25 — Lo Nuevo de Cada Versión

## Objetivo
Aprender qué se introdujo en CADA versión de Java, con ejemplos ejecutables.
Enfoque: solo features que USARÁS en el trabajo diario (no experimentales que se descartaron).

---

## Mapa de Versiones

| Versión | Fecha | LTS | Lo más importante |
|---------|-------|-----|-------------------|
| 8 | Mar 2014 | ✅ | Lambdas, Streams, Optional, Date/Time API |
| 9 | Sep 2017 | ❌ | Modules (JPMS), JShell, colecciones inmutables |
| 10 | Mar 2018 | ❌ | `var` (inferencia de tipo local) |
| 11 | Sep 2018 | ✅ | HttpClient, String methods, ejecutar .java directo |
| 12 | Mar 2019 | ❌ | Switch expressions (preview) |
| 13 | Sep 2019 | ❌ | Text blocks (preview) |
| 14 | Mar 2020 | ❌ | Switch expressions (final), Records (preview) |
| 15 | Sep 2020 | ❌ | Text blocks (final), Sealed classes (preview) |
| 16 | Mar 2021 | ❌ | Records (final), Pattern matching instanceof |
| 17 | Sep 2021 | ✅ | Sealed classes (final), pattern matching |
| 18 | Mar 2022 | ❌ | UTF-8 default, simple web server |
| 19 | Sep 2022 | ❌ | Virtual threads (preview), structured concurrency |
| 20 | Mar 2023 | ❌ | Scoped values (preview) |
| 21 | Sep 2023 | ✅ | Virtual threads (final), sequenced collections, pattern matching switch |
| 22 | Mar 2024 | ❌ | Unnamed variables, statements before super |
| 23 | Sep 2024 | ❌ | Primitive patterns (preview), module imports |
| 24 | Mar 2025 | ❌ | Stream gatherers (final), class-file API |
| 25 | Sep 2025 | ✅ | Value classes (preview), stable features consolidation |

**LTS = Long-Term Support** (soporte de 8+ años). Usar en producción: Java 21 o 25.

---

# Java 8 (2014) — La revolución

La versión que CAMBIÓ Java. Antes de Java 8, Java era verbose y funcional era imposible.

## Lambdas (funciones anónimas)
```java
// ANTES (Java 7): clases anónimas verbosas
Comparator<String> comp = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
};

// DESPUÉS (Java 8): lambdas concisas
Comparator<String> comp = (a, b) -> a.compareTo(b);

// Method reference (aún más conciso)
Comparator<String> comp = String::compareTo;

// Lambdas se usan con Functional Interfaces (1 solo método abstracto):
Predicate<Integer> esPar = n -> n % 2 == 0;
Function<String, Integer> longitud = String::length;
Consumer<String> imprimir = System.out::println;
Supplier<List<String>> crearLista = ArrayList::new;
```

## Stream API
```java
List<Producto> productos = obtenerProductos();

// Filtrar + transformar + ordenar + recolectar
List<String> nombres = productos.stream()
    .filter(p -> p.getPrecio() > 1000)          // Solo caros
    .filter(Producto::isActivo)                   // Solo activos
    .map(Producto::getNombre)                     // Extraer nombre
    .sorted()                                     // Ordenar A-Z
    .limit(10)                                    // Máximo 10
    .collect(Collectors.toList());                // Crear lista

// Reducir a un valor
double total = productos.stream()
    .mapToDouble(Producto::getPrecio)
    .sum();

// Agrupar
Map<String, List<Producto>> porCategoria = productos.stream()
    .collect(Collectors.groupingBy(Producto::getCategoria));

// Estadísticas
DoubleSummaryStatistics stats = productos.stream()
    .mapToDouble(Producto::getPrecio)
    .summaryStatistics();
// stats.getAverage(), stats.getMax(), stats.getMin(), stats.getCount()

// ParallelStream (usar con cuidado)
long count = productos.parallelStream()
    .filter(p -> p.getPrecio() > 5000)
    .count();
```

## Optional (adiós NullPointerException)
```java
// ANTES: NPE esperando a pasar
String nombre = cliente.getDireccion().getCiudad().toUpperCase(); // Boom si algo es null

// DESPUÉS: manejo explícito de ausencia
Optional<String> ciudad = Optional.ofNullable(cliente)
    .map(Cliente::getDireccion)
    .map(Direccion::getCiudad)
    .map(String::toUpperCase);

String resultado = ciudad.orElse("Ciudad no especificada");

// Métodos clave:
Optional.of(valor);              // Nunca null (lanza si es null)
Optional.ofNullable(valor);      // Puede ser null
optional.isPresent();            // ¿Tiene valor?
optional.ifPresent(v -> ...);    // Ejecutar si tiene valor
optional.orElse(default);        // Valor o default
optional.orElseThrow(() -> new NotFoundException());
optional.map(v -> transform);    // Transformar si existe
optional.filter(v -> condition); // Filtrar
```

## Date/Time API (java.time)
```java
// ANTES (java.util.Date): mutable, confuso, thread-unsafe
// DESPUÉS (java.time): inmutable, claro, thread-safe

LocalDate hoy = LocalDate.now();                    // 2026-07-21
LocalDate navidad = LocalDate.of(2026, 12, 25);
LocalTime ahora = LocalTime.now();                  // 14:30:45
LocalDateTime fechaHora = LocalDateTime.now();      // 2026-07-21T14:30:45
ZonedDateTime conZona = ZonedDateTime.now(ZoneId.of("America/Mexico_City"));

// Operaciones (inmutables, devuelven nuevo objeto)
LocalDate manana = hoy.plusDays(1);
LocalDate mesAnterior = hoy.minusMonths(1);
long diasEntre = ChronoUnit.DAYS.between(hoy, navidad);

// Formateo
DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
String formateado = fechaHora.format(fmt);  // "21/07/2026 14:30"

// Parseo
LocalDate parsed = LocalDate.parse("25/12/2026", DateTimeFormatter.ofPattern("dd/MM/yyyy"));

// Período (años, meses, días entre fechas)
Period periodo = Period.between(LocalDate.of(2020, 1, 1), hoy);
// periodo.getYears() = 6, periodo.getMonths() = 6
```

## Default Methods en Interfaces
```java
public interface Vehiculo {
    void acelerar();  // Abstracto (obligatorio implementar)
    
    // Default: implementación opcional que los hijos heredan
    default void frenar() {
        System.out.println("Frenando...");
    }
    
    // Static: método de utilidad en la interface
    static Vehiculo crear(String tipo) {
        return switch (tipo) {
            case "auto" -> new Auto();
            case "moto" -> new Moto();
            default -> throw new IllegalArgumentException();
        };
    }
}
```

---

# Java 9 (2017)

## Módulos (JPMS)
```java
// module-info.java en la raíz del proyecto
module com.empresa.ventas {
    requires java.sql;              // Dependencia
    requires java.net.http;         
    exports com.empresa.ventas.api; // Lo que otros módulos pueden usar
    opens com.empresa.ventas.entity to org.hibernate; // Reflexión permitida
}
// En la práctica: la mayoría de apps Spring Boot NO usan módulos.
// Es más para librerías y el propio JDK.
```

## Colecciones Inmutables (Factory Methods)
```java
// ANTES: verbose
List<String> lista = Collections.unmodifiableList(Arrays.asList("a", "b", "c"));

// DESPUÉS: conciso
List<String> lista = List.of("a", "b", "c");           // Inmutable
Set<Integer> set = Set.of(1, 2, 3);                    // Inmutable
Map<String, Integer> map = Map.of("a", 1, "b", 2);    // Inmutable

// Intentar modificar → UnsupportedOperationException
// lista.add("d"); // Error!
```

## JShell (REPL)
```bash
# En terminal:
jshell
jshell> int x = 5 + 3;
x ==> 8
jshell> List.of(1,2,3).stream().filter(n -> n > 1).toList()
$2 ==> [2, 3]
# Ideal para probar código rápido sin crear archivo
```

## Stream: takeWhile, dropWhile, ofNullable
```java
// takeWhile: tomar mientras la condición sea true (para streams ordenados)
Stream.of(1, 2, 3, 4, 5, 1, 2).takeWhile(n -> n < 4)  // [1, 2, 3]

// dropWhile: saltar mientras la condición sea true
Stream.of(1, 2, 3, 4, 5).dropWhile(n -> n < 3)  // [3, 4, 5]

// ofNullable: stream de 0 o 1 elementos
Stream.ofNullable(null).count()  // 0
Stream.ofNullable("hola").count()  // 1
```

---

# Java 10 (2018)

## var (Inferencia de Tipo Local)
```java
// Java infiere el tipo en compilación
var nombre = "Carlos";           // String
var precio = 99.99;              // double
var lista = new ArrayList<String>();  // ArrayList<String>
var mapa = Map.of("a", 1);      // Map<String, Integer>

// Dónde funciona:
var x = 10;                      // ✓ variables locales
for (var item : lista) { }       // ✓ for-each
try (var reader = new FileReader("f")) { }  // ✓ try-with-resources

// Dónde NO funciona:
// var campo;                    // ✗ campos de clase
// var parametro                 // ✗ parámetros de método
// var sin inicializar;          // ✗ debe tener valor inicial
// var nulo = null;              // ✗ no se puede inferir de null
```

---

# Java 11 (2018, LTS)

## HttpClient (moderno, reemplaza HttpURLConnection)
```java
HttpClient client = HttpClient.newHttpClient();

// GET
HttpRequest request = HttpRequest.newBuilder()
    .uri(URI.create("https://api.ejemplo.com/productos"))
    .header("Authorization", "Bearer token123")
    .GET()
    .build();

HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
System.out.println(response.statusCode());  // 200
System.out.println(response.body());        // JSON

// POST
HttpRequest post = HttpRequest.newBuilder()
    .uri(URI.create("https://api.ejemplo.com/productos"))
    .header("Content-Type", "application/json")
    .POST(HttpRequest.BodyPublishers.ofString("{\"nombre\":\"Laptop\",\"precio\":18999}"))
    .build();

// Async
client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenApply(HttpResponse::body)
    .thenAccept(System.out::println);
```

## String Methods Nuevos
```java
"  hola  ".strip();          // "hola" (como trim pero Unicode-aware)
"  hola  ".stripLeading();   // "hola  "
"  hola  ".stripTrailing();  // "  hola"
"".isBlank();                // true (vacío o solo espacios)
"hola".repeat(3);            // "holaholahola"
"a\nb\nc".lines().toList();  // ["a", "b", "c"]
```

## Ejecutar archivo .java directamente
```bash
# Sin compilar explícitamente:
java HolaMundo.java
# Java 11+ compila y ejecuta en un paso (para scripts simples)
```

---

# Java 14 (2020)

## Switch Expressions (final)
```java
// Retorna un valor, sin break, sin fall-through
String tipo = switch (dia) {
    case LUNES, MARTES, MIERCOLES, JUEVES, VIERNES -> "Laboral";
    case SABADO, DOMINGO -> "Fin de semana";
};

// Con bloques (yield retorna el valor)
int resultado = switch (operacion) {
    case "+" -> a + b;
    case "-" -> a - b;
    case "/" -> {
        if (b == 0) throw new ArithmeticException("División por cero");
        yield a / b;
    }
    default -> throw new IllegalArgumentException("Op no válida");
};
```

## Helpful NullPointerException
```java
// ANTES (Java 13-): "NullPointerException" (¿en cuál?)
// a.b.c.d → NullPointerException

// DESPUÉS (Java 14+): te dice EXACTAMENTE qué fue null
// "Cannot invoke String.toUpperCase() because the return value of Persona.getNombre() is null"
```

---

# Java 15 (2020)

## Text Blocks (final)
```java
// ANTES: concatenación fea
String json = "{\n" +
    "  \"nombre\": \"Carlos\",\n" +
    "  \"edad\": 30\n" +
    "}";

// DESPUÉS: text blocks (triple quote)
String json = """
        {
          "nombre": "Carlos",
          "edad": 30
        }
        """;

// SQL legible
String sql = """
        SELECT e.nombre, e.salario, s.nombre AS sucursal
        FROM empleados e
        JOIN sucursales s ON e.sucursal_id = s.id
        WHERE e.salario > %d
        ORDER BY e.salario DESC
        """.formatted(50000);

// HTML
String html = """
        <html>
            <body>
                <h1>%s</h1>
                <p>Bienvenido al sistema</p>
            </body>
        </html>
        """.formatted(nombre);
```

---

# Java 16 (2021)

## Records (final)
```java
// ANTES: 30+ líneas para un DTO simple
public class ProductoDto {
    private final String nombre;
    private final double precio;
    public ProductoDto(String nombre, double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    @Override public boolean equals(Object o) { ... }
    @Override public int hashCode() { ... }
    @Override public String toString() { ... }
}

// DESPUÉS: 1 línea
public record ProductoDto(String nombre, double precio) {}

// Genera automáticamente: constructor, getters (nombre(), precio()), equals, hashCode, toString
var p = new ProductoDto("Laptop", 18999);
p.nombre();    // "Laptop"
p.precio();    // 18999.0

// Record con validación:
public record Email(String valor) {
    public Email {  // Compact constructor
        if (!valor.contains("@")) throw new IllegalArgumentException("Email inválido");
        valor = valor.toLowerCase().trim();
    }
}

// Record con métodos adicionales:
public record Rango(int min, int max) {
    public boolean contiene(int n) { return n >= min && n <= max; }
    public int longitud() { return max - min; }
}
```

## Pattern Matching para instanceof
```java
// ANTES (Java 15-):
if (obj instanceof String) {
    String s = (String) obj;  // Cast redundante
    System.out.println(s.length());
}

// DESPUÉS (Java 16+):
if (obj instanceof String s) {
    System.out.println(s.length());  // s ya está disponible como String
}

// Con negación:
if (!(obj instanceof String s)) {
    return;  // No es String
}
// Aquí s ya está disponible
System.out.println(s.toUpperCase());
```

---

# Java 17 (2021, LTS)

## Sealed Classes (final)
```java
// CONTROLAS exactamente quién puede extender tu clase/interface
public sealed interface Pago permits Efectivo, Tarjeta, Transferencia {}

public record Efectivo(double monto) implements Pago {}
public record Tarjeta(String numero, double monto, int cuotas) implements Pago {}
public record Transferencia(String clabe, double monto) implements Pago {}

// Beneficio: switch exhaustivo (el compilador sabe que no hay más opciones)
String procesar(Pago pago) {
    return switch (pago) {
        case Efectivo e -> "Efectivo: $" + e.monto();
        case Tarjeta t -> "Tarjeta " + t.numero() + " en " + t.cuotas() + " cuotas";
        case Transferencia tr -> "Transferencia a CLABE " + tr.clabe();
        // No necesitas default! El compilador verifica que cubriste todo
    };
}
```

## Pattern Matching en Switch (preview → final en Java 21)
```java
// Combinar tipo + condición en un switch:
String describir(Object obj) {
    return switch (obj) {
        case Integer i when i > 0 -> "Entero positivo: " + i;
        case Integer i -> "Entero: " + i;
        case String s when s.length() > 10 -> "Texto largo: " + s.substring(0, 10) + "...";
        case String s -> "Texto: " + s;
        case null -> "Null";
        default -> "Otro: " + obj.getClass().getSimpleName();
    };
}
```

---

# Java 21 (2023, LTS) — La versión ACTUAL recomendada

## Virtual Threads (final)
```java
// ANTES: Platform Threads (pesados, máximo ~2000)
// Cada thread = 1MB de stack + 1 OS thread
Thread.ofPlatform().start(() -> System.out.println("Heavy thread"));

// DESPUÉS: Virtual Threads (ultra-ligeros, MILLONES)
// Cada virtual thread = ~1KB, multiplexado en pocos OS threads
Thread.ofVirtual().start(() -> System.out.println("Light thread"));

// ExecutorService con virtual threads:
try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
    // Crear 100,000 tareas (imposible con platform threads)
    for (int i = 0; i < 100_000; i++) {
        executor.submit(() -> {
            Thread.sleep(Duration.ofSeconds(1));  // No bloquea OS thread
            return "done";
        });
    }
}

// Spring Boot 3.2+ con virtual threads:
// application.yml:
// spring.threads.virtual.enabled: true
// ¡Todas las requests HTTP se manejan con virtual threads automáticamente!
```

## Sequenced Collections
```java
// ANTES: no había forma genérica de obtener primer/último elemento
List<String> lista = List.of("a", "b", "c");
lista.get(0);                    // Primer elemento (solo List)
lista.get(lista.size() - 1);    // Último (verbose)
// LinkedHashSet no tenía forma de obtener primero/último

// DESPUÉS: interface SequencedCollection
SequencedCollection<String> seq = new LinkedHashSet<>(List.of("a", "b", "c"));
seq.getFirst();     // "a"
seq.getLast();      // "c"
seq.reversed();     // Vista invertida: ["c", "b", "a"]

// También para Map:
SequencedMap<String, Integer> map = new LinkedHashMap<>();
map.put("uno", 1); map.put("dos", 2); map.put("tres", 3);
map.firstEntry();   // "uno" → 1
map.lastEntry();    // "tres" → 3
map.reversed();     // Vista invertida
```

## Pattern Matching en Switch (final)
```java
// Ahora FINAL (no preview):
sealed interface Forma permits Circulo, Rectangulo, Triangulo {}
record Circulo(double radio) implements Forma {}
record Rectangulo(double ancho, double alto) implements Forma {}
record Triangulo(double base, double altura) implements Forma {}

double area(Forma forma) {
    return switch (forma) {
        case Circulo c -> Math.PI * c.radio() * c.radio();
        case Rectangulo r -> r.ancho() * r.alto();
        case Triangulo t -> t.base() * t.altura() / 2;
    };
}
```

## Record Patterns (deconstrucción)
```java
record Punto(int x, int y) {}
record Linea(Punto inicio, Punto fin) {}

// Deconstruir en pattern matching:
void imprimir(Object obj) {
    if (obj instanceof Punto(int x, int y)) {
        System.out.println("Punto en (" + x + ", " + y + ")");
    }
    if (obj instanceof Linea(Punto(int x1, int y1), Punto(int x2, int y2))) {
        System.out.println("Línea de (" + x1 + "," + y1 + ") a (" + x2 + "," + y2 + ")");
    }
}
```

---

# Java 22-25 (2024-2025)

## Java 22: Unnamed Variables
```java
// Cuando NO necesitas la variable (solo te interesa el efecto):
try {
    int n = Integer.parseInt(input);
} catch (NumberFormatException _) {  // No necesitas la excepción
    System.out.println("Número inválido");
}

// En streams donde no usas el parámetro:
lista.stream().map(_ -> generarId()).toList();

// En for con efecto:
for (int _ = 0; _ < 10; _++) {
    ejecutarTarea();
}
```

## Java 22: Statements Before super()
```java
// ANTES: no podías ejecutar código antes de super()
// DESPUÉS:
public class Empleado extends Persona {
    public Empleado(String nombre, double salario) {
        // Validar ANTES de llamar a super:
        if (salario < 0) throw new IllegalArgumentException("Salario inválido");
        var nombreNormalizado = nombre.trim().toUpperCase();
        super(nombreNormalizado);  // Ahora sí, después de validar
        this.salario = salario;
    }
}
```

## Java 24: Stream Gatherers (final)
```java
// Operaciones intermedias CUSTOM en streams:
// Ejemplo: sliding window de tamaño 3
List<List<Integer>> windows = Stream.of(1, 2, 3, 4, 5, 6)
    .gather(Gatherers.windowSliding(3))
    .toList();
// [[1,2,3], [2,3,4], [3,4,5], [4,5,6]]

// Fixed windows:
List<List<Integer>> fixed = Stream.of(1, 2, 3, 4, 5, 6)
    .gather(Gatherers.windowFixed(2))
    .toList();
// [[1,2], [3,4], [5,6]]

// Fold (acumulación con estado):
// Gatherer custom para agrupar consecutivos iguales
```

## Java 25 (LTS): Consolidación
```java
// Java 25 es LTS: la versión para producción a partir de 2025.
// Consolida todo lo que era preview en versiones anteriores:
// - Virtual Threads (desde 21, ya estable)
// - Pattern Matching completo (switch + instanceof + records)
// - Sealed classes + records = ADT (Algebraic Data Types)
// - Sequenced Collections
// - Stream Gatherers (desde 24)

// El stack moderno Java 25:
// Record + Sealed + Pattern Matching = código funcional expresivo
sealed interface Evento permits
    UsuarioRegistrado, CompraRealizada, PagoRecibido {}

record UsuarioRegistrado(String email, Instant fecha) implements Evento {}
record CompraRealizada(Long pedidoId, BigDecimal monto) implements Evento {}
record PagoRecibido(Long pedidoId, String metodo) implements Evento {}

String procesar(Evento evento) {
    return switch (evento) {
        case UsuarioRegistrado(var email, var fecha) ->
            "Bienvenido " + email + " (registrado " + fecha + ")";
        case CompraRealizada(var id, var monto) when monto.compareTo(BigDecimal.valueOf(10000)) > 0 ->
            "Compra VIP #" + id + ": $" + monto;
        case CompraRealizada(var id, var monto) ->
            "Compra #" + id + ": $" + monto;
        case PagoRecibido(var id, var metodo) ->
            "Pago " + metodo + " para pedido #" + id;
    };
}
```

---

# Resumen: ¿Qué usar HOY? (Java 21/25)

## Features que DEBES usar a diario:

| Feature | Desde | Ejemplo rápido |
|---------|-------|----------------|
| Lambdas | 8 | `list.forEach(System.out::println)` |
| Streams | 8 | `list.stream().filter(...).map(...).toList()` |
| Optional | 8 | `repo.findById(id).orElseThrow()` |
| java.time | 8 | `LocalDate.now()`, `Duration.between()` |
| var | 10 | `var lista = new ArrayList<String>()` |
| HttpClient | 11 | `HttpClient.newHttpClient().send(...)` |
| Text blocks | 15 | `""" multi-line text """` |
| Records | 16 | `record Dto(String nombre, int edad) {}` |
| Sealed | 17 | `sealed interface X permits A, B, C {}` |
| Pattern matching | 21 | `case String s when s.length() > 5 ->` |
| Virtual Threads | 21 | `Executors.newVirtualThreadPerTaskExecutor()` |
| Sequenced Collections | 21 | `list.getFirst()`, `list.getLast()` |

## Lo que YA NO deberías usar:

| Viejo | Nuevo | Por qué |
|-------|-------|---------|
| `java.util.Date` | `java.time.LocalDate` | Inmutable, claro, thread-safe |
| `SimpleDateFormat` | `DateTimeFormatter` | Thread-safe |
| `synchronized` blocks | `Virtual Threads` + `ReentrantLock` | Más escalable |
| `new Thread().start()` | `ExecutorService` | Mejor manejo de recursos |
| Clase DTO de 50 líneas | `record` | 1 línea |
| `instanceof` + cast manual | `instanceof Pattern` | Más conciso y seguro |
| Switch con break | Switch expression (→) | Retorna valor, sin fall-through |

---

# Cómo Ejecutar estos Ejemplos

```bash
# Verificar versión:
java -version  # Debe ser 21+

# Ejecutar archivo directamente (Java 11+):
java MiEjemplo.java

# O compilar + ejecutar:
javac MiEjemplo.java && java MiEjemplo

# JShell para probar rápido:
jshell
jshell> var lista = List.of(1, 2, 3, 4, 5);
jshell> lista.stream().filter(n -> n > 2).toList()
```
