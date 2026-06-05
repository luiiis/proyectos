# Módulo 21: APIs REST

## CRUD Completo con Paginación y Filtros

```java
@RestController
@RequestMapping("/api/v1/productos")
public class ProductoController {

    @GetMapping
    public Page<ProductoDto> listar(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @RequestParam(defaultValue = "nombre") String sortBy,
        @RequestParam(required = false) String categoria,
        @RequestParam(required = false) BigDecimal precioMin
    ) { ... }

    @GetMapping("/{id}")
    public ProductoDto buscar(@PathVariable Long id) { ... }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDto crear(@Valid @RequestBody CrearProductoDto dto) { ... }

    @PutMapping("/{id}")
    public ProductoDto actualizar(@PathVariable Long id, @RequestBody ActualizarProductoDto dto) { ... }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { ... }
}
```

## Swagger/OpenAPI
```java
@Operation(summary = "Buscar producto por ID")
@ApiResponses({
    @ApiResponse(responseCode = "200", description = "Producto encontrado"),
    @ApiResponse(responseCode = "404", description = "No encontrado")
})
@GetMapping("/{id}")
public ProductoDto buscar(@PathVariable Long id) { ... }
```

## Ejercicios
1. Implementa CRUD completo para Clientes con paginación
2. Agrega filtros: por ciudad, tipo, rango de fechas
3. Documenta con Swagger y prueba desde la UI
4. Implementa HATEOAS (links en las respuestas)
5. Versiona tu API (v1, v2)
