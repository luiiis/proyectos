package com.softwarelee.productos.controller;

import com.softwarelee.productos.model.Categoria;
import com.softwarelee.productos.service.CategoriaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(Map.of("success", true, "data", categorias, "total", categorias.size()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(@PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(Map.of("success", true, "data", categoria));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Map<String, Object>> buscarPorNombre(@RequestParam String nombre) {
        List<Categoria> categorias = categoriaService.buscarPorNombre(nombre);
        return ResponseEntity.ok(Map.of("success", true, "data", categorias, "total", categorias.size()));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Categoria categoria) {
        Categoria creada = categoriaService.crear(categoria);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("success", true, "message", "Categoría creada", "data", creada));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> actualizar(@PathVariable Long id, @RequestBody Categoria categoria) {
        Categoria actualizada = categoriaService.actualizar(id, categoria);
        return ResponseEntity.ok(Map.of("success", true, "message", "Categoría actualizada", "data", actualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> eliminar(@PathVariable Long id) {
        categoriaService.eliminar(id);
        return ResponseEntity.ok(Map.of("success", true, "message", "Categoría eliminada"));
    }
}
