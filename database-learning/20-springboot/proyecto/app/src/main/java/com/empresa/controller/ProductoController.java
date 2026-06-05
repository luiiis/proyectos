package com.empresa.controller;

import com.empresa.dto.ProductoDto;
import com.empresa.service.ProductoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller: expone los endpoints HTTP.
 * Cada método maneja una ruta específica.
 */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    // GET /api/productos?page=0&size=20&sort=precio,desc
    @GetMapping
    public Page<ProductoDto> listar(Pageable pageable) {
        return service.listar(pageable);
    }

    // GET /api/productos/5
    @GetMapping("/{id}")
    public ProductoDto buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    // GET /api/productos/buscar?nombre=laptop
    @GetMapping("/buscar")
    public List<ProductoDto> buscar(@RequestParam String nombre) {
        return service.buscar(nombre);
    }

    // GET /api/productos/stock-bajo?minimo=10
    @GetMapping("/stock-bajo")
    public List<ProductoDto> stockBajo(@RequestParam(defaultValue = "10") int minimo) {
        return service.stockBajo(minimo);
    }

    // GET /api/productos/mas-vendidos?limite=10
    @GetMapping("/mas-vendidos")
    public List<ProductoDto> masVendidos(@RequestParam(defaultValue = "10") int limite) {
        return service.masVendidos(limite);
    }

    // POST /api/productos
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoDto crear(@RequestBody ProductoDto dto) {
        return service.crear(dto);
    }

    // PUT /api/productos/5
    @PutMapping("/{id}")
    public ProductoDto actualizar(@PathVariable Long id, @RequestBody ProductoDto dto) {
        return service.actualizar(id, dto);
    }

    // DELETE /api/productos/5
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}
