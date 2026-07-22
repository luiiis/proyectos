# Entrevista Técnica Java - Patrones de Diseño y Testing

## Patrones de Diseño

### 1. ¿Qué es el patrón Singleton y cuándo usarlo?
**Respuesta:**
Una sola instancia de la clase en toda la aplicación:
```java
public class Configuracion {
    private static final Configuracion INSTANCE = new Configuracion();
    private Configuracion() {} // constructor privado
    public static Configuracion getInstance() { return INSTANCE; }
}
```
Usar: connection pool, logger, configuración global.
NO usar en Spring: Spring ya maneja singletons con beans (`@Service` es singleton por defecto).

---

### 2. ¿Cuál es la diferencia entre Strategy y Template Method?
**Respuesta:**
- **Strategy**: define familia de algoritmos intercambiables en RUNTIME via composición.
- **Template Method**: define el esqueleto de un algoritmo en la clase padre, los hijos implementan los pasos.

```java
// Strategy (composición):
interface EnvioStrategy { double calcular(Paquete p); }
class EnvioExpress implements EnvioStrategy { ... }
class Pedido { private EnvioStrategy envio; }  // Se puede cambiar en runtime

// Template Method (herencia):
abstract class ProcesoPago {
    public final void procesar() {
        validar(); calcular(); cobrar(); notificar();  // Esqueleto fijo
    }
    abstract void validar();  // Los hijos implementan
    abstract void cobrar();
}
```

---

### 3. ¿Qué es el patrón Builder y cuándo lo preferirías sobre constructores?
**Respuesta:**
Cuando el objeto tiene muchos campos opcionales:
```java
// Constructor con 10 parámetros = ilegible:
new Pedido(1, "P-001", cliente, null, "CDMX", "Express", null, true, 0.0, "MXN");

// Builder = legible y flexible:
Pedido.builder()
    .numero("P-001")
    .cliente(cliente)
    .destino("CDMX")
    .envio("Express")
    .build();
```
Records con pocos campos → constructor normal. Objetos con 5+ campos o muchos opcionales → Builder.

---

### 4. ¿Qué es el patrón Observer y dónde se usa en Spring?
**Respuesta:**
Un objeto notifica a múltiples "suscriptores" cuando cambia de estado.

En Spring: `ApplicationEventPublisher` + `@EventListener`:
```java
// Publicar evento:
record VentaCreada(Long ventaId, double total) {}
eventPublisher.publishEvent(new VentaCreada(123, 18999));

// Listener (desacoplado):
@EventListener
public void onVentaCreada(VentaCreada event) {
    enviarNotificacion(event.ventaId());
}
```

---

### 5. ¿Cuándo usarías Factory Method vs Abstract Factory?
**Respuesta:**
- **Factory Method**: crear UN tipo de objeto según condición.
- **Abstract Factory**: crear FAMILIAS de objetos relacionados.

```java
// Factory Method: un método decide qué crear
NotificationSender crear(String tipo) {
    return switch (tipo) {
        case "email" -> new EmailSender();
        case "sms" -> new SmsSender();
        case "push" -> new PushSender();
        default -> throw new IllegalArgumentException();
    };
}

// Abstract Factory: familia de UI (Windows vs Mac)
interface UIFactory {
    Button createButton();
    TextField createTextField();
    Dialog createDialog();
}
```

---

## Testing

### 6. ¿Cuál es la diferencia entre mock, stub y spy?
**Respuesta:**
- **Mock**: objeto simulado que verificas si fue llamado. `verify(mock).metodo()`
- **Stub**: objeto que devuelve respuestas predefinidas. `when(stub.get()).thenReturn(valor)`
- **Spy**: objeto REAL al que puedes sobreescribir algunos métodos.

```java
// Mock: verifica interacción
@Mock EmailService emailMock;
verify(emailMock).enviar("test@mail.com", "Hola");

// Stub: respuesta predefinida
when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));

// Spy: objeto real con override parcial
@Spy VentaService ventaSpy;
doReturn(100.0).when(ventaSpy).calcularDescuento(any());
```

---

### 7. ¿Qué es TDD y cómo lo aplicas?
**Respuesta:**
Test-Driven Development: escribir el test ANTES del código.

Ciclo Red-Green-Refactor:
1. **Red**: escribir test que FALLA (método no existe aún)
2. **Green**: escribir el MÍNIMO código para que pase
3. **Refactor**: mejorar sin romper tests

```java
// 1. RED: test primero
@Test void calcularIva_debeAplicar16Porciento() {
    assertEquals(160.0, servicio.calcularIva(1000));  // Falla: método no existe
}

// 2. GREEN: implementar
public double calcularIva(double monto) { return monto * 0.16; }  // Pasa

// 3. REFACTOR: mejorar si necesario
```

---

### 8. ¿Qué son los tests de integración con Testcontainers?
**Respuesta:**
Tests que usan contenedores Docker REALES (BD, Kafka, Redis) en vez de mocks:
```java
@SpringBootTest
@Testcontainers
class ProductoServiceIT {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test void crearProducto_debePersistitEnBD() {
        var p = service.crear(new ProductoRequest("Laptop", 18999));
        assertThat(p.getId()).isNotNull();
        assertThat(repo.findById(p.getId())).isPresent();
    }
}
```
Ventaja: tests contra BD REAL (no H2 que se comporta diferente).

---

### 9. ¿Qué cobertura de tests es adecuada?
**Respuesta:**
No existe número mágico, pero:
- **Services (lógica de negocio)**: 80-90% (lo más importante)
- **Controllers**: 60-70% (testar happy path + errores principales)
- **Repositories**: tests de integración para queries custom
- **Config/DTOs**: 0% (no tienen lógica)

Cobertura alta ≠ buenos tests. 100% de coverage con tests inútiles no sirve. Mejor 70% con tests que validen COMPORTAMIENTO real.

---

### 10. ¿Cómo testearías un endpoint REST?
**Respuesta:**
```java
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ProductoControllerIT {
    @Autowired TestRestTemplate rest;

    @Test void crearProducto_201() {
        var request = new ProductoRequest("Test", 999.0, 10);
        var response = rest.postForEntity("/api/productos", request, ProductoResponse.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody().id()).isNotNull();
        assertThat(response.getBody().nombre()).isEqualTo("Test");
    }

    @Test void crearProducto_sinNombre_400() {
        var request = new ProductoRequest("", 999.0, 10);  // nombre vacío
        var response = rest.postForEntity("/api/productos", request, Map.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test void buscarProducto_noExiste_404() {
        var response = rest.getForEntity("/api/productos/99999", Map.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
```
