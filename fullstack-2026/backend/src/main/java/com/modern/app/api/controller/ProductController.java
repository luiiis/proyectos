package com.modern.app.api.controller;

import com.modern.app.api.dto.ProductDtos.*;
import com.modern.app.domain.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller de Productos.
 *
 * Patrón 2026:
 * - Records como DTOs (inmutables, type-safe)
 * - ApiResult<T> como wrapper estándar de respuesta
 * - @PreAuthorize con roles de Keycloak
 * - Sin lógica de negocio (solo orquesta)
 */
@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<ApiResult<List<ProductResponse>>> getAll() {
        var products = productService.findAll();
        return ResponseEntity.ok(ApiResult.ok(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResult<ProductResponse>> getById(@PathVariable Long id) {
        var product = productService.findById(id);
        return ResponseEntity.ok(ApiResult.ok(product));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResult<List<ProductResponse>>> search(@RequestParam String q) {
        var products = productService.search(q);
        return ResponseEntity.ok(ApiResult.ok(products));
    }

    @GetMapping("/categories")
    public ResponseEntity<ApiResult<List<String>>> getCategories() {
        return ResponseEntity.ok(ApiResult.ok(productService.findAllCategories()));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResult<List<ProductResponse>>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(ApiResult.ok(productService.findByCategory(category)));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<List<ProductResponse>>> getLowStock(
            @RequestParam(defaultValue = "10") int threshold) {
        return ResponseEntity.ok(ApiResult.ok(productService.findLowStock(threshold)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<ProductResponse>> create(@Valid @RequestBody CreateProduct dto) {
        var product = productService.create(dto);
        return ResponseEntity.ok(ApiResult.ok("Producto creado", product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<ProductResponse>> update(
            @PathVariable Long id,
            @RequestBody UpdateProduct dto) {
        var product = productService.update(id, dto);
        return ResponseEntity.ok(ApiResult.ok("Producto actualizado", product));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResult<Void>> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.ok(ApiResult.ok("Producto eliminado", null));
    }

    @PostMapping("/{id}/stock/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<ProductResponse>> addStock(
            @PathVariable Long id,
            @Valid @RequestBody StockMovement movement) {
        var product = productService.addStock(id, movement);
        return ResponseEntity.ok(ApiResult.ok("Stock agregado", product));
    }

    @PostMapping("/{id}/stock/remove")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResult<ProductResponse>> removeStock(
            @PathVariable Long id,
            @Valid @RequestBody StockMovement movement) {
        var product = productService.removeStock(id, movement);
        return ResponseEntity.ok(ApiResult.ok("Stock removido", product));
    }
}
