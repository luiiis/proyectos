# Pruebas de Seguridad — Validar que Spring Security funciona

## ¿Qué probar?
Cuando implementas seguridad, debes demostrar que:
1. Los endpoints públicos son accesibles SIN token
2. Los endpoints protegidos RECHAZAN peticiones sin token (401)
3. Los endpoints con roles RECHAZAN usuarios sin permiso (403)
4. El token expirado es rechazado (401)
5. El token inválido/manipulado es rechazado (401)

---

## 1. Matriz de pruebas de acceso

| Endpoint | Sin token | Token ADMIN | Token CAJERO | Token expirado |
|----------|:---------:|:-----------:|:------------:|:--------------:|
| POST /api/auth/login | ✅ 200 | ✅ 200 | ✅ 200 | ✅ 200 |
| GET /api/productos | ❌ 401 | ✅ 200 | ✅ 200 | ❌ 401 |
| POST /api/productos | ❌ 401 | ✅ 201 | ❌ 403 | ❌ 401 |
| DELETE /api/productos/1 | ❌ 401 | ✅ 200 | ❌ 403 | ❌ 401 |
| GET /api/auth/me | ❌ 401 | ✅ 200 | ✅ 200 | ❌ 401 |
| GET /api/admin/panel | ❌ 401 | ✅ 200 | ❌ 403 | ❌ 401 |

---

## 2. Pruebas con Postman

### Test 1: Login exitoso
```
POST {{base_url}}/api/auth/login
Body: {"username": "admin", "password": "Admin123!"}

Tests (Post-response):
pm.test("Login exitoso - 200", () => pm.response.to.have.status(200));
pm.test("Tiene accessToken", () => {
    var json = pm.response.json();
    pm.expect(json.accessToken).to.be.a("string");
    pm.expect(json.accessToken.length).to.be.above(50);
    pm.environment.set("token", json.accessToken);
});
pm.test("Tiene roles", () => {
    pm.expect(pm.response.json().roles).to.include("ROLE_ADMIN");
});
```

### Test 2: Login con credenciales incorrectas
```
POST {{base_url}}/api/auth/login
Body: {"username": "admin", "password": "INCORRECTO"}

Tests:
pm.test("Credenciales inválidas - 401", () => pm.response.to.have.status(401));
pm.test("Mensaje de error", () => {
    pm.expect(pm.response.json().error).to.include("Credenciales inválidas");
});
```

### Test 3: Acceder sin token (401 Unauthorized)
```
GET {{base_url}}/api/productos
Headers: (SIN Authorization)

Tests:
pm.test("Sin token devuelve 401", () => pm.response.to.have.status(401));
```

### Test 4: Acceder con token válido
```
GET {{base_url}}/api/productos
Headers: Authorization: Bearer {{token}}

Tests:
pm.test("Con token devuelve 200", () => pm.response.to.have.status(200));
pm.test("Devuelve datos", () => {
    pm.expect(pm.response.json().data).to.be.an("array");
});
```

### Test 5: Acceder con token manipulado
```
GET {{base_url}}/api/productos
Headers: Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.MANIPULADO.fake

Tests:
pm.test("Token manipulado devuelve 401", () => pm.response.to.have.status(401));
```

### Test 6: Acceder con rol insuficiente (403 Forbidden)
```
:: Primero: login como cajero
POST {{base_url}}/api/auth/login
Body: {"username": "cajero", "password": "Admin123!"}
Script: pm.environment.set("token_cajero", pm.response.json().accessToken);

:: Luego: intentar eliminar producto (solo ADMIN puede)
DELETE {{base_url}}/api/productos/1
Headers: Authorization: Bearer {{token_cajero}}

Tests:
pm.test("Sin permiso devuelve 403", () => pm.response.to.have.status(403));
```

---

## 3. Pruebas con MockMvc (código Java)

