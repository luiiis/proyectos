package com.modern.app.domain.service;

import com.modern.app.api.dto.ProductDtos.*;
import com.modern.app.domain.entity.ProductEntity;
import com.modern.app.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Tests de integración con Testcontainers.
 *
 * ¿Qué es Testcontainers?
 * - Levanta un contenedor Docker REAL de PostgreSQL para cada test.
 * - Los tests corren contra la BD REAL (no H2 que se comporta diferente).
 * - Detecta bugs que H2 no detectaría:
 *   - Tipos de datos específicos de PostgreSQL (JSONB, INET, TIMESTAMPTZ)
 *   - CHECK constraints
 *   - Partial indexes
 *   - Full-text search
 *
 * ¿Cómo funciona?
 * 1. @Container crea un PostgreSQL temporal en Docker
 * 2. @ServiceConnection configura Spring para usar ESE PostgreSQL
 * 3. Flyway ejecuta las migraciones (V1, V2) en el contenedor temporal
 * 4. Los tests corren contra datos reales
 * 5. Al terminar, el contenedor se DESTRUYE (datos limpios cada vez)
 *
 * Requisito: Docker debe estar corriendo en tu máquina.
 */
@SpringBootTest
@Testcontainers
class ProductServiceTest {

    // Contenedor PostgreSQL temporal (se crea al inicio, se destruye al final)
    @Container
    @ServiceConnection  // Spring Boot auto-configura el datasource con este contenedor
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    // Desactivar Keycloak y Redis para tests (no los necesitamos aquí)
    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> "http://localhost:8180/realms/test");
        registry.add("spring.data.redis.host", () -> "localhost");
        registry.add("spring.cache.type", () -> "none");
    }

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Nested
    @DisplayName("Consultas de productos")
    class QueryTests {

        @Test
        @DisplayName("findAll devuelve solo productos activos del seed")
        void findAll_returnsActiveProducts() {
            // ACT: Flyway ya insertó 15 productos en V2__seed_data.sql
            List<ProductResponse> products = productService.findAll();

            // ASSERT
            assertThat(products).isNotEmpty();
            assertThat(products).allMatch(p -> p.active());
            // Verificar que los datos del seed están presentes
            assertThat(products).anyMatch(p -> p.name().contains("MacBook"));
        }

        @Test
        @DisplayName("findById con ID existente devuelve producto")
        void findById_existingId_returnsProduct() {
            ProductResponse product = productService.findById(1L);

            assertThat(product).isNotNull();
            assertThat(product.name()).contains("MacBook");
            assertThat(product.price()).isGreaterThan(BigDecimal.ZERO);
        }

        @Test
        @DisplayName("findById con ID inexistente lanza excepción")
        void findById_nonExistingId_throwsException() {
            assertThatThrownBy(() -> productService.findById(99999L))
                    .isInstanceOf(ProductService.EntityNotFoundException.class)
                    .hasMessageContaining("no encontrado");
        }

        @Test
        @DisplayName("search encuentra productos por nombre parcial")
        void search_byPartialName_returnsMatches() {
            List<ProductResponse> results = productService.search("Dell");

            assertThat(results).isNotEmpty();
            assertThat(results).allMatch(p -> p.name().toLowerCase().contains("dell"));
        }

        @Test
        @DisplayName("findByCategory filtra correctamente")
        void findByCategory_returnsOnlyThatCategory() {
            List<ProductResponse> laptops = productService.findByCategory("Laptops");

            assertThat(laptops).isNotEmpty();
            assertThat(laptops).allMatch(p -> "Laptops".equals(p.category()));
        }
    }

    @Nested
    @DisplayName("Operaciones CRUD")
    class CrudTests {

        @Test
        @DisplayName("create persiste producto en PostgreSQL")
        void create_validProduct_persistsInDatabase() {
            // ARRANGE
            var dto = new CreateProduct(
                    "Test Product 2026",
                    "Producto de prueba",
                    new BigDecimal("999.99"),
                    50,
                    "Test",
                    "TEST-001",
                    null
            );

            // ACT
            ProductResponse created = productService.create(dto);

            // ASSERT
            assertThat(created.id()).isNotNull();
            assertThat(created.name()).isEqualTo("Test Product 2026");
            assertThat(created.price()).isEqualByComparingTo(new BigDecimal("999.99"));
            assertThat(created.stock()).isEqualTo(50);

            // Verificar que realmente está en la BD
            var fromDb = productRepository.findById(created.id());
            assertThat(fromDb).isPresent();
        }

        @Test
        @DisplayName("delete hace soft delete (no borra físicamente)")
        void delete_setsInactive_doesNotRemoveFromDb() {
            // ARRANGE: crear producto
            var dto = new CreateProduct("To Delete", null, new BigDecimal("10"), 1, null, "DEL-001", null);
            ProductResponse created = productService.create(dto);

            // ACT: "eliminar"
            productService.delete(created.id());

            // ASSERT: sigue en BD pero inactivo
            var fromDb = productRepository.findById(created.id());
            assertThat(fromDb).isPresent();
            assertThat(fromDb.get().isActive()).isFalse();
        }
    }

    @Nested
    @DisplayName("Gestión de inventario")
    class InventoryTests {

        @Test
        @DisplayName("addStock incrementa el stock correctamente")
        void addStock_incrementsStock() {
            // ARRANGE
            var dto = new CreateProduct("Stock Test", null, new BigDecimal("100"), 10, null, "STK-001", null);
            ProductResponse created = productService.create(dto);

            // ACT
            var movement = new StockMovement(5, "Test entry");
            ProductResponse updated = productService.addStock(created.id(), movement);

            // ASSERT
            assertThat(updated.stock()).isEqualTo(15); // 10 + 5
        }

        @Test
        @DisplayName("removeStock con stock insuficiente lanza excepción")
        void removeStock_insufficientStock_throwsException() {
            // ARRANGE
            var dto = new CreateProduct("Low Stock", null, new BigDecimal("50"), 3, null, "LOW-001", null);
            ProductResponse created = productService.create(dto);

            // ACT & ASSERT
            var movement = new StockMovement(10, "Too much");
            assertThatThrownBy(() -> productService.removeStock(created.id(), movement))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("Stock insuficiente");
        }

        @Test
        @DisplayName("removeStock con stock suficiente decrementa correctamente")
        void removeStock_sufficientStock_decrementsStock() {
            var dto = new CreateProduct("Remove Test", null, new BigDecimal("75"), 20, null, "REM-001", null);
            ProductResponse created = productService.create(dto);

            var movement = new StockMovement(7, "Venta test");
            ProductResponse updated = productService.removeStock(created.id(), movement);

            assertThat(updated.stock()).isEqualTo(13); // 20 - 7
        }
    }
}
