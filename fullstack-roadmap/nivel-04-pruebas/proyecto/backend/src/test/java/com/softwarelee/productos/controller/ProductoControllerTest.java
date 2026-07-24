package com.softwarelee.productos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelee.productos.model.Producto;
import com.softwarelee.productos.service.ProductoService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.bean.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * PRUEBAS DEL CONTROLLER con MockMvc.
 *
 * ¿Qué se prueba aquí?
 * - Que los endpoints responden con el código HTTP correcto
 * - Que el JSON de respuesta tiene la estructura esperada
 * - Que los parámetros de URL se mapean bien
 * - SIN levantar el servidor completo (solo la capa web)
 * - El Service está mockeado (no toca BD)
 *
 * @WebMvcTest: solo carga el Controller + filtros web
 * @MockBean: reemplaza el Service real por un mock en el contexto de Spring
 */
@WebMvcTest(ProductoController.class)
@DisplayName("ProductoController — Pruebas de Endpoints")
class ProductoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @Autowired
    private ObjectMapper objectMapper;

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
        productoEjemplo.setCategoriaNombre("Electrónica");
    }

    // ═══════════════════════════════════════════════════════════
    // GET /api/productos
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/productos")
    class ListarTodos {

        @Test
        @DisplayName("200 OK — devuelve lista de productos")
        void debeListarProductos() throws Exception {
            // Arrange
            when(productoService.listarTodos()).thenReturn(List.of(productoEjemplo));

            // Act & Assert
            mockMvc.perform(get("/api/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data").isArray())
                    .andExpect(jsonPath("$.data", hasSize(1)))
                    .andExpect(jsonPath("$.data[0].nombre").value("Laptop HP ProBook"))
                    .andExpect(jsonPath("$.total").value(1));
        }

        @Test
        @DisplayName("200 OK — devuelve lista vacía cuando no hay productos")
        void debeListarVacio() throws Exception {
            when(productoService.listarTodos()).thenReturn(List.of());

            mockMvc.perform(get("/api/productos"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data", hasSize(0)))
                    .andExpect(jsonPath("$.total").value(0));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GET /api/productos/{id}
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/productos/{id}")
    class BuscarPorId {

        @Test
        @DisplayName("200 OK — devuelve producto por ID")
        void debeEncontrarPorId() throws Exception {
            when(productoService.buscarPorId(1L)).thenReturn(productoEjemplo);

            mockMvc.perform(get("/api/productos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.id").value(1))
                    .andExpect(jsonPath("$.data.nombre").value("Laptop HP ProBook"))
                    .andExpect(jsonPath("$.data.precio").value(18999.00));
        }

        @Test
        @DisplayName("404 NOT FOUND — producto no existe")
        void debeRetornar404CuandoNoExiste() throws Exception {
            when(productoService.buscarPorId(999L))
                    .thenThrow(new RuntimeException("Producto no encontrado con ID: 999"));

            mockMvc.perform(get("/api/productos/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error").value("Producto no encontrado con ID: 999"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GET /api/productos/buscar?nombre=xxx
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/productos/buscar")
    class BuscarPorNombre {

        @Test
        @DisplayName("200 OK — busca productos por nombre")
        void debeBuscarPorNombre() throws Exception {
            when(productoService.buscarPorNombre("laptop")).thenReturn(List.of(productoEjemplo));

            mockMvc.perform(get("/api/productos/buscar").param("nombre", "laptop"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data", hasSize(1)));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GET /api/productos/categoria/{id}
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/productos/categoria/{categoriaId}")
    class BuscarPorCategoria {

        @Test
        @DisplayName("200 OK — filtra por categoría")
        void debeFiltrarPorCategoria() throws Exception {
            when(productoService.buscarPorCategoria(1L)).thenReturn(List.of(productoEjemplo));

            mockMvc.perform(get("/api/productos/categoria/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data[0].categoriaId").value(1));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // GET /api/productos/paginado
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("GET /api/productos/paginado")
    class Paginado {

        @Test
        @DisplayName("200 OK — devuelve datos paginados con metadatos")
        void debePaginar() throws Exception {
            Map<String, Object> paginado = Map.of(
                    "content", List.of(productoEjemplo),
                    "page", 0,
                    "size", 5,
                    "totalElements", 15L,
                    "totalPages", 3
            );
            when(productoService.listarPaginado(0, 5)).thenReturn(paginado);

            mockMvc.perform(get("/api/productos/paginado")
                            .param("page", "0")
                            .param("size", "5"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.page").value(0))
                    .andExpect(jsonPath("$.size").value(5))
                    .andExpect(jsonPath("$.totalElements").value(15))
                    .andExpect(jsonPath("$.totalPages").value(3))
                    .andExpect(jsonPath("$.content", hasSize(1)));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // POST /api/productos
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("POST /api/productos")
    class Crear {

        @Test
        @DisplayName("201 CREATED — crea un producto nuevo")
        void debeCrearProducto() throws Exception {
            Producto nuevo = new Producto();
            nuevo.setNombre("Monitor Dell");
            nuevo.setPrecio(new BigDecimal("12499.00"));
            nuevo.setExistencia(15);
            nuevo.setCategoriaId(1L);
            nuevo.setSku("MON-DELL-001");

            Producto creado = new Producto();
            creado.setId(2L);
            creado.setNombre("Monitor Dell");
            creado.setPrecio(new BigDecimal("12499.00"));
            creado.setExistencia(15);

            when(productoService.crear(any(Producto.class))).thenReturn(creado);

            mockMvc.perform(post("/api/productos")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(nuevo)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Producto creado"))
                    .andExpect(jsonPath("$.data.id").value(2))
                    .andExpect(jsonPath("$.data.nombre").value("Monitor Dell"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // PUT /api/productos/{id}
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("PUT /api/productos/{id}")
    class Actualizar {

        @Test
        @DisplayName("200 OK — actualiza un producto existente")
        void debeActualizarProducto() throws Exception {
            Producto actualizado = new Producto();
            actualizado.setId(1L);
            actualizado.setNombre("Laptop HP ProBook v2");
            actualizado.setPrecio(new BigDecimal("19999.00"));
            actualizado.setExistencia(20);

            when(productoService.actualizar(eq(1L), any(Producto.class))).thenReturn(actualizado);

            mockMvc.perform(put("/api/productos/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(actualizado)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Producto actualizado"))
                    .andExpect(jsonPath("$.data.nombre").value("Laptop HP ProBook v2"));
        }
    }

    // ═══════════════════════════════════════════════════════════
    // DELETE /api/productos/{id}
    // ═══════════════════════════════════════════════════════════

    @Nested
    @DisplayName("DELETE /api/productos/{id}")
    class Eliminar {

        @Test
        @DisplayName("200 OK — elimina (soft delete) un producto")
        void debeEliminarProducto() throws Exception {
            doNothing().when(productoService).eliminar(1L);

            mockMvc.perform(delete("/api/productos/1"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Producto eliminado"));

            verify(productoService).eliminar(1L);
        }

        @Test
        @DisplayName("404 NOT FOUND — error al eliminar producto inexistente")
        void debeRetornar404AlEliminarInexistente() throws Exception {
            doThrow(new RuntimeException("Producto no encontrado con ID: 999"))
                    .when(productoService).eliminar(999L);

            mockMvc.perform(delete("/api/productos/999"))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }
}