```java
@WebMvcTest(ProductoController.class)
@Import(SecurityConfig.class)  // Cargar la config de seguridad
class ProductoControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductoService productoService;

    @MockBean
    private JwtService jwtService;

    @Test
    @DisplayName("GET /api/productos sin token → 401")
    void sinTokenDevuelve401() throws Exception {
        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/productos con token válido → 200")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void conTokenDevuelve200() throws Exception {
        when(productoService.listarTodos()).thenReturn(List.of());

        mockMvc.perform(get("/api/productos"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /api/productos/1 con rol CAJERO → 403")
    @WithMockUser(username = "cajero", roles = {"CAJERO"})
    void sinPermisoDevuelve403() throws Exception {
        mockMvc.perform(delete("/api/productos/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/auth/login es público → no requiere token")
    void loginEsPublico() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"admin\",\"password\":\"Admin123!\"}"))
                .andExpect(status().isOk());  // No da 401
    }
}
```

---

## 4. Pruebas de seguridad con curl

```bash
# ═══ 1. Login ═══
TOKEN=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"Admin123!"}' | jq -r '.accessToken')

echo "Token: $TOKEN"

# ═══ 2. Acceso con token (200) ═══
curl -s http://localhost:8080/api/productos \
  -H "Authorization: Bearer $TOKEN" | jq '.success'
# true

# ═══ 3. Sin token (401) ═══
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/productos
# 401

# ═══ 4. Token inventado (401) ═══
curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/productos \
  -H "Authorization: Bearer token_falso_inventado"
# 401

# ═══ 5. Login con cajero ═══
TOKEN_CAJERO=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"cajero","password":"Admin123!"}' | jq -r '.accessToken')

# ═══ 6. Cajero intenta eliminar (403) ═══
curl -s -o /dev/null -w "%{http_code}" -X DELETE http://localhost:8080/api/productos/1 \
  -H "Authorization: Bearer $TOKEN_CAJERO"
# 403
```

---

## 5. Proteger endpoints por rol (@PreAuthorize)

```java
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // Cualquier usuario autenticado puede ver
    @GetMapping
    public ResponseEntity<?> listar() { ... }

    // Solo ADMIN y ALMACENISTA pueden crear
    @PreAuthorize("hasAnyRole('ADMIN', 'ALMACENISTA')")
    @PostMapping
    public ResponseEntity<?> crear(...) { ... }

    // Solo ADMIN puede eliminar
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(...) { ... }

    // Basado en permisos granulares
    @PreAuthorize("hasAuthority('PRODUCTO_EDITAR')")
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(...) { ... }
}
```

---

## 6. Checklist de seguridad

| # | Verificación | Cómo probar |
|---|-------------|-------------|
| 1 | ¿Endpoints públicos accesibles sin token? | curl sin header |
| 2 | ¿Endpoints protegidos devuelven 401 sin token? | curl sin header |
| 3 | ¿Token válido permite acceso? | curl con Bearer token |
| 4 | ¿Token expirado devuelve 401? | Esperar expiración o usar token viejo |
| 5 | ¿Token manipulado devuelve 401? | Modificar un carácter del token |
| 6 | ¿Rol incorrecto devuelve 403? | Login como cajero, intentar DELETE |
| 7 | ¿Password se guarda hasheado? | Revisar BD: password_hash no es texto plano |
| 8 | ¿Login bloquea después de N intentos? | Intentar 5+ veces con password mal |
| 9 | ¿Refresh token funciona? | Usar refresh token para obtener nuevo access |
| 10 | ¿Logout invalida el token? | Usar token después de logout |

---

## 7. Evidencias de seguridad (para documentación)

```
docs/seguridad/
├── matriz-acceso.md              ← Tabla de quién puede qué
├── evidencia-login-exitoso.png
├── evidencia-401-sin-token.png
├── evidencia-403-sin-permiso.png
├── evidencia-token-expirado.png
├── evidencia-password-hasheado.png  ← Captura de la BD
└── pruebas-runner-seguridad.png     ← Resultado del Runner de Postman
```

---

## Siguiente paso
Implementa y prueba la seguridad en el nivel 5, y asegúrate de generar evidencias de cada caso.
