# Java Fundamentos — Lo que necesitas ANTES del Nivel 1

## ¿Por qué Java?
Java es el lenguaje más usado en empresas de México y Latinoamérica para sistemas internos, ERPs, bancos, gobierno y comercio. Si dominas Java + Spring Boot, tienes trabajo asegurado.

---

## 1. Variables y Tipos de Datos

```java
// Tipos primitivos (se guardan directamente en memoria)
int edad = 25;                    // Entero
double salario = 45000.50;        // Decimal
boolean activo = true;            // Verdadero/falso
char inicial = 'L';              // Un solo carácter

// Tipos de referencia (objetos)
String nombre = "Carlos";         // Texto
Integer cantidad = 100;           // int como objeto (para listas, etc.)
Long id = 1L;                    // Entero grande
BigDecimal precio = new BigDecimal("199.99");  // Dinero (NUNCA usar double para dinero)
```

### ¿Cuándo usar qué?
| Tipo | Cuándo usarlo | Ejemplo |
|------|--------------|---------|
| `int` | Cantidades, contadores | existencia, edad |
| `long` / `Long` | IDs de base de datos | id, categoriaId |
| `double` | Cálculos científicos (NO dinero) | latitud, longitud |
| `BigDecimal` | Dinero, precios | precio, total, impuesto |
| `String` | Cualquier texto | nombre, email, sku |
| `boolean` | Verdadero/falso | activo, completado |
| `LocalDateTime` | Fechas con hora | createdAt, updatedAt |
| `LocalDate` | Solo fecha | fechaNacimiento |

---

## 2. Operadores

```java
// Aritméticos
int total = 10 + 5;      // 15
int resta = 10 - 3;      // 7
int producto = 4 * 5;    // 20
int division = 10 / 3;   // 3 (entero, se trunca)
int residuo = 10 % 3;    // 1

// Comparación (devuelven boolean)
edad == 25      // ¿es igual?
edad != 25      // ¿es diferente?
edad > 18       // ¿es mayor?
edad >= 18      // ¿es mayor o igual?
edad < 30       // ¿es menor?

// Lógicos
true && true    // AND: ambos deben ser true
true || false   // OR: al menos uno true
!true           // NOT: invierte (false)

// Para Strings NUNCA usar ==, usar .equals()
nombre.equals("Carlos")       // ✅ Correcto
nombre == "Carlos"            // ❌ Incorrecto (compara referencia, no valor)
```

---

## 3. Condicionales

```java
// if / else if / else
int edad = 20;

if (edad >= 18) {
    System.out.println("Mayor de edad");
} else if (edad >= 13) {
    System.out.println("Adolescente");
} else {
    System.out.println("Niño");
}

// Operador ternario (if corto)
String mensaje = edad >= 18 ? "Mayor" : "Menor";

// switch (para múltiples opciones)
String dia = "lunes";
switch (dia) {
    case "lunes", "martes", "miercoles", "jueves", "viernes" -> System.out.println("Laboral");
    case "sabado", "domingo" -> System.out.println("Fin de semana");
    default -> System.out.println("Día inválido");
}
```

---

## 4. Ciclos

```java
// for clásico
for (int i = 0; i < 10; i++) {
    System.out.println("Iteración: " + i);
}

// for-each (recorrer listas)
List<String> nombres = List.of("Ana", "Luis", "María");
for (String nombre : nombres) {
    System.out.println(nombre);
}

// while (cuando no sabes cuántas veces)
int intentos = 0;
while (intentos < 3) {
    System.out.println("Intento " + intentos);
    intentos++;
}

// do-while (al menos una vez)
do {
    System.out.println("Se ejecuta al menos una vez");
} while (false);
```

---

## 5. Arreglos y Colecciones

```java
// Arreglo (tamaño fijo, poco usado en Spring)
int[] numeros = {1, 2, 3, 4, 5};
String[] dias = new String[7];

// ═══ COLECCIONES (lo que realmente usarás) ═══

// List: lista ordenada, permite duplicados
List<String> productos = new ArrayList<>();
productos.add("Laptop");
productos.add("Mouse");
productos.add("Teclado");
productos.get(0);          // "Laptop" (por índice)
productos.size();          // 3
productos.remove(1);       // quita "Mouse"

// List inmutable (no se puede modificar)
List<String> colores = List.of("rojo", "azul", "verde");

// Map: clave → valor (como un diccionario)
Map<String, Object> respuesta = new HashMap<>();
respuesta.put("success", true);
respuesta.put("mensaje", "Producto creado");
respuesta.put("total", 15);
respuesta.get("success");  // true

// Map inmutable (lo que usamos en los controllers)
Map<String, Object> json = Map.of(
    "success", true,
    "data", productos,
    "total", productos.size()
);

// Set: sin duplicados, sin orden
Set<String> roles = new HashSet<>();
roles.add("ADMIN");
roles.add("ADMIN");  // No se agrega (ya existe)
roles.size();        // 1
```

