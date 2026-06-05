# Módulo 04: Programación Orientada a Objetos (POO)

## ¿Qué es POO?
Paradigma que organiza el código en "objetos" que combinan datos (atributos) y comportamiento (métodos). Modela el mundo real en código.

**Sin POO**: funciones sueltas + datos globales = caos en proyectos grandes.
**Con POO**: objetos encapsulados + relaciones claras = código mantenible.

---

## 1. Clases y Objetos

```java
// CLASE = plano/molde (define la estructura)
public class Producto {
    // Atributos (datos)
    private String nombre;
    private double precio;
    private int stock;

    // Constructor (crea el objeto)
    public Producto(String nombre, double precio, int stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    // Métodos (comportamiento)
    public double calcularPrecioConIva() {
        return precio * 1.16;
    }

    public boolean hayStock() {
        return stock > 0;
    }

    public void vender(int cantidad) {
        if (cantidad > stock) {
            throw new IllegalArgumentException("Stock insuficiente");
        }
        stock -= cantidad;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) {
        if (precio <= 0) throw new IllegalArgumentException("Precio debe ser positivo");
        this.precio = precio;
    }
}

// OBJETO = instancia concreta de la clase
Producto laptop = new Producto("MacBook Pro", 54999.99, 10);
Producto mouse = new Producto("Logitech MX", 1899.00, 50);

laptop.vender(2);  // stock: 10 → 8
System.out.println(laptop.calcularPrecioConIva()); // 63799.99
```

### Diagrama UML:
```
┌─────────────────────────────┐
│         Producto            │
├─────────────────────────────┤
│ - nombre: String            │
│ - precio: double            │
│ - stock: int                │
├─────────────────────────────┤
│ + Producto(nombre, precio,  │
│            stock)           │
│ + calcularPrecioConIva():   │
│   double                    │
│ + hayStock(): boolean       │
│ + vender(cantidad): void    │
│ + getNombre(): String       │
│ + getPrecio(): double       │
│ + setPrecio(precio): void   │
└─────────────────────────────┘
  - = private
  + = public
  # = protected
```

---

## 2. Los 4 Pilares de POO

### 2.1 Encapsulamiento
Ocultar los datos internos y exponer solo lo necesario.

```java
public class CuentaBancaria {
    private double saldo;  // PRIVADO: nadie puede modificarlo directamente
    private String titular;

    public CuentaBancaria(String titular, double saldoInicial) {
        this.titular = titular;
        this.saldo = saldoInicial;
    }

    // Solo se puede modificar el saldo a través de métodos controlados
    public void depositar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
        saldo += monto;
    }

    public void retirar(double monto) {
        if (monto <= 0) throw new IllegalArgumentException("Monto debe ser positivo");
        if (monto > saldo) throw new IllegalStateException("Saldo insuficiente");
        saldo -= monto;
    }

    public double getSaldo() { return saldo; }  // Solo lectura
    // NO hay setSaldo() → nadie puede poner saldo arbitrario
}
```

### 2.2 Herencia
Una clase HIJA hereda atributos y métodos de una clase PADRE.

```java
// Clase padre
public class Empleado {
    protected String nombre;
    protected double salarioBase;

    public Empleado(String nombre, double salarioBase) {
        this.nombre = nombre;
        this.salarioBase = salarioBase;
    }

    public double calcularSalario() {
        return salarioBase;
    }
}

// Clase hija (hereda de Empleado)
public class Vendedor extends Empleado {
    private double comision;
    private double ventasMes;

    public Vendedor(String nombre, double salarioBase, double comision) {
        super(nombre, salarioBase);  // Llama al constructor del padre
        this.comision = comision;
    }

    @Override  // Sobreescribe el método del padre
    public double calcularSalario() {
        return salarioBase + (ventasMes * comision);
    }

    public void registrarVenta(double monto) {
        ventasMes += monto;
    }
}

// Clase hija
public class Gerente extends Empleado {
    private double bono;

    public Gerente(String nombre, double salarioBase, double bono) {
        super(nombre, salarioBase);
        this.bono = bono;
    }

    @Override
    public double calcularSalario() {
        return salarioBase + bono;
    }
}
```

### 2.3 Polimorfismo
Un mismo método se comporta diferente según el tipo de objeto.

```java
// Polimorfismo en acción:
Empleado emp1 = new Vendedor("Carlos", 20000, 0.05);
Empleado emp2 = new Gerente("María", 50000, 15000);

// Mismo método, diferente comportamiento
System.out.println(emp1.calcularSalario()); // 20000 + comisiones
System.out.println(emp2.calcularSalario()); // 50000 + 15000

// Útil en listas:
List<Empleado> nomina = List.of(emp1, emp2);
double totalNomina = nomina.stream()
    .mapToDouble(Empleado::calcularSalario)
    .sum();
```

