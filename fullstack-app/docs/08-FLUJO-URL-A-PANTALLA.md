# Flujo Completo: Desde que el Usuario Escribe la URL hasta que Ve la Página

## Escenario: Usuario escribe http://localhost y quiere ver productos

```
PASO 1: DNS Resolution (Resolución de nombre)
═══════════════════════════════════════════════
Tu navegador: "¿Cuál es la IP de localhost?"
Sistema operativo: "127.0.0.1" (tu propia máquina)

PASO 2: TCP Connection (Conexión de red)
═══════════════════════════════════════════════
Navegador establece conexión TCP al puerto 80 de 127.0.0.1
(Si fuera HTTPS, aquí ocurre el TLS handshake)

PASO 3: HTTP Request (Petición)
═══════════════════════════════════════════════
Navegador envía:
  GET / HTTP/1.1
  Host: localhost
  Accept: text/html

PASO 4: Nginx Recibe la Petición
═══════════════════════════════════════════════
Nginx ve: GET /
Regla: location / { try_files $uri $uri/ /index.html; }
Resultado: Sirve /usr/share/nginx/html/index.html

PASO 5: Navegador Recibe index.html
═══════════════════════════════════════════════
<!doctype html>
<html>
<head>
  <script src="main.abc123.js"></script>  ← Angular compilado
  <link href="styles.def456.css">         ← Estilos
</head>
<body>
  <app-root></app-root>  ← Aquí Angular inyecta la app
</body>
</html>

PASO 6: Navegador Descarga JavaScript
═══════════════════════════════════════════════
GET /main.abc123.js → Nginx sirve el archivo estático
Este archivo contiene TODO Angular compilado y minificado

PASO 7: Angular Se Inicializa (Bootstrap)
═══════════════════════════════════════════════
1. main.ts ejecuta bootstrapApplication(AppComponent, appConfig)
2. AppComponent se renderiza: <router-outlet></router-outlet>
3. Router lee la URL actual: "/"
4. Busca en app.routes.ts: path '' → canActivate: [authGuard]

PASO 8: AuthGuard Se Ejecuta
═══════════════════════════════════════════════
authGuard():
  1. authService.isAuthenticated()
  2. Busca en localStorage: "currentUser"
  3. ¿Existe y tiene accessToken?
     - NO → router.navigate(['/login']) → Ir a paso 9A
     - SÍ → return true → Ir a paso 9B

PASO 9A: Mostrar Login (usuario no autenticado)
═══════════════════════════════════════════════
1. Router carga LoginComponent (lazy loading → descarga chunk separado)
2. Angular renderiza el template HTML del componente
3. Usuario ve: formulario con campos username y password
4. Usuario escribe credenciales y da clic en "Iniciar Sesión"
5. → Ir a PASO 10

PASO 9B: Mostrar Dashboard (usuario ya autenticado)
═══════════════════════════════════════════════
1. Router ve: path '' → redirectTo 'dashboard'
2. Carga DashboardComponent
3. Usuario ve el dashboard
4. Si navega a /products → Ir a PASO 12

PASO 10: Login - Petición al Backend
═══════════════════════════════════════════════
1. onSubmit() se ejecuta
2. authService.login({username: "admin", password: "admin123"})
3. HttpClient crea petición:
   POST /api/auth/login
   Content-Type: application/json
   Body: {"username":"admin","password":"admin123"}

4. authInterceptor: no hay token aún → no modifica la petición

5. Navegador envía a localhost:80
6. Nginx ve /api/ → proxy_pass http://backend-auth:8080/api/
7. Nginx reenvía la petición al contenedor backend-auth

PASO 11: Backend Procesa Login
═══════════════════════════════════════════════
1. Tomcat (dentro de Spring Boot) recibe la petición en puerto 8080
2. Spring Security filter chain:
   a. RateLimitFilter: ¿IP tiene tokens disponibles? SÍ → continuar
   b. JwtAuthenticationFilter: ¿Tiene header Authorization? NO → continuar
   c. AuthorizationFilter: /api/auth/** es permitAll → continuar

3. Spring MVC: POST /api/auth/login → AuthController.login()

4. AuthController.login(AuthRequest{username:"admin", password:"admin123"})

5. AuthService.login():
   a. authenticationManager.authenticate(UsernamePasswordAuthenticationToken)
   b. Spring llama a UserService.loadUserByUsername("admin")
   c. UserRepository.findByUsername("admin") → SELECT * FROM users WHERE username='admin'
   d. MySQL devuelve el registro del usuario (con password hash)
   e. Spring compara: BCrypt.matches("admin123", "$2a$12$LQv3c1y...")
   f. ¿Coincide? SÍ → autenticación exitosa

6. JwtService.generateToken(user):
   a. Crea payload: {sub:"admin", iat:1705312000, exp:1705398400}
   b. Firma con HMAC-SHA256 usando la clave secreta
   c. Resultado: "eyJhbGciOiJIUzI1NiJ9.eyJzdWI..."

7. Construye AuthResponse:
   {accessToken: "eyJ...", refreshToken: "eyJ...", username: "admin", roles: ["ADMIN","USER"]}

8. AuditService.log("LOGIN", "User", null, "admin", "Inicio de sesión", "192.168.1.1")
   → INSERT INTO audit_logs en SQL Server

9. ResponseEntity.ok(ApiResponse.ok("Login exitoso", authResponse))
   → HTTP 200 con JSON

PASO 12: Frontend Recibe Respuesta
═══════════════════════════════════════════════
1. HttpClient recibe el JSON
2. pipe(tap(...)):
   a. localStorage.setItem("currentUser", JSON.stringify(response.data))
   b. currentUserSubject.next(response.data)
3. subscribe() en LoginComponent:
   a. this.router.navigate(['/dashboard'])

4. Router: /dashboard → authGuard → localStorage tiene token → true
5. DashboardComponent se carga y renderiza

PASO 13: Usuario Navega a Productos
═══════════════════════════════════════════════
1. Clic en "Productos" → router.navigate(['/products'])
2. ProductsComponent se carga (lazy loading)
3. ngOnInit() → this.loadProducts()
4. productService.getAll() → HttpClient.get('/api/products')

5. authInterceptor INTERCEPTA:
   a. authService.getToken() → lee de localStorage → "eyJ..."
   b. req.clone({ setHeaders: { Authorization: "Bearer eyJ..." } })

6. Petición sale:
   GET /api/products
   Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...

7. Nginx → proxy a backend-auth:8080

8. Spring Security filter chain:
   a. RateLimitFilter: OK
   b. JwtAuthenticationFilter:
      - Extrae token del header
      - jwtService.extractUsername(token) → "admin"
      - userDetailsService.loadUserByUsername("admin") → carga de MySQL
      - jwtService.isTokenValid(token, user) → no expirado, firma válida
      - SecurityContextHolder.setAuthentication(authToken) → usuario autenticado
   c. AuthorizationFilter: /api/products requiere authenticated → OK (está autenticado)

9. ProductController.getAllProducts()
10. ProductService.getAllProducts():
    a. @Cacheable("products") → ¿está en Redis? 
       - SÍ → devolver de Redis (2ms)
       - NO → consultar Oracle:
         SELECT * FROM products WHERE is_active=1
         Oracle devuelve 15 productos
         Guardar en Redis con TTL 5 minutos
    b. Mapear Product → ProductDto (quitar campos internos)

11. Respuesta:
    HTTP 200
    {success: true, data: [{id:1, name:"Laptop HP", price:18999.99, stock:25}, ...]}

PASO 14: Frontend Muestra Productos
═══════════════════════════════════════════════
1. subscribe() recibe la respuesta
2. this.products = response.data (array de 15 productos)
3. Angular detecta cambio en la variable → re-renderiza el template
4. La tabla mat-table muestra los 15 productos
5. Usuario VE la lista de productos en pantalla

TIEMPO TOTAL (aproximado):
  - DNS + TCP: ~1ms (localhost)
  - Nginx → index.html: ~2ms
  - Descargar JS/CSS: ~50ms (primera vez), 0ms (cacheado)
  - Angular bootstrap: ~100ms
  - Login request: ~200ms (BCrypt es lento intencionalmente)
  - Products request: ~50ms (con Redis), ~200ms (sin Redis, Oracle)
  - Renderizado Angular: ~20ms
  TOTAL: ~400ms primera carga, ~70ms navegación subsecuente
```

