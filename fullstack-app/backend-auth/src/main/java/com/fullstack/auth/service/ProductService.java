package com.fullstack.auth.service;

import com.fullstack.auth.dto.ProductDto;
import com.fullstack.auth.entity.oracle.Inventory;
import com.fullstack.auth.entity.oracle.Product;
import com.fullstack.auth.repository.oracle.InventoryRepository;
import com.fullstack.auth.repository.oracle.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio de gestión de productos e inventario.
 * Opera contra la base de datos Oracle.
 * 
 * CACHÉ:
 * - @Cacheable: Guarda el resultado en Redis. Si ya existe, lo devuelve sin consultar la BD.
 * - @CacheEvict: Borra el caché cuando los datos cambian (create, update, delete).
 * 
 * Esto significa:
 * - getAllProducts() → primera vez consulta Oracle, después lee de Redis (5 min TTL).
 * - createProduct() → borra el caché para que la próxima consulta traiga datos frescos.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    /**
     * @Cacheable("products") → El resultado se guarda en Redis con clave "products::SimpleKey[]"
     * Las siguientes llamadas devuelven el dato de Redis sin tocar Oracle.
     * TTL configurado en RedisConfig: 5 minutos.
     */
    @Cacheable(value = "products", key = "'all'")
    public List<ProductDto> getAllProducts() {
        return productRepository.findByIsActiveTrue().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "products", key = "#id")
    public ProductDto getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        return mapToDto(product);
    }

    public List<ProductDto> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getByCategory(String category) {
        return productRepository.findByCategory(category).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ProductDto> getLowStock(Integer threshold) {
        return productRepository.findByStockLessThan(threshold).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    /**
     * @CacheEvict(allEntries = true) → Borra TODO el caché de "products".
     * Se usa en operaciones de escritura para que el caché no quede desactualizado.
     */
    @Transactional("oracleTransactionManager")
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto createProduct(ProductDto dto) {
        Product product = Product.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .stock(dto.getStock() != null ? dto.getStock() : 0)
                .category(dto.getCategory())
                .sku(dto.getSku())
                .build();

        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }

    @Transactional("oracleTransactionManager")
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto updateProduct(Long id, ProductDto dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));

        if (dto.getName() != null) product.setName(dto.getName());
        if (dto.getDescription() != null) product.setDescription(dto.getDescription());
        if (dto.getPrice() != null) product.setPrice(dto.getPrice());
        if (dto.getCategory() != null) product.setCategory(dto.getCategory());
        if (dto.getSku() != null) product.setSku(dto.getSku());
        if (dto.getIsActive() != null) product.setIsActive(dto.getIsActive());

        Product saved = productRepository.save(product);
        return mapToDto(saved);
    }

    @Transactional("oracleTransactionManager")
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
        product.setIsActive(false); // Soft delete
        productRepository.save(product);
    }

    @Transactional("oracleTransactionManager")
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto addStock(Long productId, Integer quantity, String reason, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        product.setStock(product.getStock() + quantity);
        productRepository.save(product);

        Inventory movement = Inventory.builder()
                .product(product)
                .type(Inventory.MovementType.ENTRY)
                .quantity(quantity)
                .reason(reason)
                .createdBy(username)
                .build();
        inventoryRepository.save(movement);

        return mapToDto(product);
    }

    @Transactional("oracleTransactionManager")
    @CacheEvict(value = "products", allEntries = true)
    public ProductDto removeStock(Long productId, Integer quantity, String reason, String username) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (product.getStock() < quantity) {
            throw new RuntimeException("Stock insuficiente. Disponible: " + product.getStock());
        }

        product.setStock(product.getStock() - quantity);
        productRepository.save(product);

        Inventory movement = Inventory.builder()
                .product(product)
                .type(Inventory.MovementType.EXIT)
                .quantity(quantity)
                .reason(reason)
                .createdBy(username)
                .build();
        inventoryRepository.save(movement);

        return mapToDto(product);
    }

    private ProductDto mapToDto(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .category(product.getCategory())
                .sku(product.getSku())
                .isActive(product.getIsActive())
                .build();
    }
}