### 2.4 Abstracción
Definir QUÉ hace algo sin especificar CÓMO (clases abstractas e interfaces).

```java
// Clase abstracta: no se puede instanciar directamente
public abstract class Figura {
    protected String color;

    public abstract double calcularArea();      // Sin implementación
    public abstract double calcularPerimetro(); // Las hijas DEBEN implementar

    public String getColor() { return color; }  // Método concreto (heredable)
}

public class Circulo extends Figura {
    private double radio;

    public Circulo(double radio, String color) {
        this.radio = radio;
        this.color = color;
    }

    @Override
    public double calcularArea() { return Math.PI * radio * radio; }

    @Override
    public double calcularPerimetro() { return 2 * Math.PI * radio; }
}

public class Rectangulo extends Figura {
    private double ancho, alto;

    @Override
    public double calcularArea() { return ancho * alto; }

    @Override
    public double calcularPerimetro() { return 2 * (ancho + alto); }
}
```

---

## 3. Interfaces

```java
// Interface = contrato que una clase DEBE cumplir
public interface Pagable {
    double calcularMonto();
    void procesarPago();
    default String generarRecibo() {  // Método con implementación default (Java 8+)
        return "Recibo #" + System.currentTimeMillis();
    }
}

public interface Auditable {
    void registrarAccion(String accion);
}

// Una clase puede implementar MÚLTIPLES interfaces
public class Venta implements Pagable, Auditable {
    private double total;

    @Override
    public double calcularMonto() { return total * 1.16; }

    @Override
    public void procesarPago() { /* lógica de pago */ }

    @Override
    public void registrarAccion(String accion) { /* log */ }
}
```

### Interface vs Clase Abstracta:

| Aspecto | Interface | Clase Abstracta |
|---------|-----------|-----------------|
| Herencia múltiple | Sí (implements A, B, C) | No (solo extends UNA) |
| Atributos | Solo constantes (static final) | Cualquier atributo |
| Constructores | No | Sí |
| Métodos | abstract + default + static | abstract + concretos |
| Cuándo usar | Definir CAPACIDADES (Pagable, Serializable) | Definir JERARQUÍA (Empleado → Vendedor) |

---

## 4. Records (Java 16+) - DTOs inmutables

```java
// ANTES: 50 líneas para un simple contenedor de datos
public class ClienteDto {
    private final String nombre;
    private final String email;
    // constructor, getters, equals, hashCode, toString...
}

// AHORA: 1 línea
public record ClienteDto(String nombre, String email, int edad) {}

// Uso:
var cliente = new ClienteDto("Carlos", "carlos@email.com", 30);
System.out.println(cliente.nombre());  // "Carlos" (getter automático)
System.out.println(cliente);           // ClienteDto[nombre=Carlos, email=carlos@email.com, edad=30]
```

---

## 5. Sealed Classes (Java 17+)

```java
// Solo estas clases pueden extender MetodoPago
public sealed interface MetodoPago permits Efectivo, Tarjeta, Transferencia {}

public record Efectivo(double monto) implements MetodoPago {}
public record Tarjeta(String numero, double monto) implements MetodoPago {}
public record Transferencia(String clabe, double monto) implements MetodoPago {}

// Pattern matching exhaustivo (el compilador verifica que cubras todos los casos)
String procesar(MetodoPago pago) {
    return switch (pago) {
        case Efectivo e -> "Efectivo: $" + e.monto();
        case Tarjeta t -> "Tarjeta: " + t.numero();
        case Transferencia tr -> "CLABE: " + tr.clabe();
        // No necesitas default: el compilador sabe que cubriste todo
    };
}
```

---

## 6. Ejercicios

### Básicos
1. Crea una clase `Persona` con nombre, edad, email. Incluye constructor, getters, toString.
2. Crea una clase `Rectangulo` con métodos para calcular área y perímetro.
3. Crea una clase `CuentaBancaria` con depositar, retirar, consultarSaldo.

### Herencia
4. Crea jerarquía: `Animal` → `Perro`, `Gato`, `Ave`. Cada uno con su sonido.
5. Crea jerarquía: `Vehiculo` → `Auto`, `Moto`, `Camion`. Cada uno calcula consumo diferente.
6. Crea jerarquía: `Empleado` → `Vendedor`, `Gerente`, `Director`. Cada uno calcula salario diferente.

### Interfaces
7. Crea interface `Exportable` con métodos: exportarCSV(), exportarJSON(), exportarPDF().
8. Implementa un sistema de notificaciones con interface `Notificable` (email, SMS, push).

### Avanzados
9. Diseña un sistema de carrito de compras con: Producto, CarritoCompras, Descuento (interface).
10. Implementa un mini sistema bancario con: Cuenta (abstracta), CuentaAhorro, CuentaCorriente, Transferencia.

---

## Siguiente Módulo
→ [05-Colecciones](../05-colecciones/README.md)
