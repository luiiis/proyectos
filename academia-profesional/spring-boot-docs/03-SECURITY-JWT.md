# Spring Boot Completo - Parte 3: Security y JWT

## Que es Spring Security

Spring Security es el framework de seguridad para aplicaciones Java.
Maneja: autenticacion (quien eres) y autorizacion (que puedes hacer).

## Como funciona internamente

```
Cada peticion HTTP pasa por una CADENA DE FILTROS antes de llegar al Controller:

Request HTTP
    │
    ▼
┌─────────────────────────────────────────────────┐
│ FILTER CHAIN (cadena de filtros)                 │
│                                                  │
│ 1. CorsFilter         → Valida origen (CORS)    │
│ 2. CsrfFilter         → Proteccion CSRF         │
│ 3. JwtFilter (custom) → Valida token JWT        │
│ 4. AuthorizationFilter → Verifica permisos       │
│                                                  │
│ Si TODOS pasan → llega al Controller             │
│ Si alguno falla → responde 401 o 403             │
└─────────────────────────────────────────────────┘
    │
    ▼
Controller (solo si paso todos los filtros)
```

## Configuracion completa de Security

```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity  // Habilita @PreAuthorize en metodos
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // CORS: permitir que el frontend (otro origen) llame al backend
            .cors(cors -> cors.configurationSource(corsConfig()))
            
            // CSRF: desactivar porque usamos JWT (no cookies)
            .csrf(csrf -> csrf.disable())
            
            // Stateless: NO crear sesion HTTP (cada request trae su token)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Reglas de autorizacion
            .authorizeHttpRequests(auth -> auth
                // Endpoints PUBLICOS (no necesitan token)
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                
                // Endpoints por ROL
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/productos/**").hasAnyRole("ADMIN", "GERENTE")
                
                // Todo lo demas: requiere estar autenticado
                .anyRequest().authenticated()
            )
            
            // Agregar nuestro filtro JWT ANTES del filtro de autenticacion
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
        // BCrypt con strength 12:
        // "admin123" → "$2a$12$LQv3c1yqBo9SkvXS7QTJPOoFcEoaKCixHjV9Xz0YDQwtM0GZI5Hy"
        // Cada vez genera un hash DIFERENTE (salt aleatorio)
        // Imposible de revertir (one-way function)
    }

    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    private CorsConfigurationSource corsConfig() {
        var config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:4200"));  // Frontend
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(true);
        var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
```

## JWT Service (generar y validar tokens)

```java
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;  // Clave secreta (NUNCA exponer)

    @Value("${jwt.expiration}")
    private long expiration;  // Milisegundos (86400000 = 24 horas)

    // GENERAR token
    public String generate(UserDetails user) {
        return Jwts.builder()
            .subject(user.getUsername())           // Quien es
            .claim("roles", user.getAuthorities()  // Que puede hacer
                .stream().map(Object::toString).toList())
            .issuedAt(new Date())                  // Cuando se creo
            .expiration(new Date(System.currentTimeMillis() + expiration))  // Cuando expira
            .signWith(getKey())                    // Firmar con clave secreta
            .compact();                            // Generar string
        // Resultado: "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIlJPTEVfQURNSU4iXX0.firma"
    }

    // EXTRAER username del token
    public String extractUsername(String token) {
        return Jwts.parser()
            .verifyWith(getKey())       // Verificar firma
            .build()
            .parseSignedClaims(token)   // Parsear
            .getPayload()
            .getSubject();              // Extraer "sub" (username)
    }

    // VALIDAR token
    public boolean isValid(String token, UserDetails user) {
        String username = extractUsername(token);
        Date expiration = extractExpiration(token);
        return username.equals(user.getUsername())  // Username coincide
            && !expiration.before(new Date());      // No ha expirado
    }

    private SecretKey getKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }
}
```

## JWT Filter (validar token en cada request)

```java
@Component
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter: se ejecuta UNA vez por request

    private final JwtService jwt;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        // 1. Buscar header "Authorization"
        String header = request.getHeader("Authorization");

        // 2. Si no hay header o no empieza con "Bearer " → pasar al siguiente filtro
        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        // 3. Extraer token (quitar "Bearer ")
        String token = header.substring(7);

        // 4. Extraer username del token
        String username = jwt.extractUsername(token);

        // 5. Si hay username Y no esta ya autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 6. Cargar usuario de la BD
            UserDetails user = userDetailsService.loadUserByUsername(username);

            // 7. Validar token (firma + expiracion + username)
            if (jwt.isValid(token, user)) {
                
                // 8. Crear objeto de autenticacion
                var auth = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities());
                
                // 9. Poner en el SecurityContext
                // Esto le dice a Spring: "este request es de un usuario autenticado"
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // 10. Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }
}
```

