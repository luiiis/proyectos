# Módulo 14: Patrones de Diseño (Design Patterns)

## Los más usados en Java Enterprise

### Singleton — Una sola instancia
```java
public class ConfiguracionApp {
    private static final ConfiguracionApp INSTANCE = new ConfiguracionApp();
    private ConfiguracionApp() {} // Constructor privado
    public static ConfiguracionApp getInstance() { return INSTANCE; }
}
```

### Factory — Crear objetos sin exponer la lógica
```java
public interface Notificacion { void enviar(String mensaje); }
public class NotificacionEmail implements Notificacion { ... }
public class NotificacionSMS implements Notificacion { ... }

public class NotificacionFactory {
    public static Notificacion crear(String tipo) {
        return switch (tipo) {
            case "EMAIL" -> new NotificacionEmail();
            case "SMS" -> new NotificacionSMS();
            default -> throw new IllegalArgumentException("Tipo no soportado");
        };
    }
}
```

### Builder — Construir objetos complejos paso a paso
```java
Producto producto = Producto.builder()
    .nombre("Laptop Pro")
    .precio(25000)
    .stock(10)
    .categoria("Electrónica")
    .build();
```

### Strategy — Algoritmos intercambiables
```java
public interface DescuentoStrategy {
    double calcular(double monto);
}
public class DescuentoVIP implements DescuentoStrategy {
    public double calcular(double monto) { return monto * 0.20; }
}
public class DescuentoRegular implements DescuentoStrategy {
    public double calcular(double monto) { return monto * 0.05; }
}
// Uso: venta.setDescuento(new DescuentoVIP());
```

### Observer — Notificar cambios a múltiples interesados
### Adapter — Hacer compatible una interfaz con otra
### Facade — Simplificar un sistema complejo
### Decorator — Agregar funcionalidad sin modificar la clase

## Ejercicios
1. Implementa Factory para crear diferentes tipos de reporte (PDF, CSV, Excel)
2. Implementa Strategy para diferentes métodos de envío (express, estándar, gratis)
3. Implementa Observer para notificar cuando el stock baja del mínimo
4. Implementa Builder para construir queries SQL de forma fluida
