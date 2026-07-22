# Preguntas de Entrevista - Tema: JPA / Hibernate / Spring Data

## Nivel Junior

### 1. ¿Qué es JPA y qué es Hibernate?
**Respuesta:**
- **JPA** (Jakarta Persistence API): es una ESPECIFICACIÓN (define interfaces y reglas). No tiene código ejecutable.
- **Hibernate**: es una IMPLEMENTACIÓN de JPA (el que realmente hace el trabajo).
- Analogía: JPA es el plano de una casa, Hibernate es la constructora que la construye.

Spring Data JPA agrega una capa más encima: genera implementaciones de Repository automáticamente.

---

### 2. ¿Qué es una Entity?
**Respuesta:**
Una clase Java que representa una tabla de la BD:
```java
@Entity
@Table(name = "productos")
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    private Double precio;
}
// Cada instancia de Producto = 1 fila en la tabla "productos"
```

---

### 3. ¿Cuál es la diferencia entre `save()` y `saveAll()`?
**Respuesta:**
- `save(entity)`: guarda 1 entidad. Usa INSERT si es nueva, UPDATE si ya existe (verifica si tiene ID).
- `saveAll(list)`: guarda múltiples entidades en batch. Más eficiente que llamar save() en un loop.

---

### 4. ¿Qué son los Derived Query Methods?
**Respuesta:**
Spring Data JPA genera SQL a partir del nombre del método:
```java
List<Producto> findByCategoria(String categoria);
// → SELECT * FROM productos WHERE categoria = ?

List<Producto> findByPrecioGreaterThanOrderByNombreAsc(Double precio);
// → SELECT * FROM productos WHERE precio > ? ORDER BY nombre ASC

Optional<Producto> findByNombreIgnoreCase(String nombre);
// → SELECT * FROM productos WHERE LOWER(nombre) = LOWER(?)
```
No necesitas escribir SQL: el nombre del método ES la query.

---

### 5. ¿Qué diferencia hay entre `findById()` que retorna Optional vs lanzar excepción?
**Respuesta:**
```java
// Optional (fuerza al caller a manejar "no encontrado"):
Optional<Producto> p = repo.findById(1L);
Producto producto = p.orElseThrow(() -> new ProductoNotFoundException(1L));

// Directo (menos seguro, puede dar NullPointerException):
Producto p = repo.findById(1L).get();  // ← PELIGRO si no existe
```
Siempre usar `orElseThrow()` o `orElse()`. Nunca `.get()` sin verificar.

---

## Nivel Mid

### 6. ¿Qué es el problema N+1 y cómo resolverlo?
**Respuesta:**
Al cargar una entidad con relaciones, Hibernate hace 1 query para la entidad principal + N queries para cada relación:
```
SELECT * FROM productos          -- 1 query (10 productos)
SELECT * FROM categorias WHERE id = 1  -- +1
SELECT * FROM categorias WHERE id = 2  -- +1
... (10 queries extra)
```

Soluciones:
- `@EntityGraph`: indica qué relaciones cargar en un solo JOIN
- `JOIN FETCH` en JPQL: `SELECT p FROM Producto p JOIN FETCH p.categoria`
- `@BatchSize(size=50)`: agrupa las queries en lotes

---

### 7. ¿Cuál es la diferencia entre FetchType.LAZY y EAGER?
**Respuesta:**
- **LAZY**: la relación NO se carga hasta que la accedes (query bajo demanda)
- **EAGER**: la relación se carga SIEMPRE junto con la entidad principal

```java
@ManyToOne(fetch = FetchType.LAZY)  // Solo carga categoría cuando llames getCategoria()
private Categoria categoria;

@ManyToOne(fetch = FetchType.EAGER)  // Siempre carga categoría (JOIN automático)
private Categoria categoria;
```

Regla: usa LAZY por defecto. EAGER puede causar queries pesados inesperados.

---

### 8. ¿Qué es el contexto de persistencia (Persistence Context)?
**Respuesta:**
Es un "caché de primer nivel" que Hibernate mantiene durante una transacción:
- Si llamas `findById(1)` dos veces → solo 1 query a BD (la segunda lee del caché)
- Si modificas una entidad managed → Hibernate detecta el cambio y hace UPDATE automáticamente (dirty checking)
- Se limpia al terminar la transacción

---

### 9. ¿Cuándo usar @Query en vez de Derived Query Methods?
**Respuesta:**
Cuando el nombre del método se vuelve ilegible o necesitas queries complejas:
```java
// Derived (nombre demasiado largo):
List<Producto> findByActivoTrueAndPrecioGreaterThanAndCategoriaIdOrderByPrecioDesc(...);

// @Query (más claro):
@Query("SELECT p FROM Producto p WHERE p.activo = true AND p.precio > :min AND p.categoria.id = :catId ORDER BY p.precio DESC")
List<Producto> buscarProductosFiltrados(@Param("min") Double min, @Param("catId") Long catId);
```

---

### 10. ¿Qué es `ddl-auto` y por qué NO usarlo en producción?
**Respuesta:**
`spring.jpa.hibernate.ddl-auto` controla qué hace Hibernate con el esquema de BD:
- `create`: borra y recrea tablas (DESTRUYE datos)
- `create-drop`: como create + borra al apagar
- `update`: modifica tablas sin borrar datos (puede NO funcionar bien)
- `validate`: solo verifica que las entidades coincidan con la BD
- `none`: no toca el esquema

**En producción**: usar `validate` o `none` + migraciones con Flyway/Liquibase.
`update` en producción puede generar columnas extra, nunca borra columnas, y no es reversible.

---

## Nivel Senior

### 11. ¿Cómo manejas transacciones en Spring?
**Respuesta:**
Con `@Transactional`:
```java
@Transactional
public void registrarVenta(VentaRequest req) {
    Venta venta = ventaRepo.save(new Venta(...));      // INSERT
    for (var item : req.items()) {
        productoRepo.descontarStock(item.id(), item.cantidad());  // UPDATE
        detalleRepo.save(new DetalleVenta(venta, item));          // INSERT
    }
    // Si algún paso falla → RuntimeException → ROLLBACK automático de TODO
}
```
Reglas:
- Solo funciona en métodos públicos
- Solo hace rollback en RuntimeExceptions (por defecto)
- Propagación por defecto: REQUIRED (reutiliza transacción existente)

---

### 12. ¿Cómo optimizarías un endpoint que devuelve 1 millón de registros?
**Respuesta:**
1. **NUNCA** devolver 1 millón de golpe. Paginar obligatoriamente.
2. Si es exportación: usar streaming (`Stream<T>`) con `@Transactional(readOnly = true)`
3. Proyecciones: devolver solo campos necesarios (no entidades completas)
4. Índices en BD para los filtros más comunes
5. Caché si los datos no cambian frecuentemente
6. Considerar CQRS: tabla desnormalizada solo para lecturas
