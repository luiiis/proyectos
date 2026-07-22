package com.modern.app.domain.service;

import com.modern.app.api.dto.ProductDtos.*;
import com.modern.app.domain.entity.ProductEntity;
import com.modern.app.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para ProductService.
 *
 * Patrón AAA:
 * - Arrange: preparar datos y mocks
 * - Act: ejecutar el método
 * - Assert: verificar resultado
 *
 * Ejecutar: mvn test -Dtest="ProductServiceTest"
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductService")
class ProductServiceTest {

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    private ProductEntity laptop;
    private ProductEntity mouse;

    @BeforeEach
    void setUp() {
        laptop = new ProductEntity("Laptop HP", new BigDecimal("18999.00"), 25, "Electrónica", "LAP-001");
        mouse = new ProductEntity("Mouse MX", new BigDecimal("1899.00"), 50, "Periféricos", "MOU-001");
    }

    @Nested
    @DisplayName("findAll()")
    class FindAll {

        @Test
        @DisplayName("debe retornar lista de productos activos")
        void debeRetornarProductosActivos() {
            // Arrange
            when(repository.findByActiveTrue()).thenReturn(List.of(laptop, mouse));

            // Act
            var resultado = service.findAll();

            // Assert
            assertThat(resultado).hasSize(2);
            assertThat(resultado.get(0).name()).isEqualTo("Laptop HP");
            assertThat(resultado.get(1).name()).isEqualTo("Mouse MX");
            verify(repository).findByActiveTrue();
        }

        @Test
        @DisplayName("debe retornar lista vacía si no hay productos")
        void debeRetornarListaVacia() {
            when(repository.findByActiveTrue()).thenReturn(List.of());
            var resultado = service.findAll();
            assertThat(resultado).isEmpty();
        }
    }

    @Nested
    @DisplayName("findById()")
    class FindById {

        @Test
        @DisplayName("debe retornar producto cuando existe")
        void debeRetornarProductoCuandoExiste() {
            when(repository.findById(1L)).thenReturn(Optional.of(laptop));

            var resultado = service.findById(1L);

            assertThat(resultado.name()).isEqualTo("Laptop HP");
            assertThat(resultado.price()).isEqualByComparingTo(new BigDecimal("18999.00"));
        }

        @Test
        @DisplayName("debe lanzar excepción cuando no existe")
        void debeLanzarExcepcionCuandoNoExiste() {
            when(repository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> service.findById(999L))
                .isInstanceOf(ProductService.EntityNotFoundException.class)
                .hasMessageContaining("999");
        }
    }

    @Nested
    @DisplayName("create()")
    class Create {

        @Test
        @DisplayName("debe crear producto correctamente")
        void debeCrearProducto() {
            var request = new CreateProduct("Monitor Dell", "27 pulgadas 4K",
                new BigDecimal("12499.00"), 15, "Electrónica", "MON-001", null);

            when(repository.save(any(ProductEntity.class))).thenAnswer(inv -> inv.getArgument(0));

            var resultado = service.create(request);

            assertThat(resultado.name()).isEqualTo("Monitor Dell");
            assertThat(resultado.stock()).isEqualTo(15);
            verify(repository).save(any(ProductEntity.class));
        }
    }

    @Nested
    @DisplayName("addStock() / removeStock()")
    class StockOperations {

        @Test
        @DisplayName("addStock debe incrementar el stock")
        void addStockDebeIncrementar() {
            when(repository.findById(1L)).thenReturn(Optional.of(laptop));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var movement = new StockMovement(10, "Compra proveedor");
            service.addStock(1L, movement);

            assertThat(laptop.getStock()).isEqualTo(35); // 25 + 10
        }

        @Test
        @DisplayName("removeStock debe decrementar el stock")
        void removeStockDebeDecrementar() {
            when(repository.findById(1L)).thenReturn(Optional.of(laptop));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            var movement = new StockMovement(5, "Venta #V-001");
            service.removeStock(1L, movement);

            assertThat(laptop.getStock()).isEqualTo(20); // 25 - 5
        }

        @Test
        @DisplayName("removeStock debe fallar si stock insuficiente")
        void removeStockDebeFallarSiInsuficiente() {
            when(repository.findById(1L)).thenReturn(Optional.of(laptop)); // stock=25

            var movement = new StockMovement(100, "Venta grande");

            assertThatThrownBy(() -> service.removeStock(1L, movement))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("insuficiente");
        }
    }

    @Nested
    @DisplayName("delete() - soft delete")
    class Delete {

        @Test
        @DisplayName("debe marcar como inactivo (no borrar)")
        void debeMarcarInactivo() {
            when(repository.findById(1L)).thenReturn(Optional.of(laptop));
            when(repository.save(any())).thenAnswer(inv -> inv.getArgument(0));

            service.delete(1L);

            assertThat(laptop.isActive()).isFalse(); // Soft delete
            verify(repository).save(laptop);
            verify(repository, never()).delete(any()); // Nunca se borra realmente
        }
    }
}
