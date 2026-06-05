package com.erp.controller;

import com.erp.entity.Producto;
import com.erp.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {
    private final ProductoService service;
    public ProductoController(ProductoService s) { this.service = s; }

    @GetMapping
    public Page<Producto> listar(Pageable pageable) { return service.listar(pageable); }

    @GetMapping("/{id}")
    public Producto buscar(@PathVariable Long id) { return service.buscarPorId(id); }

    @GetMapping("/buscar")
    public List<Producto> buscar(@RequestParam String q) { return service.buscar(q); }

    @GetMapping("/stock-bajo")
    public List<Producto> stockBajo(@RequestParam(defaultValue = "10") int min) { return service.stockBajo(min); }

    @PostMapping @ResponseStatus(HttpStatus.CREATED)
    public Producto crear(@RequestBody Producto p) { return service.crear(p); }

    @PutMapping("/{id}")
    public Producto actualizar(@PathVariable Long id, @RequestBody Producto p) { return service.actualizar(id, p); }

    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) { service.eliminar(id); }
}
