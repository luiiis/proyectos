package com.academia.ventas.controller;

import com.academia.ventas.entity.Producto;
import com.academia.ventas.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoRepository repo;

    public ProductoController(ProductoRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Producto> listar() { return repo.findByActivoTrue(); }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo() { return repo.findByStockLessThan(5); }

    @PostMapping
    public Producto crear(@RequestBody Producto p) { return repo.save(p); }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto datos) {
        Producto p = repo.findById(id).orElseThrow();
        p.setNombre(datos.getNombre());
        p.setPrecio(datos.getPrecio());
        p.setStock(datos.getStock());
        return repo.save(p);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        Producto p = repo.findById(id).orElseThrow();
        p.setActivo(false);
        repo.save(p);
        return ResponseEntity.noContent().build();
    }
}
