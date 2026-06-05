# Spring Boot Completo - Parte 4: REST API Completa

## Estructura de un endpoint profesional

```java
// DTO de entrada (lo que envia el cliente)
public record ProductoRequest(
    @NotBlank(message = "Nombre requerido") String nombre,
    String descripcion,
    @NotNull @DecimalMin("0.01") BigDecimal precio,
    @Min(0) int stock,
    String sku,
    String categoria
) {}

// DTO de salida (lo que responde el servidor)
public record ProductoResponse(
    Long id, String nombre, String descripcion,
    BigDecimal precio, int stock, String sku,
    String categoria, boolean activo, Instant createdAt
) {}

// Respuesta estandar wrapper
public record ApiResponse<T>(boolean success, String message, T data, Instant timestamp) {
    public static <T> ApiResponse<T> ok(T data) { return new ApiResponse<>(true, "OK", data, Instant.now()); }
    public static <T> ApiResponse<T> error(String msg) { return new ApiResponse<>(false, msg, null, Instant.now()); }
}
```

## Controller completo con todas las operaciones

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) { this.service = service; }

    // LISTAR con paginacion, ordenamiento y filtros
    // GET /api/productos?page=0&size=20&sort=precio,desc&categoria=Electronica
    @GetMapping
    public Page<ProductoResponse> listar(
            Pageable pageable,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String buscar) {
        if (buscar != null) return service.buscar(buscar, pageable);
        if (categoria != null) return service.porCategoria(categoria, pageable);
        return service.listar(pageable);
    }

    // BUSCAR por ID
    // GET /api/productos/5
    @GetMapping("/{id}")
    public ProductoResponse buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // CREAR
    // POST /api/productos (body: JSON)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest request) {
        return service.crear(request);
    }

    // ACTUALIZAR completo
    // PUT /api/productos/5
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ProductoResponse actualizar(@PathVariable Long id, @RequestBody ProductoRequest request) {
        return service.actualizar(id, request);
    }

    // ELIMINAR (soft delete)
    // DELETE /api/productos/5
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
```

## Service con logica de negocio

```java
@Service
@Transactional(readOnly = true)
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) { this.repo = repo; }

    @Cacheable(value = "productos", key = "'page:'+#pageable.pageNumber")
    public Page<ProductoResponse> listar(Pageable pageable) {
        return repo.findByActivoTrue(pageable).map(this::toResponse);
    }

    public ProductoResponse buscarPorId(Long id) {
        return repo.findById(id)
            .filter(Producto::isActivo)
            .map(this::toResponse)
            .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
    }

    @Transactional
    @CacheEvict(value = "productos", allEntries = true)
    public ProductoResponse crear(ProductoRequest req) {
        // Validacion de negocio
        if (req.sku() != null && repo.existsBySku(req.sku())) {
            throw new BusinessException("SKU ya existe: " + req.sku());
        }

        var producto = new Producto();
        producto.setNombre(req.nombre());
        producto.setDescripcion(req.descripcion());
        producto.setPrecio(req.precio());
        producto.setStock(req.stock());
        producto.setSku(req.sku());
        producto.setCategoria(req.categoria());

        return toResponse(repo.save(producto));
    }

    @Transactional
    @CacheEvict(value = "productos", allEntries = true)
    public ProductoResponse actualizar(Long id, ProductoRequest req) {
        var producto = repo.findById(id)
            .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));

        if (req.nombre() != null) producto.setNombre(req.nombre());
        if (req.precio() != null) producto.setPrecio(req.precio());
        if (req.descripcion() != null) producto.setDescripcion(req.descripcion());
        if (req.categoria() != null) producto.setCategoria(req.categoria());
        producto.setStock(req.stock());

        return toResponse(repo.save(producto));
    }

    @Transactional
    @CacheEvict(value = "productos", allEntries = true)
    public void eliminar(Long id) {
        var producto = repo.findById(id)
            .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
        producto.setActivo(false);  // Soft delete (nunca borrar fisicamente)
        repo.save(producto);
    }

    private ProductoResponse toResponse(Producto p) {
        return new ProductoResponse(p.getId(), p.getNombre(), p.getDescripcion(),
            p.getPrecio(), p.getStock(), p.getSku(), p.getCategoria(),
            p.isActivo(), p.getCreatedAt());
    }
}
```

## Manejo global de errores

```java
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 404: recurso no encontrado
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(404).body(ApiResponse.error(ex.getMessage()));
    }

    // 400: error de validacion (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
            .collect(Collectors.toMap(FieldError::getField, e -> e.getDefaultMessage()));
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Validacion fallida", errors, Instant.now()));
    }

    // 409: conflicto de negocio
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusiness(BusinessException ex) {
        return ResponseEntity.status(409).body(ApiResponse.error(ex.getMessage()));
    }

    // 500: error inesperado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneric(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(500).body(ApiResponse.error("Error interno del servidor"));
    }
}

// Excepciones custom
public class NotFoundException extends RuntimeException {
    public NotFoundException(String msg) { super(msg); }
}
public class BusinessException extends RuntimeException {
    public BusinessException(String msg) { super(msg); }
}
```

## Swagger/OpenAPI (documentacion automatica)

```java
// Solo agregar la dependencia en pom.xml:
// springdoc-openapi-starter-webmvc-ui

// Acceder a: http://localhost:8080/swagger-ui.html
// Genera documentacion interactiva de TODOS tus endpoints automaticamente

// Personalizar con anotaciones (opcional):
@Operation(summary = "Buscar producto por ID")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
@GetMapping("/{id}")
public ProductoResponse buscar(@PathVariable Long id) { ... }
```

## Probar la API con curl

```bash
# Registrar usuario
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123","email":"admin@mail.com"}'

# Login (obtener token)
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"admin123"}' | grep -o '"token":"[^"]*"' | cut -d'"' -f4)

# Listar productos (con token)
curl http://localhost:8080/api/productos -H "Authorization: Bearer $TOKEN"

# Crear producto
curl -X POST http://localhost:8080/api/productos \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop HP","precio":18999.99,"stock":25,"sku":"LAP-001","categoria":"Electronica"}'

# Buscar por ID
curl http://localhost:8080/api/productos/1 -H "Authorization: Bearer $TOKEN"

# Actualizar
curl -X PUT http://localhost:8080/api/productos/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nombre":"Laptop HP Actualizada","precio":19999.99,"stock":20}'

# Eliminar
curl -X DELETE http://localhost:8080/api/productos/1 -H "Authorization: Bearer $TOKEN"

# Paginacion
curl "http://localhost:8080/api/productos?page=0&size=5&sort=precio,desc" \
  -H "Authorization: Bearer $TOKEN"
```
