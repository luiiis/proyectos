import java.util.*;
import java.util.stream.*;
import java.math.BigDecimal;

/**
 * MÓDULO 08: Streams - Demo ejecutable
 * Ejecutar: javac StreamsDemo.java && java StreamsDemo
 */
public class StreamsDemo {
    record Producto(String nombre, double precio, String categoria, int stock) {}

    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 08: Stream API ═══\n");

        List<Producto> productos = List.of(
            new Producto("Laptop HP", 18999, "Electrónica", 25),
            new Producto("Mouse Logitech", 899, "Periféricos", 50),
            new Producto("Monitor Dell", 12499, "Electrónica", 15),
            new Producto("Teclado MX", 2899, "Periféricos", 40),
            new Producto("Silla Herman", 28500, "Mobiliario", 8),
            new Producto("SSD Samsung", 2199, "Almacenamiento", 30),
            new Producto("Webcam Elgato", 5999, "Periféricos", 20)
        );

        // filter + map
        System.out.println("── Productos > $5000 ──");
        productos.stream()
            .filter(p -> p.precio() > 5000)
            .map(p -> p.nombre() + " ($" + p.precio() + ")")
            .forEach(System.out::println);

        // reduce: suma total
        double total = productos.stream().mapToDouble(Producto::precio).sum();
        System.out.printf("\n── Total inventario: $%,.2f ──%n", total);

        // groupingBy: agrupar por categoría
        System.out.println("\n── Productos por categoría ──");
        Map<String, List<Producto>> porCategoria = productos.stream()
            .collect(Collectors.groupingBy(Producto::categoria));
        porCategoria.forEach((cat, prods) ->
            System.out.println("  " + cat + ": " + prods.size() + " productos"));

        // Promedio por categoría
        System.out.println("\n── Precio promedio por categoría ──");
        productos.stream()
            .collect(Collectors.groupingBy(Producto::categoria, Collectors.averagingDouble(Producto::precio)))
            .forEach((cat, avg) -> System.out.printf("  %s: $%,.2f%n", cat, avg));

        // sorted + limit: top 3 más caros
        System.out.println("\n── Top 3 más caros ──");
        productos.stream()
            .sorted(Comparator.comparingDouble(Producto::precio).reversed())
            .limit(3)
            .forEach(p -> System.out.printf("  %s: $%,.2f%n", p.nombre(), p.precio()));

        // anyMatch, allMatch
        boolean hayBaratos = productos.stream().anyMatch(p -> p.precio() < 1000);
        boolean todosConStock = productos.stream().allMatch(p -> p.stock() > 0);
        System.out.println("\n¿Hay productos < $1000? " + hayBaratos);
        System.out.println("¿Todos tienen stock? " + todosConStock);
    }
}
