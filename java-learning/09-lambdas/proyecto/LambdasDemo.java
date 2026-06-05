import java.util.*;
import java.util.function.*;

/**
 * MÓDULO 09: Lambdas - Demo ejecutable
 * Ejecutar: javac LambdasDemo.java && java LambdasDemo
 */
public class LambdasDemo {
    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 09: Lambdas y Functional Interfaces ═══\n");

        // Predicate: T → boolean
        Predicate<Integer> esPar = n -> n % 2 == 0;
        Predicate<String> esLargo = s -> s.length() > 5;
        System.out.println("4 es par: " + esPar.test(4));
        System.out.println("'Hola' es largo: " + esLargo.test("Hola"));

        // Function: T → R
        Function<String, Integer> longitud = String::length;
        Function<Double, String> formatear = precio -> String.format("$%,.2f", precio);
        System.out.println("\nLongitud 'Java': " + longitud.apply("Java"));
        System.out.println("Formato: " + formatear.apply(18999.99));

        // Consumer: T → void
        Consumer<String> imprimir = msg -> System.out.println("  → " + msg);
        List.of("Hola", "Mundo", "Java").forEach(imprimir);

        // Supplier: () → T
        Supplier<String> timestamp = () -> java.time.Instant.now().toString();
        System.out.println("\nTimestamp: " + timestamp.get());

        // Comparator con lambdas
        List<String> nombres = new ArrayList<>(List.of("Carlos", "Ana", "María", "Juan"));
        nombres.sort(Comparator.comparingInt(String::length).thenComparing(Comparator.naturalOrder()));
        System.out.println("\nOrdenados por longitud: " + nombres);

        // Composición de funciones
        Function<Integer, Integer> duplicar = x -> x * 2;
        Function<Integer, Integer> sumarUno = x -> x + 1;
        Function<Integer, Integer> duplicarYSumar = duplicar.andThen(sumarUno);
        System.out.println("\nduplicar(5) + 1 = " + duplicarYSumar.apply(5)); // 11
    }
}
