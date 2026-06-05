# Módulo 4: Frontend Angular - Explicación Completa

## 4.1 Concepto Teórico: SPA (Single Page Application)

### ¿Qué es una SPA?
- Una aplicación web que carga UNA sola página HTML.
- La navegación entre "páginas" NO recarga el navegador.
- JavaScript (Angular) modifica el DOM dinámicamente.
- Solo se comunica con el servidor para obtener DATOS (JSON).

### Comparación:
```
Aplicación Tradicional (MPA):
  Clic en "Productos" → Navegador pide nueva página al servidor → Recarga completa

SPA (Angular):
  Clic en "Productos" → Angular cambia el componente visible → NO recarga
  Solo pide datos JSON al backend si necesita información nueva
```

### ¿Por qué SPA?
- Experiencia de usuario más fluida (sin parpadeos)
- Menor carga en el servidor (solo envía JSON, no HTML completo)
- Funciona offline parcialmente (la app ya está cargada)

### Alternativas:
- React (librería, más libertad pero menos estructura)
- Vue (más simple, curva de aprendizaje menor)
- Next.js/Nuxt (SSR - Server Side Rendering, mejor SEO)

---

## 4.2 Estructura del Proyecto Angular

```
src/app/
├── app.component.ts       → Componente raíz (solo tiene <router-outlet>)
├── app.config.ts          → Configuración global (providers)
├── app.routes.ts          → Definición de rutas (URLs → Componentes)
│
├── core/                  → Código compartido por TODA la app
│   ├── guards/            → Protección de rutas
│   │   └── auth.guard.ts  → "¿Está logueado? Si no → /login"
│   ├── interceptors/      → Modifican TODAS las peticiones HTTP
│   │   └── auth.interceptor.ts → Agrega token JWT a cada request
│   ├── models/            → Interfaces TypeScript (contratos de datos)
│   │   ├── user.model.ts
│   │   └── product.model.ts
│   └── services/          → Comunicación con el backend
│       ├── auth.service.ts    → Login, register, logout
│       ├── user.service.ts    → CRUD usuarios
│       ├── product.service.ts → CRUD productos
│       └── mail.service.ts    → Recuperación de contraseña
│
└── features/              → Módulos de la aplicación (páginas)
    ├── auth/
    │   ├── login/
    │   ├── register/
    │   ├── forgot-password/
    │   └── reset-password/
    ├── dashboard/
    ├── users/
    └── products/
```

## 4.3 Archivo: `auth.interceptor.ts` (Línea por Línea)

```typescript
/**
 * ¿Qué es un Interceptor HTTP?
 * - Es un "middleware" que se ejecuta en CADA petición HTTP que hace Angular.
 * - Puede MODIFICAR la petición antes de enviarla.
 * - Puede MODIFICAR la respuesta antes de entregarla al componente.
 * 
 * ¿Para qué lo usamos?
 * - Agregar automáticamente el token JWT a TODAS las peticiones.
 * - Sin esto, tendrías que agregar el header manualmente en cada servicio.
 * 
 * Flujo:
 * Componente → HttpClient.get() → INTERCEPTOR agrega token → Servidor
 * Servidor → Respuesta → INTERCEPTOR verifica errores → Componente
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  // 1. Obtener el servicio de autenticación
  const authService = inject(AuthService);
  
  // 2. Obtener el token almacenado
  const token = authService.getToken();

  // 3. Si hay token, CLONAR la petición y agregar el header
  // (las peticiones HTTP son inmutables, hay que clonarlas para modificarlas)
  if (token) {
    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
        // El servidor espera: "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
      }
    });
  }

  // 4. Enviar la petición y manejar errores
  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      // Si el servidor responde 401 (no autorizado) → el token expiró
      if (error.status === 401) {
        authService.logout();  // Limpiar token y redirigir a login
      }
      return throwError(() => error);  // Propagar el error al componente
    })
  );
};
```

## 4.4 Archivo: `auth.service.ts` (Línea por Línea)

