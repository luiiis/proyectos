# Manual Técnico — Mini-Proyecto 01: Login con Sesión

## Arquitectura
```
Navegador                    Spring Boot (Tomcat)
    │                              │
    │  POST /login (form)          │
    ├─────────────────────────────►│ SecurityFilterChain
    │                              │   ├── UsernamePasswordAuthenticationFilter
    │                              │   ├── Valida credenciales (BCrypt)
    │  Set-Cookie: JSESSIONID=abc  │   └── Crea HttpSession en memoria
    │◄─────────────────────────────┤
    │                              │
    │  GET /dashboard              │
    │  Cookie: JSESSIONID=abc      │
    ├─────────────────────────────►│ SessionFilter: "abc" → usuario "admin"
    │                              │   → Acceso permitido
    │  HTML dashboard              │
    │◄─────────────────────────────┤
```

## Archivos y responsabilidades

| Archivo | Qué hace |
|---------|----------|
| `Application.java` | Punto de entrada, arranca Spring Boot |
| `SecurityConfig.java` | Configura: quién puede acceder a qué, formulario login, logout |
| `WebController.java` | Devuelve las páginas HTML (login, dashboard, admin) |
| `login.html` | Formulario de login (Thymeleaf) |
| `dashboard.html` | Página protegida (muestra info del usuario) |
| `admin.html` | Página solo para ADMIN |

## Flujo detallado
1. Usuario abre `http://localhost:8080/dashboard`
2. No tiene sesión → Spring redirige a `/login`
3. Escribe usuario/password → POST /login
4. Spring Security: busca usuario en `UserDetailsService` → compara BCrypt
5. Si coincide: crea `HttpSession` → asigna cookie `JSESSIONID`
6. Redirige a `/dashboard`
7. Navegador envía cookie en CADA request → Spring sabe quién es

## Cómo agregaría usuarios de BD (siguiente versión)
Reemplazar `InMemoryUserDetailsManager` por un `UserDetailsService` que consulte MySQL:
```java
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioMapper usuarioMapper;
    
    @Override
    public UserDetails loadUserByUsername(String username) {
        Usuario u = usuarioMapper.findByUsername(username);
        if (u == null) throw new UsernameNotFoundException("No existe");
        return User.builder()
            .username(u.getUsername())
            .password(u.getPasswordHash()) // Ya está hasheado en BD
            .roles(u.getRoles().toArray(String[]::new))
            .build();
    }
}
```
