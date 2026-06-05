import java.util.*;
import java.util.stream.*;
import java.time.LocalDateTime;

/**
 * Proyecto 03: Sistema Inventario - Colecciones Java
 * Demuestra: HashMap, ArrayList, TreeMap, Comparator, Streams, Optional
 * Ejecutar: javac SistemaInventario.java && java SistemaInventario
 */
public class SistemaInventario {

    record Producto(String sku, String nombre, double precio, String categoria) {}
    record Movimiento(String sku, String tipo, int cantidad, LocalDateTime fecha, String motivo) {}

    private final Map<String, Producto> productos = new HashMap<>();
    private final Map<String, Integer> stock = new HashMap<>();
    private final List<Movimiento> movimientos = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("  SISTEMA INVENTARIO - Proyecto 03");
        System.out.println("═══════════════════════════════════════\n");

        var sistema = new SistemaInventario();

        // Registrar productos
        sistema.registrar(new Producto("LAP-001", "Laptop HP", 18999, "Electrónica"));
        sistema.registrar(new Producto("MON-001", "Monitor Dell 27\"", 12499, "Electrónica"));
        sistema.registrar(new Producto("TEC-001", "Teclado MX Keys", 2899, "Periféricos"));
        sistema.registrar(new Producto("MOU-001", "Mouse MX Master", 1899, "Periféricos"));
        sistema.registrar(new Producto("SIL-001", "Silla Ergonómica", 8999, "Mobiliario"));
        sistema.registrar(new Producto("SSD-001", "SSD Samsung 1TB", 2199, "Almacenamiento"));

        // Entradas de inventario
        sistema.entrada("LAP-001", 25, "Compra proveedor HP");
        sistema.entrada("MON-001", 15, "Compra proveedor Dell");
        sistema.entrada("TEC-001", 40, "Compra proveedor Logitech");
        sistema.entrada("MOU-001", 50, "Compra proveedor Logitech");
        sistema.entrada("SIL-001", 8, "Compra proveedor mobiliario");
        sistema.entrada("SSD-001", 30, "Compra proveedor Samsung");

        // Salidas
        sistema.salida("LAP-001", 3, "Venta #V-001");
        sistema.salida("TEC-001", 5, "Venta #V-002");
        sistema.salida("MOU-001", 10, "Venta #V-003");

        // Reportes
        sistema.reporteStock();
        sistema.reporteStockBajo(12);
        sistema.reportePorCategoria();
        sistema.reporteMovimientos("LAP-001");
        sistema.valorInventario();
    }

    void registrar(Producto p) {
        productos.put(p.sku(), p);
        stock.put(p.sku(), 0);
        System.out.println("✓ Registrado: " + p.nombre());
    }

    void entrada(String sku, int cantidad, String motivo) {
        stock.merge(sku, cantidad, Integer::sum);
        movimientos.add(new Movimiento(sku, "ENTRADA", cantidad, LocalDateTime.now(), motivo));
    }

    void salida(String sku, int cantidad, String motivo) {
        int actual = stock.getOrDefault(sku, 0);
        if (actual < cantidad) {
            System.out.println("✗ Stock insuficiente para " + sku + " (disponible: " + actual + ")");
            return;
        }
        stock.merge(sku, -cantidad, Integer::sum);
        movimientos.add(new Movimiento(sku, "SALIDA", cantidad, LocalDateTime.now(), motivo));
    }

    void reporteStock() {
        System.out.println("\n── Reporte de Stock ──");
        // TreeMap para ordenar por nombre
        new TreeMap<>(stock).forEach((sku, qty) -> {
            var p = productos.get(sku);
            System.out.printf("  %-10s %-25s Stock: %d%n", sku, p.nombre(), qty);
        });
    }

    void reporteStockBajo(int minimo) {
        System.out.println("\n── Stock Bajo (< " + minimo + ") ──");
        stock.entrySet().stream()
            .filter(e -> e.getValue() < minimo)
            .sorted(Map.Entry.comparingByValue())
            .forEach(e -> {
                var p = productos.get(e.getKey());
                System.out.printf("  ⚠️ %s: %d unidades%n", p.nombre(), e.getValue());
            });
    }

    void reportePorCategoria() {
        System.out.println("\n── Productos por Categoría ──");
        productos.values().stream()
            .collect(Collectors.groupingBy(Producto::categoria, Collectors.counting()))
            .forEach((cat, count) -> System.out.printf("  %s: %d productos%n", cat, count));
    }

    void reporteMovimientos(String sku) {
        var p = productos.get(sku);
        System.out.println("\n── Movimientos: " + p.nombre() + " ──");
        movimientos.stream()
            .filter(m -> m.sku().equals(sku))
            .forEach(m -> System.out.printf("  [%s] %+d - %s%n",
                m.tipo(), m.tipo().equals("ENTRADA") ? m.cantidad() : -m.cantidad(), m.motivo()));
    }

    void valorInventario() {
        double total = stock.entrySet().stream()
            .mapToDouble(e -> productos.get(e.getKey()).precio() * e.getValue())
            .sum();
        System.out.printf("%n── Valor Total del Inventario: $%,.2f ──%n", total);
    }
}
