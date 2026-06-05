/**
 * HERENCIA: CuentaAhorro extiende Cuenta.
 * Hereda: titular, saldo, depositar(), retirar()
 * Agrega: tasaInteres, calcularInteres(), aplicarInteres()
 */
public class CuentaAhorro extends Cuenta {
    private double tasaInteres; // Ej: 0.04 = 4% anual

    public CuentaAhorro(String titular, String numero, double saldoInicial, double tasaInteres) {
        super(titular, numero, saldoInicial); // Llama al constructor del padre
        this.tasaInteres = tasaInteres;
    }

    // POLIMORFISMO: implementación específica de calcularInteres()
    @Override
    public double calcularInteres() {
        return getSaldo() * tasaInteres;
    }

    public void aplicarInteresMensual() {
        double interesMensual = calcularInteres() / 12;
        depositar(interesMensual);
        System.out.printf("  💰 Interés mensual aplicado: +$%.2f (tasa: %.1f%% anual)%n",
                interesMensual, tasaInteres * 100);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Ahorro %.1f%%]", tasaInteres * 100);
    }
}
