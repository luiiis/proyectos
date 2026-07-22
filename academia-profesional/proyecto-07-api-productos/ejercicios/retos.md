# Ejercicios y Retos - Proyecto 07: API Productos con JPA

## Reto 1: Soft Delete
En vez de borrar productos de la BD, implementa "eliminación lógica":
- Agrega campo `boolean eliminado = false` en la entidad
- `DELETE /api/productos/{id}` solo marca `eliminado = true`
- Todos los queries excluyen productos eliminados: `findByEliminadoFalse()`

**Lo que practicas:** Soft delete, queries derivados JPA, lógica de negocio

---

## Reto 2: Auditoría automática
Usa las anotaciones de JPA para auditoría:
```java
@CreatedDate
private LocalDateTime creadoEn;

@LastModifiedDate
private LocalDateTime modificadoEn;

@CreatedBy
private String creadoPor;
```
Agrega `@EnableJpaAuditing` en la configuración.

**Lo que practicas:** JPA Auditing, @EntityListeners

---

## Reto 3: Especificaciones dinámicas
Implementa búsqueda dinámica con Spring Data Specifications:
```
GET /api/productos?nombre=Laptop&precioMin=10000&precioMax=20000&categoria=Electronica
```
Cada filtro es opcional. Solo se aplican los que se envían.

**Pista:** Usa `Specification<Producto>` y combínalas con `.and()`:
```java
Specification<Producto> spec = Specification.where(null);
if (nombre != null) spec = spec.and(nombreContains(nombre));
if (precioMin != null) spec = spec.and(precioMayorA(precioMin));
```

**Lo que practicas:** Specification pattern, queries dinámicos, Criteria API

---

## Reto 4: Proyecciones
Crea diferentes "vistas" del mismo producto:
```java
// Vista resumida (para listados)
public interface ProductoResumen {
    Long getId();
    String getNombre();
    Double getPrecio();
}

// Vista completa (para detalle)
// Ya existe: la entidad Producto
```
Endpoint: `GET /api/productos?vista=resumida` devuelve solo id, nombre, precio.

**Lo que practicas:** Projections en Spring Data JPA, performance

---

## Reto 5: Importación masiva
Crea `POST /api/productos/importar` que reciba una lista de productos y los guarde todos:
- Usar `saveAll()` (batch insert)
- Validar cada producto
- Si uno falla, ¿se guardan los demás o se rechaza todo? (decidir y documentar)

**Lo que practicas:** Batch operations, transacciones, manejo de errores parciales

---

## Reto 6: Cache con @Cacheable
Sin Redis (solo caché en memoria):
```java
@Cacheable("productos")
public Page<Producto> listar(Pageable pageable) { ... }

@CacheEvict(value = "productos", allEntries = true)
public Producto crear(Producto p) { ... }
```
Verifica que la 2da llamada no ejecuta query en BD (no aparece SQL en logs).

**Lo que practicas:** Spring Cache abstraction, @Cacheable/@CacheEvict

---

## Reto 7: Tests de integración
Escribe tests para tu API:
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ProductoApiTest {
    @Autowired
    TestRestTemplate restTemplate;
    
    @Test
    void crearProducto_debeRetornar201() {
        var request = new ProductoRequest("Test", 999.99, 10, "Test");
        var response = restTemplate.postForEntity("/api/productos", request, Producto.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}
```

**Lo que practicas:** @SpringBootTest, TestRestTemplate, assertions

---

## Reto 8 (Avanzado): Versionado de BD con Flyway
Reemplaza `ddl-auto: update` con migraciones Flyway:
```
src/main/resources/db/migration/
├── V1__crear_tabla_productos.sql
├── V2__agregar_campo_imagen.sql
└── V3__insertar_datos_prueba.sql
```

**Lo que practicas:** Flyway, migraciones, versionado de esquema
