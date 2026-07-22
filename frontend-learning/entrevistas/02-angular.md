# Entrevista Frontend - Angular

## Nivel Junior

### 1. ¿Qué es un Componente en Angular?
**Respuesta:**
Unidad básica de UI: clase TypeScript + template HTML + estilos CSS.
```typescript
@Component({
  selector: 'app-producto',
  standalone: true,
  template: `<h2>{{ nombre }}</h2><p>{{ precio | currency }}</p>`,
  styles: [`h2 { color: blue; }`]
})
export class ProductoComponent {
  @Input() nombre = '';
  @Input() precio = 0;
}
```
Uso: `<app-producto [nombre]="'Laptop'" [precio]="18999" />`

---

### 2. ¿Qué es un Service y por qué usarlo?
**Respuesta:**
Clase que contiene lógica NO visual: HTTP calls, estado, cálculos.
```typescript
@Injectable({ providedIn: 'root' })  // Singleton global
export class ProductoService {
  private http = inject(HttpClient);
  
  getAll(): Observable<Producto[]> {
    return this.http.get<Producto[]>('/api/productos');
  }
}
```
Separar porque: reutilizable, testeable, independiente de la UI.

---

### 3. ¿Qué son los Signals en Angular?
**Respuesta:**
Estado reactivo que notifica automáticamente cuando cambia:
```typescript
// Signal: estado mutable
count = signal(0);

// Computed: derivado (se recalcula automáticamente)
double = computed(() => this.count() * 2);

// Modificar:
this.count.set(5);
this.count.update(c => c + 1);

// En template: {{ count() }} → se actualiza solo
```
Reemplazan BehaviorSubject para la mayoría de casos.

---

### 4. ¿Cómo comunicas componentes padre↔hijo?
**Respuesta:**
```typescript
// Padre → Hijo: @Input
@Input() producto: Producto;    // El padre le pasa datos
// <app-hijo [producto]="miProducto" />

// Hijo → Padre: @Output + EventEmitter
@Output() eliminar = new EventEmitter<number>();
this.eliminar.emit(productoId);
// <app-hijo (eliminar)="onEliminar($event)" />

// Con Signals (Angular 17.1+):
nombre = input<string>();       // Input como signal
nombre = input.required<string>(); // Obligatorio
seleccion = output<Producto>(); // Output
```

---

### 5. ¿Qué es Lazy Loading y cómo se implementa?
**Respuesta:**
Cargar código solo cuando el usuario navega a esa ruta (no todo al inicio):
```typescript
export const routes: Routes = [
  { path: 'productos', loadComponent: () => 
      import('./features/productos/lista.component').then(m => m.ListaComponent) },
  { path: 'admin', loadChildren: () => 
      import('./features/admin/admin.routes').then(m => m.ADMIN_ROUTES) }
];
```
Resultado: bundle inicial pequeño → carga rápida → cada feature se descarga bajo demanda.

---

## Nivel Mid

### 6. ¿Qué es un Interceptor y para qué sirve?
**Respuesta:**
Middleware que intercepta TODAS las peticiones HTTP:
```typescript
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const token = inject(AuthService).getToken();
  
  // Agregar token JWT a todas las requests:
  if (token) {
    req = req.clone({ setHeaders: { Authorization: `Bearer ${token}` } });
  }
  
  return next(req).pipe(
    catchError(err => {
      if (err.status === 401) inject(Router).navigate(['/login']);
      return throwError(() => err);
    })
  );
};
```
Usos: auth, loading spinner, error handling global, logging.

---

### 7. ¿Cuál es la diferencia entre Template-driven y Reactive Forms?
**Respuesta:**
| | Template-driven | Reactive |
|---|---|---|
| Lógica en | HTML (ngModel) | TypeScript (FormGroup) |
| Validación | Directivas en template | Funciones en clase |
| Testing | Difícil (requiere DOM) | Fácil (solo clase) |
| Dinamismo | Limitado | Total (agregar/quitar campos dinámicamente) |
| Uso ideal | Forms simples (login) | Forms complejos (wizard, arrays) |

```typescript
// Reactive (recomendado):
form = new FormGroup({
  nombre: new FormControl('', [Validators.required, Validators.minLength(3)]),
  email: new FormControl('', [Validators.required, Validators.email]),
  items: new FormArray([])  // Dinámico
});
```

