import java.util.ArrayList;
import java.util.List;

/**
 * PROGRAMA PRINCIPAL: Demuestra POO en acción.
 * 
 * Ejecución: javac *.java && java SistemaBancario
 */
public class SistemaBancario {

    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════");
        System.out.println("  SISTEMA BANCARIO - Demo POO");
        System.out.println("═══════════════════════════════════════\n");

        // Crear cuentas (POLIMORFISMO: variable tipo Cuenta, objeto tipo específico)
        Cuenta ahorro = new CuentaAhorro("Carlos García", "AH-001", 50000, 0.04);
        Cuenta corriente = new CuentaCorriente("María López", "CC-001", 20000, 10000);

        // ═══════ DEMOSTRACIÓN DE ENCAPSULAMIENTO ═══════
        System.out.println("── Encapsulamiento ──");
        System.out.println("No puedes hacer: ahorro.saldo = 1000000 (es private)");
        System.out.println("Solo puedes: ahorro.depositar(1000) o ahorro.getSaldo()");
        System.out.println("Esto PROTEGE la integridad de los datos.\n");

        // ═══════ DEMOSTRACIÓN DE HERENCIA ═══════
        System.out.println("── Herencia ──");
        System.out.println(ahorro);
        System.out.println(corriente);
        System.out.println("Ambas heredan depositar() y retirar() de Cuenta.\n");

        // ═══════ DEMOSTRACIÓN DE POLIMORFISMO ═══════
        System.out.println("── Polimorfismo ──");
        System.out.println("Mismo método retirar(), comportamiento diferente:\n");

        System.out.println("Carlos (Ahorro) retira $45,000:");
        ahorro.retirar(45000);

        System.out.println("\nMaría (Corriente) retira $25,000 (usa sobregiro):");
        corriente.retirar(25000); // Permite porque tiene sobregiro de $10,000

        // ═══════ POLIMORFISMO CON LISTA ═══════
        System.out.println("\n── Polimorfismo con colección ──");
        List<Cuenta> cuentas = new ArrayList<>();
        cuentas.add(ahorro);
        cuentas.add(corriente);
        cuentas.add(new CuentaAhorro("Juan Martínez", "AH-002", 100000, 0.06));

        System.out.println("Calcular intereses de TODAS las cuentas:");
        double totalIntereses = 0;
        for (Cuenta cuenta : cuentas) {
            double interes = cuenta.calcularInteres(); // Cada tipo calcula diferente
            totalIntereses += interes;
            System.out.printf("  %s → Interés: $%.2f%n", cuenta.getTitular(), interes);
        }
        System.out.printf("  Total intereses del banco: $%.2f%n", totalIntereses);

        // ═══════ MANEJO DE ERRORES ═══════
        System.out.println("\n── Manejo de errores ──");
        try {
            System.out.println("Intentando retirar $100,000 de cuenta con $5,000:");
            ahorro.retirar(100000);
        } catch (IllegalStateException e) {
            System.out.println("  ❌ Error controlado: " + e.getMessage());
        }

        System.out.println("\n═══════════════════════════════════════");
        System.out.println("  FIN DE LA DEMO");
        System.out.println("═══════════════════════════════════════");
    }
}
