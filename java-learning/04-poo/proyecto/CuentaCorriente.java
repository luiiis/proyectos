/**
 * HERENCIA + POLIMORFISMO: CuentaCorriente permite sobregiro.
 * Sobreescribe retirar() para permitir saldo negativo hasta el límite.
 */
public class CuentaCorriente extends Cuenta {
    private double limitesobregiro;

    public CuentaCorriente(String titular, String numero, double saldoInicial, double limiteSobregiro) {
        super(titular, numero, saldoInicial);
        this.limitesobregiro = limiteSobregiro;
    }

    // POLIMORFISMO: retirar() se comporta DIFERENTE que en la clase padre
    @Override
    public void retirar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
        if (monto > getSaldo() + limitesobregiro) {
            throw new IllegalStateException(
                String.format("Excede sobregiro. Disponible: $%.2f (saldo: $%.2f + sobregiro: $%.2f)",
                    getSaldo() + limitesobregiro, getSaldo(), limitesobregiro));
        }
        setSaldo(getSaldo() - monto);
        System.out.printf("  ✓ Retiro: -$%.2f → Saldo: $%.2f%n", monto, getSaldo());
        if (getSaldo() < 0) {
            System.out.printf("  ⚠️ SOBREGIRO activo: $%.2f de $%.2f usado%n",
                    Math.abs(getSaldo()), limitesobregiro);
        }
    }

    @Override
    public double calcularInteres() {
        // Cuenta corriente no genera intereses positivos
        // Si está en sobregiro, cobra interés del 18% anual sobre el monto negativo
        if (getSaldo() < 0) {
            return Math.abs(getSaldo()) * 0.18;
        }
        return 0;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" [Corriente, sobregiro: $%.2f]", limitesobregiro);
    }
}