---

### 8. Explica los operadores RxJS más importantes
**Respuesta:**
```typescript
// switchMap: cancela anterior al llegar uno nuevo (búsquedas)
searchInput.pipe(
  debounceTime(300),
  switchMap(term => this.http.get(`/api/buscar?q=${term}`))
);

// combineLatest: espera a todos y reacciona a cualquier cambio
combineLatest([filtroCategoria$, filtroPrecio$, orden$]).pipe(
  switchMap(([cat, precio, orden]) => this.cargar(cat, precio, orden))
);

// tap: efecto secundario sin modificar el flujo
this.http.get('/api/x').pipe(
  tap(data => console.log('Recibido:', data)),
  catchError(err => { this.error.set(err.message); return EMPTY; })
);
```

---

### 9. ¿Cómo optimizas Change Detection?
**Respuesta:**
```typescript
// 1. OnPush: solo revisa si cambian inputs o hay eventos
@Component({ changeDetection: ChangeDetectionStrategy.OnPush })

// 2. Signals: change detection granular automáticamente
count = signal(0);  // Solo actualiza donde se usa count()

// 3. trackBy en @for:
@for (item of items; track item.id) { ... }

// 4. @defer para cargar componentes pesados cuando son visibles:
@defer (on viewport) {
  <app-grafica-pesada />
} @placeholder {
  <p>Cargando gráfica...</p>
}
```

---

### 10. ¿Cómo manejas estado global en Angular?
**Respuesta:**
De simple a complejo:
1. **Services + Signals** (80% de apps): estado en services con signals
2. **NgRx Signal Store**: para features con estado complejo
3. **NgRx Store (Redux)**: para apps muy grandes con side effects complejos

```typescript
// Opción 1: Service + Signals (recomendado):
@Injectable({ providedIn: 'root' })
export class CarritoService {
  private _items = signal<Producto[]>([]);
  
  items = this._items.asReadonly();
  total = computed(() => this._items().reduce((sum, p) => sum + p.precio, 0));
  cantidad = computed(() => this._items().length);
  
  agregar(p: Producto) { this._items.update(items => [...items, p]); }
  remover(id: number) { this._items.update(items => items.filter(i => i.id !== id)); }
  vaciar() { this._items.set([]); }
}
```

---

## Nivel Senior

### 11. ¿Cómo diseñarías la arquitectura de una app Angular enterprise?
**Respuesta:**
```
src/app/
├── core/                  ← Singletons (1 vez en la app)
│   ├── interceptors/      auth, error, loading
│   ├── guards/            auth.guard, role.guard
│   ├── services/          auth, notification (globales)
│   └── core.module.ts
├── shared/                ← Reutilizables (importados donde se necesiten)
│   ├── components/        button, card, modal, table genérica
│   ├── directives/        highlight, permission
│   ├── pipes/             currency-mx, relative-time
│   └── models/            interfaces compartidas
├── features/              ← Módulos de negocio (lazy loaded)
│   ├── productos/         su propio routing, componentes, services
│   ├── ventas/
│   ├── clientes/
│   └── admin/
└── app.routes.ts          ← Lazy loading de features
```

Reglas:
- Features NO se importan entre sí (desacoplados)
- Shared NO importa de features
- Core se importa solo en app.config

---

### 12. ¿Cómo implementarías un sistema de permisos granular?
**Respuesta:**
```typescript
// Directiva que muestra/oculta según permiso:
@Directive({ selector: '[appHasPermission]', standalone: true })
export class HasPermissionDirective {
  private auth = inject(AuthService);
  
  @Input() set appHasPermission(permiso: string) {
    if (this.auth.tienePermiso(permiso)) {
      this.viewContainer.createEmbeddedView(this.templateRef);
    } else {
      this.viewContainer.clear();
    }
  }
}

// Uso en template:
<button *appHasPermission="'productos.eliminar'">Eliminar</button>

// Guard para rutas:
export const adminGuard: CanActivateFn = () => {
  return inject(AuthService).tieneRol('ADMIN') || inject(Router).createUrlTree(['/forbidden']);
};
```
