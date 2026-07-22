# Entrevista Técnica Java - Nivel Mid (Spring Boot + JPA)

## 1. ¿Qué es Spring Boot y por qué no usar Spring Framework directamente?
**Respuesta:**
Spring Boot = Spring Framework + auto-configuración + starters + servidor embebido.

Sin Spring Boot: configurar Tomcat, DataSource, Jackson, Transaction Manager manualmente (100+ líneas XML).
Con Spring Boot: `spring-boot-starter-web` → todo configurado en 0 líneas.

Convention over configuration: funciona con defaults sensatos, solo configuras lo que necesitas cambiar.

---

## 2. ¿Qué es la Inyección de Dependencias (DI)?
**Respuesta:**
En vez de crear dependencias con `new`, Spring las INYECTA automáticamente:

```java
// SIN DI (acoplado):
public class VentaService {
    private final ProductoRepository repo = new ProductoRepositoryImpl();  // Tight coupling
}

// CON DI (desacoplado):
@Service
public class VentaService {
    private final ProductoRepository repo;  // Spring inyecta la implementación
    
    public VentaService(ProductoRepository repo) { this.repo = repo; }  // Constructor injection
}
```

Beneficios: testing fácil (puedes inyectar mocks), cambiar implementación sin tocar código, principio de inversión de dependencias.

---

## 3. ¿Cuál es la diferencia entre @Component, @Service, @Repository y @Controller?
**Respuesta:**
Todos son `@Component` (Spring los detecta y crea beans). La diferencia es **semántica**:

| Anotación | Capa | Para qué |
|-----------|------|----------|
| @Component | Genérica | Cualquier bean |
| @Service | Negocio | Lógica de negocio |
| @Repository | Datos | Acceso a BD (traduce excepciones SQL) |
| @Controller/@RestController | Web | Recibe HTTP requests |

---

## 4. ¿Qué es el problema N+1 en JPA y cómo resolverlo?
**Respuesta:**
Cargar 100 productos → 1 query. Acceder a la categoría de cada uno → 100 queries adicionales.

```java
// PROBLEMA: 101 queries
List<Producto> productos = repo.findAll();  // 1 query
for (var p : productos) {
    p.getCategoria().getNombre();  // +100 queries (LAZY loading)
}
```

Soluciones:
```java
// 1. JOIN FETCH (JPQL):
@Query("SELECT p FROM Producto p JOIN FETCH p.categoria")
List<Producto> findAllConCategoria();

// 2. EntityGraph:
@EntityGraph(attributePaths = {"categoria"})
List<Producto> findAll();

// 3. BatchSize:
@BatchSize(size = 50)  // Carga categorías en lotes de 50
private Categoria categoria;
```

---

## 5. ¿Qué es @Transactional y cómo funciona?
**Respuesta:**
Declara que un método debe ejecutarse dentro de una transacción:

```java
@Transactional
public void registrarVenta(VentaRequest req) {
    var venta = ventaRepo.save(new Venta(...));
    for (var item : req.items()) {
        productoRepo.descontarStock(item.id(), item.cantidad());
        detalleRepo.save(new Detalle(venta, item));
    }
    // Si algún paso lanza RuntimeException → ROLLBACK de TODO
    // Si todo OK → COMMIT automático al salir del método
}
```

Reglas:
- Solo funciona en métodos **públicos**
- Solo hace rollback en **RuntimeException** (por default)
- Se puede configurar: `@Transactional(rollbackFor = Exception.class)`
- Propagation: REQUIRED (default), REQUIRES_NEW, etc.

---

## 6. ¿Cómo manejas errores globalmente en Spring Boot?
**Respuesta:**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse notFound(ProductoNotFoundException e) {
        return new ErrorResponse(404, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validation(MethodArgumentNotValidException e) {
        var errores = e.getFieldErrors().stream()
            .map(f -> f.getField() + ": " + f.getDefaultMessage())
            .toList();
        return new ErrorResponse(400, "Validación fallida", errores);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse generic(Exception e) {
        log.error("Error no manejado", e);
        return new ErrorResponse(500, "Error interno del servidor");
    }
}
```

---

## 7. ¿Cuál es la diferencia entre FetchType.LAZY y EAGER?
**Respuesta:**
- **LAZY**: la relación NO se carga hasta que la accedes (`getCategoria()`). Default para @OneToMany, @ManyToMany.
- **EAGER**: se carga SIEMPRE junto con la entidad principal. Default para @ManyToOne, @OneToOne.

Regla: **LAZY siempre**. Si necesitas la relación, usa JOIN FETCH explícito. EAGER causa queries inesperados.

---

## 8. ¿Cómo implementas paginación en Spring Data JPA?
**Respuesta:**
```java
// Repository:
Page<Producto> findByActivoTrue(Pageable pageable);

// Controller:
@GetMapping
public Page<Producto> listar(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "20") int size,
    @RequestParam(defaultValue = "precio,desc") String sort
) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(sort.split(",")));
    return repo.findByActivoTrue(pageable);
}

