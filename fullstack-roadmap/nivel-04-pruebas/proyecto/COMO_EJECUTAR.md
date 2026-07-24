# Cómo Ejecutar — Proyecto 4: Pruebas Automatizadas

## Requisitos
- Java 21 + Maven 3.9+
- El código del Proyecto 3 (este proyecto lo extiende)

## Ejecutar TODAS las pruebas

```cmd
cd nivel-04-pruebas/proyecto/backend
mvn test
```

**Resultado esperado:**
```
[INFO] Tests run: 12, Failures: 0, Errors: 0, Skipped: 0
[INFO] BUILD SUCCESS
```

## Ejecutar una clase de test específica

```cmd
# Solo las pruebas del Service
mvn test -Dtest="ProductoServiceTest"

# Solo las pruebas del Controller
mvn test -Dtest="ProductoControllerTest"
```

## Generar reporte de cobertura (JaCoCo)

Agregar al pom.xml:
```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.12</version>
    <executions>
        <execution><goals><goal>prepare-agent</goal></goals></execution>
        <execution><id>report</id><phase>test</phase><goals><goal>report</goal></goals></execution>
    </executions>
</plugin>
```

Luego:
```cmd
mvn test
# Reporte en: target/site/jacoco/index.html
```

---

## Tipos de prueba en este proyecto

| Tipo | Archivo | Qué prueba | Mock | BD |
|------|---------|-----------|------|-----|
| Unitaria (Service) | `ProductoServiceTest.java` | Lógica de negocio | ✅ Mapper mockeado | ❌ No |
| Controller (MockMvc) | `ProductoControllerTest.java` | Endpoints HTTP | ✅ Service mockeado | ❌ No |

## Estructura de una prueba (patrón AAA)

```java
@Test
@DisplayName("Descripción clara de lo que prueba")
void nombreDescriptivo() {
    // ARRANGE (Preparar)
    when(mock.metodo()).thenReturn(datoFalso);

    // ACT (Actuar)
    var resultado = servicio.metodoQueQuieroPorbar();

    // ASSERT (Verificar)
    assertThat(resultado).isNotNull();
    assertThat(resultado.getNombre()).isEqualTo("esperado");
    verify(mock).metodo(); // Verificar que el mock fue llamado
}
```

## Anotaciones clave de testing

| Anotación | Para qué |
|-----------|----------|
| `@ExtendWith(MockitoExtension.class)` | Activa Mockito en la clase |
| `@Mock` | Crea un simulador de una dependencia |
| `@InjectMocks` | Crea el objeto real inyectándole los mocks |
| `@WebMvcTest(Controller.class)` | Solo carga la capa web (no BD) |
| `@MockBean` | Mock para Spring context (usado en @WebMvcTest) |
| `@Test` | Marca un método como test |
| `@DisplayName("...")` | Nombre legible del test |
| `@Nested` | Agrupa tests por método/funcionalidad |
| `@BeforeEach` | Se ejecuta antes de CADA test |

## Métodos de Mockito

```java
when(mock.metodo()).thenReturn(valor);     // Configurar qué devuelve
when(mock.metodo()).thenThrow(excepcion);   // Configurar que lance error
verify(mock).metodo();                      // Verificar que se llamó
verify(mock, times(2)).metodo();            // Verificar que se llamó 2 veces
verify(mock, never()).metodo();             // Verificar que NUNCA se llamó
any(), anyLong(), anyString()               // Matchers para parámetros
```

## Métodos de AssertJ

```java
assertThat(resultado).isNotNull();
assertThat(resultado).isNull();
assertThat(lista).hasSize(3);
assertThat(lista).isEmpty();
assertThat(valor).isEqualTo(esperado);
assertThat(numero).isGreaterThan(0);
assertThat(texto).contains("parcial");
assertThatThrownBy(() -> metodo()).isInstanceOf(RuntimeException.class);
```
