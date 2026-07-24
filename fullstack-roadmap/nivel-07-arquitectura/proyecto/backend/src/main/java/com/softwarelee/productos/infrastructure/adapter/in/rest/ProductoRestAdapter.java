package com.softwarelee.productos.infrastructure.adapter.in.rest;

import com.softwarelee.productos.domain.model.Producto;
import com.softwarelee.productos.domain.ports.in.ConsultarProductoUseCase;
import com.softwarelee.productos.domain.ports.in.CrearProductoUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ADAPTADOR DE ENTRADA (REST): Traduce HTTP → casos de uso.
 *
 * Este controller NO contiene lógica de negocio.
 * Solo traduce la petición HTTP a una llamada al caso de uso.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoRestAdapter {

    private final ConsultarProductoUseCase consultarUseCase;
    private final CrearProductoUseCase crearUseCase;

    public ProductoRestAdapter(ConsultarProductoUseCase consultarUseCase,
                               CrearProductoUseCase crearUseCase) {
        this.consultarUseCase = consultarUseCase;
        this.crearUseCase = crearUseCase;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Producto> productos = consultarUseCase.listarTodos();
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "total", productos.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Producto producto = consultarUseCase.buscarPorId(id);
        return ResponseEntity.ok(Map.of("success", true, "data", producto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = consultarUseCase.buscarPorNombre(nombre);
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "total", productos.size()));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Map<String, Object>> buscarPorCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = consultarUseCase.buscarPorCategoria(categoriaId);
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "total", productos.size()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Producto producto) {
        Producto creado = crearUseCase.ejecutar(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "message", "Producto creado", "data", creado));
    }
}
