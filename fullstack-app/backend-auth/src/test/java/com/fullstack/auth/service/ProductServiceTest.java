package com.fullstack.auth.service;

import com.fullstack.auth.dto.ProductDto;
import com.fullstack.auth.entity.oracle.Inventory;
import com.fullstack.auth.entity.oracle.Product;
import com.fullstack.auth.repository.oracle.InventoryRepository;
import com.fullstack.auth.repository.oracle.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductService.
 * 
 * Usa @Nested para agrupar tests relacionados (mejor organización).
 * Cada grupo interno prueba un aspecto diferente del servicio.
 */
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;
    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private ProductService productService;

    private Product testProduct;
    private ProductDto testProductDto;

    @BeforeEach
    void setUp() {
        testProduct = Product.builder()
                .id(1L)
                .name("Laptop HP")
                .description("Laptop empresarial")
                .price(new BigDecimal("18999.99"))
                .stock(25)
                .category("Electrónica")
                .sku("HP-001")
                .isActive(true)
                .build();

        testProductDto = ProductDto.builder()
                .name("Laptop HP")
                .description("Laptop empresarial")
                .price(new BigDecimal("18999.99"))
                .stock(25)
                .category("Electrónica")
                .sku("HP-001")
                .build();
    }

    // ==================== Tests de Consulta ====================
    @Nested
    @DisplayName("Consultas de productos")
    class ConsultaTests {

        @Test
        @DisplayName("Obtener todos los productos activos")
        void getAllProducts_ReturnsActiveProducts() {
            // ARRANGE
            Product product2 = Product.builder()
                    .id(2L).name("Monitor Dell").price(new BigDecimal("12499"))
                    .stock(15).category("Electrónica").isActive(true).build();

            when(productRepository.findByIsActiveTrue())
                    .thenReturn(Arrays.asList(testProduct, product2));

            // ACT
            List<ProductDto> result = productService.getAllProducts();

            // ASSERT
            assertThat(result).hasSize(2);
            assertThat(result.get(0).getName()).isEqualTo("Laptop HP");
            assertThat(result.get(1).getName()).isEqualTo("Monitor Dell");
        }

        @Test
        @DisplayName("Obtener producto por ID existente")
        void getProductById_Exists_ReturnsProduct() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

            ProductDto result = productService.getProductById(1L);

            assertThat(result.getName()).isEqualTo("Laptop HP");
            assertThat(result.getPrice()).isEqualByComparingTo(new BigDecimal("18999.99"));
        }

        @Test
        @DisplayName("Obtener producto por ID inexistente - lanza excepción")
        void getProductById_NotExists_ThrowsException() {
            when(productRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> productService.getProductById(999L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Producto no encontrado");
        }

        @Test
        @DisplayName("Buscar productos por nombre")
        void searchProducts_ReturnsMatching() {
            when(productRepository.findByNameContainingIgnoreCase("laptop"))
                    .thenReturn(List.of(testProduct));

            List<ProductDto> result = productService.searchProducts("laptop");

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getName()).containsIgnoringCase("laptop");
        }
    }

    // ==================== Tests de Creación/Actualización ====================
    @Nested
    @DisplayName("Operaciones CRUD")
    class CrudTests {

        @Test
        @DisplayName("Crear producto exitosamente")
        void createProduct_Success() {
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);

            ProductDto result = productService.createProduct(testProductDto);

            assertThat(result).isNotNull();
            assertThat(result.getName()).isEqualTo("Laptop HP");
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Actualizar producto existente")
        void updateProduct_Success() {
            ProductDto updateDto = ProductDto.builder()
                    .name("Laptop HP Actualizada")
                    .price(new BigDecimal("19999.99"))
                    .build();

            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);

            ProductDto result = productService.updateProduct(1L, updateDto);

            assertThat(result).isNotNull();
            verify(productRepository).save(any(Product.class));
        }

        @Test
        @DisplayName("Eliminar producto (soft delete)")
        void deleteProduct_SetsInactive() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);

            productService.deleteProduct(1L);

            // Verificar que se marcó como inactivo (no se borró físicamente)
            verify(productRepository).save(argThat(product ->
                    !product.getIsActive()
            ));
        }
    }

    // ==================== Tests de Inventario ====================
    @Nested
    @DisplayName("Gestión de inventario")
    class InventarioTests {

        @Test
        @DisplayName("Agregar stock exitosamente")
        void addStock_Success() {
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);
            when(inventoryRepository.save(any(Inventory.class))).thenReturn(null);

            ProductDto result = productService.addStock(1L, 10, "Compra proveedor", "admin");

            assertThat(result).isNotNull();
            // Verificar que se guardó el movimiento de inventario
            verify(inventoryRepository).save(argThat(movement ->
                    movement.getType() == Inventory.MovementType.ENTRY &&
                    movement.getQuantity() == 10
            ));
        }

        @Test
        @DisplayName("Retirar stock exitosamente")
        void removeStock_Success() {
            testProduct.setStock(25);  // Hay suficiente stock
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));
            when(productRepository.save(any(Product.class))).thenReturn(testProduct);
            when(inventoryRepository.save(any(Inventory.class))).thenReturn(null);

            ProductDto result = productService.removeStock(1L, 5, "Venta", "manager1");

            assertThat(result).isNotNull();
            verify(inventoryRepository).save(argThat(movement ->
                    movement.getType() == Inventory.MovementType.EXIT &&
                    movement.getQuantity() == 5
            ));
        }

        @Test
        @DisplayName("Retirar stock insuficiente - lanza excepción")
        void removeStock_InsufficientStock_ThrowsException() {
            testProduct.setStock(3);  // Solo hay 3
            when(productRepository.findById(1L)).thenReturn(Optional.of(testProduct));

            assertThatThrownBy(() ->
                    productService.removeStock(1L, 10, "Venta", "manager1"))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Stock insuficiente");

            // Verificar que NO se guardó ningún movimiento
            verify(inventoryRepository, never()).save(any());
        }
    }
}