// Response automática:
// { content: [...], totalElements: 500, totalPages: 25, number: 0, size: 20 }
```

---

## 9. ¿Qué es un DTO y por qué no exponer la Entity directamente?
**Respuesta:**
DTO (Data Transfer Object) = lo que viaja entre capas/API.

No exponer Entity porque:
- **Seguridad**: Entity puede tener `passwordHash`, no lo quieres en la respuesta
- **Estabilidad**: cambiar BD no debe cambiar el API contract
- **Flexibilidad**: DTO puede tener campos calculados, combinados
- **Validación**: DTOs de request tienen sus propias validaciones

```java
// Request DTO (lo que envía el cliente):
record ProductoRequest(@NotBlank String nombre, @Positive double precio, int stock) {}

// Response DTO (lo que devuelves):
record ProductoResponse(Long id, String nombre, double precio, String categoria) {}

// Mapper:
ProductoResponse toResponse(Producto p) {
    return new ProductoResponse(p.getId(), p.getNombre(), p.getPrecio(), p.getCategoria().getNombre());
}
```

---

## 10. ¿Cómo testeas un Service con Mockito?
**Respuesta:**
```java
@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock ProductoRepository productoRepo;
    @Mock VentaRepository ventaRepo;
    @InjectMocks VentaService service;

    @Test
    void registrarVenta_conStockSuficiente_debeDescontarStock() {
        // Arrange
        var producto = new Producto("Laptop", 18999, 10);
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));
        when(ventaRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        var resultado = service.registrarVenta(new VentaRequest(1L, 1L, List.of(new Item(1L, 2))));

        // Assert
        assertThat(producto.getStock()).isEqualTo(8);  // 10 - 2
        verify(ventaRepo).save(any(Venta.class));
        verify(productoRepo).save(producto);
    }

    @Test
    void registrarVenta_sinStock_debeLanzarExcepcion() {
        var producto = new Producto("Laptop", 18999, 1);
        when(productoRepo.findById(1L)).thenReturn(Optional.of(producto));

        assertThrows(StockInsuficienteException.class,
            () -> service.registrarVenta(new VentaRequest(1L, 1L, List.of(new Item(1L, 5)))));
    }
}
```

---

## 11. ¿Cuáles son los profiles en Spring Boot?
**Respuesta:**
Configuraciones diferentes según el ambiente:
```yaml
# application.yml (default)
spring:
  profiles:
    active: dev

# application-dev.yml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/dev_db

# application-prod.yml
spring:
  datasource:
    url: ${DATABASE_URL}  # Variable de entorno en producción
```

Activar: `java -jar app.jar --spring.profiles.active=prod`

---

## 12. ¿Qué es Spring Data JPA Specifications?
**Respuesta:**
Queries dinámicos construidos programáticamente (para filtros opcionales):

```java
// En vez de crear 20 métodos de repository para cada combinación de filtros:
public Page<Producto> buscar(String nombre, String categoria, Double precioMin, Double precioMax, Pageable pageable) {
    Specification<Producto> spec = Specification.where(null);
    
    if (nombre != null) spec = spec.and((root, q, cb) -> cb.like(cb.lower(root.get("nombre")), "%" + nombre.toLowerCase() + "%"));
    if (categoria != null) spec = spec.and((root, q, cb) -> cb.equal(root.get("categoria").get("nombre"), categoria));
    if (precioMin != null) spec = spec.and((root, q, cb) -> cb.greaterThanOrEqualTo(root.get("precio"), precioMin));
    if (precioMax != null) spec = spec.and((root, q, cb) -> cb.lessThanOrEqualTo(root.get("precio"), precioMax));
    
    return productoRepo.findAll(spec, pageable);
}
```
