import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.*;

/**
 * Demo ejecutable: Features de Java 8 a Java 21
 * Requisito: Java 21+
 * Compilar: javac Java8A21Features.java
 * Ejecutar: java Java8A21Features
 */
public class Java8A21Features {

    // ═══ JAVA 16: Records ═══
    record Producto(String nombre, double precio, String categoria) {
        // Compact constructor (validación)
        Producto {
            if (precio < 0) throw new IllegalArgumentException("Precio no puede ser negativo");
            nombre = nombre.trim();
        }
        // Método adicional
        double precioConIva() { return precio * 1.16; }
    }

    // ═══ JAVA 17: Sealed Classes ═══
    sealed interface MetodoPago permits Efectivo, Tarjeta, Transferencia {}
    record Efectivo(double monto) implements MetodoPago {}
    record Tarjeta(String numero, double monto, int cuotas) implements MetodoPago {}
    record Transferencia(String clabe, double monto) implements MetodoPago {}

    public static void main(String[] args) {
        System.out.println("════════════════════════════════════════");
        System.out.println("  DEMO: Java 8 → Java 21 Features");
        System.out.println("  Versión actual: " + System.getProperty("java.version"));
        System.out.println("════════════════════════════════════════\n");

        demoJava8Streams();
        demoJava8Optional();
        demoJava8DateTime();
        demoJava10Var();
        demoJava11Strings();
        demoJava14Switch();
        demoJava15TextBlocks();
        demoJava16Records();
        demoJava16PatternMatching();
        demoJava17Sealed();
        demoJava21SequencedCollections();
        demoJava21PatternSwitch();
    }

    // ═══ JAVA 8: Streams ═══
    static void demoJava8Streams() {
        System.out.println("── Java 8: Streams ──");
        var productos = List.of(
            new Producto("Laptop HP", 18999, "Electrónica"),
            new Producto("Mouse MX", 1899, "Periféricos"),
            new Producto("Monitor Dell", 12499, "Electrónica"),
            new Producto("Teclado MX", 2899, "Periféricos"),
            new Producto("Silla Ergo", 8999, "Mobiliario"),
            new Producto("SSD 1TB", 2199, "Almacenamiento")
        );

        // Filter + Map + Sort
        System.out.println("Productos > $5000 (ordenados):");
        productos.stream()
            .filter(p -> p.precio() > 5000)
            .sorted(Comparator.comparingDouble(Producto::precio).reversed())
            .forEach(p -> System.out.printf("  %s: $%,.2f%n", p.nombre(), p.precio()));

        // GroupBy
        System.out.println("\nProductos por categoría:");
        productos.stream()
            .collect(Collectors.groupingBy(Producto::categoria, Collectors.counting()))
            .forEach((cat, count) -> System.out.printf("  %s: %d productos%n", cat, count));

        // Statistics
        var stats = productos.stream().mapToDouble(Producto::precio).summaryStatistics();
        System.out.printf("\nEstadísticas: min=$%,.0f, max=$%,.0f, avg=$%,.0f, total=$%,.0f%n",
            stats.getMin(), stats.getMax(), stats.getAverage(), stats.getSum());
        System.out.println();
    }

    // ═══ JAVA 8: Optional ═══
    static void demoJava8Optional() {
        System.out.println("── Java 8: Optional ──");
        Optional<String> presente = Optional.of("Hola Mundo");
        Optional<String> vacio = Optional.empty();

        System.out.println("Presente: " + presente.map(String::toUpperCase).orElse("vacío"));
        System.out.println("Vacío: " + vacio.map(String::toUpperCase).orElse("(no hay valor)"));
        System.out.println("Filter: " + presente.filter(s -> s.length() > 5).isPresent());
        System.out.println();
    }

