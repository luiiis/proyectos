# Java Cheatsheet — Referencia Rápida

## Tipos de Datos
```java
int n = 42;           double d = 3.14;       boolean b = true;
String s = "Hola";    var x = "inferido";    final int C = 100;
```

## Colecciones
```java
List<String> list = new ArrayList<>(List.of("a","b","c"));
Set<Integer> set = new HashSet<>(Set.of(1,2,3));
Map<String,Integer> map = new HashMap<>(Map.of("a",1,"b",2));
Queue<String> queue = new LinkedList<>();

list.add("d");  list.get(0);  list.size();  list.contains("a");
map.put("c",3); map.get("a"); map.containsKey("a"); map.getOrDefault("x",0);
```

## Streams (Java 8+)
```java
list.stream()
    .filter(s -> s.length() > 3)      // Filtrar
    .map(String::toUpperCase)          // Transformar
    .sorted()                          // Ordenar
    .distinct()                        // Eliminar duplicados
    .limit(10)                         // Limitar
    .toList();                         // Recolectar (Java 16+)

// Reducir
double sum = list.stream().mapToDouble(Item::precio).sum();
Optional<Item> max = list.stream().max(Comparator.comparing(Item::precio));

// Agrupar
Map<String, List<Item>> groups = list.stream().collect(Collectors.groupingBy(Item::categoria));
Map<String, Double> avgs = list.stream().collect(Collectors.groupingBy(Item::cat, Collectors.averagingDouble(Item::precio)));
```

## Optional
```java
Optional.of(valor);           Optional.ofNullable(nullable);     Optional.empty();
opt.isPresent();              opt.ifPresent(v -> uso(v));
opt.orElse(default);          opt.orElseThrow(() -> new Ex());
opt.map(v -> transform);      opt.filter(v -> condition);
```

## Records (Java 16+)
```java
record Producto(String nombre, double precio) {
    Producto { if (precio < 0) throw new IllegalArgumentException(); }
    double conIva() { return precio * 1.16; }
}
var p = new Producto("Laptop", 18999);  p.nombre();  p.precio();
```

## Pattern Matching (Java 21)
```java
if (obj instanceof String s) { s.length(); }

String result = switch (obj) {
    case Integer i when i > 0 -> "positivo: " + i;
    case String s -> "texto: " + s;
    case null -> "null";
    default -> "otro";
};
```

## Sealed Classes (Java 17+)
```java
sealed interface Pago permits Efectivo, Tarjeta {}
record Efectivo(double monto) implements Pago {}
record Tarjeta(String num, double monto) implements Pago {}
```

## Fechas (java.time)
```java
LocalDate.now();  LocalDate.of(2026,12,25);  LocalDateTime.now();
hoy.plusDays(7);  hoy.minusMonths(1);
ChronoUnit.DAYS.between(a, b);  Period.between(a, b);
hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
```

## Excepciones
```java
try { ... }
catch (IOException | SQLException e) { log(e); }
finally { cleanup(); }

try (var reader = Files.newBufferedReader(path)) { ... }  // Auto-close
throw new RuntimeException("msg");
```

## Lambdas + Functional Interfaces
```java
Predicate<T>   → T → boolean      → list.removeIf(p -> p.precio() == 0)
Function<T,R>  → T → R            → list.stream().map(Producto::nombre)
Consumer<T>    → T → void         → list.forEach(System.out::println)
Supplier<T>    → () → T           → Optional.orElseGet(() -> new Obj())
Comparator<T>  → (T,T) → int     → list.sort(Comparator.comparing(P::precio))
```

## Spring Boot Anotaciones Clave
```java
@SpringBootApplication       // Main class
@RestController              // Controller REST (devuelve JSON)
@Service                     // Lógica de negocio
@Repository                  // Acceso a datos
@Entity @Table(name="x")    // Entidad JPA
@Id @GeneratedValue          // Primary Key auto-increment

@GetMapping("/api/x")        // GET endpoint
@PostMapping("/api/x")       // POST endpoint
@PathVariable Long id        // /api/x/{id}
@RequestParam String q       // /api/x?q=valor
@RequestBody Dto dto         // JSON body

@Transactional               // Transacción ACID
@Cacheable("name")           // Cache resultado
@Valid                       // Activar validación
@PreAuthorize("hasRole()")   // Seguridad
```

## Docker
```dockerfile
FROM eclipse-temurin:21-jre-alpine
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```yaml
# docker-compose.yml
services:
  app:
    build: .
    ports: ["8080:8080"]
    depends_on:
      db: { condition: service_healthy }
  db:
    image: postgres:16-alpine
    environment: { POSTGRES_PASSWORD: pass123 }
```

## Comandos Maven
```bash
mvn clean compile          # Compilar
mvn test                   # Ejecutar tests
mvn package                # Crear JAR
mvn spring-boot:run        # Ejecutar Spring Boot
mvn dependency:tree        # Ver dependencias
```

## Git (lo esencial)
```bash
git add . && git commit -m "feat: nueva feature"
git push -u origin mi-rama
git pull origin main
git log --oneline -10
```
