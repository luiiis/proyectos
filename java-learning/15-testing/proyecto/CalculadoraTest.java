/**
 * MÓDULO 15: Testing - Demo (requiere JUnit 5 + Maven/Gradle para ejecutar)
 * Este archivo muestra la ESTRUCTURA de tests. Para ejecutar realmente,
 * usa el proyecto Maven del módulo 19 o 30.
 *
 * Ejecutar con Maven: mvn test
 */

// import org.junit.jupiter.api.*;
// import static org.junit.jupiter.api.Assertions.*;

/**
 * Ejemplo de tests unitarios con JUnit 5.
 * Patrón AAA: Arrange → Act → Assert
 */
public class CalculadoraTest {

    // La clase que vamos a testear
    static class Calculadora {
        double sumar(double a, double b) { return a + b; }
        double dividir(double a, double b) {
            if (b == 0) throw new ArithmeticException("División por cero");
            return a / b;
        }
    }

    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 15: Testing (simulación sin JUnit) ═══\n");

        var calc = new Calculadora();

        // Test 1: Suma correcta
        assert calc.sumar(2, 3) == 5 : "2 + 3 debería ser 5";
        System.out.println("✓ sumar(2, 3) = 5");

        // Test 2: Suma con negativos
        assert calc.sumar(-1, -1) == -2 : "-1 + -1 debería ser -2";
        System.out.println("✓ sumar(-1, -1) = -2");

        // Test 3: División correcta
        assert calc.dividir(10, 2) == 5 : "10 / 2 debería ser 5";
        System.out.println("✓ dividir(10, 2) = 5");

        // Test 4: División por cero lanza excepción
        try {
            calc.dividir(10, 0);
            System.out.println("✗ Debería haber lanzado excepción");
        } catch (ArithmeticException e) {
            System.out.println("✓ dividir(10, 0) lanza ArithmeticException");
        }

        System.out.println("\n✓ Todos los tests pasaron");
        System.out.println("\nNOTA: Para tests reales usa JUnit 5 + Maven:");
        System.out.println("  cd ../19-spring-boot/proyecto/app && mvn test");
    }
}
