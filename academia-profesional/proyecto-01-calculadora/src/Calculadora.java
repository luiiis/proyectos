import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Proyecto 01: Calculadora Java
 * Demuestra: variables, funciones, switch, records, ArrayList, manejo de errores.
 */
public class Calculadora {

    // Record: contenedor inmutable para cada operación del historial
    record Operacion(double num1, String operador, double num2, double resultado) {
        @Override
        public String toString() {
            return String.format("%.2f %s %.2f = %.2f", num1, operador, num2, resultado);
        }
    }

    // Historial de operaciones
    private static final List<Operacion> historial = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean ejecutando = true;

        System.out.println("═══════════════════════════════════");
        System.out.println("  CALCULADORA JAVA - Proyecto 01");
        System.out.println("═══════════════════════════════════");

        while (ejecutando) {
            mostrarMenu();
            int opcion = leerOpcion(scanner);

            switch (opcion) {
                case 1, 2, 3, 4, 5 -> ejecutarOperacion(scanner, opcion);
                case 6 -> mostrarHistorial();
                case 7 -> {
                    ejecutando = false;
                    System.out.println("\n¡Hasta luego! Operaciones realizadas: " + historial.size());
                }
                default -> System.out.println("⚠️ Opción no válida. Intenta de nuevo.");
            }
        }
        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n┌─────────────────────┐");
        System.out.println("│ 1. Sumar (+)        │");
        System.out.println("│ 2. Restar (-)       │");
        System.out.println("│ 3. Multiplicar (×)  │");
        System.out.println("│ 4. Dividir (÷)      │");
        System.out.println("│ 5. Potencia (^)     │");
        System.out.println("│ 6. Ver historial    │");
        System.out.println("│ 7. Salir            │");
        System.out.println("└─────────────────────┘");
        System.out.print("Opción: ");
    }

    private static int leerOpcion(Scanner scanner) {
        try {
            return scanner.nextInt();
        } catch (InputMismatchException e) {
            scanner.nextLine(); // Limpiar buffer
            return -1;
        }
    }

    private static void ejecutarOperacion(Scanner scanner, int opcion) {
        try {
            System.out.print("Primer número: ");
            double num1 = scanner.nextDouble();
            System.out.print("Segundo número: ");
            double num2 = scanner.nextDouble();

            // Switch expression (Java 14+): retorna un valor directamente
            String operador = switch (opcion) {
                case 1 -> "+";
                case 2 -> "-";
                case 3 -> "×";
                case 4 -> "÷";
                case 5 -> "^";
                default -> "?";
            };

            double resultado = calcular(num1, num2, opcion);

            // Guardar en historial
            var op = new Operacion(num1, operador, num2, resultado);
            historial.add(op);

            System.out.printf("\n✓ Resultado: %s%n", op);

        } catch (InputMismatchException e) {
            System.out.println("⚠️ Error: Ingresa un número válido.");
            scanner.nextLine();
        } catch (ArithmeticException e) {
            System.out.println("⚠️ Error: " + e.getMessage());
        }
    }

    private static double calcular(double a, double b, int operacion) {
        return switch (operacion) {
            case 1 -> a + b;
            case 2 -> a - b;
            case 3 -> a * b;
            case 4 -> {
                if (b == 0) throw new ArithmeticException("No se puede dividir por cero");
                yield a / b;
            }
            case 5 -> Math.pow(a, b);
            default -> throw new IllegalArgumentException("Operación no válida");
        };
    }

    private static void mostrarHistorial() {
        if (historial.isEmpty()) {
            System.out.println("\n📋 Historial vacío. Realiza alguna operación primero.");
            return;
        }
        System.out.println("\n📋 Historial de operaciones:");
        System.out.println("─────────────────────────────");
        for (int i = 0; i < historial.size(); i++) {
            System.out.printf("  %d. %s%n", i + 1, historial.get(i));
        }
        System.out.println("─────────────────────────────");
        System.out.println("Total: " + historial.size() + " operaciones");
    }
}
