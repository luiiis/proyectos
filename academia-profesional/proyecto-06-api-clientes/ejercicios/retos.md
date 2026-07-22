# Ejercicios y Retos - Proyecto 06: API REST Clientes

## Reto 1: Agregar endpoint de búsqueda
Crea `GET /api/clientes/buscar?nombre=Carlos&ciudad=Mexico`:
- Filtra por nombre Y/O ciudad (ambos opcionales)
- Si no se pasa ningún filtro, devuelve todos

**Lo que practicas:** @RequestParam(required = false), filtros dinámicos

---

## Reto 2: Paginación manual
Agrega paginación a `GET /api/clientes?page=0&size=10`:
- Devuelve solo N elementos por página
- Incluir metadata: totalElementos, totalPaginas, paginaActual

**Respuesta parcial:**
```java
record PaginaResponse<T>(List<T> contenido, int pagina, int tamano, long total, int totalPaginas) {}
```

**Lo que practicas:** Paginación, DTOs de respuesta

---

## Reto 3: Validaciones custom
Crea una validación personalizada `@EmailEmpresarial` que solo acepte emails con dominio corporativo:
```java
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailEmpresarialValidator.class)
public @interface EmailEmpresarial {
    String message() default "Debe ser un email corporativo";
    // ...
}
```

**Lo que practicas:** Custom validators, anotaciones, Jakarta Validation

---

## Reto 4: Versionado de API
Implementa versionado de tu API:
- `GET /api/v1/clientes` → respuesta simple (id, nombre, email)
- `GET /api/v2/clientes` → respuesta extendida (id, nombre, email, telefono, ciudad, activo)

**Lo que practicas:** Versionado de APIs, múltiples DTOs

---

## Reto 5: Rate Limiting simple
Implementa un limitador que solo permita 10 requests por minuto por IP:
- Usa `ConcurrentHashMap<String, List<LocalDateTime>>` para trackear requests
- Si excede el límite, responde 429 Too Many Requests

**Lo que practicas:** Filter/Interceptor, control de acceso, ConcurrentHashMap

---

## Reto 6: HATEOAS
Agrega links de navegación en las respuestas:
```json
{
  "id": 1,
  "nombre": "Carlos",
  "_links": {
    "self": "/api/clientes/1",
    "update": "/api/clientes/1",
    "delete": "/api/clientes/1",
    "all": "/api/clientes"
  }
}
```

**Lo que practicas:** REST nivel 3 (Richardson Maturity Model), HATEOAS

---

## Reto 7: Documentación OpenAPI
Agrega Swagger/OpenAPI a tu API:
1. Agregar dependencia: `springdoc-openapi-starter-webmvc-ui`
2. Anotar endpoints con `@Operation`, `@ApiResponse`
3. Acceder a `http://localhost:8080/swagger-ui.html`

**Lo que practicas:** Documentación de APIs, OpenAPI 3.0

---

## Reto 8 (Avanzado): Exportar a CSV/Excel
Crea `GET /api/clientes/exportar?formato=csv`:
- Genera un archivo CSV con todos los clientes
- Header: `Content-Disposition: attachment; filename="clientes.csv"`
- Content-Type: `text/csv`

**Lo que practicas:** ResponseEntity, headers HTTP, generación de archivos