---

## Diagrama de Secuencia Simplificado

```
Navegador          Nginx           Backend-Auth        MySQL      Oracle     Redis
    │                │                  │                │           │          │
    │── GET / ──────>│                  │                │           │          │
    │<── index.html ─│                  │                │           │          │
    │                │                  │                │           │          │
    │── GET main.js─>│                  │                │           │          │
    │<── main.js ────│                  │                │           │          │
    │                │                  │                │           │          │
    │ [Angular carga, muestra login]    │                │           │          │
    │                │                  │                │           │          │
    │── POST /api/auth/login ──────────>│                │           │          │
    │                │                  │── SELECT user─>│           │          │
    │                │                  │<── user data ──│           │          │
    │                │                  │── BCrypt check │           │          │
    │                │                  │── Generate JWT │           │          │
    │<── {accessToken, roles} ──────────│                │           │          │
    │                │                  │                │           │          │
    │ [Guarda token, navega a /products]│                │           │          │
    │                │                  │                │           │          │
    │── GET /api/products (+ Bearer) ──>│                │           │          │
    │                │                  │── GET cache ──────────────────────────>│
    │                │                  │<── cache miss ────────────────────────│
    │                │                  │── SELECT products ────────>│          │
    │                │                  │<── 15 products ───────────│          │
    │                │                  │── SET cache ──────────────────────────>│
    │<── {products: [...]} ─────────────│                │           │          │
    │                │                  │                │           │          │
    │ [Angular renderiza tabla de productos]             │           │          │
```
