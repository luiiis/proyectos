package com.softwarelee.productos.controller;

import com.softwarelee.productos.model.Producto;
import com.softwarelee.productos.service.ProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() {
        List<Producto> productos = productoService.listarTodos();
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "total", productos.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Producto producto = productoService.buscarPorId(id);
        return ResponseEntity.ok(Map.of("success", true, "data", producto));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorNombre(@RequestParam String nombre) {
        List<Producto> productos = productoService.buscarPorNombre(nombre);
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "total", productos.size()));
    }

    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<Map<String, Object>> buscarPorCategoria(@PathVariable Long categoriaId) {
        List<Producto> productos = productoService.buscarPorCategoria(categoriaId);
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "total", productos.size()));
    }

    @GetMapping("/stock-bajo")
    public ResponseEntity<Map<String, Object>> stockBajo(@RequestParam(defaultValue = "10") int minimo) {
        List<Producto> productos = productoService.stockBajo(minimo);
        return ResponseEntity.ok(Map.of("success", true, "data", productos, "alertas", productos.size()));
    }

    @GetMapping("/paginado")
    public ResponseEntity<Map<String, Object>> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(productoService.listarPaginado(page, size));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Producto producto) {
        Producto creado = productoService.crear(producto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "message", "Producto creado", "data", creado));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        Producto actualizado = productoService.actualizar(id, producto);
        return ResponseEntity.ok(Map.of("success", true, "message", "Producto actualizado", "data", actualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Producto eliminado"));
    }
}
