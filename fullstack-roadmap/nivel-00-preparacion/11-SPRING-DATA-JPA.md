# Spring Data JPA vs MyBatis — Cuándo usar cada uno

## Contexto
En el roadmap usamos **MyBatis** (control total del SQL). Pero en muchas empresas usan **Spring Data JPA** (SQL auto-generado). Necesitas conocer ambos.

---

## 1. Comparación rápida

| Aspecto | MyBatis (roadmap) | Spring Data JPA |
|---------|-------------------|-----------------|
| SQL | TÚ lo escribes | Se genera automático |
| Entidades | POJOs simples | Clases con @Entity |
| Queries | XML o @Select | Nombres de métodos |
| Control | Total | Menos control |
| Performance | Óptima (tú decides) | Puede generar queries malos |
| Relaciones | JOIN manual | @OneToMany, @ManyToOne |
| Curva aprendizaje | Necesitas saber SQL | Menos SQL al inicio |
| Uso en México | Bancos, gobierno, ERP | Startups, apps web |

---

## 2. Ejemplo: lo MISMO con ambos

### Entidad con JPA
```java
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(nullable = false)
    private int existencia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @Column(nullable = false)
    private boolean activo = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters y setters...
}
```

### Repository con JPA
```java
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Queries por nombre de método (Spring las genera automáticamente)
    List<Producto> findByActivoTrue();
    List<Producto> findByNombreContainingIgnoreCase(String nombre);
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByExistenciaLessThan(int minimo);

    // Paginación (Spring la maneja automática)
    Page<Producto> findByActivoTrue(Pageable pageable);

    // Query personalizada (cuando el nombre de método no alcanza)
    @Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precio > :precio")
    List<Producto> findByPrecioMayorA(BigDecimal precio);

    // Query nativa SQL
    @Query(value = "SELECT * FROM productos WHERE activo = true ORDER BY created_at DESC LIMIT :limit",
           nativeQuery = true)
    List<Producto> findRecientes(int limit);
}
```

### Service con JPA
```java
@Service
@Transactional
public class ProductoService {

    private final ProductoRepository repo;

    public ProductoService(ProductoRepository repo) {
        this.repo = repo;
    }

    public List<Producto> listarTodos() {
        return repo.findByActivoTrue();
    }

    public Producto buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + id));
    }

    public Producto crear(Producto producto) {
        return repo.save(producto);  // INSERT automático
    }

    public Producto actualizar(Long id, Producto datos) {
        Producto producto = buscarPorId(id);
        producto.setNombre(datos.getNombre());
        producto.setPrecio(datos.getPrecio());
        producto.setExistencia(datos.getExistencia());
        return repo.save(producto);  // UPDATE automático (detecta que ya tiene ID)
    }

    public void eliminar(Long id) {
        Producto producto = buscarPorId(id);
        producto.setActivo(false);
        repo.save(producto);  // Soft delete
    }

    // Paginación
    public Page<Producto> listarPaginado(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nombre"));
        return repo.findByActivoTrue(pageable);
    }
}
```

---

## 3. Relaciones en JPA

### @ManyToOne (Producto → Categoría)
```java
// Un producto PERTENECE a una categoría
@Entity
public class Producto {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
}
```

### @OneToMany (Categoría → Productos)
```java
// Una categoría TIENE muchos productos
@Entity
public class Categoria {
    @OneToMany(mappedBy = "categoria")
    private List<Producto> productos;
}
```

### @ManyToMany (Usuario ↔ Roles)
```java
@Entity
public class Usuario {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuarios_roles",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles;
}
```

---

## 4. Paginación con JPA (automática)

### Controller
```java
@GetMapping("/paginado")
public ResponseEntity<Page<Producto>> listarPaginado(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "nombre") String sortBy) {

    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    Page<Producto> resultado = productoRepository.findByActivoTrue(pageable);
    return ResponseEntity.ok(resultado);
}
```

### Respuesta automática de Page<T>
```json
{
  "content": [
    {"id": 1, "nombre": "Laptop HP", ...},
    {"id": 2, "nombre": "Monitor Dell", ...}
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {"sorted": true, "property": "nombre"}
  },
  "totalElements": 15,
  "totalPages": 2,
  "first": true,
  "last": false
}
```

---

## 5. Transacciones con JPA

```java
@Service
@Transactional  // Todo el service es transaccional
public class VentaService {

    public Venta registrarVenta(VentaRequest request) {
        // Si CUALQUIERA de estas operaciones falla,
        // se deshace TODO (ROLLBACK automático)
        Venta venta = crearCabecera(request);
        List<VentaDetalle> detalles = crearDetalles(request, venta);
        actualizarStock(detalles);
        return venta;
    }
}
```

---

## 6. application.yml para JPA

```yaml
spring:
  jpa:
    # Mostrar SQL generado en consola (solo en desarrollo)
    show-sql: true
    properties:
      hibernate:
        format_sql: true
    # Crear/actualizar tablas automáticamente (solo dev)
    hibernate:
      ddl-auto: validate  # validate | update | create | create-drop
    # Dialecto de la BD
    database-platform: org.hibernate.dialect.MySQLDialect

  datasource:
    url: jdbc:mysql://localhost:3306/productos_db
    username: root
    password: Root123!
```

### Valores de ddl-auto
| Valor | Qué hace | Cuándo usar |
|-------|----------|-------------|
| `validate` | Solo verifica que las tablas coincidan | Producción (con Flyway) |
| `update` | Crea/modifica tablas automáticamente | Desarrollo rápido |
| `create` | Borra y recrea todo al arrancar | Tests |
| `create-drop` | Lo mismo pero también al parar | Tests |
| `none` | No toca la BD | Producción |

---

## 7. ¿Cuándo usar cada uno?

### Usa MyBatis cuando:
- Tienes queries complejas (reportes, múltiples JOINs)
- Necesitas optimizar performance al máximo
- El equipo sabe SQL
- El proyecto tiene BD legacy (tablas ya existentes)
- Necesitas stored procedures o funciones de BD

### Usa Spring Data JPA cuando:
- CRUDs simples (90% de las apps web)
- Quieres desarrollar rápido
- No necesitas queries complicadas
- Cambiar de BD es un requisito (PostgreSQL → MySQL)
- El equipo es junior en SQL

### En este roadmap:
Usamos **MyBatis** porque:
1. Aprendes SQL de verdad (fundamental para entrevistas)
2. Entiendes qué pasa debajo (no hay magia)
3. Es lo que piden en empresas grandes de México
4. Puedes migrar a JPA después fácilmente (al revés es más difícil)

---

## Siguiente paso
Entiende MyBatis a fondo en el roadmap (niveles 3-7). Si en el futuro necesitas JPA, ya tienes las bases de SQL y patrones de Spring para aprenderlo en 1-2 días.
