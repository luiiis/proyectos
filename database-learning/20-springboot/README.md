# Módulo 20: Java Spring Boot + JPA/Hibernate

## ¿Cómo se conecta Java con la Base de Datos?

```
Aplicación Java
      │
      ▼
┌─────────────────┐
│  Spring Boot    │  ← Framework que configura todo automáticamente
│  ┌───────────┐  │
│  │Controller │  │  ← Recibe peticiones HTTP (REST API)
│  └─────┬─────┘  │
│        ▼        │
│  ┌───────────┐  │
│  │  Service  │  │  ← Lógica de negocio
│  └─────┬─────┘  │
│        ▼        │
│  ┌───────────┐  │
│  │Repository │  │  ← Acceso a datos (genera SQL automáticamente)
│  └─────┬─────┘  │
│        ▼        │
│  ┌───────────┐  │
│  │JPA/Hibern.│  │  ← ORM: mapea objetos Java ↔ tablas SQL
│  └─────┬─────┘  │
└────────┼────────┘
         ▼
┌─────────────────┐
│   PostgreSQL    │  ← Base de datos
└─────────────────┘
```

---

## 1. Estructura del Proyecto Spring Boot

```
src/main/java/com/empresa/
├── Application.java              ← Punto de entrada
├── config/
│   └── SecurityConfig.java       ← Configuración de seguridad
├── domain/
│   ├── entity/                   ← Clases que mapean a tablas
│   │   ├── Producto.java
│   │   ├── Cliente.java
│   │   └── Venta.java
│   ├── repository/               ← Interfaces de acceso a datos
│   │   ├── ProductoRepository.java
│   │   └── VentaRepository.java
│   └── service/                  ← Lógica de negocio
│       ├── ProductoService.java
│       └── VentaService.java
├── api/
│   ├── controller/               ← Endpoints REST
│   │   ├── ProductoController.java
│   │   └── VentaController.java
│   └── dto/                      ← Objetos de transferencia
│       ├── ProductoDto.java
│       └── VentaDto.java
└── exception/
    └── GlobalExceptionHandler.java
```

---

## 2. Entity (Mapeo tabla ↔ clase Java)

```java
// Producto.java - Cada instancia = 1 fila en la tabla "productos"
@Entity                          // "Esta clase se mapea a una tabla"
@Table(name = "productos")       // Nombre de la tabla en la BD
public class Producto {

    @Id                          // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(nullable = false, length = 150)  // NOT NULL, VARCHAR(150)
    private String nombre;

    @Column(precision = 10, scale = 2)  // NUMERIC(10,2)
    private BigDecimal precio;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)   // Relación N:1 con categorias
    @JoinColumn(name = "categoria_id")   // FK en esta tabla
    private Categoria categoria;

    @OneToMany(mappedBy = "producto")     // Relación 1:N con detalle_venta
    private List<DetalleVenta> detalles;

    // Getters, setters, constructors...
}
```

### Mapeo de relaciones:

| SQL | JPA | Ejemplo |
|-----|-----|---------|
| FK simple (N:1) | `@ManyToOne` | Producto → Categoría |
| Tabla referenciada (1:N) | `@OneToMany` | Categoría → Productos |
| Tabla pivote (M:N) | `@ManyToMany` + `@JoinTable` | Usuario ↔ Roles |
| Misma tabla (self) | `@ManyToOne` en sí misma | Empleado → Jefe |

---

## 3. Repository (Acceso a datos sin escribir SQL)

```java
// Spring Data JPA genera la implementación AUTOMÁTICAMENTE
// Solo defines la interfaz con el nombre del método
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring genera: SELECT * FROM productos WHERE activo = true
    List<Producto> findByActivoTrue();

    // SELECT * FROM productos WHERE categoria_id = ? AND precio < ?
    List<Producto> findByCategoriaIdAndPrecioLessThan(Long categoriaId, BigDecimal precio);

    // SELECT * FROM productos WHERE nombre ILIKE '%?%'
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // Query personalizada cuando el nombre del método no alcanza
    @Query("SELECT p FROM Producto p WHERE p.stock < p.stockMinimo AND p.activo = true")
    List<Producto> findConStockBajo();

    // Query nativa (SQL directo)
    @Query(value = "SELECT * FROM productos WHERE precio > :precio", nativeQuery = true)
    List<Producto> findCaros(@Param("precio") BigDecimal precio);
}
```

