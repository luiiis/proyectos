import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.time.*;
import java.time.format.*;

/**
 * SOLUCIONES - Ejercicios B051 a B080 (Streams, Lambdas, Fechas)
 * Compilar: javac soluciones-basicos-B051-B080.java
 * Ejecutar: java SolucionesStreams
 */
public class SolucionesStreams {

    record Producto(String nombre, double precio, String categoria, int stock) {}
    record Venta(String producto, double monto, LocalDate fecha, String vendedor) {}
    record Empleado(String nombre, String depto, double salario, LocalDate ingreso) {}

    public static void main(String[] args) {
        System.out.println("═══ SOLUCIONES B051-B080: Streams + Lambdas + Fechas ═══\n");

        var productos = List.of(
            new Producto("Laptop HP", 18999, "Electrónica", 25),
            new Producto("Mouse MX", 1899, "Periféricos", 50),
            new Producto("Monitor Dell", 12499, "Electrónica", 15),
            new Producto("Teclado MX", 2899, "Periféricos", 40),
            new Producto("Silla Ergo", 28500, "Mobiliario", 8),
            new Producto("SSD 1TB", 2199, "Almacenamiento", 30),
            new Producto("Cable HDMI", 299, "Periféricos", 100),
            new Producto("Webcam", 5999, "Periféricos", 20),
            new Producto("iPad", 15999, "Electrónica", 12),
            new Producto("Escritorio", 9500, "Mobiliario", 5)
        );

        // B051: Filtrar y ordenar
        System.out.println("── B051: Productos < $3000 ordenados por precio ──");
        productos.stream()
            .filter(p -> p.precio() < 3000)
            .sorted(Comparator.comparingDouble(Producto::precio))
            .forEach(p -> System.out.printf("  %s: $%,.0f%n", p.nombre(), p.precio()));

        // B052: Precio total del inventario (precio × stock)
        System.out.println("\n── B052: Valor total del inventario ──");
        double valorTotal = productos.stream()
            .mapToDouble(p -> p.precio() * p.stock())
            .sum();
        System.out.printf("  $%,.2f%n", valorTotal);

        // B053: Producto más caro y más barato
        System.out.println("\n── B053: Extremos ──");
        productos.stream().max(Comparator.comparingDouble(Producto::precio))
            .ifPresent(p -> System.out.println("  Más caro: " + p.nombre() + " $" + p.precio()));
        productos.stream().min(Comparator.comparingDouble(Producto::precio))
            .ifPresent(p -> System.out.println("  Más barato: " + p.nombre() + " $" + p.precio()));

        // B054: Nombres en mayúsculas separados por coma
        System.out.println("\n── B054: Nombres concatenados ──");
        String nombres = productos.stream()
            .map(p -> p.nombre().toUpperCase())
            .collect(Collectors.joining(", "));
        System.out.println("  " + nombres);

        // B055: Contar por categoría
        System.out.println("\n── B055: Productos por categoría ──");
        productos.stream()
            .collect(Collectors.groupingBy(Producto::categoria, Collectors.counting()))
            .forEach((cat, n) -> System.out.println("  " + cat + ": " + n));

        // B056: Promedio de precios por categoría
        System.out.println("\n── B056: Precio promedio por categoría ──");
        productos.stream()
            .collect(Collectors.groupingBy(Producto::categoria, Collectors.averagingDouble(Producto::precio)))
            .forEach((cat, avg) -> System.out.printf("  %s: $%,.0f%n", cat, avg));

        // B057: Categoría con producto más caro
        System.out.println("\n── B057: Más caro por categoría ──");
        productos.stream()
            .collect(Collectors.groupingBy(Producto::categoria,
                Collectors.maxBy(Comparator.comparingDouble(Producto::precio))))
            .forEach((cat, p) -> System.out.printf("  %s: %s ($%,.0f)%n", cat, p.get().nombre(), p.get().precio()));

        // B058: Productos con stock < 15 (alertas)
        System.out.println("\n── B058: Stock bajo (< 15) ──");
        productos.stream()
            .filter(p -> p.stock() < 15)
            .sorted(Comparator.comparingInt(Producto::stock))
            .forEach(p -> System.out.printf("  ⚠️ %s: %d unidades%n", p.nombre(), p.stock()));

        // B059: Partitioning (caros vs baratos)
        System.out.println("\n── B059: Partición (> $5000 vs ≤ $5000) ──");
        var particion = productos.stream()
            .collect(Collectors.partitioningBy(p -> p.precio() > 5000));
        System.out.println("  Caros: " + particion.get(true).stream().map(Producto::nombre).toList());
        System.out.println("  Baratos: " + particion.get(false).stream().map(Producto::nombre).toList());

        // B060: Estadísticas completas
        System.out.println("\n── B060: Estadísticas de precios ──");
        var stats = productos.stream().mapToDouble(Producto::precio).summaryStatistics();
        System.out.printf("  Count=%d, Min=$%,.0f, Max=$%,.0f, Avg=$%,.0f, Sum=$%,.0f%n",
            stats.getCount(), stats.getMin(), stats.getMax(), stats.getAverage(), stats.getSum());

        // ═══ LAMBDAS AVANZADAS (B061-B070) ═══

        // B061: Composición de Predicates
        System.out.println("\n── B061: Composición de Predicates ──");
        Predicate<Producto> esElectronica = p -> p.categoria().equals("Electrónica");
        Predicate<Producto> esCaro = p -> p.precio() > 10000;
        Predicate<Producto> esElectronicaCara = esElectronica.and(esCaro);

        productos.stream().filter(esElectronicaCara)
            .forEach(p -> System.out.println("  " + p.nombre()));

        // B062: Function composition
        System.out.println("\n── B062: Composición de Functions ──");
        Function<Double, Double> aplicarDescuento = precio -> precio * 0.85;
        Function<Double, Double> aplicarIva = precio -> precio * 1.16;
        Function<Double, String> formatear = precio -> String.format("$%,.2f", precio);

        Function<Double, String> precioFinal = aplicarDescuento.andThen(aplicarIva).andThen(formatear);
        System.out.println("  $10,000 con descuento + IVA = " + precioFinal.apply(10000.0));

        // B063: Comparator encadenado
        System.out.println("\n── B063: Ordenamiento multi-criterio ──");
        productos.stream()
            .sorted(Comparator.comparing(Producto::categoria)
                .thenComparing(Comparator.comparingDouble(Producto::precio).reversed()))
            .forEach(p -> System.out.printf("  [%s] %s: $%,.0f%n", p.categoria(), p.nombre(), p.precio()));

        // B064: Collector custom (joining con formato)
        System.out.println("\n── B064: Collector custom ──");
        String catalogo = productos.stream()
            .map(p -> String.format("%s ($%,.0f)", p.nombre(), p.precio()))
            .collect(Collectors.joining(" | ", "📦 ", " 📦"));
        System.out.println("  " + catalogo);

        // ═══ FECHAS (B071-B080) ═══

        System.out.println("\n── B071-B080: java.time ──");
        var hoy = LocalDate.now();

        // B071: Fecha actual formateada
        System.out.println("  Hoy: " + hoy.format(DateTimeFormatter.ofPattern("EEEE dd 'de' MMMM yyyy", Locale.forLanguageTag("es"))));

        // B072: Días para fin de año
        var finAnio = LocalDate.of(hoy.getYear(), 12, 31);
        System.out.println("  Días para fin de año: " + ChronoUnit.DAYS.between(hoy, finAnio));

        // B073: Generar fechas del mes actual
        System.out.println("  Días del mes actual: " + hoy.lengthOfMonth());

        // B074: ¿Es año bisiesto?
        System.out.println("  ¿2024 es bisiesto? " + Year.of(2024).isLeap());
        System.out.println("  ¿2025 es bisiesto? " + Year.of(2025).isLeap());

        // B075: Edad a partir de fecha de nacimiento
        var nacimiento = LocalDate.of(1995, 6, 15);
        var edad = Period.between(nacimiento, hoy);
        System.out.printf("  Nacido %s → %d años, %d meses%n", nacimiento, edad.getYears(), edad.getMonths());

        // B076: Siguiente lunes
        var proximoLunes = hoy.with(java.time.temporal.TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.println("  Próximo lunes: " + proximoLunes);

        // B077: Diferencia entre dos timestamps
        var inicio = LocalDateTime.of(2026, 7, 21, 9, 0);
        var fin = LocalDateTime.of(2026, 7, 21, 17, 30);
        var duracion = Duration.between(inicio, fin);
        System.out.printf("  Jornada: %dh %dmin%n", duracion.toHours(), duracion.toMinutesPart());

        // B078: Parsear fecha en formato español
        var formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var parsed = LocalDate.parse("25/12/2026", formatter);
        System.out.println("  Parseado '25/12/2026': " + parsed);

        // B079: Ventas agrupadas por mes
        var ventas = List.of(
            new Venta("Laptop", 18999, LocalDate.of(2026, 1, 15), "Carlos"),
            new Venta("Mouse", 1899, LocalDate.of(2026, 1, 20), "María"),
            new Venta("Monitor", 12499, LocalDate.of(2026, 2, 5), "Carlos"),
            new Venta("Teclado", 2899, LocalDate.of(2026, 2, 18), "Juan"),
            new Venta("SSD", 2199, LocalDate.of(2026, 3, 1), "María")
        );

        System.out.println("\n  Ventas por mes:");
        ventas.stream()
            .collect(Collectors.groupingBy(v -> v.fecha().getMonth(), Collectors.summingDouble(Venta::monto)))
            .forEach((mes, total) -> System.out.printf("    %s: $%,.0f%n", mes, total));

        // B080: Empleados por antigüedad
        var empleados = List.of(
            new Empleado("Carlos", "Tech", 55000, LocalDate.of(2020, 3, 15)),
            new Empleado("María", "Tech", 48000, LocalDate.of(2022, 7, 1)),
            new Empleado("Juan", "Ventas", 42000, LocalDate.of(2019, 1, 10)),
            new Empleado("Ana", "RRHH", 38000, LocalDate.of(2023, 11, 20))
        );

        System.out.println("\n  Empleados por antigüedad:");
        empleados.stream()
            .sorted(Comparator.comparing(Empleado::ingreso))
            .forEach(e -> {
                var antig = Period.between(e.ingreso(), hoy);
                System.out.printf("    %s: %d años %d meses (%s)%n", e.nombre(), antig.getYears(), antig.getMonths(), e.ingreso());
            });

        System.out.println("\n═══ FIN ═══");
    }
}
