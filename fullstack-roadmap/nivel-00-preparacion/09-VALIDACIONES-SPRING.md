# Validaciones en Spring Boot — Bean Validation + Ejemplos

## ¿Por qué validar?
Si no validas, cualquiera puede enviar datos basura a tu API:
- Nombre vacío
- Precio negativo
- Email inválido
- Campos que no existen

Las validaciones protegen tu base de datos y evitan errores.

---

## 1. Dependencia necesaria

```xml
<!-- Ya la tienes en los proyectos del roadmap -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

---

## 2. Anotaciones de validación en el DTO

```java
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public class ProductoRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 3, max = 150, message = "El nombre debe tener entre 3 y 150 caracteres")
    private String nombre;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    @Digits(integer = 8, fraction = 2, message = "El precio debe tener máximo 8 enteros y 2 decimales")
    private BigDecimal precio;

    @Min(value = 0, message = "La existencia no puede ser negativa")
    private int existencia;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @Pattern(regexp = "^[A-Z]{3}-[A-Z]{3}-\\d{3}$",
             message = "El SKU debe tener formato AAA-BBB-999")
    private String sku;

    // Getters y setters...
}
```

---

## 3. Activar validación en el Controller

```java
@PostMapping
public ResponseEntity<Map<String, Object>> crear(
        @Valid @RequestBody ProductoRequest request) {  // ← @Valid activa las validaciones
    // Si el body no cumple las validaciones, Spring lanza MethodArgumentNotValidException
    // ANTES de llegar aquí
    Producto creado = productoService.crear(request);
    return ResponseEntity.status(HttpStatus.CREATED)
            .body(Map.of("success", true, "data", creado));
}
```

---

## 4. Manejar errores de validación (GlobalExceptionHandler)

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Errores de validación (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        // Recopilar TODOS los errores
        List<Map<String, String>> errores = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> Map.of(
                    "campo", error.getField(),
                    "mensaje", error.getDefaultMessage(),
                    "valorRechazado", String.valueOf(error.getRejectedValue())
                ))
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                    "success", false,
                    "error", "Datos inválidos",
                    "detalles", errores,
                    "total_errores", errores.size()
                ));
    }
}
```

---

## 5. Ejemplo: request inválido y respuesta

### Request (datos incorrectos):
```json
POST /api/productos
{
  "nombre": "",
  "precio": -50,
  "existencia": -1,
  "sku": "invalido"
}
```

### Response (400 Bad Request):
```json
{
  "success": false,
  "error": "Datos inválidos",
  "detalles": [
    {
      "campo": "nombre",
      "mensaje": "El nombre es obligatorio",
      "valorRechazado": ""
    },
    {
      "campo": "precio",
      "mensaje": "El precio debe ser mayor a 0",
      "valorRechazado": "-50"
    },
    {
      "campo": "existencia",
      "mensaje": "La existencia no puede ser negativa",
      "valorRechazado": "-1"
    },
    {
      "campo": "categoriaId",
      "mensaje": "La categoría es obligatoria",
      "valorRechazado": "null"
    },
    {
      "campo": "sku",
      "mensaje": "El SKU debe tener formato AAA-BBB-999",
      "valorRechazado": "invalido"
    }
  ],
  "total_errores": 5
}
```

---

## 6. Todas las anotaciones de validación

| Anotación | Para qué | Ejemplo |
|-----------|----------|---------|
| `@NotNull` | No puede ser null | IDs, objetos obligatorios |
| `@NotBlank` | No null, no vacío, no solo espacios | Strings obligatorios |
| `@NotEmpty` | No null, no vacío (para colecciones) | Listas, maps |
| `@Size(min, max)` | Longitud del texto | `@Size(min=3, max=100)` |
| `@Min(value)` | Valor mínimo (números) | `@Min(0)` existencia |
| `@Max(value)` | Valor máximo (números) | `@Max(999999)` |
| `@DecimalMin` | Mínimo para decimales | `@DecimalMin("0.01")` |
| `@DecimalMax` | Máximo para decimales | `@DecimalMax("99999.99")` |
| `@Digits(integer, fraction)` | Formato numérico | `@Digits(integer=8, fraction=2)` |
| `@Email` | Formato de email válido | `@Email` |
| `@Pattern(regexp)` | Regex personalizado | SKU, teléfono, RFC |
| `@Past` | Fecha en el pasado | Fecha de nacimiento |
| `@Future` | Fecha en el futuro | Fecha de vencimiento |
| `@Positive` | Mayor que 0 | Cantidades |
| `@PositiveOrZero` | Mayor o igual a 0 | Existencia |

---

## 7. Validaciones personalizadas

### Validar que un email no exista en BD
```java
// En el Service (validación de negocio, no de formato)
public Usuario registrar(RegistroRequest request) {
    // Validar que el email no esté registrado
    if (usuarioMapper.findByEmail(request.getEmail()) != null) {
        throw new RuntimeException("El email ya está registrado");
    }

    // Validar que el username no exista
    if (usuarioMapper.findByUsername(request.getUsername()) != null) {
        throw new RuntimeException("El username ya está en uso");
    }

    // ... crear usuario
}
```

### Validar password fuerte (regex)
```java
@Pattern(
    regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "El password debe tener mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial"
)
private String password;
```

---

## 8. Validaciones en capas

| Capa | Tipo de validación | Ejemplo |
|------|-------------------|---------|
| **Controller** | Formato (anotaciones @Valid) | "nombre no vacío", "email válido" |
| **Service** | Lógica de negocio | "email no duplicado", "stock suficiente" |
| **BD** | Integridad referencial | UNIQUE, FOREIGN KEY, CHECK |

Las 3 capas validan cosas DIFERENTES. No son redundantes.

---

## 9. Ejemplo completo: validar una venta

```java
// DTO
public class VentaRequest {
    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotEmpty(message = "Debe incluir al menos un producto")
    @Valid  // ← valida también los elementos de la lista
    private List<VentaDetalleRequest> detalles;
}

public class VentaDetalleRequest {
    @NotNull(message = "El producto es obligatorio")
    private Long productoId;

    @Min(value = 1, message = "La cantidad mínima es 1")
    private int cantidad;
}

// Service — validación de negocio
public Venta registrarVenta(VentaRequest request) {
    // 1. Validar que el cliente exista
    Cliente cliente = clienteMapper.findById(request.getClienteId());
    if (cliente == null) throw new RuntimeException("Cliente no encontrado");

    // 2. Validar stock de cada producto
    for (VentaDetalleRequest detalle : request.getDetalles()) {
        Producto producto = productoMapper.findById(detalle.getProductoId());
        if (producto == null) {
            throw new RuntimeException("Producto no encontrado: " + detalle.getProductoId());
        }
        if (producto.getExistencia() < detalle.getCantidad()) {
            throw new RuntimeException(
                "Stock insuficiente para " + producto.getNombre() +
                ". Disponible: " + producto.getExistencia() +
                ", Solicitado: " + detalle.getCantidad()
            );
        }
    }

    // 3. Registrar venta (si todo pasa)
    // ...
}
```

---

## Siguiente paso
Agrega validaciones con @Valid a los controllers de tus proyectos del roadmap (niveles 2+).
