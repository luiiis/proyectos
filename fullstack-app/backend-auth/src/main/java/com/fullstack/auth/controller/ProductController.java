package com.fullstack.auth.controller;

import com.fullstack.auth.dto.ApiResponse;
import com.fullstack.auth.dto.ProductDto;
import com.fullstack.auth.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller de productos e inventario.
 * CRUD completo con gestión de stock.
 */
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductDto>>> getAllProducts() {
        List<ProductDto> products = productService.getAllProducts();
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductDto>> getProductById(@PathVariable Long id) {
        ProductDto product = productService.getProductById(id);
        return ResponseEntity.ok(ApiResponse.ok(product));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<ProductDto>>> searchProducts(@RequestParam String name) {
        List<ProductDto> products = productService.searchProducts(name);
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getByCategory(@PathVariable String category) {
        List<ProductDto> products = productService.getByCategory(category);
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<List<ProductDto>>> getLowStock(
            @RequestParam(defaultValue = "10") Integer threshold) {
        List<ProductDto> products = productService.getLowStock(threshold);
        return ResponseEntity.ok(ApiResponse.ok(products));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProductDto>> createProduct(@Valid @RequestBody ProductDto dto) {
        ProductDto created = productService.createProduct(dto);
        return ResponseEntity.ok(ApiResponse.ok("Producto creado", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProductDto>> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductDto dto) {
        ProductDto updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(ApiResponse.ok("Producto actualizado", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok(ApiResponse.ok("Producto eliminado", null));
    }

    @PostMapping("/{id}/stock/add")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProductDto>> addStock(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        Integer quantity = (Integer) request.get("quantity");
        String reason = (String) request.get("reason");
        ProductDto product = productService.addStock(id, quantity, reason, authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Stock agregado", product));
    }

    @PostMapping("/{id}/stock/remove")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ApiResponse<ProductDto>> removeStock(
            @PathVariable Long id,
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        Integer quantity = (Integer) request.get("quantity");
        String reason = (String) request.get("reason");
        ProductDto product = productService.removeStock(id, quantity, reason, authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok("Stock removido", product));
    }
}
