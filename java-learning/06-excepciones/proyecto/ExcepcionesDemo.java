/**
 * MÓDULO 06: Excepciones - Demo ejecutable
 * Ejecutar: javac ExcepcionesDemo.java && java ExcepcionesDemo
 */
public class ExcepcionesDemo {
    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 06: Excepciones ═══\n");

        // Try-catch básico
        try {
            int resultado = 10 / 0;
        } catch (ArithmeticException e) {
            System.out.println("✓ Capturado: " + e.getMessage());
        }

        // Custom exception
        try {
            validarEdad(-5);
        } catch (ValidacionException e) {
            System.out.println("✓ Custom exception: [" + e.getCodigo() + "] " + e.getMessage());
        }

        // Try con múltiples catch
        try {
            String[] arr = {"hola"};
            System.out.println(arr[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("✓ Índice fuera de rango: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error genérico: " + e.getMessage());
        } finally {
            System.out.println("  (finally siempre se ejecuta)");
        }

        System.out.println("\n✓ Programa terminó sin crashear");
    }

    static void validarEdad(int edad) {
        if (edad < 0) throw new ValidacionException("EDAD_INVALIDA", "La edad no puede ser negativa: " + edad);
        if (edad > 150) throw new ValidacionException("EDAD_INVALIDA", "Edad no realista: " + edad);
    }
}

// Custom Exception (patrón enterprise)
class ValidacionException extends RuntimeException {
    private final String codigo;
    public ValidacionException(String codigo, String mensaje) {
        super(mensaje);
        this.codigo = codigo;
    }
    public String getCodigo() { return codigo; }
}
