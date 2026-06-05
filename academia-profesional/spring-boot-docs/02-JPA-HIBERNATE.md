# Spring Boot Completo - Parte 2: JPA e Hibernate

## Que es JPA

JPA (Java Persistence API) es una ESPECIFICACION que define como mapear
objetos Java a tablas de base de datos. Hibernate es la IMPLEMENTACION mas usada.

```
Tu codigo Java → JPA (especificacion) → Hibernate (implementacion) → JDBC → PostgreSQL
                                         genera SQL automaticamente
```

## Que es Hibernate

Hibernate es un ORM (Object-Relational Mapping):
- Mapea clases Java ↔ tablas de BD
- Genera SQL automaticamente
- Maneja relaciones (1:N, M:N)
- Cachea datos (Level 1 y Level 2 cache)
- Maneja transacciones

## Entity: Mapear tabla a clase Java

```java
@Entity                              // "Esta clase es una tabla"
@Table(name = "productos")           // Nombre de la tabla en BD
public class Producto {

    @Id                              // Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Auto-increment
    private Long id;

    @Column(nullable = false, length = 150)  // NOT NULL, VARCHAR(150)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(nullable = false, precision = 10, scale = 2)  // NUMERIC(10,2)
    private BigDecimal precio;

    @Column(nullable = false)
    private int stock;

    @Column(unique = true, length = 50)  // UNIQUE constraint
    private String sku;

    @Column(name = "is_active")      // Nombre diferente en BD
    private boolean activo = true;

    @Column(name = "created_at", updatable = false)  // No se actualiza
    private Instant createdAt;

    @PrePersist                       // Se ejecuta ANTES de INSERT
    void onCreate() {
        this.createdAt = Instant.now();
    }

    @PreUpdate                       // Se ejecuta ANTES de UPDATE
    void onUpdate() {
        this.updatedAt = Instant.now();
    }

    // Getters y Setters...
}
```

## Relaciones entre entidades

### Muchos a Uno (N:1) - La mas comun
```java
// Un producto pertenece a UNA categoria
// Muchos productos pueden tener la misma categoria
@Entity
public class Producto {
    @ManyToOne(fetch = FetchType.LAZY)   // LAZY = no cargar hasta que se use
    @JoinColumn(name = "categoria_id")    // Nombre de la FK en la tabla
    private Categoria categoria;
}

// SQL generado: productos.categoria_id → categorias.id
```

### Uno a Muchos (1:N)
```java
// Una categoria tiene MUCHOS productos
@Entity
public class Categoria {
    @OneToMany(mappedBy = "categoria")  // "categoria" = nombre del campo en Producto
    private List<Producto> productos = new ArrayList<>();
}
```

### Muchos a Muchos (M:N)
```java
// Un usuario tiene muchos roles, un rol tiene muchos usuarios
@Entity
public class Usuario {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_roles",                          // Tabla pivote
        joinColumns = @JoinColumn(name = "usuario_id"),  // FK a esta tabla
        inverseJoinColumns = @JoinColumn(name = "rol_id") // FK a la otra tabla
    )
    private Set<Rol> roles = new HashSet<>();
}
// Crea tabla: usuario_roles (usuario_id, rol_id)
```

### Uno a Uno (1:1)
```java
@Entity
public class Empleado {
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "expediente_id", unique = true)
    private Expediente expediente;
}
```

## LAZY vs EAGER

```java
// LAZY (recomendado): carga la relacion SOLO cuando la accedes
@ManyToOne(fetch = FetchType.LAZY)
private Categoria categoria;
// SELECT * FROM productos WHERE id = 1  (sin JOIN)
// Solo si haces producto.getCategoria() → SELECT * FROM categorias WHERE id = X

// EAGER: carga la relacion SIEMPRE (puede causar N+1 queries)
@ManyToOne(fetch = FetchType.EAGER)
private Categoria categoria;
// SELECT * FROM productos p JOIN categorias c ON p.categoria_id = c.id WHERE p.id = 1

// Problema N+1:
// Si cargas 100 productos con EAGER en categoria:
//   1 query para productos + 100 queries para categorias = 101 queries (LENTO)
// Solucion: usar LAZY + @EntityGraph o JOIN FETCH cuando necesites la relacion
```