```typescript
/**
 * Servicio de autenticación.
 * Centraliza toda la lógica de login/register/logout.
 * 
 * providedIn: 'root' = Singleton (una sola instancia para toda la app).
 * Todos los componentes que inyecten AuthService comparten la misma instancia.
 */
@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly API_URL = environment.apiAuthUrl + '/auth';
  
  // BehaviorSubject: Observable que SIEMPRE tiene un valor actual.
  // Cualquier componente puede suscribirse para saber si hay usuario logueado.
  private currentUserSubject = new BehaviorSubject<AuthResponse | null>(this.getStoredUser());
  
  // Observable público (solo lectura) para que los componentes se suscriban
  public currentUser$ = this.currentUserSubject.asObservable();

  login(credentials: LoginRequest): Observable<ApiResponse<AuthResponse>> {
    // HttpClient.post() NO ejecuta la petición inmediatamente.
    // Devuelve un Observable (stream de datos).
    // La petición se ejecuta cuando alguien se SUSCRIBE (.subscribe()).
    return this.http.post<ApiResponse<AuthResponse>>(`${this.API_URL}/login`, credentials)
      .pipe(
        // tap() = "efecto secundario" - ejecuta código sin modificar el stream
        tap(response => {
          if (response.success) {
            this.storeUser(response.data);           // Guardar en localStorage
            this.currentUserSubject.next(response.data); // Notificar a suscriptores
          }
        })
      );
  }

  logout(): void {
    localStorage.removeItem('currentUser');     // Borrar token del navegador
    this.currentUserSubject.next(null);         // Notificar: "ya no hay usuario"
    this.router.navigate(['/login']);           // Redirigir a login
  }

  getToken(): string | null {
    // Leer token de localStorage (persiste entre recargas de página)
    const user = this.getStoredUser();
    return user?.accessToken || null;
  }

  isAuthenticated(): boolean {
    return !!this.getToken();  // !! convierte a boolean (null → false, "token" → true)
  }
}
```

## 4.5 Archivo: `auth.guard.ts` (Protección de Rutas)

```typescript
/**
 * ¿Qué es un Guard?
 * - Es un "guardia" que decide si un usuario puede acceder a una ruta.
 * - Se ejecuta ANTES de cargar el componente de la ruta.
 * - Si devuelve true → se carga la página.
 * - Si devuelve false → se redirige (generalmente a /login).
 * 
 * ¿Cuándo se usa?
 * - Rutas que requieren estar logueado (dashboard, productos, usuarios)
 * - Rutas que requieren un rol específico (admin panel)
 */
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (authService.isAuthenticated()) {
    return true;   // ✓ Tiene token → puede acceder
  }

  router.navigate(['/login']);  // ✗ No tiene token → redirigir a login
  return false;
};
```

## 4.6 Archivo: `app.routes.ts` (Rutas con Lazy Loading)

```typescript
/**
 * Lazy Loading: Los componentes se cargan SOLO cuando el usuario navega a esa ruta.
 * Sin lazy loading: TODA la app se descarga al inicio (lento).
 * Con lazy loading: Solo se descarga login al inicio, el resto bajo demanda.
 * 
 * loadComponent: () => import('./path').then(m => m.Component)
 * Esto crea un "chunk" separado de JavaScript que se descarga solo cuando se necesita.
 */
export const routes: Routes = [
  {
    path: 'login',
    // Lazy load: este componente se descarga SOLO cuando el usuario va a /login
    loadComponent: () => import('./features/auth/login/login.component')
      .then(m => m.LoginComponent)
  },
  {
    path: '',
    canActivate: [authGuard],  // TODAS las rutas hijas requieren autenticación
    children: [
      { path: 'dashboard', loadComponent: () => import('./features/dashboard/dashboard.component').then(m => m.DashboardComponent) },
      { path: 'users', loadComponent: () => import('./features/users/users.component').then(m => m.UsersComponent) },
      { path: 'products', loadComponent: () => import('./features/products/products.component').then(m => m.ProductsComponent) },
      { path: '', redirectTo: 'dashboard', pathMatch: 'full' }
    ]
  },
  { path: '**', redirectTo: 'login' }  // Cualquier ruta no definida → login
];
```

---

## 4.7 Flujo Completo: Login desde el Frontend

```
1. Usuario abre http://localhost → Nginx sirve index.html
2. Angular se carga en el navegador
3. Router ve la URL "/" → authGuard verifica token
4. No hay token → redirige a /login
5. LoginComponent se carga (lazy loading)
6. Usuario escribe username y password
7. Clic en "Iniciar Sesión"
8. loginForm.value = {username: "admin", password: "admin123"}
9. authService.login({username: "admin", password: "admin123"}).subscribe()
10. HttpClient hace POST a http://localhost/api/auth/login
11. Interceptor: no hay token aún, no agrega header
12. Nginx recibe /api/auth/login → proxy a backend-auth:8080
13. Spring Security: /api/auth/** es permitAll → deja pasar
14. AuthController.login() se ejecuta
15. AuthService.login() → authenticationManager.authenticate()
16. Spring carga User de MySQL → compara BCrypt hash
17. Si coincide → JwtService.generateToken() → genera JWT
18. Responde: {success: true, data: {accessToken: "eyJ...", roles: ["ADMIN"]}}
19. Angular recibe respuesta
20. tap() en el pipe: guarda en localStorage, actualiza BehaviorSubject
21. Componente: this.router.navigate(['/dashboard'])
22. Router: /dashboard → authGuard → hay token → permite
23. DashboardComponent se carga
24. Muestra "Bienvenido, admin"
```