### Cheatsheet de colecciones
| Colección | Duplicados | Orden | Acceso | Uso típico |
|-----------|-----------|-------|--------|------------|
| `ArrayList` | Sí | Sí (inserción) | Por índice | Listas de productos, resultados |
| `HashMap` | Claves no | No | Por clave | Respuestas JSON, configuración |
| `HashSet` | No | No | Contiene/no | Roles, permisos únicos |
| `LinkedHashMap` | Claves no | Sí (inserción) | Por clave | JSON con orden |

---

## 6. Clases y Objetos

```java
// Una clase es un MOLDE. Un objeto es una INSTANCIA de ese molde.

public class Producto {
    // Campos (datos que guarda)
    private Long id;
    private String nombre;
    private BigDecimal precio;
    private int existencia;
    private boolean activo;

    // Constructor vacío (Spring lo necesita)
    public Producto() {}

    // Constructor con parámetros
    public Producto(String nombre, BigDecimal precio, int existencia) {
        this.nombre = nombre;
        this.precio = precio;
        this.existencia = existencia;
        this.activo = true;
    }

    // Getters (leer datos)
    public String getNombre() { return nombre; }
    public BigDecimal getPrecio() { return precio; }

    // Setters (escribir datos)
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    // Método de negocio
    public boolean tieneStockBajo(int minimo) {
        return existencia < minimo;
    }
}

// Usar la clase:
Producto laptop = new Producto("Laptop HP", new BigDecimal("18999"), 25);
System.out.println(laptop.getNombre());    // "Laptop HP"
System.out.println(laptop.tieneStockBajo(10));  // false
```

### ¿Por qué `private` + getters/setters?
- **Encapsulamiento**: nadie modifica tus datos directamente
- **Control**: puedes validar antes de asignar
- **Spring/MyBatis**: los frameworks los necesitan para funcionar

---

## 7. Interfaces

```java
// Una interface define QUÉ debe hacer algo, pero no CÓMO

public interface Repositorio {
    List<Producto> findAll();
    Producto findById(Long id);
    void save(Producto producto);
    void delete(Long id);
}

// Una clase IMPLEMENTA la interface (dice CÓMO hacerlo)
public class ProductoRepositoryImpl implements Repositorio {

    private List<Producto> productos = new ArrayList<>();

    @Override
    public List<Producto> findAll() {
        return productos;
    }

    @Override
    public Producto findById(Long id) {
        return productos.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void save(Producto producto) {
        productos.add(producto);
    }

    @Override
    public void delete(Long id) {
        productos.removeIf(p -> p.getId().equals(id));
    }
}
```

### ¿Para qué sirven las interfaces?
- **Desacoplamiento**: tu código depende de la interface, no de la implementación
- **Testabilidad**: en tests puedes crear implementaciones falsas (mocks)
- **Flexibilidad**: puedes cambiar MySQL por PostgreSQL sin tocar la lógica
- **Spring**: así funciona la inyección de dependencias

---

## 8. Herencia

```java
// Clase padre (base)
public class Persona {
    private String nombre;
    private String email;

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}

// Clase hija (hereda de Persona)
public class Empleado extends Persona {
    private String puesto;
    private double salario;

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }
}

// Empleado TIENE todo lo de Persona + sus campos propios
Empleado emp = new Empleado();
emp.setNombre("Carlos");    // Heredado de Persona
emp.setPuesto("Developer"); // Propio de Empleado
```

### En la práctica (Spring Boot)
No usarás herencia directamente mucho. Pero verás:
- Tus excepciones heredan de `RuntimeException`
- Tus filtros heredan de `OncePerRequestFilter`
- Los conceptos de "padre/hijo" aplican en BD (categoría → producto)

---

## 9. Excepciones

```java
// Lanzar una excepción (algo salió mal)
public Producto buscarPorId(Long id) {
    Producto producto = mapper.findById(id);
    if (producto == null) {
        throw new RuntimeException("Producto no encontrado con ID: " + id);
    }
    return producto;
}

// Atrapar una excepción (manejar el error)
try {
    Producto p = service.buscarPorId(999L);
} catch (RuntimeException e) {
    System.out.println("Error: " + e.getMessage());
}

// Excepción personalizada
public class ProductoNotFoundException extends RuntimeException {
    public ProductoNotFoundException(Long id) {
        super("Producto no encontrado con ID: " + id);
    }
}

// En Spring Boot: el GlobalExceptionHandler se encarga de atraparlas
// Tú solo lanzas (throw), Spring responde con el JSON de error
```

