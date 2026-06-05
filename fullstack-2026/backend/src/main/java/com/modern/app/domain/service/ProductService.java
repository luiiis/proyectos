package com.modern.app.domain.service;

import com.modern.app.api.dto.ProductDtos.*;
import com.modern.app.domain.entity.ProductEntity;
import com.modern.app.domain.repository.ProductRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Servicio de productos con caché Redis.
 *
 * Patrón 2026: Métodos de servicio retornan Records (DTOs inmutables).
 * La conversión Entity → Record ocurre aquí (no en el controller).
 */
@Service
@Transactional(readOnly = true) // Por defecto solo lectura (optimización)
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Cacheable(value = "products", key = "'all'")
    public List<ProductResponse> findAll() {
        return repository.findByActiveTrue().stream()
                .map(this::toResponse)
                .toList(); // .toList() en lugar de .collect(Collectors.toList()) — Java 16+
    }

    @Cacheable(value = "products", key = "#id")
    public ProductResponse findById(Long id) {
        return repository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
    }

    public List<ProductResponse> search(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<ProductResponse> findByCategory(String category) {
        return repository.findByCategory(category).stream()
                .map(this::toResponse)
                .toList();
    }

    public List<String> findAllCategories() {
        return repository.findAllCategories();
    }

    public List<ProductResponse> findLowStock(int threshold) {
        return repository.findLowStock(threshold).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional // Override: este método ESCRIBE
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse create(CreateProduct dto) {
        var entity = new ProductEntity(dto.name(), dto.price(), dto.stock(), dto.category(), dto.sku());
        entity.setDescription(dto.description());
        entity.setImageUrl(dto.imageUrl());

        var saved = repository.save(entity);
        return toResponse(saved);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse update(Long id, UpdateProduct dto) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));

        // Pattern matching con instanceof para null-safe updates (Java 21+)
        if (dto.name() instanceof String name) entity.setName(name);
        if (dto.description() instanceof String desc) entity.setDescription(desc);
        if (dto.price() != null) entity.setPrice(dto.price());
        if (dto.category() instanceof String cat) entity.setCategory(cat);
        if (dto.sku() instanceof String sku) entity.setSku(sku);
        if (dto.imageUrl() instanceof String url) entity.setImageUrl(url);
        if (dto.active() instanceof Boolean active) entity.setActive(active);

        return toResponse(repository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public void delete(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
        entity.setActive(false); // Soft delete
        repository.save(entity);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse addStock(Long id, StockMovement movement) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
        entity.addStock(movement.quantity());
        return toResponse(repository.save(entity));
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse removeStock(Long id, StockMovement movement) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado: " + id));
        entity.removeStock(movement.quantity());
        return toResponse(repository.save(entity));
    }

    // ═══════ Mapper: Entity → Record ═══════
    private ProductResponse toResponse(ProductEntity entity) {
        return new ProductResponse(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getPrice(),
                entity.getStock(),
                entity.getCategory(),
                entity.getSku(),
                entity.getImageUrl(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }

    // ═══════ Custom Exception (Sealed class) ═══════
    public static final class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }
}