## Auth Controller (login y registro)

```java
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authManager;
    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    // DTOs como Records (inmutables, concisos)
    record LoginRequest(String username, String password) {}
    record RegisterRequest(String username, String password, String email, String nombre) {}
    record AuthResponse(String token, String username, String rol) {}

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        // 1. Spring Security valida credenciales
        //    (carga usuario de BD, compara BCrypt hash)
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(req.username(), req.password())
        );
        // Si llega aqui: credenciales CORRECTAS (si no, lanza excepcion → 401)

        // 2. Cargar usuario
        var user = repo.findByUsername(req.username()).orElseThrow();

        // 3. Generar JWT
        String token = jwt.generate(user);

        // 4. Responder
        return ResponseEntity.ok(new AuthResponse(
            token, user.getUsername(), user.getAuthorities().iterator().next().getAuthority()
        ));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        // 1. Verificar que no exista
        if (repo.existsByUsername(req.username())) {
            return ResponseEntity.badRequest().build();
        }

        // 2. Crear usuario con password HASHEADO
        var user = new Usuario();
        user.setUsername(req.username());
        user.setPassword(encoder.encode(req.password()));  // BCrypt hash
        user.setEmail(req.email());
        user.setNombre(req.nombre());
        user.setRolId(3);  // VENDEDOR por defecto
        repo.save(user);

        // 3. Generar token y responder
        String token = jwt.generate(user);
        return ResponseEntity.ok(new AuthResponse(token, user.getUsername(), "ROLE_VENDEDOR"));
    }
}
```

## Proteger endpoints por rol

```java
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    // Cualquier usuario autenticado puede ver productos
    @GetMapping
    public List<Producto> listar() { ... }

    // Solo ADMIN y GERENTE pueden crear
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public Producto crear(@RequestBody Producto p) { ... }

    // Solo ADMIN puede eliminar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void eliminar(@PathVariable Long id) { ... }

    // El usuario puede ver su propio perfil O un admin puede ver cualquiera
    @GetMapping("/perfil/{id}")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public Usuario verPerfil(@PathVariable Long id) { ... }
}
```

## Flujo completo paso a paso

```
1. REGISTRO:
   POST /api/auth/register {"username":"carlos","password":"carlos123"}
   → Backend: encoder.encode("carlos123") → "$2a$12$hash..."
   → INSERT INTO usuarios (username, password) VALUES ('carlos', '$2a$12$hash...')
   → jwt.generate(user) → "eyJ..."
   → Responde: {"token":"eyJ...","username":"carlos","rol":"ROLE_VENDEDOR"}

2. LOGIN:
   POST /api/auth/login {"username":"carlos","password":"carlos123"}
   → Backend: carga usuario de BD → compara BCrypt("carlos123", "$2a$12$hash...")
   → Si coincide: jwt.generate(user) → "eyJ..."
   → Responde: {"token":"eyJ...","username":"carlos","rol":"ROLE_VENDEDOR"}

3. PETICION PROTEGIDA:
   GET /api/productos
   Header: Authorization: Bearer eyJ...
   → JwtFilter: extrae token → valida firma → extrae username "carlos"
   → Carga usuario de BD → verifica que token no expiro
   → SecurityContext.setAuthentication(carlos)
   → Controller se ejecuta normalmente
   → Responde: [{id:1, nombre:"Laptop"}, ...]

4. SIN TOKEN:
   GET /api/productos (sin header Authorization)
   → JwtFilter: no hay header → pasa al siguiente filtro
   → AuthorizationFilter: /api/productos requiere authenticated → FALLA
   → Responde: 401 Unauthorized

5. ROL INSUFICIENTE:
   DELETE /api/productos/1
   Header: Authorization: Bearer eyJ... (token de VENDEDOR)
   → JwtFilter: token valido, usuario autenticado
   → @PreAuthorize("hasRole('ADMIN')") → usuario es VENDEDOR → FALLA
   → Responde: 403 Forbidden
```
