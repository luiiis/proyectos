# Swagger / OpenAPI — Documentación automática de tu API

## ¿Qué es Swagger?
Swagger genera una página web interactiva donde puedes VER y PROBAR todos tus endpoints sin Postman ni curl. Se genera AUTOMÁTICAMENTE desde tu código.

---

## 1. Agregar Swagger al proyecto

### pom.xml (agregar esta dependencia)
```xml
<!-- SpringDoc OpenAPI: genera Swagger UI automáticamente -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.5.0</version>
</dependency>
```

### Ejecutar y abrir
```cmd
mvn spring-boot:run
```

Abrir en navegador:
```
http://localhost:8080/swagger-ui.html
```

¡Listo! Swagger detecta TODOS tus endpoints automáticamente.

---

## 2. Lo que ves en Swagger UI

```
┌─────────────────────────────────────────────────────────────┐
│  API Productos - Swagger UI                                   │
├─────────────────────────────────────────────────────────────┤
│                                                               │
│  ▼ producto-controller                                        │
│    GET    /api/productos          Listar todos               │
│    GET    /api/productos/{id}     Buscar por ID              │
│    GET    /api/productos/buscar   Buscar por nombre          │
│    POST   /api/productos          Crear producto             │
│    PUT    /api/productos/{id}     Actualizar producto        │
│    DELETE /api/productos/{id}     Eliminar producto          │
│                                                               │
│  ▼ categoria-controller                                       │
│    GET    /api/categorias         Listar todas               │
│    POST   /api/categorias         Crear categoría            │
│    ...                                                        │
│                                                               │
└─────────────────────────────────────────────────────────────┘
```

Puedes hacer click en cualquier endpoint → "Try it out" → llenar parámetros → "Execute" → ver la respuesta.

---

## 3. Personalizar la documentación

### En Application.java o en una config separada
```java
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

@OpenAPIDefinition(
    info = @Info(
        title = "API de Productos y Categorías",
        version = "1.0.0",
        description = "CRUD completo con MyBatis + MySQL + Flyway",
        contact = @Contact(name = "SoftwareLee", email = "contacto@softwarelee.com")
    )
)
@SpringBootApplication
public class Application { ... }
```

### En los Controllers (describir cada endpoint)
```java
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/productos")
@Tag(name = "Productos", description = "CRUD de productos con búsqueda y paginación")
public class ProductoController {

    @Operation(
        summary = "Listar todos los productos",
        description = "Devuelve todos los productos activos con nombre de categoría"
    )
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public ResponseEntity<Map<String, Object>> listarTodos() { ... }

    @Operation(summary = "Buscar producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> buscarPorId(
            @Parameter(description = "ID del producto", example = "1")
            @PathVariable Long id) { ... }

    @Operation(summary = "Crear nuevo producto")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    @ApiResponse(responseCode = "400", description = "Datos inválidos")
    @PostMapping
    public ResponseEntity<Map<String, Object>> crear(@RequestBody Producto producto) { ... }
}
```

---

## 4. Swagger con autenticación JWT

### Configurar Swagger para aceptar JWT
```java
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Ingresa el token JWT (sin el prefijo 'Bearer')"
)
public class SwaggerConfig { }
```

### Marcar endpoints protegidos
```java
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/api/productos")
@SecurityRequirement(name = "bearerAuth")  // Todos los endpoints requieren JWT
public class ProductoController { ... }
```

### En Swagger UI:
1. Click en el botón "Authorize" 🔒
2. Pegar tu token JWT (sin "Bearer", solo el token)
3. Click "Authorize"
4. Ahora puedes probar endpoints protegidos

---

## 5. Configuración en application.yml

```yaml
# Personalizar rutas de Swagger
springdoc:
  api-docs:
    path: /api-docs              # JSON de OpenAPI
  swagger-ui:
    path: /swagger-ui.html       # UI interactiva
    operations-sorter: method    # Ordenar por método HTTP
    tags-sorter: alpha           # Ordenar tags alfabéticamente
  show-actuator: false           # No mostrar endpoints de Actuator
```

### URLs disponibles
```
http://localhost:8080/swagger-ui.html     ← UI interactiva
http://localhost:8080/api-docs            ← JSON de OpenAPI (para herramientas)
http://localhost:8080/api-docs.yaml       ← YAML de OpenAPI
```

---

## 6. ¿Swagger o Postman?

| Aspecto | Swagger | Postman |
|---------|---------|---------|
| Se genera | Automáticamente del código | Manualmente |
| Tests automáticos | No | Sí (scripts) |
| Siempre actualizado | Sí (refleja el código) | Puede desactualizarse |
| Para compartir | URL del servidor | Archivo exportado |
| Para desarrollo rápido | ✅ Ideal | Más pasos |
| Para pruebas completas | ❌ Limitado | ✅ Ideal |
| Para documentación | ✅ Perfecto | No es su propósito |

**Recomendación**: usa AMBOS.
- **Swagger** → documentación + pruebas rápidas durante desarrollo
- **Postman** → pruebas completas con scripts + evidencias + Runner

---

## Siguiente paso
Agrega SpringDoc a tus proyectos del nivel 3+ para tener documentación interactiva automática.
