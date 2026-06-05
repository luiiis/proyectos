/**
 * EJERCICIO 03: FizzBuzz (pregunta clásica de entrevistas)
 * 
 * Reglas:
 * - Si el número es múltiplo de 3: imprime "Fizz"
 * - Si es múltiplo de 5: imprime "Buzz"
 * - Si es múltiplo de ambos: imprime "FizzBuzz"
 * - Si no es múltiplo de ninguno: imprime el número
 * 
 * Ejecución: javac Ejercicio03_FizzBuzz.java && java Ejercicio03_FizzBuzz
 */
public class Ejercicio03_FizzBuzz {

    public static void main(String[] args) {
        System.out.println("═══ FIZZBUZZ (1-100) ═══\n");

        for (int i = 1; i <= 100; i++) {
            if (i % 15 == 0) {          // Múltiplo de 3 Y 5 (verificar primero)
                System.out.printf("%3d → FizzBuzz%n", i);
            } else if (i % 3 == 0) {    // Solo múltiplo de 3
                System.out.printf("%3d → Fizz%n", i);
            } else if (i % 5 == 0) {    // Solo múltiplo de 5
                System.out.printf("%3d → Buzz%n", i);
            } else {
                System.out.printf("%3d%n", i);
            }
        }

        // ═══════ VERSIÓN SENIOR (más elegante) ═══════
        System.out.println("\n═══ VERSIÓN CON STREAMS ═══\n");
        java.util.stream.IntStream.rangeClosed(1, 30).forEach(i -> {
            String result = (i % 3 == 0 ? "Fizz" : "") + (i % 5 == 0 ? "Buzz" : "");
            System.out.println(i + " → " + (result.isEmpty() ? String.valueOf(i) : result));
        });
    }
}
