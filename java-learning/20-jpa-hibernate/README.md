# Módulo 20: JPA e Hibernate

## Entity + Repository = CRUD sin escribir SQL

```java
@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    private BigDecimal precio;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
    private List<DetalleVenta> detalles = new ArrayList<>();
}

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByActivoTrue();
    Page<Producto> findByCategoria(Categoria cat, Pageable pageable);
    @Query("SELECT p FROM Producto p WHERE p.precio BETWEEN :min AND :max")
    List<Producto> findByRangoPrecio(@Param("min") BigDecimal min, @Param("max") BigDecimal max);
}
```

## Relaciones
```java
@OneToOne    // 1:1 (Empleado ↔ Usuario)
@OneToMany   // 1:N (Categoria → Productos)
@ManyToOne   // N:1 (Producto → Categoria)
@ManyToMany  // M:N (Usuario ↔ Roles) con @JoinTable
```

## Fetch Types
- `LAZY`: carga la relación SOLO cuando la accedes (recomendado)
- `EAGER`: carga la relación SIEMPRE (puede causar N+1 queries)

## Ejercicios
1. Mapea las 16 tablas del sistema empresarial como entidades JPA
2. Implementa paginación con Pageable
3. Resuelve el problema N+1 con @EntityGraph o JOIN FETCH
4. Implementa auditoría automática con @CreatedDate, @LastModifiedDate