## Repository: Acceso a datos sin escribir SQL

```java
// Spring Data JPA genera la implementacion AUTOMATICAMENTE
// Solo defines la INTERFAZ con el nombre del metodo
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    // Spring genera: SELECT * FROM productos WHERE activo = true
    List<Producto> findByActivoTrue();

    // SELECT * FROM productos WHERE nombre ILIKE '%?%'
    List<Producto> findByNombreContainingIgnoreCase(String nombre);

    // SELECT * FROM productos WHERE categoria_id = ? AND precio < ?
    List<Producto> findByCategoriaIdAndPrecioLessThan(Long catId, BigDecimal precio);

    // SELECT * FROM productos WHERE precio BETWEEN ? AND ?
    List<Producto> findByPrecioBetween(BigDecimal min, BigDecimal max);

    // SELECT * FROM productos ORDER BY precio DESC LIMIT ?
    List<Producto> findTop10ByOrderByPrecioDesc();

    // Con paginacion
    Page<Producto> findByActivoTrue(Pageable pageable);

    // Query personalizada (JPQL)
    @Query("SELECT p FROM Producto p WHERE p.stock < :minimo AND p.activo = true")
    List<Producto> findStockBajo(@Param("minimo") int minimo);

    // Query nativa (SQL directo)
    @Query(value = "SELECT * FROM productos WHERE precio > :precio", nativeQuery = true)
    List<Producto> findCaros(@Param("precio") BigDecimal precio);

    // Contar
    long countByCategoriaId(Long categoriaId);

    // Existe
    boolean existsBySku(String sku);
}
```

### Convencion de nombres → SQL automatico

```
findBy + Campo + Condicion

findByNombre(String n)                    → WHERE nombre = ?
findByPrecioGreaterThan(BigDecimal p)     → WHERE precio > ?
findByPrecioLessThanEqual(BigDecimal p)   → WHERE precio <= ?
findByNombreContaining(String n)          → WHERE nombre LIKE '%?%'
findByNombreStartingWith(String n)        → WHERE nombre LIKE '?%'
findByActivoTrue()                        → WHERE activo = true
findByEmailIsNull()                       → WHERE email IS NULL
findByIdIn(List<Long> ids)                → WHERE id IN (?, ?, ?)
findByCategoriaIdOrderByPrecioDesc(Long)  → WHERE cat_id = ? ORDER BY precio DESC
countByActivoTrue()                       → SELECT COUNT(*) WHERE activo = true
existsByEmail(String e)                   → SELECT EXISTS(... WHERE email = ?)
deleteByActivoFalse()                     → DELETE FROM ... WHERE activo = false
```

## Paginacion

```java
// Controller
@GetMapping
public Page<Producto> listar(Pageable pageable) {
    return repository.findByActivoTrue(pageable);
}
// Llamar: GET /api/productos?page=0&size=20&sort=precio,desc
// page=0 (primera pagina), size=20 (20 por pagina), sort=precio,desc (ordenar)

// Respuesta automatica:
{
  "content": [...],           // Los 20 productos
  "totalElements": 500,       // Total en BD
  "totalPages": 25,           // 500/20 = 25 paginas
  "number": 0,                // Pagina actual
  "size": 20,                 // Tamano de pagina
  "first": true,              // Es la primera pagina?
  "last": false               // Es la ultima?
}
```

## Transacciones

```java
@Service
@Transactional(readOnly = true)  // Default: solo lectura (optimizacion)
public class VentaService {

    @Transactional  // Este metodo ESCRIBE (override del readOnly)
    public Venta registrar(VentaRequest request) {
        // 1. Crear venta
        var venta = new Venta();
        venta.setNumero(generarNumero());
        ventaRepo.save(venta);

        // 2. Para cada producto: descontar stock
        for (var item : request.items()) {
            var producto = productoRepo.findById(item.productoId())
                .orElseThrow(() -> new RuntimeException("No encontrado"));
            producto.setStock(producto.getStock() - item.cantidad());
            productoRepo.save(producto);
        }

        // 3. Si ALGO falla en cualquier punto → ROLLBACK automatico
        //    (la venta no se crea Y el stock no se descuenta)
        return venta;
    }
}
```
