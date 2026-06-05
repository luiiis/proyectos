import java.util.*;

/**
 * MÓDULO 14: Patrones de Diseño - Demo ejecutable
 * Ejecutar: javac PatronesDemo.java && java PatronesDemo
 */
public class PatronesDemo {
    public static void main(String[] args) {
        System.out.println("═══ MÓDULO 14: Design Patterns ═══\n");

        // STRATEGY: diferentes algoritmos de descuento
        System.out.println("── Strategy: Descuentos ──");
        var venta1 = new Venta(1000, new DescuentoVIP());
        var venta2 = new Venta(1000, new DescuentoRegular());
        System.out.println("VIP: $" + venta1.calcularTotal());
        System.out.println("Regular: $" + venta2.calcularTotal());

        // BUILDER: construir objetos complejos
        System.out.println("\n── Builder: Producto ──");
        var producto = new ProductoBuilder()
            .nombre("Laptop Pro").precio(25000).stock(10).categoria("Electrónica").build();
        System.out.println(producto);

        // FACTORY: crear objetos sin exponer lógica
        System.out.println("\n── Factory: Notificaciones ──");
        enviar(NotificacionFactory.crear("EMAIL"), "Tu pedido fue enviado");
        enviar(NotificacionFactory.crear("SMS"), "Código: 1234");
    }

    static void enviar(Notificacion n, String msg) { n.enviar(msg); }
}

// ═══ STRATEGY ═══
interface DescuentoStrategy { double calcular(double monto); }
class DescuentoVIP implements DescuentoStrategy { public double calcular(double m) { return m * 0.20; } }
class DescuentoRegular implements DescuentoStrategy { public double calcular(double m) { return m * 0.05; } }
class Venta {
    double monto; DescuentoStrategy estrategia;
    Venta(double monto, DescuentoStrategy e) { this.monto = monto; this.estrategia = e; }
    double calcularTotal() { return monto - estrategia.calcular(monto); }
}

// ═══ BUILDER ═══
class ProductoBuilder {
    private String nombre, categoria; private double precio; private int stock;
    ProductoBuilder nombre(String n) { nombre = n; return this; }
    ProductoBuilder precio(double p) { precio = p; return this; }
    ProductoBuilder stock(int s) { stock = s; return this; }
    ProductoBuilder categoria(String c) { categoria = c; return this; }
    String build() { return String.format("Producto{%s, $%.2f, stock=%d, cat=%s}", nombre, precio, stock, categoria); }
}

// ═══ FACTORY ═══
interface Notificacion { void enviar(String mensaje); }
class EmailNotif implements Notificacion { public void enviar(String m) { System.out.println("  📧 Email: " + m); } }
class SmsNotif implements Notificacion { public void enviar(String m) { System.out.println("  📱 SMS: " + m); } }
class NotificacionFactory {
    static Notificacion crear(String tipo) {
        return switch (tipo) {
            case "EMAIL" -> new EmailNotif();
            case "SMS" -> new SmsNotif();
            default -> throw new IllegalArgumentException("Tipo no soportado: " + tipo);
        };
    }
}
