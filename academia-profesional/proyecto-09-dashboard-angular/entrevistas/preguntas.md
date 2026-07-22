# Preguntas de Entrevista - Tema: Angular

## Nivel Junior

### 1. ¿Qué es un Componente en Angular?
**Respuesta:**
Es la unidad básica de UI. Combina:
- **Template** (HTML): lo que se ve
- **Clase** (TypeScript): la lógica
- **Estilos** (CSS): cómo se ve

Cada componente es reutilizable e independiente. Una app Angular es un árbol de componentes.

---

### 2. ¿Qué es un Service y por qué usarlo?
**Respuesta:**
Un servicio es una clase con lógica que NO es de UI (HTTP calls, lógica de negocio, estado compartido). Se separa del componente porque:
- **Reutilización**: múltiples componentes usan el mismo servicio
- **Testing**: más fácil mockear un servicio que un componente
- **Separation of Concerns**: el componente solo maneja UI

```typescript
@Injectable({ providedIn: 'root' })  // Singleton global
export class AuthService {
  login(credentials: LoginRequest): Observable<Token> { ... }
}
```

---

### 3. ¿Cuál es la diferencia entre Observables y Promises?
**Respuesta:**
| | Promise | Observable |
|---|---------|------------|
| Valores | 1 solo | Múltiples en el tiempo |
| Lazy | No (ejecuta al crear) | Sí (ejecuta al suscribirse) |
| Cancelable | No | Sí (unsubscribe) |
| Operadores | .then/.catch | pipe(map, filter, switchMap...) |

Angular usa Observables (RxJS) para HTTP, forms, router, WebSocket.

---

### 4. ¿Qué es un Guard y para qué sirve?
**Respuesta:**
Un Guard protege rutas. Se ejecuta ANTES de navegar:
```typescript
export const authGuard: CanActivateFn = () => {
  const authService = inject(AuthService);
  if (authService.isLoggedIn()) return true;
  return inject(Router).createUrlTree(['/login']);  // Redirige
};

// En rutas:
{ path: 'dashboard', component: DashboardComponent, canActivate: [authGuard] }
```

---

### 5. ¿Qué son los Signals en Angular (17+)?
**Respuesta:**
Signals son una forma reactiva de manejar estado sin RxJS:
```typescript
count = signal(0);                    // Crear
doubleCount = computed(() => this.count() * 2);  // Derivado (se actualiza solo)

increment() { this.count.update(c => c + 1); }  // Modificar
```
Ventajas: más simple que BehaviorSubject, mejor performance (fine-grained reactivity), no necesitan unsubscribe.

---

## Nivel Mid

### 6. ¿Cómo funciona un HTTP Interceptor?
**Respuesta:**
Es un middleware que intercepta TODAS las peticiones HTTP (salientes y entrantes):
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('token');
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  return next(req).pipe(
    catchError(err => {
      if (err.status === 401) { /* redirigir a login */ }
      return throwError(() => err);
    })
  );
};
```
Usos comunes: agregar token JWT, logging, loading spinner, manejo global de errores.

---

### 7. ¿Qué es Lazy Loading y por qué es importante?
**Respuesta:**
Cargar módulos/componentes solo cuando el usuario navega a esa ruta:
```typescript
{ path: 'admin', loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent) }
```
- Sin lazy loading: el browser descarga TODO el app al inicio (bundle grande, carga lenta)
- Con lazy loading: descarga solo lo necesario (carga inicial rápida)

En apps grandes puede reducir el bundle inicial de 2MB a 200KB.

---

### 8. ¿Cuál es la diferencia entre Template-driven y Reactive Forms?
**Respuesta:**
- **Template-driven**: lógica en el HTML con ngModel. Simple para forms pequeños.
- **Reactive Forms**: lógica en TypeScript con FormGroup/FormControl. Mejor para forms complejos.

```typescript
// Reactive (recomendado para apps enterprise):
form = new FormGroup({
  nombre: new FormControl('', [Validators.required, Validators.minLength(3)]),
  email: new FormControl('', [Validators.required, Validators.email])
});
```
Reactive: más testeable, validación dinámica, mejor control.

---

### 9. ¿Cómo compartes datos entre componentes?
**Respuesta:**
- **Padre → Hijo**: `@Input()` property binding
- **Hijo → Padre**: `@Output()` EventEmitter
- **Hermanos**: Service compartido con BehaviorSubject o Signal
- **Cualquiera**: Store/State management (NgRx, signal store)
- **Ruta**: Router params, query params, data resolver

---

### 10. ¿Qué es Change Detection y cómo optimizarla?
**Respuesta:**
Angular revisa el árbol de componentes para detectar cambios y actualizar el DOM.
- Default: revisa TODOS los componentes en cada evento
- OnPush: solo revisa si cambian los @Input o se dispara un evento

```typescript
@Component({
  changeDetection: ChangeDetectionStrategy.OnPush  // Más performante
})
```
Con Signals (Angular 17+): change detection es automáticamente granular (solo actualiza lo que cambió).

---

## Nivel Senior

### 11. ¿Cómo manejas estado global en una app Angular grande?
**Respuesta:**
Opciones (de simple a complejo):
1. **Services con Signals** (Angular 17+): para apps medianas
2. **NgRx Signal Store**: para apps grandes con estado predecible
3. **NgRx Store (Redux)**: para apps enterprise con side effects complejos

Regla: no uses NgRx si services con Signals son suficientes. Over-engineering es peor que under-engineering.

---

### 12. ¿Qué estrategias de performance conoces en Angular?
**Respuesta:**
1. **Lazy loading**: cargar módulos bajo demanda
2. **OnPush + Signals**: reducir change detection innecesario
3. **trackBy** en *ngFor: evitar re-crear DOM elements
4. **Virtual scrolling** (CDK): para listas largas (render solo lo visible)
5. **Preloading strategy**: pre-cargar módulos probables en segundo plano
6. **Bundle size**: tree-shaking, eliminar imports innecesarios
7. **Service Worker**: cache offline con @angular/pwa
8. **SSR** (Angular Universal): render en servidor para SEO y first paint rápido
