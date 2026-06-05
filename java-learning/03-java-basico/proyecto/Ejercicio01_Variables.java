/**
 * EJERCICIO 01: Variables y Tipos de Datos
 * 
 * Concepto: Java es TIPADO ESTÁTICO - debes declarar el tipo de cada variable.
 * Esto previene errores en compilación (no en runtime como Python/JS).
 * 
 * Ejecución: javac Ejercicio01_Variables.java && java Ejercicio01_Variables
 */
public class Ejercicio01_Variables {

    public static void main(String[] args) {
        // ═══════ TIPOS PRIMITIVOS ═══════
        // Enteros
        byte edad = 25;              // 8 bits: -128 a 127
        short año = 2026;            // 16 bits
        int poblacion = 130_000_000; // 32 bits (separador _ para legibilidad)
        long distanciaLuz = 9_460_730_472_580L; // 64 bits (L al final)

        // Decimales
        float pi = 3.14159f;         // 32 bits (f al final)
        double precio = 18999.99;    // 64 bits (más preciso, USAR ESTE)

        // Carácter y booleano
        char inicial = 'C';          // 16 bits (un carácter Unicode)
        boolean activo = true;       // true o false

        // ═══════ TIPOS DE REFERENCIA ═══════
        String nombre = "Carlos García";  // Objeto (no primitivo)
        String nulo = null;               // Los objetos pueden ser null

        // ═══════ var (Java 10+) - Inferencia de tipo ═══════
        var mensaje = "Hola";        // Java infiere: String
        var numero = 42;             // Java infiere: int
        var lista = new java.util.ArrayList<String>(); // Java infiere el tipo

        // ═══════ CONSTANTES ═══════
        final double IVA = 0.16;     // No se puede cambiar
        final String EMPRESA = "TechCorp";

        // ═══════ IMPRIMIR ═══════
        System.out.println("═══ Tipos Primitivos ═══");
        System.out.println("Edad: " + edad);
        System.out.println("Año: " + año);
        System.out.println("Población: " + poblacion);
        System.out.println("Precio: $" + precio);
        System.out.println("Activo: " + activo);

        System.out.println("\n═══ Cálculos ═══");
        double precioConIva = precio * (1 + IVA);
        System.out.printf("Precio sin IVA: $%.2f%n", precio);
        System.out.printf("IVA (16%%): $%.2f%n", precio * IVA);
        System.out.printf("Precio con IVA: $%.2f%n", precioConIva);

        System.out.println("\n═══ Casting (conversión de tipos) ═══");
        int entero = (int) 3.99;     // 3 (trunca, no redondea)
        double decimal = entero;      // 3.0 (conversión implícita, no pierde datos)
        System.out.println("(int) 3.99 = " + entero);
        System.out.println("int → double: " + decimal);

        // ═══════ EJERCICIO PARA TI ═══════
        // 1. Declara variables para: tu nombre, tu edad, tu estatura, si eres estudiante
        // 2. Calcula tu IMC (peso / estatura²)
        // 3. Imprime todo con formato bonito usando printf
    }
}
