import java.util.*;

/**
 * MÓDULO 07: Generics - Demo ejecutable
 * Ejecutar: javac GenericsDemo.java && java GenericsDemo
 */
public class GenericsDemo {
    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 07: Generics ═══\n");

        // Clase genérica: Resultado<T>
        Resultado<String> exito = Resultado.ok("Producto creado exitosamente");
        Resultado<String> error = Resultado.error("Producto no encontrado");
        System.out.println("Éxito: " + exito);
        System.out.println("Error: " + error);

        // Método genérico
        List<Integer> numeros = List.of(3, 1, 4, 1, 5, 9);
        System.out.println("\nMáximo: " + maximo(numeros));

        List<String> palabras = List.of("java", "spring", "angular");
        System.out.println("Máximo string: " + maximo(palabras));

        // Par genérico
        Par<String, Double> producto = new Par<>("Laptop", 18999.99);
        System.out.println("\nPar: " + producto.primero() + " = $" + producto.segundo());
    }

    // Método genérico con bounded type
    static <T extends Comparable<T>> T maximo(List<T> lista) {
        return lista.stream().max(Comparable::compareTo).orElseThrow();
    }
}

// Clase genérica
class Resultado<T> {
    private final boolean exito;
    private final T datos;
    private final String error;

    private Resultado(boolean exito, T datos, String error) {
        this.exito = exito; this.datos = datos; this.error = error;
    }
    static <T> Resultado<T> ok(T datos) { return new Resultado<>(true, datos, null); }
    static <T> Resultado<T> error(String msg) { return new Resultado<>(false, null, msg); }

    public String toString() { return exito ? "OK: " + datos : "ERROR: " + error; }
}

// Record genérico (Java 16+)
record Par<A, B>(A primero, B segundo) {}