    // ═══ JAVA 8: Date/Time API ═══
    static void demoJava8DateTime() {
        System.out.println("── Java 8: Date/Time API ──");
        var hoy = LocalDate.now();
        var navidad = LocalDate.of(2026, 12, 25);
        var diasParaNavidad = ChronoUnit.DAYS.between(hoy, navidad);

        System.out.println("Hoy: " + hoy);
        System.out.println("Navidad: " + navidad);
        System.out.println("Días para Navidad: " + diasParaNavidad);
        System.out.println("Hace 1 mes: " + hoy.minusMonths(1));
        System.out.println("Formateado: " + hoy.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println();
    }

    // ═══ JAVA 10: var ═══
    static void demoJava10Var() {
        System.out.println("── Java 10: var (inferencia de tipo) ──");
        var nombre = "Carlos";              // String
        var precio = 18999.99;              // double
        var lista = List.of("a", "b", "c"); // List<String>
        var mapa = Map.of("x", 1, "y", 2); // Map<String, Integer>

        System.out.println("nombre (String): " + nombre);
        System.out.println("precio (double): " + precio);
        System.out.println("lista (List<String>): " + lista);
        System.out.println("mapa (Map<String,Integer>): " + mapa);
        System.out.println();
    }

    // ═══ JAVA 11: String methods ═══
    static void demoJava11Strings() {
        System.out.println("── Java 11: Nuevos métodos de String ──");
        System.out.println("isBlank('   '): " + "   ".isBlank());
        System.out.println("strip('  hola  '): '" + "  hola  ".strip() + "'");
        System.out.println("repeat('ja', 3): " + "ja".repeat(3));
        System.out.println("lines('a\\nb\\nc'): " + "a\nb\nc".lines().toList());
        System.out.println();
    }

    // ═══ JAVA 14: Switch Expressions ═══
    static void demoJava14Switch() {
        System.out.println("── Java 14: Switch Expressions ──");
        var dias = List.of("LUNES", "SABADO", "MIERCOLES");
        for (var dia : dias) {
            String tipo = switch (dia) {
                case "LUNES", "MARTES", "MIERCOLES", "JUEVES", "VIERNES" -> "Laboral";
                case "SABADO", "DOMINGO" -> "Fin de semana";
                default -> "Desconocido";
            };
            System.out.printf("  %s → %s%n", dia, tipo);
        }
        System.out.println();
    }

    // ═══ JAVA 15: Text Blocks ═══
    static void demoJava15TextBlocks() {
        System.out.println("── Java 15: Text Blocks ──");
        String json = """
                {
                  "nombre": "Carlos",
                  "edad": 30,
                  "activo": true
                }
                """;
        System.out.println("JSON con Text Block:");
        System.out.println(json);
    }

    // ═══ JAVA 16: Records ═══
    static void demoJava16Records() {
        System.out.println("── Java 16: Records ──");
        var p = new Producto("Laptop HP", 18999, "Electrónica");
        System.out.println("Record: " + p);
        System.out.println("Nombre: " + p.nombre());
        System.out.println("Precio con IVA: $" + String.format("%,.2f", p.precioConIva()));
        System.out.println("equals(): " + p.equals(new Producto("Laptop HP", 18999, "Electrónica")));
        System.out.println();
    }

    // ═══ JAVA 16: Pattern Matching instanceof ═══
    static void demoJava16PatternMatching() {
        System.out.println("── Java 16: Pattern Matching instanceof ──");
        Object[] objetos = {"Hola", 42, 3.14, List.of(1, 2, 3), null};
        for (var obj : objetos) {
            if (obj instanceof String s) {
                System.out.println("  String de " + s.length() + " chars: " + s);
            } else if (obj instanceof Integer i && i > 10) {
                System.out.println("  Integer > 10: " + i);
            } else if (obj instanceof Double d) {
                System.out.println("  Double: " + d);
            } else if (obj instanceof List<?> lista) {
                System.out.println("  List con " + lista.size() + " elementos");
            } else {
                System.out.println("  Otro: " + obj);
            }
        }
        System.out.println();
    }

    // ═══ JAVA 17: Sealed Classes + Pattern Matching ═══
    static void demoJava17Sealed() {
        System.out.println("── Java 17: Sealed Classes ──");
        List<MetodoPago> pagos = List.of(
            new Efectivo(500),
            new Tarjeta("4111-1111-1111-1111", 18999, 6),
            new Transferencia("072180002834567890", 45000)
        );

        for (var pago : pagos) {
            String descripcion = switch (pago) {
                case Efectivo e -> String.format("💵 Efectivo: $%,.2f", e.monto());
                case Tarjeta t -> String.format("💳 Tarjeta %s: $%,.2f en %d cuotas", t.numero().substring(0,4)+"...", t.monto(), t.cuotas());
                case Transferencia tr -> String.format("🏦 Transferencia CLABE %s...: $%,.2f", tr.clabe().substring(0,6), tr.monto());
            };
            System.out.println("  " + descripcion);
        }
        System.out.println();
    }

    // ═══ JAVA 21: Sequenced Collections ═══
    static void demoJava21SequencedCollections() {
        System.out.println("── Java 21: Sequenced Collections ──");
        var lista = new ArrayList<>(List.of("Primero", "Segundo", "Tercero", "Cuarto", "Último"));
        System.out.println("getFirst(): " + lista.getFirst());
        System.out.println("getLast(): " + lista.getLast());
        System.out.println("reversed(): " + lista.reversed());

        var mapa = new LinkedHashMap<String, Integer>();
        mapa.put("uno", 1); mapa.put("dos", 2); mapa.put("tres", 3);
        System.out.println("Map firstEntry(): " + mapa.firstEntry());
        System.out.println("Map lastEntry(): " + mapa.lastEntry());
        System.out.println();
    }

    // ═══ JAVA 21: Pattern Matching en Switch (final) ═══
    static void demoJava21PatternSwitch() {
        System.out.println("── Java 21: Pattern Matching Switch (final) ──");
        Object[] valores = {42, "Java 21", 3.14, List.of(1,2,3), null, true};
        for (var v : valores) {
            String resultado = switch (v) {
                case Integer i when i > 100 -> "Número grande: " + i;
                case Integer i -> "Número: " + i;
                case String s when s.contains("Java") -> "Java version: " + s;
                case String s -> "Texto: " + s;
                case Double d -> "Decimal: " + d;
                case List<?> l -> "Lista de " + l.size() + " elementos";
                case null -> "Es null";
                default -> "Otro tipo: " + v.getClass().getSimpleName();
            };
            System.out.println("  " + resultado);
        }
        System.out.println("\n═══ FIN DEL DEMO ═══");
    }
}
