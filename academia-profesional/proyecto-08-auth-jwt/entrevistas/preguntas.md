# Preguntas de Entrevista - Tema: Seguridad, JWT, Spring Security

## Nivel Junior

### 1. ¿Qué es JWT y cuáles son sus 3 partes?
**Respuesta:**
JWT (JSON Web Token) es un token firmado que contiene información del usuario:
```
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbCI6IkFETUlOIn0.firma
│── HEADER ──│───────── PAYLOAD ────────────│── SIGNATURE ──│
```
- **Header**: algoritmo de firma (HS256, RS256)
- **Payload**: datos del usuario (username, roles, expiración)
- **Signature**: firma criptográfica que verifica que nadie modificó el token

---

### 2. ¿Cuál es la diferencia entre autenticación y autorización?
**Respuesta:**
- **Autenticación**: ¿QUIÉN eres? (login: verificar identidad)
- **Autorización**: ¿QUÉ puedes hacer? (permisos: verificar roles)

Flujo: Autenticación primero → si es válido → verificar autorización.

---

### 3. ¿Por qué se hashea el password y no se encripta?
**Respuesta:**
- **Encriptar**: se puede des-encriptar (reversible). Si alguien roba la clave, lee todos los passwords.
- **Hashear**: irreversible. No se puede obtener el password original del hash.

BCrypt agrega un "salt" aleatorio → dos usuarios con el mismo password tienen hashes DIFERENTES.
```java
// Guardar: BCrypt.hashpw("admin123", BCrypt.gensalt())
// Verificar: BCrypt.checkpw("admin123", hashGuardado)
```

---

### 4. ¿Dónde se almacena el JWT en el cliente?
**Respuesta:**
Opciones (de más segura a menos):
1. **HttpOnly Cookie**: no accesible desde JavaScript (protege contra XSS)
2. **localStorage**: persistente, pero vulnerable a XSS
3. **sessionStorage**: se pierde al cerrar tab, vulnerable a XSS
4. **Memoria (variable)**: más seguro, pero se pierde al refrescar

En SPAs típicas: localStorage + protección CSRF. En apps enterprise: HttpOnly Cookie.

---

### 5. ¿Qué pasa si un JWT es robado?
**Respuesta:**
El atacante tiene acceso total hasta que expire. Mitigaciones:
- Expiración corta (15-30 minutos)
- Refresh tokens (revocar el refresh invalida el acceso)
- Detectar ubicación/IP inusual
- Blacklist de tokens (almacenar tokens invalidados)
- HTTPS siempre (evita sniffing)

---

## Nivel Mid

### 6. ¿Cómo funciona el filtro JWT en Spring Security?
**Respuesta:**
```
Request HTTP → JwtFilter → SecurityContext → Controller
                  │
                  ├── 1. Extrae token del header "Authorization: Bearer xxx"
                  ├── 2. Valida firma (no fue manipulado)
                  ├── 3. Verifica expiración
                  ├── 4. Extrae username del payload
                  ├── 5. Carga usuario de BD (UserDetailsService)
                  ├── 6. Crea Authentication y lo pone en SecurityContext
                  └── 7. Continúa la cadena de filtros
```
Si falla en cualquier paso → responde 401 Unauthorized.

---

### 7. ¿Cuál es la diferencia entre HS256 y RS256?
**Respuesta:**
- **HS256** (simétrico): la misma clave firma y verifica. Todos los servicios deben conocer el secreto.
- **RS256** (asimétrico): clave privada firma, clave pública verifica. Solo el auth server tiene la privada.

Usar RS256 cuando:
- Múltiples servicios deben validar tokens (microservicios)
- No quieres compartir el secreto con todos

---

### 8. ¿Qué es CORS y por qué es necesario en SPAs?
**Respuesta:**
CORS (Cross-Origin Resource Sharing): política del navegador que bloquea requests entre dominios diferentes.
- Frontend en `localhost:4200`, API en `localhost:8080` = orígenes diferentes → bloqueado
- El backend debe responder con headers `Access-Control-Allow-Origin` para permitirlo

```java
@Configuration
public class CorsConfig {
    @Bean
    public CorsConfigurationSource corsSource() {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(List.of("*"));
        // ...
    }
}
```

---

### 9. ¿Cómo protegerías un endpoint para que solo ADMIN pueda acceder?
**Respuesta:**
Tres formas:
```java
// 1. En SecurityConfig (configuración global):
.requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

// 2. Con anotación en el método:
@PreAuthorize("hasRole('ADMIN')")
@DeleteMapping("/{id}")
public void eliminar(...) {}

// 3. Programáticamente:
Authentication auth = SecurityContextHolder.getContext().getAuthentication();
if (!auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
    throw new AccessDeniedException("No autorizado");
}
```

---

### 10. ¿Qué es un CSRF token y cuándo es necesario?
**Respuesta:**
CSRF (Cross-Site Request Forgery): un sitio malicioso hace requests en nombre del usuario usando su cookie de sesión.

**Necesario cuando:** usas cookies para autenticación (apps con sesión server-side).
**NO necesario cuando:** usas JWT en header Authorization (el navegador no lo envía automáticamente).

Por eso en SPAs con JWT, Spring Security desactiva CSRF: `.csrf(csrf -> csrf.disable())`.

---

## Nivel Senior

### 11. ¿Cómo implementarías autenticación en un sistema de microservicios?
**Respuesta:**
Patrón: API Gateway + Auth Service centralizados:
```
Cliente → Gateway → valida JWT → rutea al microservicio
              ↓
         Auth Service (login, refresh, revoke)
              ↓
         Tokens firmados con RS256 (clave pública en cada servicio)
```
Cada microservicio solo VALIDA tokens (tiene la clave pública), nunca los GENERA.
Alternativa enterprise: Keycloak/Auth0 como Identity Provider (OAuth2 + OIDC).

---

### 12. ¿Cuáles son las vulnerabilidades más comunes en APIs y cómo prevenirlas?
**Respuesta (OWASP Top 10 API):**
1. **Broken Auth**: tokens sin expiración, secretos débiles → JWT con TTL corto + rotación
2. **Broken Object Authorization**: `/api/users/2` sin verificar que eres el user 2 → verificar ownership
3. **Excessive Data Exposure**: devolver campos sensibles → usar DTOs específicos
4. **Rate Limiting ausente**: brute force → implementar throttling
5. **Injection**: SQL injection → PreparedStatement / JPA
6. **Mass Assignment**: enviar campos extra (ej: `role: "ADMIN"`) → DTOs con solo campos permitidos
7. **Security Misconfiguration**: endpoints debug expuestos → profiles de producción
