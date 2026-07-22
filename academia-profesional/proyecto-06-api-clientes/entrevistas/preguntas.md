# Preguntas de Entrevista - Tema: Spring Boot y REST APIs

## Nivel Junior

### 1. ¿Qué es Spring Boot y por qué usarlo en vez de Java puro?
**Respuesta:**
Spring Boot es un framework que auto-configura una aplicación web. Sin él necesitarías configurar manualmente: servidor web (Tomcat), serialización JSON (Jackson), inyección de dependencias, validaciones, etc. Spring Boot lo hace en 0 líneas de configuración.

---

### 2. ¿Qué es una API REST?
**Respuesta:**
API que sigue principios REST:
- **Recursos** identificados por URLs: `/api/clientes`, `/api/clientes/1`
- **Verbos HTTP** para operaciones: GET (leer), POST (crear), PUT (actualizar), DELETE (eliminar)
- **Stateless**: cada request contiene toda la información necesaria (no hay sesión)
- **Respuestas** en formato estándar (JSON)

---

### 3. ¿Cuáles son los códigos HTTP más importantes?
**Respuesta:**
- `200 OK` → operación exitosa
- `201 Created` → recurso creado
- `204 No Content` → éxito sin body (DELETE)
- `400 Bad Request` → datos inválidos del cliente
- `401 Unauthorized` → no autenticado
- `403 Forbidden` → autenticado pero sin permisos
- `404 Not Found` → recurso no existe
- `409 Conflict` → conflicto (ej: email duplicado)
- `500 Internal Server Error` → error del servidor

---

### 4. ¿Qué es un DTO y por qué no exponer la entidad directamente?
**Respuesta:**
DTO (Data Transfer Object) = objeto que define qué datos viajan entre capas.
No exponer la entidad porque:
- Seguridad: la entidad puede tener campos sensibles (password, tokens)
- Flexibilidad: el API puede devolver campos calculados que no están en la entidad
- Estabilidad: cambiar la BD no rompe el contrato del API
- Validación: los DTOs tienen sus propias reglas de validación

---

### 5. ¿Qué hace @RestController vs @Controller?
**Respuesta:**
- `@Controller`: devuelve vistas (HTML). Necesita `@ResponseBody` para devolver JSON.
- `@RestController` = `@Controller` + `@ResponseBody` en todos los métodos. Siempre devuelve JSON.

---

## Nivel Mid

### 6. ¿Cómo funciona la inyección de dependencias en Spring?
**Respuesta:**
Spring crea y administra objetos (beans). En vez de crear dependencias con `new`, Spring las inyecta:
```java
// Sin Spring (acoplado):
ClienteService service = new ClienteService(new ClienteRepository());

// Con Spring (desacoplado):
@Service
public class ClienteService {
    private final ClienteRepository repo;  // Spring inyecta automáticamente
    
    public ClienteService(ClienteRepository repo) {
        this.repo = repo;  // Constructor injection (recomendado)
    }
}
```
Beneficios: testing fácil (mocks), cambiar implementación sin modificar código.

---

### 7. ¿Cómo manejas errores globalmente en Spring Boot?
**Respuesta:**
Con `@RestControllerAdvice`:
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ClienteNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse manejar(ClienteNoEncontradoException e) {
        return new ErrorResponse(404, e.getMessage());
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validacion(MethodArgumentNotValidException e) {
        // Extraer errores de validación
    }
}
```
Ventaja: un solo lugar para todos los errores, respuestas consistentes.

---

### 8. ¿Cuál es la diferencia entre @PathVariable y @RequestParam?
**Respuesta:**
- `@PathVariable`: parte de la URL. Identifica un recurso.
  - `GET /api/clientes/42` → `@PathVariable Long id` = 42
- `@RequestParam`: parámetro de query. Filtra/modifica la consulta.
  - `GET /api/clientes?ciudad=CDMX&activo=true` → `@RequestParam String ciudad`

---

### 9. ¿Qué es la estructura de capas en Spring Boot?
**Respuesta:**
```
Controller (entrada HTTP) → recibe request, valida, delega
    ↓
Service (lógica de negocio) → reglas, validaciones, orquestación
    ↓
Repository (acceso a datos) → CRUD contra la BD
    ↓
Entity/Model (datos) → representa una tabla de la BD
```
Cada capa tiene UNA responsabilidad. El Controller no debe tener SQL. El Repository no debe tener lógica de negocio.

---

### 10. ¿Qué diferencia hay entre PUT y PATCH?
**Respuesta:**
- `PUT`: reemplaza TODO el recurso. Debes enviar todos los campos.
- `PATCH`: actualiza solo los campos enviados (actualización parcial).

```json
// PUT /api/clientes/1 (debes enviar TODO):
{"nombre": "Carlos", "email": "nuevo@mail.com", "telefono": "555-0000", "ciudad": "CDMX"}

// PATCH /api/clientes/1 (solo lo que cambió):
{"email": "nuevo@mail.com"}
```

---

## Nivel Senior

### 11. ¿Cómo diseñarías un API para manejar 10,000 requests/segundo?
**Respuesta:**
1. Caché (Redis) para lecturas frecuentes
2. Paginación obligatoria (nunca devolver todos los registros)
3. Connection pool optimizado (HikariCP)
4. Compresión GZIP en responses
5. CDN para assets estáticos
6. Rate limiting por usuario/IP
7. Async processing para operaciones pesadas (Kafka/colas)
8. Escalado horizontal (múltiples instancias + load balancer)
9. Índices correctos en BD
10. Monitoring para identificar cuellos de botella

---

### 12. ¿Cuáles son los principios de un buen diseño de API REST?
**Respuesta:**
- Nombres de recursos en plural y sustantivos: `/api/clientes` (no `/api/getClientes`)
- Verbos HTTP para acciones (no en la URL): `DELETE /api/clientes/1` (no `/api/borrarCliente/1`)
- Respuestas consistentes (mismo formato de error siempre)
- Versionado (`/api/v1/...`) para compatibilidad backward
- Paginación por defecto en listados
- HATEOAS (links de navegación) para autodescubrimiento
- Idempotencia: llamar PUT/DELETE 2 veces produce el mismo resultado