---

## 10. Streams y Lambdas (Java moderno)

```java
List<Producto> productos = obtenerProductos();

// Filtrar: solo productos activos
List<Producto> activos = productos.stream()
        .filter(p -> p.isActivo())
        .toList();

// Transformar: obtener solo los nombres
List<String> nombres = productos.stream()
        .map(p -> p.getNombre())
        .toList();

// Buscar uno
Producto encontrado = productos.stream()
        .filter(p -> p.getId().equals(5L))
        .findFirst()
        .orElse(null);

// Contar
long total = productos.stream()
        .filter(p -> p.getPrecio().compareTo(new BigDecimal("1000")) > 0)
        .count();

// Ordenar
List<Producto> ordenados = productos.stream()
        .sorted((a, b) -> a.getNombre().compareTo(b.getNombre()))
        .toList();

// Sumar precios
BigDecimal totalPrecio = productos.stream()
        .map(Producto::getPrecio)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
```

### ¿Qué es una lambda?
Una función anónima (sin nombre). Es un atajo:
```java
// Sin lambda (verbose)
productos.sort(new Comparator<Producto>() {
    @Override
    public int compare(Producto a, Producto b) {
        return a.getNombre().compareTo(b.getNombre());
    }
});

// Con lambda (lo mismo, pero corto)
productos.sort((a, b) -> a.getNombre().compareTo(b.getNombre()));

// Con method reference (aún más corto)
productos.sort(Comparator.comparing(Producto::getNombre));
```

---

## 11. Optional (evitar NullPointerException)

```java
// SIN Optional (peligroso):
Producto p = mapper.findById(5L);  // puede ser null
p.getNombre();  // 💥 NullPointerException si p es null

// CON Optional (seguro):
Optional<Producto> resultado = Optional.ofNullable(mapper.findById(5L));

// Opción 1: si existe, hacer algo
resultado.ifPresent(p -> System.out.println(p.getNombre()));

// Opción 2: obtener o lanzar error
Producto p = resultado.orElseThrow(() ->
    new RuntimeException("Producto no encontrado")
);

// Opción 3: obtener o devolver un default
Producto p = resultado.orElse(new Producto());
```

---

## 12. Records (Java 16+, simplifica DTOs)

```java
// ANTES: un DTO necesitaba 30 líneas
public class ProductoResponse {
    private Long id;
    private String nombre;
    private BigDecimal precio;
    // constructor, getters, equals, hashCode, toString...
}

// DESPUÉS: un record lo hace en 1 línea
public record ProductoResponse(Long id, String nombre, BigDecimal precio) {}

// Uso:
ProductoResponse dto = new ProductoResponse(1L, "Laptop", new BigDecimal("18999"));
dto.id();       // 1L
dto.nombre();   // "Laptop"
```

**Nota**: En los proyectos del roadmap usamos clases normales para que entiendas los getters/setters. Records son un atajo que verás en proyectos modernos.

---

## 13. Inyección de Dependencias (concepto clave para Spring)

```java
// ❌ MAL: crear dependencias manualmente
public class ProductoService {
    private ProductoMapper mapper = new ProductoMapperImpl(); // acoplado

    public List<Producto> listar() {
        return mapper.findAll();
    }
}

// ✅ BIEN: recibir la dependencia (inyección por constructor)
public class ProductoService {
    private final ProductoMapper mapper; // solo la interface

    // Spring inyecta automáticamente la implementación correcta
    public ProductoService(ProductoMapper mapper) {
        this.mapper = mapper;
    }

    public List<Producto> listar() {
        return mapper.findAll();
    }
}
```

### ¿Por qué importa?
- **Testabilidad**: en tests inyectas un mock (simulador)
- **Flexibilidad**: cambias la implementación sin tocar el service
- **Spring lo hace automático**: tú solo pones el constructor, Spring inyecta

---

## Ejercicios para practicar ANTES del Nivel 1

### Ejercicio 1: Clase Producto
Crea una clase `Producto` con campos (nombre, precio, existencia), constructor, getters/setters y un método `calcularTotal(int cantidad)`.

### Ejercicio 2: Lista de productos
Crea una `List<Producto>`, agrega 5 productos, y usa streams para:
- Filtrar los que cuesten más de $1000
- Obtener solo los nombres
- Calcular la suma de precios

### Ejercicio 3: Interface + implementación
Crea una interface `Calculadora` con métodos (sumar, restar, multiplicar, dividir). Crea una clase que la implemente. Úsala.

### Ejercicio 4: Manejar null
Crea un método que busque un producto por nombre en una lista. Si no existe, lanza una excepción personalizada.

---

## Siguiente paso
Cuando domines estos conceptos → ve al **Nivel 1: API de Saludos** con confianza.
