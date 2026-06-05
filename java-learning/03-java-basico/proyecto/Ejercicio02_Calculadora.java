import java.util.Scanner;

/**
 * EJERCICIO 02: Calculadora con Scanner y Switch
 * 
 * Conceptos: entrada de datos (Scanner), switch moderno (Java 14+), validación.
 * 
 * Ejecución: javac Ejercicio02_Calculadora.java && java Ejercicio02_Calculadora
 */
public class Ejercicio02_Calculadora {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("═══ CALCULADORA JAVA ═══");
        System.out.print("Primer número: ");
        double num1 = scanner.nextDouble();

        System.out.print("Operación (+, -, *, /): ");
        String operacion = scanner.next();

        System.out.print("Segundo número: ");
        double num2 = scanner.nextDouble();

        // Switch moderno (Java 14+) - sin break, con arrow
        String resultado = switch (operacion) {
            case "+" -> String.format("%.2f + %.2f = %.2f", num1, num2, num1 + num2);
            case "-" -> String.format("%.2f - %.2f = %.2f", num1, num2, num1 - num2);
            case "*" -> String.format("%.2f × %.2f = %.2f", num1, num2, num1 * num2);
            case "/" -> {
                if (num2 == 0) {
                    yield "Error: División por cero";
                }
                yield String.format("%.2f ÷ %.2f = %.2f", num1, num2, num1 / num2);
            }
            default -> "Operación no válida: " + operacion;
        };

        System.out.println("\nResultado: " + resultado);
        scanner.close();
    }
}
