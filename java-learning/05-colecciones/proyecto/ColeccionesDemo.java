import java.util.*;
import java.util.stream.Collectors;

/**
 * MÓDULO 05: Colecciones - Demo ejecutable
 * Ejecutar: javac ColeccionesDemo.java && java ColeccionesDemo
 */
public class ColeccionesDemo {
    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 05: Colecciones ═══\n");

        // ArrayList: acceso rápido por índice O(1)
        List<String> productos = new ArrayList<>(List.of("Laptop", "Mouse", "Monitor", "Teclado", "SSD"));
        productos.add("Webcam");
        System.out.println("ArrayList: " + productos);
        System.out.println("Elemento 0: " + productos.get(0));

        // HashMap: búsqueda por clave O(1)
        Map<String, Double> precios = new HashMap<>();
        precios.put("Laptop", 18999.99);
        precios.put("Mouse", 899.00);
        precios.put("Monitor", 12499.00);
        System.out.println("\nHashMap precios: " + precios);
        System.out.println("Precio Laptop: $" + precios.get("Laptop"));

        // HashSet: elementos únicos, sin orden
        Set<String> ciudades = new HashSet<>(List.of("CDMX", "Monterrey", "CDMX", "Guadalajara", "CDMX"));
        System.out.println("\nHashSet (sin duplicados): " + ciudades);

        // TreeMap: claves ordenadas
        Map<String, Integer> stock = new TreeMap<>(Map.of("Zebra", 5, "Apple", 20, "Monitor", 15));
        System.out.println("\nTreeMap (ordenado): " + stock);

        // Queue: FIFO
        Queue<String> cola = new LinkedList<>();
        cola.offer("Cliente 1");
        cola.offer("Cliente 2");
        cola.offer("Cliente 3");
        System.out.println("\nQueue: " + cola);
        System.out.println("Atendido: " + cola.poll());
        System.out.println("Siguiente: " + cola.peek());

        // Ordenar con Comparator
        List<Map.Entry<String, Double>> ordenados = new ArrayList<>(precios.entrySet());
        ordenados.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        System.out.println("\nProductos por precio (desc):");
        ordenados.forEach(e -> System.out.printf("  %s: $%.2f%n", e.getKey(), e.getValue()));
    }
}
