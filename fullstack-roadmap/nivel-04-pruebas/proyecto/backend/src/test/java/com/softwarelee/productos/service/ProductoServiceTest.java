package com.softwarelee.productos.service;

import com.softwarelee.productos.mapper.ProductoMapper;
import com.softwarelee.productos.model.Producto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * PRUEBAS UNITARIAS del ProductoService.
 *
 * ¿Qué se prueba aquí?
 * - La LÓGICA de negocio del Service
 * - SIN base de datos (el Mapper está mockeado)
 * - SIN levantar Spring (puro Java + Mockito)
 *
 * Patrón AAA:
 * - Arrange: preparar datos y mocks
 * - Act: ejecutar el método
 * - Assert: verificar el resultado
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("ProductoService — Pruebas Unitarias")
class ProductoServiceTest {

    @Mock
    private ProductoMapper productoMapper;

    @InjectMocks
    private ProductoService productoService;

    // ═══ Datos de prueba reutilizables ═══
    private Producto productoEjemplo;

    @BeforeEach
    void setUp() {
        productoEjemplo = new Producto();
        productoEjemplo.setId(1L);
        productoEjemplo.setNombre("Laptop HP ProBook");
        productoEjemplo.setDescripcion("Laptop empresarial");
        productoEjemplo.setPrecio(new BigDecimal("18999.00"));
        productoEjemplo.setExistencia(25);
        productoEjemplo.setCategoriaId(1L);
        productoEjemplo.setSku("LAP-HP-001");
        productoEjemplo.setActivo(true);
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: listarTodos()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("listarTodos()")
    class ListarTodos {

        @Test
        @DisplayName("Debe devolver lista de productos cuando hay datos")
        void debeListarProductosCuandoExisten() {
            // Arrange
            List<Producto> productosSimulados = List.of(productoEjemplo);
            when(productoMapper.findAll()).thenReturn(productosSimulados);

            // Act
            List<Producto> resultado = productoService.listarTodos();

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNombre()).isEqualTo("Laptop HP ProBook");
            verify(productoMapper).findAll();
        }

        @Test
        @DisplayName("Debe devolver lista vacía cuando no hay productos")
        void debeListarVacioCuandoNoHayProductos() {
            // Arrange
            when(productoMapper.findAll()).thenReturn(List.of());

            // Act
            List<Producto> resultado = productoService.listarTodos();

            // Assert
            assertThat(resultado).isEmpty();
            verify(productoMapper).findAll();
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: buscarPorId()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("buscarPorId()")
    class BuscarPorId {

        @Test
        @DisplayName("Debe devolver producto cuando el ID existe")
        void debeEncontrarProductoPorId() {
            // Arrange
            when(productoMapper.findById(1L)).thenReturn(productoEjemplo);

            // Act
            Producto resultado = productoService.buscarPorId(1L);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(1L);
            assertThat(resultado.getNombre()).isEqualTo("Laptop HP ProBook");
            verify(productoMapper).findById(1L);
        }

        @Test
        @DisplayName("Debe lanzar excepción cuando el ID no existe")
        void debeLanzarExcepcionCuandoNoExiste() {
            // Arrange
            when(productoMapper.findById(999L)).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> productoService.buscarPorId(999L))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Producto no encontrado con ID: 999");

            verify(productoMapper).findById(999L);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: buscarPorNombre()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("buscarPorNombre()")
    class BuscarPorNombre {

        @Test
        @DisplayName("Debe encontrar productos que coincidan con el nombre")
        void debeBuscarPorNombre() {
            // Arrange
            when(productoMapper.findByNombre("laptop")).thenReturn(List.of(productoEjemplo));

            // Act
            List<Producto> resultado = productoService.buscarPorNombre("laptop");

            // Assert
            assertThat(resultado).hasSize(1);
            assertThat(resultado.get(0).getNombre()).containsIgnoringCase("laptop");
            verify(productoMapper).findByNombre("laptop");
        }

        @Test
        @DisplayName("Debe devolver lista vacía cuando no hay coincidencias")
        void debeRetornarVacioCuandoNoCoincide() {
            // Arrange
            when(productoMapper.findByNombre("xyz123")).thenReturn(List.of());

            // Act
            List<Producto> resultado = productoService.buscarPorNombre("xyz123");

            // Assert
            assertThat(resultado).isEmpty();
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: crear()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("crear()")
    class Crear {

        @Test
        @DisplayName("Debe crear un producto correctamente")
        void debeCrearProducto() {
            // Arrange
            Producto nuevo = new Producto();
            nuevo.setNombre("Monitor Dell");
            nuevo.setPrecio(new BigDecimal("12499.00"));
            nuevo.setExistencia(15);
            nuevo.setCategoriaId(1L);
            nuevo.setSku("MON-DELL-001");

            doAnswer(invocation -> {
                Producto p = invocation.getArgument(0);
                p.setId(2L); // Simula que la BD genera el ID
                return null;
            }).when(productoMapper).insert(any(Producto.class));

            Producto guardado = new Producto();
            guardado.setId(2L);
            guardado.setNombre("Monitor Dell");
            guardado.setPrecio(new BigDecimal("12499.00"));
            when(productoMapper.findById(2L)).thenReturn(guardado);

            // Act
            Producto resultado = productoService.crear(nuevo);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getId()).isEqualTo(2L);
            assertThat(resultado.getNombre()).isEqualTo("Monitor Dell");
            verify(productoMapper).insert(any(Producto.class));
            verify(productoMapper).findById(2L);
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: actualizar()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("actualizar()")
    class Actualizar {

        @Test
        @DisplayName("Debe actualizar un producto existente")
        void debeActualizarProducto() {
            // Arrange
            Producto actualizado = new Producto();
            actualizado.setNombre("Laptop HP ProBook v2");
            actualizado.setPrecio(new BigDecimal("19999.00"));
            actualizado.setExistencia(20);
            actualizado.setCategoriaId(1L);
            actualizado.setSku("LAP-HP-001");

            when(productoMapper.findById(1L)).thenReturn(productoEjemplo, actualizado);

            // Act
            Producto resultado = productoService.actualizar(1L, actualizado);

            // Assert
            assertThat(resultado).isNotNull();
            assertThat(resultado.getNombre()).isEqualTo("Laptop HP ProBook v2");
            verify(productoMapper).update(any(Producto.class));
        }

        @Test
        @DisplayName("Debe lanzar excepción al actualizar producto inexistente")
        void debeLanzarExcepcionAlActualizarInexistente() {
            // Arrange
            when(productoMapper.findById(999L)).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> productoService.actualizar(999L, new Producto()))
                    .isInstanceOf(RuntimeException.class)
                    .hasMessageContaining("Producto no encontrado");

            verify(productoMapper, never()).update(any());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: eliminar()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("eliminar()")
    class Eliminar {

        @Test
        @DisplayName("Debe realizar soft delete de un producto existente")
        void debeEliminarProducto() {
            // Arrange
            when(productoMapper.findById(1L)).thenReturn(productoEjemplo);

            // Act
            productoService.eliminar(1L);

            // Assert
            verify(productoMapper).softDelete(1L);
        }

        @Test
        @DisplayName("Debe lanzar excepción al eliminar producto inexistente")
        void debeLanzarExcepcionAlEliminarInexistente() {
            // Arrange
            when(productoMapper.findById(999L)).thenReturn(null);

            // Act & Assert
            assertThatThrownBy(() -> productoService.eliminar(999L))
                    .isInstanceOf(RuntimeException.class);

            verify(productoMapper, never()).softDelete(anyLong());
        }
    }

    // ═══════════════════════════════════════════════════════════
    // TESTS: listarPaginado()
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("listarPaginado()")
    class ListarPaginado {

        @Test
        @DisplayName("Debe devolver página con metadatos correctos")
        void debePaginarCorrectamente() {
            // Arrange
            when(productoMapper.findPaginated(0, 5)).thenReturn(List.of(productoEjemplo));
            when(productoMapper.count()).thenReturn(15L);

            // Act
            Map<String, Object> resultado = productoService.listarPaginado(0, 5);

            // Assert
            assertThat(resultado.get("page")).isEqualTo(0);
            assertThat(resultado.get("size")).isEqualTo(5);
            assertThat(resultado.get("totalElements")).isEqualTo(15L);
            assertThat(resultado.get("totalPages")).isEqualTo(3);
            assertThat((List<?>) resultado.get("content")).hasSize(1);
        }

        @Test
        @DisplayName("Debe calcular offset correctamente para página 2")
        void debeCalcularOffsetCorrecto() {
            // Arrange
            when(productoMapper.findPaginated(10, 5)).thenReturn(List.of());
            when(productoMapper.count()).thenReturn(15L);

            // Act
            productoService.listarPaginado(2, 5);

            // Assert: verifica que el offset es page*size = 2*5 = 10
            verify(productoMapper).findPaginated(10, 5);
        }
    }
}
