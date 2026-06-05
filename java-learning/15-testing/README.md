# Módulo 15: Testing (JUnit 5 + Mockito)

## JUnit 5 — Tests Unitarios
```java
class ProductoServiceTest {

    @Test
    @DisplayName("Crear producto con datos válidos")
    void crearProducto_datosValidos_retornaProducto() {
        // ARRANGE
        var service = new ProductoService(mockRepo);
        var dto = new CrearProductoDto("Laptop", BigDecimal.valueOf(999), 10);

        // ACT
        var resultado = service.crear(dto);

        // ASSERT
        assertThat(resultado.nombre()).isEqualTo("Laptop");
        assertThat(resultado.precio()).isEqualByComparingTo("999");
    }

    @Test
    @DisplayName("Crear producto con precio negativo lanza excepción")
    void crearProducto_precioNegativo_lanzaExcepcion() {
        var dto = new CrearProductoDto("Test", BigDecimal.valueOf(-1), 10);
        assertThatThrownBy(() -> service.crear(dto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("precio");
    }
}
```

## Mockito — Simular dependencias
```java
@ExtendWith(MockitoExtension.class)
class VentaServiceTest {

    @Mock ProductoRepository productoRepo;
    @Mock InventarioRepository inventarioRepo;
    @InjectMocks VentaService ventaService;

    @Test
    void registrarVenta_stockSuficiente_descuentaInventario() {
        // Simular que el producto existe con stock 10
        when(productoRepo.findById(1L)).thenReturn(Optional.of(productoConStock(10)));

        ventaService.registrar(1L, 3);

        // Verificar que se llamó a descontar
        verify(inventarioRepo).descontar(1L, 3);
    }
}
```

## Ejercicios
1. Escribe tests para un servicio de CuentaBancaria (depositar, retirar, transferir)
2. Usa Mockito para testear un servicio que depende de un repositorio
3. Alcanza 80% de cobertura en tu módulo de productos
4. Implementa TDD: escribe el test ANTES del código