### Convención de nombres → SQL automático:

| Método | SQL generado |
|--------|-------------|
| `findByNombre(String n)` | `WHERE nombre = ?` |
| `findByPrecioGreaterThan(BigDecimal p)` | `WHERE precio > ?` |
| `findByNombreContaining(String n)` | `WHERE nombre LIKE '%?%'` |
| `findByCategoriaIdOrderByPrecioDesc(Long id)` | `WHERE categoria_id = ? ORDER BY precio DESC` |
| `countByCategoriaId(Long id)` | `SELECT COUNT(*) WHERE categoria_id = ?` |
| `existsByEmail(String e)` | `SELECT EXISTS(... WHERE email = ?)` |

---

## 4. Service (Lógica de negocio)

```java
@Service
@Transactional(readOnly = true)  // Optimización: por defecto solo lectura
public class ProductoService {

    private final ProductoRepository repository;

    // Constructor injection (recomendado sobre @Autowired)
    public ProductoService(ProductoRepository repository) {
        this.repository = repository;
    }

    public List<ProductoDto> listarActivos() {
        return repository.findByActivoTrue().stream()
                .map(this::toDto)  // Convertir Entity → DTO
                .toList();
    }

    public ProductoDto buscarPorId(Long id) {
        return repository.findById(id)
                .map(this::toDto)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado: " + id));
    }

    @Transactional  // Este método ESCRIBE (override del readOnly)
    public ProductoDto crear(CrearProductoDto dto) {
        var producto = new Producto();
        producto.setNombre(dto.nombre());
        producto.setPrecio(dto.precio());
        producto.setStock(dto.stock());
        // ... más campos

        var guardado = repository.save(producto);  // INSERT INTO productos ...
        return toDto(guardado);
    }

    @Transactional
    public void eliminar(Long id) {
        var producto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Producto no encontrado"));
        producto.setActivo(false);  // Soft delete
        repository.save(producto);  // UPDATE productos SET activo=false WHERE id=?
    }

    private ProductoDto toDto(Producto p) {
        return new ProductoDto(p.getId(), p.getNombre(), p.getPrecio(), p.getStock());
    }
}
```

---

## 5. Controller (Endpoints REST)

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping                    // GET /api/productos
    public List<ProductoDto> listar() {
        return service.listarActivos();
    }

    @GetMapping("/{id}")           // GET /api/productos/5
    public ProductoDto buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PostMapping                   // POST /api/productos (body: JSON)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDto crear(@Valid @RequestBody CrearProductoDto dto) {
        return service.crear(dto);
    }

    @DeleteMapping("/{id}")        // DELETE /api/productos/5
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
```

---

## 6. DTO con Records (Java 16+)

```java
// Request DTO: lo que envía el cliente
public record CrearProductoDto(
    @NotBlank String nombre,
    @NotNull @DecimalMin("0.01") BigDecimal precio,
    @Min(0) int stock,
    String categoria
) {}

// Response DTO: lo que devuelve el servidor
public record ProductoDto(
    Long id,
    String nombre,
    BigDecimal precio,
    int stock
) {}
```

---

## 7. Seguridad con JWT

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/api/productos/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }
}
```

---

## 8. application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/empresa_db
    username: postgres
    password: postgres123
  jpa:
    hibernate:
      ddl-auto: validate  # Flyway maneja migraciones
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  flyway:
    enabled: true
```

---

## 9. Ejercicios

1. Crea una Entity para `Cliente` con todas sus relaciones
2. Crea un Repository con 5 métodos de búsqueda personalizados
3. Implementa un Service con CRUD completo + validaciones de negocio
4. Crea un Controller REST con paginación (`Pageable`)
5. Implementa un endpoint que registre una venta completa (encabezado + detalles + actualizar inventario) en una transacción
6. Agrega caché Redis a las consultas de productos
7. Implementa auditoría con `@EntityListeners` y `AuditingEntityListener`
8. Escribe un test de integración con Testcontainers

---

## Siguiente Módulo
→ [21-Proyecto Final](../21-proyecto-final/README.md)
