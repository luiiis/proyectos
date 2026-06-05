/**
 * Clase ABSTRACTA: define la estructura base de una cuenta bancaria.
 * No se puede instanciar directamente (new Cuenta() → ERROR).
 * Las clases hijas DEBEN implementar calcularInteres().
 */
public abstract class Cuenta {
    // ENCAPSULAMIENTO: campos privados, acceso controlado
    private String titular;
    private double saldo;
    private String numeroCuenta;

    // Constructor
    protected Cuenta(String titular, String numeroCuenta, double saldoInicial) {
        if (saldoInicial < 0) throw new IllegalArgumentException("Saldo inicial no puede ser negativo");
        this.titular = titular;
        this.numeroCuenta = numeroCuenta;
        this.saldo = saldoInicial;
    }

    // Métodos públicos (interfaz controlada)
    public void depositar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
        this.saldo += monto;
        System.out.printf("  ✓ Depósito: +$%.2f → Saldo: $%.2f%n", monto, saldo);
    }

    public void retirar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
        if (monto > saldo) throw new IllegalStateException("Saldo insuficiente: $" + saldo);
        this.saldo -= monto;
        System.out.printf("  ✓ Retiro: -$%.2f → Saldo: $%.2f%n", monto, saldo);
    }

    // ABSTRACCIÓN: cada tipo de cuenta calcula intereses diferente
    public abstract double calcularInteres();

    // Getters (solo lectura)
    public double getSaldo() { return saldo; }
    public String getTitular() { return titular; }
    public String getNumeroCuenta() { return numeroCuenta; }

    // Protected: solo las hijas pueden modificar el saldo directamente
    protected void setSaldo(double saldo) { this.saldo = saldo; }

    @Override
    public String toString() {
        return String.format("[%s] %s - Saldo: $%.2f", numeroCuenta, titular, saldo);
    }
}
